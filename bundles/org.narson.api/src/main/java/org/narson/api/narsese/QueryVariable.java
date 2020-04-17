package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * QueryVariable class represents an immutable Narsese query variable.
 * <p>
 * A QueryVariable can be obtained from a {@link NarseseFactory}.
 *
 */
@ProviderType
public interface QueryVariable extends Term
{
  /**
   * Returns the name of this query variable. If the name is empty, it means that it is an anonymous
   * variable.
   *
   * @return the name of this query variable
   */
  String getName();

  /**
   * Returns true if this variable is anonymous. In this case, the {@link QueryVariable#getName}
   * method returns the empty string.
   *
   * @return true if this variable is anonymous
   */
  boolean isAnonymous();
}
