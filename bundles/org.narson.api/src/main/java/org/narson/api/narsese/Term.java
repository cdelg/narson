package org.narson.api.narsese;

import javax.management.relation.Relation;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Term class represents an immutable Narsese term.
 *
 * <p>
 * A Narsese term is one of the following: a {@link Constant}, an {@link Operation}, a
 * {@link Relation}, a {@link CompoundTerm} term, an {@link IndependentVariable}, a
 * {@link DependentVariable} or a {@link QueryVariable}.
 */
@ProviderType
public interface Term extends NarseseValue, Comparable<Term>
{
  /**
   * Returns the syntactic complexity of this term.
   *
   * @return the syntactic complexity of this term
   */
  int getSyntacticComplexity();

  /**
   * Returns the syntactic simplicity of this term given the razor parameter.
   *
   * @return the syntactic complexity of this term
   * @throws IllegalArgumentException if razorParameter if less or equals to 0
   */
  double getSyntacticSimplicity(double razorParameter) throws IllegalArgumentException;

  /**
   * Returns this term as an operation term.
   *
   * @return this term as an operation term
   * @throws IllegalStateException if this term is not an operation
   */
  Operation asOperation() throws IllegalStateException;

  /**
   * Returns this term as a copula term.
   *
   * @return this term as a copula term
   * @throws IllegalStateException if this term is not a copula term
   */
  CopulaTerm asCopulaTerm() throws IllegalStateException;

  /**
   * Returns this term as a compound term.
   *
   * @return this term as a compound term
   * @throws IllegalStateException if this term is not a compound term
   */
  CompoundTerm asCompoundTerm() throws IllegalStateException;

  /**
   * Returns this term as a constant term.
   *
   * @return this term as a constant term
   * @throws IllegalStateException if this term is not a constant
   */
  Constant asConstant() throws IllegalStateException;

  /**
   * Returns this term as an independent variable term.
   *
   * @return this term as an independent variable term
   * @throws IllegalStateException if this term is not an independent variable
   */
  IndependentVariable asIndependentVariable() throws IllegalStateException;

  /**
   * Returns this term as a dependent variable term.
   *
   * @return this term as a dependent variable term
   * @throws IllegalStateException if this term is not a dependent variable
   */
  DependentVariable asDependentVariable() throws IllegalStateException;

  /**
   * Returns this term as a query variable term.
   *
   * @return this term as a query variable term
   * @throws IllegalStateException if this term is not a query variable
   */
  QueryVariable asQueryVariable() throws IllegalStateException;
}
