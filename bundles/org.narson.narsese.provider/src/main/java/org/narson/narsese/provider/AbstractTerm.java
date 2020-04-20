package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicReference;
import org.narson.api.narsese.NarseseGenerator;
import org.narson.api.narsese.Term;

abstract class AbstractTerm extends AbstractNarseseValue implements Term
{
  private final int bufferSize;
  private final int prefixThreshold;
  private final AtomicReference<Integer> cachedSyntacticComplexity = new AtomicReference<>();
  private final AtomicReference<Double> cachedSyntacticSimplicity = new AtomicReference<>();

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

  @Override
  public int getSyntacticComplexity()
  {
    Integer result = cachedSyntacticComplexity.get();
    if (result == null)
    {
      result = computeSyntacticComplexity();
      if (!cachedSyntacticComplexity.compareAndSet(null, result))
      {
        return cachedSyntacticComplexity.get();
      }
    }
    return result;
  }

  protected abstract int computeSyntacticComplexity();

  @Override
  public double getSyntacticSimplicity(double razorParameter) throws IllegalArgumentException
  {
    checkArgument(0 < razorParameter, "razorParameter <= 0");
    Double result = cachedSyntacticSimplicity.get();
    if (result == null)
    {
      result = 1 / Math.pow(getSyntacticComplexity(), razorParameter);
      if (!cachedSyntacticSimplicity.compareAndSet(null, result))
      {
        return cachedSyntacticSimplicity.get();
      }
    }
    return result;
  }
}
