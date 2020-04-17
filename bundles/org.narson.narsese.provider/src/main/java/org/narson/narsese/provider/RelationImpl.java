package org.narson.narsese.provider;

import org.narson.api.narsese.Copula;
import org.narson.api.narsese.Relation;
import org.narson.api.narsese.Term;

final class RelationImpl extends AbstractTerm implements Relation
{
  private final Term subject;
  private final Copula copula;
  private final Term predicate;

  public RelationImpl(int bufferSize, int prefixThreshold, Term subject, Copula copula,
      Term predicate)
  {
    super(ValueType.RELATION, bufferSize, prefixThreshold);
    this.subject = subject;
    this.copula = copula;
    this.predicate = predicate;
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
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + copula.hashCode();
    result = prime * result + predicate.hashCode();
    result = prime * result + subject.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }
    if (!(obj instanceof RelationImpl))
    {
      return false;
    }
    final Relation other = (Relation) obj;

    if (copula != other.getCopula())
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
