package org.narson.narsese.provider.util;

import java.util.function.Function;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Sentence;

final public class TermPatterns
{
  private final NarseseFactory nf;
  private final Function<String, Sentence> reading;

  public TermPatterns(Narsese narsese)
  {
    nf = narsese.getNarseseFactory();
    reading = narsese.reading().toSentence();
  }

  public TermPattern compile(String pattern, String resultPattern)
  {
    return new TermPattern(nf, reading.apply(pattern + ".").getStatement(),
        reading.apply(resultPattern + ".").getStatement());
  }

  public TermPattern compile(String pattern1, String pattern2, String resultPattern)
  {
    return new TermPattern(nf, reading.apply(pattern1 + ".").getStatement(),
        reading.apply(resultPattern + ".").getStatement());
  }
}
