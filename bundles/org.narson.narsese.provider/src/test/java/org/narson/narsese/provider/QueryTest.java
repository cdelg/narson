package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;

public class QueryTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  @Test
  public void testRightValueType()
  {
    assertThat(nf.query(nf.constant("a")).getValueType(), equalTo(ValueType.QUERY));
  }
}
