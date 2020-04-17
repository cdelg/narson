package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.TruthValue;

public class TruthValueTest
{
  private TruthValue v1;
  private TruthValue v2;

  @Test
  public void testEqual()
  {
    v1 = new TruthValueImpl(0.5, 0.5);
    v2 = new TruthValueImpl(0.5, 0.5);
    assertThat(v1, equalTo(v2));

    v1 = new TruthValueImpl(0.5, 0.6);
    v2 = new TruthValueImpl(0.5, 0.5);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new TruthValueImpl(0.6, 0.5);
    v2 = new TruthValueImpl(0.5, 0.5);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new TruthValueImpl(0.5, 0.5);
    v2 = new TruthValueImpl(0.6, 0.5);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new TruthValueImpl(0.5, 0.5);
    v2 = new TruthValueImpl(0.5, 0.6);
    assertThat(v1, is(not(equalTo(v2))));
  }
}
