package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;

public class CompoundTermTest
{
  private final NarseseFactory nf = new NarseseLanguage().getNarseseFactory();

  private CompoundTerm c1;
  private CompoundTerm c2;

  @Test
  public void testRightValueType()
  {
    assertThat(nf.compoundTerm(Connector.CONJUNCTION).of(nf.constant("a"), nf.constant("a")).build()
        .getValueType(), equalTo(ValueType.COMPOUND_TERM));
  }

  @Test
  public void testTermsListImmutable()
  {
    assertThrows(UnsupportedOperationException.class, () -> nf.compoundTerm(Connector.CONJUNCTION)
        .of(nf.constant("a"), nf.constant("a")).build().getTerms().add(nf.constant("a")));
  }

  @Test
  public void testEqual()
  {
    c1 = nf.compoundTerm(Connector.CONJUNCTION).of(nf.constant("a"), nf.constant("a")).build();
    c2 = nf.compoundTerm(Connector.CONJUNCTION).of(nf.constant("a"), nf.constant("a")).build();

    assertThat(c1, equalTo(c2));

    c1 = nf.compoundTerm(Connector.DISJUNCTION).of(nf.constant("a"), nf.constant("a")).build();
    c2 = nf.compoundTerm(Connector.CONJUNCTION).of(nf.constant("a"), nf.constant("a")).build();

    assertThat(c1, is(not(equalTo(c2))));

    c1 = nf.compoundTerm(Connector.CONJUNCTION).of(nf.constant("a"), nf.constant("a")).build();
    c2 = nf.compoundTerm(Connector.CONJUNCTION).of(nf.constant("a"), nf.constant("b")).build();

    assertThat(c1, is(not(equalTo(c2))));
  }

  @Test
  public void testEqualOrderMatter()
  {
    c1 = nf.compoundTerm(Connector.EXTENSIONAL_DIFFERENCE).of(nf.constant("b"), nf.constant("a"))
        .build();
    c2 = nf.compoundTerm(Connector.EXTENSIONAL_DIFFERENCE).of(nf.constant("a"), nf.constant("b"))
        .build();

    assertThat(c1, is(not(equalTo(c2))));

    c1 = nf.compoundTerm(Connector.EXTENSIONAL_DIFFERENCE).of(nf.constant("b"), nf.constant("a"))
        .build();
    c2 = nf.compoundTerm(Connector.EXTENSIONAL_DIFFERENCE).of(nf.constant("b"), nf.constant("a"))
        .build();

    assertThat(c1, equalTo(c2));
  }

  @Test
  public void testEqualOrderDoesntMatter()
  {
    c1 = nf.compoundTerm(Connector.CONJUNCTION)
        .of(nf.constant("b"), nf.constant("b"), nf.constant("a")).build();
    c2 = nf.compoundTerm(Connector.CONJUNCTION)
        .of(nf.constant("a"), nf.constant("b"), nf.constant("b")).build();

    assertThat(c1, equalTo(c2));
  }

  @Test
  public void testComplexity()
  {
    c1 = nf.compoundTerm(Connector.CONJUNCTION).of(nf.constant("b"), nf.constant("a")).build();
    assertThat(c1.getSyntacticComplexity(), equalTo(3));

    c1 = nf.compoundTerm(Connector.CONJUNCTION).of(nf.constant("b"), nf.constant("a"), c1).build();
    assertThat(c1.getSyntacticComplexity(), equalTo(6));
  }
}
