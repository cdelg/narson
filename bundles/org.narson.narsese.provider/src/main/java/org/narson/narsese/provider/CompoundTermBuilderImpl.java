package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import static org.narson.tools.PredChecker.checkState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.CompoundTermBuilder;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Term;

final class CompoundTermBuilderImpl implements CompoundTermBuilder
{
  private final int bufferSize;
  private final int prefixThreshold;
  private final Connector connector;
  private final List<Term> terms = new ArrayList<>();
  private int placeHolderPosition = -1;

  public CompoundTermBuilderImpl(int bufferSize, int prefixThreshold, Connector connector)
  {
    this.bufferSize = bufferSize;
    this.prefixThreshold = prefixThreshold;
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
  public CompoundTermBuilder of(List<Term> terms)
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
    if (connector == Connector.INTENSIONAL_IMAGE || connector == Connector.EXTENSIONAL_IMAGE)
    {
      checkState(placeHolderPosition != -1, "placeholder not set");
      checkState(placeHolderPosition <= terms.size() + 1,
          () -> "placeholder position > terms.size() + 1 =" + terms.size() + 1);
      checkTerms();

      return new CompoundTermImpl(bufferSize, prefixThreshold, connector,
          Collections.unmodifiableList(new ArrayList<>(terms)), placeHolderPosition);
    } else
    {
      checkTerms();
      return new CompoundTermImpl(bufferSize, prefixThreshold, connector,
          Collections.unmodifiableList(new ArrayList<>(terms)));
    }
  }

  private void checkTerms()
  {
    switch (connector)
    {
      case CONJUNCTION:
        checkTermsSup(2);
        break;
      case DISJUNCTION:
        checkTermsSup(2);
        break;
      case EXTENSIONAL_DIFFERENCE:
        checkTermsEqual(2);
        break;
      case EXTENSIONAL_IMAGE:
        checkTermsSup(2);
        break;
      case EXTENSIONAL_INTERSECTION:
        checkTermsSup(2);
        break;
      case EXTENSIONAL_SET:
        checkTermsSup(1);
        break;
      case INTENSIONAL_DIFFERENCE:
        checkTermsEqual(2);
        break;
      case INTENSIONAL_IMAGE:
        checkTermsSup(2);
        break;
      case INTENSIONAL_INTERSECTION:
        checkTermsSup(2);
        break;
      case INTENSIONAL_SET:
        checkTermsSup(1);
        break;
      case NEGATION:
        checkTermsEqual(1);
        break;
      case PARALLEL_CONJUNCTION:
        checkTermsSup(2);
        break;
      case PRODUCT:
        checkTermsSup(2);
        break;
      case SEQUENTIAL_CONJUNCTION:
        checkTermsSup(2);
        break;
      default:
        checkState(false, "Unknown connector");
        break;
    }
  }

  private void checkTermsEqual(int c)
  {
    checkState(c == terms.size(), () -> "terms.size() != " + c);
  }

  private void checkTermsSup(int c)
  {
    checkState(c <= terms.size(), () -> "terms.size() < " + c);
  }
}
