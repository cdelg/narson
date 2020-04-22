package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Judgment class represents an immutable Narsese judgment sentence.
 * <p>
 * A Judgment can be obtained by using a {@link JudgmentBuilder}.
 *
 */
@ProviderType
public interface Judgment extends Sentence
{
  /**
   * Returns the truth value of this judgment.
   *
   * @return the truth value of this judgment
   */
  TruthValue getTruthValue();

  /**
   * Returns the tense of this judgment.
   *
   * @return the tense of this judgment
   */
  Tense getTense();

  /**
   * Convert this judgment into a question by dropping the truth value.
   *
   * @return the derived question
   */
  Question toQuestion();
}
