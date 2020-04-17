package org.narson.narsese.provider;

import org.narson.api.narsese.QueryVariable;

final class QueryVariableImpl extends AbstractTerm implements QueryVariable
{
  private final String name;
  private final boolean anonymous;

  public QueryVariableImpl(int bufferSize, String name)
  {
    super(ValueType.QUERY_VARIABLE, bufferSize, 0);
    this.name = name != null ? name : "";
    anonymous = this.name.isEmpty();
  }

  public QueryVariableImpl(int bufferSize)
  {
    this(bufferSize, null);
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
}
