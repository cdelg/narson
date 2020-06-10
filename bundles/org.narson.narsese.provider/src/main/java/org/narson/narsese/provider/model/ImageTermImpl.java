package org.narson.narsese.provider.model;

import java.util.List;
import org.narson.api.narsese.ImageConnector;
import org.narson.api.narsese.ImageTerm;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;

final class ImageTermImpl extends AbstractTerm implements ImageTerm
{
  private final ImageConnector connector;
  private final List<Term> unmodifiableTerms;
  private final int placeHolderPosition;

  public ImageTermImpl(Narsese narsese, ImageConnector connector, List<Term> unmodifiableTerms,
      int placeHolderPosition)
  {
    super(narsese, ValueType.IMAGE_TERM);
    this.connector = connector;
    this.unmodifiableTerms = unmodifiableTerms;
    this.placeHolderPosition = placeHolderPosition;
  }

  @Override
  final public ImageConnector getConnector()
  {
    return connector;
  }

  @Override
  final public List<Term> getTerms()
  {
    return unmodifiableTerms;
  }

  @Override
  final public int getPlaceHolderPosition()
  {
    return placeHolderPosition;
  }

  @Override
  final protected int computeSyntacticComplexity()
  {
    return getTerms().stream().mapToInt(Term::getSyntacticComplexity).sum() + 1;
  }

  @Override
  final public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + connector.hashCode();
    result = prime * result + placeHolderPosition;
    result = prime * result + unmodifiableTerms.hashCode();
    return result;
  }

  @Override
  final public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (!super.equals(obj))
    {
      return false;
    }
    if (!(obj instanceof ImageTerm))
    {
      return false;
    }
    final ImageTerm other = (ImageTerm) obj;

    if (connector != other.getConnector())
    {
      return false;
    }

    if (placeHolderPosition != other.getPlaceHolderPosition())
    {
      return false;
    }

    if (unmodifiableTerms.equals(other.getTerms()))
    {
      return true;
    } else
    {
      return false;
    }
  }
}


