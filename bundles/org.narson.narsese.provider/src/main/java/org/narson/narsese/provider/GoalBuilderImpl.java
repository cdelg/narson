package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import org.narson.api.narsese.Goal;
import org.narson.api.narsese.GoalBuilder;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;

final class GoalBuilderImpl implements GoalBuilder
{
  private final Narsese narsese;
  private final Term statement;
  private TruthValue desireValue;

  public GoalBuilderImpl(Narsese narsese, Term statement, TruthValue defaultDesireValue)
  {
    this.narsese = narsese;
    this.statement = statement;
    desireValue = defaultDesireValue;
  }

  @Override
  public GoalBuilderImpl desireValue(double frequency, double confidence)
  {
    checkArgument(0 <= frequency, "frequency < 0");
    checkArgument(frequency <= 1, "frequency > 1");
    checkArgument(0 < confidence, "confidence <= 0");
    checkArgument(confidence < 1, "confidence >= 1");

    desireValue = new TruthValueImpl(frequency, confidence);
    return this;
  }

  @Override
  public GoalBuilder desireValue(TruthValue desireValue) throws NullPointerException
  {
    checkNotNull(desireValue, "desireValue");

    this.desireValue = desireValue;
    return this;
  }

  @Override
  public Goal build()
  {
    return new GoalImpl(narsese, statement, desireValue);
  }
}
