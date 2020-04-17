package org.narson.api.narsese;

import java.util.List;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Operation class represents an immutable Narsese operation term.
 * <p>
 * An Operation can be obtained by using a {@link OperationBuilder}.
 *
 */
@ProviderType
public interface Operation extends Term
{
  /**
   * Returns the name of this operation.
   *
   * @return the name of this operation
   */
  String getName();

  /**
   * Returns the immutable list of term parameters for this operation.
   * 
   * @return the immutable list of term parameters for this operation.
   */
  List<Term> getTerms();
}
