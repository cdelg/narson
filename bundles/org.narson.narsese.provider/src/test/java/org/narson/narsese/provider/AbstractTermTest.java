package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.NarseseFactory;

public class AbstractTermTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  @Test
  public void testToStringJustWorks()
  {
    assertThat(nf.queryVariable("var"), hasToString("?var"));
  }

  @Test
  public void testSimplicity()
  {
    assertThat(nf.constant("var").getSyntacticSimplicity(1), equalTo(1.0));
    assertThat(nf.copulaTerm(nf.constant("var"), Copula.IMPLICATION, nf.constant("var"))
        .getSyntacticSimplicity(1), closeTo(0.33, 0.01));
    assertThat(nf.copulaTerm(nf.constant("var"), Copula.IMPLICATION, nf.constant("var"))
        .getSyntacticSimplicity(2), closeTo(0.11, 0.01));
  }
}
