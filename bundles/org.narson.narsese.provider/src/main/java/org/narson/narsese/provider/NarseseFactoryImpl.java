package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotEmpty;
import static org.narson.tools.PredChecker.checkNotNull;
import org.narson.api.narsese.CompoundTermBuilder;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Constant;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.DependentVariableBuilder;
import org.narson.api.narsese.GoalBuilder;
import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.JudgmentBuilder;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.OperationBuilder;
import org.narson.api.narsese.Query;
import org.narson.api.narsese.QueryVariable;
import org.narson.api.narsese.QuestionBuilder;
import org.narson.api.narsese.SecondaryCopula;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;

final class NarseseFactoryImpl implements NarseseFactory
{
  private final int bufferSize;
  private final int prefixThreshold;
  private final TruthValue defaultTruthValue;
  private final TruthValue defaultDesireValue;

  public NarseseFactoryImpl(int bufferSize, int prefixThreshold, TruthValue defaultTruthValue,
      TruthValue defaultDesireValue)
  {
    this.bufferSize = bufferSize;
    this.prefixThreshold = prefixThreshold;
    this.defaultTruthValue = defaultTruthValue;
    this.defaultDesireValue = defaultDesireValue;
  }

  @Override
  public JudgmentBuilder judgment(Term statement)
  {
    checkNotNull(statement, "statement");
    return new JudgmentBuilderImpl(bufferSize, prefixThreshold, statement, defaultTruthValue);
  }

  @Override
  public QuestionBuilder question(Term statement)
  {
    checkNotNull(statement, "statement");
    return new QuestionBuilderImpl(bufferSize, prefixThreshold, statement);
  }

  @Override
  public GoalBuilder goal(Term statement)
  {
    checkNotNull(statement, "statement");
    return new GoalBuilderImpl(bufferSize, prefixThreshold, statement, defaultDesireValue);
  }

  @Override
  public Query query(Term statement)
  {
    checkNotNull(statement, "statement");
    return new QueryImpl(bufferSize, prefixThreshold, statement);
  }

  @Override
  public OperationBuilder operation(String name)
  {
    checkNotNull(name, "name");
    checkNotEmpty(name, "name");
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

    return new OperationBuilderImpl(bufferSize, prefixThreshold, name);
  }

  @Override
  public CopulaTerm copulaTerm(Term subject, Copula copula, Term predicate)
  {
    checkNotNull(subject, "subject");
    checkNotNull(copula, "copula");
    checkNotNull(predicate, "predicate");

    return new CopulaTermImpl(bufferSize, prefixThreshold, subject, copula, predicate, Tense.NONE);
  }

  @Override
  public CopulaTerm copulaTerm(Term subject, SecondaryCopula copula, Term predicate)
  {
    checkNotNull(subject, "subject");
    checkNotNull(copula, "copula");
    checkNotNull(predicate, "predicate");

    switch (copula)
    {
      case INSTANCE:
        return new CopulaTermImpl(bufferSize, prefixThreshold,
            compoundTerm(Connector.EXTENSIONAL_SET).of(subject).build(), Copula.INHERITANCE,
            predicate, Tense.NONE);
      case INSTANCE_PROPERTY:
        return new CopulaTermImpl(bufferSize, prefixThreshold,
            compoundTerm(Connector.EXTENSIONAL_SET).of(subject).build(), Copula.INHERITANCE,
            compoundTerm(Connector.INTENSIONAL_SET).of(predicate).build(), Tense.NONE);
      case PROPERTY:
        return new CopulaTermImpl(bufferSize, prefixThreshold, subject, Copula.INHERITANCE,
            compoundTerm(Connector.INTENSIONAL_SET).of(predicate).build(), Tense.NONE);
      case CONCURRENT_EQUIVALENCE:
        return new CopulaTermImpl(bufferSize, prefixThreshold, subject, Copula.EQUIVALENCE,
            predicate, Tense.PRESENT);
      case CONCURRENT_IMPLICATION:
        return new CopulaTermImpl(bufferSize, prefixThreshold, subject, Copula.IMPLICATION,
            predicate, Tense.PRESENT);
      case PREDICTIVE_EQUIVALENCE:
        return new CopulaTermImpl(bufferSize, prefixThreshold, subject, Copula.EQUIVALENCE,
            predicate, Tense.FUTURE);
      case PREDICTIVE_IMPLICATION:
        return new CopulaTermImpl(bufferSize, prefixThreshold, subject, Copula.IMPLICATION,
            predicate, Tense.FUTURE);
      case RETROSPECTIVE_IMPLICATION:
        return new CopulaTermImpl(bufferSize, prefixThreshold, subject, Copula.IMPLICATION,
            predicate, Tense.PAST);
      default:
        throw new IllegalArgumentException("Unknow copula.");
    }
  }

  @Override
  public CompoundTermBuilder compoundTerm(Connector connector)
  {
    checkNotNull(connector, "connector");

    return new CompoundTermBuilderImpl(bufferSize, prefixThreshold, connector);
  }

  @Override
  public Constant constant(String name)
  {
    checkNotNull(name, "name");
    checkNotEmpty(name, "name");
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

    return new ConstantImpl(bufferSize, name);
  }

  @Override
  public IndependentVariable independentVariable(String name)
  {
    checkNotNull(name, "name");
    checkNotEmpty(name, "name");
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

    return new IndependentVariableImpl(bufferSize, name);
  }

  @Override
  public DependentVariable dependentVariable()
  {
    return new DependentVariableImpl(bufferSize);
  }

  @Override
  public DependentVariableBuilder dependentVariable(String name)
  {
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

    return new DependentVariableBuilderImpl(bufferSize, name);
  }

  @Override
  public QueryVariable queryVariable()
  {
    return new QueryVariableImpl(bufferSize);
  }

  @Override
  public QueryVariable queryVariable(String name)
  {
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

    return new QueryVariableImpl(bufferSize, name);
  }
}
