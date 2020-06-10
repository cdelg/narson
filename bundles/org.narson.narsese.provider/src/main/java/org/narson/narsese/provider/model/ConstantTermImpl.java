package org.narson.narsese.provider.model;

import org.narson.api.narsese.ConstantTerm;
import org.narson.api.narsese.Narsese;

final class ConstantTermImpl extends AbstractTerm implements ConstantTerm
{
  private final String name;

  public ConstantTermImpl(Narsese narsese, String name)
  {
    super(narsese, ValueType.CONSTANT_TERM);
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

    if (!(obj instanceof ConstantTerm))
    {
      return false;
    }

    final ConstantTerm other = (ConstantTerm) obj;

    if (!name.equals(other.getName()))
    {
      return false;
    }

    return true;
  }
}
