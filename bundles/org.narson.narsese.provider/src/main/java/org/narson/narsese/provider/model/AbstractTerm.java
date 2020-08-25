package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkArgument;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.ConstantTerm;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.ImageTerm;
import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NegationTerm;
import org.narson.api.narsese.OperationTerm;
import org.narson.api.narsese.QueryVariable;
import org.narson.api.narsese.SetTerm;
import org.narson.api.narsese.Term;

abstract class AbstractTerm extends AbstractNarseseValue implements Term
{
  private volatile Integer cachedSyntacticComplexity;
  private volatile Double cachedSyntacticSimplicity;

  public AbstractTerm(Narsese narsese, ValueType valueType)
  {
    super(narsese, valueType);
  }

  @Override
  final public String toString()
  {
    final String result = narsese.write(new QueryImpl(narsese, this)).toOutputString();
    return result.substring(0, result.length() - 1);
  }

  @Override
  final public int getSyntacticComplexity()
  {
    return cachedSyntacticComplexity != null ? cachedSyntacticComplexity
        : (cachedSyntacticComplexity = computeSyntacticComplexity());
  }

  protected abstract int computeSyntacticComplexity();

  @Override
  final public double getSyntacticSimplicity(double razorParameter)
  {
    checkArgument(razorParameter > 0, "razorParameter <= 0");

    return cachedSyntacticSimplicity != null ? cachedSyntacticSimplicity
        : (cachedSyntacticSimplicity = 1 / Math.pow(getSyntacticComplexity(), razorParameter));
  }

  @Override
  final public OperationTerm asOperationTerm() throws IllegalStateException
  {
    try
    {
      return (OperationTerm) this;
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
  public ImageTerm asImageTerm() throws IllegalStateException
  {
    try
    {
      return (ImageTerm) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not a compound term.");
    }
  }

  @Override
  public SetTerm asSetTerm() throws IllegalStateException
  {
    try
    {
      return (SetTerm) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not a compound term.");
    }
  }

  @Override
  public NegationTerm asNegationTerm() throws IllegalStateException
  {
    try
    {
      return (NegationTerm) this;
    } catch (final ClassCastException e)
    {
      throw new IllegalStateException("This term is not a compound term.");
    }
  }

  @Override
  final public ConstantTerm asConstantTerm() throws IllegalStateException
  {
    try
    {
      return (ConstantTerm) this;
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
