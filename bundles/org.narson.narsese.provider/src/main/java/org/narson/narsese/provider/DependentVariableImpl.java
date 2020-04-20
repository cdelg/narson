package org.narson.narsese.provider;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.narson.api.narsese.DependentVariable;

final class DependentVariableImpl extends AbstractTerm implements DependentVariable
{
  private final String name;
  private final Set<String> unmodifiableIndependentVariableNames;
  private final boolean anonymous;

  public DependentVariableImpl(int bufferSize, String name,
      Set<String> unmodifiableIndependentVariableNames)
  {
    super(ValueType.DEPENDENT_VARIABLE, bufferSize, 0);
    this.name = name;
    anonymous = this.name.isEmpty();
    this.unmodifiableIndependentVariableNames =
        anonymous && !unmodifiableIndependentVariableNames.isEmpty()
            ? Collections.unmodifiableSet(new HashSet<>())
            : unmodifiableIndependentVariableNames;
  }

  public DependentVariableImpl(int bufferSize)
  {
    this(bufferSize, "", Collections.unmodifiableSet(new HashSet<>()));
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public Set<String> getIndependentVariableNames()
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
}
