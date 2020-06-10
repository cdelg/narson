package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.narson.api.narsese.ImageConnector;
import org.narson.api.narsese.ImageTerm;
import org.narson.api.narsese.ImageTermBuilder;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;

final class ImageTermBuilderImpl extends AbstractTermBuilder implements ImageTermBuilder
{
  private final ImageConnector connector;
  private int placeHolderPosition = -1;

  public ImageTermBuilderImpl(Narsese narsese, ImageConnector connector)
  {
    super(narsese);
    this.connector = connector;
  }

  @Override
  public ImageTermBuilder of(Term term)
  {
    add(term);
    return this;
  }

  @Override
  public ImageTermBuilder of(Term... terms)
  {
    add(terms);
    return this;
  }

  @Override
  public ImageTermBuilder of(Collection<Term> terms)
  {
    add(terms);
    return this;
  }

  @Override
  public ImageTermBuilder setPlaceHolderPosition()
  {
    placeHolderPosition = terms.size() + 1;
    checkState(2 <= placeHolderPosition, "placeholder position < 2");
    return this;
  }

  @Override
  public ImageTermBuilder setPlaceHolderPosition(int position)
  {
    checkArgument(2 <= position, "placeholder position < 2");
    placeHolderPosition = position;
    return this;
  }

  @Override
  public ImageTerm build()
  {
    checkState(placeHolderPosition != -1, "placeholder not set");
    checkState(placeHolderPosition <= terms.size() + 1,
        () -> "placeholder position > terms.size() + 1 =" + terms.size() + 1);
    checkState(2 <= terms.size(), () -> "terms.size() < 2");

    final List<Term> effectiveTerms = new ArrayList<>(terms);

    return new ImageTermImpl(narsese, connector, Collections.unmodifiableList(effectiveTerms),
        placeHolderPosition);
  }
}
