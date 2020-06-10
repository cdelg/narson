package org.narson.narsese.provider.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.narsese.NarseseProvider;

public class QueryTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  @Test
  public void testRightValueType()
  {
    assertThat(nf.query(nf.constant("a")).getValueType(), equalTo(ValueType.QUERY));
  }

  @Test
  public void testConvertion()
  {
    assertThat(nf.query(nf.constant("a")).asQuery(), is(anything()));
    assertThrows(IllegalStateException.class, () -> nf.query(nf.constant("a")).asGoal());
  }

}
