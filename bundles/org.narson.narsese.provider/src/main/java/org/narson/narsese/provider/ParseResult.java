package org.narson.narsese.provider;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.narson.api.narsese.NarseseParser.Event;

final class ParseResult
{
  enum SpecialEvent
  {
    WORD, NUMBER, START_COMPOUND
  }

  private final Derivation remainder;
  private final boolean success;
  private final Event event;
  private final SpecialEvent specialEvent;
  private final Integer valueEvent;
  private final ParseResult[] subResults;
  private Deque<InternalEvent> internalEvents = null;

  private ParseResult(Derivation remainder, boolean success, Event event, SpecialEvent specialEvent,
      Integer valueEvent, ParseResult[] subResults)
  {
    this.remainder = remainder;
    this.success = success;
    this.event = event;
    this.specialEvent = specialEvent;
    this.valueEvent = valueEvent;
    this.subResults = subResults;
  }

  public static ParseResult emptyResult(Derivation remainder)
  {
    return new ParseResult(remainder, true, null, null, null, null);
  }

  public static ParseResult charResult(Derivation remainder, boolean success, int value)
  {
    return new ParseResult(remainder, success, null, null, value, null);
  }

  public static ParseResult eventResult(Derivation remainder, Event event)
  {
    return new ParseResult(remainder, true, event, null, null, null);
  }

  public static ParseResult specialEventResult(Derivation remainder, SpecialEvent event)
  {
    return new ParseResult(remainder, true, null, event, null, null);
  }

  public static ParseResult sequenceResult(Derivation remainder, ParseResult[] subResults)
  {
    return new ParseResult(remainder, true, null, null, null, subResults);
  }

  public Derivation getRemainder()
  {
    return remainder;
  }

  public boolean isSuccess()
  {
    return success;
  }

  public String peekValue()
  {
    if (internalEvents == null)
    {
      internalEvents = computeEvents(new ArrayDeque<>());
    }

    final InternalEvent e = internalEvents.peekFirst();
    if (e != null && e.value != null)
    {
      return e.value;
    } else
    {
      return null;
    }
  }

  public Event popEvent()
  {
    if (internalEvents == null)
    {
      internalEvents = computeEvents(new ArrayDeque<>());
    }

    InternalEvent e = internalEvents.pollFirst();
    while (e != null && e.event == null)
    {
      e = internalEvents.pollFirst();
    }
    return e != null ? e.event : null;
  }

  public boolean hasEvent()
  {
    if (internalEvents == null)
    {
      internalEvents = computeEvents(new ArrayDeque<>());
    }

    if (!internalEvents.isEmpty())
    {
      if (internalEvents.peekFirst().event != null)
      {
        return true;
      } else
      {
        return internalEvents.stream().anyMatch(e -> e.event != null);
      }
    } else
    {
      return false;
    }
  }

  private void fillValue(StringBuilder sb)
  {
    if (subResults != null)
    {
      for (int i = 0; i < subResults.length; i++)
      {
        subResults[i].fillValue(sb);
      }
    } else if (valueEvent != null)
    {
      sb.append(Character.toChars(valueEvent));
    }
  }

  private Deque<InternalEvent> computeEvents(Deque<Event> compounds)
  {
    final Deque<InternalEvent> internalEvents = new ArrayDeque<>();

    if (subResults != null && subResults.length != 0)
    {
      if (subResults[subResults.length - 1].specialEvent != null
          && (subResults[subResults.length - 1].specialEvent == SpecialEvent.NUMBER
              || subResults[subResults.length - 1].specialEvent == SpecialEvent.WORD))
      {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subResults.length; i++)
        {
          subResults[i].fillValue(sb);
        }
        internalEvents.add(new InternalEvent(sb.toString()));
      } else if (subResults[0].specialEvent != null
          && subResults[0].specialEvent == SpecialEvent.START_COMPOUND)
      {
        compounds.addFirst(Event.END);
        final List<InternalEvent> tmpEvents = new ArrayList<>();
        for (int i = 1; i < subResults.length; i++)
        {
          if (subResults[i].event != null)
          {
            if (isStartCompound(subResults[i].event))
            {
              if (compounds.getFirst() == Event.END)
              {
                compounds.pollFirst();
                compounds.addFirst(subResults[i].event);
              }
            } else
            {
              tmpEvents.add(new InternalEvent(subResults[i].event));
            }
          } else
          {
            tmpEvents.addAll(subResults[i].computeEvents(compounds));
          }
        }
        internalEvents.add(new InternalEvent(compounds.pollFirst()));
        internalEvents.addAll(tmpEvents);
      } else
      {
        for (int i = 0; i < subResults.length; i++)
        {
          if (subResults[i].event != null)
          {
            if (isStartCompound(subResults[i].event))
            {
              if (compounds.peekFirst() == Event.END)
              {
                compounds.pollFirst();
                compounds.addFirst(subResults[i].event);
              }
            } else
            {
              internalEvents.add(new InternalEvent(subResults[i].event));
            }
          } else
          {
            internalEvents.addAll(subResults[i].computeEvents(compounds));
          }
        }
      }
    }

    return internalEvents;
  }

  private boolean isStartCompound(Event event)
  {
    switch (event)
    {
      case START_CONCURRENT_EQUIVALENCE_COPULA:
        return true;
      case START_CONCURRENT_IMPLICATION_COPULA:
        return true;
      case START_CONJUNCTION:
        return true;
      case START_DISJUNCTION:
        return true;
      case START_EQUIVALENCE_COPULA:
        return true;
      case START_EXTENSIONAL_DIFFERENCE:
        return true;
      case START_EXTENSIONAL_IMAGE:
        return true;
      case START_EXTENSIONAL_INTERSECTION:
        return true;
      case START_EXTENSIONAL_SET:
        return true;
      case START_IMPLICATION_COPULA:
        return true;
      case START_INHERITANCE_COPULA:
        return true;
      case START_INSTANCE_COPULA:
        return true;
      case START_INSTANCE_PROPERTY_COPULA:
        return true;
      case START_INTENSIONAL_DIFFERENCE:
        return true;
      case START_INTENSIONAL_IMAGE:
        return true;
      case START_INTENSIONAL_INTERSECTION:
        return true;
      case START_INTENSIONAL_SET:
        return true;
      case START_NEGATION:
        return true;
      case START_PARALLEL_CONJUNCTION:
        return true;
      case START_PREDICTIVE_EQUIVALENCE_COPULA:
        return true;
      case START_PREDICTIVE_IMPLICATION_COPULA:
        return true;
      case START_PRODUCT:
        return true;
      case START_PROPERTY_COPULA:
        return true;
      case START_RETROSPECTIVE_IMPLICATION_COPULA:
        return true;
      case START_SEQUENTIAL_CONJUNCTION:
        return true;
      case START_SIMILARITY_COPULA:
        return true;
      default:
        return false;
    }
  }

  private class InternalEvent
  {
    public Event event;
    public String value;

    public InternalEvent(Event event)
    {
      this.event = event;
      value = null;
    }

    public InternalEvent(String value)
    {
      event = null;
      this.value = value;
    }
  }

  @Override
  public String toString()
  {
    if (event != null)
    {
      return "Event[" + event + "]";
    } else if (specialEvent != null)
    {
      return "SpecialEvent[" + specialEvent + "]";
    } else if (valueEvent != null)
    {
      return "ValuEvent[" + new String(Character.toChars(valueEvent)) + ", Success=" + success
          + "]";
    } else
    {
      return "EmptyEvent";
    }
  }
}
