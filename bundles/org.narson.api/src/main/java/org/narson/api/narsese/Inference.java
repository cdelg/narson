package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Inference
{
  InferenceType getType();

  Judgment getJudgment();
}
