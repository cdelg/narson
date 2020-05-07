package org.narson.api.inference;

import java.util.List;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface InferenceEngine
{
  InferenceBB choose(Inferenciable inferenciable, Inferenciable otherInferenciable,
      double razorParameter);

  InferenceBB revise(Inferenciable inferenciable, Inferenciable otherInferenciable);

  List<InferenceBB> reason(Inferenciable inferenciable, double evidentialHorizon);

  List<InferenceBB> reason(Inferenciable inferenciable, Inferenciable otherInferenciable,
      double evidentialHorizon);
}
