package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.IndependentVariable;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;

public class IndenpendentVariableTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private IndependentVariable v1;
  private IndependentVariable v2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.independentVariable("a").getValueType(), equalTo(ValueType.INDEPENDENT_VARIABLE));
  }

  @Test
  public void testEqual()
  {
    v1 = nf.independentVariable("a");
    v2 = nf.independentVariable("a");
    assertThat(v1, equalTo(v2));

    v1 = nf.independentVariable("a");
    v2 = nf.independentVariable("b");
    assertThat(v1, is(not(equalTo(v2))));
  }

  @Test
  public void testConvertion()
  {
    assertThat(nf.independentVariable("d").asIndependentVariable(), is(anything()));
    assertThrows(IllegalStateException.class, () -> nf.independentVariable("a").asConstant());
  }

  @Test
  public void testComplexity()
  {
    v1 = nf.independentVariable("a");
    assertThat(v1.getSyntacticComplexity(), equalTo(1));
  }
}
