package org.narson.inference.provider;

import static org.narson.tools.PredChecker.checkArgument;
import static org.narson.tools.PredChecker.checkNotNull;
import java.util.List;
import org.narson.api.inference.BasicInferenciable;
import org.narson.api.inference.Inference;
import org.narson.api.inference.Inference.Type;
import org.narson.api.inference.InferenceEngine;
import org.narson.api.inference.Inferenciable;
import org.narson.api.narsese.Judgment;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Term;
import org.narson.api.narsese.TruthValue;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
final public class DefaultInferenceEngine implements InferenceEngine
{
  private final NarseseFactory nf;
  private final RuleMachine ruleMachine;

  @Activate
  public DefaultInferenceEngine(@Reference Narsese narsese)
  {
    checkNotNull(narsese, "narsese");
    nf = narsese.getNarseseFactory();
    ruleMachine = new RuleMachine(narsese);

    initRules();
  }

  private void initRules()
  {
    /*
     * First order strong syllogistic rules (must have different evidential base)
     */
    ruleMachine.addRule("(M-->P).", "(S-->M).", "(S-->P).", Inference.Type.SYLLOGISTIC_DEDUCTION,
        false);
    ruleMachine.addRule("(P-->M).", "(M-->S).", "(P-->S).", Inference.Type.SYLLOGISTIC_DEDUCTION,
        true);
    ruleMachine.addRule("(M-->P).", "(S<->M).", "(S-->P).", Inference.Type.SYLLOGISTIC_ANALOGY,
        false);
    ruleMachine.addRule("(P-->M).", "(S<->M).", "(P-->S).", Inference.Type.SYLLOGISTIC_ANALOGY,
        false);
    ruleMachine.addRule("(M<->P).", "(S-->M).", "(S-->P).", Inference.Type.SYLLOGISTIC_ANALOGY,
        true);
    ruleMachine.addRule("(M<->P).", "(M-->S).", "(P-->S).", Inference.Type.SYLLOGISTIC_ANALOGY,
        true);
    ruleMachine.addRule("(M<->P).", "(S<->M).", "(S<->P).", Inference.Type.SYLLOGISTIC_RESEMBLANCE,
        false);

    /*
     * First order weak syllogistic rules (must have different evidential base)
     */
    ruleMachine.addRule("(M-->P).", "(M-->S).", "(S-->P).", Inference.Type.SYLLOGISTIC_INDUCTION,
        false);
    ruleMachine.addRule("(M-->P).", "(M-->S).", "(P-->S).", Inference.Type.SYLLOGISTIC_INDUCTION,
        true);
    ruleMachine.addRule("(P-->M).", "(S-->M).", "(S-->P).", Inference.Type.SYLLOGISTIC_ABDUCTION,
        false);
    ruleMachine.addRule("(P-->M).", "(S-->M).", "(P-->S).", Inference.Type.SYLLOGISTIC_ABDUCTION,
        true);
    ruleMachine.addRule("(M-->P).", "(S-->M).", "(P-->S).",
        Inference.Type.SYLLOGISTIC_EXEMPLIFICATION, true);
    ruleMachine.addRule("(P-->M).", "(M-->S).", "(S-->P).",
        Inference.Type.SYLLOGISTIC_EXEMPLIFICATION, false);
    ruleMachine.addRule("(M-->P).", "(M-->S).", "(S<->P).", Inference.Type.SYLLOGISTIC_COMPARISON,
        false);
    ruleMachine.addRule("(P-->M).", "(S-->M).", "(S<->P).", Inference.Type.SYLLOGISTIC_COMPARISON,
        true);

    /*
     * Higher order strong syllogistic rules (must have different evidential base)
     */
    ruleMachine.addRule("(M==>P).", "(S==>M).", "(S==>P).", Inference.Type.SYLLOGISTIC_DEDUCTION,
        false);
    ruleMachine.addRule("(P==>M).", "(M==>S).", "(P==>S).", Inference.Type.SYLLOGISTIC_DEDUCTION,
        true);
    ruleMachine.addRule("(P==>M).", "(S<=>M).", "(P==>S).", Inference.Type.SYLLOGISTIC_ANALOGY,
        false);
    ruleMachine.addRule("(M<=>P).", "(S==>M).", "(S==>P).", Inference.Type.SYLLOGISTIC_ANALOGY,
        true);
    ruleMachine.addRule("(M<=>P).", "(M==>S).", "(P==>S).", Inference.Type.SYLLOGISTIC_ANALOGY,
        true);
    ruleMachine.addRule("(M==>P).", "(S<=>M).", "(S==>P).", Inference.Type.SYLLOGISTIC_ANALOGY,
        false);
    ruleMachine.addRule("(M<=>P).", "(S<=>M).", "(S<=>P).", Inference.Type.SYLLOGISTIC_RESEMBLANCE,
        false);

    /*
     * Higher order weak syllogistic rules (must have different evidential base)
     */

    ruleMachine.addRule("(M==>P).", "(M==>S).", "(S==>P).", Inference.Type.SYLLOGISTIC_INDUCTION,
        false);
    ruleMachine.addRule("(M==>P).", "(M==>S).", "(P==>S).", Inference.Type.SYLLOGISTIC_INDUCTION,
        true);
    ruleMachine.addRule("(P==>M).", "(S==>M).", "(S==>P).", Inference.Type.SYLLOGISTIC_ABDUCTION,
        false);
    ruleMachine.addRule("(P==>M).", "(S==>M).", "(P==>S).", Inference.Type.SYLLOGISTIC_ABDUCTION,
        true);
    ruleMachine.addRule("(M==>P).", "(S==>M).", "(P==>S).",
        Inference.Type.SYLLOGISTIC_EXEMPLIFICATION, true);
    ruleMachine.addRule("(P==>M).", "(M==>S).", "(S==>P).",
        Inference.Type.SYLLOGISTIC_EXEMPLIFICATION, false);
    ruleMachine.addRule("(M==>P).", "(M==>S).", "(S<=>P).", Inference.Type.SYLLOGISTIC_COMPARISON,
        false);
    ruleMachine.addRule("(P==>M).", "(S==>M).", "(S<=>P).", Inference.Type.SYLLOGISTIC_COMPARISON,
        true);

    /*
     * First order strong compositional rules (must have different evidential base)
     */
    ruleMachine.addRule("(T1-->M).", "(T2-->M).", "((T1 | T2)-->M).",
        Inference.Type.COMPOSITIONAL_INTERSECTION, false);
    ruleMachine.addRule("(T1-->M).", "(T2-->M).", "((T1 & T2)-->M).",
        Inference.Type.COMPOSITIONAL_UNION, false);
    ruleMachine.addRule("(T1-->M).", "(T2-->M).", "((T1 ~ T2)-->M).",
        Inference.Type.COMPOSITIONAL_DIFFERENCE, false);
    ruleMachine.addRule("(T1-->M).", "(T2-->M).", "((T2 ~ T1)-->M).",
        Inference.Type.COMPOSITIONAL_DIFFERENCE, true);
    ruleMachine.addRule("(M-->T1).", "(M-->T2).", "(M-->(T1 & T2)).",
        Inference.Type.COMPOSITIONAL_INTERSECTION, false);
    ruleMachine.addRule("(M-->T1).", "(M-->T2).", "(M-->(T1 | T2)).",
        Inference.Type.COMPOSITIONAL_UNION, false);
    ruleMachine.addRule("(M-->T1).", "(M-->T2).", "(M-->(T1 - T2)).",
        Inference.Type.COMPOSITIONAL_DIFFERENCE, false);
    ruleMachine.addRule("(M-->T1).", "(M-->T2).", "(M-->(T2 - T1)).",
        Inference.Type.COMPOSITIONAL_DIFFERENCE, true);

    /*
     * Higher order strong compositional rules (must have different evidential base)
     */
    ruleMachine.addRule("(T1==>M).", "(T2==>M).", "((T1 || T2)==>M).",
        Inference.Type.COMPOSITIONAL_INTERSECTION, false);
    ruleMachine.addRule("(T1==>M).", "(T2==>M).", "((T1 && T2)==>M).",
        Inference.Type.COMPOSITIONAL_UNION, false);
    ruleMachine.addRule("(M==>T1).", "(M==>T2).", "(M==>(T1 && T2)).",
        Inference.Type.COMPOSITIONAL_INTERSECTION, false);
    ruleMachine.addRule("(M==>T1).", "(M==>T2).", "(M==>(T1 || T2)).",
        Inference.Type.COMPOSITIONAL_UNION, false);

    /*
     * Higher order conditional strong syllogistic rules (must have different evidential base)
     */
    ruleMachine.addRule("(M==>P).", "M.", "P.", Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION,
        false);
    ruleMachine.addRule("((S && M)==>P).", "M.", "(S==>P).",
        Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION, false);
    ruleMachine.addRule("((Q && M)==>P).", "(S==>M).", "((Q && S)==>P).",
        Inference.Type.CONDITIONAL_SYLLOGISTIC_DEDUCTION, false);
    ruleMachine.addRule("(M<=>P).", "M.", "P.", Inference.Type.CONDITIONAL_SYLLOGISTIC_ANALOGY,
        true);

    /*
     * Higher order conditional weak syllogistic rules (must have different evidential base)
     */
    ruleMachine.addRule("(P==>M).", "M.", "P.", Inference.Type.CONDITIONAL_SYLLOGISTIC_ABDUCTION,
        false);
    ruleMachine.addRule("((C && P)==>M).", "(C==>M).", "P.",
        Inference.Type.CONDITIONAL_SYLLOGISTIC_ABDUCTION, false);
    ruleMachine.addRule("((C && P)==>M).", "((C && S)==>M).", "(S==>P).",
        Inference.Type.CONDITIONAL_SYLLOGISTIC_ABDUCTION, false);
    ruleMachine.addRule("(M==>P).", "S.", "((S && M)==>P).",
        Inference.Type.CONDITIONAL_SYLLOGISTIC_INDUCTION, false);
    ruleMachine.addRule("((Q && M)==>P).", "(M==>S).", "((Q && S)==>P).",
        Inference.Type.CONDITIONAL_SYLLOGISTIC_INDUCTION, false);

    /*
     * Higher order conditional strong compositional rules (must have different evidential base and
     * additional condition???? TODO)
     */
    ruleMachine.addRule("P.", "S.", "(P && S).",
        Inference.Type.CONDITIONAL_COMPOSITIONAL_INTERSECTION, false);
    ruleMachine.addRule("P.", "S.", "(P || S).", Inference.Type.CONDITIONAL_COMPOSITIONAL_UNION,
        false);

    /*
     * Higher order conditional weak compositional rules (must have different evidential base and
     * additional condition???? TODO)
     */
    ruleMachine.addRule("P.", "S.", "(S==>P).", Inference.Type.CONDITIONAL_COMPOSITIONAL_INDUCTION,
        false);
    ruleMachine.addRule("P.", "S.", "(S<=>P).", Inference.Type.CONDITIONAL_COMPOSITIONAL_COMPARISON,
        false);

    /*
     * First order weak immediate rules
     */
    ruleMachine.addRule("(P-->S).", "(S-->P).", Inference.Type.IMMEDIATE_CONVERSION, false);

    /*
     * Higher order strong immediate rules
     */
    ruleMachine.addRule("P.", "(!P).", Inference.Type.IMMEDIATE_NEGATION, false);

    /*
     * Higher order weak immediate rules
     */
    ruleMachine.addRule("(P==>S).", "(S==>P).", Inference.Type.IMMEDIATE_CONVERSION, false);
    ruleMachine.addRule("(S==>P).", "((!P)==>(!S)).", Inference.Type.IMMEDIATE_CONTRAPOSITION,
        false);

    /*
     * To Verify (structural?)
     */
    ruleMachine.addRule("(S<->P).", "(S-->P).", Inference.Type.IMPLICATION_RULE, false);

    ruleMachine.addRule("(S<->P).", "(P<->S).", Inference.Type.EQUIVALENCE_RULE, false);

    ruleMachine.addRule("(S-->{P}).", "(S<->{P}).", Inference.Type.EQUIVALENCE_RULE, false);
    ruleMachine.addRule("(S<->{P}).", "(S-->{P}).", Inference.Type.EQUIVALENCE_RULE, false);
    ruleMachine.addRule("([S]-->P).", "([S]<->P).", Inference.Type.EQUIVALENCE_RULE, false);
    ruleMachine.addRule("([S]<->P).", "([S]-->P).", Inference.Type.EQUIVALENCE_RULE, false);
  }

