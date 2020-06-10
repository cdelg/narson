package org.narson.api.narsese;

import java.util.Set;
import org.osgi.annotation.versioning.ProviderType;

/**
 * CompoundTerm class represents an immutable Narsese compound term, that is, any term composed of
 * one or more other terms and which support any of the term and statement connectors, see
 * {@link Connector}.
 * <p>
 * A CompoundTerm can be obtained by using a {@link CompoundTermBuilder}.
 *
 */
@ProviderType
public interface SetTerm extends Term
{
  /**
   * Returns the connector of this compound term.
   *
   * @return the connector of this compound term.
   */
  SetConnector getConnector();

  /**
   * Returns the immutable collection of the terms that compose this compound term.
   * <p>
   * The returned collection contains at least one term and the order of the terms may not be
   * relevant depending of the connector.
   *
   * @return the immutable collection of the terms that compose this compound term
   */
  Set<Term> getTerms();
}
