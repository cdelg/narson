package org.narson.narsese.provider;

import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;

final class IndependentVariableImpl extends AbstractTerm implements IndependentVariable
{
  private final String name;

  public IndependentVariableImpl(Narsese narsese, String name)
  {
    super(narsese, ValueType.INDEPENDENT_VARIABLE);
    this.name = name;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  protected int computeSyntacticComplexity()
  {
    return 1;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + name.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof IndependentVariable))
    {
      return false;
    }

    final IndependentVariable other = (IndependentVariable) obj;

    if (!name.equals(other.getName()))
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
      return compare(o.asIndependentVariable());
    }
  }

  private int compare(IndependentVariable o)
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
      return 0;
    }
  }
}
