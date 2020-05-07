package org.narson.narsese.provider;

import org.narson.api.narsese.EvidenceAmount;
import org.narson.api.narsese.FrequencyInterval;
import org.narson.api.narsese.TruthValue;

final class EvidenceAmountImpl implements EvidenceAmount
{
  private final double positiveAmountOfEvidence;
  private final double amountOfEvidence;

  public EvidenceAmountImpl(double positiveAmountOfEvidence, double amountOfEvidence)
  {
    this.positiveAmountOfEvidence = positiveAmountOfEvidence;
    this.amountOfEvidence = amountOfEvidence;
  }

  @Override
  public double getPositiveAmountOfEvidence()
  {
    return positiveAmountOfEvidence;
  }

  @Override
  public double getAmountOfEvidence()
  {
    return amountOfEvidence;
  }

  @Override
  public double getExpectation(double evidentialHorizon)
  {
    return (positiveAmountOfEvidence + evidentialHorizon / 2.0)
        / (amountOfEvidence + evidentialHorizon);
  }

  @Override
  public TruthValue toTruthValue(double evidentialHorizon)
  {
    return new TruthValueImpl(positiveAmountOfEvidence / amountOfEvidence,
        amountOfEvidence / (amountOfEvidence + evidentialHorizon));
  }

  @Override
  public FrequencyInterval toFrequencyInterval(double evidentialHorizon)
  {
    return new FrequencyIntervalImpl(
        positiveAmountOfEvidence / (amountOfEvidence + evidentialHorizon),
        (positiveAmountOfEvidence + evidentialHorizon) / (amountOfEvidence + evidentialHorizon));
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(positiveAmountOfEvidence);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(amountOfEvidence);
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

    if (!(obj instanceof EvidenceAmount))
    {
      return false;
    }

    final EvidenceAmount other = (EvidenceAmount) obj;

    if (Double.doubleToLongBits(positiveAmountOfEvidence) != Double
        .doubleToLongBits(other.getPositiveAmountOfEvidence()))
    {
      return false;
    }

    if (Double.doubleToLongBits(amountOfEvidence) != Double
        .doubleToLongBits(other.getAmountOfEvidence()))
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();

    sb.append(NarseseChars.CHAR_START_EVIDENCE_COUNT);
    sb.append(positiveAmountOfEvidence);
    sb.append(NarseseChars.CHAR_EVIDENCE_COUNT_SEPARATOR);
    sb.append(amountOfEvidence);
    sb.append(NarseseChars.CHAR_END_EVIDENCE_COUNT);

    return sb.toString();
  }
}
