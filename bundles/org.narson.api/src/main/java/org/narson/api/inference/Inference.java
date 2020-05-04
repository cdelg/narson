package org.narson.api.inference;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Inference
{
  enum Type
  {
    CHOICE(false),
    REVISION(false),
    SYLLOGISTIC_DEDUCTION(true),
    SYLLOGISTIC_ANALOGY(true),
    SYLLOGISTIC_RESEMBLANCE(true),
    SYLLOGISTIC_ABDUCTION(false),
    SYLLOGISTIC_INDUCTION(false),
    SYLLOGISTIC_EXEMPLIFICATION(false),
    SYLLOGISTIC_COMPARISON(false),
    IMMEDIATE_CONVERSION(false),
    IMMEDIATE_CONTRAPOSITION(false),
    IMMEDIATE_NEGATION(true),
    CONDITIONAL_SYLLOGISTIC_DEDUCTION(true),
    CONDITIONAL_SYLLOGISTIC_ANALOGY(true),
    CONDITIONAL_SYLLOGISTIC_ABDUCTION(false),
    CONDITIONAL_SYLLOGISTIC_INDUCTION(false),
    CONDITIONAL_COMPOSITIONAL_INDUCTION(false),
    CONDITIONAL_COMPOSITIONAL_COMPARISON(false),
    CONDITIONAL_COMPOSITIONAL_UNION(true),
    CONDITIONAL_COMPOSITIONAL_INTERSECTION(true),
    EQUIVALENCE_RULE(true),
    IMPLICATION_RULE(true),
    COMPOSITIONAL_UNION(true),
    COMPOSITIONAL_INTERSECTION(true),
    COMPOSITIONAL_DIFFERENCE(true);

    private final boolean strong;

    Type(boolean strong)
    {
      this.strong = strong;
    }

    public boolean isStrong()
    {
      return strong;
    }
  }

  Type getType();

  Inferenciable getInferenciable();
}
