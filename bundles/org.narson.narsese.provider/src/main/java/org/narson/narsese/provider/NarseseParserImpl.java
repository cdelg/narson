package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkState;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import org.narson.api.narsese.NarseseException;
import org.narson.api.narsese.NarseseLocation;
import org.narson.api.narsese.NarseseParser;
import org.narson.api.narsese.NarseseParsingException;
import org.narson.narsese.provider.ParseResult.SpecialEvent;

final class NarseseParserImpl implements NarseseParser, NarseseChars
{
  private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

  private final BufferedReader reader;
  private boolean closed;

  private long columnNumber = 0;
  private long lineNumber = 1;
  private long streamOffset = 0;
  private int previousChar = CHAR_SPACE;

  private ParseResult currentResult = ParseResult.emptyResult(new Derivation(-1, this::readChar));

  NarseseParserImpl(final Reader reader, int bufferSize)
  {
    this.reader = new BufferedReader(reader, bufferSize);
  }

  NarseseParserImpl(final InputStream in, int bufferSize)
  {
    this(new InputStreamReader(in, UTF8_CHARSET), bufferSize);
  }

  NarseseParserImpl(final InputStream in, final Charset encoding, int bufferSize)
  {
    this(new InputStreamReader(in, encoding), bufferSize);
  }

  private int readChar()
  {
    int nextChar;

    try
    {
      nextChar = reader.read();
    } catch (final IOException exception)
    {
      throw new NarseseException(exception.getMessage(), exception);
    }

    streamOffset++;
    columnNumber++;

    if ((nextChar == CHAR_EOL && previousChar != CHAR_CR) || nextChar == CHAR_CR)
    {
      columnNumber = 0;
      lineNumber++;
    }

    return previousChar = nextChar;
  }

  @Override
  public NarseseLocation getLocation()
  {
    return new Location(columnNumber, lineNumber, streamOffset);
  }

  @Override
  public boolean hasNext() throws NarseseException, NarseseParsingException
  {
    if (!currentResult.hasEvent())
    {
      try
      {
        loadNextSentence();
        return currentResult.hasEvent();
      } catch (final NarseseParsingException pE)
      {
        throw new NarseseParsingException(pE.getMessage(), pE.getCause(), pE.getLocation());
      } catch (final NarseseException e)
      {
        throw new NarseseException(e.getMessage(), e.getCause());
      }
    } else
    {
      return true;
    }
  }

  @Override
  public Event next() throws NarseseException, NarseseParsingException, NoSuchElementException
  {
    Event event = currentResult.popEvent();
    if (event == null)
    {
      try
      {
        loadNextSentence();
        event = currentResult.popEvent();
      } catch (final NarseseParsingException pE)
      {
        throw new NarseseParsingException(pE.getMessage(), pE.getCause(), pE.getLocation());
      } catch (final NarseseException e)
      {
        throw new NarseseException(e.getMessage(), e.getCause());
      }
    }

    if (event != null)
    {
      return event;
    } else
    {
      throw new NoSuchElementException("THe parser has no more events.");
    }
  }

  private void loadNextSentence() throws NarseseException, NarseseParsingException
  {
    if (currentResult.getRemainder().getStreamOffset() < streamOffset)
    {
      currentResult = new Derivation(streamOffset, this::readChar, previousChar).parseSentence();
    } else
    {
      currentResult = new Derivation(streamOffset, this::readChar).parseSentence();
    }

    if (!currentResult.isSuccess())
    {
      final NarseseLocation location = getLocation();
      if (previousChar == CHAR_EOF)
      {
        throw new NarseseParsingException("Unexpected end of stream at ligne "
            + location.getLineNumber() + ", column " + location.getColumnNumber() + ". ", location);
      } else if (Character.isWhitespace(previousChar))
      {
        throw new NarseseParsingException("Unexpected whitespace at ligne "
            + location.getLineNumber() + ", column " + location.getColumnNumber() + ". ", location);
      } else
      {
        throw new NarseseParsingException(
            "Unexpected character `" + new String(Character.toChars(previousChar)) + "' at ligne "
                + location.getLineNumber() + ", column " + location.getColumnNumber() + ". ",
            location);
      }
    }
  }

