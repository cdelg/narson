package org.narson.narsese.provider;

import org.narson.api.narsese.Question;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class QuestionImpl extends AbstractSentence implements Question
{
  private final Tense tense;

  public QuestionImpl(int bufferSize, int prefixThreshold, Term statement, Tense tense)
  {
    super(ValueType.QUESTION, bufferSize, prefixThreshold, statement);
    this.tense = tense;
  }

  @Override
  public Tense getTense()
  {
    return tense;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + tense.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof Question))
    {
      return false;
    }

    final Question other = (Question) obj;

    if (!tense.equals(other.getTense()))
    {
      return false;
    }

    return true;
  }
}
