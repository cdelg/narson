package org.narson.api.narsese;

import java.util.Collection;
import java.util.List;
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
public interface CompoundTerm extends Term
{
  /**
   * Returns the connector of this compound term.
   *
   * @return the connector of this compound term.
   */
  CompoundConnector getConnector();

  /**
   * Returns the immutable collection of the terms that compose this compound term.
   * <p>
   * The returned collection contains at least one term and the order of the terms may not be
   * relevant depending of the connector.
   *
   * @return the immutable collection of the terms that compose this compound term
   */
  Collection<Term> getTerms();

  List<RecursiveCompoundView> getRecursiveViews();
}
