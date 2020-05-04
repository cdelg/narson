package org.narson.inference.provider;

import org.narson.api.inference.Inference;
import org.narson.api.inference.Inferenciable;

final class DefaultInference implements Inference
{
  private final Type type;
  private final Inferenciable inferenciable;

  public DefaultInference(Type type, Inferenciable inferenciable)
  {
    this.type = type;
    this.inferenciable = inferenciable;
  }

  @Override
  public Type getType()
  {
    return type;
  }

  @Override
  public Inferenciable getInferenciable()
  {
    return inferenciable;
  }
}
