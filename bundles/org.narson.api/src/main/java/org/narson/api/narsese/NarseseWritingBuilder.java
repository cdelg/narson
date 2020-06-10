package org.narson.api.narsese;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NarseseWritingBuilder<T>
{
  NarseseWritingBuilder<Collection<Sentence>> batch();

  NarseseWritingBuilder<Sentence[]> arrayBatch();

  Function<T, String> toOutputString();

  BiConsumer<T, OutputStream> toOutputStream();

  BiConsumer<T, Writer> toWriter();
}
