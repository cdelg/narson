package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * EvidenceAmount class represent an immutable amount of evidence which can be used to compute a
 * Narsese truth value or frequency interval.
 */
@ProviderType
public interface EvidenceAmount
{
  /**
   * Returns the amount of positive evidence.
   *
   * @return the amount of positive evidence
   */
  double getPositiveAmountOfEvidence();

  /**
   * Returns the amount of evidence.
   *
   * @return the amount of evidence
   */
  double getAmountOfEvidence();

  /**
   * Returns the the expectation value using the specified evidential horizon.
   *
   * @return the expectation value
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  double getExpectation(double evidentialHorizon) throws IllegalArgumentException;

  /**
   * Convert this amount of evidences to a truth value using the specified evidential horizon.
   *
   * @param evidentialHorizon the evidential horizon to use for the conversion, must be greater than
   *        0
   * @return a truth value
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  TruthValue toTruthValue(double evidentialHorizon) throws IllegalArgumentException;

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
  FrequencyInterval toFrequencyInterval(double evidentialHorizon) throws IllegalArgumentException;
}
