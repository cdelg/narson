package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

  @Test
  public void testConvertionToFrequency()
  {
    v1 = new TruthValueImpl(0.5, 0.5);
    assertThat(v1.toFrequencyInterval().getLowerBound(), equalTo(0.25));
    assertThat(v1.toFrequencyInterval().getUpperBound(), equalTo(0.75));

    v1 = new TruthValueImpl(1.0, 0.9);
    assertThat(v1.toFrequencyInterval().toTruthValue().getFrequency(), equalTo(1.0));
    assertThat(v1.toFrequencyInterval().toTruthValue().getConfidence(), equalTo(0.9));

    v1 = new TruthValueImpl(0.4, 0.3);
    assertThat(v1.toFrequencyInterval().toTruthValue().getFrequency(), equalTo(0.4));
    assertThat(v1.toFrequencyInterval().toTruthValue().getConfidence(), equalTo(0.3));
  }

  @Test
  public void testConvertionToEvidenCount()
  {
    assertThrows(IllegalArgumentException.class,
        () -> new TruthValueImpl(0.5, 0.5).toEvidenceCount(0));
    assertThrows(IllegalArgumentException.class,
        () -> new TruthValueImpl(0.5, 0.5).toEvidenceCount(-1));

    v1 = new TruthValueImpl(1, 0.9);
    assertThat(v1.toEvidenceCount(1).getEvidenceCount(), equalTo(9L));
    assertThat(v1.toEvidenceCount(1).getPositiveEvidenceCount(), equalTo(9L));
    assertThat(v1.toEvidenceCount(2).getEvidenceCount(), equalTo(18L));
    assertThat(v1.toEvidenceCount(2).getPositiveEvidenceCount(), equalTo(18L));

    v1 = new TruthValueImpl(0.02, 0.99);
    assertThat(v1.toEvidenceCount(1).toTruthValue(1).getFrequency(), closeTo(0.02, 0.002));
    assertThat(v1.toEvidenceCount(1).toTruthValue(1).getConfidence(), equalTo(0.99));

    v1 = new TruthValueImpl(0.5, 0.5);
    assertThat(v1.toEvidenceCount(1).toTruthValue(1).getFrequency(), closeTo(0.5, 0.5));
    assertThat(v1.toEvidenceCount(1).toTruthValue(1).getConfidence(), equalTo(0.5));

    v1 = new TruthValueImpl(0.4, 0.3);
    assertThat(v1.toEvidenceCount(1).toTruthValue(1).getFrequency(), closeTo(0.4, 0.5));
    assertThat(v1.toEvidenceCount(1).toTruthValue(1).getConfidence(), closeTo(0.3, 0.5));
  }

  @Test
  public void testRevision()
  {
    assertThrows(NullPointerException.class, () -> new TruthValueImpl(1, 0.9).revise(null));

    v1 = new TruthValueImpl(1, 0.9);
    assertThat(v1.revise(v1).getFrequency(), equalTo(1.0));
    assertThat(v1.revise(v1).getConfidence(), closeTo(0.9, 0.94));

    v1 = new TruthValueImpl(1, 0.9);
    assertThat(v1.revise(new TruthValueImpl(1, 0.5)).getFrequency(), equalTo(1.0));
    assertThat(v1.revise(new TruthValueImpl(1, 0.5)).getConfidence(), closeTo(0.9, 0.909));

    v1 = new TruthValueImpl(1, 0.9);
    assertThat(v1.revise(new TruthValueImpl(0.2, 0.9)).getFrequency(), equalTo(0.6));
    assertThat(v1.revise(new TruthValueImpl(0.2, 0.9)).getConfidence(), closeTo(0.9, 0.94));

    v1 = new TruthValueImpl(1, 0.9);
    assertThat(v1.revise(new TruthValueImpl(0.2, 0.3)).getFrequency(), closeTo(0.96, 0.963));
    assertThat(v1.revise(new TruthValueImpl(0.2, 0.3)).getConfidence(), closeTo(0.9, 0.904));
  }

  @Test
  public void testExpectation()
  {
    v1 = new TruthValueImpl(1, 0.9);
    assertThat(v1.getExpectation(), equalTo(0.95));

    v1 = new TruthValueImpl(0.5, 0.2);
    assertThat(v1.getExpectation(), equalTo(0.5));

    v1 = new TruthValueImpl(1, 0.9);
    assertThat(v1.toFrequencyInterval().getExpectation(), equalTo(v1.getExpectation()));
    assertThat(v1.toEvidenceCount(1).getExpectation(1), closeTo(v1.getExpectation(), 0.000000001));
  }
}
