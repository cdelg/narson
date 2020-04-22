package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Sentence class represents an immutable Narsese sentence. A sentence is all about a statement.
 *
 * <p>
 * A Narsese sentence is one of the following: a {@link Judgment}, a {@link Goal}, a
 * {@link Question} or a {@link Query}.
 */
@ProviderType
public interface Sentence extends NarseseValue
{
  /**
   * Returns the statement of this sentence.
   *
   * @return the statement of this sentence
   */
  Term getStatement();

  /**
   * Returns this sentence as a judgment.
   *
   * @return this sentence as a judgment
   * @throws IllegalStateException if this sentence is not a jugdment
   */
  Judgment asJudgment() throws IllegalStateException;

  /**
   * Returns this sentence as a goal.
   *
   * @return this sentence as a goal
   * @throws IllegalStateException if this sentence is not a goal
   */
  Goal asGoal() throws IllegalStateException;

  /**
   * Returns this sentence as a question.
   *
   * @return this sentence as a question
   * @throws IllegalStateException if this sentence is not a question
   */
  Question asQuestion() throws IllegalStateException;

  /**
   * Returns this sentence as a query.
   *
   * @return this sentence as a query
   * @throws IllegalStateException if this sentence is not a query
   */
  Query asQuery() throws IllegalStateException;
}
