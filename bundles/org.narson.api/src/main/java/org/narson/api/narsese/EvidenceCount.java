package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * EvidenceCount class represent the amount of evidences which can be used to compute a Narsese
 * truth value or frequency interval.
 */
@ProviderType
public interface EvidenceCount
{
  /**
   * Returns the total amount of positive evidences.
   *
   * @return the total amount of positive evidences
   */
  long getPositiveEvidenceCount();

  /**
   * Returns the total amount of evidences.
   *
   * @return the total amount of evidences
   */
  long getEvidenceCount();

  /**
   * Convert this amount of evidences to a truth value using the specified evidential horizon.
   *
   * @param evidentialHorizon the evidential horizon to use for the conversion, must be greater than
   *        0
   * @return a truth value
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  TruthValue toTruthValue(long evidentialHorizon) throws IllegalArgumentException;

  /**
   *
   * Convert this amount of evidences to a frequency interval using the specified evidential
   * horizon.
   *
   * @param evidentialHorizon the evidential horizon to use for the conversion, must be greater than
   *        0
   * @return a frequency interval
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  FrequencyInterval toFrequencyInterval(long evidentialHorizon) throws IllegalArgumentException;
}
