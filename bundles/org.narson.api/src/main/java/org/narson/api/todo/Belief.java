package org.narson.api.todo;

import org.narson.api.narsese.Judgment;

public interface Belief
{
  Judgment getJudgment();

  EvidentialBase getEvidentialBase();
}
