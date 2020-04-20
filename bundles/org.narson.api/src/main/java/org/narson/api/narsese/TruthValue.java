package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * TruthValue class represent a Narsese truth value composed of a frequency and a confidence value.
 */
@ProviderType
public interface TruthValue
{
  /**
   * Returns the frequency of this truth value in the range [0,1].
   *
   * @return the frequency of this truth value in the range [0,1]
   */
  double getFrequency();

  /**
   * Returns the confidence of this truth value in the range ]0,1[.
   *
   * @return the confidence of this truth value in the range ]0,1[
   */
  double getConfidence();

  /**
   * Apply the revision function to this truth value and the one in argument.
   *
   * @return a revised truth value
   */
  TruthValue revise(TruthValue truthValue) throws NullPointerException;

  /**
   * Convert this truth value to a frequency interval.
   *
   * @return the frequency interval of this truth value
   */
  FrequencyInterval toFrequencyInterval();

  /**
   * Convert this truth value to the amount of evidences using the specified evidential horizon.
   *
   * @param evidentialHorizon the evidential horizon to use for the conversion, must be greater than
   *        0
   * @return the amount of evidences of this truth value
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  EvidenceCount toEvidenceCount(long evidentialHorizon) throws IllegalArgumentException;
}
