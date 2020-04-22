package org.narson.narsese.provider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.List;
import java.util.function.Supplier;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Constant;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.Goal;
import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.NarseseGenerationException;
import org.narson.api.narsese.NarseseGenerator;
import org.narson.api.narsese.NarseseValue;
import org.narson.api.narsese.Operation;
import org.narson.api.narsese.Query;
import org.narson.api.narsese.QueryVariable;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.Relation;
import org.narson.api.narsese.Term;

final class NarseseGeneratorImpl implements NarseseGenerator, NarseseChars, NarseseOperations
{
  private static final Charset UTF8SET = Charset.forName("UTF-8");

  private final BufferedWriter writer;
  private final int prefixThreshold;
  private boolean closed;

  /* Context variables to control sentence */
  private char currentSentenceEndChar;
  private boolean currentSentenceAllowTense;
  private boolean currentSentenceAllowTruth;
  private boolean firstSentence = true;
  private boolean truthSet;
  private boolean tenseSet;

  private final ArrayDeque<Context> contextQueue = new ArrayDeque<>();

  private enum State
  {
    ROOT(false, false, false, false),
    START_SENTENCE(true, false, false, false),
    IN_SENTENCE(false, false, false, true),
    START_COMPOUND(true, true, false, false),
    IN_COMPOUND_wait_op_copula(false, true, true, false),
    IN_COMPOUND_wait_op(false, true, true, false),
    IN_COMPOUND_wait_op_endable(false, true, true, true),
    IN_COMPOUND_wait_term(true, false, true, false),
    IN_COMPOUND_wait_term_endable(true, false, true, true),
    START_EXTENSIONAL_SET(true, false, false, false),
    IN_EXTENSIONAL_SET(true, false, true, true),
    START_INTENSIONAL_SET(true, false, false, false),
    IN_INTENSIONAL_SET(true, false, true, true),
    START_DEPENDENT_VARIABLE(false, false, false, true),
    START_ANONYMOUS_DEPENDENT_VARIABLE(false, false, false, true),
    IN_DEPENDENT_VARIABLE(false, false, true, true);

    private final boolean allowTerm;
    private final boolean allowConnector;
    private final boolean needSeparator;
    private final boolean endable;

    State(final boolean allowTerm, boolean allowConnector, boolean needSeparator,
        final boolean endable)
    {
      this.allowTerm = allowTerm;
      this.allowConnector = allowConnector;
      this.needSeparator = needSeparator;
      this.endable = endable;
    }
  }

  private class Context
  {
    State state;
    int connector = OP_UNSET;
    int numberOfAllowedTerms = -1;
    boolean isStrict = false;
    boolean isPrefixed = false;
    int placeHolder = OP_PLACEHOLDER_NO;

    Context(State state)
    {
      this.state = state;
    }
  }

  NarseseGeneratorImpl(final Writer writer, final int bufferSize, int prefixThreshold)
  {
    this.writer = new BufferedWriter(writer, bufferSize);
    this.prefixThreshold = prefixThreshold;
    push(State.ROOT);
  }

  NarseseGeneratorImpl(final OutputStream out, int bufferSize, int prefixThreshold)
  {
    this(new OutputStreamWriter(out, UTF8SET), bufferSize, prefixThreshold);
  }

  NarseseGeneratorImpl(final OutputStream out, final Charset encoding, final int bufferSize,
      int prefixThreshold)
  {
    this(new OutputStreamWriter(out, encoding), bufferSize, prefixThreshold);
  }

