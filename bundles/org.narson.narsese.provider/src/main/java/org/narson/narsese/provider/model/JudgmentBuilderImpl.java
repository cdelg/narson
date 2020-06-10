package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.JudgmentBuilder;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;

final class JudgmentBuilderImpl implements JudgmentBuilder
{
  private final Narsese narsese;
  private final Term statement;
  private Tense tense = Tense.NONE;
  private TruthValue truthValue;

  public JudgmentBuilderImpl(Narsese narsese, Term statement, TruthValue defaultTruthValue)
  {
    this.narsese = narsese;
    this.statement = statement;
    truthValue = defaultTruthValue;
  }

  @Override
  public JudgmentBuilder tense(Tense tense)
  {
    this.tense = checkNotNull(tense);
    return this;
  }

  @Override
  public JudgmentBuilder truthValue(double frequency, double confidence)
  {
    checkArgument(0 <= frequency, "frequency < 0");
    checkArgument(frequency <= 1, "frequency > 1");
    checkArgument(0 < confidence, "confidence <= 0");
    checkArgument(confidence < 1, "confidence >= 1");

    truthValue = new TruthValueImpl(frequency, confidence);
    return this;
  }

  @Override
  public JudgmentBuilder truthValue(TruthValue truthValue) throws NullPointerException
  {
    checkNotNull(truthValue, "truthValue");

    this.truthValue = truthValue;
    return this;
  }

  @Override
  public Judgment build()
  {
    return new JudgmentImpl(narsese, statement, truthValue, tense);
  }
}
