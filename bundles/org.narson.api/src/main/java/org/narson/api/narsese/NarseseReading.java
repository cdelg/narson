package org.narson.api.narsese;

import java.util.List;
import java.util.stream.Stream;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NarseseReading
{
  List<Sentence> toSentences() throws NarseseException, NarseseParsingException;

  Sentence toSentence() throws NarseseException, NarseseParsingException;

  Stream<Sentence> toStream();
}
