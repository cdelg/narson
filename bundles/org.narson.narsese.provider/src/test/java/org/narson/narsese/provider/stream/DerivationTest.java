package org.narson.narsese.provider.stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import org.junit.jupiter.api.Test;

public class DerivationTest
{
  private Derivation d;

  @Test
  public void testCurrentCharEquals()
  {
    d = new Derivation(0, () -> 0);
    assertThat(d.currentCharEquals(0).isSuccess(), equalTo(true));

    d = new Derivation(0, () -> 0, 0);
    assertThat(d.currentCharEquals(0).isSuccess(), equalTo(true));

    d = new Derivation(0, () -> 1, 0);
    assertThat(d.currentCharEquals(0).isSuccess(), equalTo(true));

    d = new Derivation(0, () -> 1);
    assertThat(d.currentCharEquals(0).isSuccess(), equalTo(false));

    d = new Derivation(0, () -> 1, 1);
    assertThat(d.currentCharEquals(0).isSuccess(), equalTo(false));

    d = new Derivation(0, () -> 1, 1);
    assertThat(d.currentCharEquals(0).isSuccess(), equalTo(false));
  }

  @Test
  public void testCurrentCharSatisfies()
  {
    d = new Derivation(0, () -> 0);
    assertThat(d.currentCharSatisfies(i -> i.equals(0)).isSuccess(), equalTo(true));

    d = new Derivation(0, () -> 0, 0);
    assertThat(d.currentCharSatisfies(i -> i.equals(0)).isSuccess(), equalTo(true));

    d = new Derivation(0, () -> 1, 0);
    assertThat(d.currentCharSatisfies(i -> i.equals(0)).isSuccess(), equalTo(true));

    d = new Derivation(0, () -> 1);
    assertThat(d.currentCharSatisfies(i -> i.equals(0)).isSuccess(), equalTo(false));

    d = new Derivation(0, () -> 1, 1);
    assertThat(d.currentCharSatisfies(i -> i.equals(0)).isSuccess(), equalTo(false));

    d = new Derivation(0, () -> 1, 1);
    assertThat(d.currentCharSatisfies(i -> i.equals(0)).isSuccess(), equalTo(false));
  }

  @Test
  public void testResultComputedOnce()
  {
    d = new Derivation(0, () -> 0);

    final ParseResult r = d.parse(NarseseSymbols.SENTENCE);
    assertThat(r, sameInstance(d.parse(NarseseSymbols.SENTENCE)));
    assertThat(r, sameInstance(d.parseSentence()));
  }
}
