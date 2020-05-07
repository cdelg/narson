package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import static org.narson.tools.PredChecker.checkState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.CompoundTermBuilder;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;

final class CompoundTermBuilderImpl implements CompoundTermBuilder
{
  private final Narsese narsese;
  private final Connector connector;
  private final List<Term> terms = new ArrayList<>();
  private int placeHolderPosition = -1;

  public CompoundTermBuilderImpl(Narsese narsese, Connector connector)
  {
    this.narsese = narsese;
    this.connector = connector;
  }

  @Override
  public CompoundTermBuilder of(Term term)
  {
    terms.add(checkNotNull(term, "term"));
    return this;
  }

  @Override
  public CompoundTermBuilder of(Term... terms)
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
  public CompoundTermBuilder of(Collection<Term> terms)
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
  public CompoundTermBuilder setPlaceHolderPosition()
  {
    placeHolderPosition = terms.size() + 1;
    checkState(
        (connector != Connector.INTENSIONAL_IMAGE && connector != Connector.EXTENSIONAL_IMAGE)
            || 2 <= placeHolderPosition,
        "placeholder position < 2");
    return this;
  }

  @Override
  public CompoundTermBuilder setPlaceHolderPosition(int position)
  {
    checkArgument(
        (connector != Connector.INTENSIONAL_IMAGE && connector != Connector.EXTENSIONAL_IMAGE)
            || 2 <= position,
        "placeholder position < 2");
    placeHolderPosition = position;
    return this;
  }

  @Override
  public CompoundTerm build()
  {
    int placeHolder;

    if (connector == Connector.INTENSIONAL_IMAGE || connector == Connector.EXTENSIONAL_IMAGE)
    {
      checkState(placeHolderPosition != -1, "placeholder not set");
      checkState(placeHolderPosition <= terms.size() + 1,
          () -> "placeholder position > terms.size() + 1 =" + terms.size() + 1);

      placeHolder = placeHolderPosition;
    } else
    {
      placeHolder = -1;
    }

    checkState(connector.minCardinality() <= terms.size(),
        () -> "terms.size() < " + connector.minCardinality());
    checkState(terms.size() <= connector.maxCardinality(),
        () -> "terms.size() > " + connector.maxCardinality());

    final ArrayList<Term> effectiveTerms =
        connector.hasDistinctComponents() ? new ArrayList<>(new HashSet<>(terms))
            : new ArrayList<>(terms);

    if (!connector.isOrdered())
    {
      Collections.sort(effectiveTerms);
    }

    return new CompoundTermImpl(narsese, connector, Collections.unmodifiableList(effectiveTerms),
        placeHolder);
  }
}
