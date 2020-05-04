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
    result = prime * result + getCopula().hashCode();
    result = prime * result + getPredicate().hashCode();
    result = prime * result + getSubject().hashCode();
    result = prime * result + getTense().hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
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
  public int compareTo(Term o)
  {
    final int compareType = getValueType().compareTo(o.getValueType());
    if (compareType < 0)
    {
      return -1;
    }
    if (compareType > 0)
    {
      return 1;
    } else
    {
      return compare(o.asCopulaTerm());
    }
  }

  private int compare(CopulaTerm o)
  {
    final int compareSubject = getSubject().compareTo(o.getSubject());
    if (compareSubject < 0)
    {
      return -1;
    }
    if (compareSubject > 0)
    {
      return 1;
    } else
    {
      final int compareCopula = getCopula().compareTo(o.getCopula());
      if (compareCopula < 0)
      {
        return -1;
      }
      if (compareCopula > 0)
      {
        return 1;
      } else
      {
        final int comparePredicate = getPredicate().compareTo(o.getPredicate());
        if (comparePredicate < 0)
        {
          return -1;
        }
        if (comparePredicate > 0)
        {
          return 1;
        } else
        {
          return 0;
        }
      }
    }
  }
}
