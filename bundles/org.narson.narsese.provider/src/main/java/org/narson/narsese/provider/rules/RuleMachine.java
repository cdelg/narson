package org.narson.narsese.provider.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.narson.api.narsese.Inference;
import org.narson.api.narsese.InferenceType;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;
import org.narson.narsese.provider.model.BiTruthValueFunction;
import org.narson.narsese.provider.model.TruthValueFunction;

final public class RuleMachine
{
  private final NarseseFactory nf;
  private final List<BiRule> biRules;
  private final List<Rule> rules;

  private RuleMachine(NarseseFactory nf, List<BiRule> biRules, List<Rule> rules)
  {
    this.nf = nf;
    this.biRules = biRules;
    this.rules = rules;
  }

  public List<Inference> run(Term j1, Term j2, BiTruthValueFunction truthProvider)
  {
    return biRules.stream().flatMap(rule -> {
      final List<Term> matches = rule.pattern.matcher(j1, j2).matches();
      if (matches.size() > 0)
      {
        final TruthValue truth = rule.truthFunc.apply(truthProvider);
        return matches.stream().map(
            term -> new DefaultInference(rule.type, nf.judgment(term).truthValue(truth).build()));
      } else
      {
        return Stream.empty();
      }
    }).collect(Collectors.toList());
  }

  public List<Inference> run(Term j1, TruthValueFunction truthProvider)
  {
    return rules.stream().flatMap(rule -> {
      final List<Term> matches = rule.pattern.matcher(j1).matches();
      if (matches.size() > 0)
      {
        final TruthValue truth = rule.truthFunc.apply(truthProvider);
        return matches.stream().map(
            term -> new DefaultInference(rule.type, nf.judgment(term).truthValue(truth).build()));
      } else
      {
        return Stream.empty();
      }
    }).collect(Collectors.toList());
  }

  public static RuleMachineBuilder createBuilder(Narsese narsese)
  {
    return new RuleMachineBuilder(narsese);
  }

  static private class Rule
  {
    final public InferenceType type;
    final public TermPattern pattern;
    final public Function<TruthValueFunction, TruthValue> truthFunc;

    public Rule(InferenceType type, TermPattern pattern,
        Function<TruthValueFunction, TruthValue> truthFunc)
    {
      super();
      this.type = type;
      this.pattern = pattern;
      this.truthFunc = truthFunc;
    }
  }

  static private class BiRule
  {
    final public InferenceType type;
    final public BiTermPattern pattern;
    final public Function<BiTruthValueFunction, TruthValue> truthFunc;

    public BiRule(InferenceType type, BiTermPattern pattern,
        Function<BiTruthValueFunction, TruthValue> truthFunc)
    {
      super();
      this.type = type;
      this.pattern = pattern;
      this.truthFunc = truthFunc;
    }
  }

  static public class RuleMachineBuilder
  {
    private final NarseseFactory nf;
    private final TermPatterns termPatterns;
    private final List<BiRule> biRules = new ArrayList<>();
    private final List<Rule> rules = new ArrayList<>();

    private RuleMachineBuilder(Narsese narsese)
    {
      nf = narsese.getNarseseFactory();
      termPatterns = new TermPatterns(narsese);
    }

    public RuleMachineBuilder addRule(String j1, String j2, String j, String condition, String type,
        String truthFunc)
    {
      return addRule(j1, j2, j, condition, type, truthFunc, false);
    }

    public RuleMachineBuilder addRule(String j1, String j2, String j, String condition, String type,
        String truthFunc, boolean inv)
    {
      biRules.add(new BiRule(decideInferenceType(type, truthFunc), termPatterns.compile(j1, j2, j),
          decideBiTruthFunction(truthFunc)));
      return this;
    }

    public RuleMachineBuilder addRule(String j1, String j, String condition, String truthFunc)
    {
      rules.add(new Rule(decideInferenceType(truthFunc), termPatterns.compile(j1, j),
          decideTruthFunction(truthFunc)));
      return this;
    }

