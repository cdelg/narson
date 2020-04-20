package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.NarseseReader;
import org.narson.api.narsese.NarseseWriter;
import org.narson.api.narsese.Sentence;

public class NarseseParsingGeneratorTest implements NarseseChars
{
  private final Narsese n = new NarseseLanguage();

  @Test
  public void testParsingGeneratingWithoutAlteration()
      throws NarseseException, NullPointerException, FileNotFoundException
  {
    List<Sentence> firstRound = null;
    List<Sentence> secondRound = null;

    try (NarseseReader reader = n.createReader(new FileInputStream(
        getClass().getClassLoader().getResource("narsese_sentences.txt").getFile())))
    {
      firstRound = reader.read().collect(Collectors.toList());
    }

    final StringWriter out = new StringWriter();
    try (NarseseWriter writer = n.createWriter(out))
    {
      writer.write(firstRound);
    }
    try (NarseseReader reader = n.createReader(new StringReader(out.getBuffer().toString())))
    {
      secondRound = reader.read().collect(Collectors.toList());
    }

    assertThat(firstRound, equalTo(secondRound));
  }
}
