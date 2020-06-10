package org.narson.narsese.provider.model;

import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Query;
import org.narson.api.narsese.Term;

final class QueryImpl extends AbstractSentence implements Query
{
  public QueryImpl(Narsese narsese, Term statement)
  {
    super(narsese, ValueType.QUERY, statement);
  }
}
