package org.narson.api.todo;

import java.util.Set;

public interface EvidentialBase extends Set<Long>
{
  boolean overlap(EvidentialBase evidentialBase);

  EvidentialBase merge(EvidentialBase evidentialBase);
}