  @Override
  public String getString() throws IllegalStateException
  {
    checkState(currentResult.isSuccess(), "No String value in the current state.");

    final String value = currentResult.peekValue();

    checkState(value != null, "No String value in the current state.");

    return value;
  }

  @Override
  public double getDouble() throws IllegalStateException
  {
    try
    {
      return Double.parseDouble(getString());
    } catch (final IllegalStateException e)
    {
      throw new IllegalStateException("No Double value in the current state.");
    } catch (final NumberFormatException e)
    {
      throw new IllegalStateException("No Double value in the current state.");
    }
  }

  @Override
  public void close() throws NarseseException
  {
    if (!closed)
    {
      try
      {
        reader.close();
      } catch (final IOException e)
      {
        throw new NarseseException(e.getMessage(), e);
      } finally
      {
        closed = true;
      }
    }
  }

  static class GrammarSpec implements NarseseSymbols, NarseseChars
  {
    static Map<Integer, Function<Derivation, ParseResult>> instance = new GrammarSpec().specs;

    private final Map<Integer, Function<Derivation, ParseResult>> specs = new HashMap<>();

    private GrammarSpec()
    {
      add(SENTENCE, seq(SKIP, Event.START_JUDGMENT, JUDGMENT, Event.END),
          seq(SKIP, Event.START_GOAL, GOAL, Event.END),
          seq(SKIP, Event.START_QUESTION, QUESTION, Event.END),
          seq(SKIP, Event.START_QUERY, QUERY, Event.END), seq(SKIP, CHAR_EOF));
      add(JUDGMENT, seq(TENSE, SKIP, TERM, SKIP, CHAR_END_JUDGMENT, SKIP, TRUTH),
          seq(TENSE, SKIP, TERM, SKIP, CHAR_END_JUDGMENT),
          seq(TERM, SKIP, CHAR_END_JUDGMENT, SKIP, TRUTH), seq(TERM, SKIP, CHAR_END_JUDGMENT));
      add(GOAL, seq(TERM, SKIP, CHAR_END_GOAL, SKIP, TRUTH), seq(TERM, SKIP, CHAR_END_GOAL));
      add(QUESTION, seq(TENSE, SKIP, TERM, SKIP, CHAR_END_QUESTION),
          seq(TERM, SKIP, CHAR_END_QUESTION));
      add(QUERY, seq(TERM, SKIP, CHAR_END_QUERY));


      add(TERM, seq(Event.VALUE_CONSTANT, WORD), seq(VARIABLE),
          seq(SpecialEvent.START_COMPOUND, RELATION, Event.END),
          seq(Event.START_OPERATION, OPERATION, Event.END),
          seq(SpecialEvent.START_COMPOUND, COMPOUND_TERM, Event.END));
      add(TERMS, seq(TERM, SKIP, TERMS), seq(TERM));
      add(PREFIXED_TERMS, seq(BIPLUS_CONNECTOR, SKIP, TERM, SKIP, PREFIXED_TERMS),
          seq(BIPLUS_CONNECTOR, SKIP, TERM));

      add(VARIABLE, seq(Event.START_DEPENDENT_VARIABLE, DEPENDENT_VARIABLE, Event.END),
          seq(Event.VALUE_INDEPENDENT_VARIABLE, INDEPENDENT_VARIABLE),
          seq(Event.START_DEPENDENT_VARIABLE, ANONYMOUS_DEPENDENT_VARIABLE, Event.END),
          seq(Event.VALUE_QUERY_VARIABLE, QUERY_VARIABLE));
      add(DEPENDENT_VARIABLE, seq(CHAR_VARIABLE_MARK, WORD, CHAR_START_BLOCK, SKIP, CHAR_END_BLOCK),
          seq(CHAR_VARIABLE_MARK, WORD, CHAR_START_BLOCK, SKIP,
              Event.VALUE_DEPENDENT_INDEPENDENT_VARIABLE, WORD, SKIP, CHAR_END_BLOCK),
          seq(CHAR_VARIABLE_MARK, WORD, CHAR_START_BLOCK, SKIP,
              Event.VALUE_DEPENDENT_INDEPENDENT_VARIABLE, WORD, SKIP,
              DEPENDENT_INDEPENDENT_VARIABLES, SKIP, CHAR_END_BLOCK));
      add(DEPENDENT_INDEPENDENT_VARIABLES,
          seq(Event.VALUE_DEPENDENT_INDEPENDENT_VARIABLE, WORD, SKIP,
              DEPENDENT_INDEPENDENT_VARIABLES),
          seq(Event.VALUE_DEPENDENT_INDEPENDENT_VARIABLE, WORD));
      add(INDEPENDENT_VARIABLE, seq(CHAR_VARIABLE_MARK, WORD));
      add(ANONYMOUS_DEPENDENT_VARIABLE, seq(CHAR_VARIABLE_MARK, EMPTY_WORD));
      add(QUERY_VARIABLE, seq(CHAR_QUERY_VARIABLE_MARK, WORD),
          seq(CHAR_QUERY_VARIABLE_MARK, EMPTY_WORD));

      add(RELATION,
          seq(CHAR_START_BLOCK, SKIP, TERM, SKIP, COPULA, SKIP, TERM, SKIP, CHAR_END_BLOCK));
      add(COPULA, seq(INHERITANCE, Event.START_INHERITANCE_COPULA),
          seq(SIMILARITY, Event.START_SIMILARITY_COPULA),
          seq(IMPLICATION, Event.START_IMPLICATION_COPULA),
          seq(EQUIVALENCE, Event.START_EQUIVALENCE_COPULA),
          seq(INSTANCE, Event.START_INSTANCE_COPULA), seq(PROPERTY, Event.START_PROPERTY_COPULA),
          seq(INSTANCE_PROPERTY, Event.START_INSTANCE_PROPERTY_COPULA),
          seq(PREDICTIVE_IMPLICATION, Event.START_PREDICTIVE_IMPLICATION_COPULA),
          seq(RETROSPECTIVE_IMPLICATION, Event.START_RETROSPECTIVE_IMPLICATION_COPULA),
          seq(CONCURRENT_IMPLICATION, Event.START_CONCURRENT_IMPLICATION_COPULA),
          seq(PREDICTIVE_EQUIVALENCE, Event.START_PREDICTIVE_EQUIVALENCE_COPULA),
          seq(CONCURRENT_EQUIVALENCE, Event.START_CONCURRENT_EQUIVALENCE_COPULA));
      add(INHERITANCE, seq(CHAR_INHERITANCE_0, CHAR_INHERITANCE_1, CHAR_INHERITANCE_2));
      add(SIMILARITY, seq(CHAR_SIMILARITY_0, CHAR_SIMILARITY_1, CHAR_SIMILARITY_2));
      add(IMPLICATION, seq(CHAR_IMPLICATION_0, CHAR_IMPLICATION_1, CHAR_IMPLICATION_2));
      add(EQUIVALENCE, seq(CHAR_EQUIVALENCE_0, CHAR_EQUIVALENCE_1, CHAR_EQUIVALENCE_2));
      add(INSTANCE, seq(CHAR_INSTANCE_0, CHAR_INSTANCE_1, CHAR_INSTANCE_2));
      add(PROPERTY, seq(CHAR_PROPERTY_0, CHAR_PROPERTY_1, CHAR_PROPERTY_2));
      add(INSTANCE_PROPERTY,
          seq(CHAR_INSTANCE_PROPERTY_0, CHAR_INSTANCE_PROPERTY_1, CHAR_INSTANCE_PROPERTY_2));
      add(PREDICTIVE_IMPLICATION, seq(CHAR_PREDICTIVE_IMPLICATION_0, CHAR_PREDICTIVE_IMPLICATION_1,
          CHAR_PREDICTIVE_IMPLICATION_2));
      add(RETROSPECTIVE_IMPLICATION, seq(CHAR_RETROSPECTIVE_IMPLICATION_0,
          CHAR_RETROSPECTIVE_IMPLICATION_1, CHAR_RETROSPECTIVE_IMPLICATION_2));
      add(CONCURRENT_IMPLICATION, seq(CHAR_CONCURRENT_IMPLICATION_0, CHAR_CONCURRENT_IMPLICATION_1,
          CHAR_CONCURRENT_IMPLICATION_2));
      add(PREDICTIVE_EQUIVALENCE, seq(CHAR_PREDICTIVE_EQUIVALENCE_0, CHAR_PREDICTIVE_EQUIVALENCE_1,
          CHAR_PREDICTIVE_EQUIVALENCE_2));
      add(CONCURRENT_EQUIVALENCE, seq(CHAR_CONCURRENT_EQUIVALENCE_0, CHAR_CONCURRENT_EQUIVALENCE_1,
          CHAR_CONCURRENT_EQUIVALENCE_2));

      add(OPERATION,
          seq(CHAR_START_BLOCK, SKIP, CHAR_OPERATION, WORD, SKIP, TERM, SKIP, TERMS, SKIP,
              CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, CHAR_OPERATION, WORD, SKIP, TERM, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, CHAR_OPERATION, WORD, SKIP, CHAR_END_BLOCK));

      add(COMPOUND_TERM,
          seq(CHAR_START_BLOCK, SKIP, CHAR_NEGATION, Event.START_NEGATION, SKIP, TERM, SKIP,
              CHAR_END_BLOCK),
          seq(CHAR_NEGATION, Event.START_NEGATION, SKIP, TERM),
          seq(CHAR_START_EXTENSIONAL_SET, Event.START_EXTENSIONAL_SET, SKIP, TERM, SKIP, TERMS,
              SKIP, CHAR_END_EXTENSIONAL_SET),
          seq(CHAR_START_EXTENSIONAL_SET, Event.START_EXTENSIONAL_SET, SKIP, TERM, SKIP,
              CHAR_END_EXTENSIONAL_SET),
          seq(CHAR_START_INTENSIONAL_SET, Event.START_INTENSIONAL_SET, SKIP, TERM, SKIP, TERMS,
              SKIP, CHAR_END_INTENSIONAL_SET),
          seq(CHAR_START_INTENSIONAL_SET, Event.START_INTENSIONAL_SET, SKIP, TERM, SKIP,
              CHAR_END_INTENSIONAL_SET),
          seq(CHAR_START_BLOCK, SKIP, BIPLUS_CONNECTOR, SKIP, TERM, SKIP, TERMS, SKIP,
              CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, TERM, SKIP, PREFIXED_TERMS, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, BI_CONNECTOR, SKIP, TERM, SKIP, TERM, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, TERM, SKIP, BI_CONNECTOR, SKIP, TERM, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, IMAGE, SKIP, TERM, SKIP, TERMS, SKIP, CHAR_PLACEHOLDER,
              Event.VALUE_PLACEHOLDER, SKIP, TERM, SKIP, TERMS, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, IMAGE, SKIP, TERM, SKIP, TERMS, SKIP, CHAR_PLACEHOLDER,
              Event.VALUE_PLACEHOLDER, SKIP, TERM, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, IMAGE, SKIP, TERM, SKIP, TERMS, SKIP, CHAR_PLACEHOLDER,
              Event.VALUE_PLACEHOLDER, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, IMAGE, SKIP, TERM, SKIP, CHAR_PLACEHOLDER,
              Event.VALUE_PLACEHOLDER, SKIP, TERM, SKIP, TERMS, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, IMAGE, SKIP, TERM, SKIP, CHAR_PLACEHOLDER,
              Event.VALUE_PLACEHOLDER, SKIP, TERM, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, IMAGE, SKIP, CHAR_PLACEHOLDER, Event.VALUE_PLACEHOLDER, SKIP,
              TERM, SKIP, TERMS, SKIP, CHAR_END_BLOCK),
          seq(CHAR_START_BLOCK, SKIP, IMAGE, SKIP, CHAR_PLACEHOLDER, Event.VALUE_PLACEHOLDER, SKIP,
              TERM, SKIP, CHAR_END_BLOCK));

      add(BI_CONNECTOR, seq(EXTENSIONAL_DIFFERENCE, Event.START_EXTENSIONAL_DIFFERENCE),
          seq(INTENSIONAL_DIFFERENCE, Event.START_INTENSIONAL_DIFFERENCE));
      add(BIPLUS_CONNECTOR, seq(PRODUCT, Event.START_PRODUCT),
          seq(CONJUNCTION, Event.START_CONJUNCTION), seq(DISJUNCTION, Event.START_DISJUNCTION),
          seq(SEQUENTIAL_CONJUNCTION, Event.START_SEQUENTIAL_CONJUNCTION),
          seq(PARALLEL_CONJUNCTION, Event.START_PARALLEL_CONJUNCTION),
          seq(EXTENSIONAL_INTERSECTION, Event.START_EXTENSIONAL_INTERSECTION),
          seq(INTENSIONAL_INTERSECTION, Event.START_INTENSIONAL_INTERSECTION));
      add(IMAGE, seq(EXTENSIONAL_IMAGE, Event.START_EXTENSIONAL_IMAGE),
          seq(INTENSIONAL_IMAGE, Event.START_INTENSIONAL_IMAGE));

      add(EXTENSIONAL_DIFFERENCE, seq(CHAR_EXTENSIONAL_DIFFERENCE));
      add(INTENSIONAL_DIFFERENCE, seq(CHAR_INTENSIONAL_DIFFERENCE));
      add(EXTENSIONAL_INTERSECTION, seq(CHAR_EXTENSIONAL_INTERSECTION));
      add(INTENSIONAL_INTERSECTION, seq(CHAR_INTENSIONAL_INTERSECTION));
      add(PRODUCT, seq(CHAR_PRODUCT));
      add(CONJUNCTION, seq(CHAR_CONJUNCTION_0, CHAR_CONJUNCTION_1));
      add(DISJUNCTION, seq(CHAR_DISJUNCTION_0, CHAR_DISJUNCTION_1));
      add(SEQUENTIAL_CONJUNCTION,
          seq(CHAR_SEQUENTIAL_CONJUNCTION_0, CHAR_SEQUENTIAL_CONJUNCTION_1));
      add(PARALLEL_CONJUNCTION, seq(CHAR_PARALLEL_CONJUNCTION_0, CHAR_PARALLEL_CONJUNCTION_1));
      add(EXTENSIONAL_IMAGE, seq(CHAR_EXTENSIONAL_IMAGE));
      add(INTENSIONAL_IMAGE, seq(CHAR_INTENSIONAL_IMAGE));


      add(TENSE, seq(PAST_TENSE, Event.VALUE_PAST_TENSE),
          seq(PRESENT_TENSE, Event.VALUE_PRESENT_TENSE),
          seq(FUTURE_TENSE, Event.VALUE_FUTURE_TENSE));
      add(PAST_TENSE, seq(CHAR_PAST_TENSE_0, CHAR_PAST_TENSE_1, CHAR_PAST_TENSE_2));
      add(PRESENT_TENSE, seq(CHAR_PRESENT_TENSE_0, CHAR_PRESENT_TENSE_1, CHAR_PRESENT_TENSE_2));
      add(FUTURE_TENSE, seq(CHAR_FUTURE_TENSE_0, CHAR_FUTURE_TENSE_1, CHAR_FUTURE_TENSE_2));


      add(TRUTH, seq(CHAR_START_END_TRUTH, SKIP, Event.VALUE_FREQUENCY, NUMBER, SKIP,
          CHAR_TRUTH_SEPARATOR, SKIP, Event.VALUE_CONFIDENCE, NUMBER, SKIP, CHAR_START_END_TRUTH));


      add(WORD, seq(LETTER, WORD_TOKENS, SpecialEvent.WORD),
          seq(CHAR_UNDERSCORE, WORD_TOKENS, SpecialEvent.WORD), seq(LETTER, SpecialEvent.WORD));
      add(WORD_TOKENS, seq(LETTER, WORD_TOKENS), seq(CHAR_UNDERSCORE, WORD_TOKENS),
          seq(DIGIT, WORD_TOKENS), seq(LETTER), seq(CHAR_UNDERSCORE), seq(DIGIT));
      add(NUMBER, seq(DIGITS, DECIMAL, SpecialEvent.NUMBER), seq(DIGITS, SpecialEvent.NUMBER));
      add(DECIMAL, seq(CHAR_DOT, DIGITS));
      add(DIGITS, seq(DIGIT, DIGITS), seq(DIGIT));
      add(SKIP, seq(COMMENT, SKIP), seq(WHITESPACE, SKIP), seq(EMPTY));
      add(COMMENT, seq(CHAR_COMMENT_LINE_0, CHAR_COMMENT_LINE_1, ANYTHINGS, CHAR_EOL),
          seq(CHAR_COMMENT_LINE_0, CHAR_COMMENT_LINE_1, ANYTHINGS, CHAR_EOF));
      add(ANYTHINGS, seq(ANYTHING, ANYTHINGS), seq(EMPTY));
      add(EMPTY_WORD, seq(SpecialEvent.WORD));
    }