  @Override
  public NarseseGenerator write(NarseseValue value)
      throws NarseseException, NarseseGenerationException
  {
    checkGeneratorState(value != null, "Invalid null Narsese value.");

    Runnable infix = null;
    switch (value.getValueType())
    {
      case COMPOUND_TERM:
        final CompoundTerm c = (CompoundTerm) value;
        switch (c.getConnector())
        {
          case CONJUNCTION:
            writeStartCompoundTerm();
            if (c.getTerms().size() > prefixThreshold)
            {
              writeConjunction();
            } else
            {
              infix = this::writeConjunction;
            }
            break;
          case DISJUNCTION:
            writeStartCompoundTerm();
            if (c.getTerms().size() > prefixThreshold)
            {
              writeDisjunction();
            } else
            {
              infix = this::writeDisjunction;
            }
            break;
          case EXTENSIONAL_DIFFERENCE:
            writeStartCompoundTerm();
            if (c.getTerms().size() > prefixThreshold)
            {
              writeExtensionalDifference();
            } else
            {
              infix = this::writeExtensionalDifference;
            }
            break;
          case EXTENSIONAL_IMAGE:
            writeStartCompoundTerm();
            writeExtensionalImage();
            break;
          case EXTENSIONAL_INTERSECTION:
            writeStartCompoundTerm();
            if (c.getTerms().size() > prefixThreshold)
            {
              writeExtensionalIntersection();
            } else
            {
              infix = this::writeExtensionalIntersection;
            }
            break;
          case EXTENSIONAL_SET:
            writeStartExtensionalSet();
            break;
          case INTENSIONAL_DIFFERENCE:
            writeStartCompoundTerm();
            if (c.getTerms().size() > prefixThreshold)
            {
              writeIntensionalDifference();
            } else
            {
              infix = this::writeIntensionalDifference;
            }
            break;
          case INTENSIONAL_IMAGE:
            writeStartCompoundTerm();
            writeIntensionalImage();
            break;
          case INTENSIONAL_INTERSECTION:
            writeStartCompoundTerm();
            if (c.getTerms().size() > prefixThreshold)
            {
              writeIntensionalIntersection();
            } else
            {
              infix = this::writeIntensionalIntersection;
            }
            break;
          case INTENSIONAL_SET:
            writeStartIntensionalSet();
            break;
          case NEGATION:
            writeStartCompoundTerm();
            writeNegation();
            break;
          case PARALLEL_CONJUNCTION:
            writeStartCompoundTerm();
            if (c.getTerms().size() > prefixThreshold)
            {
              writeParallelConjunction();
            } else
            {
              infix = this::writeParallelConjunction;
            }
            break;
          case PRODUCT:
            writeStartCompoundTerm();
            if (c.getTerms().size() > prefixThreshold)
            {
              writeProduct();
            } else
            {
              infix = this::writeProduct;
            }
            break;
          case SEQUENTIAL_CONJUNCTION:
            writeStartCompoundTerm();
            if (c.getTerms().size() > prefixThreshold)
            {
              writeSequentialConjunction();
            } else
            {
              infix = this::writeSequentialConjunction;
            }
            break;
          default:
        }

        if (c.getConnector() == Connector.EXTENSIONAL_IMAGE
            || c.getConnector() == Connector.INTENSIONAL_IMAGE)
        {
          final List<Term> terms = c.getTerms();
          for (int i = 0; i < c.getPlaceHolderPosition() - 1; i++)
          {
            write(terms.get(i));
          }
          writePlaceHolder();
          for (int i = c.getPlaceHolderPosition() - 1; i < terms.size(); i++)
          {
            write(terms.get(i));
          }
        } else if (c.getConnector() != Connector.NEGATION
            && c.getConnector() != Connector.INTENSIONAL_SET
            && c.getConnector() != Connector.EXTENSIONAL_SET)
        {
          if (c.getTerms().size() > prefixThreshold)
          {
            c.getTerms().forEach(this::write);
          } else
          {
            final List<Term> terms = c.getTerms();
            for (int i = 0; i < terms.size() - 1; i++)
            {
              write(terms.get(i));
              infix.run();
            }
            write(terms.get(terms.size() - 1));
          }
        } else
        {
          c.getTerms().forEach(this::write);
        }
        writeEnd();
        break;
      case CONSTANT:
        writeConstant(((Constant) value).getName());
        break;
      case DEPENDENT_VARIABLE:
        final DependentVariable d = (DependentVariable) value;
        writeStartDependentVariable(d.getName());
        d.getIndependentVariableNames().forEach(this::writeDependentIndependentVariable);
        writeEnd();
        break;
      case GOAL:
        final Goal g = (Goal) value;
        writeStartGoal();
        write(g.getStatement());
        writeTruthValue(g.getDesireValue().getFrequency(), g.getDesireValue().getConfidence());
        writeEnd();
        break;
      case INDEPENDENT_VARIABLE:
        writeIndependentVariable(((IndependentVariable) value).getName());
        break;
      case JUDGMENT:
        final Judgment j = (Judgment) value;
        writeStartJudgment();
        switch (j.getTense())
        {
          case FUTURE:
            writeFutureTense();
            break;
          case PAST:
            writePastTense();
            break;
          case PRESENT:
            writePresentTense();
            break;
          default:
        }
        write(j.getStatement());
        writeTruthValue(j.getTruthValue().getFrequency(), j.getTruthValue().getConfidence());
        writeEnd();
        break;
      case OPERATION:
        final Operation o = (Operation) value;
        writeStartCompoundTerm();
        writeOperation(o.getName());
        o.getTerms().forEach(this::write);
        writeEnd();
        break;
      case QUERY:
        final Query q = (Query) value;
        writeStartQuery();
        write(q.getStatement());
        writeEnd();
        break;
      case QUERY_VARIABLE:
        writeQueryVariable(((QueryVariable) value).getName());
        break;
      case QUESTION:
        final Question qu = (Question) value;
        writeStartQuestion();
        switch (qu.getTense())
        {
          case FUTURE:
            writeFutureTense();
            break;
          case PAST:
            writePastTense();
            break;
          case PRESENT:
            writePresentTense();
            break;
          default:
        }
        write(qu.getStatement());
        writeEnd();
        break;
      case RELATION:
        final Relation r = (Relation) value;
        writeStartCompoundTerm();
        write(r.getSubject());
        switch (r.getCopula())
        {
          case CONCURRENT_EQUIVALENCE:
            writeConcurrentEquivalenceCopula();
            break;
          case CONCURRENT_IMPLICATION:
            writeConcurrentImplicationCopula();
            break;
          case EQUIVALENCE:
            writeEquivalenceCopula();
            break;
          case IMPLICATION:
            writeImplicationCopula();
            break;
          case INHERITANCE:
            writeInheritanceCopula();
            break;
          case PREDICTIVE_EQUIVALENCE:
            writePredictiveEquivalenceCopula();
            break;
          case PREDICTIVE_IMPLICATION:
            writePredictiveImplicationCopula();
            break;
          case RETROSPECTIVE_IMPLICATION:
            writeRetrospectiveImplicationCopula();
            break;
          case SIMILARITY:
            writeSimilarityCopula();
            break;
          default:
        }
        write(r.getPredicate());
        writeEnd();
        break;
      default:
    }
    return this;
  }

