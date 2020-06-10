package org.narson.narsese.provider.model;

import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NegationTerm;
import org.narson.api.narsese.Term;

final class NegationTermImpl extends AbstractTerm implements NegationTerm
{
  private final Term term;

  public NegationTermImpl(Narsese narsese, Term term)
  {
    super(narsese, ValueType.NEGATION_TERM);
    this.term = term;
  }

  @Override
  public Term getTerm()
  {
    return term;
  }

  @Override
  protected int computeSyntacticComplexity()
  {
    return term.getSyntacticComplexity() + 1;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + term.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof NegationTerm))
    {
      return false;
    }

    final NegationTerm other = (NegationTerm) obj;

    if (!term.equals(other.getTerm()))
    {
      return false;
    }

    return true;
  }
}
