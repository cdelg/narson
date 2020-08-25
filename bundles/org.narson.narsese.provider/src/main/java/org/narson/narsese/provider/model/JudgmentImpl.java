package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import java.util.List;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.InferenceType;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;
import org.narson.narsese.provider.rules.RuleMachine;

final class JudgmentImpl extends AbstractSentence implements Judgment
{
  private final Tense tense;
  private final RuleMachine ruleMachine;
  private final TruthValue truthValue;
  private volatile Question cachedQuestion;

  public JudgmentImpl(Narsese narsese, RuleMachine ruleMachine, Term statement,
      TruthValue truthValue, Tense tense)
  {
    super(narsese, ValueType.JUDGMENT, statement);
    this.ruleMachine = ruleMachine;
    this.truthValue = truthValue;
    this.tense = tense;
  }

  @Override
  final public TruthValue getTruthValue()
  {
    return truthValue;
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
    result = prime * result + truthValue.hashCode();
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

    if (!truthValue.equals(other.getTruthValue()))
    {
      return false;
    }

    if (!tense.equals(other.getTense()))
    {
      return false;
    }

    return true;
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

    return new DefaultInference(InferenceType.CHOICE, result);
  }

  @Override
  public Inference revise(Judgment otherJudgment)
  {
    checkNotNull(otherJudgment, "otherJudgment");
    checkArgument(getStatement().equals(otherJudgment.getStatement()),
        "statements are not equals.");

    return new DefaultInference(InferenceType.REVISION,
        nf.judgment(getStatement()).truthValue(
            new BiTruthValueFunction(truthValue, otherJudgment.getTruthValue()).computeRevision())
            .build());
  }

  @Override
  public List<Inference> reason(double evidentialHorizon)
  {
    return ruleMachine.run(getStatement(),
        new TruthValueFunction(getTruthValue(), evidentialHorizon));
  }

  @Override
  public List<Inference> reason(Judgment otherJudgment, double evidentialHorizon)
  {
    checkNotNull(otherJudgment, "otherJudgment");

    return ruleMachine.run(getStatement(), otherJudgment.getStatement(), new BiTruthValueFunction(
        getTruthValue(), otherJudgment.getTruthValue(), evidentialHorizon));
  }
}
