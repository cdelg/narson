package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkNotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.narson.api.narsese.Operation;
import org.narson.api.narsese.OperationBuilder;
import org.narson.api.narsese.Term;

final class OperationBuilderImpl implements OperationBuilder
{
  private final int bufferSize;
  private final int prefixThreshold;
  private final String name;
  private final List<Term> terms = new ArrayList<>();

  public OperationBuilderImpl(int bufferSize, int prefixThreshold, String name)
  {
    this.bufferSize = bufferSize;
    this.prefixThreshold = prefixThreshold;
    this.name = name;
  }

  @Override
  public OperationBuilder on(Term term)
  {
    terms.add(checkNotNull(term, "term"));
    return this;
  }

  @Override
  public OperationBuilder on(Term... terms)
  {
    checkNotNull(terms, "terms");

    for (int i = 0; i < terms.length; i++)
    {
      checkNotNull(terms[i], "One of the provided terms is null.");

      this.terms.add(terms[i]);
    }
    return this;
  }

  @Override
  public OperationBuilder on(List<Term> terms)
  {
    checkNotNull(terms, "terms");

    for (final Term term : terms)
    {
      checkNotNull(term, "One of the provided terms is null.");

      this.terms.add(term);
    }
    return this;
  }

  @Override
  public Operation build()
  {
    return new OperationImpl(bufferSize, prefixThreshold, name,
        Collections.unmodifiableList(new ArrayList<>(terms)));
  }
}
