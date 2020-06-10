package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Constant class represents an immutable Narsese constant term, that is, a simple word.
 *
 */
@ProviderType
public interface ConstantTerm extends Term
{
  /**
   * Returns the name of this constant.
   *
   * @return the name of this constant
   */
  String getName();
}
