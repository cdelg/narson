package org.narson.narsese.provider.codec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.NarseseParsingException;
import org.narson.api.narsese.NarseseReader;
import org.narson.api.narsese.NarseseReading;
import org.narson.api.narsese.Sentence;

final public class NarseseReadingImpl implements NarseseReading
{
  private final Supplier<NarseseReader> readerProvider;

  public NarseseReadingImpl(Supplier<NarseseReader> readerProvider)
  {
    this.readerProvider = readerProvider;
  }

  @Override
  public List<Sentence> toSentences() throws NarseseException, NarseseParsingException
  {
    Sentence sentence = null;
    final List<Sentence> sentences = new ArrayList<>();
    try (NarseseReader reader = readerProvider.get())
    {
      while ((sentence = reader.read()) != null)
      {
        sentences.add(sentence);
      }
    }
    return sentences;
  }

  @Override
  public Sentence toSentence() throws NarseseException, NarseseParsingException
  {
    try (NarseseReader reader = readerProvider.get())
    {
      final Sentence result = reader.read();
      if (result != null)
      {
        return result;
      } else
      {
        throw new NarseseException("No sentence to parse.");
      }
    }
  }

  @Override
  public Stream<Sentence> toStream()
  {
    final NarseseReader reader = readerProvider.get();
    final Iterable<Sentence> it = () -> new Iterator<Sentence>()
    {
      private Sentence currentSentence = null;

      @Override
      public boolean hasNext()
      {
        return currentSentence != null || (currentSentence = reader.read()) != null;
      }

      @Override
      public Sentence next()
      {
        Sentence result = null;
        if (currentSentence != null)
        {
          result = currentSentence;
          currentSentence = null;
        } else
        {
          result = reader.read();
        }

        if (result != null)
        {
          return result;
        } else
        {
          throw new NoSuchElementException();
        }
      }
    };
    return StreamSupport.stream(it.spliterator(), false).onClose(() -> reader.close());
  }
}
