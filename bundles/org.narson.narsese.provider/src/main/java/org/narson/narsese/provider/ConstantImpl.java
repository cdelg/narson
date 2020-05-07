package org.narson.narsese.provider;

import org.narson.api.narsese.Constant;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;

final class ConstantImpl extends AbstractTerm implements Constant
{
  private final String name;

  public ConstantImpl(Narsese narsese, String name)
  {
    super(narsese, ValueType.CONSTANT);
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

    if (!(obj instanceof Constant))
    {
      return false;
    }

    final Constant other = (Constant) obj;

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
      return compare(o.asConstant());
    }
  }

  private int compare(Constant o)
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
