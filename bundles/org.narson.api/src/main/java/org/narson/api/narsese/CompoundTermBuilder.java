package org.narson.api.narsese;

import java.util.Collection;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A builder for creating {@link CompoundTerm} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface CompoundTermBuilder
{
  /**
   * Add a term to the list of terms that compose the compound term being built.
   *
   * @param term the term to add
   * @return this builder
   * @throws NullPointerException if the term is null
   */
  CompoundTermBuilder of(Term term) throws NullPointerException;

  /**
   * Add the terms to the list of terms that compose the compound term being built.
   *
   * @param terms the terms to add
   * @return this builder
   * @throws NullPointerException if terms is null or one of its elements
   */
  CompoundTermBuilder of(Term... terms) throws NullPointerException;

  /**
   * Add the terms to the list of terms that compose the compound term being built.
   *
   * @param terms the terms to add
   * @return this builder
   * @throws NullPointerException if terms is null or one of its elements
   */
  CompoundTermBuilder of(Collection<Term> terms) throws NullPointerException;

  /**
   * Set the position of the placeholder at the current position in the term list. The position must
   * be greater or equals to 2 and less or equals to the number of terms + 1. That is, this method
   * can start to be called just after inserting the first term.
   * <p>
   * This method is only effective for the {@link Connector#EXTENSIONAL_IMAGE} and the
   * {@link Connector#INTENSIONAL_IMAGE} connectors.
   *
   * @return this builder
   * @throws IllegalStateException if there is no term yet in the compound term being build
   */
  CompoundTermBuilder setPlaceHolderPosition() throws IllegalStateException;

  /**
   * Set the position of the placeholder at the specified position. The position must be greater or
   * equals to 2 and less or equals to the number of terms + 1.
   *
   * @param position the position value
   * @return this builder
   * @throws IllegalArgumentException if the position is less than 2
   */
  CompoundTermBuilder setPlaceHolderPosition(int position) throws IllegalArgumentException;

  /**
   * Returns a new {@link CompoundTerm} object conform to the state of this object builder.
   *
   * @return a new {@link CompoundTerm} object conform to the state of this object builder
   * @throws IllegalStateException if there is not enough terms for the current connector, if it is
   *         an extensional or intensional image and that the placeholder is not set or its position
   *         exceeds the number of terms + 1.
   */
  CompoundTerm build() throws IllegalStateException;
}
