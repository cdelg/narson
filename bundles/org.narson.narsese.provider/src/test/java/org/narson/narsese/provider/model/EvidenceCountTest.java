package org.narson.narsese.provider.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.EvidenceAmount;

public class EvidenceCountTest
{
  private EvidenceAmount v1;
  private EvidenceAmount v2;

  @Test
  public void testEqual()
  {
    v1 = new EvidenceAmountImpl(5, 6);
    v2 = new EvidenceAmountImpl(5, 6);
    assertThat(v1, equalTo(v2));

    v1 = new EvidenceAmountImpl(5, 7);
    v2 = new EvidenceAmountImpl(5, 6);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new EvidenceAmountImpl(6, 7);
    v2 = new EvidenceAmountImpl(5, 7);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new EvidenceAmountImpl(5, 7);
    v2 = new EvidenceAmountImpl(6, 7);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new EvidenceAmountImpl(4, 5);
    v2 = new EvidenceAmountImpl(4, 6);
    assertThat(v1, is(not(equalTo(v2))));
  }

  @Test
  public void testConvertionToTruthValue()
  {
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceAmountImpl(5, 6).toTruthValue(0));
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceAmountImpl(5, 6).toTruthValue(-1));

    v1 = new EvidenceAmountImpl(1, 2);
    assertThat(v1.toTruthValue(1).getFrequency(), equalTo(0.5));
    assertThat(v1.toTruthValue(1).getConfidence(), closeTo(0.6, 0.1));
    assertThat(v1.toTruthValue(2).getFrequency(), equalTo(0.5));
    assertThat(v1.toTruthValue(2).getConfidence(), equalTo(0.5));

    v1 = new EvidenceAmountImpl(2, 8);
    assertThat(v1.toTruthValue(1).toEvidenceAmount(1).getPositiveAmountOfEvidence(),
        closeTo(2.0, 0.00001));
    assertThat(v1.toTruthValue(1).toEvidenceAmount(1).getAmountOfEvidence(), closeTo(8.0, 0.00001));

    v1 = new EvidenceAmountImpl(3, 4);
    assertThat(v1.toTruthValue(1).toEvidenceAmount(1).getPositiveAmountOfEvidence(),
        closeTo(3.0, 0.00001));
    assertThat(v1.toTruthValue(1).toEvidenceAmount(1).getAmountOfEvidence(), closeTo(4.0, 0.00001));
  }

  @Test
  public void testConvertionToFrequenceInterval()
  {
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceAmountImpl(5, 6).toFrequencyInterval(0));
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceAmountImpl(5, 6).toFrequencyInterval(-1));

    v1 = new EvidenceAmountImpl(1, 2);
    assertThat(v1.toFrequencyInterval(1).getLowerBound(), closeTo(0.3, 0.04));
    assertThat(v1.toFrequencyInterval(1).getUpperBound(), closeTo(0.6, 0.07));
    assertThat(v1.toFrequencyInterval(2).getLowerBound(), equalTo(0.25));
    assertThat(v1.toFrequencyInterval(2).getUpperBound(), equalTo(0.75));

    v1 = new EvidenceAmountImpl(2, 8);
    assertThat(v1.toFrequencyInterval(1).toEvidenceAmount(1).getPositiveAmountOfEvidence(),
        equalTo(2.0));
    assertThat(v1.toFrequencyInterval(1).toEvidenceAmount(1).getAmountOfEvidence(), equalTo(8.0));

    v1 = new EvidenceAmountImpl(3, 4);
    assertThat(v1.toFrequencyInterval(1).toEvidenceAmount(1).getPositiveAmountOfEvidence(),
        closeTo(3.0, 0.000001));
    assertThat(v1.toFrequencyInterval(1).toEvidenceAmount(1).getAmountOfEvidence(),
        closeTo(4.0, 0.000001));
  }

  @Test
  public void testExpectation()
  {
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceAmountImpl(5, 6).getExpectation(0));
    assertThrows(IllegalArgumentException.class,
        () -> new EvidenceAmountImpl(5, 6).getExpectation(-1));

    v1 = new EvidenceAmountImpl(3, 7);
    assertThat(v1.getExpectation(1), closeTo(0.43, 0.01));

    v1 = new EvidenceAmountImpl(1, 2);
    assertThat(v1.getExpectation(1), closeTo(0.5, 0.00001));

    v1 = new EvidenceAmountImpl(3, 7);
    assertThat(v1.toTruthValue(1).getExpectation(), closeTo(v1.getExpectation(1), 0.000000001));
    assertThat(v1.toFrequencyInterval(1).getExpectation(),
        closeTo(v1.getExpectation(1), 0.000000001));
  }
}
