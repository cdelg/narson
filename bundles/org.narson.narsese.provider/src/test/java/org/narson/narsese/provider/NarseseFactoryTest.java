package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Relation;

public class NarseseFactoryTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  @Test
  public void testCompoundTerm()
  {
    assertThrows(NullPointerException.class, () -> nf.compoundTerm(null));
    assertThat(nf.compoundTerm(Connector.NEGATION).of(nf.constant("a")).build().getConnector(),
        equalTo(Connector.NEGATION));
  }

  @Test
  public void testConstant()
  {
    assertThrows(NullPointerException.class, () -> nf.constant(null));
    assertThrows(IllegalArgumentException.class, () -> nf.constant("a-"));
    assertThrows(IllegalArgumentException.class, () -> nf.constant("1a"));
    assertThrows(IllegalArgumentException.class, () -> nf.constant("_"));
    assertThrows(IllegalArgumentException.class, () -> nf.constant(""));

    assertThat(nf.constant("a").getName(), equalTo("a"));
  }

  @Test
  public void testAnonymousDependentVariable()
  {
    assertThat(nf.dependentVariable().getName(), equalTo(""));
  }

  @Test
  public void testDependentVariable()
  {
    assertThrows(NullPointerException.class, () -> nf.dependentVariable(null));
    assertThrows(IllegalArgumentException.class, () -> nf.dependentVariable("a-"));
    assertThrows(IllegalArgumentException.class, () -> nf.dependentVariable("1a"));
    assertThrows(IllegalArgumentException.class, () -> nf.dependentVariable("_"));

    assertThat(nf.dependentVariable("").build().getName(), equalTo(""));
    assertThat(nf.dependentVariable("a").build().getName(), equalTo("a"));
  }

  @Test
  public void testGoal()
  {
    assertThrows(NullPointerException.class, () -> nf.goal(null));
    assertThat(nf.goal(nf.constant("a")).build().getStatement(), equalTo(nf.constant("a")));
  }

  @Test
  public void testIndependantVariable()
  {
    assertThrows(NullPointerException.class, () -> nf.independantVariable(null));
    assertThrows(IllegalArgumentException.class, () -> nf.independantVariable("a-"));
    assertThrows(IllegalArgumentException.class, () -> nf.independantVariable("1a"));
    assertThrows(IllegalArgumentException.class, () -> nf.independantVariable("_"));
    assertThrows(IllegalArgumentException.class, () -> nf.independantVariable(""));

    assertThat(nf.independantVariable("a").getName(), equalTo("a"));
  }

  @Test
  public void testJudgment()
  {
    assertThrows(NullPointerException.class, () -> nf.judgment(null));
    assertThat(nf.judgment(nf.constant("a")).build().getStatement(), equalTo(nf.constant("a")));
  }

  @Test
  public void testOperation()
  {
    assertThrows(NullPointerException.class, () -> nf.operation(null));
    assertThrows(IllegalArgumentException.class, () -> nf.operation("a-"));
    assertThrows(IllegalArgumentException.class, () -> nf.operation("1a"));
    assertThrows(IllegalArgumentException.class, () -> nf.operation("_"));
    assertThrows(IllegalArgumentException.class, () -> nf.operation(""));

    assertThat(nf.operation("a").build().getName(), equalTo("a"));
  }

  @Test
  public void testQuery()
  {
    assertThrows(NullPointerException.class, () -> nf.query(null));
    assertThat(nf.query(nf.constant("a")).getStatement(), equalTo(nf.constant("a")));
  }

  @Test
  public void testAnonymousQueryVariable()
  {
    assertThat(nf.queryVariable().getName(), equalTo(""));

  }

  @Test
  public void testQueryVariable()
  {
    assertThrows(NullPointerException.class, () -> nf.queryVariable(null));
    assertThrows(IllegalArgumentException.class, () -> nf.queryVariable("a-"));
    assertThrows(IllegalArgumentException.class, () -> nf.queryVariable("1a"));
    assertThrows(IllegalArgumentException.class, () -> nf.queryVariable("_"));

    assertThat(nf.queryVariable("").getName(), equalTo(""));
    assertThat(nf.queryVariable("a").getName(), equalTo("a"));
  }

  @Test
  public void testQuestion()
  {
    assertThrows(NullPointerException.class, () -> nf.question(null));
    assertThat(nf.question(nf.constant("a")).build().getStatement(), equalTo(nf.constant("a")));
  }

  @Test
  public void testRelation()
  {
    assertThrows(NullPointerException.class,
        () -> nf.relation(null, Copula.INSTANCE, nf.constant("a")));
    assertThrows(NullPointerException.class,
        () -> nf.relation(nf.constant("a"), null, nf.constant("a")));
    assertThrows(NullPointerException.class,
        () -> nf.relation(nf.constant("a"), Copula.INSTANCE, null));

    final Relation r = nf.relation(nf.constant("a"), Copula.INSTANCE, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.INSTANCE));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
  }
}
