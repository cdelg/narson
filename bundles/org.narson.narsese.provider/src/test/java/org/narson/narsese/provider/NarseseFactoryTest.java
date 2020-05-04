package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.SecondaryCopula;
import org.narson.api.narsese.Tense;

public class NarseseFactoryTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

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
    assertThrows(NullPointerException.class, () -> nf.independentVariable(null));
    assertThrows(IllegalArgumentException.class, () -> nf.independentVariable("a-"));
    assertThrows(IllegalArgumentException.class, () -> nf.independentVariable("1a"));
    assertThrows(IllegalArgumentException.class, () -> nf.independentVariable("_"));
    assertThrows(IllegalArgumentException.class, () -> nf.independentVariable(""));

    assertThat(nf.independentVariable("a").getName(), equalTo("a"));
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
    final Copula c = null;
    assertThrows(NullPointerException.class,
        () -> nf.copulaTerm(null, Copula.INHERITANCE, nf.constant("a")));
    assertThrows(NullPointerException.class,
        () -> nf.copulaTerm(nf.constant("a"), c, nf.constant("a")));
    assertThrows(NullPointerException.class,
        () -> nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, null));

    CopulaTerm r = nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.INHERITANCE));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
    assertThat(r.getTense(), equalTo(Tense.NONE));

    r = nf.copulaTerm(nf.constant("a"), Copula.SIMILARITY, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.SIMILARITY));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
    assertThat(r.getTense(), equalTo(Tense.NONE));

    r = nf.copulaTerm(nf.constant("a"), Copula.IMPLICATION, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.IMPLICATION));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
    assertThat(r.getTense(), equalTo(Tense.NONE));

    r = nf.copulaTerm(nf.constant("a"), Copula.EQUIVALENCE, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.EQUIVALENCE));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
    assertThat(r.getTense(), equalTo(Tense.NONE));
  }

  @Test
  public void testSecondaryRelation()
  {
    final SecondaryCopula c = null;
    assertThrows(NullPointerException.class,
        () -> nf.copulaTerm(null, SecondaryCopula.INSTANCE, nf.constant("a")));
    assertThrows(NullPointerException.class,
        () -> nf.copulaTerm(nf.constant("a"), c, nf.constant("a")));
    assertThrows(NullPointerException.class,
        () -> nf.copulaTerm(nf.constant("a"), SecondaryCopula.INSTANCE, null));

    CopulaTerm r = nf.copulaTerm(nf.constant("a"), SecondaryCopula.INSTANCE, nf.constant("b"));
    assertThat(r.getSubject(),
        equalTo(nf.compoundTerm(Connector.EXTENSIONAL_SET).of(nf.constant("a")).build()));
    assertThat(r.getCopula(), equalTo(Copula.INHERITANCE));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));

    r = nf.copulaTerm(nf.constant("a"), SecondaryCopula.PROPERTY, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.INHERITANCE));
    assertThat(r.getPredicate(),
        equalTo(nf.compoundTerm(Connector.INTENSIONAL_SET).of(nf.constant("b")).build()));

    r = nf.copulaTerm(nf.constant("a"), SecondaryCopula.INSTANCE_PROPERTY, nf.constant("b"));
    assertThat(r.getSubject(),
        equalTo(nf.compoundTerm(Connector.EXTENSIONAL_SET).of(nf.constant("a")).build()));
    assertThat(r.getCopula(), equalTo(Copula.INHERITANCE));
    assertThat(r.getPredicate(),
        equalTo(nf.compoundTerm(Connector.INTENSIONAL_SET).of(nf.constant("b")).build()));

    r = nf.copulaTerm(nf.constant("a"), SecondaryCopula.CONCURRENT_EQUIVALENCE, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.EQUIVALENCE));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
    assertThat(r.getTense(), equalTo(Tense.PRESENT));

    r = nf.copulaTerm(nf.constant("a"), SecondaryCopula.PREDICTIVE_EQUIVALENCE, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.EQUIVALENCE));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
    assertThat(r.getTense(), equalTo(Tense.FUTURE));

    r = nf.copulaTerm(nf.constant("a"), SecondaryCopula.CONCURRENT_IMPLICATION, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.IMPLICATION));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
    assertThat(r.getTense(), equalTo(Tense.PRESENT));

    r = nf.copulaTerm(nf.constant("a"), SecondaryCopula.PREDICTIVE_IMPLICATION, nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.IMPLICATION));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
    assertThat(r.getTense(), equalTo(Tense.FUTURE));

    r = nf.copulaTerm(nf.constant("a"), SecondaryCopula.RETROSPECTIVE_IMPLICATION,
        nf.constant("b"));
    assertThat(r.getSubject(), equalTo(nf.constant("a")));
    assertThat(r.getCopula(), equalTo(Copula.IMPLICATION));
    assertThat(r.getPredicate(), equalTo(nf.constant("b")));
    assertThat(r.getTense(), equalTo(Tense.PAST));
  }
}
