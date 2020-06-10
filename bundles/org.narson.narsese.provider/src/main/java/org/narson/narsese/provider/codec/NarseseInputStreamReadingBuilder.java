package org.narson.narsese.provider.codec;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseReadingBuilder;
import org.narson.api.narsese.Sentence;

final public class NarseseInputStreamReadingBuilder implements NarseseReadingBuilder<InputStream>
{
  private final Narsese narsese;

  public NarseseInputStreamReadingBuilder(Narsese narsese)
  {
    this.narsese = narsese;
  }

  @Override
  public NarseseReadingBuilder<Reader> fromReader()
  {
    return new NarseseReaderReadingBuilder(narsese);
  }

  @Override
  public NarseseReadingBuilder<InputStream> fromInputStream()
  {
    return this;
  }

  @Override
  public Function<InputStream, List<Sentence>> toSentences()
  {
    return in -> {
      return narsese.read(in).toSentences();
    };
  }

  @Override
  public Function<InputStream, Sentence> toSentence()
  {
    return in -> {
      return narsese.read(in).toSentence();
    };
  }

  @Override
  public Function<InputStream, Stream<Sentence>> toStream()
  {
    return in -> {
      return narsese.read(in).toStream();
    };
  }
}
