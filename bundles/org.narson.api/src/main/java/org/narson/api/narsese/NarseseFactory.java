package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Factory to create the various elements of the Narsese's object model.
 */
@ProviderType
public interface NarseseFactory
{
  /**
   * Create a new {@link JudgmentBuilder} which can then be used to complete the creation of a
   * {@link Judgment} sentence.
   *
   * @param statement the sentence's statement
   * @return a new {@link JudgmentBuilder}
   * @throws NullPointerException if statement is null
   */
  JudgmentBuilder judgment(Term statement) throws NullPointerException;

  /**
   * Create a new {@link QuestionBuilder} which can then be used to complete the creation of a
   * {@link Question} sentence.
   *
   * @param statement the sentence's statement
   * @return a new {@link QuestionBuilder}
   * @throws NullPointerException if statement is null
   */
  QuestionBuilder question(Term statement) throws NullPointerException;

  /**
   * Create a new {@link GoalBuilder} which can then be used to complete the creation of a
   * {@link Goal} sentence.
   *
   * @param statement the sentence's statement
   * @return a new {@link GoalBuilder}
   * @throws NullPointerException if statement is null
   */
  GoalBuilder goal(Term statement) throws NullPointerException;

  /**
   * Create a new {@link Query} sentence.
   *
   * @param statement the sentence's statement
   * @return a new {@link Query} sentence
   * @throws NullPointerException if statement is null
   */
  Query query(Term statement) throws NullPointerException;

  /**
   * Create a new {@link OperationBuilder} which can then be used to complete the creation of an
   * {@link Operation}. The name of the operation must be a non empty word in the Narsese language,
   * that is, it can contain any unicode letter, digits and the underscore character and must not
   * start with a digit or be the single underscore character.
   *
   * @param name the name of the operation
   * @return a new {@link OperationBuilder}
   * @throws NullPointerException if name is null
   * @throws IllegalArgumentException if name is not a valid word in the Narsese language
   */
  OperationBuilder operation(String name) throws NullPointerException, IllegalArgumentException;

  /**
   * Create a new {@link CopulaTerm} with the given subject, copula and predicate.
   *
   * @param subject the subject term of the copula term
   * @param copula the copula of the copula term
   * @param predicate the predicate term of the copula term
   * @return a new {@link CopulaTerm}
   * @throws NullPointerException if any of the parameters is null
   */
  CopulaTerm copulaTerm(Term subject, Copula copula, Term predicate) throws NullPointerException;

  /**
   * Create a new {@link CopulaTerm} with the given subject and predicate.
   * <p>
   * TODO explain rewritting copulas
   *
   * @param subject the subject term of the copula term
   * @param copula the copula of the copula term
   * @param predicate the predicate term of the copula term
   * @return a new {@link CopulaTerm}
   * @throws NullPointerException if any of the parameters is null
   */
  CopulaTerm copulaTerm(Term subject, SecondaryCopula copula, Term predicate)
      throws NullPointerException;

  /**
   * Create a new {@link CompoundTermBuilder} which can then be used to complete the creation of a
   * {@link CompoundTerm}.
   *
   * @param connector the compound term connector
   * @return a new {@link CompoundTermBuilder}
   * @throws NullPointerException
   */
  CompoundTermBuilder compoundTerm(Connector connector) throws NullPointerException;

  /**
   * Create a new {@link Constant}. The name of the constant must be a non empty word in the Narsese
   * language, that is, it can contain any unicode letter, digits and the underscore character and
   * must not start with a digit or be the single underscore character.
   *
   * @param name the name of the constant
   * @return a new {@link Constant}
   * @throws NullPointerException if name is null
   * @throws IllegalArgumentException if name is not a valid word in the Narsese language
   */
  Constant constant(String name) throws NullPointerException, IllegalArgumentException;

  /**
   * Create a new {@link IndependentVariable}. The name of the independent variable must be a non
   * empty word in the Narsese language, that is, it can contain any unicode letter, digits and the
   * underscore character and must not start with a digit or be the single underscore character.
   *
   * @param name the name of the independent variable
   * @return a new {@link IndependentVariable}
   * @throws NullPointerException if name is null
   * @throws IllegalArgumentException if name is not a valid word in the Narsese language
   */
  IndependentVariable independentVariable(String name)
      throws NullPointerException, IllegalArgumentException;

  /**
   * Create a new anonymous {@link DependentVariable}.
   *
   * @return a new {@link DependentVariable}
   */
  DependentVariable dependentVariable();

  /**
   * Create a new {@link DependentVariableBuilder} which can then be used to complete the creation
   * of a {@link DependentVariable}. The name of the dependent variable must be a word in the
   * Narsese language, that is, it can contain any unicode letter, digits and the underscore
   * character and must not start with a digit or be the single underscore character.
   *
   * @param name the name of the dependent variable
   * @return a new {@link DependentVariableBuilder}
   * @throws NullPointerException if name is null
   * @throws IllegalArgumentException if name is not a valid word in the Narsese language
   */
  DependentVariableBuilder dependentVariable(String name)
      throws NullPointerException, IllegalArgumentException;

  /**
   * Create a new anonymous {@link QueryVariable}.
   *
   * @return a new {@link QueryVariable}
   */
  QueryVariable queryVariable();

  /**
   * Create a new {@link QueryVariable} with the specified name. The name of the dependent variable
   * must be a word in the Narsese language, that is, it can contain any unicode letter, digits and
   * the underscore character and must not start with a digit or be the single underscore character.
   *
   * @param name the name of the query variable
   * @return a new {@link QueryVariable}
   * @throws NullPointerException if name is null
   * @throws IllegalArgumentException if name is not a valid word in the Narsese language
   */
  QueryVariable queryVariable(String name) throws NullPointerException, IllegalArgumentException;
}
