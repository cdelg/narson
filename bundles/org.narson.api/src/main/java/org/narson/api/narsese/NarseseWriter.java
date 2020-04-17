package org.narson.api.narsese;

import java.io.Closeable;
import java.io.Flushable;
import java.util.Collection;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NarseseWriter extends Closeable, Flushable
{
  void write(Collection<Sentence> sentences) throws NarseseException, IllegalStateException;

  void write(Sentence... sentences) throws NarseseException, IllegalStateException;

  void write(Sentence sentence) throws NarseseException, IllegalStateException;

  @Override
  void flush() throws NarseseException;

  @Override
  void close() throws NarseseException;
}
