package org.narson.narsese.provider.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Question;
import org.narson.api.narsese.QuestionBuilder;
import org.narson.api.narsese.Tense;
import org.narson.narsese.NarseseProvider;

public class QuestionBuilderTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private QuestionBuilder b;
  private Question v;

  @Test
  public void testWithoutTense()
  {
    v = nf.question(nf.constant("a")).build();

    assertThat(v.getTense(), equalTo(Tense.NONE));
  }


  @Test
  public void testWithTense()
  {
    v = nf.question(nf.constant("a")).tense(Tense.FUTURE).build();

    assertThat(v.getTense(), equalTo(Tense.FUTURE));

    b = nf.question(nf.constant("a"));

    assertThrows(NullPointerException.class, () -> b.tense(null));
  }
}
