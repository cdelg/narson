package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Term class represents an immutable Narsese term.
 *
 * <p>
 * A Narsese term is one of the following: a {@link Constant}, an {@link Operation}, a
 * {@link Relation}, a {@link CompoundTerm} term, an {@link IndependentVariable}, a
 * {@link DependentVariable} or a {@link QueryVariable}.
 */
@ProviderType
public interface Term extends NarseseValue
{
  /**
   * Returns the Narsese text for this term.
   *
   * @return the Narsese text for this term
   */
  @Override
  String toString();
}
