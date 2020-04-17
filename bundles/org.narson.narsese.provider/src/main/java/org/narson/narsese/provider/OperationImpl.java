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
}
