package org.narson.narsese.provider.codec;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseWritingBuilder;
import org.narson.api.narsese.Sentence;

final public class NarseseArrayBatchWritingBuilder implements NarseseWritingBuilder<Sentence[]>
{
  private final Narsese narsese;

  public NarseseArrayBatchWritingBuilder(Narsese narsese)
  {
    this.narsese = narsese;
  }

  @Override
  public NarseseWritingBuilder<Collection<Sentence>> batch()
  {
    return new NarseseBatchWritingBuilder(narsese);
  }

  @Override
  public NarseseWritingBuilder<Sentence[]> arrayBatch()
  {
    return this;
  }

  @Override
  public Function<Sentence[], String> toOutputString()
  {
    return sentences -> {
      return narsese.write(sentences).toOutputString();
    };
  }

  @Override
  public BiConsumer<Sentence[], OutputStream> toOutputStream()
  {
    return (sentences, out) -> {
      narsese.write(sentences).to(out);
    };
  }

  @Override
  public BiConsumer<Sentence[], Writer> toWriter()
  {
    return (sentences, out) -> {
      narsese.write(sentences).to(out);
    };
  }
}
