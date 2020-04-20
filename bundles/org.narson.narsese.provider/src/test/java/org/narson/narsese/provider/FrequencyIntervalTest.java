package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.FrequencyInterval;

public class FrequencyIntervalTest
{
  private FrequencyInterval v1;
  private FrequencyInterval v2;

  @Test
  public void testEqual()
  {
    v1 = new FrequencyIntervalImpl(0.5, 0.6);
    v2 = new FrequencyIntervalImpl(0.5, 0.6);
    assertThat(v1, equalTo(v2));

    v1 = new FrequencyIntervalImpl(0.5, 0.7);
    v2 = new FrequencyIntervalImpl(0.5, 0.6);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new FrequencyIntervalImpl(0.6, 0.7);
    v2 = new FrequencyIntervalImpl(0.5, 0.7);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new FrequencyIntervalImpl(0.5, 0.7);
    v2 = new FrequencyIntervalImpl(0.6, 0.7);
    assertThat(v1, is(not(equalTo(v2))));

    v1 = new FrequencyIntervalImpl(0.4, 0.5);
    v2 = new FrequencyIntervalImpl(0.4, 0.6);
    assertThat(v1, is(not(equalTo(v2))));
  }

  @Test
  public void testIgnorance()
  {
    assertThat(new FrequencyIntervalImpl(0.2, 0.3).getIgnorance(), closeTo(0.1, 0.01));
  }

  @Test
  public void testConvertionToTruthValue()
  {
    v1 = new FrequencyIntervalImpl(0.2, 0.8);
    assertThat(v1.toTruthValue().toFrequencyInterval().getLowerBound(), equalTo(0.2));
    assertThat(v1.toTruthValue().toFrequencyInterval().getUpperBound(), closeTo(0.8, 0.01));

    v1 = new FrequencyIntervalImpl(0.3, 0.4);
    assertThat(v1.toTruthValue().toFrequencyInterval().getLowerBound(), equalTo(0.3));
    assertThat(v1.toTruthValue().toFrequencyInterval().getUpperBound(), equalTo(0.4));
  }

  @Test
  public void testConvertionToEvidenCount()
  {
    assertThrows(IllegalArgumentException.class,
        () -> new FrequencyIntervalImpl(0.5, 0.5).toEvidenceCount(0));
    assertThrows(IllegalArgumentException.class,
        () -> new FrequencyIntervalImpl(0.5, 0.5).toEvidenceCount(-1));

    v1 = new FrequencyIntervalImpl(0.5, 0.9);
    assertThat(v1.toEvidenceCount(1).getEvidenceCount(), equalTo(1L));
    assertThat(v1.toEvidenceCount(1).getPositiveEvidenceCount(), equalTo(1L));
    assertThat(v1.toEvidenceCount(2).getEvidenceCount(), equalTo(3L));
    assertThat(v1.toEvidenceCount(2).getPositiveEvidenceCount(), equalTo(3L));

    v1 = new FrequencyIntervalImpl(0.02, 0.99);
    assertThat(v1.toEvidenceCount(1).toFrequencyInterval(1).getLowerBound(), closeTo(0.02, 0.02));
    assertThat(v1.toEvidenceCount(1).toFrequencyInterval(1).getUpperBound(), closeTo(0.99, 0.5));

    v1 = new FrequencyIntervalImpl(0.3, 0.4);
    assertThat(v1.toEvidenceCount(1).toFrequencyInterval(1).getLowerBound(), closeTo(0.3, 0.5));
    assertThat(v1.toEvidenceCount(1).toFrequencyInterval(1).getUpperBound(), closeTo(0.4, 0.5));
  }
}
