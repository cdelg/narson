package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkNotNull;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.QuestionBuilder;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class QuestionBuilderImpl implements QuestionBuilder
{
  private final int bufferSize;
  private final int prefixThreshold;
  private final Term statement;
  private Tense tense = Tense.NONE;

  public QuestionBuilderImpl(int bufferSize, int prefixThreshold, Term statement)
  {
    this.bufferSize = bufferSize;
    this.prefixThreshold = prefixThreshold;
    this.statement = statement;
  }

  @Override
  public QuestionBuilder tense(Tense tense)
  {
    this.tense = checkNotNull(tense);
    return this;
  }

  @Override
  public Question build()
  {
    return new QuestionImpl(bufferSize, prefixThreshold, statement, tense);
  }
}
