package org.narson.narsese.provider;

import org.narson.api.narsese.Copula;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class SimilarityCopulaTerm extends AbstractSymmetricCopulaTerm
{
  public SimilarityCopulaTerm(Narsese narsese, Term subject, Term predicate, Tense tense)
  {
    super(narsese, subject, Copula.SIMILARITY, predicate, tense);
  }
}
