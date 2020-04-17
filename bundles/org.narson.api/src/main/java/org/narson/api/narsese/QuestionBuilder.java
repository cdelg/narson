package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A builder for creating {@link Question} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface QuestionBuilder
{
  /**
   * Set the tense for the question being built.
   *
   * @param tense the tense to set
   * @return this builder
   * @throws NullPointerException if the tense is null
   */
  QuestionBuilder tense(Tense tense) throws NullPointerException;

  /**
   * Returns a new {@link Question} object conform to the state of this object builder.
   *
   * @return a new {@link Question} object conform to the state of this object builder
   */
  Question build();
}
