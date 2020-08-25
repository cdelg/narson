package org.narson.narsese.provider.rules;

import static org.narson.tools.PredChecker.checkArgument;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Term;

final public class TermPattern
{
  private final NarseseFactory nf;
  private final Term toMatch;
  private final Term toInstanciate;

  protected TermPattern(NarseseFactory nf, Term toMatch, Term toInstanciate)
  {
    checkPatterns(toMatch, toInstanciate);
    this.nf = nf;
    this.toMatch = toMatch;
    this.toInstanciate = toInstanciate;
  }

  public AbstractTermMatcher matcher(Term term)
  {
    return new TermMatcher(nf, toInstanciate, toMatch, term);
  }

  static private void checkPatterns(Term toMatch, Term toInstanciate)
  {
    final List<String> cToMatch = new ArrayList<>();
    final List<String> cToInstanciate = new ArrayList<>();

    checkPattern(toMatch, cToMatch);
    checkPattern(toInstanciate, cToInstanciate);


    checkArgument(cToMatch.size() == new HashSet<>(cToMatch).size(),
        "Constants must be different.");
    checkArgument(cToInstanciate.size() == new HashSet<>(cToInstanciate).size(),
        "Constants must be different.");

    cToInstanciate.removeAll(cToMatch);

    checkArgument(cToInstanciate.size() == 0, "Unbound constant.");
  }

  static protected void checkPattern(Term term, List<String> constants)
  {
    switch (term.getValueType())
    {
      case COMPOUND_TERM:
        term.asCompoundTerm().getTerms().forEach(t -> checkPattern(term, constants));
        break;
      case CONSTANT_TERM:
        constants.add(term.asConstantTerm().getName());
        break;
      case COPULA_TERM:
        checkPattern(term.asCopulaTerm().getSubject(), constants);
        checkPattern(term.asCopulaTerm().getPredicate(), constants);
        break;
      case IMAGE_TERM:
        term.asImageTerm().getTerms().forEach(t -> checkPattern(term, constants));
        checkArgument(term.asImageTerm().getTerms().size() == 2, "Invalid image nb terms.");
        break;
      case NEGATION_TERM:
        checkPattern(term.asNegationTerm().getTerm(), constants);
        break;
      case SET_TERM:
        term.asSetTerm().getTerms().forEach(t -> checkPattern(term, constants));
        checkArgument(term.asSetTerm().getTerms().size() == 1, "Invalid set nb terms.");
        break;
      default:
        checkArgument(false, "Invalid pattern.");
        break;
    }
  }
}
