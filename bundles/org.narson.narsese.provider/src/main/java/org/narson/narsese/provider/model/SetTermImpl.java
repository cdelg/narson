package org.narson.narsese.provider.model;

import java.util.Set;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.SetConnector;
import org.narson.api.narsese.SetTerm;
import org.narson.api.narsese.Term;

final class SetTermImpl extends AbstractTerm implements SetTerm
{
  private final SetConnector connector;
  private final Set<Term> unmodifiableTerms;

  public SetTermImpl(Narsese narsese, SetConnector connector, Set<Term> unmodifiableTerms)
  {
    super(narsese, ValueType.SET_TERM);
    this.connector = connector;
    this.unmodifiableTerms = unmodifiableTerms;
  }

  @Override
  final public SetConnector getConnector()
  {
    return connector;
  }

  @Override
  final public Set<Term> getTerms()
  {
    return unmodifiableTerms;
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
    if (!(obj instanceof SetTerm))
    {
      return false;
    }
    final SetTerm other = (SetTerm) obj;

    if (connector != other.getConnector())
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


