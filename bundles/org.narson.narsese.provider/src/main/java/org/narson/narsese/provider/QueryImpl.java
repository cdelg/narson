package org.narson.narsese.provider;

import org.narson.api.narsese.Query;
import org.narson.api.narsese.Term;

final class QueryImpl extends AbstractSentence implements Query
{
  public QueryImpl(int bufferSize, int prefixThreshold, Term statement)
  {
    super(ValueType.QUERY, bufferSize, prefixThreshold, statement);
  }
}
