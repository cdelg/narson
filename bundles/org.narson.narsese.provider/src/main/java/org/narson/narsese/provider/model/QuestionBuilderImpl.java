package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkNotNull;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.QuestionBuilder;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class QuestionBuilderImpl implements QuestionBuilder
{
  private final Narsese narsese;
  private final Term statement;
  private Tense tense = Tense.NONE;

  public QuestionBuilderImpl(Narsese narsese, Term statement)
  {
    this.narsese = narsese;
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
    return new QuestionImpl(narsese, statement, tense);
  }
}
