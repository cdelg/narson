package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Question class represents an immutable Narsese question sentence.
 * <p>
 * A Question can be obtained by using a {@link QuestionBuilder}.
 *
 */
@ProviderType
public interface Question extends Sentence
{
  /**
   * Returns the tense of this question.
   *
   * @return the tense of this question
   */
  Tense getTense();
}
