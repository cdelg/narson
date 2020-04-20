package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import java.util.concurrent.atomic.AtomicReference;
import org.narson.api.narsese.EvidenceCount;
import org.narson.api.narsese.FrequencyInterval;
import org.narson.api.narsese.TruthValue;

final class TruthValueImpl implements TruthValue
{
  private final double frequency;
  private final double confidence;
  private final AtomicReference<FrequencyInterval> cachedFrequencyInterval =
      new AtomicReference<>();

  public TruthValueImpl(double frequency, double confidence)
  {
    this.frequency = frequency;
    this.confidence = confidence;
  }

  public TruthValueImpl(double frequency, double confidence, FrequencyInterval frequencyInterval)
  {
    this.frequency = frequency;
    this.confidence = confidence;
    cachedFrequencyInterval.set(frequencyInterval);
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
  public TruthValue revise(TruthValue truthValue) throws NullPointerException
  {
    checkNotNull(truthValue, "truthValue");

    return new TruthValueImpl(
        (frequency * confidence * (1 - truthValue.getConfidence())
            + truthValue.getFrequency() * truthValue.getConfidence() * (1 - confidence))
            / (confidence * (1 - truthValue.getConfidence())
                + truthValue.getConfidence() * (1 - confidence)),
        (confidence * (1 - truthValue.getConfidence())
            + truthValue.getConfidence() * (1 - confidence))
            / (confidence * (1 - truthValue.getConfidence())
                + truthValue.getConfidence() * (1 - confidence)
                + (1 - confidence) * (1 - truthValue.getConfidence())));
  }

  @Override
  public FrequencyInterval toFrequencyInterval()
  {
    FrequencyInterval result = cachedFrequencyInterval.get();
    if (result == null)
    {
      result = new FrequencyIntervalImpl(frequency * confidence, (1 - confidence) * (1 - frequency),
          this);
      if (!cachedFrequencyInterval.compareAndSet(null, result))
      {
        return cachedFrequencyInterval.get();
      }
    }
    return result;
  }

  @Override
  public EvidenceCount toEvidenceCount(long evidentialHorizon) throws IllegalArgumentException
  {
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");
    return new EvidenceCountImpl(
        Math.round(evidentialHorizon * frequency * confidence / (1 - confidence)),
        Math.max(Math.round(evidentialHorizon * confidence / (1 - confidence)), 1L));
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
