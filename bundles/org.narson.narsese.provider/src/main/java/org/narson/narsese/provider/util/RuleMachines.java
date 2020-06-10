package org.narson.narsese.provider.util;

import org.narson.api.narsese.Narsese;
import org.narson.narsese.provider.util.RuleMachine.RuleMachineBuilder;

final public class RuleMachines
{
  private RuleMachine sys;

  public RuleMachines(Narsese narsese)
  {
    init(narsese);
  }

  private void init(Narsese narsese)
  {
    RuleMachineBuilder builder;

    builder = RuleMachine.createBuilder(narsese);
    builder.addRule("A-->B", "B-->A", "A<->B").diff("A", "B").res().switchable().done();
    sys = builder.build();
  }
}
