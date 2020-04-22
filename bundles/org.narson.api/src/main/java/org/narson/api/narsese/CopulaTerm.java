package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * CopulaTerm class represents an immutable Narsese term where two terms, a subject and a predicate,
 * are linked by a copula.
 * <p>
 * A CopulaTerm can be obtained from a {@link NarseseFactory}.
 *
 */
@ProviderType
public interface CopulaTerm extends Term
{
  /**
   * Returns the subject term of this copula term.
   *
   * @return the subject term of this copula term
   */
  Term getSubject();

  /**
   * Returns the copula of this copula term.
   *
   * @return the copula of this copula term
   */
  Copula getCopula();

  /**
   * Returns the predicate term of this copula term.
   *
   * @return the predicate term of this copula term
   */
  Term getPredicate();

  /**
   * Returns the tense of this copula term.
   *
   * @return the tense of this copula term.
   */
  Tense getTense();
}
