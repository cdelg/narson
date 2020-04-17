package org.narson.narsese.provider;

import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;

final class JudgmentImpl extends AbstractSentence implements Judgment
{
  private final TruthValue truthValue;
  private final Tense tense;

  public JudgmentImpl(int bufferSize, int prefixThreshold, Term statement, TruthValue truthValue,
      Tense tense)
  {
    super(ValueType.JUDGMENT, bufferSize, prefixThreshold, statement);
    this.truthValue = truthValue;
    this.tense = tense;
  }

  @Override
  public TruthValue getTruthValue()
  {
    return truthValue;
  }

  @Override
  public Tense getTense()
  {
    return tense;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + truthValue.hashCode();
    result = prime * result + tense.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
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
}