  @Override
  public NarseseGenerator writeStartJudgment() throws NarseseException, NarseseGenerationException
  {
    checkSentenceState();

    currentSentenceEndChar = CHAR_END_JUDGMENT;
    currentSentenceAllowTense = true;
    currentSentenceAllowTruth = true;
    truthSet = false;
    tenseSet = false;

    push(State.START_SENTENCE);

    if (firstSentence)
    {
      firstSentence = false;
    } else
    {
      justWrite(CHAR_EOL);
    }
    return this;
  }

  @Override
  public NarseseGenerator writeStartGoal() throws NarseseException, NarseseGenerationException
  {
    checkSentenceState();

    currentSentenceEndChar = CHAR_END_GOAL;
    currentSentenceAllowTense = false;
    currentSentenceAllowTruth = true;
    truthSet = false;
    tenseSet = false;

    push(State.START_SENTENCE);

    if (firstSentence)
    {
      firstSentence = false;
    } else
    {
      justWrite(CHAR_EOL);
    }
    return this;
  }

  @Override
  public NarseseGenerator writeStartQuestion() throws NarseseException, NarseseGenerationException
  {
    checkSentenceState();

    currentSentenceEndChar = CHAR_END_QUESTION;
    currentSentenceAllowTense = true;
    currentSentenceAllowTruth = false;
    truthSet = false;
    tenseSet = false;

    push(State.START_SENTENCE);

    if (firstSentence)
    {
      firstSentence = false;
    } else
    {
      justWrite(CHAR_EOL);
    }
    return this;
  }

