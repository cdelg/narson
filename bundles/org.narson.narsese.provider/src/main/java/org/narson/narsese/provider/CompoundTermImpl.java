package org.narson.narsese.provider;

import java.util.List;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;

final class CompoundTermImpl extends AbstractTerm implements CompoundTerm
{
  private final Connector connector;
  private final List<Term> unmodifiableTerms;
  private final int placeHolderPosition;

  public CompoundTermImpl(Narsese narsese, Connector connector, List<Term> unmodifiableTerms,
      int placeHolderPosition)
  {
    super(narsese, ValueType.COMPOUND_TERM);
    this.connector = connector;
    this.unmodifiableTerms = unmodifiableTerms;
    this.placeHolderPosition = placeHolderPosition;
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

  @Override
  protected int computeSyntacticComplexity()
  {
    return getTerms().stream().mapToInt(Term::getSyntacticComplexity).sum() + 1;
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

    if (unmodifiableTerms.equals(other.getTerms()))
    {
      return true;
    } else
    {
      return false;
    }
  }

  @Override
  public int compareTo(Term o)
  {
    final int compareType = getValueType().compareTo(o.getValueType());
    if (compareType < 0)
    {
      return -1;
    }
    if (compareType > 0)
    {
      return 1;
    } else
    {
      return compare(o.asCompoundTerm());
    }
  }

  private int compare(CompoundTerm o)
  {
    final int compareConnector = getConnector().compareTo(o.getConnector());
    if (compareConnector < 0)
    {
      return -1;
    }
    if (compareConnector > 0)
    {
      return 1;
    } else
    {
      final int comparePlaceholder = getPlaceHolderPosition() - o.getPlaceHolderPosition();
      if (comparePlaceholder < 0)
      {
        return -1;
      }
      if (comparePlaceholder > 0)
      {
        return 1;
      } else
      {
        return ListHelper.compareTerms(getTerms(), o.getTerms());
      }
    }
  }
}


