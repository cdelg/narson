package org.narson.narsese.provider;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Function;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.NarseseWriter;
import org.narson.api.narsese.NarseseWriting;
import org.narson.api.narsese.Sentence;

public class NarseseSingleWritingImpl implements NarseseWriting
{
  private final Function<OutputStream, NarseseWriter> streamFunction;
  private final Function<Writer, NarseseWriter> writerFunction;
  private final Sentence sentence;

  public NarseseSingleWritingImpl(Sentence sentence,
      Function<OutputStream, NarseseWriter> streamFunction,
      Function<Writer, NarseseWriter> writerFunction)
  {
    this.sentence = sentence;
    this.streamFunction = streamFunction;
    this.writerFunction = writerFunction;
  }

  @Override
  public void to(OutputStream out) throws NarseseException
  {
    try (NarseseWriter writer = streamFunction.apply(out))
    {
      writer.write(sentence);
    }
  }

  @Override
  public void to(Writer out) throws NarseseException
  {
    try (NarseseWriter writer = writerFunction.apply(out))
    {
      writer.write(sentence);
    }
  }

  @Override
  public String toOutputString() throws NarseseException
  {
    final StringWriter out = new StringWriter();
    try (NarseseWriter writer = writerFunction.apply(out))
    {
      writer.write(sentence);
    }
    return out.toString();
  }
}
