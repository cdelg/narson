package org.narson.narsese.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;

final class DependentVariableImpl extends AbstractTerm implements DependentVariable
{
  private final String name;
  private final List<String> unmodifiableIndependentVariableNames;
  private final boolean anonymous;

  public DependentVariableImpl(Narsese narsese, String name,
      List<String> unmodifiableIndependentVariableNames)
  {
    super(narsese, ValueType.DEPENDENT_VARIABLE);
    this.name = name;
    anonymous = this.name.isEmpty();
    this.unmodifiableIndependentVariableNames =
        anonymous ? Collections.unmodifiableList(new ArrayList<>())
            : unmodifiableIndependentVariableNames;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public List<String> getIndependentVariableNames()
  {
    return unmodifiableIndependentVariableNames;
  }

  @Override
  public boolean isAnonymous()
  {
    return anonymous;
  }

  @Override
  protected int computeSyntacticComplexity()
  {
    return 1;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + name.hashCode();
    result = prime * result + unmodifiableIndependentVariableNames.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (!(obj instanceof DependentVariable))
    {
      return false;
    }

    final DependentVariable other = (DependentVariable) obj;

    if (!name.equals(other.getName()))
    {
      return false;
    }

    if (!unmodifiableIndependentVariableNames.equals(other.getIndependentVariableNames()))
    {
      return false;
    }

    return true;
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
      return compare(o.asDependentVariable());
    }
  }

  private int compare(DependentVariable o)
  {
    final int compareName = getName().compareTo(o.getName());
    if (compareName < 0)
    {
      return -1;
    }
    if (compareName > 0)
    {
      return 1;
    } else
    {
      return ListHelper.compareStrings(getIndependentVariableNames(),
          o.getIndependentVariableNames());
    }
  }
}
