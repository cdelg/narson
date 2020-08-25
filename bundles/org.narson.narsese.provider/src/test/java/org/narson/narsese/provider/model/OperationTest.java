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
import org.narson.api.narsese.OperationTerm;
import org.narson.narsese.NarseseProvider;

public class OperationTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private OperationTerm c1;
  private OperationTerm c2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.operation("a").build().getValueType(), equalTo(ValueType.OPERATION));
  }

  @Test
  public void testTermsListImmutable()
  {
    assertThrows(UnsupportedOperationException.class,
        () -> nf.operation("a").build().getTerms().add(nf.constant("a")));
  }

  @Test
  public void testConvertion()
  {
    assertThat(nf.operation("d").build().asOperation(), is(anything()));
    assertThrows(IllegalStateException.class, () -> nf.operation("d").build().asConstant());
  }

  @Test
  public void testEqual()
  {
    c1 = nf.operation("a").on(nf.constant("a"), nf.constant("a")).build();
    c2 = nf.operation("a").on(nf.constant("a"), nf.constant("a")).build();

    assertThat(c1, equalTo(c2));

    c1 = nf.operation("a").on(nf.constant("a"), nf.constant("a")).build();
    c2 = nf.operation("b").on(nf.constant("a"), nf.constant("a")).build();

    assertThat(c1, is(not(equalTo(c2))));

    c1 = nf.operation("a").on(nf.constant("a"), nf.constant("a")).build();
    c2 = nf.operation("a").on(nf.constant("a"), nf.constant("b")).build();

    assertThat(c1, is(not(equalTo(c2))));

    c1 = nf.operation("a").on(nf.constant("b"), nf.constant("a")).build();
    c2 = nf.operation("a").on(nf.constant("a"), nf.constant("b")).build();

    assertThat(c1, is(not(equalTo(c2))));
  }

  @Test
  public void testComplexity()
  {
    c1 = nf.operation("a").build();
    assertThat(c1.getSyntacticComplexity(), equalTo(1));

    c1 = nf.operation("a").on(nf.constant("b"), nf.constant("a")).build();
    assertThat(c1.getSyntacticComplexity(), equalTo(3));

    c1 = nf.operation("a").on(c1, nf.constant("a")).build();
    assertThat(c1.getSyntacticComplexity(), equalTo(5));
  }
}