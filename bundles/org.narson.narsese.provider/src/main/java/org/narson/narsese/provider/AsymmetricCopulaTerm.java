package org.narson.narsese.provider;

import java.util.List;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

final class AsymmetricCopulaTerm extends AbstractCopulaTerm
{
  public AsymmetricCopulaTerm(Narsese narsese, Term subject, boolean firstOrder, Term predicate,
      Tense tense)
  {
    super(narsese, subject, firstOrder ? Copula.INHERITANCE : Copula.IMPLICATION, predicate, tense);
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
  public void computeInferences(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    super.computeInferences(truthValue, otherTerm, otherTruthValue, evidentialHorizon, inferences);

    if (!getCopula().isFirstOrder())
    {
      computeStrongConditionalSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
      computeWeakConditionalSyllogism(truthValue, otherTerm, otherTruthValue, evidentialHorizon,
          inferences);
    }
  }

  @Override
  public void computeInferences(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, double evidentialHorizon, List<Inference> inferences)
  {
    computeStrongSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
    computeWeakSyllogism(truthValue, otherTerm, otherTruthValue, evidentialHorizon, inferences);
    computeCompositional(truthValue, otherTerm, otherTruthValue, inferences);
    if (!getCopula().isFirstOrder())
    {
      computeStrongConditionalSyllogism(truthValue, otherTerm, otherTruthValue, inferences);
      computeWeakConditionalSyllogism(truthValue, otherTerm, otherTruthValue, evidentialHorizon,
          inferences);
    }
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
        nf.judgment(nf.copulaTerm(getPredicate(), getCopula(), getSubject()))
            .truthValue(truthValue.computeConversion(evidentialHorizon)).build()));

