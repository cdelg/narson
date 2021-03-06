package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Constant;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;

public class ConstantTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  private Constant c1;
  private Constant c2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.constant("a").getValueType(), equalTo(ValueType.CONSTANT));
  }

  @Test
  public void testEqual()
  {
    c1 = nf.constant("a");
    c2 = nf.constant("a");

    assertThat(c1, equalTo(c2));

    c1 = nf.constant("a");
    c2 = nf.constant("b");

    assertThat(c1, is(not(equalTo(c2))));
  }
}
