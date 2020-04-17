package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseParser.Event;
import org.narson.narsese.provider.ParseResult.SpecialEvent;

public class ParseResultTest
{
  private ParseResult v;

  @Test
  public void testEmptyResultSucess()
  {
    v = ParseResult.emptyResult(new Derivation(0, () -> new Integer(0)));

    assertThat(v.isSuccess(), equalTo(true));
    assertThat(v.getRemainder(), is(not(nullValue())));
    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.popEvent(), equalTo(null));
  }

  @Test
  public void testCharResultSucess()
  {
    v = ParseResult.charResult(new Derivation(0, () -> new Integer(0)), true, 'a');

    assertThat(v.isSuccess(), equalTo(true));
    assertThat(v.getRemainder(), is(not(nullValue())));
    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.popEvent(), equalTo(null));

    v = ParseResult.charResult(new Derivation(0, () -> new Integer(0)), false, 'a');

    assertThat(v.isSuccess(), equalTo(false));
  }

  @Test
  public void testEventResultSucess()
  {
    v = ParseResult.eventResult(new Derivation(0, () -> new Integer(0)), Event.END);

    assertThat(v.isSuccess(), equalTo(true));
    assertThat(v.getRemainder(), is(not(nullValue())));
    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.popEvent(), equalTo(null));
  }

  @Test
  public void testSpecialEventResultSucess()
  {
    v = ParseResult.specialEventResult(new Derivation(0, () -> new Integer(0)),
        SpecialEvent.NUMBER);

    assertThat(v.isSuccess(), equalTo(true));
    assertThat(v.getRemainder(), is(not(nullValue())));
    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.popEvent(), equalTo(null));
  }

  @Test
  public void testSequenceResultSucess()
  {
    v = ParseResult.sequenceResult(new Derivation(0, () -> new Integer(0)), sub());

    assertThat(v.isSuccess(), equalTo(true));
    assertThat(v.getRemainder(), is(not(nullValue())));
    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.popEvent(), equalTo(null));
  }

  @Test
  public void testValueIsExtracted()
  {
    v = seq(car('a'), car('b'), car('c'), sevent(SpecialEvent.WORD));

    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.peekValue(), equalTo("abc"));
    assertThat(v.popEvent(), is(nullValue()));
    assertThat(v.peekValue(), is(nullValue()));

    v = seq(car('a'), seq(car('b'), car('b')), car('c'), sevent(SpecialEvent.WORD));

    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.peekValue(), equalTo("abbc"));
    assertThat(v.popEvent(), is(nullValue()));
    assertThat(v.peekValue(), is(nullValue()));
  }

  @Test
  public void testEventIsExtracted()
  {
    v = seq(car('a'), car('b'), car('c'), event(Event.VALUE_CONSTANT));

    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.VALUE_CONSTANT));
    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.popEvent(), is(nullValue()));

    v = seq(car('a'), seq(car('b'), event(Event.VALUE_CONSTANT)), car('c'),
        event(Event.VALUE_FREQUENCY));

    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.VALUE_CONSTANT));
    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.VALUE_FREQUENCY));
    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.popEvent(), is(nullValue()));

    v = seq(car('a'), seq(car('b'), event(Event.VALUE_FREQUENCY)),
        seq(car('a'), car('b'), car('c'), sevent(SpecialEvent.NUMBER)),
        event(Event.VALUE_CONSTANT));

    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.VALUE_FREQUENCY));
    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), equalTo("abc"));
    assertThat(v.popEvent(), equalTo(Event.VALUE_CONSTANT));
    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.popEvent(), is(nullValue()));
  }

  @Test
  public void testEventValueInterleave()
  {
    v = seq(car('a'), seq(car('b'), event(Event.VALUE_FREQUENCY)),
        seq(car('a'), car('b'), car('c'), sevent(SpecialEvent.NUMBER)),
        event(Event.VALUE_CONSTANT));

    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.VALUE_FREQUENCY));
    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), equalTo("abc"));
    assertThat(v.popEvent(), equalTo(Event.VALUE_CONSTANT));
    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.popEvent(), is(nullValue()));
  }

  @Test
  public void testCompoundEventNotExtracted()
  {
    v = seq(car('a'), car('b'), car('c'), event(Event.START_CONJUNCTION));

    assertThat(v.hasEvent(), equalTo(false));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), is(nullValue()));
  }

  @Test
  public void testCompoundEventExtracted()
  {
    v = seq(sevent(SpecialEvent.START_COMPOUND), car('a'), car('b'), car('c'),
        event(Event.START_CONJUNCTION));

    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.START_CONJUNCTION));
  }

  @Test
  public void testCompoundEventGoodOrder()
  {
    v = seq(sevent(SpecialEvent.START_COMPOUND), car('a'), event(Event.VALUE_FREQUENCY), car('b'),
        car('c'), event(Event.START_CONJUNCTION));

    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.START_CONJUNCTION));
    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.VALUE_FREQUENCY));
  }

  @Test
  public void testCompoundEventGoodOrderDepth()
  {
    v = seq(sevent(SpecialEvent.START_COMPOUND),
        seq(sevent(SpecialEvent.START_COMPOUND), event(Event.START_DISJUNCTION), event(Event.END)),
        event(Event.START_CONJUNCTION), event(Event.END));

    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.START_CONJUNCTION));
    assertThat(v.hasEvent(), equalTo(true));
    assertThat(v.peekValue(), is(nullValue()));
    assertThat(v.popEvent(), equalTo(Event.START_DISJUNCTION));
  }

  private ParseResult[] sub(ParseResult... results)
  {
    if (results == null)
    {
      return new ParseResult[0];
    }
    return results;
  }

  private ParseResult car(char c)
  {
    return ParseResult.charResult(new Derivation(0, () -> new Integer(0)), true, c);
  }

  private ParseResult event(Event e)
  {
    return ParseResult.eventResult(new Derivation(0, () -> new Integer(0)), e);
  }

  private ParseResult sevent(SpecialEvent e)
  {
    return ParseResult.specialEventResult(new Derivation(0, () -> new Integer(0)), e);
  }

  private ParseResult seq(ParseResult... r)
  {
    return ParseResult.sequenceResult(new Derivation(0, () -> new Integer(0)), r);
  }
}
