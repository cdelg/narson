package org.narson.narsese.provider;

import org.narson.api.narsese.NarseseValue;

abstract class AbstractNarseseValue implements NarseseValue
{
  private final ValueType valueType;

  public AbstractNarseseValue(ValueType valueType)
  {
    this.valueType = valueType;
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
