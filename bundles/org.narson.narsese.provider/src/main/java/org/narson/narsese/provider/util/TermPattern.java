package org.narson.narsese.provider.util;

import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Term;

final public class TermPattern
{
  private final NarseseFactory nf;
  private final Term toMatch;
  private final Term toInstanciate;

  protected TermPattern(NarseseFactory nf, Term toMatch, Term toInstanciate)
  {
    this.nf = nf;
    this.toMatch = toMatch;
    this.toInstanciate = toInstanciate;
  }

  public TermMatcher matcher(Term term)
  {
    return new TermMatcher(nf, toMatch, toInstanciate, term);
  }
}
