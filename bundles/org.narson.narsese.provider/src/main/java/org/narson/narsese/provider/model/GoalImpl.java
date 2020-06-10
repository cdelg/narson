package org.narson.narsese.provider.model;

import org.narson.api.narsese.Goal;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Query;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;

final class GoalImpl extends AbstractSentence implements Goal
{
  private final TruthValue desireValue;
  private volatile Query cachedQuery;

  public GoalImpl(Narsese narsese, Term statement, TruthValue desireValue)
  {
    super(narsese, ValueType.GOAL, statement);
    this.desireValue = desireValue;
  }

  @Override
  public TruthValue getDesireValue()
  {
    return desireValue;
  }

  @Override
  public Query toQuery()
  {
    return cachedQuery != null ? cachedQuery
        : (cachedQuery = new QueryImpl(narsese, getStatement()));
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
