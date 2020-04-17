package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Sentence class represents an immutable Narsese sentence. A sentence is all about a statement.
 *
 * <p>
 * A Narsese sentence is one of the following: a {@link Judgment}, a {@link Goal}, a
 * {@link Question} or a {@link Query}.
 */
@ProviderType
public interface Sentence extends NarseseValue
{
  /**
   * Returns the statement of this sentence.
   *
   * @return the statement of this sentence
   */
  Term getStatement();
}
