package org.narson.api.narsese;

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
  Connector getConnector();

  /**
   * Returns the immutable list of the terms that compose this compound term.
   * <p>
   * The returned list contains at least one term and the order of the terms may not be relevant
   * depending of the connector.
   *
   * @return the immutable list of the terms that compose this compound term
   */
  List<Term> getTerms();

  /**
   * Returns the position of the placeholder or -1 if this compound term does not support
   * placeholder.
   * <p>
   * Only the {@link Connector#EXTENSIONAL_IMAGE} and the {@link Connector#INTENSIONAL_IMAGE}
   * connectors support placeholder. The returned placeholder is then greater or equal to 2 and less
   * or equal to the number of terms that compose this compound term, eg; a position of 2 means that
   * the placeholder is at the 2nd position, after the term at index 0 and before the term at index
   * 1.
   *
   * @return the position of the placeholder or -1 if this compound term does not support
   *         placeholder.
   */
  int getPlaceHolderPosition();

  // TODO check equality iwth the recurssive def of compound and a way to reprenset for matching
  // rule
}
