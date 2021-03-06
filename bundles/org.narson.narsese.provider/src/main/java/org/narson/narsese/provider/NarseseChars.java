package org.narson.narsese.provider;

import java.util.regex.Pattern;

interface NarseseChars
{
  Pattern WORD_PATTERN = Pattern.compile("^[\\p{L}0-9_]*$");

  static boolean isWordValid(String word)
  {
    if (word.isEmpty())
    {
      return true;
    }

    if (!WORD_PATTERN.matcher(word).matches())
    {
      return false;
    }

    if (Character.isDigit(word.charAt(0)))
    {
      return false;
    }

    if (word.length() == 1 && word.charAt(0) == CHAR_UNDERSCORE)
    {
      return false;
    }

    return true;
  }

  int CHAR_EOF = -1;
  char CHAR_EOL = '\n';
  char CHAR_SPACE = ' ';
  char CHAR_CR = '\r';
  char CHAR_UNDERSCORE = '_';
  char CHAR_DOT = '.';

  char CHAR_START_BLOCK = '(';
  char CHAR_END_BLOCK = ')';
  char CHAR_START_EXTENSIONAL_SET = '{';
  char CHAR_END_EXTENSIONAL_SET = '}';
  char CHAR_START_INTENSIONAL_SET = '[';
  char CHAR_END_INTENSIONAL_SET = ']';
  char CHAR_OPERATION = '^';
  char CHAR_START_END_TRUTH = '%';
  char CHAR_TRUTH_SEPARATOR = ';';
  char CHAR_TERM_SEPARATOR = CHAR_SPACE;
  char CHAR_END_JUDGMENT = '.';
  char CHAR_END_GOAL = '!';
  char CHAR_END_QUESTION = '?';
  char CHAR_END_QUERY = '@';
  char CHAR_PLACEHOLDER = '_';
  char CHAR_QUERY_VARIABLE_MARK = '?';
  char CHAR_VARIABLE_MARK = '#';

  char CHAR_COMMENT_LINE_0 = '/';
  char CHAR_COMMENT_LINE_1 = '/';
  char[] CHAR_COMMENT_LINE = new char[] {CHAR_COMMENT_LINE_0, CHAR_COMMENT_LINE_1};

  char CHAR_EXTENSIONAL_INTERSECTION = '&';
  char CHAR_INTENSIONAL_INTERSECTION = '|';
  char CHAR_EXTENSIONAL_DIFFERENCE = '-';
  char CHAR_INTENSIONAL_DIFFERENCE = '~';
  char CHAR_PRODUCT = '*';
  char CHAR_EXTENSIONAL_IMAGE = '/';
  char CHAR_INTENSIONAL_IMAGE = '\\';

  char CHAR_NEGATION = '!';

  char CHAR_CONJUNCTION_0 = '&';
  char CHAR_CONJUNCTION_1 = '&';
  char[] CHAR_CONJUNCTION = new char[] {CHAR_CONJUNCTION_0, CHAR_CONJUNCTION_1};

  char CHAR_DISJUNCTION_0 = '|';
  char CHAR_DISJUNCTION_1 = '|';
  char[] CHAR_DISJUNCTION = new char[] {CHAR_DISJUNCTION_0, CHAR_DISJUNCTION_1};

  char CHAR_SEQUENTIAL_CONJUNCTION_0 = '&';
  char CHAR_SEQUENTIAL_CONJUNCTION_1 = '|';
  char[] CHAR_SEQUENTIAL_CONJUNCTION =
      new char[] {CHAR_SEQUENTIAL_CONJUNCTION_0, CHAR_SEQUENTIAL_CONJUNCTION_1};

  char CHAR_PARALLEL_CONJUNCTION_0 = '&';
  char CHAR_PARALLEL_CONJUNCTION_1 = '|';
  char[] CHAR_PARALLEL_CONJUNCTION =
      new char[] {CHAR_PARALLEL_CONJUNCTION_0, CHAR_PARALLEL_CONJUNCTION_1};

  char CHAR_INHERITANCE_0 = '-';
  char CHAR_INHERITANCE_1 = '-';
  char CHAR_INHERITANCE_2 = '>';
  char[] CHAR_INHERITANCE = new char[] {CHAR_INHERITANCE_0, CHAR_INHERITANCE_1, CHAR_INHERITANCE_2};

  char CHAR_SIMILARITY_0 = '<';
  char CHAR_SIMILARITY_1 = '-';
  char CHAR_SIMILARITY_2 = '>';
  char[] CHAR_SIMILARITY = new char[] {CHAR_SIMILARITY_0, CHAR_SIMILARITY_1, CHAR_SIMILARITY_2};

  char CHAR_IMPLICATION_0 = '=';
  char CHAR_IMPLICATION_1 = '=';
  char CHAR_IMPLICATION_2 = '>';
  char[] CHAR_IMPLICATION = new char[] {CHAR_IMPLICATION_0, CHAR_IMPLICATION_1, CHAR_IMPLICATION_2};

