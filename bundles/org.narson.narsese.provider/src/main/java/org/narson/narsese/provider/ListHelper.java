package org.narson.narsese.provider;

import java.util.List;
import org.narson.api.narsese.Term;

public class ListHelper
{
  static public <T> int compareTerms(List<Term> l1, List<Term> l2)
  {
    final int compareSize = l1.size() - l2.size();
    if (compareSize < 0)
    {
      return -1;
    }
    if (compareSize > 0)
    {
      return 1;
    } else
    {
      int compareTerm = 0;
      for (int i = 0; i < l1.size() && compareTerm == 0; i++)
      {
        compareTerm = l1.get(i).compareTo(l2.get(i));
      }
      return compareTerm;
    }
  }

  static public <T> int compareStrings(List<String> l1, List<String> l2)
  {
    final int compareSize = l1.size() - l2.size();
    if (compareSize < 0)
    {
      return -1;
    }
    if (compareSize > 0)
    {
      return 1;
    } else
    {
      int compareTerm = 0;
      for (int i = 0; i < l1.size() && compareTerm == 0; i++)
      {
        compareTerm = l1.get(i).compareTo(l2.get(i));
      }
      return compareTerm;
    }
  }
}
