package org.narson.api.inference;

import java.util.List;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface InferenceEngine
{
  Inference choose(Inferenciable inferenciable, Inferenciable otherInferenciable,
      double razorParameter);

  Inference revise(Inferenciable inferenciable, Inferenciable otherInferenciable);

  List<Inference> reason(Inferenciable inferenciable, double evidentialHorizon);

  List<Inference> reason(Inferenciable inferenciable, Inferenciable otherInferenciable,
      double evidentialHorizon);
}
