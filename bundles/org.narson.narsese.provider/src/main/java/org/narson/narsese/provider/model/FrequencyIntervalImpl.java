package org.narson.narsese.provider.model;

import org.narson.api.narsese.EvidenceAmount;
import org.narson.api.narsese.FrequencyInterval;
import org.narson.api.narsese.TruthValue;

final class FrequencyIntervalImpl implements FrequencyInterval
{
  private final double lower;
  private final double upper;
  private final double ignorance;
  private volatile TruthValue cachedTruthValue;
  private volatile Double cachedExpectation;

  public FrequencyIntervalImpl(double lower, double upper, TruthValue truthValue)
  {
    this.lower = lower;
    this.upper = upper;
    ignorance = upper - lower;
    cachedTruthValue = truthValue;
  }

  public FrequencyIntervalImpl(double lower, double upper)
  {
    this(lower, upper, null);
  }

  @Override
  public double getLowerBound()
  {
    return lower;
  }

  @Override
  public double getUpperBound()
  {
    return upper;
  }

  @Override
  public double getIgnorance()
  {
    return ignorance;
  }

  @Override
  public double getExpectation()
  {
    return cachedExpectation != null ? cachedExpectation
        : (cachedExpectation = (lower + upper) / 2.0);
  }

  @Override
  public TruthValue toTruthValue()
  {
    return cachedTruthValue != null ? cachedTruthValue
        : (cachedTruthValue = new TruthValueImpl(lower / (1 - ignorance), 1 - ignorance, this));
  }

  @Override
  public EvidenceAmount toEvidenceAmount(double evidentialHorizon)
  {
    return new EvidenceAmountImpl(evidentialHorizon * lower / ignorance,
        evidentialHorizon * (1 - ignorance) / ignorance);
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(lower);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(upper);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (!(obj instanceof FrequencyInterval))
    {
      return false;
    }

    final FrequencyInterval other = (FrequencyInterval) obj;

    if (Double.doubleToLongBits(lower) != Double.doubleToLongBits(other.getLowerBound()))
    {
      return false;
    }

    if (Double.doubleToLongBits(upper) != Double.doubleToLongBits(other.getUpperBound()))
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();

    sb.append('[');
    sb.append(lower);
    sb.append(';');
    sb.append(upper);
    sb.append(']');

    return sb.toString();
  }
}
