package org.narson.api.narsese;

import java.util.Collection;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A builder for creating {@link CompoundTerm} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface TermBuilder
{
  /**
   * Add a term to the list of terms that compose the compound term being built.
   *
   * @param term the term to add
   * @return this builder
   * @throws NullPointerException if the term is null
   */
  TermBuilder of(Term term) throws NullPointerException;

  /**
   * Add the terms to the list of terms that compose the compound term being built.
   *
   * @param terms the terms to add
   * @return this builder
   * @throws NullPointerException if terms is null or one of its elements
   */
  TermBuilder of(Term... terms) throws NullPointerException;

  /**
   * Add the terms to the list of terms that compose the compound term being built.
   *
   * @param terms the terms to add
   * @return this builder
   * @throws NullPointerException if terms is null or one of its elements
   */
  TermBuilder of(Collection<Term> terms) throws NullPointerException;

  /**
   * Returns a new {@link CompoundTerm} object conform to the state of this object builder.
   *
   * @return a new {@link CompoundTerm} object conform to the state of this object builder
   * @throws IllegalStateException if there is not enough terms for the current connector, if it is
   *         an extensional or intensional image and that the placeholder is not set or its position
   *         exceeds the number of terms + 1.
   */
  Term build() throws IllegalStateException;
}
