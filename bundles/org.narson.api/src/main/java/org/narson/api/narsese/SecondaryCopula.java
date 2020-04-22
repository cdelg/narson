package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A copula only used in communication to link two terms in a {@link Relation}. A secondary copula
 * is rewritten for internal representation using the class {@link Copula}.
 *
 */
@ProviderType
public enum SecondaryCopula
{
  INSTANCE, PROPERTY, INSTANCE_PROPERTY
}
