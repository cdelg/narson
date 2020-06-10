package org.narson.narsese.provider.model;

import java.util.Collection;
import java.util.List;
import org.narson.api.narsese.CompoundConnector;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.RecursiveCompoundView;
import org.narson.api.narsese.Term;

final class CompoundTermImpl extends AbstractTerm implements CompoundTerm
{
  private final CompoundConnector connector;
  private final Collection<Term> unmodifiableTerms;
  private final List<RecursiveCompoundView> unmodifiableRecurTerms;

  public CompoundTermImpl(Narsese narsese, CompoundConnector connector,
      Collection<Term> unmodifiableTerms, List<RecursiveCompoundView> unmodifiableRecurTerms)
  {
    super(narsese, ValueType.COMPOUND_TERM);
    this.connector = connector;
    this.unmodifiableTerms = unmodifiableTerms;
    this.unmodifiableRecurTerms = unmodifiableRecurTerms;
  }

  @Override
  final public CompoundConnector getConnector()
  {
    return connector;
  }

  @Override
  final public Collection<Term> getTerms()
  {
    return unmodifiableTerms;
  }

  @Override
  final public List<RecursiveCompoundView> getRecursiveViews()
  {
    return unmodifiableRecurTerms;
  }

  @Override
  final protected int computeSyntacticComplexity()
  {
    return getTerms().stream().mapToInt(Term::getSyntacticComplexity).sum() + 1;
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, double evidentialHorizon,
      List<Inference> inferences)
  {
    super.computeInferences(truthValue, evidentialHorizon, inferences);
    computeStructuralInferences(truthValue, inferences);
  }

  public void computeStructuralInferences(TruthValueImpl truthValue, List<Inference> inferences)
  {
    // if (isConjunctionOfCopulas())
    // {
    // final CopulaTerm copula1 = unmodifiableTerms.get(0).asCopulaTerm();
    // final CopulaTerm copula2 = unmodifiableTerms.get(1).asCopulaTerm();
    //
    // if (copula1.getCopula() == Copula.INHERITANCE && copula2.getCopula() == Copula.INHERITANCE)
    // {
    // if (copula1.getSubject().equals(copula2.getPredicate())
    // && copula1.getPredicate().equals(copula2.getSubject()))
    // {
    // if (!copula1.getSubject().equals(copula2.getSubject()))
    // {
    // /* Structural equivalence rule (S<->P) <===> (S-->P) & (P-->S) */
    // inferences.add(new DefaultInference(Inference.Type.STRUCTURAL_EQUIVALENCE,
    // nf.judgment(
    // nf.copulaTerm(copula1.getSubject(), Copula.SIMILARITY, copula2.getSubject()))
    // .truthValue(truthValue).build()));
    // }
    // }
    // }
    // }
  }

  // private boolean isConjunctionOfCopulas()
  // {
  // // if (getConnector() == CompoundConnector.CONJUNCTION && unmodifiableTerms.size() == 2)
  // // {
  // // return unmodifiableTerms.get(0).getValueType() == ValueType.COPULA_TERM
  // // && unmodifiableTerms.get(1).getValueType() == ValueType.COPULA_TERM;
  // // } else
  // // {
  // // return false;
  // // }
  // }

  @Override
  final public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + connector.hashCode();
    result = prime * result + unmodifiableTerms.hashCode();
    return result;
  }

  @Override
  final public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (!super.equals(obj))
    {
      return false;
    }
    if (!(obj instanceof CompoundTerm))
    {
      return false;
    }
    final CompoundTerm other = (CompoundTerm) obj;

    if (connector != other.getConnector())
    {
      return false;
    }

    if (unmodifiableTerms.equals(other.getTerms()))
    {
      return true;
    } else
    {
      return false;
    }
  }
}
