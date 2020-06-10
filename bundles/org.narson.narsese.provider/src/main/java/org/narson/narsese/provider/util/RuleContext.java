package org.narson.narsese.provider.util;

import java.util.ArrayList;
import java.util.List;
import org.narson.api.inference.Inferenciable;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.ConstantTerm;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.Term;

class RuleContext
{
  private final double evidentialHorizon;

  public final Term statment1;
  public final Term statment2;

  private final double f1;
  private final double c1;
  private double f2;
  private double c2;

  private double derivedF;
  private double derivedC;

  private final List<Term> terms = new ArrayList<>();
  private Term equalTerm1 = null;
  private Term equalTerm2 = null;
  private Term diffTerm = null;


  public RuleContext(Inferenciable inferenciable1, Inferenciable inferenciable2,
      double evidentialHorizon)
  {
    this.evidentialHorizon = evidentialHorizon;

    statment1 = inferenciable1.getJudgment().getStatement();
    f1 = inferenciable1.getJudgment().getTruthValue().getFrequency();
    c1 = inferenciable1.getJudgment().getTruthValue().getConfidence();

    if (inferenciable2 != null)
    {
      statment2 = inferenciable2.getJudgment().getStatement();
      f2 = inferenciable2.getJudgment().getTruthValue().getFrequency();
      c2 = inferenciable2.getJudgment().getTruthValue().getConfidence();
    } else
    {
      statment2 = null;
    }
  }

  public RuleContext(Inferenciable inferenciable, double evidentialHorizon)
  {
    this(inferenciable, null, evidentialHorizon);
  }

  public double getDerivedFrequency()
  {
    return derivedF;
  }

  public double getDerivedConfidence()
  {
    return derivedC;
  }

  public Term getTermAt(int index)
  {
    return terms.get(index);
  }

  public boolean match(Term pattern1, Term pattern2)
  {
    terms.clear();
    equalTerm1 = null;
    equalTerm2 = null;
    diffTerm = null;

    if (statment2 == null)
    {
      if (pattern2 == null)
      {
        return termMatch(statment1, pattern1);
      } else
      {
        return false;
      }
    } else
    {
      if (pattern2 != null)
      {
        return termMatch(statment1, pattern1) && termMatch(statment2, pattern2);
      } else
      {
        return false;
      }
    }
  }

  private boolean termMatch(Term term, Term pattern)
  {
    switch (pattern.getValueType())
    {
      case COMPOUND_TERM:
        if (term.getValueType() == ValueType.COMPOUND_TERM)
        {
          return termMatch(term.asCompoundTerm(), pattern.asCompoundTerm());
        } else
        {
          return false;
        }
      case CONSTANT_TERM:
        terms.add(term);
        final ConstantTerm p = pattern.asConstantTerm();
        if (p.getName().equals("M"))
        {
          if (equalTerm1 != null)
          {
            return equalTerm1.equals(term);
          } else
          {
            equalTerm1 = term;
            return true;
          }
        } else if (p.getName().equals("C"))
        {
          if (equalTerm2 != null)
          {
            return equalTerm2.equals(term);
          } else
          {
            equalTerm2 = term;
            return true;
          }
        } else if (p.getName().equals("T1") || p.getName().equals("T2"))
        {
          if (diffTerm != null)
          {
            if (!diffTerm.equals(term))
            {
              boolean ok = true;

              if (diffTerm.getValueType() == ValueType.COMPOUND_TERM)
              {
                ok = !diffTerm.asCompoundTerm().getTerms().contains(term);
              }

              if (ok && term.getValueType() == ValueType.COMPOUND_TERM)
              {
                ok = !term.asCompoundTerm().getTerms().contains(diffTerm);
              }

              return ok;
            } else
            {
              return false;
            }
          } else
          {
            diffTerm = term;
            return true;
          }
        } else
        {
          return true;
        }
      case COPULA_TERM:
        if (term.getValueType() == ValueType.COPULA_TERM)
        {
          return termMatch(term.asCopulaTerm(), pattern.asCopulaTerm());
        } else
        {
          return false;
        }
      default:
        return false;
    }

  }

  private boolean termMatch(CopulaTerm term, CopulaTerm pattern)
  {
    return term.getCopula() == pattern.getCopula()
        && termMatch(term.getSubject(), pattern.getSubject())
        && termMatch(term.getPredicate(), pattern.getPredicate());
  }

  private boolean termMatch(CompoundTerm term, CompoundTerm pattern)
  {
    return term.getConnector() == pattern.getConnector()
        && term.getTerms().size() == pattern.getTerms().size();
  }



  /*
   * Truth Value Functions
   */

  public void computeDeduction()
  {
    derivedF = f1 * f2;
    derivedC = derivedF * c1 * c2;
  }

  public void computeDeductionInv()
  {
    derivedF = f2 * f1;
    derivedC = derivedF * c2 * c1;
  }

  public void computeAnalogy()
  {
    derivedF = f1 * f2;
    derivedC = f2 * c1 * c2;
  }

  public void computeAnalogyInv()
  {
    derivedF = f2 * f1;
    derivedC = f1 * c2 * c1;
  }

  public void computeResemblance()
  {
    derivedF = f1 * f2;
    derivedC = (f1 + f2 - f1 * f2) * c1 * c2;
  }

  public void computeAbduction()
  {
    final double pw = f1 * f2 * c1 * c2;
    final double w = f1 * c1 * c2;

    derivedF = pw / w;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeAbductionInv()
  {
    final double pw = f2 * f1 * c2 * c1;
    final double w = f2 * c2 * c1;

    derivedF = pw / w;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeInduction()
  {
    final double pw = f1 * f2 * c1 * c2;
    final double w = f2 * c1 * c2;

    derivedF = pw / w;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeInductionInv()
  {
    final double pw = f2 * f1 * c2 * c1;
    final double w = f1 * c2 * c1;

    derivedF = pw / w;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeExemplification()
  {
    final double w = f1 * f2 * c1 * c2;

    derivedF = 1.0;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeExemplificationInv()
  {
    final double w = f2 * f1 * c2 * c1;

    derivedF = 1.0;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeComparison()
  {
    final double pw = f1 * f2 * c1 * c2;
    final double w = (f1 + f2 - f1 * f2) * c1 * c2;

    derivedF = pw / w;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeComparisonInv()
  {
    final double pw = f2 * f1 * c2 * c1;
    final double w = (f2 + f1 - f2 * f1) * c2 * c1;

    derivedF = pw / w;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeConversion()
  {
    final double w = f1 * c1;

    derivedF = 1.0;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeContraposition()
  {
    final double w = (1 - f1) * c1;

    derivedF = 0.0;
    derivedC = w / (w + evidentialHorizon);
  }

  public void computeNegation()
  {
    derivedF = 1 - f1;
    derivedC = c1;
  }

  public void computeIdentity()
  {
    derivedF = f1;
    derivedC = c1;
  }

  public void computeIntersection()
  {
    derivedF = f1 * f2;
    derivedC = c1 * c2;
  }

  public void computeUnion()
  {
    derivedF = 1 - (1 - f1) * (1 - f2);
    derivedC = c1 * c2;
  }

  public void computeDifference()
  {
    derivedF = f1 * (1 - f2);
    derivedC = c1 * c2;
  }

  public void computeDifferenceInv()
  {
    derivedF = f2 * (1 - f1);
    derivedC = c2 * c1;
  }
}
