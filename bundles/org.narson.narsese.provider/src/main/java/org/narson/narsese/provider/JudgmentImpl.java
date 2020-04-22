package org.narson.narsese.provider;

import java.util.concurrent.atomic.AtomicReference;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;

final class JudgmentImpl extends AbstractSentence implements Judgment
{
  private final TruthValue truthValue;
  private final Tense tense;
  private final AtomicReference<Question> cachedQuestion = new AtomicReference<>();

  public JudgmentImpl(int bufferSize, int prefixThreshold, Term statement, TruthValue truthValue,
      Tense tense)
  {
    super(ValueType.JUDGMENT, bufferSize, prefixThreshold, statement);
    this.truthValue = truthValue;
    this.tense = tense;
  }

  @Override
  final public TruthValue getTruthValue()
  {
    return truthValue;
  }

  @Override
  final public Tense getTense()
  {
    return tense;
  }

  @Override
  public Question toQuestion()
  {
    Question result = cachedQuestion.get();
    if (result == null)
    {
      result = new QuestionImpl(getBufferSize(), getPrefixThreshold(), getStatement(), getTense());
      if (!cachedQuestion.compareAndSet(null, result))
      {
        return cachedQuestion.get();
      }
    }
    return result;
  }

  @Override
  final public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + truthValue.hashCode();
    result = prime * result + tense.hashCode();
    return result;
  }

  @Override
  final public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof Judgment))
    {
      return false;
    }

    final Judgment other = (Judgment) obj;

    if (!truthValue.equals(other.getTruthValue()))
    {
      return false;
    }

    if (!tense.equals(other.getTense()))
    {
      return false;
    }

    return true;
  }
}
