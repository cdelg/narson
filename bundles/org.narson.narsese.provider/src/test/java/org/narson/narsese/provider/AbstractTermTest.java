package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseFactory;

public class AbstractTermTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  @Test
  public void testToStringJustWorks()
  {
    assertThat(nf.queryVariable("var"), hasToString("?var"));
  }
}
