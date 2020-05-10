package org.narson.narsese.provider;

import java.util.List;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

abstract class AbstractSymmetricCopulaTerm extends AbstractCopulaTerm
{
  public AbstractSymmetricCopulaTerm(Narsese narsese, Term subject, Copula copula, Term predicate,
      Tense tense)
  {
    super(narsese, subject, copula, predicate, tense);
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    computeStrongSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
  }

  private void computeStrongSyllogism(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    if (!otherTerm.getCopula().isSymmetric())
    {
      if (getSubject().equals(otherTerm.getPredicate()))
      {
        if (!getPredicate().equals(otherTerm.getSubject()))
        {
          /* Strong syllogism rule {(M<->P),(S-->M)} |- (S-->P) ana' */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ANALOGY,
              nf.judgment(
                  nf.copulaTerm(getPredicate(), otherTerm.getCopula(), otherTerm.getSubject()))
                  .truthValue(otherTruthValue.computeAnalogy(truthValue)).build()));
        }

      }
      if (getSubject().equals(otherTerm.getSubject()))
      {
        if (!getPredicate().equals(otherTerm.getPredicate()))
        {
          /* Strong syllogism rule {(M<->P),(M-->S)} |- (P-->S) ana' */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ANALOGY,
              nf.judgment(
                  nf.copulaTerm(getPredicate(), otherTerm.getCopula(), otherTerm.getPredicate()))
                  .truthValue(otherTruthValue.computeAnalogy(truthValue)).build()));
        }
      }
    } else
    {
      if (getSubject().equals(otherTerm.getPredicate()))
      {
        if (!getPredicate().equals(otherTerm.getSubject()))
        {
          /* Strong syllogism rule {(M<->P),(S<->M)} |- (S<->P) res */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_RESEMBLANCE,
              nf.judgment(
                  nf.copulaTerm(getPredicate(), otherTerm.getCopula(), otherTerm.getSubject()))
                  .truthValue(truthValue.computeResemblance(otherTruthValue)).build()));
        }
      }
    }
  }
}
