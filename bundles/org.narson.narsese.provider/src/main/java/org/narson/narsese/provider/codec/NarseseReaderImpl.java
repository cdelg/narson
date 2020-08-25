package org.narson.narsese.provider.codec;

import static org.narson.tools.PredChecker.checkNotNull;
import static org.narson.tools.PredChecker.checkState;
import java.util.NoSuchElementException;
import org.narson.api.narsese.CompoundConnector;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.CompoundTermBuilder;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.DependentVariableBuilder;
import org.narson.api.narsese.Goal;
import org.narson.api.narsese.GoalBuilder;
import org.narson.api.narsese.ImageConnector;
import org.narson.api.narsese.ImageTerm;
import org.narson.api.narsese.ImageTermBuilder;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.JudgmentBuilder;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseLocation;
import org.narson.api.narsese.NarseseParser;
import org.narson.api.narsese.NarseseParser.Event;
import org.narson.api.narsese.NarseseParsingException;
import org.narson.api.narsese.NarseseReader;
import org.narson.api.narsese.NegationTerm;
import org.narson.api.narsese.OperationBuilder;
import org.narson.api.narsese.OperationTerm;
import org.narson.api.narsese.Query;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.SecondaryCopula;
import org.narson.api.narsese.Sentence;
import org.narson.api.narsese.SetConnector;
import org.narson.api.narsese.SetTerm;
import org.narson.api.narsese.SetTermBuilder;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final public class NarseseReaderImpl implements NarseseReader
{
  private final NarseseParser parser;
  private final NarseseFactory nf;
  private boolean closed = false;

  private Event current;

  public NarseseReaderImpl(NarseseParser parser, NarseseFactory narseseFactory)
  {
    this.parser = parser;
    nf = narseseFactory;
  }

  @Override
  public int read(Sentence[] sentences)
      throws NarseseException, NarseseParsingException, IllegalStateException
  {
    checkNotNull(sentences, "sentences");
    checkState(!closed, "The reader was closed.");

    if (sentences.length != 0)
    {
      Sentence current = read();
      if (current != null)
      {
        int n = 0;
        for (int i = 0; i < sentences.length && current != null; i++)
        {
          n = i;
          sentences[i] = current;
          current = read();
        }
        return n + 1;
      } else
      {
        return -1;
      }
    } else
    {
      return 0;
    }
  }

  @Override
  public Sentence read() throws NarseseException, NarseseParsingException, IllegalStateException
  {
    checkState(!closed, "The reader was closed.");

    if (parser.hasNext() || current != null)
    {
      try
      {
        return handleSentence();
      } catch (final IllegalStateException e)
      {
        throw new NarseseException(e.getMessage(), e);
      } catch (final NoSuchElementException e)
      {
        throw new NarseseException("Unexpected end of stream.", e);
      } catch (final NarseseException e)
      {
        throw e;
      }
    } else
    {
      return null;
    }
  }

  private Sentence handleSentence()
  {
    Sentence result = null;
    switch (pop())
    {
      case START_JUDGMENT:
        result = handleJudgment();
        break;
      case START_GOAL:
        result = handleGoal();
        break;
      case START_QUESTION:
        result = handleQuestion();
        break;
      case START_QUERY:
        result = handleQuery();
        break;
      default:
        bug();
    }
    if (pop() != Event.END)
    {
      bug();
    }
    return result;
  }

  private Query handleQuery()
  {
    return nf.query(handleTerm());
  }

  private Question handleQuestion()
  {
    Tense tense = Tense.NONE;
    switch (peek())
    {
      case VALUE_FUTURE_TENSE:
        tense = Tense.FUTURE;
        pop();
        break;
      case VALUE_PRESENT_TENSE:
        tense = Tense.PRESENT;
        pop();
        break;
      case VALUE_PAST_TENSE:
        tense = Tense.PAST;
        pop();
        break;
      default:
    }
    return nf.question(handleTerm()).tense(tense).build();
  }

  private Goal handleGoal()
  {
    final GoalBuilder builder = nf.goal(handleTerm());

    switch (peek())
    {
      case VALUE_FREQUENCY:
        final double freq = parser.getDouble();
        pop();
        if (pop() != Event.VALUE_CONFIDENCE)
        {
          bug();
        }
        try
        {
          builder.desireValue(freq, parser.getDouble());
        } catch (final IllegalArgumentException e)
        {
          final NarseseLocation location = parser.getLocation();
          throw new NarseseParsingException("Invalid desire value near ligne "
              + location.getLineNumber() + ", column " + location.getColumnNumber() + ". ",
              location);
        }
        break;
      default:
    }
    return builder.build();
  }

  private Judgment handleJudgment()
  {
    Tense tense = Tense.NONE;
    switch (peek())
    {
      case VALUE_FUTURE_TENSE:
        tense = Tense.FUTURE;
        pop();
        break;
      case VALUE_PRESENT_TENSE:
        tense = Tense.PRESENT;
        pop();
        break;
      case VALUE_PAST_TENSE:
        tense = Tense.PAST;
        pop();
        break;
      default:
    }

    final JudgmentBuilder builder = nf.judgment(handleTerm()).tense(tense);

    switch (peek())
    {
      case VALUE_FREQUENCY:
        final double freq = parser.getDouble();
        pop();
        if (pop() != Event.VALUE_CONFIDENCE)
        {
          bug();
        }
        try
        {
          builder.truthValue(freq, parser.getDouble());
        } catch (final IllegalArgumentException e)
        {
          final NarseseLocation location = parser.getLocation();
          throw new NarseseParsingException("Invalid truth value near ligne "
              + location.getLineNumber() + ", column " + location.getColumnNumber() + ". ",
              location);
        }
        break;
      default:
    }
    return builder.build();
  }

  private Term handleTerm()
  {
    switch (pop())
    {
      case VALUE_CONSTANT:
        return nf.constant(parser.getString());
      case VALUE_QUERY_VARIABLE:
        return nf.queryVariable(parser.getString());
      case VALUE_INDEPENDENT_VARIABLE:
        return nf.independentVariable(parser.getString());
      case START_DEPENDENT_VARIABLE:
        return handleDependentVariable(nf.dependentVariable(parser.getString()));
      case START_OPERATION:
        return handleOperation(nf.operation(parser.getString()));
      case START_CONCURRENT_EQUIVALENCE_COPULA:
        return handleCopulaTerm(SecondaryCopula.CONCURRENT_EQUIVALENCE);
      case START_CONCURRENT_IMPLICATION_COPULA:
        return handleCopulaTerm(SecondaryCopula.CONCURRENT_IMPLICATION);
      case START_EQUIVALENCE_COPULA:
        return handleCopulaTerm(Copula.EQUIVALENCE);
      case START_IMPLICATION_COPULA:
        return handleCopulaTerm(Copula.IMPLICATION);
      case START_INHERITANCE_COPULA:
        return handleCopulaTerm(Copula.INHERITANCE);
      case START_INSTANCE_COPULA:
        return handleCopulaTerm(SecondaryCopula.INSTANCE);
      case START_PREDICTIVE_EQUIVALENCE_COPULA:
        return handleCopulaTerm(SecondaryCopula.PREDICTIVE_EQUIVALENCE);
      case START_PREDICTIVE_IMPLICATION_COPULA:
        return handleCopulaTerm(SecondaryCopula.PREDICTIVE_IMPLICATION);
      case START_INSTANCE_PROPERTY_COPULA:
        return handleCopulaTerm(SecondaryCopula.INSTANCE_PROPERTY);
      case START_PROPERTY_COPULA:
        return handleCopulaTerm(SecondaryCopula.PROPERTY);
      case START_RETROSPECTIVE_IMPLICATION_COPULA:
        return handleCopulaTerm(SecondaryCopula.RETROSPECTIVE_IMPLICATION);
      case START_SIMILARITY_COPULA:
        return handleCopulaTerm(Copula.SIMILARITY);
      case START_CONJUNCTION:
        return handleCompoundTerm(nf.compound(CompoundConnector.CONJUNCTION));
      case START_DISJUNCTION:
        return handleCompoundTerm(nf.compound(CompoundConnector.DISJUNCTION));
      case START_SEQUENTIAL_CONJUNCTION:
        return handleCompoundTerm(nf.compound(CompoundConnector.SEQUENTIAL_CONJUNCTION));
      case START_EXTENSIONAL_DIFFERENCE:
        return handleCompoundTerm(nf.compound(CompoundConnector.EXTENSIONAL_DIFFERENCE));
      case START_EXTENSIONAL_IMAGE:
        return handleImageTerm(nf.image(ImageConnector.EXTENSIONAL_IMAGE));
      case START_EXTENSIONAL_INTERSECTION:
        return handleCompoundTerm(nf.compound(CompoundConnector.EXTENSIONAL_INTERSECTION));
      case START_EXTENSIONAL_SET:
        return handleSetTerm(nf.set(SetConnector.EXTENSIONAL_SET));
      case START_INTENSIONAL_DIFFERENCE:
        return handleCompoundTerm(nf.compound(CompoundConnector.INTENSIONAL_DIFFERENCE));
      case START_INTENSIONAL_IMAGE:
        return handleImageTerm(nf.image(ImageConnector.INTENSIONAL_IMAGE));
      case START_INTENSIONAL_INTERSECTION:
        return handleCompoundTerm(nf.compound(CompoundConnector.INTENSIONAL_INTERSECTION));
      case START_INTENSIONAL_SET:
        return handleSetTerm(nf.set(SetConnector.INTENSIONAL_SET));
      case START_NEGATION:
        return handleNegation();
      case START_PARALLEL_CONJUNCTION:
        return handleCompoundTerm(nf.compound(CompoundConnector.PARALLEL_CONJUNCTION));
      case START_PRODUCT:
        return handleCompoundTerm(nf.compound(CompoundConnector.PRODUCT));
      default:
    }
    return bug();
  }

  private CompoundTerm handleCompoundTerm(CompoundTermBuilder builder)
  {
    switch (peek())
    {
      case END:
        pop();
        return builder.build();
      default:
        builder.of(handleTerm());
        return handleCompoundTerm(builder);
    }
  }

  private SetTerm handleSetTerm(SetTermBuilder builder)
  {
    switch (peek())
    {
      case END:
        pop();
        return builder.build();
      default:
        builder.of(handleTerm());
        return handleSetTerm(builder);
    }
  }

  private ImageTerm handleImageTerm(ImageTermBuilder builder)
  {
    switch (peek())
    {
      case VALUE_PLACEHOLDER:
        builder.setPlaceHolderPosition();
        pop();
        return handleImageTerm(builder);
      case END:
        pop();
        return builder.build();
      default:
        builder.of(handleTerm());
        return handleImageTerm(builder);
    }
  }

  private DependentVariable handleDependentVariable(DependentVariableBuilder builder)
  {
    switch (peek())
    {
      case VALUE_DEPENDENT_INDEPENDENT_VARIABLE:
        builder.dependsOn(parser.getString());
        pop();
        return handleDependentVariable(builder);
      case END:
        pop();
        return builder.build();
      default:
    }
    return bug();
  }

  private OperationTerm handleOperation(OperationBuilder builder)
  {
    switch (peek())
    {
      case END:
        pop();
        return builder.build();
      default:
        builder.on(handleTerm());
        return handleOperation(builder);
    }
  }

  private NegationTerm handleNegation()
  {
    final NegationTerm term = nf.negation(handleTerm());
    if (pop() != Event.END)
    {
      bug();
    }
    return term;
  }

  private CopulaTerm handleCopulaTerm(Copula copula)
  {
    final CopulaTerm copulaTerm = nf.copula(handleTerm(), copula, handleTerm());
    if (pop() != Event.END)
    {
      bug();
    }
    return copulaTerm;
  }

  private CopulaTerm handleCopulaTerm(SecondaryCopula copula)
  {
    final CopulaTerm copulaTerm = nf.copula(handleTerm(), copula, handleTerm());
    if (pop() != Event.END)
    {
      bug();
    }
    return copulaTerm;
  }

  private Event peek()
  {
    return current != null ? current : (current = parser.next());
  }

  private Event pop()
  {
    final Event e = current != null ? current : parser.next();
    current = null;
    return e;
  }

  static private <T> T bug()
  {
    throw new IllegalStateException(
        "BUG: Unexpected parsing error. This error should be reported.");
  }

  @Override
  public void close() throws NarseseException
  {
    if (!closed)
    {
      try
      {
        parser.close();
      } catch (final NarseseParsingException pE)
      {
        throw new NarseseParsingException(pE.getMessage(), pE.getCause(), pE.getLocation());
      } catch (final NarseseException e)
      {
        throw new NarseseException(e.getMessage(), e.getCause());
      } catch (final Exception e)
      {
        throw new NarseseException(e.getMessage(), e);
      } finally
      {
        closed = true;
      }
    }
  }
}