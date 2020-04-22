package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.SecondaryCopula;

public class CopulaTermTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  private CopulaTerm v1;
  private CopulaTerm v2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("a")).getValueType(),
        equalTo(ValueType.COPULA_TERM));
  }

  @Test
  public void testConvertion()
  {
    assertThat(nf.copulaTerm(nf.constant("a"), Copula.EQUIVALENCE, nf.constant("a")).asCopulaTerm(),
        is(anything()));
    assertThrows(IllegalStateException.class,
        () -> nf.copulaTerm(nf.constant("a"), Copula.EQUIVALENCE, nf.constant("a")).asConstant());
  }

  @Test
  public void testEqual()
  {
    v1 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("a"));
    v2 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("a"));
    assertThat(v1, equalTo(v2));

    v1 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("a"));
    v2 = nf.copulaTerm(nf.constant("a"), Copula.SIMILARITY, nf.constant("a"));
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("b"));
    v2 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("a"));
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.copulaTerm(nf.constant("b"), Copula.INHERITANCE, nf.constant("a"));
    v2 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("a"));
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("a"));
    v2 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("b"));
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("a"));
    v2 = nf.copulaTerm(nf.constant("b"), Copula.INHERITANCE, nf.constant("a"));
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.copulaTerm(nf.constant("a"), SecondaryCopula.PREDICTIVE_EQUIVALENCE, nf.constant("a"));
    v2 = nf.copulaTerm(nf.constant("a"), SecondaryCopula.CONCURRENT_EQUIVALENCE, nf.constant("a"));
    assertThat(v1, is(not(equalTo(v2))));
  }


  @Test
  public void testComplexity()
  {
    v1 = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("a"));
    assertThat(v1.getSyntacticComplexity(), equalTo(3));

    v1 = nf.copulaTerm(v1, Copula.INHERITANCE, nf.constant("a"));
    assertThat(v1.getSyntacticComplexity(), equalTo(5));
  }
}
