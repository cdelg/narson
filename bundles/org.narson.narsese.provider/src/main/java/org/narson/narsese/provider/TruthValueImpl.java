package org.narson.narsese.provider;

import org.narson.api.narsese.TruthValue;

final class TruthValueImpl implements TruthValue
{
  private final double frequency;
  private final double confidence;

  public TruthValueImpl(double frequency, double confidence)
  {
    this.frequency = frequency;
    this.confidence = confidence;
  }

  @Override
  public double getFrequency()
  {
    return frequency;
  }

  @Override
  public double getConfidence()
  {
    return confidence;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(confidence);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(frequency);
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

    if (Double.doubleToLongBits(confidence) != Double.doubleToLongBits(other.getConfidence()))
    {
      return false;
    }

    if (Double.doubleToLongBits(frequency) != Double.doubleToLongBits(other.getFrequency()))
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();

    sb.append(NarseseChars.CHAR_START_END_TRUTH);
    sb.append(frequency);
    sb.append(NarseseChars.CHAR_TRUTH_SEPARATOR);
    sb.append(confidence);
    sb.append(NarseseChars.CHAR_START_END_TRUTH);

    return sb.toString();
  }
}
