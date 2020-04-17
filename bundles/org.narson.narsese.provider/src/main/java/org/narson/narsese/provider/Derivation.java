package org.narson.narsese.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import org.narson.narsese.provider.NarseseParserImpl.GrammarSpec;

final class Derivation
{
  private final Supplier<Integer> lexer;
  private Derivation next;
  private int currentChar;
  private final long streamOffset;
  private final Map<Integer, ParseResult> results = new HashMap<>();

  public Derivation(long currentOffset, Supplier<Integer> lexer, int currentChar)
  {
    streamOffset = currentOffset;
    this.currentChar = currentChar;
    this.lexer = lexer;
    next = new Derivation(currentOffset, lexer);
  }

  public Derivation(long currentOffset, Supplier<Integer> lexer)
  {
    streamOffset = currentOffset + 1;
    this.lexer = lexer;
  }

  public ParseResult currentCharEquals(int c)
  {
    if (next == null)
    {
      currentChar = lexer.get();
      next = new Derivation(streamOffset, lexer);
    }

    return ParseResult.charResult(next, currentChar == c, currentChar);
  }

  public ParseResult currentCharSatisfies(Function<Integer, Boolean> c)
  {
    if (next == null)
    {
      currentChar = lexer.get();
      next = new Derivation(streamOffset, lexer);
    }

    return ParseResult.charResult(next, c.apply(currentChar), currentChar);
  }

  public ParseResult parseSentence()
  {
    return parse(NarseseSymbols.SENTENCE);
  }

  public long getStreamOffset()
  {
    return streamOffset;
  }

  public ParseResult parse(int symbol)
  {
    ParseResult r = results.get(symbol);
    if (r == null)
    {
      r = GrammarSpec.instance.get(symbol).apply(this);
      results.put(symbol, r);
    }
    return r;
  }
}
