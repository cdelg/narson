package org.narson.narsese.provider.model;

import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue;

abstract class AbstractNarseseValue implements NarseseValue
{
  protected final Narsese narsese;
  protected final NarseseFactory nf;
  private final ValueType valueType;

  public AbstractNarseseValue(Narsese narsese, ValueType valueType)
  {
    this.valueType = valueType;
    this.narsese = narsese;
    nf = narsese.getNarseseFactory();
  }

  @Override
  final public ValueType getValueType()
  {
    return valueType;
  }

  @Override
  public int hashCode()
  {
    return valueType.hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof NarseseValue))
    {
      return false;
    }
    final NarseseValue other = (NarseseValue) obj;

    if (!valueType.equals(other.getValueType()))
    {
      return false;
    }

    return true;
  }
}
