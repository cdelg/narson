package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import java.util.concurrent.atomic.AtomicReference;
import org.narson.api.narsese.EvidenceCount;
import org.narson.api.narsese.FrequencyInterval;
import org.narson.api.narsese.TruthValue;

final class FrequencyIntervalImpl implements FrequencyInterval
{
  private final double lower;
  private final double upper;
  private final double ignorance;
  private final AtomicReference<TruthValue> cachedTruthValue = new AtomicReference<>();
  private final AtomicReference<Double> cachedExpectation = new AtomicReference<>();

  public FrequencyIntervalImpl(double lower, double upper, TruthValue truthValue)
  {
    this.lower = lower;
    this.upper = upper;
    ignorance = upper - lower;
    cachedTruthValue.set(truthValue);
  }

  public FrequencyIntervalImpl(double lower, double upper)
  {
    this.lower = lower;
    this.upper = upper;
    ignorance = upper - lower;
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
    Double result = cachedExpectation.get();
    if (result == null)
    {
      result = (lower + upper) / 2.0;
      if (!cachedExpectation.compareAndSet(null, result))
      {
        return cachedExpectation.get();
      }
    }
    return result;
  }

  @Override
  public TruthValue toTruthValue()
  {
    TruthValue result = cachedTruthValue.get();
    if (result == null)
    {
      result = new TruthValueImpl(lower / (1 - ignorance), 1 - ignorance, this);
      if (!cachedTruthValue.compareAndSet(null, result))
      {
        return cachedTruthValue.get();
      }
    }
    return result;
  }

  @Override
  public EvidenceCount toEvidenceCount(long evidentialHorizon) throws IllegalArgumentException
  {
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");
    return new EvidenceCountImpl(Math.round(evidentialHorizon * lower / ignorance),
        Math.max(Math.round(evidentialHorizon * (1 - ignorance) / ignorance), 1L));
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

    sb.append(NarseseChars.CHAR_START_INTERVAL);
    sb.append(lower);
    sb.append(NarseseChars.CHAR_INTERVAL_SEPARATOR);
    sb.append(upper);
    sb.append(NarseseChars.CHAR_END_INTERVAL);

    return sb.toString();
  }
}
