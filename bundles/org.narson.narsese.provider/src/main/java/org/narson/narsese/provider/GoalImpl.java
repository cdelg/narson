package org.narson.narsese.provider;

import org.narson.api.narsese.Goal;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;

final class GoalImpl extends AbstractSentence implements Goal
{
  private final TruthValue desireValue;

  public GoalImpl(int bufferSize, int prefixThreshold, Term statement, TruthValue desireValue)
  {
    super(ValueType.GOAL, bufferSize, prefixThreshold, statement);
    this.desireValue = desireValue;
  }

  @Override
  public TruthValue getDesireValue()
  {
    return desireValue;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + desireValue.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }
    if (!(obj instanceof Goal))
    {
      return false;
    }

    final Goal other = (Goal) obj;

    if (!desireValue.equals(other.getDesireValue()))
    {
      return false;
    }

    return true;
  }
}
