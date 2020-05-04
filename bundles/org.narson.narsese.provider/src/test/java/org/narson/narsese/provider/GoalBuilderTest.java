package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Goal;
import org.narson.api.narsese.GoalBuilder;
import org.narson.api.narsese.NarseseFactory;

public class GoalBuilderTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private GoalBuilder b;
  private Goal v;

  @Test
  public void testWithoutDesireValue()
  {
    v = nf.goal(nf.constant("a")).build();

    assertThat(v.getDesireValue(), notNullValue());
    assertThat(v.getDesireValue().getFrequency(), equalTo(1.0));
    assertThat(v.getDesireValue().getConfidence(), equalTo(0.9));
  }

  @Test
  public void testWithDesireValue()
  {
    v = nf.goal(nf.constant("a")).desireValue(0.5, 0.5).build();

    assertThat(v.getDesireValue(), notNullValue());
    assertThat(v.getDesireValue().getFrequency(), equalTo(0.5));
    assertThat(v.getDesireValue().getConfidence(), equalTo(0.5));

    b = nf.goal(nf.constant("a"));

    assertThrows(IllegalArgumentException.class, () -> b.desireValue(-0.1, 0.5));
    assertThrows(IllegalArgumentException.class, () -> b.desireValue(1.1, 0.5));
    assertThrows(IllegalArgumentException.class, () -> b.desireValue(0.5, 0));
    assertThrows(IllegalArgumentException.class, () -> b.desireValue(0.5, 1));
  }
}
