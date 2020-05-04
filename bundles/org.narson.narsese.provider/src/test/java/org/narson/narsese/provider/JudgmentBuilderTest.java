package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.JudgmentBuilder;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Tense;

public class JudgmentBuilderTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private JudgmentBuilder b;
  private Judgment v;

  @Test
  public void testWithoutTenseTruthValue()
  {
    v = nf.judgment(nf.constant("a")).build();

    assertThat(v.getTruthValue(), notNullValue());
    assertThat(v.getTruthValue().getFrequency(), equalTo(1.0));
    assertThat(v.getTruthValue().getConfidence(), equalTo(0.9));
    assertThat(v.getTense(), equalTo(Tense.NONE));
  }


  @Test
  public void testWithTenseTruthValue()
  {
    v = nf.judgment(nf.constant("a")).truthValue(0.5, 0.5).tense(Tense.FUTURE).build();

    assertThat(v.getTruthValue(), notNullValue());
    assertThat(v.getTruthValue().getFrequency(), equalTo(0.5));
    assertThat(v.getTruthValue().getConfidence(), equalTo(0.5));
    assertThat(v.getTense(), equalTo(Tense.FUTURE));

    b = nf.judgment(nf.constant("a"));

    assertThrows(IllegalArgumentException.class, () -> b.truthValue(-0.1, 0.5));
    assertThrows(IllegalArgumentException.class, () -> b.truthValue(1.1, 0.5));
    assertThrows(IllegalArgumentException.class, () -> b.truthValue(0.5, 0));
    assertThrows(IllegalArgumentException.class, () -> b.truthValue(0.5, 1));
    assertThrows(NullPointerException.class, () -> b.tense(null));
  }
}
