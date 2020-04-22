package org.narson.api.inference;

import java.util.List;
import org.narson.api.todo.Belief;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface InferenceEngine
{
  List<Belief> reason(Belief belief);

  List<Belief> reason(Belief belief, Belief otherBelief);
}
