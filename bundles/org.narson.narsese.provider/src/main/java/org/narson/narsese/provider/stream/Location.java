package org.narson.narsese.provider.stream;

import org.narson.api.narsese.NarseseLocation;

final class Location implements NarseseLocation
{
  private final long columnNumber;
  private final long lineNumber;
  private final long streamOffset;

  public Location(long columnNumber, long lineNumber, long streamOffset)
  {
    super();
    this.columnNumber = columnNumber;
    this.lineNumber = lineNumber;
    this.streamOffset = streamOffset;
  }

  @Override
  public long getColumnNumber()
  {
    return columnNumber;
  }

  @Override
  public long getLineNumber()
  {
    return lineNumber;
  }

  @Override
  public long getStreamOffset()
  {
    return streamOffset;
  }
}
