package org.narson.narsese.provider.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.QueryVariable;
import org.narson.narsese.NarseseProvider;

public class QueryVariableTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private QueryVariable v1;
  private QueryVariable v2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.queryVariable("a").getValueType(), equalTo(ValueType.QUERY_VARIABLE));
  }

  @Test
  public void testAnonymous()
  {
    v1 = nf.queryVariable("a");
    assertThat(v1.isAnonymous(), equalTo(false));

    v1 = nf.queryVariable();
    assertThat(v1.isAnonymous(), equalTo(true));

    v1 = nf.queryVariable("");
    assertThat(v1.isAnonymous(), equalTo(true));
  }

  @Test
  public void testConvertion()
  {
    assertThat(nf.queryVariable().asQueryVariable(), is(anything()));
    assertThrows(IllegalStateException.class, () -> nf.queryVariable().asConstant());
  }

  @Test
  public void testEqual()
  {
    v1 = nf.queryVariable("a");
    v2 = nf.queryVariable("a");
    assertThat(v1, equalTo(v2));

    v1 = nf.queryVariable();
    v2 = nf.queryVariable();
    assertThat(v1, equalTo(v2));

    v1 = nf.queryVariable("a");
    v2 = nf.queryVariable("b");
    assertThat(v1, is(not(equalTo(v2))));
  }

  @Test
  public void testComplexity()
  {
    v1 = nf.queryVariable("a");
    assertThat(v1.getSyntacticComplexity(), equalTo(1));
  }
}
