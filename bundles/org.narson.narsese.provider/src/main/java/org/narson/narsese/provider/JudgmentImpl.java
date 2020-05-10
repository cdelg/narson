package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import java.util.ArrayList;
import java.util.List;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Operation;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;

final class JudgmentImpl extends AbstractSentence implements Judgment
{
  private final Tense tense;
  private volatile Question cachedQuestion;
  private final TruthValueImpl truthValueImpl;
  private final AbstractTerm termImpl;

  public JudgmentImpl(Narsese narsese, Term statement, TruthValue truthValue, Tense tense)
  {
    super(narsese, ValueType.JUDGMENT, statement);
    this.tense = tense;
    termImpl = wrap(statement);
    truthValueImpl = wrap(truthValue);
  }

  @Override
  final public TruthValue getTruthValue()
  {
    return truthValueImpl;
  }

  @Override
  final public Tense getTense()
  {
    return tense;
  }

  @Override
  public Question toQuestion()
  {
    return cachedQuestion != null ? cachedQuestion
        : (cachedQuestion = new QuestionImpl(narsese, getStatement(), getTense()));
  }

  @Override
  final public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + truthValueImpl.hashCode();
    result = prime * result + tense.hashCode();
    return result;
  }

  @Override
  final public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof Judgment))
    {
      return false;
    }

    final Judgment other = (Judgment) obj;

    if (!truthValueImpl.equals(other.getTruthValue()))
    {
      return false;
    }

    if (!tense.equals(other.getTense()))
    {
      return false;
    }

    return true;
  }

  private TruthValueImpl wrap(TruthValue v)
  {
    return v instanceof TruthValueImpl ? (TruthValueImpl) v
        : new TruthValueImpl(v.getFrequency(), v.getConfidence());
  }

  private AbstractTerm wrap(Term t)
  {
    if (t instanceof AbstractTerm)
    {
      return (AbstractTerm) t;
    } else
    {
      switch (t.getValueType())
      {
        case COMPOUND_TERM:
          final CompoundTerm cm = t.asCompoundTerm();
          return new CompoundTermImpl(narsese, cm.getConnector(), cm.getTerms(),
              cm.getPlaceHolderPosition());
        case CONSTANT:
          return new ConstantImpl(narsese, t.asConstant().getName());
        case COPULA_TERM:
          final CopulaTerm c = t.asCopulaTerm();
          if (c.getCopula().isSymmetric())
          {
            if (c.getCopula().isFirstOrder())
            {
              return new SimilarityCopulaTerm(narsese, c.getSubject(), c.getPredicate(),
                  c.getTense());
            } else
            {
              return new EquivalenceCopulaTerm(narsese, c.getSubject(), c.getPredicate(),
                  c.getTense());
            }
          } else
          {
            if (c.getCopula().isFirstOrder())
            {
              return new InheritanceCopulaTerm(narsese, c.getSubject(), c.getPredicate(),
                  c.getTense());
            } else
            {
              return new ImplicationCopulaTerm(narsese, c.getSubject(), c.getPredicate(),
                  c.getTense());
            }
          }
        case DEPENDENT_VARIABLE:
          final DependentVariable d = t.asDependentVariable();
          return new DependentVariableImpl(narsese, d.getName(), d.getIndependentVariableNames());
        case INDEPENDENT_VARIABLE:
          return new IndependentVariableImpl(narsese, t.asIndependentVariable().getName());
        case OPERATION:
          final Operation o = t.asOperation();
          return new OperationImpl(narsese, o.getName(), o.getTerms());
        case QUERY_VARIABLE:
          return new QueryVariableImpl(narsese, t.asQueryVariable().getName());
        default:
          throw new IllegalStateException("BUG: Bad term, should be reported.");
      }
    }
  }

  @Override
  public Inference choose(Judgment otherJudgment, double razorParameter)
  {
    checkNotNull(otherJudgment, "otherJudgment");

    final Judgment result;

    if (getStatement().equals(otherJudgment.getStatement()))
    {
      if (getTruthValue().getConfidence() > otherJudgment.getTruthValue().getConfidence())
      {
        result = this;
      } else
      {
        result = otherJudgment;
      }
    } else
    {
      if (getTruthValue().getExpectation() * getStatement()
          .getSyntacticSimplicity(razorParameter) > otherJudgment.getTruthValue().getExpectation()
              * otherJudgment.getStatement().getSyntacticSimplicity(razorParameter))
      {
        result = this;
      } else
      {
        result = otherJudgment;
      }
    }

    return new DefaultInference(Inference.Type.CHOICE, result);
  }

  @Override
  public Inference revise(Judgment otherJudgment)
  {
    checkNotNull(otherJudgment, "otherJudgment");
    checkArgument(getStatement().equals(otherJudgment.getStatement()),
        "statements are not equals.");

    return new DefaultInference(Inference.Type.REVISION, nf.judgment(getStatement())
        .truthValue(truthValueImpl.computeRevision(otherJudgment.getTruthValue())).build());
  }

  @Override
  public List<Inference> reason(double evidentialHorizon)
  {
    final List<Inference> inferences = new ArrayList<>();

    termImpl.computeInferences(truthValueImpl, evidentialHorizon, inferences);

    return inferences;
  }

  @Override
  public List<Inference> reason(Judgment otherJudgment, double evidentialHorizon)
  {
    checkNotNull(otherJudgment, "otherJudgment");

    final List<Inference> inferences = new ArrayList<>();

    termImpl.computeInferences(truthValueImpl, wrap(otherJudgment.getStatement()),
        wrap(otherJudgment.getTruthValue()), evidentialHorizon, inferences);

    return inferences;
  }
}
