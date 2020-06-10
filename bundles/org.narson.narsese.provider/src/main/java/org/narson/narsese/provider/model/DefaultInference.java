package org.narson.narsese.provider.model;

import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Judgment;

final class DefaultInference implements Inference
{
  private final Type type;
  private final Judgment judgment;

  public DefaultInference(Type type, Judgment judgment)
  {
    this.type = type;
    this.judgment = judgment;
  }

  @Override
  public Type getType()
  {
    return type;
  }

  @Override
  public Judgment getJudgment()
  {
    return judgment;
  }
}
