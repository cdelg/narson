package org.narson.narsese.provider.codec;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseReadingBuilder;
import org.narson.api.narsese.Sentence;

final public class NarseseReaderReadingBuilder implements NarseseReadingBuilder<Reader>
{
  private final Narsese narsese;

  public NarseseReaderReadingBuilder(Narsese narsese)
  {
    this.narsese = narsese;
  }

  @Override
  public NarseseReadingBuilder<Reader> fromReader()
  {
    return this;
  }

  @Override
  public NarseseReadingBuilder<InputStream> fromInputStream()
  {
    return new NarseseInputStreamReadingBuilder(narsese);
  }

  @Override
  public Function<Reader, List<Sentence>> toSentences()
  {
    return in -> {
      return narsese.read(in).toSentences();
    };
  }

  @Override
  public Function<Reader, Sentence> toSentence()
  {
    return in -> {
      return narsese.read(in).toSentence();
    };
  }

  @Override
  public Function<Reader, Stream<Sentence>> toStream()
  {
    return in -> {
      return narsese.read(in).toStream();
    };
  }
}
