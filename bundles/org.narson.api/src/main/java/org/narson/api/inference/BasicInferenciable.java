package org.narson.api.inference;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import org.narson.api.narsese.Judgment;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
final public class BasicInferenciable implements Inferenciable
{
  private final Judgment judgment;
  private final long timestamp;

  public BasicInferenciable(Judgment judgment, long timestamp)
  {
    checkNotNull(judgment, "judgment");
    checkArgument(timestamp > 0, "timestamp <= 0");

    this.judgment = judgment;
    this.timestamp = timestamp;
  }

  public BasicInferenciable(Judgment judgment)
  {
    this(judgment, System.currentTimeMillis());
  }

  @Override
  public Judgment getJudgment()
  {
    return judgment;
  }

  @Override
  public long getTimestamp()
  {
    return timestamp;
  }
}
