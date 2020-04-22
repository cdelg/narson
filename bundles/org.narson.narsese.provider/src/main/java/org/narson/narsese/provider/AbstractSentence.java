package org.narson.narsese.provider;

import java.io.StringWriter;
import org.narson.api.narsese.Goal;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.NarseseGenerator;
import org.narson.api.narsese.Query;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.Sentence;
import org.narson.api.narsese.Term;

abstract class AbstractSentence extends AbstractNarseseValue implements Sentence
{
  private final int bufferSize;
  private final int prefixThreshold;
  private final Term statement;

  public AbstractSentence(ValueType valueType, int bufferSize, int prefixThreshold, Term statement)
  {
    super(valueType);
    this.bufferSize = bufferSize;
    this.prefixThreshold = prefixThreshold;
    this.statement = statement;
  }

  @Override
  final public Term getStatement()
  {
    return statement;
  }

  final protected int getBufferSize()
  {
    return bufferSize;
  }

  final protected int getPrefixThreshold()
  {
    return prefixThreshold;
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
    final StringWriter out = new StringWriter();
    try (NarseseGenerator generator = new NarseseGeneratorImpl(out, bufferSize, prefixThreshold))
    {
      generator.write(this);
    }

    return out.toString();
  }
}
