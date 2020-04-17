package org.narson.narsese.provider;

interface NarseseOperations
{
  int OP_UNSET = -1;
  int OP_OPERATION = 0;
  int OP_EXTENSIONAL_INTERSECTION = 1;
  int OP_INTENSIONAL_INTERSECTION = 2;
  int OP_EXTENSIONAL_DIFFERENCE = 3;
  int OP_INTENSIONAL_DIFFERENCE = 4;
  int OP_PRODUCT = 5;
  int OP_EXTENSIONAL_IMAGE = 6;
  int OP_INTENSIONAL_IMAGE = 7;
  int OP_NEGATION = 8;
  int OP_CONJUNCTION = 9;
  int OP_DISJUNCTION = 10;
  int OP_SEQUENTIAL_CONJUNCTION = 11;
  int OP_PARALLEL_CONJUNCTION = 12;

  int[] OP_NB_TERMS = new int[] {0, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2};

  boolean[] OP_NB_TERMS_STRICTS = new boolean[] {false, false, false, true, true, false, false,
      false, true, false, false, false, false};

  boolean[] OP_SUPPORT_INFIX = new boolean[] {false, true, true, true, true, true, false, false,
      false, true, true, true, true};

  int OP_PLACEHOLDER_YES = 1;
  int OP_PLACEHOLDER_NO = 0;
  int OP_PLACEHOLDER_ALREADY_SET = 2;

  int[] OP_PLACEHOLDER = new int[] {OP_PLACEHOLDER_NO, OP_PLACEHOLDER_NO, OP_PLACEHOLDER_NO,
      OP_PLACEHOLDER_NO, OP_PLACEHOLDER_NO, OP_PLACEHOLDER_NO, OP_PLACEHOLDER_YES,
      OP_PLACEHOLDER_YES, OP_PLACEHOLDER_NO, OP_PLACEHOLDER_NO, OP_PLACEHOLDER_NO,
      OP_PLACEHOLDER_NO, OP_PLACEHOLDER_NO};
}
