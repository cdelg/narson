package org.narson.narsese.provider;

import org.narson.api.narsese.Goal;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Query;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.Sentence;
import org.narson.api.narsese.Term;

abstract class AbstractSentence extends AbstractNarseseValue implements Sentence
{
  private final Term statement;

  public AbstractSentence(Narsese narsese, ValueType valueType, Term statement)
  {
    super(narsese, valueType);
    this.statement = statement;
  }

  @Override
  final public Term getStatement()
  {
    return statement;
  }

  @Override
  final public Judgment asJudgment() throws IllegalStateException
  {
    try
    {
      return (Judgment) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This sentence is not a judgment.");
    }
  }

  @Override
  final public Goal asGoal() throws IllegalStateException
  {
    try
    {
      return (Goal) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This sentence is not a goal.");
    }
  }

  @Override
  final public Question asQuestion() throws IllegalStateException
  {
    try
    {
      return (Question) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This sentence is not a question.");
    }
  }

  @Override
  final public Query asQuery() throws IllegalStateException
  {
    try
    {
      return (Query) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This sentence is not a query.");
    }
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + statement.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof Sentence))
    {
      return false;
    }

    final Sentence other = (Sentence) obj;

    if (!statement.equals(other.getStatement()))
    {
      return false;
    }

    return true;
  }

  @Override
  final public String toString()
  {
    return narsese.write(this).toOutputString();
  }
}
