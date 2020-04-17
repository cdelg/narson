package org.narson.narsese.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Term;

final class CompoundTermImpl extends AbstractTerm implements CompoundTerm
{
  private final Connector connector;
  private final List<Term> unmodifiableTerms;
  private final int placeHolderPosition;

  public CompoundTermImpl(int bufferSize, int prefixThreshold, Connector connector,
      List<Term> unmodifiableTerms, int placeHolderPosition)
  {
    super(ValueType.COMPOUND_TERM, bufferSize, placeHolderPosition);
    this.connector = connector;
    this.unmodifiableTerms = unmodifiableTerms;
    this.placeHolderPosition = placeHolderPosition;
  }

  public CompoundTermImpl(int bufferSize, int prefixThreshold, Connector connector,
      List<Term> unmodifiableTerms)
  {
    this(bufferSize, prefixThreshold, connector, unmodifiableTerms, -1);
  }

  @Override
  public Connector getConnector()
  {
    return connector;
  }

  @Override
  public List<Term> getTerms()
  {
    return unmodifiableTerms;
  }

  @Override
  public int getPlaceHolderPosition()
  {
    return placeHolderPosition;
  }


  private boolean orderMatters()
  {
    return connector == Connector.EXTENSIONAL_DIFFERENCE
        || connector == Connector.INTENSIONAL_DIFFERENCE || connector == Connector.EXTENSIONAL_IMAGE
        || connector == Connector.INTENSIONAL_IMAGE
        || connector == Connector.SEQUENTIAL_CONJUNCTION;
  }

  private boolean termsUnorderedEquals(List<Term> terms)
  {
    if (unmodifiableTerms.size() != terms.size())
    {
      return false;
    }
    final Map<Term, Integer> freq = new HashMap<>();
    for (final Term o : unmodifiableTerms)
    {
      freq.merge(o, 1, Integer::sum);
    }
    for (final Term o : terms)
    {
      if (freq.merge(o, -1, Integer::sum) < 0)
      {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + connector.hashCode();
    result = prime * result + placeHolderPosition;
    result = prime * result + unmodifiableTerms.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (!super.equals(obj))
    {
      return false;
    }
    if (!(obj instanceof CompoundTerm))
    {
      return false;
    }
    final CompoundTerm other = (CompoundTerm) obj;

    if (connector != other.getConnector())
    {
      return false;
    }

    if (placeHolderPosition != other.getPlaceHolderPosition())
    {
      return false;
    }

    if (orderMatters())
    {
      if (unmodifiableTerms.equals(other.getTerms()))
      {
        return true;
      } else
      {
        return false;
      }
    } else
    {
      if (termsUnorderedEquals(other.getTerms()))
      {
        return true;
      } else
      {
        return false;
      }
    }
  }
}


