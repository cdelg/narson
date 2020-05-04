package org.narson.api.narsese;

import javax.management.relation.Relation;
import org.osgi.annotation.versioning.ProviderType;

/**
 * NarseseValue represents an immutable Narsese value.
 *
 * <p>
 * A Narsese value is one of the following: a {@link Judgment} sentence, a {@link Goal} sentence, a
 * {@link Question} sentence, a {@link Query} sentence, an {@link Operation} term, a
 * {@link Relation} term, a {@link CompoundTerm} term, a {@link Constant} term, a {@link Constant}
 * term, an {@link IndependentVariable} term, a {@link DependentVariable} term or a
 * {@link QueryVariable} term.
 *
 */
@ProviderType
public interface NarseseValue
{
  /**
   * The type of a {@link NarseseValue}
   *
   */
  public enum ValueType
  {
    CONSTANT,
    INDEPENDENT_VARIABLE,
    QUERY_VARIABLE,
    DEPENDENT_VARIABLE,
    COPULA_TERM,
    COMPOUND_TERM,
    OPERATION,
    QUERY,
    QUESTION,
    GOAL,
    JUDGMENT
  }

  /**
   * Returns the type of this Narsese value.
   *
   * @return the type of this Narsese value
   */
  NarseseValue.ValueType getValueType();

  /**
   * Returns the Narsese text for this Narsese value.
   *
   * @return the Narsese text for this Narsese value
   */
  @Override
  String toString();
}