  @Override
  public NarseseGenerator writeStartQuery() throws NarseseException, NarseseGenerationException
  {
    checkSentenceState();

    currentSentenceEndChar = CHAR_END_QUERY;
    currentSentenceAllowTense = false;
    currentSentenceAllowTruth = false;
    truthSet = false;
    tenseSet = false;

    push(State.START_SENTENCE);

    if (firstSentence)
    {
      firstSentence = false;
    } else
    {
      justWrite(CHAR_EOL);
    }
    return this;
  }

  private void checkSentenceState()
  {
    checkGeneratorState(State.ROOT == peek().state, "A sentence is only valid at document root.");
  }

  @Override
  public NarseseGenerator writePastTense() throws NarseseException, NarseseGenerationException
  {
    checkTenseState();

    justWrite(CHAR_PAST_TENSE);
    justWrite(CHAR_SPACE);
    return this;
  }

  @Override
  public NarseseGenerator writePresentTense() throws NarseseException, NarseseGenerationException
  {
    checkTenseState();

    justWrite(CHAR_PRESENT_TENSE);
    justWrite(CHAR_SPACE);
    return this;
  }

  @Override
  public NarseseGenerator writeFutureTense() throws NarseseException, NarseseGenerationException
  {
    checkTenseState();

    justWrite(CHAR_FUTURE_TENSE);
    justWrite(CHAR_SPACE);
    return this;
  }

  private void checkTenseState()
  {
    final State current = peek().state;
    checkGeneratorState(State.START_SENTENCE == current, () -> "Bad state " + current
        + ", tense can only be written before the statement of a sentence.");
    checkGeneratorState(currentSentenceAllowTense, () -> "The current sentence (ending by "
        + currentSentenceEndChar + ") does not allow a tense.");
    checkGeneratorState(!tenseSet, "The tense of a sentence can only be written once.");
  }

  @Override
  public NarseseGenerator writeTruthValue(double frequency, double confidence)
      throws NarseseException, NarseseGenerationException
  {
    final State current = peek().state;
    checkGeneratorState(State.IN_SENTENCE == current, () -> "Bad state " + current
        + ", truth value can only be written after the statement of a sentence.");
    checkGeneratorState(currentSentenceAllowTruth, () -> "The current sentence (ending by "
        + currentSentenceEndChar + ") does not allow a truth value.");
    checkGeneratorState(!truthSet, "The truth value of a sentence can only be written once.");
    checkGeneratorState(0 <= frequency,
        () -> "Frequency must be greater or equals to 0, current value is " + frequency);
    checkGeneratorState(frequency <= 1,
        () -> "Frequency must be less or equals to 1, current value is " + frequency);
    checkGeneratorState(0 < confidence,
        () -> "Confidence must be greater than 0, current value is " + frequency);
    checkGeneratorState(confidence < 1,
        () -> "Frequency must be less than 1, current value is " + frequency);

    justWrite(CHAR_SPACE);
    justWrite(CHAR_START_END_TRUTH);
    justWrite(String.valueOf(frequency));
    justWrite(CHAR_TRUTH_SEPARATOR);
    justWrite(String.valueOf(confidence));
    justWrite(CHAR_START_END_TRUTH);
    return this;
  }

  @Override
  public NarseseGenerator writeStartCompoundTerm()
      throws NarseseException, NarseseGenerationException
  {
    prepareTerm();

    push(State.START_COMPOUND);

    justWrite(CHAR_START_BLOCK);
    return this;
  }

  @Override
  public NarseseGenerator writeStartExtensionalSet()
      throws NarseseException, NarseseGenerationException
  {
    prepareTerm();

    push(State.START_EXTENSIONAL_SET);

    justWrite(CHAR_START_EXTENSIONAL_SET);
    return this;
  }

  @Override
  public NarseseGenerator writeStartIntensionalSet()
      throws NarseseException, NarseseGenerationException
  {
    prepareTerm();

    push(State.START_INTENSIONAL_SET);

    justWrite(CHAR_START_INTENSIONAL_SET);
    return this;
  }

