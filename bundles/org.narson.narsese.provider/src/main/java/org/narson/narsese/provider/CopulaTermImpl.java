package org.narson.narsese.provider;

import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class CopulaTermImpl extends AbstractTerm implements CopulaTerm
{
  private final Term subject;
  private final Copula copula;
  private final Term predicate;
  private final Tense tense;

  public CopulaTermImpl(int bufferSize, int prefixThreshold, Term subject, Copula copula,
      Term predicate, Tense tense)
  {
    super(ValueType.COPULA_TERM, bufferSize, prefixThreshold);
    this.subject = subject;
    this.copula = copula;
    this.predicate = predicate;
    this.tense = tense;
  }

  @Override
  public Term getSubject()
  {
    return subject;
  }

  @Override
  public Copula getCopula()
  {
    return copula;
  }

  @Override
  public Term getPredicate()
  {
    return predicate;
  }

  @Override
  public Tense getTense()
  {
    return tense;
  }

  @Override
  protected int computeSyntacticComplexity()
  {
    return subject.getSyntacticComplexity() + predicate.getSyntacticComplexity() + 1;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + copula.hashCode();
    result = prime * result + predicate.hashCode();
    result = prime * result + subject.hashCode();
    result = prime * result + tense.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }
    if (!(obj instanceof CopulaTermImpl))
    {
      return false;
    }
    final CopulaTerm other = (CopulaTerm) obj;

    if (copula != other.getCopula())
    {
      return false;
    }

    if (tense != other.getTense())
    {
      return false;
    }

    if (!predicate.equals(other.getPredicate()))
    {
      return false;
    }

    if (!subject.equals(other.getSubject()))
    {
      return false;
    }

    return true;
  }
}
