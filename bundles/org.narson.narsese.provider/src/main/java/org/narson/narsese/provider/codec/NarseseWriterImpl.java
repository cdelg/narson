package org.narson.narsese.provider.codec;

import static org.narson.tools.PredChecker.checkNotNull;
import static org.narson.tools.PredChecker.checkState;
import java.util.Collection;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.NarseseGenerationException;
import org.narson.api.narsese.NarseseGenerator;
import org.narson.api.narsese.NarseseWriter;
import org.narson.api.narsese.Sentence;

final public class NarseseWriterImpl implements NarseseWriter
{
  private final NarseseGenerator generator;
  private boolean closed = false;

  public NarseseWriterImpl(NarseseGenerator generator)
  {
    this.generator = generator;
  }

  @Override
  public void write(Collection<Sentence> sentences) throws NarseseException, IllegalStateException
  {
    checkNotNull(sentences, "sentences");
    checkState(!closed, "The stream has already been closed");

    for (final Sentence sentence : sentences)
    {
      checkNotNull(sentence, "One of the provided sentences is null.");
      generator.write(sentence);
    }
  }

  @Override
  public void write(Sentence... sentences) throws NarseseException, IllegalStateException
  {
    checkNotNull(sentences, "sentences");
    checkState(!closed, "The stream has already been closed");

    for (final Sentence sentence : sentences)
    {
      checkNotNull(sentence, "One of the provided sentences is null.");
      generator.write(sentence);
    }
  }

  @Override
  public void write(Sentence sentence) throws NarseseException, IllegalStateException
  {
    checkNotNull(sentence, "sentence");
    checkState(!closed, "The stream has already been closed");

    generator.write(sentence);
  }


  @Override
  public void flush() throws NarseseException
  {
    try
    {
      generator.flush();
    } catch (final NarseseException e)
    {
      throw new NarseseException(e.getMessage(), e.getCause());
    } catch (final Exception e)
    {
      throw new NarseseException(e.getMessage(), e);
    }
  }

  @Override
  public void close() throws NarseseException
  {
    if (!closed)
    {
      closed = true;
      try
      {
        generator.close();
      } catch (final NarseseGenerationException e)
      {
        throw new NarseseGenerationException(e.getMessage(), e.getCause());
      } catch (final NarseseException e)
      {
        throw new NarseseException(e.getMessage(), e.getCause());
      } catch (final Exception e)
      {
        throw new NarseseException(e.getMessage(), e);
      }
    }
  }
}
