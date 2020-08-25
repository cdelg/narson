package org.narson.narsese.provider.rules;

import org.narson.api.narsese.Inference;
import org.narson.api.narsese.InferenceType;
import org.narson.api.narsese.Judgment;

final class DefaultInference implements Inference
{
  private final InferenceType type;
  private final Judgment judgment;

  public DefaultInference(InferenceType type, Judgment judgment)
  {
    this.type = type;
    this.judgment = judgment;
  }

  @Override
  public InferenceType getType()
  {
    return type;
  }

  @Override
  public Judgment getJudgment()
  {
    return judgment;
  }
}
