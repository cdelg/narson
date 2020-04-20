package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * FrequencyInterval class represent a Narsese frequency interval composed of a lower bound and an
 * upper bound.
 *
 */
@ProviderType
public interface FrequencyInterval
{
  /**
   * Returns the lower bound of this frequency interval.
   *
   * @return the lower bound of this frequency interval
   */
  double getLowerBound();

  /**
   * Returns the upper bound of this frequency interval.
   *
   * @return the upper bound of this frequency interval
   */
  double getUpperBound();

  /**
   * Returns the ignorance value of this frequency interval. Equivalent to upper - lower.
   *
   * @return the ignorance value of this frequency interval
   */
  double getIgnorance();

  /**
   * Convert this frequency interval to a truth value.
   *
   * @return the truth value version of this frequency interval
   */
  TruthValue toTruthValue();

  /**
   * Convert this frequency interval to the amount of evidences using the specified evidential
   * horizon.
   *
   * @param evidentialHorizon the evidential horizon to use for the conversion, must be greater than
   *        0
   * @return the amount of evidences of this frequency interval
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  EvidenceCount toEvidenceCount(long evidentialHorizon) throws IllegalArgumentException;
}
