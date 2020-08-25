package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import java.util.HashSet;
import org.narson.api.narsese.CompoundConnector;
import org.narson.api.narsese.CompoundTermBuilder;
import org.narson.api.narsese.ConstantTerm;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.DependentVariableBuilder;
import org.narson.api.narsese.GoalBuilder;
import org.narson.api.narsese.ImageConnector;
import org.narson.api.narsese.ImageTermBuilder;
import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.JudgmentBuilder;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NegationTerm;
import org.narson.api.narsese.OperationBuilder;
import org.narson.api.narsese.Query;
import org.narson.api.narsese.QueryVariable;
import org.narson.api.narsese.QuestionBuilder;
import org.narson.api.narsese.SecondaryCopula;
import org.narson.api.narsese.SetConnector;
import org.narson.api.narsese.SetTermBuilder;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;
import org.narson.narsese.provider.rules.RuleMachine;
import org.narson.narsese.provider.rules.RuleMachines;
import org.narson.narsese.provider.util.WordHelper;

final public class NarseseFactoryImpl implements NarseseFactory
{
  private final Narsese narsese;
  private final TruthValue defaultTruthValue;
  private final TruthValue defaultDesireValue;
  private final RuleMachine ruleMachine;

  public NarseseFactoryImpl(Narsese narsese, double defaultTruthFrequency,
      double defaultTruthConfidence, double defaultDesireFrequency, double defaultDesireConfidence)
  {
    this.narsese = narsese;
    defaultTruthValue = new TruthValueImpl(defaultTruthFrequency, defaultTruthConfidence);
    defaultDesireValue = new TruthValueImpl(defaultDesireFrequency, defaultDesireConfidence);
    ruleMachine = RuleMachines.createMachine(narsese);
  }

  @Override
  public JudgmentBuilder judgment(Term statement)
  {
    checkNotNull(statement, "statement");
    return new JudgmentBuilderImpl(narsese, ruleMachine, statement, defaultTruthValue);
  }

  @Override
  public QuestionBuilder question(Term statement)
  {
    checkNotNull(statement, "statement");
    return new QuestionBuilderImpl(narsese, statement);
  }

  @Override
  public GoalBuilder goal(Term statement)
  {
    checkNotNull(statement, "statement");
    return new GoalBuilderImpl(narsese, statement, defaultDesireValue);
  }

  @Override
  public Query query(Term statement)
  {
    checkNotNull(statement, "statement");
    return new QueryImpl(narsese, statement);
  }

  @Override
  public OperationBuilder operation(String name)
  {
    checkNotNull(name, "name");
    checkArgument(WordHelper.isWordValid(name), () -> "Invalide name: " + name);

    return new OperationTermBuilderImpl(narsese, name);
  }

  @Override
  public CopulaTerm copula(Term subject, Copula copula, Term predicate)
  {
    checkNotNull(subject, "subject");
    checkNotNull(copula, "copula");
    checkNotNull(predicate, "predicate");

    if (copula.isSymmetric())
    {
      if (copula.isFirstOrder())
      {
        return new CopulaTermImpl(narsese, subject, Copula.SIMILARITY, predicate, Tense.NONE);
      } else
      {
        return new CopulaTermImpl(narsese, subject, Copula.EQUIVALENCE, predicate, Tense.NONE);
      }
    } else
    {
      if (copula.isFirstOrder())
      {
        return new CopulaTermImpl(narsese, subject, Copula.INHERITANCE, predicate, Tense.NONE);
      } else
      {
        return new CopulaTermImpl(narsese, subject, Copula.IMPLICATION, predicate, Tense.NONE);
      }
    }
  }

  @Override
  public CopulaTerm copula(Term subject, SecondaryCopula copula, Term predicate)
  {
    checkNotNull(subject, "subject");
    checkNotNull(copula, "copula");
    checkNotNull(predicate, "predicate");

    switch (copula)
    {
      case INSTANCE:
        return new CopulaTermImpl(narsese, set(SetConnector.EXTENSIONAL_SET).of(subject).build(),
            Copula.INHERITANCE, predicate, Tense.NONE);
      case INSTANCE_PROPERTY:
        return new CopulaTermImpl(narsese, set(SetConnector.EXTENSIONAL_SET).of(subject).build(),
            Copula.INHERITANCE, set(SetConnector.INTENSIONAL_SET).of(predicate).build(),
            Tense.NONE);
      case PROPERTY:
        return new CopulaTermImpl(narsese, subject, Copula.INHERITANCE,
            set(SetConnector.INTENSIONAL_SET).of(predicate).build(), Tense.NONE);
      case CONCURRENT_EQUIVALENCE:
        return new CopulaTermImpl(narsese, subject, Copula.EQUIVALENCE, predicate, Tense.PRESENT);
      case CONCURRENT_IMPLICATION:
        return new CopulaTermImpl(narsese, subject, Copula.IMPLICATION, predicate, Tense.PRESENT);
      case PREDICTIVE_EQUIVALENCE:
        return new CopulaTermImpl(narsese, subject, Copula.EQUIVALENCE, predicate, Tense.FUTURE);
      case PREDICTIVE_IMPLICATION:
        return new CopulaTermImpl(narsese, subject, Copula.IMPLICATION, predicate, Tense.FUTURE);
      case RETROSPECTIVE_IMPLICATION:
        return new CopulaTermImpl(narsese, subject, Copula.IMPLICATION, predicate, Tense.PAST);
      default:
        throw new IllegalArgumentException("Unknow copula.");
    }
  }

  @Override
  public CompoundTermBuilder compound(CompoundConnector connector)
  {
    checkNotNull(connector, "connector");

    return new CompoundTermBuilderImpl(narsese, connector);
  }

  @Override
  public SetTermBuilder set(SetConnector connector) throws NullPointerException
  {
    checkNotNull(connector, "connector");
    return new SetTermBuilderImpl(narsese, connector);
  }

  @Override
  public ImageTermBuilder image(ImageConnector connector) throws NullPointerException
  {
    checkNotNull(connector, "connector");
    return new ImageTermBuilderImpl(narsese, connector);
  }

  @Override
  public NegationTerm negation(Term term) throws NullPointerException
  {
    checkNotNull(term, "term");
    return new NegationTermImpl(narsese, term);
  }

  @Override
  public ConstantTerm constant(String name)
  {
    checkNotNull(name, "name");
    checkArgument(WordHelper.isWordValid(name), () -> "Invalide name: " + name);

    return new ConstantTermImpl(narsese, name);
  }

  @Override
  public IndependentVariable independentVariable(String name)
  {
    checkNotNull(name, "name");
    checkArgument(WordHelper.isWordValid(name), () -> "Invalide name: " + name);

    return new IndependentVariableImpl(narsese, name);
  }

  @Override
  public DependentVariable dependentVariable()
  {
    return new DependentVariableImpl(narsese, "", new HashSet<>());
  }

  @Override
  public DependentVariableBuilder dependentVariable(String name)
  {
    checkArgument(name.isEmpty() || WordHelper.isWordValid(name), () -> "Invalide name: " + name);

    return new DependentVariableBuilderImpl(narsese, name);
  }

  @Override
  public QueryVariable queryVariable()
  {
    return new QueryVariableImpl(narsese, "");
  }

  @Override
  public QueryVariable queryVariable(String name)
  {
    checkArgument(name.isEmpty() || WordHelper.isWordValid(name), () -> "Invalide name: " + name);

    return new QueryVariableImpl(narsese, name);
  }
}
