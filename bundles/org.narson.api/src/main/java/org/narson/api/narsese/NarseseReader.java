package org.narson.api.narsese;

import java.io.Closeable;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NarseseReader extends AutoCloseable, Closeable
{
  Sentence read() throws NarseseException, NarseseParsingException, IllegalStateException;

  int read(Sentence[] sentences)
      throws NarseseException, NarseseParsingException, IllegalStateException;

  @Override
  void close() throws NarseseException;
}
