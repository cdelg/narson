package org.narson.narsese.provider.model;

import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.Narsese;

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
}
