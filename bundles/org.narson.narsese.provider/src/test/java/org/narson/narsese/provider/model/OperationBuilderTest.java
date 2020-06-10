package org.narson.narsese.provider.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Term;
import org.narson.narsese.NarseseProvider;

public class OperationBuilderTest
{
  private final NarseseFactory nf = new NarseseProvider().getNarseseFactory();

  @Test
  public void testWithNullTerm()
  {
    final Term nullTerm = null;
    final Term[] nullATerms = null;
    final List<Term> nullTerms = null;

    assertThrows(NullPointerException.class, () -> nf.operation("a").on(nullTerm));
    assertThrows(NullPointerException.class, () -> nf.operation("a").on(nullATerms));
    assertThrows(NullPointerException.class, () -> nf.operation("a").on(nullTerms));

    final Term[] aTerms = new Term[1];
    final List<Term> terms = new ArrayList<>();
    terms.add(null);

    assertThrows(NullPointerException.class, () -> nf.operation("a").on(aTerms));
    assertThrows(NullPointerException.class, () -> nf.operation("a").on(terms));
  }

  public void testWithTerm()
  {
    assertThat(nf.operation("a").on(nf.constant("a")).build().getTerms().size(), equalTo(1));
    assertThat(nf.operation("a").on(nf.constant("a"), nf.constant("b")).build().getTerms(),
        hasItems(nf.constant("a"), nf.constant("b")));
  }
}
