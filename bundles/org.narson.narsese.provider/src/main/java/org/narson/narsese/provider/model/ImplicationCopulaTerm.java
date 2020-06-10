package org.narson.narsese.provider.model;

import java.util.List;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class ImplicationCopulaTerm extends AbstractAsymmetricCopulaTerm
{
  public ImplicationCopulaTerm(Narsese narsese, Term subject, Term predicate, Tense tense)
  {
    super(narsese, subject, Copula.IMPLICATION, predicate, tense);
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, double evidentialHorizon,
      List<Inference> inferences)
  {
    super.computeInferences(truthValue, evidentialHorizon, inferences);

    computeImmediate(truthValue, inferences, evidentialHorizon);
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    super.computeInferences(truthValue, otherTerm, otherTruthValue, evidentialHorizon, inferences);
    computeStrongConditionalSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
    computeWeakConditionalSyllogism(truthValue, otherTerm, otherTruthValue, evidentialHorizon,
        inferences);
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    super.computeInferences(truthValue, otherTerm, otherTruthValue, evidentialHorizon, inferences);
    computeStrongConditionalSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
    computeWeakConditionalSyllogism(truthValue, otherTerm, otherTruthValue, evidentialHorizon,
        inferences);
  }

  private void computeImmediate(TruthValueImpl truthValue, List<Inference> inferences,
      double evidentialHorizon)
  {
    /* Immediate Contraposition rule {(S==>P)} |- ((!P)==>(!S)) */
    inferences.add(new DefaultInference(Inference.Type.IMMEDIATE_CONTRAPOSITION,
        nf.judgment(nf.copula(nf.negation(getPredicate()), getCopula(), nf.negation(getSubject())))
            .truthValue(truthValue.computeContraposition(evidentialHorizon)).build()));
  }

  private void computeStrongConditionalSyllogism(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    // /* Strong conditional syllogism rule {(M==>P),M} |- P ded */
    // inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION,
    // nf.judgment(getPredicate()).truthValue(truthValue.computeDeduction(otherTruthValue))
    // .build()));
    //
    // if (getSubject().getValueType() == ValueType.COMPOUND_TERM)
    // {
    // final CompoundTerm compound = getSubject().asCompoundTerm();
    // if (compound.getConnector() == CompoundConnector.CONJUNCTION
    // && compound.getTerms().size() == 2)
    // {
    // if (compound.getTerms().get(1).equals(otherTerm))
    // {
    // /* Strong conditional syllogism rule {(M==>P),M} |- P ded */
    // inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION,
    // nf.judgment(nf.copulaTerm(compound.getTerms().get(0), getCopula(), getPredicate()))
    // .truthValue(truthValue.computeDeduction(otherTruthValue)).build()));
    // }
    // }
    // }
  }

  private void computeStrongConditionalSyllogism(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    // if (getSubject().getValueType() == ValueType.COMPOUND_TERM)
    // {
    // final CompoundTerm compound = getSubject().asCompoundTerm();
    // if (compound.getConnector() == CompoundConnector.CONJUNCTION
    // && compound.getTerms().size() == 2)
    // {
    // if (compound.getTerms().get(1).equals(otherTerm.getPredicate()))
    // {
    // /* Strong conditional syllogism rule {((Q && M)==>P),(S==>M)} |- ((Q && S)==>P) ded */
    // inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION, nf
    // .judgment(nf.copulaTerm(
    // nf.compound(CompoundConnector.CONJUNCTION)
    // .of(compound.getTerms().get(0), otherTerm.getSubject()).build(),
    // getCopula(), getPredicate()))
    // .truthValue(truthValue.computeDeduction(otherTruthValue)).build()));
    // }
    // }
    // }
  }

  private void computeWeakConditionalSyllogism(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    /* Strong conditional syllogism rule {(P==>M),M} |- P abd */
    inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_ABDUCTION,
        nf.judgment(getSubject())
            .truthValue(truthValue.computeAbduction(otherTruthValue, evidentialHorizon)).build()));
  }

  private void computeWeakConditionalSyllogism(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    // /* Weak conditional syllogism rule {(M==>P),S} |- ((S && M)==>P) ind */
    // inferences
    // .add(
    // new DefaultInference(
    // Inference.Type.CONDITIONAL_SYLLOGISTIC_INDUCTION, nf
    // .judgment(nf.copula(nf.compound(CompoundConnector.CONJUNCTION)
    // .of(otherTerm, getSubject()).build(), getCopula(), getPredicate()))
    // .truthValue(truthValue.computeInduction(otherTruthValue, evidentialHorizon))
    // .build()));
    //
    // if (getSubject().getValueType() == ValueType.COMPOUND_TERM)
    // {
    // final CompoundTerm compound = getSubject().asCompoundTerm();
    // if (compound.getConnector() == CompoundConnector.CONJUNCTION
    // && compound.getTerms().size() == 2)
    // {
    // if (compound.getTerms().get(0).equals(otherTerm.getSubject())
    // && getPredicate().equals(otherTerm.getPredicate()))
    // {
    // /* Weak conditional syllogism rule {((C && P)==>M),(C==>M)} |- P abd */
    // inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_ABDUCTION,
    // nf.judgment(compound.getTerms().get(1))
    // .truthValue(truthValue.computeAbduction(otherTruthValue, evidentialHorizon))
    // .build()));
    // }
    //
    // if (compound.getTerms().get(1).equals(otherTerm.getSubject()))
    // {
    // /* Weak conditional syllogism rule {((Q && M)==>P),(M==>S)} |- ((Q && S)==>P) ind */
    // inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_INDUCTION, nf
    // .judgment(nf.copulaTerm(
    // nf.compound(CompoundConnector.CONJUNCTION)
    // .of(compound.getTerms().get(0), otherTerm.getPredicate()).build(),
    // getCopula(), getPredicate()))
    // .truthValue(truthValue.computeInduction(otherTruthValue, evidentialHorizon))
    // .build()));
    // }
    //
    // if (otherTerm.getSubject().getValueType() == ValueType.COMPOUND_TERM)
    // {
    // final CompoundTerm otherCompound = otherTerm.getSubject().asCompoundTerm();
    //
    // if (compound.getTerms().get(0).equals(otherCompound.getTerms().get(0))
    // && getPredicate().equals(otherTerm.getPredicate()))
    // {
    // /* Weak conditional syllogism rule {((C && P)==>M),((C && S)==>M)} |- (S==>P) abd */
    // inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_ABDUCTION,
    // nf.judgment(nf.copulaTerm(otherCompound.getTerms().get(1), getCopula(),
    // compound.getTerms().get(1)))
    // .truthValue(truthValue.computeAbduction(otherTruthValue, evidentialHorizon))
    // .build()));
    // }
    // }
    // }
    // }
  }
}
