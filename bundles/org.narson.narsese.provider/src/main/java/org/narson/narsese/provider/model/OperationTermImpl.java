package org.narson.narsese.provider.model;

import java.util.List;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.OperationTerm;
import org.narson.api.narsese.Term;

final class OperationTermImpl extends AbstractTerm implements OperationTerm
{
  private final String name;
  private final List<Term> unmodifiableTerms;

  public OperationTermImpl(Narsese narsese, String name, List<Term> unmodifiableTerms)
  {
    super(narsese, ValueType.OPERATION_TERM);
    this.name = name;
    this.unmodifiableTerms = unmodifiableTerms;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public List<Term> getTerms()
  {
    return unmodifiableTerms;
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
    result = prime * result + name.hashCode();
    result = prime * result + unmodifiableTerms.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof OperationTerm))
    {
      return false;
    }

    final OperationTerm other = (OperationTerm) obj;

    if (!name.equals(other.getName()))
    {
      return false;
    }

    if (!unmodifiableTerms.equals(other.getTerms()))
    {
      return false;
    }

    return true;
  }
}
