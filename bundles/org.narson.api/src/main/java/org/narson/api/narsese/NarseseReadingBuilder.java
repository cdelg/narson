package org.narson.api.narsese;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NarseseReadingBuilder<T>
{
  NarseseReadingBuilder<Reader> fromReader();

  NarseseReadingBuilder<InputStream> fromInputStream();

  Function<T, List<Sentence>> toSentences();

  Function<T, Sentence> toSentence();

  Function<T, Stream<Sentence>> toStream();
}
