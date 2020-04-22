package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Goal class represents an immutable Narsese goal sentence.
 * <p>
 * A Goal can be obtained by using a {@link GoalBuilder}.
 *
 */
@ProviderType
public interface Goal extends Sentence
{
  /**
   * Returns the desire value of this goal.
   *
   * @return the desire value of this goal
   */
  TruthValue getDesireValue();

  /**
   * Convert this goal into a query by dropping the desire value.
   *
   * @return the derived query
   */
  Query toQuery();
}
