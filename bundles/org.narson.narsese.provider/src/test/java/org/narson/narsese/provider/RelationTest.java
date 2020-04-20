package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.Relation;

public class RelationTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  private Relation v1;
  private Relation v2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("a")).getValueType(),
        equalTo(ValueType.RELATION));
  }

  @Test
  public void testEqual()
  {
    v1 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("a"));
    v2 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("a"));
    assertThat(v1, equalTo(v2));

    v1 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("a"));
    v2 = nf.relation(nf.constant("a"), Copula.INSTANCE_PROPERTY, nf.constant("a"));
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("b"));
    v2 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("a"));
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.relation(nf.constant("b"), Copula.INSTANCE, nf.constant("a"));
    v2 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("a"));
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("a"));
    v2 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("b"));
    assertThat(v1, is(not(equalTo(v2))));

    v1 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("a"));
    v2 = nf.relation(nf.constant("b"), Copula.INSTANCE, nf.constant("a"));
    assertThat(v1, is(not(equalTo(v2))));
  }

  @Test
  public void testComplexity()
  {
    v1 = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("a"));
    assertThat(v1.getSyntacticComplexity(), equalTo(3));

    v1 = nf.relation(v1, Copula.INSTANCE, nf.constant("a"));
    assertThat(v1.getSyntacticComplexity(), equalTo(5));
  }
}
