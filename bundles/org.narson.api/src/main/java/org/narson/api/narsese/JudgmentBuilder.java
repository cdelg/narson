package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A builder for creating {@link Judgment} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface JudgmentBuilder
{
  /**
   * Set the tense for the judgment being built.
   *
   * @param tense the tense to set
   * @return this builder
   * @throws NullPointerException if the tense is null
   */
  JudgmentBuilder tense(Tense tense) throws NullPointerException;

  /**
   * Set a truth value for the judgment being built.
   * <p>
   * The frequency must be a double in [0.0,1.0] and the confidence must be a double in (0.0,1.0).
   *
   * @param frequency the frequency of the truth value
   * @param confidence the confidence of the truth value
   * @return this builder
   * @throws IllegalArgumentException if the frequency is not in [0.0,1.0] of the confidence is not
   *         in (0.0,1.0)
   */
  JudgmentBuilder truthValue(double frequency, double confidence) throws IllegalArgumentException;

  /**
   * Set the given truth value for the judgment being built.
   *
   * @param truthValue the truth value
   * @return this builder
   * @throws IllegalArgumentException if truthValue is null
   */
  JudgmentBuilder truthValue(TruthValue truthValue) throws NullPointerException;

  /**
   * Returns a new {@link Judgment} object conform to the state of this object builder.
   *
   * @return a new {@link Judgment} object conform to the state of this object builder
   */
  Judgment build();
}
