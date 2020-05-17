package org.narson.narsese.provider;

import java.util.List;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class SimilarityCopulaTerm extends AbstractSymmetricCopulaTerm
{
  public SimilarityCopulaTerm(Narsese narsese, Term subject, Term predicate, Tense tense)
  {
    super(narsese, subject, Copula.SIMILARITY, predicate, tense);
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, double evidentialHorizon,
      List<Inference> inferences)
  {
    super.computeInferences(truthValue, evidentialHorizon, inferences);
    computeStructuralInferences(truthValue, inferences);
  }

  public void computeStructuralInferences(TruthValueImpl truthValue, List<Inference> inferences)
  {
    /* Structural equivalence rule (S<->P) <===> (S-->P) & (P-->S) */
    inferences.add(new DefaultInference(Inference.Type.STRUCTURAL_EQUIVALENCE, nf
        .judgment(nf.compoundTerm(Connector.CONJUNCTION)
            .of(nf.copulaTerm(getSubject(), Copula.INHERITANCE, getPredicate()),
                nf.copulaTerm(getPredicate(), Copula.INHERITANCE, getSubject()))
            .build())
        .truthValue(truthValue).build()));

    /* Structural equivalence rule (S<->P) <===> {S}<->{P} */
    inferences.add(new DefaultInference(Inference.Type.STRUCTURAL_EQUIVALENCE,
        nf.judgment(nf.copulaTerm(
            nf.compoundTerm(Connector.EXTENSIONAL_SET).of(getSubject()).build(), Copula.SIMILARITY,
            nf.compoundTerm(Connector.EXTENSIONAL_SET).of(getPredicate()).build()))
            .truthValue(truthValue).build()));

    /* Structural equivalence rule (S<->P) <===> [S]<->[P] */
    inferences.add(new DefaultInference(Inference.Type.STRUCTURAL_EQUIVALENCE,
        nf.judgment(nf.copulaTerm(
            nf.compoundTerm(Connector.INTENSIONAL_SET).of(getSubject()).build(), Copula.SIMILARITY,
            nf.compoundTerm(Connector.INTENSIONAL_SET).of(getPredicate()).build()))
            .truthValue(truthValue).build()));

    /* Structural implication rule (S<->P) ===> (S-->P) */
    inferences.add(new DefaultInference(Inference.Type.STRUCTURAL_IMPLICATION,
        nf.judgment(nf.copulaTerm(getSubject(), Copula.INHERITANCE, getPredicate()))
            .truthValue(truthValue).build()));
  }
}
