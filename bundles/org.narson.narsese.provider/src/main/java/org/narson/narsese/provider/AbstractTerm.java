package org.narson.narsese.provider;

import java.io.StringWriter;
import org.narson.api.narsese.NarseseGenerator;
import org.narson.api.narsese.Term;

abstract class AbstractTerm extends AbstractNarseseValue implements Term
{
  private final int bufferSize;
  private final int prefixThreshold;

  public AbstractTerm(ValueType valueType, int bufferSize, int prefixThreshold)
  {
    super(valueType);
    this.bufferSize = bufferSize;
    this.prefixThreshold = prefixThreshold;
  }

  @Override
  final public String toString()
  {
    final StringWriter out = new StringWriter();
    try (NarseseGenerator generator = new NarseseGeneratorImpl(out, bufferSize, prefixThreshold))
    {
      generator.write(new QueryImpl(bufferSize, prefixThreshold, this));
    }

    final String result = out.toString();
    return result.substring(0, result.length() - 1);
  }
}
