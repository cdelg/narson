package org.narson.narsese.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotEmpty;
import static org.narson.tools.PredChecker.checkNotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.DependentVariableBuilder;

final class DependentVariableBuilderImpl implements DependentVariableBuilder
{
  private final int bufferSize;
  private final String name;
  private final Set<String> independentVariableNames = new HashSet<>();

  public DependentVariableBuilderImpl(int bufferSize, String name)
  {
    this.bufferSize = bufferSize;
    this.name = name;
  }

  @Override
  public DependentVariableBuilder dependsOn(String independentVariableName)
  {
    checkNotNull(independentVariableName, "independentVariableName");
    checkNotEmpty(independentVariableName, "independentVariableName");
    checkArgument(NarseseChars.isWordValid(independentVariableName),
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
      checkNotEmpty(independentVariableName, "One of the provided name is empty.");
      checkArgument(NarseseChars.isWordValid(independentVariableName),
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
      checkNotEmpty(independentVariableName, "One of the provided name is empty.");
      checkArgument(NarseseChars.isWordValid(independentVariableName),
          () -> "Invalid independentVariableName: " + independentVariableName);

      this.independentVariableNames.add(independentVariableName);
    }

    return this;
  }

  @Override
  public DependentVariable build()
  {
    final List<String> v = new ArrayList<>(independentVariableNames);

    Collections.sort(v);

    return new DependentVariableImpl(bufferSize, name, Collections.unmodifiableList(v));
  }
}
