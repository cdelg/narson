package org.narson.narsese.provider.rules;

import static org.narson.tools.PredChecker.checkArgument;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Term;

final public class BiTermPattern
{
  private final NarseseFactory nf;
  private final Term toMatch;
  private final Term otherToMatch;
  private final Term toInstanciate;

  protected BiTermPattern(NarseseFactory nf, Term toMatch, Term otherToMatch, Term toInstanciate)
  {
    checkPatterns(toMatch, otherToMatch, toInstanciate);
    this.nf = nf;
    this.toMatch = toMatch;
    this.otherToMatch = otherToMatch;
    this.toInstanciate = toInstanciate;
  }

  public AbstractTermMatcher matcher(Term term, Term otherTerm)
  {
    return new BiTermMatcher(nf, toInstanciate, toMatch, term, otherToMatch, otherTerm);
  }

  static private void checkPatterns(Term toMatch, Term otherToMatch, Term toInstanciate)
  {
    final List<String> cToMatch = new ArrayList<>();
    final List<String> cToInstanciate = new ArrayList<>();

    TermPattern.checkPattern(toMatch, cToMatch);
    TermPattern.checkPattern(otherToMatch, cToMatch);
    TermPattern.checkPattern(toInstanciate, cToInstanciate);


    checkArgument(cToMatch.size() == new HashSet<>(cToMatch).size(),
        "Constants must be different.");
    checkArgument(cToInstanciate.size() == new HashSet<>(cToInstanciate).size(),
        "Constants must be different.");

    cToInstanciate.removeAll(cToMatch);
    checkArgument(cToInstanciate.size() == 0, "Unbound constant.");
  }
}
