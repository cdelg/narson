package org.narson.narsese.provider.codec;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseWritingBuilder;
import org.narson.api.narsese.Sentence;

final public class NarseseSentenceWritingBuilder implements NarseseWritingBuilder<Sentence>
{
  private final Narsese narsese;

  public NarseseSentenceWritingBuilder(Narsese narsese)
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
    return new NarseseArrayBatchWritingBuilder(narsese);
  }

  @Override
  public Function<Sentence, String> toOutputString()
  {
    return sentence -> {
      return narsese.write(sentence).toOutputString();
    };
  }

  @Override
  public BiConsumer<Sentence, OutputStream> toOutputStream()
  {
    return (sentence, out) -> {
      narsese.write(sentence).to(out);
    };
  }

  @Override
  public BiConsumer<Sentence, Writer> toWriter()
  {
    return (sentence, out) -> {
      narsese.write(sentence).to(out);
    };
  }
}
