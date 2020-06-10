package org.narson.narsese.provider.model;

import java.util.List;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

abstract class AbstractCopulaTerm extends AbstractTerm implements CopulaTerm
{
  private final Term subject;
  private final Copula copula;
  private final Term predicate;
  private final Tense tense;

  public AbstractCopulaTerm(Narsese narsese, Term subject, Copula copula, Term predicate,
      Tense tense)
  {
    super(narsese, ValueType.COPULA_TERM);
    this.subject = subject;
    this.copula = copula;
    this.predicate = predicate;
    this.tense = tense;
  }

  @Override
  final public Term getSubject()
  {
    return subject;
  }

  @Override
  final public Copula getCopula()
  {
    return copula;
  }

  @Override
  final public Term getPredicate()
  {
    return predicate;
  }

  @Override
  final public Tense getTense()
  {
    return tense;
  }

  @Override
  final protected int computeSyntacticComplexity()
  {
    return subject.getSyntacticComplexity() + predicate.getSyntacticComplexity() + 1;
  }

  @Override
  final public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + getCopula().hashCode();
    result = prime * result + getPredicate().hashCode();
    result = prime * result + getSubject().hashCode();
    result = prime * result + getTense().hashCode();
    return result;
  }

  @Override
  final public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof CopulaTerm))
    {
      return false;
    }

    final CopulaTerm other = (CopulaTerm) obj;

    if (getCopula() != other.getCopula())
    {
      return false;
    }

    if (getTense() != other.getTense())
    {
      return false;
    }

    if (!getPredicate().equals(other.getPredicate()))
    {
      return false;
    }

    if (!getSubject().equals(other.getSubject()))
    {
      return false;
    }

    return true;
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    super.computeInferences(truthValue, otherTerm, otherTruthValue, evidentialHorizon, inferences);

    if (otherTerm.getValueType() == ValueType.COPULA_TERM)
    {
      final CopulaTerm otherCopulaTerm = otherTerm.asCopulaTerm();
      if (!(copula.isFirstOrder() ^ otherCopulaTerm.getCopula().isFirstOrder()))
      {
        computeInferences(truthValue, otherCopulaTerm, otherTruthValue, evidentialHorizon,
            inferences);
      }
    }
  }

  abstract public void computeInferences(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences);
}
