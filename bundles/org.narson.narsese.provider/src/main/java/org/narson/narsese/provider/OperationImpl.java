package org.narson.narsese.provider;

import java.util.List;
import org.narson.api.narsese.Operation;
import org.narson.api.narsese.Term;

final class OperationImpl extends AbstractTerm implements Operation
{
  private final String name;
  private final List<Term> unmodifiableTerms;

  public OperationImpl(int bufferSize, int prefixThreshold, String name,
      List<Term> unmodifiableTerms)
  {
    super(ValueType.OPERATION, bufferSize, prefixThreshold);
    this.name = name;
    this.unmodifiableTerms = unmodifiableTerms;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public List<Term> getTerms()
  {
    return unmodifiableTerms;
  }

  @Override
  protected int computeSyntacticComplexity()
  {
    return getTerms().stream().mapToInt(Term::getSyntacticComplexity).sum() + 1;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + name.hashCode();
    result = prime * result + unmodifiableTerms.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof Operation))
    {
      return false;
    }

    final Operation other = (Operation) obj;

    if (!name.equals(other.getName()))
    {
      return false;
    }

    if (!unmodifiableTerms.equals(other.getTerms()))
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
      return compare(o.asOperation());
    }
  }

  private int compare(Operation o)
  {
    final int compareName = getName().compareTo(o.getName());
    if (compareName < 0)
    {
      return -1;
    }
    if (compareName > 0)
    {
      return 1;
    } else
    {
      return ListHelper.compareTerms(getTerms(), o.getTerms());
    }
  }
}
