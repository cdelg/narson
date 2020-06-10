package org.narson.narsese.provider.model;

import java.util.List;
import org.narson.api.narsese.CompoundConnector;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.SetConnector;
import org.narson.api.narsese.SetTerm;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class InheritanceCopulaTerm extends AbstractAsymmetricCopulaTerm
{
  public InheritanceCopulaTerm(Narsese narsese, Term subject, Term predicate, Tense tense)
  {
    super(narsese, subject, Copula.INHERITANCE, predicate, tense);
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
    if (getPredicate().getValueType() == ValueType.SET_TERM)
    {
      final SetTerm term = getPredicate().asSetTerm();
      if (term.getConnector() == SetConnector.EXTENSIONAL_SET && term.getTerms().size() == 1)
      {
        if (!getSubject().equals(getPredicate()))
        {
          /* Structural equivalence rule S-->{P} <===> S<->{P} */
          inferences.add(new DefaultInference(Inference.Type.STRUCTURAL_EQUIVALENCE,
              nf.judgment(nf.copula(getSubject(), Copula.SIMILARITY, getPredicate()))
                  .truthValue(truthValue).build()));
        }
      }
    }

    if (getSubject().getValueType() == ValueType.SET_TERM)
    {
      final SetTerm term = getPredicate().asSetTerm();
      if (term.getConnector() == SetConnector.INTENSIONAL_SET && term.getTerms().size() == 1)
      {
        if (!getSubject().equals(getPredicate()))
        {
          /* Structural equivalence rule [S]-->P <===> [S]<->P */
          inferences.add(new DefaultInference(Inference.Type.STRUCTURAL_EQUIVALENCE,
              nf.judgment(nf.copula(getSubject(), Copula.SIMILARITY, getPredicate()))
                  .truthValue(truthValue).build()));
        }
      }
    }


  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    super.computeInferences(truthValue, otherTerm, otherTruthValue, evidentialHorizon, inferences);
    computeCompositional(truthValue, otherTerm, otherTruthValue, inferences);
  }

  private void computeCompositional(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    if (!otherTerm.getCopula().isSymmetric())
    {
      if (getPredicate().equals(otherTerm.getPredicate()))
      {
        if (composable(getSubject(), otherTerm.getSubject()))
        {
          /* Strong compositional rule {(T1-->M),(T2-->M)} |- ((T1 ~ T2)-->M) diff */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_DIFFERENCE, nf
              .judgment(nf.copula(
                  nf.compound(CompoundConnector.INTENSIONAL_DIFFERENCE)
                      .of(getSubject(), otherTerm.getSubject()).build(),
                  otherTerm.getCopula(), getPredicate()))
              .truthValue(truthValue.computeDifference(otherTruthValue)).build()));

          /* Strong compositional rule {(T1-->M),(T2-->M)} |- ((T2 ~ T1)-->M) diff' */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_DIFFERENCE, nf
              .judgment(nf.copula(
                  nf.compound(CompoundConnector.INTENSIONAL_DIFFERENCE)
                      .of(otherTerm.getSubject(), getSubject()).build(),
                  otherTerm.getCopula(), getPredicate()))
              .truthValue(otherTruthValue.computeDifference(truthValue)).build()));
        }
      } else if (getSubject().equals(otherTerm.getSubject()))
      {
        if (composable(getPredicate(), otherTerm.getPredicate()))
        {
          /* Strong compositional rule {(M-->T1),(M-->T2)} |- (M-->(T1 - T2)) diff */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_DIFFERENCE, nf
              .judgment(nf.copula(getSubject(), otherTerm.getCopula(),
                  nf.compound(CompoundConnector.EXTENSIONAL_DIFFERENCE)
                      .of(getPredicate(), otherTerm.getPredicate()).build()))
              .truthValue(truthValue.computeDifference(otherTruthValue)).build()));

          /* Strong compositional rule {(M-->T1),(M-->T2)} |- (M-->(T2 - T1)) diff' */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_DIFFERENCE, nf
              .judgment(nf.copula(getSubject(), otherTerm.getCopula(),
                  nf.compound(CompoundConnector.EXTENSIONAL_DIFFERENCE)
                      .of(otherTerm.getPredicate(), getPredicate()).build()))
              .truthValue(otherTruthValue.computeDifference(truthValue)).build()));
        }
      }
    }
  }
}