  @Override
  public NarseseGenerator writeConstant(String name)
      throws NarseseException, NarseseGenerationException
  {
    checkGeneratorState(name != null, "Invalid null name.");
    checkGeneratorState(NarseseChars.isWordValid(name), () -> "Invalid name: " + name);
    prepareTerm();

    justWrite(name);

    if (peek().state == State.IN_SENTENCE)
    {
      justWrite(currentSentenceEndChar);
    }

    return this;
  }

  @Override
  public NarseseGenerator writeQueryVariable(String name)
      throws NarseseException, NarseseGenerationException
  {
    checkGeneratorState(name != null, "Invalid null name.");
    checkGeneratorState(NarseseChars.isWordValid(name), () -> "Invalid name: " + name);
    prepareTerm();

    justWrite(CHAR_QUERY_VARIABLE_MARK);
    justWrite(name);

    if (peek().state == State.IN_SENTENCE)
    {
      justWrite(currentSentenceEndChar);
    }

    return this;
  }

  @Override
  public NarseseGenerator writeStartDependentVariable(String name)
      throws NarseseException, NarseseGenerationException
  {
    checkGeneratorState(name != null, "Invalid null name.");
    checkGeneratorState(NarseseChars.isWordValid(name), () -> "Invalid name: " + name);
    prepareTerm();

    justWrite(CHAR_VARIABLE_MARK);
    justWrite(name);

    if (!name.isEmpty())
    {
      push(State.START_DEPENDENT_VARIABLE);
      justWrite(CHAR_START_BLOCK);
    } else
    {
      push(State.START_ANONYMOUS_DEPENDENT_VARIABLE);
    }

    return this;
  }

  @Override
  public NarseseGenerator writeDependentIndependentVariable(String name)
      throws NarseseException, NarseseGenerationException
  {
    checkGeneratorState(name != null, "Invalid null name.");

    final Context current = peek();
    checkGeneratorState(
        current.state == State.START_DEPENDENT_VARIABLE
            || current.state == State.IN_DEPENDENT_VARIABLE,
        () -> "Bad state " + current
            + ", a dependent independent variable cannot be written in this context.");
    checkGeneratorState(!name.isEmpty(), () -> "Invalid independent variable, name is empty.");
    checkGeneratorState(NarseseChars.isWordValid(name), () -> "Invalid name: " + name);

    if (current.state.needSeparator)
    {
      justWrite(CHAR_TERM_SEPARATOR);
    }

    if (current.state == State.START_DEPENDENT_VARIABLE)
    {
      current.state = State.IN_DEPENDENT_VARIABLE;
    }

    justWrite(name);
    return this;
  }

  @Override
  public NarseseGenerator writeIndependentVariable(String name)
      throws NarseseException, NarseseGenerationException
  {
    checkGeneratorState(name != null, "Invalid null name.");
    checkGeneratorState(!name.isEmpty(), () -> "Invalid independent variable, name is empty.");
    checkGeneratorState(NarseseChars.isWordValid(name), () -> "Invalid name: " + name);
    prepareTerm();

    justWrite(CHAR_VARIABLE_MARK);
    justWrite(name);

    if (peek().state == State.IN_SENTENCE)
    {
      justWrite(currentSentenceEndChar);
    }

    return this;
  }

  @Override
  public NarseseGenerator writePlaceHolder() throws NarseseException, NarseseGenerationException
  {
    final Context current = peek();
    checkGeneratorState(current.state == State.IN_COMPOUND_wait_term,
        () -> "Bad state " + current + ", a place holder cannot be written in this context.");
    checkGeneratorState(current.placeHolder != OP_PLACEHOLDER_NO,
        "This term context does not allow placeholder.");
    checkGeneratorState(current.placeHolder != OP_PLACEHOLDER_ALREADY_SET,
        "A placeholder has already been set.");
    checkGeneratorState(current.numberOfAllowedTerms <= 1,
        "A placeholder can only be set after the 2nd position.");

    current.numberOfAllowedTerms = current.numberOfAllowedTerms - 1;
    current.placeHolder = OP_PLACEHOLDER_ALREADY_SET;
    current.state = State.IN_COMPOUND_wait_term_endable;

    justWrite(CHAR_TERM_SEPARATOR);
    justWrite(CHAR_PLACEHOLDER);
    return this;
  }

