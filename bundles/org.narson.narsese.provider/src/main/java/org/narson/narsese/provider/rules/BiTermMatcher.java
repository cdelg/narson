package org.narson.narsese.provider.rules;

import java.util.List;
import java.util.Map;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.Term;

final class BiTermMatcher extends AbstractTermMatcher
{
  private final Term toMatch;
  private final Term otherToMatch;
  private final Term term;
  private final Term otherTerm;

  protected BiTermMatcher(NarseseFactory nf, Term toInstanciate, Term toMatch, Term otherToMatch,
      Term term, Term otherTerm)
  {
    super(nf, toInstanciate);
    this.toMatch = toMatch;
    this.otherToMatch = otherToMatch;
    this.term = term;
    this.otherTerm = otherTerm;
  }

  @Override
  protected List<Map<String, Term>> applyMatch()
  {
    return combine(match(toMatch, term), match(otherToMatch, otherTerm));
  }


  static protected boolean composable(Term t1, Term t2)
  {
    if (!t1.equals(t2))
    {
      boolean ok = true;

      if (t1.getValueType() == ValueType.COMPOUND_TERM)
      {
        ok = !t1.asCompoundTerm().getTerms().contains(t2);
      }

      if (ok && t2.getValueType() == ValueType.COMPOUND_TERM)
      {
        ok = !t2.asCompoundTerm().getTerms().contains(t1);
      }

      return ok;
    } else
    {
      return false;
    }
  }
}
