package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.CompoundTermBuilder;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Term;

public class CompoundTermBuilderTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  @Test
  public void testWithNullTerm()
  {
    final Term nullTerm = null;
    final Term[] nullATerms = null;
    final List<Term> nullTerms = null;

    assertThrows(NullPointerException.class,
        () -> nf.compoundTerm(Connector.CONJUNCTION).of(nullTerm));
    assertThrows(NullPointerException.class,
        () -> nf.compoundTerm(Connector.CONJUNCTION).of(nullATerms));
    assertThrows(NullPointerException.class,
        () -> nf.compoundTerm(Connector.CONJUNCTION).of(nullTerms));

    final Term[] aTerms = new Term[1];
    final List<Term> terms = new ArrayList<>();
    terms.add(null);

    assertThrows(NullPointerException.class,
        () -> nf.compoundTerm(Connector.CONJUNCTION).of(aTerms));
    assertThrows(NullPointerException.class,
        () -> nf.compoundTerm(Connector.CONJUNCTION).of(terms));
  }

  @Test
  public void testNotEnoughTerms()
  {
    assertThrows(IllegalStateException.class, () -> buildCompoundTerm(Connector.CONJUNCTION, 1));
    assertThrows(IllegalStateException.class, () -> buildCompoundTerm(Connector.DISJUNCTION, 1));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.EXTENSIONAL_DIFFERENCE, 1));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.EXTENSIONAL_IMAGE, 1));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.EXTENSIONAL_INTERSECTION, 1));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.EXTENSIONAL_SET, 0));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.INTENSIONAL_DIFFERENCE, 1));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.INTENSIONAL_IMAGE, 1));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.INTENSIONAL_INTERSECTION, 1));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.INTENSIONAL_SET, 0));
    assertThrows(IllegalStateException.class, () -> buildCompoundTerm(Connector.NEGATION, 0));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.PARALLEL_CONJUNCTION, 1));
    assertThrows(IllegalStateException.class, () -> buildCompoundTerm(Connector.PRODUCT, 1));
    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.SEQUENTIAL_CONJUNCTION, 1));
  }

  @Test
  public void testEnoughTerms()
  {
    assertThat(buildCompoundTerm(Connector.CONJUNCTION, 2).getTerms().size(), equalTo(2));
    assertThat(buildCompoundTerm(Connector.CONJUNCTION, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.DISJUNCTION, 2).getTerms().size(), equalTo(2));
    assertThat(buildCompoundTerm(Connector.DISJUNCTION, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_DIFFERENCE, 2).getTerms().size(),
        equalTo(2));
    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_DIFFERENCE, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_IMAGE, 2).getTerms().size(), equalTo(2));
    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_IMAGE, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_INTERSECTION, 2).getTerms().size(),
        equalTo(2));
    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_INTERSECTION, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_SET, 1).getTerms().size(), equalTo(1));
    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_SET, 1).getTerms(),
        hasItems(nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.INTENSIONAL_DIFFERENCE, 2).getTerms().size(),
        equalTo(2));
    assertThat(buildCompoundTerm(Connector.INTENSIONAL_DIFFERENCE, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.INTENSIONAL_IMAGE, 2).getTerms().size(), equalTo(2));
    assertThat(buildCompoundTerm(Connector.INTENSIONAL_IMAGE, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.INTENSIONAL_INTERSECTION, 2).getTerms().size(),
        equalTo(2));
    assertThat(buildCompoundTerm(Connector.INTENSIONAL_INTERSECTION, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.INTENSIONAL_SET, 1).getTerms().size(), equalTo(1));
    assertThat(buildCompoundTerm(Connector.INTENSIONAL_SET, 1).getTerms(),
        hasItems(nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.NEGATION, 1).getTerms().size(), equalTo(1));
    assertThat(buildCompoundTerm(Connector.NEGATION, 1).getTerms(), hasItems(nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.PARALLEL_CONJUNCTION, 2).getTerms().size(), equalTo(2));
    assertThat(buildCompoundTerm(Connector.PARALLEL_CONJUNCTION, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.PRODUCT, 2).getTerms().size(), equalTo(2));
    assertThat(buildCompoundTerm(Connector.PRODUCT, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.SEQUENTIAL_CONJUNCTION, 2).getTerms().size(),
        equalTo(2));
    assertThat(buildCompoundTerm(Connector.SEQUENTIAL_CONJUNCTION, 2).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t")));
  }

  @Test
  public void testMoreTerms()
  {
    assertThat(buildCompoundTerm(Connector.CONJUNCTION, 3).getTerms().size(), equalTo(3));
    assertThat(buildCompoundTerm(Connector.CONJUNCTION, 3).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.DISJUNCTION, 3).getTerms().size(), equalTo(3));
    assertThat(buildCompoundTerm(Connector.DISJUNCTION, 3).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t"), nf.constant("t")));

    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.EXTENSIONAL_DIFFERENCE, 3));

    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_IMAGE, 3).getTerms().size(), equalTo(3));
    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_IMAGE, 3).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_INTERSECTION, 3).getTerms().size(),
        equalTo(3));
    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_INTERSECTION, 3).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_SET, 2).getTerms().size(), equalTo(1));
    assertThat(buildCompoundTerm(Connector.EXTENSIONAL_SET, 2).getTerms(),
        hasItems(nf.constant("t")));

    assertThrows(IllegalStateException.class,
        () -> buildCompoundTerm(Connector.INTENSIONAL_DIFFERENCE, 3));

    assertThat(buildCompoundTerm(Connector.INTENSIONAL_IMAGE, 3).getTerms().size(), equalTo(3));
    assertThat(buildCompoundTerm(Connector.INTENSIONAL_IMAGE, 3).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.INTENSIONAL_INTERSECTION, 3).getTerms().size(),
        equalTo(3));
    assertThat(buildCompoundTerm(Connector.INTENSIONAL_INTERSECTION, 3).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.INTENSIONAL_SET, 2).getTerms().size(), equalTo(1));
    assertThat(buildCompoundTerm(Connector.INTENSIONAL_SET, 2).getTerms(),
        hasItems(nf.constant("t")));

    assertThrows(IllegalStateException.class, () -> buildCompoundTerm(Connector.NEGATION, 2));

    assertThat(buildCompoundTerm(Connector.PARALLEL_CONJUNCTION, 3).getTerms().size(), equalTo(3));
    assertThat(buildCompoundTerm(Connector.PARALLEL_CONJUNCTION, 3).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.PRODUCT, 3).getTerms().size(), equalTo(3));
    assertThat(buildCompoundTerm(Connector.PRODUCT, 3).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t"), nf.constant("t")));

    assertThat(buildCompoundTerm(Connector.SEQUENTIAL_CONJUNCTION, 3).getTerms().size(),
        equalTo(3));
    assertThat(buildCompoundTerm(Connector.SEQUENTIAL_CONJUNCTION, 3).getTerms(),
        hasItems(nf.constant("t"), nf.constant("t"), nf.constant("t")));
  }

  @Test
  public void testPlaceHolderRight()
  {
    CompoundTermBuilder b = null;

    b = nf.compoundTerm(Connector.INTENSIONAL_IMAGE);
    b.of(nf.constant("t"));
    b.setPlaceHolderPosition();
    b.of(nf.constant("t"));

    assertThat(b.build().getPlaceHolderPosition(), equalTo(2));

    b = nf.compoundTerm(Connector.INTENSIONAL_IMAGE);
    b.of(nf.constant("t"));
    b.of(nf.constant("t"));
    b.setPlaceHolderPosition();
    b.of(nf.constant("t"));

    assertThat(b.build().getPlaceHolderPosition(), equalTo(3));

    b = nf.compoundTerm(Connector.INTENSIONAL_IMAGE);
    b.of(nf.constant("t"));
    b.of(nf.constant("t"));
    b.of(nf.constant("t"));
    b.setPlaceHolderPosition();

    assertThat(b.build().getPlaceHolderPosition(), equalTo(4));

    b = nf.compoundTerm(Connector.INTENSIONAL_IMAGE);
    b.of(nf.constant("t"));
    b.of(nf.constant("t"));
    b.setPlaceHolderPosition(2);

    assertThat(b.build().getPlaceHolderPosition(), equalTo(2));

    b = nf.compoundTerm(Connector.INTENSIONAL_IMAGE);
    b.of(nf.constant("t"));
    b.of(nf.constant("t"));
    b.of(nf.constant("t"));
    b.setPlaceHolderPosition(3);

    assertThat(b.build().getPlaceHolderPosition(), equalTo(3));

    b = nf.compoundTerm(Connector.INTENSIONAL_IMAGE);
    b.of(nf.constant("t"));
    b.of(nf.constant("t"));
    b.of(nf.constant("t"));
    b.setPlaceHolderPosition(4);

    assertThat(b.build().getPlaceHolderPosition(), equalTo(4));
  }

  @Test
  public void testPlaceHolderNoEffect()
  {
    CompoundTermBuilder b = nf.compoundTerm(Connector.CONJUNCTION);

    b.of(nf.constant("t"));
    b.of(nf.constant("t"));

    assertThat(b.build().getPlaceHolderPosition(), equalTo(-1));

    b = nf.compoundTerm(Connector.CONJUNCTION);

    b.of(nf.constant("t"));
    b.of(nf.constant("t"));
    b.setPlaceHolderPosition(2);

    assertThat(b.build().getPlaceHolderPosition(), equalTo(-1));
  }

  @Test
  public void testPlaceHolderTooLow()
  {
    final CompoundTermBuilder b = nf.compoundTerm(Connector.INTENSIONAL_IMAGE);

    assertThrows(IllegalStateException.class, () -> b.setPlaceHolderPosition());

    final CompoundTermBuilder b1 = nf.compoundTerm(Connector.INTENSIONAL_IMAGE);

    assertThrows(IllegalArgumentException.class, () -> b1.setPlaceHolderPosition(1));
  }

  @Test
  public void testPlaceHolderTooHigh()
  {
    final CompoundTermBuilder b1 = nf.compoundTerm(Connector.INTENSIONAL_IMAGE);
    b1.of(nf.constant("t"));
    b1.of(nf.constant("t"));
    b1.setPlaceHolderPosition(4);
    assertThrows(IllegalStateException.class, () -> b1.build());
  }

  public CompoundTerm buildCompoundTerm(Connector c, int numberOfTerm)
  {
    final CompoundTermBuilder builder = nf.compoundTerm(c);
    for (int i = 0; i < numberOfTerm; i++)
    {
      builder.of(nf.constant("t"));
    }
    if (c == Connector.INTENSIONAL_IMAGE || c == Connector.EXTENSIONAL_IMAGE)
    {
      builder.setPlaceHolderPosition();
    }
    return builder.build();
  }
}
