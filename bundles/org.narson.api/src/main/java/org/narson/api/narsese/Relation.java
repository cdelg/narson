package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Relation class represents an immutable Narsese relation term. This is two terms, a subject and a
 * predicate, linked by a copula.
 * <p>
 * A Relation can be obtained from a {@link NarseseFactory}.
 *
 */
@ProviderType
public interface Relation extends Term
{
  /**
   * Returns the subject term of this relation
   * 
   * @return the subject term of this relation
   */
  Term getSubject();

  /**
   * Returns the copula of this relation
   * 
   * @return the copula of this relation
   */
  Copula getCopula();

  /**
   * Returns the predicate term of this relation
   * 
   * @return the predicate term of this relation
   */
  Term getPredicate();
}
