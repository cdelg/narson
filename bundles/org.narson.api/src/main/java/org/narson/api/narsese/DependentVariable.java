package org.narson.api.narsese;

import java.util.Set;
import org.osgi.annotation.versioning.ProviderType;

/**
 * DependentVariable class represents an immutable Narsese dependent variable.
 * <p>
 * A DependentVariable can be obtained by using a {@link DependentVariableBuilder}.
 *
 */
@ProviderType
public interface DependentVariable extends Term
{
  /**
   * Returns the name of this dependent variable. If the name is empty, it means that it is an
   * anonymous variable.
   *
   * @return the name of this dependent variable
   */
  String getName();

  /**
   * Returns the immutable set of the independent variable names upon which this variable depends.
   *
   * @return the immutable set of the independent variable names upon which this variable depends
   */
  Set<String> getIndependentVariableNames();

  /**
   * Returns true if this variable is anonymous. In this case, the {@link DependentVariable#getName}
   * method returns the empty string and the {@link DependentVariable#getIndependentVariableNames}
   * method returns an empty set.
   *
   * @return true if this variable is anonymous
   */
  boolean isAnonymous();
}
