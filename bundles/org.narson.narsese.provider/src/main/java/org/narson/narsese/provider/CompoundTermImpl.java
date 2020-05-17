package org.narson.narsese.provider;

import java.util.List;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Connector;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.Term;

final class CompoundTermImpl extends AbstractTerm implements CompoundTerm
{
  private final Connector connector;
  private final List<Term> unmodifiableTerms;
  private final int placeHolderPosition;

  public CompoundTermImpl(Narsese narsese, Connector connector, List<Term> unmodifiableTerms,
      int placeHolderPosition)
  {
    super(narsese, ValueType.COMPOUND_TERM);
    this.connector = connector;
    this.unmodifiableTerms = unmodifiableTerms;
    this.placeHolderPosition = placeHolderPosition;
  }

  @Override
  public Connector getConnector()
  {
    return connector;
  }

  @Override
  public List<Term> getTerms()
  {
    return unmodifiableTerms;
  }

  @Override
  public int getPlaceHolderPosition()
  {
    return placeHolderPosition;
  }

  @Override
  protected int computeSyntacticComplexity()
  {
    return getTerms().stream().mapToInt(Term::getSyntacticComplexity).sum() + 1;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + connector.hashCode();
    result = prime * result + placeHolderPosition;
    result = prime * result + unmodifiableTerms.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (!super.equals(obj))
    {
      return false;
    }
    if (!(obj instanceof CompoundTerm))
    {
      return false;
    }
    final CompoundTerm other = (CompoundTerm) obj;

    if (connector != other.getConnector())
    {
      return false;
    }

    if (placeHolderPosition != other.getPlaceHolderPosition())
    {
      return false;
    }

    if (unmodifiableTerms.equals(other.getTerms()))
    {
      return true;
    } else
    {
      return false;
    }
  }

  @Override
  public int compareTo(Term o)
  {
    final int compareType = getValueType().compareTo(o.getValueType());
    if (compareType < 0)
    {
      return -1;
    }
    if (compareType > 0)
    {
      return 1;
    } else
    {
      return compare(o.asCompoundTerm());
    }
  }

  private int compare(CompoundTerm o)
  {
    final int compareConnector = getConnector().compareTo(o.getConnector());
    if (compareConnector < 0)
    {
      return -1;
    }
    if (compareConnector > 0)
    {
      return 1;
    } else
    {
      final int comparePlaceholder = getPlaceHolderPosition() - o.getPlaceHolderPosition();
      if (comparePlaceholder < 0)
      {
        return -1;
      }
      if (comparePlaceholder > 0)
      {
        return 1;
      } else
      {
        return ListHelper.compareTerms(getTerms(), o.getTerms());
      }
    }
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
    if (isConjunctionOfCopulas())
    {
      final CopulaTerm copula1 = unmodifiableTerms.get(0).asCopulaTerm();
      final CopulaTerm copula2 = unmodifiableTerms.get(1).asCopulaTerm();

      if (copula1.getCopula() == Copula.INHERITANCE && copula2.getCopula() == Copula.INHERITANCE)
      {
        if (copula1.getSubject().equals(copula2.getPredicate())
            && copula1.getPredicate().equals(copula2.getSubject()))
        {
          if (!copula1.getSubject().equals(copula2.getSubject()))
          {
            /* Structural equivalence rule (S<->P) <===> (S-->P) & (P-->S) */
            inferences.add(new DefaultInference(Inference.Type.STRUCTURAL_EQUIVALENCE,
                nf.judgment(
                    nf.copulaTerm(copula1.getSubject(), Copula.SIMILARITY, copula2.getSubject()))
                    .truthValue(truthValue).build()));
          }
        }
      }
    }
  }

  private boolean isConjunctionOfCopulas()
  {
    if (connector == Connector.CONJUNCTION && unmodifiableTerms.size() == 2)
    {
      return unmodifiableTerms.get(0).getValueType() == ValueType.COPULA_TERM
          && unmodifiableTerms.get(1).getValueType() == ValueType.COPULA_TERM;
    } else
    {
      return false;
    }
  }
}


