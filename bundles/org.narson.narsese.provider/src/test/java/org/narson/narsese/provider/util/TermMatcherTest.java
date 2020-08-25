package org.narson.narsese.provider.util;

import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Sentence;
import org.narson.narsese.NarseseProvider;
import org.narson.narsese.provider.rules.TermPattern;
import org.narson.narsese.provider.rules.TermPatterns;

public class TermMatcherTest
{

  @Test
  public void testCurrentCharEquals()
  {
    final Narsese n = new NarseseProvider();
    final Function<String, Sentence> r = n.reading().toSentence();

    final TermPatterns patterns = new TermPatterns(n);

    final TermPattern p = patterns.compile("(A-->(B & C))", "(A ==> B)");
    p.matcher(r.apply("(a-->(b & c & d& e& f& g)).").getStatement()).matches()
        .forEach(t -> System.out.println(t));
  }

}
