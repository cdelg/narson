package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A builder for creating {@link Goal} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface GoalBuilder
{
  /**
   * Set a desire value for the goal being built.
   * <p>
   * The frequency must be a double in [0.0,1.0] and the confidence must be a double in ]0.0,1.0[.
   *
   * @param frequency the frequency of the desire value
   * @param confidence the confidence of the desire value
   * @return this builder
   * @throws IllegalArgumentException if the frequency is not in [0.0,1.0] of the confidence is not
   *         in ]0.0,1.0[
   */
  GoalBuilder desireValue(double frequency, double confidence) throws IllegalArgumentException;

  /**
   * Set the given desire value for the goal being built.
   *
   * @param desireValue the desire value
   * @return this builder
   * @throws IllegalArgumentException if desireValue is null
   */
  GoalBuilder desireValue(TruthValue desireValue) throws NullPointerException;

  /**
   * Returns a new {@link Goal} object conform to the state of this object builder.
   *
   * @return a new {@link Goal} object conform to the state of this object builder
   */
  Goal build();
}
