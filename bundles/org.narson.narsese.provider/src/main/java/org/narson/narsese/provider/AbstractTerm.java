package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import java.util.List;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Constant;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Operation;
import org.narson.api.narsese.QueryVariable;
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

  public void computeInferences(TruthValueImpl truthValue, double evidentialHorizon,
      List<Inference> inferences)
  {
    /* Immediate Negation rule {S} |- !S */
    inferences.add(new DefaultInference(Inference.Type.IMMEDIATE_NEGATION,
        nf.judgment(nf.compoundTerm(Connector.NEGATION).of(this).build())
            .truthValue(truthValue.computeNegation()).build()));
  }

  public void computeInferences(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    // Nothing to do
    // TODO
    /*
     * Higher order conditional strong compositional rules (must have different evidential base and
     * additional condition???? TODO)
     */
    // ruleMachine.addRule("P.", "S.", "(P && S).",
    // InferenceBB.Type.CONDITIONAL_COMPOSITIONAL_INTERSECTION, false);
    // ruleMachine.addRule("P.", "S.", "(P || S).",
    // InferenceBB.Type.CONDITIONAL_COMPOSITIONAL_UNION,
    // false);

    /*
     * Higher order conditional weak compositional rules (must have different evidential base and
     * additional condition???? TODO)
     */
    // ruleMachine.addRule("P.", "S.", "(S==>P).",
    // InferenceBB.Type.CONDITIONAL_COMPOSITIONAL_INDUCTION, false);
    // ruleMachine.addRule("P.", "S.", "(S<=>P).",
    // InferenceBB.Type.CONDITIONAL_COMPOSITIONAL_COMPARISON, false);

  }
}
