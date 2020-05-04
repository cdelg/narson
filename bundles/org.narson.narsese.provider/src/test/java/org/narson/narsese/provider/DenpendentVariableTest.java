package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;

public class DenpendentVariableTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private DependentVariable v1;
  private DependentVariable v2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.dependentVariable().getValueType(), equalTo(ValueType.DEPENDENT_VARIABLE));
  }

  @Test
  public void testVarListImmutable()
  {
    assertThrows(UnsupportedOperationException.class, () -> nf.dependentVariable("v").dependsOn("a")
        .build().getIndependentVariableNames().add("b"));
  }

  @Test
  public void testConvertion()
  {
    assertThat(nf.dependentVariable().asDependentVariable(), is(anything()));
    assertThrows(IllegalStateException.class, () -> nf.dependentVariable().asConstant());
  }

  @Test
  public void testEqual()
  {
    v1 = nf.dependentVariable();
    v2 = nf.dependentVariable();
    assertThat(v1, equalTo(v2));

    v1 = nf.dependentVariable();
    v2 = nf.dependentVariable("").build();
    assertThat(v1, equalTo(v2));

    v1 = nf.dependentVariable("a").build();
    v2 = nf.dependentVariable("a").build();
    assertThat(v1, equalTo(v2));

    v1 = nf.dependentVariable("a").dependsOn("x", "y").build();
    v2 = nf.dependentVariable("a").dependsOn("x", "y").build();
    assertThat(v1, equalTo(v2));

    v1 = nf.dependentVariable("a").dependsOn("x", "y").build();
    v2 = nf.dependentVariable("a").dependsOn("y", "x").build();
    assertThat(v1, equalTo(v2));

    v1 = nf.dependentVariable("a").build();
    v2 = nf.dependentVariable("b").build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.dependentVariable("a").dependsOn("x", "y").build();
    v2 = nf.dependentVariable("a").dependsOn("x", "z").build();
    assertThat(v1, is(not(equalTo(v2))));
  }

  @Test
  public void testComplexity()
  {
    v1 = nf.dependentVariable("a").build();
    assertThat(v1.getSyntacticComplexity(), equalTo(1));

    v1 = nf.dependentVariable("a").dependsOn("x", "y").build();
    assertThat(v1.getSyntacticComplexity(), equalTo(1));
  }
}
