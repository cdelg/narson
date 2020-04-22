package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicReference;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Constant;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.NarseseGenerator;
import org.narson.api.narsese.Operation;
import org.narson.api.narsese.QueryVariable;
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
  final public int getSyntacticComplexity()
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
  final public double getSyntacticSimplicity(double razorParameter) throws IllegalArgumentException
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


  @Override
  final public Operation asOperation() throws IllegalStateException
  {
    try
    {
      return (Operation) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not an operation.");
    }
  }

  @Override
  final public CopulaTerm asCopulaTerm() throws IllegalStateException
  {
    try
    {
      return (CopulaTerm) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not a copula term.");
    }
  }

  @Override
  final public CompoundTerm asCompoundTerm() throws IllegalStateException
  {
    try
    {
      return (CompoundTerm) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not a compound term.");
    }
  }

  @Override
  final public Constant asConstant() throws IllegalStateException
  {
    try
    {
      return (Constant) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not a constant.");
    }
  }

  @Override
  final public IndependentVariable asIndependentVariable() throws IllegalStateException
  {
    try
    {
      return (IndependentVariable) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not a independent variable.");
    }
  }

  @Override
  final public DependentVariable asDependentVariable() throws IllegalStateException
  {
    try
    {
      return (DependentVariable) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not a dependent variable.");
    }
  }

  @Override
  final public QueryVariable asQueryVariable() throws IllegalStateException
  {
    try
    {
      return (QueryVariable) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not a query variable.");
    }
  }
}