  @Override
  public Inference choose(Inferenciable inferenciable, Inferenciable otherInferenciable,
      double razorParameter)
  {
    checkNotNull(inferenciable, "inferenciable");
    checkNotNull(otherInferenciable, "otherInferenciable");

    final Judgment result;
    final Judgment j1 = inferenciable.getJudgment();
    final Judgment j2 = otherInferenciable.getJudgment();

    if (j1.getStatement().equals(j2.getStatement()))
    {
      if (j1.getTruthValue().getConfidence() > j2.getTruthValue().getConfidence())
      {
        result = j1;
      } else
      {
        result = j2;
      }
    } else
    {
      if (j1.getTruthValue().getExpectation()
          * j1.getStatement().getSyntacticSimplicity(razorParameter) > j2.getTruthValue()
              .getExpectation() * j2.getStatement().getSyntacticSimplicity(razorParameter))
      {
        result = j1;
      } else
      {
        result = j2;
      }
    }

    return new DefaultInference(Type.CHOICE, new BasicInferenciable(result));
  }

  @Override
  public Inference revise(Inferenciable inferenciable, Inferenciable otherInferenciable)
  {
    checkNotNull(inferenciable, "inferenciable");
    checkNotNull(otherInferenciable, "otherInferenciable");

    final Judgment j1 = inferenciable.getJudgment();
    final Judgment j2 = otherInferenciable.getJudgment();
    final Term statement = j1.getStatement();

    checkArgument(statement.equals(j2.getStatement()), "statements are not equals.");

    final TruthValue t1 = j1.getTruthValue();
    final TruthValue t2 = j2.getTruthValue();

    final double f1 = t1.getFrequency();
    final double c1 = t1.getConfidence();
    final double f2 = t2.getFrequency();
    final double c2 = t2.getConfidence();

    final double c1c2 = c1 * (1 - c2);
    final double c2c1 = c2 * (1 - c1);
    final double d = c1c2 + c2c1;

    final double f = (f1 * c1c2 + f2 * c2c1) / d;
    final double c = d / (c1c2 + c2c1 + (1 - c1) * (1 - c2));

    return new DefaultInference(Type.REVISION,
        new BasicInferenciable(nf.judgment(statement).truthValue(f, c).build()));
  }

  @Override
  public List<Inference> reason(Inferenciable inferenciable, double evidentialHorizon)
  {
    checkNotNull(inferenciable, "inferenciable");

    return ruleMachine.computeInferences(new RuleContext(inferenciable, evidentialHorizon));
  }

  @Override
  public List<Inference> reason(Inferenciable inferenciable, Inferenciable otherInferenciable,
      double evidentialHorizon)
  {
    checkNotNull(inferenciable, "inferenciable");
    checkNotNull(otherInferenciable, "otherInferenciable");
    checkArgument(evidentialHorizon > 0, "evidentialHorizon <= 0");

    return ruleMachine
        .computeInferences(new RuleContext(inferenciable, otherInferenciable, evidentialHorizon));
  }
}
