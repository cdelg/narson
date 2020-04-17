package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.Tense;

public class JudgementTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  private Judgment v1;
  private Judgment v2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.judgment(nf.constant("a")).build().getValueType(), equalTo(ValueType.JUDGMENT));
  }

  @Test
  public void testEqual()
  {
    v1 = nf.judgment(nf.constant("a")).build();
    v2 = nf.judgment(nf.constant("a")).build();
    assertThat(v1, equalTo(v2));

    v1 = nf.judgment(nf.constant("a")).build();
    v2 = nf.judgment(nf.constant("a")).truthValue(1, 0.9).build();
    assertThat(v1, equalTo(v2));

    v1 = nf.judgment(nf.constant("a")).truthValue(0.5, 0.5).build();
    v2 = nf.judgment(nf.constant("a")).truthValue(0.5, 0.5).build();
    assertThat(v1, equalTo(v2));

    v1 = nf.judgment(nf.constant("a")).truthValue(0.5, 0.5).tense(Tense.FUTURE).build();
    v2 = nf.judgment(nf.constant("a")).truthValue(0.5, 0.5).tense(Tense.FUTURE).build();
    assertThat(v1, equalTo(v2));

    v1 = nf.judgment(nf.constant("a")).truthValue(0.5, 0.5).tense(Tense.FUTURE).build();
    v2 = nf.judgment(nf.constant("a")).truthValue(0.5, 0.5).tense(Tense.PAST).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.judgment(nf.constant("a")).truthValue(0.5, 0.5).tense(Tense.FUTURE).build();
    v2 = nf.judgment(nf.constant("a")).truthValue(0.5, 0.5).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.judgment(nf.constant("a")).truthValue(1, 0.9).build();
    v2 = nf.judgment(nf.constant("a")).truthValue(1, 0.8).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.judgment(nf.constant("a")).truthValue(1, 0.9).build();
    v2 = nf.judgment(nf.constant("a")).truthValue(0.9, 0.9).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.judgment(nf.constant("a")).truthValue(1, 0.9).build();
    v2 = nf.judgment(nf.constant("b")).truthValue(1, 0.9).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.judgment(nf.constant("a")).truthValue(0.9, 0.9).build();
    v2 = nf.judgment(nf.constant("b")).truthValue(1, 0.8).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.judgment(nf.constant("a")).truthValue(0.9, 0.9).tense(Tense.FUTURE).build();
    v2 = nf.judgment(nf.constant("b")).truthValue(1, 0.8).tense(Tense.PAST).build();
    assertThat(v1, is(not(equalTo(v2))));
  }
}
