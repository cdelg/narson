package org.narson.narsese.provider.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.ConstantTerm;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.narsese.NarseseProvider;

public class ConstantTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private ConstantTerm c1;
  private ConstantTerm c2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.constant("a").getValueType(), equalTo(ValueType.CONSTANT));
  }

  @Test
  public void testConvertion()
  {
    assertThat(nf.constant("a").asConstant(), is(anything()));
    assertThrows(IllegalStateException.class, () -> nf.constant("a").asCopulaTerm());
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

  @Test
  public void testComplexity()
  {
    c1 = nf.constant("a");
    assertThat(c1.getSyntacticComplexity(), equalTo(1));
  }
}
