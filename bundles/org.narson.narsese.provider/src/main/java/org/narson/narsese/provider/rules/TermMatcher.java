package org.narson.narsese.provider.rules;

import java.util.List;
import java.util.Map;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Term;

final class TermMatcher extends AbstractTermMatcher
{
  private final Term toMatch;
  private final Term term;

  protected TermMatcher(NarseseFactory nf, Term toInstanciate, Term toMatch, Term term)
  {
    super(nf, toInstanciate);
    this.toMatch = toMatch;
    this.term = term;
  }

  @Override
  protected List<Map<String, Term>> applyMatch()
  {
    return match(toMatch, term);
  }
}
