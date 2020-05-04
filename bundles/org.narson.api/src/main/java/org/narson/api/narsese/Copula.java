package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A copula that links two terms in a {@link CopulaTerm}.
 *
 */
@ProviderType
public enum Copula
{
  INHERITANCE(true, false),
  SIMILARITY(true, true),
  IMPLICATION(false, false),
  EQUIVALENCE(false, true);

  private final boolean firstOrder;
  private final boolean symmetric;

  Copula(boolean firstOrder, boolean symmetric)
  {
    this.firstOrder = firstOrder;
    this.symmetric = symmetric;
  }

  public boolean isFirstOrder()
  {
    return firstOrder;
  }

  public boolean isSymmetric()
  {
    return symmetric;
  }
}
