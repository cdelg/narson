package org.narson.narsese.provider.model;

import org.narson.api.narsese.EvidenceAmount;
import org.narson.api.narsese.FrequencyInterval;
import org.narson.api.narsese.TruthValue;

final class TruthValueImpl implements TruthValue
{
  private final double f1;
  private final double c1;
  private volatile FrequencyInterval cachedFrequencyInterval;
  private volatile Double cachedExpectation;

  public TruthValueImpl(double frequency, double confidence)
  {
    this(frequency, confidence, null);
  }

  public TruthValueImpl(double frequency, double confidence, FrequencyInterval frequencyInterval)
  {
    f1 = frequency;
    c1 = confidence;
    cachedFrequencyInterval = frequencyInterval;
  }

  @Override
  public double getFrequency()
  {
    return f1;
  }

  @Override
  public double getConfidence()
  {
    return c1;
  }

  @Override
  public double getExpectation()
  {
    return cachedExpectation != null ? cachedExpectation
        : (cachedExpectation = c1 * (f1 - 0.5) + 0.5);
  }

  @Override
  public FrequencyInterval toFrequencyInterval()
  {
    return cachedFrequencyInterval != null ? cachedFrequencyInterval
        : (cachedFrequencyInterval = new FrequencyIntervalImpl(f1 * c1, 1 - c1 * (1 - f1), this));
  }

  @Override
  public EvidenceAmount toEvidenceAmount(double evidentialHorizon)
  {
    return new EvidenceAmountImpl(evidentialHorizon * f1 * c1 / (1 - c1),
        evidentialHorizon * c1 / (1 - c1));
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(c1);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(f1);
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

    if (!(obj instanceof TruthValue))
    {
      return false;
    }

    final TruthValue other = (TruthValue) obj;

    if (Double.doubleToLongBits(c1) != Double.doubleToLongBits(other.getConfidence()))
    {
      return false;
    }

    if (Double.doubleToLongBits(f1) != Double.doubleToLongBits(other.getFrequency()))
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();

    sb.append('%');
    sb.append(f1);
    sb.append(';');
    sb.append(c1);
    sb.append('%');

    return sb.toString();
  }
}
