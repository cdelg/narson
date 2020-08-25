package org.narson.narsese.provider.rules;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.RecursiveCompoundView;
import org.narson.api.narsese.Term;

abstract class AbstractTermMatcher
{
  private final NarseseFactory nf;
  private final Term toInstanciate;

  protected AbstractTermMatcher(NarseseFactory nf, Term toInstanciate)
  {
    this.nf = nf;
    this.toInstanciate = toInstanciate;
  }

  final public List<Term> matches()
  {
    return applyMatch().stream().map(matched -> instanciate(toInstanciate, matched))
        .collect(Collectors.toList());
  }

  abstract protected List<Map<String, Term>> applyMatch();

  private Term instanciate(Term pattern, Map<String, Term> matched)
  {
    switch (pattern.getValueType())
    {
      case CONSTANT_TERM:
        return matched.get(pattern.asConstantTerm().getName());
      case COPULA_TERM:
        return instanciate(pattern.asCopulaTerm(), matched);
      case COMPOUND_TERM:
        return instanciate(pattern.asCompoundTerm(), matched);
      case NEGATION_TERM:
        return nf.negation(instanciate(pattern.asNegationTerm().getTerm(), matched));
      default:
        throw new IllegalStateException("Invalid pattern " + toInstanciate);
    }
  }

  private CompoundTerm instanciate(CompoundTerm pattern, Map<String, Term> matched)
  {
    return nf.compound(pattern.getConnector()).of(
        pattern.getTerms().stream().map(t -> instanciate(t, matched)).collect(Collectors.toList()))
        .build();
  }

  private CopulaTerm instanciate(CopulaTerm pattern, Map<String, Term> matched)
  {
    return nf.copula(instanciate(pattern.getSubject(), matched), pattern.getCopula(),
        instanciate(pattern.getPredicate(), matched));
  }

  protected List<Map<String, Term>> match(Term pattern, Term term)
  {
    switch (pattern.getValueType())
    {
      case CONSTANT_TERM:
        return Collections
            .singletonList(Collections.singletonMap(pattern.asConstantTerm().getName(), term));
      case COMPOUND_TERM:
        return match(pattern.asCompoundTerm(), term.asCompoundTerm());
      case COPULA_TERM:
        return match(pattern.asCopulaTerm(), term.asCopulaTerm());
      case NEGATION_TERM:
        return match(pattern.asNegationTerm().getTerm(), term.asNegationTerm().getTerm());
      default:
        throw new IllegalStateException("Invalid pattern " + pattern);
    }
  }

  private List<Map<String, Term>> match(CopulaTerm pattern, CopulaTerm term)
  {
    return combine(match(pattern.getSubject(), term.getSubject()),
        match(pattern.getPredicate(), term.getPredicate()));
  }

  private List<Map<String, Term>> match(CompoundTerm pattern, CompoundTerm term)
  {
    if (term.getTerms().size() == 1)
    {
      return match(pattern.getTerms().iterator().next(), term.getTerms().iterator().next());
    } else
    {
      final RecursiveCompoundView patternV = pattern.getRecursiveViews().get(0);

      return term.getRecursiveViews().stream()
          .flatMap(r -> combine(match(patternV.getFirst(), r.getFirst()),
              match(patternV.getSecond(), r.getSecond())).stream())
          .collect(Collectors.toList());
    }
  }

  protected List<Map<String, Term>> combine(List<Map<String, Term>> matched1,
      List<Map<String, Term>> matched2)
  {
    return matched1.stream().flatMap(m1 -> {
      return matched2.stream().map(m2 -> {
        final Map<String, Term> merge = new HashMap<>(m1);
        merge.putAll(m2);
        return merge;
      });
    }).collect(Collectors.toList());
  }
}
