package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * TruthValue class represent a Narsese truth value composed of a frequency and a confidence value.
 *
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
}