  char CHAR_EQUIVALENCE_0 = '<';
  char CHAR_EQUIVALENCE_1 = '=';
  char CHAR_EQUIVALENCE_2 = '>';
  char[] CHAR_EQUIVALENCE = new char[] {CHAR_EQUIVALENCE_0, CHAR_EQUIVALENCE_1, CHAR_EQUIVALENCE_2};

  char CHAR_INSTANCE_0 = '{';
  char CHAR_INSTANCE_1 = '-';
  char CHAR_INSTANCE_2 = '-';
  char[] CHAR_INSTANCE = new char[] {CHAR_INSTANCE_0, CHAR_INSTANCE_1, CHAR_INSTANCE_2};

  char CHAR_PROPERTY_0 = '-';
  char CHAR_PROPERTY_1 = '-';
  char CHAR_PROPERTY_2 = ']';
  char[] CHAR_PROPERTY = new char[] {CHAR_PROPERTY_0, CHAR_PROPERTY_1, CHAR_PROPERTY_2};

  char CHAR_INSTANCE_PROPERTY_0 = '{';
  char CHAR_INSTANCE_PROPERTY_1 = '-';
  char CHAR_INSTANCE_PROPERTY_2 = ']';
  char[] CHAR_INSTANCE_PROPERTY =
      new char[] {CHAR_INSTANCE_PROPERTY_0, CHAR_INSTANCE_PROPERTY_1, CHAR_INSTANCE_PROPERTY_2};

  char CHAR_PREDICTIVE_IMPLICATION_0 = '=';
  char CHAR_PREDICTIVE_IMPLICATION_1 = '/';
  char CHAR_PREDICTIVE_IMPLICATION_2 = '>';
  char[] CHAR_PREDICTIVE_IMPLICATION = new char[] {CHAR_PREDICTIVE_IMPLICATION_0,
      CHAR_PREDICTIVE_IMPLICATION_1, CHAR_PREDICTIVE_IMPLICATION_2};

  char CHAR_RETROSPECTIVE_IMPLICATION_0 = '=';
  char CHAR_RETROSPECTIVE_IMPLICATION_1 = '\\';
  char CHAR_RETROSPECTIVE_IMPLICATION_2 = '>';
  char[] CHAR_RETROSPECTIVE_IMPLICATION = new char[] {CHAR_RETROSPECTIVE_IMPLICATION_0,
      CHAR_RETROSPECTIVE_IMPLICATION_1, CHAR_RETROSPECTIVE_IMPLICATION_2};

  char CHAR_CONCURRENT_IMPLICATION_0 = '=';
  char CHAR_CONCURRENT_IMPLICATION_1 = '|';
  char CHAR_CONCURRENT_IMPLICATION_2 = '>';
  char[] CHAR_CONCURRENT_IMPLICATION = new char[] {CHAR_CONCURRENT_IMPLICATION_0,
      CHAR_CONCURRENT_IMPLICATION_1, CHAR_CONCURRENT_IMPLICATION_2};

  char CHAR_PREDICTIVE_EQUIVALENCE_0 = '<';
  char CHAR_PREDICTIVE_EQUIVALENCE_1 = '/';
  char CHAR_PREDICTIVE_EQUIVALENCE_2 = '>';
  char[] CHAR_PREDICTIVE_EQUIVALENCE = new char[] {CHAR_PREDICTIVE_EQUIVALENCE_0,
      CHAR_PREDICTIVE_EQUIVALENCE_1, CHAR_PREDICTIVE_EQUIVALENCE_2};

  char CHAR_CONCURRENT_EQUIVALENCE_0 = '<';
  char CHAR_CONCURRENT_EQUIVALENCE_1 = '|';
  char CHAR_CONCURRENT_EQUIVALENCE_2 = '>';
  char[] CHAR_CONCURRENT_EQUIVALENCE = new char[] {CHAR_CONCURRENT_EQUIVALENCE_0,
      CHAR_CONCURRENT_EQUIVALENCE_1, CHAR_CONCURRENT_EQUIVALENCE_2};

  char CHAR_PAST_TENSE_0 = ':';
  char CHAR_PAST_TENSE_1 = '\\';
  char CHAR_PAST_TENSE_2 = ':';
  char[] CHAR_PAST_TENSE = new char[] {CHAR_PAST_TENSE_0, CHAR_PAST_TENSE_1, CHAR_PAST_TENSE_2};

  char CHAR_PRESENT_TENSE_0 = ':';
  char CHAR_PRESENT_TENSE_1 = '|';
  char CHAR_PRESENT_TENSE_2 = ':';
  char[] CHAR_PRESENT_TENSE =
      new char[] {CHAR_PRESENT_TENSE_0, CHAR_PRESENT_TENSE_1, CHAR_PRESENT_TENSE_2};

  char CHAR_FUTURE_TENSE_0 = ':';
  char CHAR_FUTURE_TENSE_1 = '/';
  char CHAR_FUTURE_TENSE_2 = ':';
  char[] CHAR_FUTURE_TENSE =
      new char[] {CHAR_FUTURE_TENSE_0, CHAR_FUTURE_TENSE_1, CHAR_FUTURE_TENSE_2};
}
