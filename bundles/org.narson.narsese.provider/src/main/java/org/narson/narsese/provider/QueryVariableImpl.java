package org.narson.narsese.provider;

import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.QueryVariable;
import org.narson.api.narsese.Term;

final class QueryVariableImpl extends AbstractTerm implements QueryVariable
{
  private final String name;
  private final boolean anonymous;

  public QueryVariableImpl(Narsese narsese, String name)
  {
    super(narsese, ValueType.QUERY_VARIABLE);
    this.name = name != null ? name : "";
    anonymous = this.name.isEmpty();
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public boolean isAnonymous()
  {
    return anonymous;
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

    if (!(obj instanceof QueryVariable))
    {
      return false;
    }

    final QueryVariable other = (QueryVariable) obj;

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
      return compare(o.asQueryVariable());
    }
  }

  private int compare(QueryVariable o)
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
