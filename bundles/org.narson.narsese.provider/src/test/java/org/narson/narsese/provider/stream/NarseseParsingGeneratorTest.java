package org.narson.narsese.provider.stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.Sentence;
import org.narson.narsese.NarseseProvider;

public class NarseseParsingGeneratorTest implements NarseseChars
{
  private final Narsese n = new NarseseProvider();

  @Test
  public void testParsingGeneratingWithoutAlteration()
      throws NarseseException, NullPointerException, FileNotFoundException
  {
    final FileInputStream input = new FileInputStream(
        getClass().getClassLoader().getResource("narsese_sentences.txt").getFile());

    final List<Sentence> firstRound = n.read(input).toSentences();
    final List<Sentence> secondRound = n.read(n.write(firstRound).toOutputString()).toSentences();

    assertThat(firstRound, equalTo(secondRound));
  }
}
