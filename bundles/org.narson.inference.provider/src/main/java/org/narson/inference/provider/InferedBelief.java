package org.narson.inference.provider;

import org.narson.api.narsese.Judgment;
import org.narson.api.todo.Belief;
import org.narson.api.todo.EvidentialBase;

class InferedBelief implements Belief
{
  private final Judgment judgment;
  private final EvidentialBase evidentialBase;

  public InferedBelief(Judgment judgment, EvidentialBase evidentialBase)
  {
    this.judgment = judgment;
    this.evidentialBase = evidentialBase;
  }

  @Override
  public Judgment getJudgment()
  {
    return judgment;
  }

  @Override
  public EvidentialBase getEvidentialBase()
  {
    return evidentialBase;
  }
}