  private void prepareTerm()
  {
    final Context current = peek();
    checkGeneratorState(current.state.allowTerm,
        () -> "Bad state " + current + ", a term cannot be written in this context.");

    if (current.state.needSeparator)
    {
      justWrite(CHAR_TERM_SEPARATOR);
    }

    if (current.state == State.START_SENTENCE)
    {
      current.state = State.IN_SENTENCE;
    } else if (current.state == State.START_COMPOUND)
    {
      current.state = State.IN_COMPOUND_wait_op_copula;
    } else if (current.state == State.IN_COMPOUND_wait_term)
    {
      current.numberOfAllowedTerms--;

      if (current.isPrefixed)
      {
        if (current.numberOfAllowedTerms <= 0 && current.placeHolder == OP_PLACEHOLDER_NO)
        {
          current.state = State.IN_COMPOUND_wait_term_endable;
        } else if (current.isStrict && current.numberOfAllowedTerms < 0)
        {
          checkGeneratorState(false, "Too many terms in this context.");
        }
      } else
      {
        if (current.numberOfAllowedTerms <= 0)
        {
          current.state = State.IN_COMPOUND_wait_op_endable;
        } else if (current.isStrict && current.numberOfAllowedTerms < 0)
        {
          checkGeneratorState(false, "Too many terms in this context.");
        } else
        {
          current.state = State.IN_COMPOUND_wait_op;
        }
      }
    } else if (current.state == State.IN_COMPOUND_wait_term_endable)
    {
      current.numberOfAllowedTerms--;

      if (current.isStrict && current.numberOfAllowedTerms < 0)
      {
        checkGeneratorState(false, "Too many terms in this context.");
      }

      if (!current.isPrefixed)
      {
        current.state = State.IN_COMPOUND_wait_op_endable;
      }
    } else if (current.state == State.START_EXTENSIONAL_SET)
    {
      current.state = State.IN_EXTENSIONAL_SET;
    } else if (current.state == State.START_INTENSIONAL_SET)
    {
      current.state = State.IN_INTENSIONAL_SET;
    }
  }

