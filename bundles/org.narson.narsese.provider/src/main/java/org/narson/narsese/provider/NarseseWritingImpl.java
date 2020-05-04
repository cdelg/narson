package org.narson.narsese.provider;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.NarseseWriter;
import org.narson.api.narsese.NarseseWriting;
import org.narson.api.narsese.Sentence;

public class NarseseWritingImpl implements NarseseWriting
{
  private final Function<OutputStream, NarseseWriter> streamFunction;
  private final Function<Writer, NarseseWriter> writerFunction;
  private final List<Sentence> sentences;

  public NarseseWritingImpl(List<Sentence> sentences,
      Function<OutputStream, NarseseWriter> streamFunction,
      Function<Writer, NarseseWriter> writerFunction)
  {
    this.sentences = sentences;
    this.streamFunction = streamFunction;
    this.writerFunction = writerFunction;
  }

  public NarseseWritingImpl(Sentence[] sentences,
      Function<OutputStream, NarseseWriter> streamFunction,
      Function<Writer, NarseseWriter> writerFunction)
  {
    this(Arrays.asList(sentences), streamFunction, writerFunction);
  }

  @Override
  public void to(OutputStream out) throws NarseseException
  {
    try (NarseseWriter writer = streamFunction.apply(out))
    {
      writer.write(sentences);
    }
  }

  @Override
  public void to(Writer out) throws NarseseException
  {
    try (NarseseWriter writer = writerFunction.apply(out))
    {
      writer.write(sentences);
    }
  }

  @Override
  public String toOutputString() throws NarseseException
  {
    final StringWriter out = new StringWriter();
    try (NarseseWriter writer = writerFunction.apply(out))
    {
      writer.write(sentences);
    }
    return out.toString();
  }
}
