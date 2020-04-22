package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A copula that link two terms in a {@link CopulaTerm}.
 *
 */
@ProviderType
public enum SecondaryCopula
{
  INSTANCE,
  PROPERTY,
  INSTANCE_PROPERTY,
  PREDICTIVE_IMPLICATION,
  RETROSPECTIVE_IMPLICATION,
  CONCURRENT_IMPLICATION,
  PREDICTIVE_EQUIVALENCE,
  CONCURRENT_EQUIVALENCE
}
