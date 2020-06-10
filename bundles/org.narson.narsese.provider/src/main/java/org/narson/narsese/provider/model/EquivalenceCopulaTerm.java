package org.narson.narsese.provider.model;

import java.util.List;
import org.narson.api.narsese.CompoundConnector;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class EquivalenceCopulaTerm extends AbstractSymmetricCopulaTerm
{
  public EquivalenceCopulaTerm(Narsese narsese, Term subject, Term predicate, Tense tense)
  {
    super(narsese, subject, Copula.EQUIVALENCE, predicate, tense);
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
    /* Structural equivalence rule (S<=>P) <===> (S==>P) & (P==>S) */
    inferences.add(new DefaultInference(Inference.Type.STRUCTURAL_EQUIVALENCE, nf
        .judgment(nf.compound(CompoundConnector.CONJUNCTION)
            .of(nf.copula(getSubject(), Copula.IMPLICATION, getPredicate()),
                nf.copula(getPredicate(), Copula.IMPLICATION, getSubject()))
            .build())
        .truthValue(truthValue).build()));

  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    super.computeInferences(truthValue, otherTerm, otherTruthValue, evidentialHorizon, inferences);
    computeStrongConditionalSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
  }

  private void computeStrongConditionalSyllogism(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    /* Strong conditional syllogism rule {(M<=>P),M} |- P ana' */
    inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION,
        nf.judgment(getPredicate()).truthValue(otherTruthValue.computeDeduction(truthValue))
            .build()));
  }
}
