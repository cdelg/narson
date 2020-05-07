package org.narson.inference.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.narson.api.inference.BasicInferenciable;
import org.narson.api.inference.InferenceBB;
import org.narson.api.inference.InferenceBB.Type;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.Constant;
import org.narson.api.narsese.CopulaTerm;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.Term;

class RuleMachine
{
  private final Narsese narsese;
  private final NarseseFactory nf;
  private final List<ConditionalRule> rules = new ArrayList<>();

  public RuleMachine(Narsese narsese)
  {
    this.narsese = narsese;
    nf = narsese.getNarseseFactory();
  }

  public List<InferenceBB> computeInferences(RuleContext context)
  {
    final List<InferenceBB> result = new ArrayList<>();
    for (final ConditionalRule rule : rules)
    {
      if (context.match(rule.pattern1, rule.pattern2))
      {
        rule.truthFunction.accept(context);
        result.add(new DefaultInference(rule.type,
            new BasicInferenciable(nf.judgment(rule.statementFunction.apply(context))
                .truthValue(context.getDerivedFrequency(), context.getDerivedConfidence())
                .build())));
      }
    }
    return result;
  }

  public void addRule(String judgment, String judgmentResult, InferenceBB.Type type, boolean inv)
  {
    addRule(judgment, null, judgmentResult, type, inv);
  }

  public void addRule(String judgment1, String judgment2, String judgmentResult,
      InferenceBB.Type type, boolean inv)
  {
    final Term pattern1 = readJudgment(judgment1).getStatement();
    final Term pattern2 = judgment2 != null ? readJudgment(judgment2).getStatement() : null;
    final Term resultPattern = readJudgment(judgmentResult).getStatement();

    final Map<String, Integer> indexes = findIndexes(pattern1, pattern2, resultPattern);
    rules.add(new ConditionalRule(type, pattern1, pattern2, decideTruthFunction(type, inv),
        c -> instanciate(c, resultPattern, indexes)));
  }

  private Map<String, Integer> findIndexes(Term pattern1, Term pattern2, Term resultPattern)
  {
    final Map<String, Integer> result = new HashMap<>();
    final RuleContext context = pattern2 != null
        ? new RuleContext(new BasicInferenciable(nf.judgment(pattern1).build()),
            new BasicInferenciable(nf.judgment(pattern2).build()), 1.0)
        : new RuleContext(new BasicInferenciable(nf.judgment(pattern1).build()), 1.0);

    context.match(pattern1, pattern2);

    getContants(resultPattern)
        .forEach(c -> result.put(c.getName(), findTermIndex(context, c.getName())));

    return result;
  }

  private int findTermIndex(RuleContext context, String name)
  {
    boolean stop = false;
    int i = -1;
    while (!stop)
    {
      i++;
      final Term t = context.getTermAt(i);
      if (t.getValueType() == ValueType.CONSTANT)
      {
        if (t.asConstant().getName().equals(name))
        {
          stop = true;
        }
      }
    }
    return i;
  }

  private List<Constant> getContants(Term pattern)
  {
    final List<Constant> result = new ArrayList<>();
    switch (pattern.getValueType())
    {
      case COMPOUND_TERM:
        pattern.asCompoundTerm().getTerms().forEach(t -> result.addAll(getContants(t)));
        break;
      case CONSTANT:
        result.add(pattern.asConstant());
        break;
      case COPULA_TERM:
        result.addAll(getContants(pattern.asCopulaTerm().getSubject()));
        result.addAll(getContants(pattern.asCopulaTerm().getPredicate()));
        break;
      default:
    }
    return result;
  }

  private Term instanciate(RuleContext context, Term pattern, Map<String, Integer> indexes)
  {
    switch (pattern.getValueType())
    {
      case COMPOUND_TERM:
        return instanciate(context, pattern.asCompoundTerm(), indexes);
      case CONSTANT:
        return context.getTermAt(indexes.get(pattern.asConstant().getName()));
      case COPULA_TERM:
        return instanciate(context, pattern.asCopulaTerm(), indexes);
      default:
        throw new IllegalStateException("Bug: Invalid inference.");
    }
  }

