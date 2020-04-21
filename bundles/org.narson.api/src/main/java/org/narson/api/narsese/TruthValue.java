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
   * Returns the confidence of this truth value in the range (0,1).
   *
   * @return the confidence of this truth value in the range (0,1)
   */
  double getConfidence();

  /**
   * Returns the the expectation value.
   *
   * @return the expectation value
   */
  double getExpectation();

  /**
   * Apply the revision function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   */
  TruthValue applyRevision(TruthValue truthValue) throws NullPointerException;

  /**
   * Apply the negation function to this truth value.
   *
   * @return the resulting truth value
   */
  TruthValue applyNegation();

  /**
   * Apply the conversion function to this truth value.
   *
   * @return the resulting truth value
   */
  TruthValue applyConversion(double evidentialHorizon) throws IllegalArgumentException;

  /**
   * Apply the contraposition function to this truth value.
   *
   * @return the resulting truth value
   */
  TruthValue applyContraposition(double evidentialHorizon) throws IllegalArgumentException;

  /**
   * Apply the deduction function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   */
  TruthValue applyDeduction(TruthValue truthValue) throws NullPointerException;

  /**
   * Apply the analogy function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   */
  TruthValue applyAnalogy(TruthValue truthValue) throws NullPointerException;

  /**
   * Apply the resemblance function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   */
  TruthValue applyResemblance(TruthValue truthValue) throws NullPointerException;

  /**
   * Apply the abduction function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  TruthValue applyAbduction(TruthValue truthValue, double evidentialHorizon)
      throws IllegalArgumentException, NullPointerException;

  /**
   * Apply the induction function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  TruthValue applyInduction(TruthValue truthValue, double evidentialHorizon)
      throws IllegalArgumentException, NullPointerException;

  /**
   * Apply the exemplification function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  TruthValue applyExemplification(TruthValue truthValue, double evidentialHorizon)
      throws IllegalArgumentException, NullPointerException;

  /**
   * Apply the comparison function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   * @throws IllegalArgumentException if evidentialHorizon is less or equals to 0
   */
  TruthValue applyComparison(TruthValue truthValue, double evidentialHorizon)
      throws IllegalArgumentException, NullPointerException;

  /**
   * Apply the intersection function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   */
  TruthValue applyIntersection(TruthValue truthValue) throws NullPointerException;

  /**
   * Apply the union function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   */
  TruthValue applyUnion(TruthValue truthValue) throws NullPointerException;

  /**
   * Apply the difference function using this truth value and the one in argument.
   *
   * @return the resulting truth value
   * @throws NullPointerException if truthValue is null
   */
  TruthValue applyDifference(TruthValue truthValue) throws NullPointerException;

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
  EvidenceAmount toEvidenceAmount(double evidentialHorizon) throws IllegalArgumentException;
}
