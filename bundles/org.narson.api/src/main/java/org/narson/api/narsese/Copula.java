package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A copula that links two terms in a {@link CopulaTerm}.
 *
 */
@ProviderType
public enum Copula
{
  INHERITANCE, SIMILARITY, IMPLICATION, EQUIVALENCE
}
