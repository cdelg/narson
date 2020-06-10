package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkNotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TermBuilder;

abstract class AbstractTermBuilder implements TermBuilder
{
  protected final Narsese narsese;
  protected final List<Term> terms = new ArrayList<>();

  public AbstractTermBuilder(Narsese narsese)
  {
    this.narsese = narsese;
  }

  final protected void add(Term term)
  {
    terms.add(checkNotNull(term, "term"));
  }

  final protected void add(Term... terms)
  {
    checkNotNull(terms, "terms");

    for (int i = 0; i < terms.length; i++)
    {
      checkNotNull(terms[i], "One of the provided terms is null.");

      this.terms.add(terms[i]);
    }
  }

  final protected void add(Collection<Term> terms)
  {
    checkNotNull(terms, "terms");

    for (final Term term : terms)
    {
      checkNotNull(term, "One of the provided terms is null.");

      this.terms.add(term);
    }
  }
}
