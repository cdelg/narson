package org.narson.narsese.provider;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseValue.ValueType;

public class AbstractNarseseValueTest
{
  class NarseseValue extends AbstractNarseseValue
  {
    public NarseseValue(ValueType valueType)
    {
      super(new NarseseProvider(), valueType);
    }
  }

  private final NarseseValue constantValue1 = new NarseseValue(ValueType.CONSTANT);
  private final NarseseValue constantValue2 = new NarseseValue(ValueType.CONSTANT);
  private final NarseseValue variableValue = new NarseseValue(ValueType.QUERY_VARIABLE);

  @Test
  public void testValueIsRight()
  {
    assertThat(constantValue1.getValueType(), equalTo(ValueType.CONSTANT));
    assertThat(constantValue2.getValueType(), equalTo(ValueType.CONSTANT));
    assertThat(variableValue.getValueType(), equalTo(ValueType.QUERY_VARIABLE));
  }

  @Test
  public void testEquals()
  {
    assertThat(constantValue1.equals(constantValue2), is(true));
    assertThat(constantValue2.equals(constantValue1), is(true));
    assertThat(constantValue2.equals(null), is(false));
    assertThat(constantValue2.equals(variableValue), is(false));
    assertThat(variableValue.equals(constantValue2), is(false));
  }
}
