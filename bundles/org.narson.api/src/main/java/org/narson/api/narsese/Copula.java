package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A copula that link two terms in a {@link Relation}.
 *
 */
@ProviderType
public enum Copula
{
  INHERITANCE,
  SIMILARITY,
  IMPLICATION,
  EQUIVALENCE,
  PREDICTIVE_IMPLICATION,
  RETROSPECTIVE_IMPLICATION,
  CONCURRENT_IMPLICATION,
  PREDICTIVE_EQUIVALENCE,
  CONCURRENT_EQUIVALENCE
}
