package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * IndependentVariable class represents an immutable Narsese independent variable.
 * <p>
 * A IndependentVariable can be obtained from a {@link NarseseFactory}.
 *
 */
@ProviderType
public interface IndependentVariable extends Term
{
  /**
   * Returns the name of this independent variable.
   * 
   * @return the name of this independent variable
   */
  String getName();
}
