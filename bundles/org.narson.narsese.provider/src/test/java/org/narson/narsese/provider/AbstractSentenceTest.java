package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Sentence;

public class AbstractSentenceTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private final Sentence judgment1 = nf.judgment(nf.constant("one")).build();
  private final Sentence judgment2 = nf.judgment(nf.constant("one")).build();
  private final Sentence goal1 = nf.goal(nf.constant("one")).build();
  private final Sentence goal2 = nf.goal(nf.constant("two")).build();

  @Test
  public void testTermIsRight()
  {
    assertThat(judgment1.getStatement(), equalTo(nf.constant("one")));
    assertThat(judgment2.getStatement(), equalTo(nf.constant("one")));
    assertThat(goal1.getStatement(), equalTo(nf.constant("one")));
    assertThat(goal2.getStatement(), equalTo(nf.constant("two")));
  }

  @Test
  public void testEquals()
  {
    assertThat(judgment1.equals(judgment2), is(true));
    assertThat(judgment2.equals(judgment1), is(true));
    assertThat(judgment2.equals(goal1), is(false));
    assertThat(goal1.equals(goal2), is(false));
    assertThat(goal2.equals(judgment1), is(false));
  }

  @Test
  public void testToStringJustWorks()
  {
    assertThat(nf.judgment(nf.constant("one")).truthValue(0.5, 0.5).build(),
        hasToString("one. %0.5;0.5%"));
  }
}
