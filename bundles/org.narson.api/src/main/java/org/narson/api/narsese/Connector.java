package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A connector that affect one or terms in a {@link CompoundTerm}.
 *
 */
@ProviderType
public enum Connector
{
  EXTENSIONAL_SET,
  INTENSIONAL_SET,
  EXTENSIONAL_INTERSECTION,
  INTENSIONAL_INTERSECTION,
  EXTENSIONAL_DIFFERENCE,
  INTENSIONAL_DIFFERENCE,
  PRODUCT,
  EXTENSIONAL_IMAGE,
  INTENSIONAL_IMAGE,
  NEGATION,
  CONJUNCTION,
  DISJUNCTION,
  SEQUENTIAL_CONJUNCTION,
  PARALLEL_CONJUNCTION
}
