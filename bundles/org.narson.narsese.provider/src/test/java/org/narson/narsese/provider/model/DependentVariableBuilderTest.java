package org.narson.narsese.provider.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.DependentVariable;
import org.narson.api.narsese.DependentVariableBuilder;
import org.narson.api.narsese.NarseseFactory;
import org.narson.narsese.NarseseProvider;

public class DependentVariableBuilderTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  private DependentVariableBuilder b;
  private DependentVariable v;

  @Test
  public void testNonAnonymousVar()
  {
    v = nf.dependentVariable("v").build();

    assertThat(v.getName(), equalTo("v"));
    assertThat(v.isAnonymous(), is(false));
    assertThat(v.getIndependentVariableNames().size(), equalTo(0));

    v = nf.dependentVariable("v").dependsOn("a").build();

    assertThat(v.getName(), equalTo("v"));
    assertThat(v.isAnonymous(), is(false));
    assertThat(v.getIndependentVariableNames().size(), equalTo(1));
  }

  @Test
  public void testAnonymousVar()
  {
    v = nf.dependentVariable("").build();

    assertThat(v.getName(), equalTo(""));
    assertThat(v.isAnonymous(), is(true));
    assertThat(v.getIndependentVariableNames().size(), equalTo(0));

    v = nf.dependentVariable("").dependsOn("a").build();

    assertThat(v.getName(), equalTo(""));
    assertThat(v.isAnonymous(), is(true));
    assertThat(v.getIndependentVariableNames().size(), equalTo(0));
  }

  @Test
  public void testWithNullVar()
  {
    b = nf.dependentVariable("v");

    final String nullVar = null;
    final String[] nullAVars = null;
    final Set<String> nullVars = null;

    assertThrows(NullPointerException.class, () -> b.dependsOn(nullVar));
    assertThrows(NullPointerException.class, () -> b.dependsOn(nullAVars));
    assertThrows(NullPointerException.class, () -> b.dependsOn(nullVars));

    final String[] aVars = new String[1];
    final Set<String> vars = new HashSet<>();
    vars.add(null);

    assertThrows(NullPointerException.class, () -> b.dependsOn(aVars));
    assertThrows(NullPointerException.class, () -> b.dependsOn(vars));
  }
}
