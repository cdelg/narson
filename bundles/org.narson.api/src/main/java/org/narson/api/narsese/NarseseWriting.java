package org.narson.api.narsese;

import java.io.OutputStream;
import java.io.Writer;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NarseseWriting
{
  void to(OutputStream out) throws NarseseException;

  void to(Writer out) throws NarseseException;

  String toOutputString() throws NarseseException;
}
