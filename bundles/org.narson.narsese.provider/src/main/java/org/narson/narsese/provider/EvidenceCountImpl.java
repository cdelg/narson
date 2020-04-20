package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import org.narson.api.narsese.EvidenceCount;
import org.narson.api.narsese.FrequencyInterval;
import org.narson.api.narsese.TruthValue;

final class EvidenceCountImpl implements EvidenceCount
{
  private final long positiveEvidenceCount;
  private final long evidenceCount;

  public EvidenceCountImpl(long positiveEvidenceCount, long evidenceCount)
  {
    this.positiveEvidenceCount = positiveEvidenceCount;
    this.evidenceCount = evidenceCount;
  }

  @Override
  public long getPositiveEvidenceCount()
  {
    return positiveEvidenceCount;
  }

  @Override
  public long getEvidenceCount()
  {
    return evidenceCount;
  }

  @Override
  public TruthValue toTruthValue(long evidentialHorizon) throws IllegalArgumentException
  {
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");
    return new TruthValueImpl((double) positiveEvidenceCount / (double) evidenceCount,
        (double) evidenceCount / (double) (evidenceCount + evidentialHorizon));
  }

  @Override
  public FrequencyInterval toFrequencyInterval(long evidentialHorizon)
      throws IllegalArgumentException
  {
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");
    return new FrequencyIntervalImpl(
        (double) positiveEvidenceCount / (double) (evidenceCount + evidentialHorizon),
        (double) (positiveEvidenceCount + evidentialHorizon)
            / (double) (evidenceCount + evidentialHorizon));
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = positiveEvidenceCount;
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = evidenceCount;
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

    if (!(obj instanceof EvidenceCount))
    {
      return false;
    }

    final EvidenceCount other = (EvidenceCount) obj;

    if (positiveEvidenceCount != other.getPositiveEvidenceCount())
    {
      return false;
    }

    if (evidenceCount != other.getEvidenceCount())
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
    sb.append(positiveEvidenceCount);
    sb.append(NarseseChars.CHAR_EVIDENCE_COUNT_SEPARATOR);
    sb.append(evidenceCount);
    sb.append(NarseseChars.CHAR_END_EVIDENCE_COUNT);

    return sb.toString();
  }
}
