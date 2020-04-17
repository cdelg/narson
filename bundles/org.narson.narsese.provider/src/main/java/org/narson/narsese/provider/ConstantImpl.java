package org.narson.narsese.provider;

import org.narson.api.narsese.Constant;

final class ConstantImpl extends AbstractTerm implements Constant
{
  private final String name;

  public ConstantImpl(int bufferSize, String name)
  {
    super(ValueType.CONSTANT, bufferSize, 0);
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
}
