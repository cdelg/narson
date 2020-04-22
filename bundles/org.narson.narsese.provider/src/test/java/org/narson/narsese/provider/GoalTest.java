package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Goal;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;

public class GoalTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  private Goal v1;
  private Goal v2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.goal(nf.constant("a")).build().getValueType(), equalTo(ValueType.GOAL));
  }

  @Test
  public void testConvertion()
  {
    assertThat(nf.goal(nf.constant("a")).build().asGoal(), is(anything()));
    assertThrows(IllegalStateException.class, () -> nf.goal(nf.constant("a")).build().asJudgment());
  }

  @Test
  public void testToQuery()
  {
    v1 = nf.goal(nf.constant("a")).build();

    assertThat(v1.toQuery().getStatement(), equalTo(v1.getStatement()));
  }

  @Test
  public void testEqual()
  {
    v1 = nf.goal(nf.constant("a")).build();
    v2 = nf.goal(nf.constant("a")).build();
    assertThat(v1, equalTo(v2));

    v1 = nf.goal(nf.constant("a")).build();
    v2 = nf.goal(nf.constant("a")).desireValue(1, 0.9).build();
    assertThat(v1, equalTo(v2));

    v1 = nf.goal(nf.constant("a")).desireValue(0.5, 0.5).build();
    v2 = nf.goal(nf.constant("a")).desireValue(0.5, 0.5).build();
    assertThat(v1, equalTo(v2));

    v1 = nf.goal(nf.constant("a")).desireValue(1, 0.9).build();
    v2 = nf.goal(nf.constant("a")).desireValue(1, 0.8).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.goal(nf.constant("a")).desireValue(1, 0.9).build();
    v2 = nf.goal(nf.constant("a")).desireValue(0.9, 0.9).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.goal(nf.constant("a")).desireValue(1, 0.9).build();
    v2 = nf.goal(nf.constant("b")).desireValue(1, 0.9).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.goal(nf.constant("a")).desireValue(0.9, 0.9).build();
    v2 = nf.goal(nf.constant("b")).desireValue(1, 0.8).build();
    assertThat(v1, is(not(equalTo(v2))));
  }
}
