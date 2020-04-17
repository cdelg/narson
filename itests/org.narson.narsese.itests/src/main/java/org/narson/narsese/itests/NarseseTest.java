package org.narson.narsese.itests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Narsese;
import org.narson.osgisupport.library.OSGiSupport;

public class NarseseTest
{
  OSGiSupport support = OSGiSupport.create(NarseseTest.class);

  @Test
  public void testServiceAvailable()
  {
    final Narsese n = support.getService(Narsese.class, 1000);
    assertThat(n, is(not(nullValue())));
  }
}
