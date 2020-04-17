package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.Tense;

public class QuestionTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  private Question v1;
  private Question v2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.question(nf.constant("a")).build().getValueType(), equalTo(ValueType.QUESTION));
  }

  @Test
  public void testEqual()
  {
    v1 = nf.question(nf.constant("a")).build();
    v2 = nf.question(nf.constant("a")).build();
    assertThat(v1, equalTo(v2));

    v1 = nf.question(nf.constant("a")).tense(Tense.FUTURE).build();
    v2 = nf.question(nf.constant("a")).tense(Tense.FUTURE).build();
    assertThat(v1, equalTo(v2));

    v1 = nf.question(nf.constant("a")).tense(Tense.FUTURE).build();
    v2 = nf.question(nf.constant("a")).tense(Tense.PAST).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.question(nf.constant("a")).tense(Tense.FUTURE).build();
    v2 = nf.question(nf.constant("a")).build();
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.question(nf.constant("a")).tense(Tense.FUTURE).build();
    v2 = nf.question(nf.constant("b")).tense(Tense.PAST).build();
    assertThat(v1, is(not(equalTo(v2))));
  }
}
