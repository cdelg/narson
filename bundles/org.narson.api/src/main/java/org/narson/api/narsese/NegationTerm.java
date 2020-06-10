package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Constant class represents an immutable Narsese constant term, that is, a simple word.
 *
 */
@ProviderType
public interface NegationTerm extends Term
{
  /**
   * Returns the negated term.
   *
   * @return the negated term
   */
  Term getTerm();
}
