package org.narson.inference.provider;

import java.util.List;
import org.narson.api.inference.InferenceEngine;
import org.narson.api.todo.Belief;
import org.osgi.service.component.annotations.Component;

@Component
final public class DefaultInferenceEngine implements InferenceEngine
{

  @Override
  public List<Belief> reason(Belief belief)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Belief> reason(Belief belief, Belief otherBelief)
  {
    // TODO Auto-generated method stub
    return null;
  }
}
