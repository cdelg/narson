package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotEmpty;
import static org.narson.tools.PredChecker.checkNotNull;
import java.util.ArrayList;
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
import org.narson.api.narsese.Narsese;
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
  private final Narsese narsese;
  private final TruthValue defaultTruthValue;
  private final TruthValue defaultDesireValue;

  public NarseseFactoryImpl(Narsese narsese, TruthValue defaultTruthValue,
      TruthValue defaultDesireValue)
  {
    this.narsese = narsese;
    this.defaultTruthValue = defaultTruthValue;
    this.defaultDesireValue = defaultDesireValue;
  }

  @Override
  public JudgmentBuilder judgment(Term statement)
  {
    checkNotNull(statement, "statement");
    return new JudgmentBuilderImpl(narsese, statement, defaultTruthValue);
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
    checkNotEmpty(name, "name");
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

    return new OperationBuilderImpl(narsese, name);
  }

  @Override
  public CopulaTerm copulaTerm(Term subject, Copula copula, Term predicate)
  {
    checkNotNull(subject, "subject");
    checkNotNull(copula, "copula");
    checkNotNull(predicate, "predicate");

    if (copula.isSymmetric())
    {
      if (copula.isFirstOrder())
      {
        return new SimilarityCopulaTerm(narsese, subject, predicate, Tense.NONE);
      } else
      {
        return new EquivalenceCopulaTerm(narsese, subject, predicate, Tense.NONE);
      }
    } else
    {
      if (copula.isFirstOrder())
      {
        return new InheritanceCopulaTerm(narsese, subject, predicate, Tense.NONE);
      } else
      {
        return new ImplicationCopulaTerm(narsese, subject, predicate, Tense.NONE);
      }
    }
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
        return new InheritanceCopulaTerm(narsese,
            compoundTerm(Connector.EXTENSIONAL_SET).of(subject).build(), predicate, Tense.NONE);
      case INSTANCE_PROPERTY:
        return new InheritanceCopulaTerm(narsese,
            compoundTerm(Connector.EXTENSIONAL_SET).of(subject).build(),
            compoundTerm(Connector.INTENSIONAL_SET).of(predicate).build(), Tense.NONE);
      case PROPERTY:
        return new InheritanceCopulaTerm(narsese, subject,
            compoundTerm(Connector.INTENSIONAL_SET).of(predicate).build(), Tense.NONE);
      case CONCURRENT_EQUIVALENCE:
        return new EquivalenceCopulaTerm(narsese, subject, predicate, Tense.PRESENT);
      case CONCURRENT_IMPLICATION:
        return new ImplicationCopulaTerm(narsese, subject, predicate, Tense.PRESENT);
      case PREDICTIVE_EQUIVALENCE:
        return new EquivalenceCopulaTerm(narsese, subject, predicate, Tense.FUTURE);
      case PREDICTIVE_IMPLICATION:
        return new ImplicationCopulaTerm(narsese, subject, predicate, Tense.FUTURE);
      case RETROSPECTIVE_IMPLICATION:
        return new ImplicationCopulaTerm(narsese, subject, predicate, Tense.PAST);
      default:
        throw new IllegalArgumentException("Unknow copula.");
    }
  }

  @Override
  public CompoundTermBuilder compoundTerm(Connector connector)
  {
    checkNotNull(connector, "connector");

    return new CompoundTermBuilderImpl(narsese, connector);
  }

  @Override
  public Constant constant(String name)
  {
    checkNotNull(name, "name");
    checkNotEmpty(name, "name");
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

    return new ConstantImpl(narsese, name);
  }

  @Override
  public IndependentVariable independentVariable(String name)
  {
    checkNotNull(name, "name");
    checkNotEmpty(name, "name");
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

    return new IndependentVariableImpl(narsese, name);
  }

  @Override
  public DependentVariable dependentVariable()
  {
    return new DependentVariableImpl(narsese, "", new ArrayList<>());
  }

  @Override
  public DependentVariableBuilder dependentVariable(String name)
  {
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

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
    checkArgument(NarseseChars.isWordValid(name), () -> "Invalide name: " + name);

    return new QueryVariableImpl(narsese, name);
  }
}
