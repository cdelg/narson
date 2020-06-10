package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkState;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.SetConnector;
import org.narson.api.narsese.SetTerm;
import org.narson.api.narsese.SetTermBuilder;
import org.narson.api.narsese.Term;

final class SetTermBuilderImpl extends AbstractTermBuilder implements SetTermBuilder
{
  private final SetConnector connector;

  public SetTermBuilderImpl(Narsese narsese, SetConnector connector)
  {
    super(narsese);
    this.connector = connector;
  }

  @Override
  public SetTermBuilder of(Term term)
  {
    add(term);
    return this;
  }

  @Override
  public SetTermBuilder of(Term... terms)
  {
    add(terms);
    return this;
  }

  @Override
  public SetTermBuilder of(Collection<Term> terms)
  {
    add(terms);
    return this;
  }

  @Override
  public SetTerm build()
  {
    checkState(1 <= terms.size(), () -> "terms.size() < 1");

    final Set<Term> effectiveTerms = new HashSet<>(terms);

    return new SetTermImpl(narsese, connector, Collections.unmodifiableSet(effectiveTerms));
  }
}
