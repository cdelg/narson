package org.narson.narsese.provider;

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

    sb.append(NarseseChars.CHAR_START_END_TRUTH);
    sb.append(f1);
    sb.append(NarseseChars.CHAR_TRUTH_SEPARATOR);
    sb.append(c1);
    sb.append(NarseseChars.CHAR_START_END_TRUTH);

    return sb.toString();
  }

  public TruthValue computeDeduction(TruthValue other)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();

    final double f = f1 * f2;

    return new TruthValueImpl(f, f * c1 * c2);
  }

  public TruthValue computeAnalogy(TruthValue other)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();

    return new TruthValueImpl(f1 * f2, f2 * c1 * c2);
  }

  public TruthValue computeResemblance(TruthValue other)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();

    return new TruthValueImpl(f1 * f2, (f1 + f2 - f1 * f2) * c1 * c2);
  }

  public TruthValue computeAbduction(TruthValue other, double evidentialHorizon)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();
    final double pw = f1 * f2 * c1 * c2;
    final double w = f1 * c1 * c2;

    return new TruthValueImpl(pw / w, w / (w + evidentialHorizon));
  }

  public TruthValue computeInduction(TruthValue other, double evidentialHorizon)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();
    final double pw = f1 * f2 * c1 * c2;
    final double w = f2 * c1 * c2;

    return new TruthValueImpl(pw / w, w / (w + evidentialHorizon));
  }

  public TruthValue computeExemplification(TruthValue other, double evidentialHorizon)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();
    final double w = f1 * f2 * c1 * c2;

    return new TruthValueImpl(1.0, w / (w + evidentialHorizon));
  }

  public TruthValue computeComparison(TruthValue other, double evidentialHorizon)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();
    final double pw = f1 * f2 * c1 * c2;
    final double w = (f1 + f2 - f1 * f2) * c1 * c2;

    return new TruthValueImpl(pw / w, w / (w + evidentialHorizon));
  }

  public TruthValue computeConversion(double evidentialHorizon)
  {
    final double w = f1 * c1;

    return new TruthValueImpl(1.0, w / (w + evidentialHorizon));
  }

  public TruthValue computeContraposition(double evidentialHorizon)
  {
    final double w = (1 - f1) * c1;

    return new TruthValueImpl(0.0, w / (w + evidentialHorizon));
  }

  public TruthValue computeNegation()
  {
    return new TruthValueImpl(1 - f1, c1);
  }

  public TruthValue computeIntersection(TruthValue other)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();

    return new TruthValueImpl(f1 * f2, c1 * c2);
  }

  public TruthValue computeUnion(TruthValue other)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();

    return new TruthValueImpl(1 - (1 - f1) * (1 - f2), c1 * c2);
  }

  public TruthValue computeDifference(TruthValue other)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();

    return new TruthValueImpl(f1 * (1 - f2), c1 * c2);
  }

  public TruthValue computeRevision(TruthValue other)
  {
    final double f2 = other.getFrequency();
    final double c2 = other.getConfidence();

    final double c1c2 = c1 * (1 - c2);
    final double c2c1 = c2 * (1 - c1);
    final double d = c1c2 + c2c1;

    return new TruthValueImpl((f1 * c1c2 + f2 * c2c1) / d, d / (c1c2 + c2c1 + (1 - c1) * (1 - c2)));
  }
}
