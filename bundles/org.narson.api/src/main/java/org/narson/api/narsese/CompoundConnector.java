package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A connector that affect one or terms in a {@link CompoundTerm}.
 *
 */
@ProviderType
public enum CompoundConnector
{
  EXTENSIONAL_INTERSECTION(false, 2, Integer.MAX_VALUE),
  INTENSIONAL_INTERSECTION(false, 2, Integer.MAX_VALUE),
  EXTENSIONAL_DIFFERENCE(true, 2, 2),
  INTENSIONAL_DIFFERENCE(true, 2, 2),
  PRODUCT(true, 2, Integer.MAX_VALUE),
  CONJUNCTION(false, 2, Integer.MAX_VALUE),
  DISJUNCTION(false, 2, Integer.MAX_VALUE),
  SEQUENTIAL_CONJUNCTION(true, 2, Integer.MAX_VALUE),
  PARALLEL_CONJUNCTION(false, 2, Integer.MAX_VALUE);

  private boolean ordered;
  private int minCardinality;
  private int maxCardinality;

  CompoundConnector(boolean ordered, int minCardinality, int maxCardinality)
  {
    this.ordered = ordered;
    this.minCardinality = minCardinality;
    this.maxCardinality = maxCardinality;
  }

  public boolean isOrdered()
  {
    return ordered;
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
