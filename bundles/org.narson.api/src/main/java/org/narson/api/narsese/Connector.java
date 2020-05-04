package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A connector that affect one or terms in a {@link CompoundTerm}.
 *
 */
@ProviderType
public enum Connector
{
  NEGATION(false, true, 1, 1),
  EXTENSIONAL_SET(false, true, 1, Integer.MAX_VALUE),
  INTENSIONAL_SET(false, true, 1, Integer.MAX_VALUE),
  EXTENSIONAL_INTERSECTION(false, false, 2, Integer.MAX_VALUE),
  INTENSIONAL_INTERSECTION(false, false, 2, Integer.MAX_VALUE),
  EXTENSIONAL_DIFFERENCE(true, false, 2, 2),
  INTENSIONAL_DIFFERENCE(true, false, 2, 2),
  PRODUCT(true, false, 2, Integer.MAX_VALUE),
  EXTENSIONAL_IMAGE(true, false, 2, Integer.MAX_VALUE),
  INTENSIONAL_IMAGE(true, false, 2, Integer.MAX_VALUE),
  CONJUNCTION(false, false, 2, Integer.MAX_VALUE),
  DISJUNCTION(false, false, 2, Integer.MAX_VALUE),
  SEQUENTIAL_CONJUNCTION(true, false, 2, Integer.MAX_VALUE),
  PARALLEL_CONJUNCTION(false, false, 2, Integer.MAX_VALUE);

  private boolean ordered;
  private boolean hasDistinctComponents;
  private int minCardinality;
  private int maxCardinality;

  Connector(boolean ordered, boolean hasDistinctComponents, int minCardinality, int maxCardinality)
  {
    this.ordered = ordered;
    this.hasDistinctComponents = hasDistinctComponents;
    this.minCardinality = minCardinality;
    this.maxCardinality = maxCardinality;
  }

  public boolean isOrdered()
  {
    return ordered;
  }

  public boolean hasDistinctComponents()
  {
    return hasDistinctComponents;
  }

  public int minCardinality()
  {
    return minCardinality;
  }

  public int maxCardinality()
  {
    return maxCardinality;
  }
}
