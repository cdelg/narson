package org.narson.api.narsese;

import java.util.Collection;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A builder for creating {@link CompoundTerm} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface ImageTermBuilder extends TermBuilder
{
  @Override
  ImageTermBuilder of(Term term) throws NullPointerException;

  @Override
  ImageTermBuilder of(Term... terms) throws NullPointerException;

  @Override
  ImageTermBuilder of(Collection<Term> terms) throws NullPointerException;

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
  ImageTermBuilder setPlaceHolderPosition() throws IllegalStateException;

  /**
   * Set the position of the placeholder at the specified position. The position must be greater or
   * equals to 2 and less or equals to the number of terms + 1.
   *
   * @param position the position value
   * @return this builder
   * @throws IllegalArgumentException if the position is less than 2
   */
  ImageTermBuilder setPlaceHolderPosition(int position) throws IllegalArgumentException;

  @Override
  ImageTerm build() throws IllegalStateException;
}
