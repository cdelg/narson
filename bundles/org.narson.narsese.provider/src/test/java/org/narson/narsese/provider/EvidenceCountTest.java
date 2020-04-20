package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.EvidenceCount;

public class EvidenceCountTest
{
  private EvidenceCount v1;
  private EvidenceCount v2;

  @Test
  public void testEqual()
  {
    v1 = new EvidenceCountImpl(5, 6);
    v2 = new EvidenceCountImpl(5, 6);
    assertThat(v1, equalTo(v2));

    v1 = new EvidenceCountImpl(5, 7);
    v2 = new EvidenceCountImpl(5, 6);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new EvidenceCountImpl(6, 7);
    v2 = new EvidenceCountImpl(5, 7);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new EvidenceCountImpl(5, 7);
    v2 = new EvidenceCountImpl(6, 7);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new EvidenceCountImpl(4, 5);
    v2 = new EvidenceCountImpl(4, 6);
    assertThat(v1, is(not(equalTo(v2))));
  }

  @Test
  public void testConvertionToTruthValue()
  {
    assertThrows(IllegalArgumentException.class, () -> new EvidenceCountImpl(5, 6).toTruthValue(0));
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceCountImpl(5, 6).toTruthValue(-1));

    v1 = new EvidenceCountImpl(1, 2);
    assertThat(v1.toTruthValue(1).getFrequency(), equalTo(0.5));
    assertThat(v1.toTruthValue(1).getConfidence(), closeTo(0.6, 0.1));
    assertThat(v1.toTruthValue(2).getFrequency(), equalTo(0.5));
    assertThat(v1.toTruthValue(2).getConfidence(), equalTo(0.5));

    v1 = new EvidenceCountImpl(2, 8);
    assertThat(v1.toTruthValue(1).toEvidenceCount(1).getPositiveEvidenceCount(), equalTo(2L));
    assertThat(v1.toTruthValue(1).toEvidenceCount(1).getEvidenceCount(), equalTo(8L));

    v1 = new EvidenceCountImpl(3, 4);
    assertThat(v1.toTruthValue(1).toEvidenceCount(1).getPositiveEvidenceCount(), equalTo(3L));
    assertThat(v1.toTruthValue(1).toEvidenceCount(1).getEvidenceCount(), equalTo(4L));
  }

  @Test
  public void testConvertionToFrequenceInterval()
  {
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceCountImpl(5, 6).toFrequencyInterval(0));
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceCountImpl(5, 6).toFrequencyInterval(-1));

    v1 = new EvidenceCountImpl(1, 2);
    assertThat(v1.toFrequencyInterval(1).getLowerBound(), closeTo(0.3, 0.04));
    assertThat(v1.toFrequencyInterval(1).getUpperBound(), closeTo(0.6, 0.07));
    assertThat(v1.toFrequencyInterval(2).getLowerBound(), equalTo(0.25));
    assertThat(v1.toFrequencyInterval(2).getUpperBound(), equalTo(0.75));

    v1 = new EvidenceCountImpl(2, 8);
    assertThat(v1.toFrequencyInterval(1).toEvidenceCount(1).getPositiveEvidenceCount(),
        equalTo(2L));
    assertThat(v1.toFrequencyInterval(1).toEvidenceCount(1).getEvidenceCount(), equalTo(8L));

    v1 = new EvidenceCountImpl(3, 4);
    assertThat(v1.toFrequencyInterval(1).toEvidenceCount(1).getPositiveEvidenceCount(),
        equalTo(3L));
    assertThat(v1.toFrequencyInterval(1).toEvidenceCount(1).getEvidenceCount(), equalTo(4L));
  }

  @Test
  public void testExpectation()
  {
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceCountImpl(5, 6).getExpectation(0));
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceCountImpl(5, 6).getExpectation(-1));

    v1 = new EvidenceCountImpl(3, 7);
    assertThat(v1.getExpectation(1), closeTo(0.43, 0.01));

    v1 = new EvidenceCountImpl(1, 2);
    assertThat(v1.getExpectation(1), closeTo(0.5, 0.00001));

    v1 = new EvidenceCountImpl(3, 7);
    assertThat(v1.toTruthValue(1).getExpectation(), closeTo(v1.getExpectation(1), 0.000000001));
    assertThat(v1.toFrequencyInterval(1).getExpectation(),
        closeTo(v1.getExpectation(1), 0.000000001));
  }
}
