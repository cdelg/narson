package org.narson.api.narsese;

import java.util.Set;
import org.osgi.annotation.versioning.ProviderType;


/**
 * A builder for creating {@link DependentVariable} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface DependentVariableBuilder
{
  /**
   * Add an independent variable name upon which the dependent variable being build dependents.
   *
   * @param independentVariableName the independent variable name to add
   * @return this builder
   * @throws NullPointerException independentVariableName is null
   */
  DependentVariableBuilder dependsOn(String independentVariableName) throws NullPointerException;

  /**
   * Add a list of independent variable names upon which the dependent variable being build
   * dependents.
   *
   * @param independentVariableNames the independent variable names to add
   * @return this builder
   * @throws NullPointerException if independentVariableNames is null or one of its elements
   */
  DependentVariableBuilder dependsOn(String... independentVariableNames)
      throws NullPointerException;

  /**
   * Add a set of independent variable names upon which the dependent variable being build depends.
   *
   * @param independentVariableNames the independent variable names to add
   * @return this builder
   * @throws NullPointerException if independentVariableNames is null or one of its elements
   */
  DependentVariableBuilder dependsOn(Set<String> independentVariableNames)
      throws NullPointerException;

  /**
   * Returns a new {@link DependentVariable} object conform to the state of this object builder.
   *
   * @return a new {@link DependentVariable} object conform to the state of this object builder
   */
  DependentVariable build();
}
