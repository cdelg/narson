package org.narson.api.inference;

import org.narson.api.narsese.Judgment;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface Inferenciable
{
  Judgment getJudgment();

  long getTimestamp();
}
