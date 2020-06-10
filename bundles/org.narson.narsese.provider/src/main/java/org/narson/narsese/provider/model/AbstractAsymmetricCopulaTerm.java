package org.narson.narsese.provider.model;

import java.util.List;
import org.narson.api.narsese.CompoundConnector;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

abstract class AbstractAsymmetricCopulaTerm extends AbstractCopulaTerm
{
  public AbstractAsymmetricCopulaTerm(Narsese narsese, Term subject, Copula copula, Term predicate,
      Tense tense)
  {
    super(narsese, subject, copula, predicate, tense);
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, double evidentialHorizon,
      List<Inference> inferences)
  {
    super.computeInferences(truthValue, evidentialHorizon, inferences);

    computeImmediate(truthValue, inferences, evidentialHorizon);
    computeStructural(truthValue, inferences);
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    computeStrongSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
    computeWeakSyllogism(truthValue, otherTerm, otherTruthValue, evidentialHorizon, inferences);
    computeCompositional(truthValue, otherTerm, otherTruthValue, inferences);
  }

  private void computeStructural(TruthValueImpl truthValue, List<Inference> inferences)
  {
    // TODO
  }

  private void computeImmediate(TruthValueImpl truthValue, List<Inference> inferences,
      double evidentialHorizon)
  {
    /* Immediate Conversion rule {(P-->S)} |- (S-->P) */
    inferences.add(new DefaultInference(Inference.Type.IMMEDIATE_CONVERSION,
        nf.judgment(nf.copula(getPredicate(), getCopula(), getSubject()))
            .truthValue(truthValue.computeConversion(evidentialHorizon)).build()));
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
          /* Strong compositional rule {(T1-->M),(T2-->M)} |- ((T1 | T2)-->M) uni */
          inferences
              .add(new DefaultInference(Inference.Type.COMPOSITIONAL_UNION, nf
                  .judgment(nf.copula(
                      nf.compound(CompoundConnector.INTENSIONAL_INTERSECTION)
                          .of(getSubject(), otherTerm.getSubject()).build(),
                      otherTerm.getCopula(), getPredicate()))
                  .truthValue(truthValue.computeUnion(otherTruthValue)).build()));

          /* Strong compositional rule {(T1-->M),(T2-->M)} |- ((T1 & T2)-->M) int */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_INTERSECTION, nf
              .judgment(nf.copula(
                  nf.compound(CompoundConnector.EXTENSIONAL_INTERSECTION)
                      .of(getSubject(), otherTerm.getSubject()).build(),
                  otherTerm.getCopula(), getPredicate()))
              .truthValue(truthValue.computeIntersection(otherTruthValue)).build()));
        }
      } else if (getSubject().equals(otherTerm.getSubject()))
      {
        if (composable(getPredicate(), otherTerm.getPredicate()))
        {
          /* Strong compositional rule {(M-->T1),(M-->T2)} |- (M-->(T1 | T2)) uni */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_UNION, nf
              .judgment(nf.copula(getSubject(), otherTerm.getCopula(),
                  nf.compound(CompoundConnector.INTENSIONAL_INTERSECTION)
                      .of(getPredicate(), otherTerm.getPredicate()).build()))
              .truthValue(truthValue.computeUnion(otherTruthValue)).build()));

          /* Strong compositional rule {(M-->T1),(M-->T2)} |- (M-->(T1 & T2)) int */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_INTERSECTION, nf
              .judgment(nf.copula(getSubject(), otherTerm.getCopula(),
                  nf.compound(CompoundConnector.EXTENSIONAL_INTERSECTION)
                      .of(getPredicate(), otherTerm.getPredicate()).build()))
              .truthValue(truthValue.computeIntersection(otherTruthValue)).build()));
        }
      }
    }
  }

  private void computeWeakSyllogism(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    if (!otherTerm.getCopula().isSymmetric())
    {
      if (getSubject().equals(otherTerm.getSubject()))
      {
        if (!getPredicate().equals(otherTerm.getPredicate()))
        {
          /* Weak syllogism rule {(M-->P),(M-->S)} |- (S-->P) ind */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_INDUCTION, nf
              .judgment(nf.copula(otherTerm.getPredicate(), otherTerm.getCopula(), getPredicate()))
              .truthValue(truthValue.computeInduction(otherTruthValue, evidentialHorizon))
              .build()));

          /* Weak syllogism rule {(M-->P),(M-->S)} |- (P-->S) ind' */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_INDUCTION, nf
              .judgment(nf.copula(getPredicate(), otherTerm.getCopula(), otherTerm.getPredicate()))
              .truthValue(otherTruthValue.computeInduction(truthValue, evidentialHorizon))
              .build()));

          /* Weak syllogism rule {(M-->P),(M-->S)} |- (S<->P) comp */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_COMPARISON,
              nf.judgment(nf.copula(otherTerm.getPredicate(),
                  getCopula().isFirstOrder() ? Copula.SIMILARITY : Copula.EQUIVALENCE,
                  getPredicate()))
                  .truthValue(truthValue.computeComparison(otherTruthValue, evidentialHorizon))
                  .build()));
        }
      } else if (getPredicate().equals(otherTerm.getPredicate()))
      {
        /* Weak syllogism rule {(P-->M),(S-->M)} |- (S-->P) abd */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ABDUCTION,
            nf.judgment(nf.copula(otherTerm.getSubject(), otherTerm.getCopula(), getSubject()))
                .truthValue(truthValue.computeAbduction(otherTruthValue, evidentialHorizon))
                .build()));

        /* Weak syllogism rule {(P-->M),(S-->M)} |- (P-->S) abd' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ABDUCTION,
            nf.judgment(nf.copula(getSubject(), otherTerm.getCopula(), otherTerm.getSubject()))
                .truthValue(otherTruthValue.computeAbduction(truthValue, evidentialHorizon))
                .build()));

        /* Weak syllogism rule {(P-->M),(S-->M)} |- (S<->P) comp' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_COMPARISON, nf
            .judgment(nf.copula(otherTerm.getSubject(),
                getCopula().isFirstOrder() ? Copula.SIMILARITY : Copula.EQUIVALENCE, getSubject()))
            .truthValue(otherTruthValue.computeComparison(truthValue, evidentialHorizon)).build()));
      }
      if (getSubject().equals(otherTerm.getPredicate()))
      {
        if (!getPredicate().equals(otherTerm.getSubject()))
        {
          /* Weak syllogism rule {(M-->P),(S-->M)} |- (P-->S) exe' */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_EXEMPLIFICATION,
              nf.judgment(nf.copula(getPredicate(), otherTerm.getCopula(), otherTerm.getSubject()))
                  .truthValue(otherTruthValue.computeExemplification(truthValue, evidentialHorizon))
                  .build()));
        }
      } else if (getPredicate().equals(otherTerm.getSubject()))
      {
        /* Weak syllogism rule {(P-->M),(M-->S)} |- (S-->P) exe */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_EXEMPLIFICATION,
            nf.judgment(nf.copula(otherTerm.getPredicate(), otherTerm.getCopula(), getSubject()))
                .truthValue(truthValue.computeExemplification(otherTruthValue, evidentialHorizon))
                .build()));
      }
    }
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
          /* Strong syllogism rule {(M-->P),(S-->M)} |- (S-->P) ded */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_DEDUCTION,
              nf.judgment(nf.copula(getPredicate(), otherTerm.getCopula(), otherTerm.getSubject()))
                  .truthValue(truthValue.computeDeduction(otherTruthValue)).build()));
        }
      } else if (getPredicate().equals(otherTerm.getSubject()))
      {
        /* Strong syllogism rule {(P-->M),(M-->S)} |- (P-->S) ded' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_DEDUCTION,
            nf.judgment(nf.copula(getSubject(), otherTerm.getCopula(), otherTerm.getPredicate()))
                .truthValue(otherTruthValue.computeDeduction(truthValue)).build()));
      }
    } else
    {
      if (getSubject().equals(otherTerm.getPredicate()))
      {
        if (!getPredicate().equals(otherTerm.getSubject()))
        {
          /* Strong syllogism rule {(M-->P),(S<->M)} |- (S-->P) ana */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_RESEMBLANCE,
              nf.judgment(nf.copula(getSubject(), getCopula(), otherTerm.getPredicate()))
                  .truthValue(truthValue.computeAnalogy(otherTruthValue)).build()));
        }
      }
      if (getPredicate().equals(otherTerm.getPredicate()))
      {
        if (!getSubject().equals(otherTerm.getSubject()))
        {
          /* Strong syllogism rule {(P-->M),(S<->M)} |- (P-->S) ana */
          inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ANALOGY,
              nf.judgment(nf.copula(getPredicate(), getCopula(), otherTerm.getSubject()))
                  .truthValue(truthValue.computeAnalogy(otherTruthValue)).build()));
        }
      }
    }
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