    public RuleMachine build()
    {
      return new RuleMachine(nf, new ArrayList<>(biRules), new ArrayList<>(rules));
    }

    private InferenceType decideInferenceType(String type, String truthFunc)
    {
      switch (type)
      {
        case "syllo":
          switch (truthFunc)
          {
            case "ded":
              return InferenceType.SYLLOGISTIC_DEDUCTION;
            case "ana":
              return InferenceType.SYLLOGISTIC_ANALOGY;
            case "res":
              return InferenceType.SYLLOGISTIC_RESEMBLANCE;
            case "ind":
              return InferenceType.SYLLOGISTIC_INDUCTION;
            case "abd":
              return InferenceType.SYLLOGISTIC_ABDUCTION;
            case "exe":
              return InferenceType.SYLLOGISTIC_EXEMPLIFICATION;
            case "com":
              return InferenceType.SYLLOGISTIC_COMPARISON;
            default:
              throw new IllegalStateException("Invalid inference truth function " + truthFunc);
          }
        case "compo":
          switch (truthFunc)
          {
            case "int":
              return InferenceType.COMPOSITIONAL_INTERSECTION;
            case "uni":
              return InferenceType.COMPOSITIONAL_UNION;
            case "dif":
              return InferenceType.COMPOSITIONAL_DIFFERENCE;
            default:
              throw new IllegalStateException("Invalid inference truth function " + truthFunc);
          }
        case "condsyllo":
          switch (truthFunc)
          {
            case "ded":
              return InferenceType.CONDITIONAL_SYLLOGISTIC_DEDUCTION;
            case "ana":
              return InferenceType.CONDITIONAL_SYLLOGISTIC_ANALOGY;
            case "ind":
              return InferenceType.CONDITIONAL_SYLLOGISTIC_INDUCTION;
            case "abd":
              return InferenceType.CONDITIONAL_SYLLOGISTIC_ABDUCTION;
            default:
              throw new IllegalStateException("Invalid inference truth function " + truthFunc);
          }
        default:
          throw new IllegalStateException("Invalid inference rule type " + type);
      }
    }

    private Function<TruthValueFunction, TruthValue> decideTruthFunction(String truthFunc)
    {
      switch (truthFunc)
      {
        case "neg":
          return TruthValueFunction::computeNegation;
        case "conv":
          return TruthValueFunction::computeConversion;
        case "cont":
          return TruthValueFunction::computeContraposition;
        default:
          throw new IllegalStateException("Invalid truth function " + truthFunc);
      }
    }


    private InferenceType decideInferenceType(String truthFunc)
    {
      switch (truthFunc)
      {
        case "neg":
          return InferenceType.IMMEDIATE_NEGATION;
        case "conv":
          return InferenceType.IMMEDIATE_CONVERSION;
        case "cont":
          return InferenceType.IMMEDIATE_CONTRAPOSITION;
        default:
          throw new IllegalStateException("Invalid truth function " + truthFunc);
      }
    }

    private Function<BiTruthValueFunction, TruthValue> decideBiTruthFunction(String truthFunc)
    {
      switch (truthFunc)
      {
        case "abd":
          return BiTruthValueFunction::computeAbduction;
        case "ana":
          return BiTruthValueFunction::computeAnalogy;
        case "com":
          return BiTruthValueFunction::computeComparison;
        case "ded":
          return BiTruthValueFunction::computeDeduction;
        case "dif":
          return BiTruthValueFunction::computeDifference;
        case "exe":
          return BiTruthValueFunction::computeExemplification;
        case "ind":
          return BiTruthValueFunction::computeInduction;
        case "int":
          return BiTruthValueFunction::computeIntersection;
        case "res":
          return BiTruthValueFunction::computeResemblance;
        case "uni":
          return BiTruthValueFunction::computeUnion;
        default:
          throw new IllegalStateException("Invalid truth function " + truthFunc);
      }
    }
  }
}
