package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.DependentVariableBuilder;
import org.narson.api.narsese.Narsese;
import org.narson.narsese.provider.util.WordHelper;

final class DependentVariableBuilderImpl implements DependentVariableBuilder
{
  private final Narsese narsese;
  private final String name;
  private final Set<String> independentVariableNames = new HashSet<>();

  public DependentVariableBuilderImpl(Narsese narsese, String name)
  {
    this.narsese = narsese;
    this.name = name;
  }

  @Override
  public DependentVariableBuilder dependsOn(String independentVariableName)
  {
    checkNotNull(independentVariableName, "independentVariableName");
    checkArgument(WordHelper.isWordValid(independentVariableName),
        () -> "Invalid independentVariableName: " + independentVariableName);

    independentVariableNames.add(independentVariableName);
    return this;
  }

  @Override
  public DependentVariableBuilder dependsOn(String... independentVariableNames)
  {
    checkNotNull(independentVariableNames, "independentVariableNames");

    for (int i = 0; i < independentVariableNames.length; i++)
    {
      final String independentVariableName = independentVariableNames[i];
      checkNotNull(independentVariableName, "One of the provided name is null.");
      checkArgument(WordHelper.isWordValid(independentVariableName),
          () -> "Invalid independentVariableName: " + independentVariableName);

      this.independentVariableNames.add(independentVariableName);
    }

    return this;
  }

  @Override
  public DependentVariableBuilder dependsOn(Set<String> independentVariableNames)
  {
    checkNotNull(independentVariableNames, "independentVariableNames");

    for (final String independentVariableName : independentVariableNames)
    {
      checkNotNull(independentVariableName, "One of the provided name is null.");
      checkArgument(WordHelper.isWordValid(independentVariableName),
          () -> "Invalid independentVariableName: " + independentVariableName);

      this.independentVariableNames.add(independentVariableName);
    }

    return this;
  }

  @Override
  public DependentVariable build()
  {
    return new DependentVariableImpl(narsese, name,
        Collections.unmodifiableSet(independentVariableNames));
  }
}
