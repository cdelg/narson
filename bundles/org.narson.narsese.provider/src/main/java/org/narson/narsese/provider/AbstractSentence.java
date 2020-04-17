package org.narson.narsese.provider;

import java.io.StringWriter;
import org.narson.api.narsese.NarseseGenerator;
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