  private Term instanciate(RuleContext context, CompoundTerm pattern, Map<String, Integer> indexes)
  {
    if (pattern.getPlaceHolderPosition() > 0)
    {
      return nf.compoundTerm(pattern.getConnector())
          .of(pattern.getTerms().stream().map(p -> instanciate(context, p, indexes))
              .collect(Collectors.toList()))
          .setPlaceHolderPosition(pattern.getPlaceHolderPosition()).build();
    } else
    {
      return nf.compoundTerm(pattern.getConnector()).of(pattern.getTerms().stream()
          .map(p -> instanciate(context, p, indexes)).collect(Collectors.toList())).build();
    }
  }

  private Term instanciate(RuleContext context, CopulaTerm pattern, Map<String, Integer> indexes)
  {
    return nf.copulaTerm(instanciate(context, pattern.getSubject(), indexes), pattern.getCopula(),
        instanciate(context, pattern.getPredicate(), indexes));
  }

  private Consumer<RuleContext> decideTruthFunction(Type type, boolean inv)
  {
    switch (type)
    {
      case SYLLOGISTIC_ABDUCTION:
        return inv ? RuleContext::computeAbductionInv : RuleContext::computeAbduction;
      case SYLLOGISTIC_ANALOGY:
        return inv ? RuleContext::computeAnalogyInv : RuleContext::computeAnalogy;
      case SYLLOGISTIC_COMPARISON:
        return inv ? RuleContext::computeComparisonInv : RuleContext::computeComparison;
      case SYLLOGISTIC_DEDUCTION:
        return inv ? RuleContext::computeDeductionInv : RuleContext::computeDeduction;
      case SYLLOGISTIC_EXEMPLIFICATION:
        return inv ? RuleContext::computeExemplificationInv : RuleContext::computeExemplification;
      case SYLLOGISTIC_INDUCTION:
        return inv ? RuleContext::computeInductionInv : RuleContext::computeInduction;
      case SYLLOGISTIC_RESEMBLANCE:
        return RuleContext::computeResemblance;
      case IMMEDIATE_CONVERSION:
        return RuleContext::computeConversion;
      case IMMEDIATE_CONTRAPOSITION:
        return RuleContext::computeContraposition;
      case IMMEDIATE_NEGATION:
        return RuleContext::computeNegation;
      case COMPOSITIONAL_DIFFERENCE:
        return inv ? RuleContext::computeDifferenceInv : RuleContext::computeDifference;
      case COMPOSITIONAL_UNION:
        return RuleContext::computeUnion;
      case COMPOSITIONAL_INTERSECTION:
        return RuleContext::computeIntersection;
      case CONDITIONAL_COMPOSITIONAL_COMPARISON:
        return inv ? RuleContext::computeComparisonInv : RuleContext::computeComparison;
      case CONDITIONAL_COMPOSITIONAL_INDUCTION:
        return inv ? RuleContext::computeInductionInv : RuleContext::computeInduction;
      case CONDITIONAL_COMPOSITIONAL_INTERSECTION:
        return RuleContext::computeIntersection;
      case CONDITIONAL_COMPOSITIONAL_UNION:
        return RuleContext::computeUnion;
      case CONDITIONAL_SYLLOGISTIC_ABDUCTION:
        return inv ? RuleContext::computeAbductionInv : RuleContext::computeAbduction;
      case CONDITIONAL_SYLLOGISTIC_ANALOGY:
        return inv ? RuleContext::computeAnalogyInv : RuleContext::computeAnalogy;
      case CONDITIONAL_SYLLOGISTIC_DEDUCTION:
        return inv ? RuleContext::computeDeductionInv : RuleContext::computeDeduction;
      case CONDITIONAL_SYLLOGISTIC_INDUCTION:
        return inv ? RuleContext::computeInductionInv : RuleContext::computeInduction;
      default:
        return RuleContext::computeIdentity;
    }
  }

  private Judgment readJudgment(String j)
  {
    return narsese.read(j).toSentence().get().asJudgment();
  }

  private class ConditionalRule
  {
    public InferenceBB.Type type;
    public final Term pattern1;
    public final Term pattern2;
    public Consumer<RuleContext> truthFunction;
    public Function<RuleContext, Term> statementFunction;

    public ConditionalRule(InferenceBB.Type type, Term pattern1, Term pattern2,
        Consumer<RuleContext> truthFunction, Function<RuleContext, Term> statementFunction)
    {
      this.type = type;
      this.pattern1 = pattern1;
      this.pattern2 = pattern2;
      this.truthFunction = truthFunction;
      this.statementFunction = statementFunction;
    }
  }
}
