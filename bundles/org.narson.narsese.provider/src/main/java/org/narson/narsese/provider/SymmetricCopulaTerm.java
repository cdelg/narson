package org.narson.narsese.provider;

import java.util.List;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class SymmetricCopulaTerm extends AbstractCopulaTerm
{
  public SymmetricCopulaTerm(Narsese narsese, Term subject, boolean firstOrder, Term predicate,
      Tense tense)
  {
    super(narsese, subject, firstOrder ? Copula.SIMILARITY : Copula.EQUIVALENCE, predicate, tense);
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    super.computeInferences(truthValue, otherTerm, otherTruthValue, evidentialHorizon, inferences);

    if (!getCopula().isFirstOrder())
    {
      computeStrongConditionalSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
    }
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    computeStrongSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
  }

  private void computeStrongConditionalSyllogism(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    /* Strong conditional syllogism rule {(M<=>P),M} |- P ana' */
    inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION,
        nf.judgment(getPredicate()).truthValue(otherTruthValue.computeDeduction(truthValue))
            .build()));
  }

  public void computeStrongSyllogism(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    if (!otherTerm.getCopula().isSymmetric())
    {
      if (getSubject().equals(otherTerm.getPredicate()))
      {
        /* Strong syllogism rule {(M<->P),(S-->M)} |- (S-->P) ana' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ANALOGY, nf
            .judgment(nf.copulaTerm(getPredicate(), otherTerm.getCopula(), otherTerm.getSubject()))
            .truthValue(otherTruthValue.computeAnalogy(truthValue)).build()));

      }
      if (getSubject().equals(otherTerm.getSubject()))
      {
        /* Strong syllogism rule {(M<->P),(M-->S)} |- (P-->S) ana' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ANALOGY,
            nf.judgment(
                nf.copulaTerm(getPredicate(), otherTerm.getCopula(), otherTerm.getPredicate()))
                .truthValue(otherTruthValue.computeAnalogy(truthValue)).build()));
      }
    } else
    {
      if (getSubject().equals(otherTerm.getPredicate()))
      {
        /* Strong syllogism rule {(M<->P),(S<->M)} |- (S<->P) res */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_RESEMBLANCE, nf
            .judgment(nf.copulaTerm(getPredicate(), otherTerm.getCopula(), otherTerm.getSubject()))
            .truthValue(truthValue.computeResemblance(otherTruthValue)).build()));
      }
    }
  }
}