    @SafeVarargs
    final private void add(int name, Function<Derivation, ParseResult>... specs)
    {
      this.specs.put(name, d -> {
        ParseResult result = specs[0].apply(d);
        for (int i = 1; i < specs.length && !result.isSuccess(); i++)
        {
          result = specs[i].apply(d);
        }
        return result;
      });
    }

    final private Function<Derivation, ParseResult> seq(Object... symbols)
    {
      @SuppressWarnings("unchecked")
      final Function<Derivation, ParseResult>[] functions =
          (Function<Derivation, ParseResult>[]) new Function<?, ?>[symbols.length];

      for (int i = 0; i < functions.length; i++)
      {
        if (symbols[i] instanceof SpecialEvent)
        {
          final SpecialEvent e = (SpecialEvent) symbols[i];
          functions[i] = d -> ParseResult.specialEventResult(d, e);
        } else if (symbols[i] instanceof Event)
        {
          final Event e = (Event) symbols[i];
          functions[i] = d -> ParseResult.eventResult(d, e);
        } else if (symbols[i] instanceof Character)
        {
          final int s = (char) symbols[i];
          functions[i] = d -> d.currentCharEquals(s);
        } else
        {
          final int s = (int) symbols[i];
          if (s < SYMBOL_MASK)
          {
            functions[i] = d -> d.currentCharEquals(s);
          } else
          {
            if (s == EMPTY)
            {
              functions[i] = d -> ParseResult.emptyResult(d);
            } else if (s == ANYTHING)
            {
              functions[i] = d -> d.currentCharSatisfies(c -> c != CHAR_EOL && c != CHAR_EOF);
            } else if (s == WHITESPACE)
            {
              functions[i] = d -> d.currentCharSatisfies(Character::isWhitespace);
            } else if (s == LETTER)
            {
              functions[i] = d -> d.currentCharSatisfies(Character::isLetter);
            } else if (s == DIGIT)
            {
              functions[i] = d -> d.currentCharSatisfies(Character::isDigit);
            } else
            {
              functions[i] = d -> d.parse(s);
            }
          }
        }
      }

      return d -> {
        ParseResult last;
        final ParseResult[] subResults = new ParseResult[functions.length];

        subResults[0] = last = functions[0].apply(d);
        for (int i = 1; i < functions.length && last.isSuccess(); i++)
        {
          subResults[i] = last = functions[i].apply(last.getRemainder());
        }
        if (last.isSuccess())
        {
          return ParseResult.sequenceResult(last.getRemainder(), subResults);
        } else
        {
          return last;
        }
      };
    }
  }
}
