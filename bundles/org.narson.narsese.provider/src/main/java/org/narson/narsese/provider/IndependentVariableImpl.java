package org.narson.narsese.provider;

import org.narson.api.narsese.IndependentVariable;

final class IndependentVariableImpl extends AbstractTerm implements IndependentVariable
{
  private final String name;

  public IndependentVariableImpl(int bufferSize, String name)
  {
    super(ValueType.INDEPENDENT_VARIABLE, bufferSize, 0);
    this.name = name;
  }

  @Override
  public String getName()
  {
    return name;
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
}
