package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.NarseseGenerator;

public class NarseseGeneratorTest implements NarseseChars
{
  private final Narsese n = new NarseseProvider();
  private final StringWriter out = new StringWriter();
  private final NarseseGenerator g = n.createGenerator(out);

  @Test
  public void testCloseSucces()
  {
    g.writeStartJudgment().writeConstant("a").writeEnd();
    g.close();
  }

  @Test
  public void testCloseFailureDuToInvalidNarsese()
  {
    g.writeStartJudgment().writeConstant("a");
    assertThrows(NarseseException.class, () -> g.close());
  }

  @Test
  public void testFlushIsWorking() throws IOException
  {
    g.writeStartJudgment().writeConstant("a").writeEnd();
    assertThat(out.getBuffer().length(), equalTo(0));

    g.flush();

    assertThat(out.getBuffer().toString(), equalTo("a" + CHAR_END_JUDGMENT));
  }
}
