package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import java.util.concurrent.atomic.AtomicReference;
import org.narson.api.narsese.EvidenceAmount;
import org.narson.api.narsese.FrequencyInterval;
import org.narson.api.narsese.TruthValue;

final class TruthValueImpl implements TruthValue
{
  private final double frequency;
  private final double confidence;
  private final AtomicReference<FrequencyInterval> cachedFrequencyInterval =
      new AtomicReference<>();
  private final AtomicReference<Double> cachedExpectation = new AtomicReference<>();

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
  public double getExpectation()
  {
    Double result = cachedExpectation.get();
    if (result == null)
    {
      result = confidence * (frequency - 0.5) + 0.5;
      if (!cachedExpectation.compareAndSet(null, result))
      {
        return cachedExpectation.get();
      }
    }
    return result;
  }

  @Override
  public TruthValue applyRevision(TruthValue truthValue) throws NullPointerException
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
  public TruthValue applyNegation()
  {
    return new TruthValueImpl(1 - frequency, confidence);
  }

  @Override
  public TruthValue applyConversion(double evidentialHorizon)
  {
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");

    return new EvidenceAmountImpl(frequency * confidence, frequency * confidence)
        .toTruthValue(evidentialHorizon);
  }

  @Override
  public TruthValue applyContraposition(double evidentialHorizon)
  {
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");

    return new EvidenceAmountImpl(0,
        (1 - frequency) * confidence / ((1 - frequency) * confidence + evidentialHorizon))
            .toTruthValue(evidentialHorizon);
  }

  @Override
  public TruthValue applyDeduction(TruthValue truthValue)
  {
    checkNotNull(truthValue, "truthValue");

    return new TruthValueImpl(frequency * truthValue.getFrequency(),
        frequency * truthValue.getFrequency() * confidence * truthValue.getConfidence());
  }

  @Override
  public TruthValue applyAnalogy(TruthValue truthValue)
  {
    checkNotNull(truthValue, "truthValue");

    return new TruthValueImpl(frequency * truthValue.getFrequency(),
        truthValue.getFrequency() * confidence * truthValue.getConfidence());
  }

  @Override
  public TruthValue applyResemblance(TruthValue truthValue)
  {
    checkNotNull(truthValue, "truthValue");

    return new TruthValueImpl(frequency * truthValue.getFrequency(),
        (frequency + truthValue.getFrequency() - frequency * truthValue.getFrequency()) * confidence
            * truthValue.getConfidence());
  }

  @Override
  public TruthValue applyAbduction(TruthValue truthValue, double evidentialHorizon)
  {
    checkNotNull(truthValue, "truthValue");
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");

    return new EvidenceAmountImpl(
        frequency * truthValue.getFrequency() * confidence * truthValue.getConfidence(),
        frequency * confidence * truthValue.getConfidence()).toTruthValue(evidentialHorizon);
  }

  @Override
  public TruthValue applyInduction(TruthValue truthValue, double evidentialHorizon)
  {
    checkNotNull(truthValue, "truthValue");
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");

    return new EvidenceAmountImpl(
        frequency * truthValue.getFrequency() * confidence * truthValue.getConfidence(),
        truthValue.getFrequency() * confidence * truthValue.getConfidence())
            .toTruthValue(evidentialHorizon);
  }

  @Override
  public TruthValue applyExemplification(TruthValue truthValue, double evidentialHorizon)
  {
    checkNotNull(truthValue, "truthValue");
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");

    return new EvidenceAmountImpl(
        frequency * truthValue.getFrequency() * confidence * truthValue.getConfidence(),
        frequency * truthValue.getFrequency() * confidence * truthValue.getConfidence())
            .toTruthValue(evidentialHorizon);
  }

  @Override
  public TruthValue applyComparison(TruthValue truthValue, double evidentialHorizon)
  {
    checkNotNull(truthValue, "truthValue");
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");

    return new EvidenceAmountImpl(
        frequency * truthValue.getFrequency() * confidence * truthValue.getConfidence(),
        (frequency + truthValue.getFrequency() - frequency * truthValue.getFrequency()) * confidence
            * truthValue.getConfidence()).toTruthValue(evidentialHorizon);
  }

  @Override
  public TruthValue applyIntersection(TruthValue truthValue)
  {
    checkNotNull(truthValue, "truthValue");

    return new TruthValueImpl(frequency * truthValue.getFrequency(),
        confidence * truthValue.getConfidence());
  }

  @Override
  public TruthValue applyUnion(TruthValue truthValue)
  {
    checkNotNull(truthValue, "truthValue");

    return new TruthValueImpl(1 - (1 - frequency) * (1 - truthValue.getFrequency()),
        confidence * truthValue.getConfidence());
  }

  @Override
  public TruthValue applyDifference(TruthValue truthValue)
  {
    checkNotNull(truthValue, "truthValue");

    return new TruthValueImpl(frequency * (1 - truthValue.getFrequency()),
        confidence * truthValue.getConfidence());
  }

  @Override
  public FrequencyInterval toFrequencyInterval()
  {
    FrequencyInterval result = cachedFrequencyInterval.get();
    if (result == null)
    {
      result =
          new FrequencyIntervalImpl(frequency * confidence, 1 - confidence * (1 - frequency), this);
      if (!cachedFrequencyInterval.compareAndSet(null, result))
      {
        return cachedFrequencyInterval.get();
      }
    }
    return result;
  }

  @Override
  public EvidenceAmount toEvidenceAmount(double evidentialHorizon) throws IllegalArgumentException
  {
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");
    return new EvidenceAmountImpl(evidentialHorizon * frequency * confidence / (1 - confidence),
        evidentialHorizon * confidence / (1 - confidence));
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
