package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;

public class IndenpendentVariableTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  private IndependentVariable v1;
  private IndependentVariable v2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.independantVariable("a").getValueType(), equalTo(ValueType.INDEPENDENT_VARIABLE));
  }

  @Test
  public void testEqual()
  {
    v1 = nf.independantVariable("a");
    v2 = nf.independantVariable("a");
    assertThat(v1, equalTo(v2));

    v1 = nf.independantVariable("a");
    v2 = nf.independantVariable("b");
    assertThat(v1, is(not(equalTo(v2))));
  }
}