  @Override
  public NarseseGenerator writeInheritanceCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_INHERITANCE);
    return this;
  }

  @Override
  public NarseseGenerator writeSimilarityCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_SIMILARITY);
    return this;
  }

  @Override
  public NarseseGenerator writeImplicationCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_IMPLICATION);
    return this;
  }

  @Override
  public NarseseGenerator writeEquivalenceCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_EQUIVALENCE);
    return this;
  }

  @Override
  public NarseseGenerator writeInstanceCopula() throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_INSTANCE);
    return this;
  }

  @Override
  public NarseseGenerator writePropertyCopula() throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_PROPERTY);
    return this;
  }

  @Override
  public NarseseGenerator writeInstancePropertyCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_INSTANCE_PROPERTY);
    return this;
  }

  @Override
  public NarseseGenerator writePredictiveImplicationCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_PREDICTIVE_IMPLICATION);
    return this;
  }

  @Override
  public NarseseGenerator writeRetrospectiveImplicationCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_RETROSPECTIVE_IMPLICATION);
    return this;
  }

  @Override
  public NarseseGenerator writeConcurrentImplicationCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_CONCURRENT_IMPLICATION);
    return this;
  }

  @Override
  public NarseseGenerator writePredictiveEquivalenceCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_PREDICTIVE_EQUIVALENCE);
    return this;
  }

  @Override
  public NarseseGenerator writeConcurrentEquivalenceCopula()
      throws NarseseException, NarseseGenerationException
  {
    prepareCopula();
    justWrite(CHAR_CONCURRENT_EQUIVALENCE);
    return this;
  }

  private void prepareCopula()
  {
    final Context current = peek();
    checkGeneratorState(current.state == State.IN_COMPOUND_wait_op_copula,
        () -> "Bad state " + current + ", a copula cannot be written in this context.");

    if (current.state.needSeparator)
    {
      justWrite(CHAR_TERM_SEPARATOR);
    }

    current.state = State.IN_COMPOUND_wait_term;
    current.numberOfAllowedTerms = 1;
    current.isStrict = true;
  }

  @Override
  public NarseseGenerator writeOperation(String name)
      throws NarseseException, NarseseGenerationException
  {
    checkGeneratorState(name != null, "Invalid null name.");
    checkGeneratorState(!name.isEmpty(), () -> "Invalid operation, name is empty.");
    checkGeneratorState(NarseseChars.isWordValid(name), () -> "Invalid name: " + name);
    prepareConnector(OP_OPERATION);

    justWrite(CHAR_OPERATION);
    justWrite(name);
    return this;
  }

  @Override
  public NarseseGenerator writeExtensionalIntersection()
      throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_EXTENSIONAL_INTERSECTION);
    justWrite(CHAR_EXTENSIONAL_INTERSECTION);
    return this;
  }

  @Override
  public NarseseGenerator writeIntensionalIntersection()
      throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_INTENSIONAL_INTERSECTION);
    justWrite(CHAR_INTENSIONAL_INTERSECTION);
    return this;
  }

  @Override
  public NarseseGenerator writeExtensionalDifference()
      throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_EXTENSIONAL_DIFFERENCE);
    justWrite(CHAR_EXTENSIONAL_DIFFERENCE);
    return this;
  }

  @Override
  public NarseseGenerator writeIntensionalDifference()
      throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_INTENSIONAL_DIFFERENCE);
    justWrite(CHAR_INTENSIONAL_DIFFERENCE);
    return this;
  }

  @Override
  public NarseseGenerator writeProduct() throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_PRODUCT);
    justWrite(CHAR_PRODUCT);
    return this;
  }

  @Override
  public NarseseGenerator writeExtensionalImage()
      throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_EXTENSIONAL_IMAGE);
    justWrite(CHAR_EXTENSIONAL_IMAGE);
    return this;
  }

  @Override
  public NarseseGenerator writeIntensionalImage()
      throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_INTENSIONAL_IMAGE);
    justWrite(CHAR_INTENSIONAL_IMAGE);
    return this;
  }

  @Override
  public NarseseGenerator writeNegation() throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_NEGATION);
    justWrite(CHAR_NEGATION);
    return this;
  }

  @Override
  public NarseseGenerator writeConjunction() throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_CONJUNCTION);
    justWrite(CHAR_CONJUNCTION);
    return this;
  }

  @Override
  public NarseseGenerator writeDisjunction() throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_DISJUNCTION);
    justWrite(CHAR_DISJUNCTION);
    return this;
  }

  @Override
  public NarseseGenerator writeSequentialConjunction()
      throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_SEQUENTIAL_CONJUNCTION);
    justWrite(CHAR_SEQUENTIAL_CONJUNCTION);
    return this;
  }

  @Override
  public NarseseGenerator writeParallelConjunction()
      throws NarseseException, NarseseGenerationException
  {
    prepareConnector(OP_PARALLEL_CONJUNCTION);
    justWrite(CHAR_PARALLEL_CONJUNCTION);
    return this;
  }

  private void prepareConnector(int n_connector)
  {
    final Context current = peek();
    checkGeneratorState(current.state.allowConnector,
        () -> "Bad state " + current + ", a connector cannot be written in this context.");

    final int currentConnector = current.connector;
    checkGeneratorState(currentConnector == OP_UNSET || currentConnector == n_connector,
        () -> "Bad connector " + n_connector + ", the connector " + currentConnector
            + " has already been set in this context.");

    if (current.state.needSeparator)
    {
      justWrite(CHAR_TERM_SEPARATOR);
    }

    if (currentConnector != n_connector)
    {
      current.connector = n_connector;
      if (current.state == State.IN_COMPOUND_wait_op_copula)
      {
        current.numberOfAllowedTerms = OP_NB_TERMS[n_connector] - 1;
        current.isPrefixed = false;
      } else
      {
        current.numberOfAllowedTerms = OP_NB_TERMS[n_connector];
        current.isPrefixed = true;
      }
      current.isStrict = OP_NB_TERMS_STRICTS[n_connector];
      current.placeHolder = OP_PLACEHOLDER[n_connector];
    }

    if (current.state == State.START_COMPOUND)
    {
      if (current.numberOfAllowedTerms == 0)
      {
        current.state = State.IN_COMPOUND_wait_term_endable;
      } else
      {
        current.state = State.IN_COMPOUND_wait_term;
      }
    } else
    {
      checkGeneratorState(OP_SUPPORT_INFIX[n_connector], () -> "Bad state " + current
          + ", the connector " + n_connector + " does not support infix form.");
      current.state = State.IN_COMPOUND_wait_term;
    }
  }

  @Override
  public NarseseGenerator writeEnd() throws NarseseException, NarseseGenerationException
  {
    final Context last = pop();

    checkGeneratorState(last.state.endable,
        () -> "Bad state " + last + ", the context cannot be ended.");

    if (last.state == State.IN_COMPOUND_wait_op_endable
        || last.state == State.IN_COMPOUND_wait_term_endable
        || last.state == State.START_DEPENDENT_VARIABLE
        || last.state == State.IN_DEPENDENT_VARIABLE)
    {
      justWrite(CHAR_END_BLOCK);
      if (peek().state == State.IN_SENTENCE)
      {
        justWrite(currentSentenceEndChar);
      }
    } else if (last.state == State.START_ANONYMOUS_DEPENDENT_VARIABLE
        && peek().state == State.IN_SENTENCE)
    {
      justWrite(currentSentenceEndChar);
    } else if (last.state == State.IN_EXTENSIONAL_SET)
    {
      justWrite(CHAR_END_EXTENSIONAL_SET);
      if (peek().state == State.IN_SENTENCE)
      {
        justWrite(currentSentenceEndChar);
      }
    } else if (last.state == State.IN_INTENSIONAL_SET)
    {
      justWrite(CHAR_END_INTENSIONAL_SET);
      if (peek().state == State.IN_SENTENCE)
      {
        justWrite(currentSentenceEndChar);
      }
    }
    return this;
  }

  private static void checkGeneratorState(boolean mustBeTrue, String message)
      throws NarseseGenerationException
  {
    if (!mustBeTrue)
    {
      throw new NarseseGenerationException(message);
    }
  }

  private static void checkGeneratorState(boolean mustBeTrue, Supplier<String> messageSupplier)
      throws NarseseGenerationException
  {
    if (!mustBeTrue)
    {
      throw new NarseseGenerationException(messageSupplier.get());
    }
  }

  @Override
  public void close()
  {
    if (!closed)
    {
      NarseseGenerationException ex = null;
      final State current = peek().state;
      if (current != State.ROOT)
      {
        ex = new NarseseGenerationException("Invalid Narsese");
      }
      try
      {
        writer.close();
      } catch (final IOException e)
      {
        if (ex != null)
        {
          throw ex;
        }
        throw new NarseseGenerationException(e.getMessage(), e);
      } finally
      {
        closed = true;
      }
      if (ex != null)
      {
        throw ex;
      }
    }
  }

  @Override
  public void flush()
  {
    try
    {
      writer.flush();
    } catch (final IOException e)
    {
      throw new NarseseException(e.getMessage(), e);
    }
  }

  private void justWrite(final String value)
  {
    try
    {
      writer.write(value);
    } catch (final IOException e)
    {
      throw new NarseseException(e.getMessage(), e);
    }
  }

  private void justWrite(final char[] value)
  {
    try
    {
      writer.write(value);
    } catch (final IOException e)
    {
      throw new NarseseException(e.getMessage(), e);
    }
  }

  private void justWrite(final char value)
  {
    try
    {
      writer.write(value);
    } catch (final IOException e)
    {
      throw new NarseseException(e.getMessage(), e);
    }
  }

  private void push(final State state)
  {
    contextQueue.addFirst(new Context(state));
  }

  private Context peek()
  {
    return contextQueue.getFirst();
  }

  private Context pop()
  {
    return contextQueue.removeFirst();
  }
}