    if (!getCopula().isFirstOrder())
    {
      /* Immediate Contraposition rule {(S==P)} |- ((!P)==>(!S)) */
      inferences.add(new DefaultInference(Inference.Type.IMMEDIATE_CONTRAPOSITION,
          nf.judgment(nf.copulaTerm(nf.compoundTerm(Connector.NEGATION).of(getPredicate()).build(),
              getCopula(), nf.compoundTerm(Connector.NEGATION).of(getSubject()).build()))
              .truthValue(truthValue.computeContraposition(evidentialHorizon)).build()));
    }
  }

  private void computeStrongConditionalSyllogism(TruthValueImpl truthValue, Term otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    /* Strong conditional syllogism rule {(M==>P),M} |- P ded */
    inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION,
        nf.judgment(getPredicate()).truthValue(truthValue.computeDeduction(otherTruthValue))
            .build()));

    if (getSubject().getValueType() == ValueType.COMPOUND_TERM)
    {
      final CompoundTerm compound = getSubject().asCompoundTerm();
      if (compound.getConnector() == Connector.CONJUNCTION && compound.getTerms().size() == 2)
      {
        if (compound.getTerms().get(1).equals(otherTerm))
        {
          /* Strong conditional syllogism rule {(M==>P),M} |- P ded */
          inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION,
              nf.judgment(nf.copulaTerm(compound.getTerms().get(0), getCopula(), getPredicate()))
                  .truthValue(truthValue.computeDeduction(otherTruthValue)).build()));
        }
      }
    }
  }

  private void computeStrongConditionalSyllogism(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    if (getSubject().getValueType() == ValueType.COMPOUND_TERM)
    {
      final CompoundTerm compound = getSubject().asCompoundTerm();
      if (compound.getConnector() == Connector.CONJUNCTION && compound.getTerms().size() == 2)
      {
        if (compound.getTerms().get(1).equals(otherTerm.getPredicate()))
        {
          /* Strong conditional syllogism rule {((Q && M)==>P),(S==>M)} |- ((Q && S)==>P) ded */
          inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION, nf
              .judgment(nf.copulaTerm(
                  nf.compoundTerm(Connector.CONJUNCTION)
                      .of(compound.getTerms().get(0), otherTerm.getSubject()).build(),
                  getCopula(), getPredicate()))
              .truthValue(truthValue.computeDeduction(otherTruthValue)).build()));
        }
      }
    }
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
    /* Weak conditional syllogism rule {(M==>P),S} |- ((S && M)==>P) ind */
    inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_INDUCTION,
        nf.judgment(nf.copulaTerm(
            nf.compoundTerm(Connector.CONJUNCTION).of(otherTerm, getSubject()).build(), getCopula(),
            getPredicate()))
            .truthValue(truthValue.computeInduction(otherTruthValue, evidentialHorizon)).build()));

    if (getSubject().getValueType() == ValueType.COMPOUND_TERM)
    {
      final CompoundTerm compound = getSubject().asCompoundTerm();
      if (compound.getConnector() == Connector.CONJUNCTION && compound.getTerms().size() == 2)
      {
        if (compound.getTerms().get(0).equals(otherTerm.getSubject())
            && getPredicate().equals(otherTerm.getPredicate()))
        {
          /* Weak conditional syllogism rule {((C && P)==>M),(C==>M)} |- P abd */
          inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_ABDUCTION,
              nf.judgment(compound.getTerms().get(1))
                  .truthValue(truthValue.computeAbduction(otherTruthValue, evidentialHorizon))
                  .build()));
        }

        if (compound.getTerms().get(1).equals(otherTerm.getSubject()))
        {
          /* Weak conditional syllogism rule {((Q && M)==>P),(M==>S)} |- ((Q && S)==>P) ind */
          inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_INDUCTION, nf
              .judgment(nf.copulaTerm(
                  nf.compoundTerm(Connector.CONJUNCTION)
                      .of(compound.getTerms().get(0), otherTerm.getPredicate()).build(),
                  getCopula(), getPredicate()))
              .truthValue(truthValue.computeInduction(otherTruthValue, evidentialHorizon))
              .build()));
        }

        if (otherTerm.getSubject().getValueType() == ValueType.COMPOUND_TERM)
        {
          final CompoundTerm otherCompound = otherTerm.getSubject().asCompoundTerm();

          if (compound.getTerms().get(0).equals(otherCompound.getTerms().get(0))
              && getPredicate().equals(otherTerm.getPredicate()))
          {
            /* Weak conditional syllogism rule {((C && P)==>M),((C && S)==>M)} |- (S==>P) abd */
            inferences.add(new DefaultInference(Inference.Type.CONDITIONAL_SYLLOGISTIC_ABDUCTION,
                nf.judgment(nf.copulaTerm(otherCompound.getTerms().get(1), getCopula(),
                    compound.getTerms().get(1)))
                    .truthValue(truthValue.computeAbduction(otherTruthValue, evidentialHorizon))
                    .build()));
          }
        }
      }
    }
  }

  private void computeCompositional(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    if (!otherTerm.getCopula().isSymmetric())
    {

      if (getPredicate().equals(otherTerm.getPredicate()))
      {
        if (diff(getSubject(), otherTerm.getSubject()))
        {
          /* Strong compositional rule {(T1-->M),(T2-->M)} |- ((T1 | T2)-->M) uni */
          inferences
              .add(new DefaultInference(Inference.Type.COMPOSITIONAL_UNION, nf
                  .judgment(nf.copulaTerm(
                      nf.compoundTerm(Connector.INTENSIONAL_INTERSECTION)
                          .of(getSubject(), otherTerm.getSubject()).build(),
                      otherTerm.getCopula(), getPredicate()))
                  .truthValue(truthValue.computeUnion(otherTruthValue)).build()));

          /* Strong compositional rule {(T1-->M),(T2-->M)} |- ((T1 & T2)-->M) int */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_INTERSECTION, nf
              .judgment(nf.copulaTerm(
                  nf.compoundTerm(Connector.EXTENSIONAL_INTERSECTION)
                      .of(getSubject(), otherTerm.getSubject()).build(),
                  otherTerm.getCopula(), getPredicate()))
              .truthValue(truthValue.computeIntersection(otherTruthValue)).build()));

          if (getCopula().isFirstOrder())
          {
            /* Strong compositional rule {(T1-->M),(T2-->M)} |- ((T1 ~ T2)-->M) diff */
            inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_DIFFERENCE, nf
                .judgment(nf.copulaTerm(
                    nf.compoundTerm(Connector.INTENSIONAL_DIFFERENCE)
                        .of(getSubject(), otherTerm.getSubject()).build(),
                    otherTerm.getCopula(), getPredicate()))
                .truthValue(truthValue.computeDifference(otherTruthValue)).build()));

            /* Strong compositional rule {(T1-->M),(T2-->M)} |- ((T2 ~ T1)-->M) diff' */
            inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_DIFFERENCE, nf
                .judgment(nf.copulaTerm(
                    nf.compoundTerm(Connector.INTENSIONAL_DIFFERENCE)
                        .of(otherTerm.getSubject(), getSubject()).build(),
                    otherTerm.getCopula(), getPredicate()))
                .truthValue(otherTruthValue.computeDifference(truthValue)).build()));
          }
        }
      } else if (getSubject().equals(otherTerm.getSubject()))
      {
        if (diff(getPredicate(), otherTerm.getPredicate()))
        {
          /* Strong compositional rule {(M-->T1),(M-->T2)} |- (M-->(T1 | T2)) uni */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_UNION, nf
              .judgment(nf.copulaTerm(getSubject(), otherTerm.getCopula(),
                  nf.compoundTerm(Connector.INTENSIONAL_INTERSECTION)
                      .of(getPredicate(), otherTerm.getPredicate()).build()))
              .truthValue(truthValue.computeUnion(otherTruthValue)).build()));

          /* Strong compositional rule {(M-->T1),(M-->T2)} |- (M-->(T1 & T2)) int */
          inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_INTERSECTION, nf
              .judgment(nf.copulaTerm(getSubject(), otherTerm.getCopula(),
                  nf.compoundTerm(Connector.EXTENSIONAL_INTERSECTION)
                      .of(getPredicate(), otherTerm.getPredicate()).build()))
              .truthValue(truthValue.computeIntersection(otherTruthValue)).build()));

          if (getCopula().isFirstOrder())
          {
            /* Strong compositional rule {(M-->T1),(M-->T2)} |- (M-->(T1 - T2)) diff */
            inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_DIFFERENCE, nf
                .judgment(nf.copulaTerm(getSubject(), otherTerm.getCopula(),
                    nf.compoundTerm(Connector.EXTENSIONAL_DIFFERENCE)
                        .of(getPredicate(), otherTerm.getPredicate()).build()))
                .truthValue(truthValue.computeDifference(otherTruthValue)).build()));

            /* Strong compositional rule {(M-->T1),(M-->T2)} |- (M-->(T2 - T1)) diff' */
            inferences.add(new DefaultInference(Inference.Type.COMPOSITIONAL_DIFFERENCE, nf
                .judgment(nf.copulaTerm(getSubject(), otherTerm.getCopula(),
                    nf.compoundTerm(Connector.EXTENSIONAL_DIFFERENCE)
                        .of(otherTerm.getPredicate(), getPredicate()).build()))
                .truthValue(otherTruthValue.computeDifference(truthValue)).build()));
          }
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
        /* Weak syllogism rule {(M-->P),(M-->S)} |- (S-->P) ind */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_INDUCTION, nf
            .judgment(
                nf.copulaTerm(otherTerm.getPredicate(), otherTerm.getCopula(), getPredicate()))
            .truthValue(truthValue.computeInduction(otherTruthValue, evidentialHorizon)).build()));

        /* Weak syllogism rule {(M-->P),(M-->S)} |- (P-->S) ind' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_INDUCTION, nf
            .judgment(
                nf.copulaTerm(getPredicate(), otherTerm.getCopula(), otherTerm.getPredicate()))
            .truthValue(otherTruthValue.computeInduction(truthValue, evidentialHorizon)).build()));

        /* Weak syllogism rule {(M-->P),(M-->S)} |- (S<->P) comp */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_COMPARISON,
            nf.judgment(nf.copulaTerm(otherTerm.getPredicate(),
                getCopula().isFirstOrder() ? Copula.SIMILARITY : Copula.EQUIVALENCE,
                getPredicate()))
                .truthValue(truthValue.computeComparison(otherTruthValue, evidentialHorizon))
                .build()));
      }
      if (getPredicate().equals(otherTerm.getPredicate()))
      {
        /* Weak syllogism rule {(P-->M),(S-->M)} |- (S-->P) abd */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ABDUCTION,
            nf.judgment(nf.copulaTerm(otherTerm.getSubject(), otherTerm.getCopula(), getSubject()))
                .truthValue(truthValue.computeAbduction(otherTruthValue, evidentialHorizon))
                .build()));

        /* Weak syllogism rule {(P-->M),(S-->M)} |- (P-->S) abd' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ABDUCTION,
            nf.judgment(nf.copulaTerm(getSubject(), otherTerm.getCopula(), otherTerm.getSubject()))
                .truthValue(otherTruthValue.computeAbduction(truthValue, evidentialHorizon))
                .build()));

        /* Weak syllogism rule {(P-->M),(S-->M)} |- (S<->P) comp' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_COMPARISON, nf
            .judgment(nf.copulaTerm(otherTerm.getSubject(),
                getCopula().isFirstOrder() ? Copula.SIMILARITY : Copula.EQUIVALENCE, getSubject()))
            .truthValue(otherTruthValue.computeComparison(truthValue, evidentialHorizon)).build()));
      }
      if (getSubject().equals(otherTerm.getPredicate()))
      {
        /* Weak syllogism rule {(M-->P),(S-->M)} |- (P-->S) exe' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_EXEMPLIFICATION, nf
            .judgment(nf.copulaTerm(getPredicate(), otherTerm.getCopula(), otherTerm.getSubject()))
            .truthValue(otherTruthValue.computeExemplification(truthValue, evidentialHorizon))
            .build()));
      }
      if (getPredicate().equals(otherTerm.getSubject()))
      {
        /* Weak syllogism rule {(P-->M),(M-->S)} |- (S-->P) exe */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_EXEMPLIFICATION, nf
            .judgment(nf.copulaTerm(otherTerm.getPredicate(), otherTerm.getCopula(), getSubject()))
            .truthValue(truthValue.computeExemplification(otherTruthValue, evidentialHorizon))
            .build()));
      }
    }
  }

  public void computeStrongSyllogism(TruthValueImpl truthValue, CopulaTerm otherTerm,
      TruthValueImpl otherTruthValue, List<Inference> inferences)
  {
    if (!otherTerm.getCopula().isSymmetric())
    {
      if (getSubject().equals(otherTerm.getPredicate()))
      {
        /* Strong syllogism rule {(M-->P),(S-->M)} |- (S-->P) ded */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_DEDUCTION, nf
            .judgment(nf.copulaTerm(getPredicate(), otherTerm.getCopula(), otherTerm.getSubject()))
            .truthValue(truthValue.computeDeduction(otherTruthValue)).build()));
      }
      if (getPredicate().equals(otherTerm.getSubject()))
      {
        /* Strong syllogism rule {(P-->M),(M-->S)} |- (P-->S) ded' */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_DEDUCTION, nf
            .judgment(nf.copulaTerm(getSubject(), otherTerm.getCopula(), otherTerm.getPredicate()))
            .truthValue(otherTruthValue.computeDeduction(truthValue)).build()));
      }
    } else
    {
      if (getSubject().equals(otherTerm.getPredicate()))
      {
        /* Strong syllogism rule {(M-->P),(S<->M)} |- (S-->P) ana */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_RESEMBLANCE,
            nf.judgment(nf.copulaTerm(getSubject(), getCopula(), otherTerm.getPredicate()))
                .truthValue(truthValue.computeAnalogy(otherTruthValue)).build()));
      }
      if (getPredicate().equals(otherTerm.getPredicate()))
      {
        /* Strong syllogism rule {(P-->M),(S<->M)} |- (P-->S) ana */
        inferences.add(new DefaultInference(Inference.Type.SYLLOGISTIC_ANALOGY,
            nf.judgment(nf.copulaTerm(getPredicate(), getCopula(), otherTerm.getSubject()))
                .truthValue(truthValue.computeAnalogy(otherTruthValue)).build()));
      }
    }
  }

  static private boolean diff(Term t1, Term t2)
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
