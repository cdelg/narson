package org.narson.api.narsese;

import java.util.Collection;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A builder for creating {@link OperationTerm} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface OperationBuilder
{
  /**
   * Add a term to operation's parameters list of the operation being built.
   *
   * @param term the term to add
   * @return this builder
   * @throws NullPointerException if the term is null
   */
  OperationBuilder on(Term term) throws NullPointerException;

  /**
   * Add the terms to operation's parameters list of the operation being built.
   *
   * @param terms the terms to add
   * @return this builder
   * @throws NullPointerException if terms is null or one of its elements
   */
  OperationBuilder on(Term... terms) throws NullPointerException;

  /**
   * Add the terms to operation's parameters list of the operation being built.
   *
   * @param terms the terms to add
   * @return this builder
   * @throws NullPointerException if terms is null or one of its elements
   */
  OperationBuilder on(Collection<Term> terms) throws NullPointerException;

  /**
   * Returns a new {@link OperationTerm} object conform to the state of this object builder.
   *
   * @return a new {@link OperationTerm} object conform to the state of this object builder
   */
  OperationTerm build();
}
