package org.narson.narsese.provider.util;

import java.util.regex.Pattern;

final public class WordHelper
{
  final public static Pattern WORD_PATTERN = Pattern.compile("^[\\p{L}0-9_]*$");

  static public boolean isWordValid(String word)
  {
    if (word.isEmpty())
    {
      return false;
    }

    if (!WORD_PATTERN.matcher(word).matches())
    {
      return false;
    }

    if (Character.isDigit(word.charAt(0)))
    {
      return false;
    }

    if (word.length() == 1 && word.charAt(0) == '_')
    {
      return false;
    }

    return true;
  }
}
