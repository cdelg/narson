package org.narson.narsese.provider.rules;

import org.narson.api.narsese.Narsese;
import org.narson.narsese.provider.rules.RuleMachine.RuleMachineBuilder;

final public class RuleMachines
{
  public static RuleMachine createMachine(Narsese narsese)
  {
    final RuleMachineBuilder b = RuleMachine.createBuilder(narsese);

    /*
     * First order strong syllogistic rules (must have different evidential base)
     */
    b.addRule("M-->P", "S-->M", "S-->P", "S!=P", "syllo", "ded");
    b.addRule("P-->M", "M-->S", "P-->S", "S!=P", "syllo", "ded", true);
    b.addRule("M-->P", "S<->M", "S-->P", "S!=P", "syllo", "ana");
    b.addRule("P-->M", "S<->M", "P-->S", "S!=P", "syllo", "ana");
    b.addRule("M<->P", "S-->M", "S-->P", "S!=P", "syllo", "ana", true);
    b.addRule("M<->P", "M-->S", "P-->S", "S!=P", "syllo", "ana", true);
    b.addRule("M<->P", "S<->M", "S<->P", "S!=P", "syllo", "res");

    /*
     * First order weak syllogistic rules (must have different evidential base)
     */
    b.addRule("M-->P", "M-->S", "S-->P", "S!=P", "syllo", "ind");
    b.addRule("M-->P", "M-->S", "P-->S", "S!=P", "syllo", "ind", true);
    b.addRule("P-->M", "S-->M", "S-->P", "S!=P", "syllo", "abd");
    b.addRule("P-->M", "S-->M", "P-->S", "S!=P", "syllo", "abd", true);
    b.addRule("P-->M", "M-->S", "S-->P", "S!=P", "syllo", "exe");
    b.addRule("M-->P", "S-->M", "P-->S", "S!=P", "syllo", "exe", true);
    b.addRule("M-->P", "M-->S", "S<->P", "S!=P", "syllo", "com");
    b.addRule("P-->M", "S-->M", "S<->P", "S!=P", "syllo", "com", true);

    /*
     * Higher order strong syllogistic rules (must have different evidential base)
     */
    b.addRule("M==>P", "S==>M", "S==>P", "S!=P", "syllo", "ded");
    b.addRule("P==>M", "M==>S", "P==>S", "S!=P", "syllo", "ded", true);
    b.addRule("M==>P", "S<=>M", "S==>P", "S!=P", "syllo", "ana");
    b.addRule("P==>M", "S<=>M", "P==>S", "S!=P", "syllo", "ana");
    b.addRule("M<=>P", "S==>M", "S==>P", "S!=P", "syllo", "ana", true);
    b.addRule("M<=>P", "M==>S", "P==>S", "S!=P", "syllo", "ana", true);
    b.addRule("M<=>P", "S<=>M", "S<=>P", "S!=P", "syllo", "res");

    /*
     * Higher order weak syllogistic rules (must have different evidential base)
     */
    b.addRule("M==>P", "M==>S", "S==>P", "S!=P", "syllo", "ind");
    b.addRule("M==>P", "M==>S", "P==>S", "S!=P", "syllo", "ind", true);
    b.addRule("P==>M", "S==>M", "S==>P", "S!=P", "syllo", "abd");
    b.addRule("P==>M", "S==>M", "P==>S", "S!=P", "syllo", "abd", true);
    b.addRule("P==>M", "M==>S", "S==>P", "S!=P", "syllo", "exe");
    b.addRule("M==>P", "S==>M", "P==>S", "S!=P", "syllo", "exe", true);
    b.addRule("M==>P", "M==>S", "S<=>P", "S!=P", "syllo", "com");
    b.addRule("P==>M", "S==>M", "S<=>P", "S!=P", "syllo", "com", true);

    /*
     * First order strong compositional rules (must have different evidential base)
     */
    b.addRule("T1-->M", "T2-->M", "(T1 | T2)-->M", "T1<>T2", "compo", "int");
    b.addRule("T1-->M", "T2-->M", "(T1 & T2)-->M", "T1<>T2", "compo", "uni");
    b.addRule("T1-->M", "T2-->M", "(T1 ~ T2)-->M", "T1<>T2", "compo", "dif");
    b.addRule("T1-->M", "T2-->M", "(T2 ~ T1)-->M", "T1<>T2", "compo", "dif", true);
    b.addRule("M-->T1", "M-->T2", "M-->(T1 & T2)", "T1<>T2", "compo", "int");
    b.addRule("M-->T1", "M-->T2", "M-->(T1 | T2)", "T1<>T2", "compo", "uni");
    b.addRule("M-->T1", "M-->T2", "M-->(T1 - T2)", "T1<>T2", "compo", "dif");
    b.addRule("M-->T1", "M-->T2", "M-->(T2 - T1)", "T1<>T2", "compo", "dif", true);

    /*
     * Higher order strong compositional rules (must have different evidential base)
     */
    b.addRule("T1==>M", "T2==>M", "(T1 || T2)==>M", "T1<>T2", "compo", "int");
    b.addRule("T1==>M", "T2==>M", "(T1 && T2)==>M", "T1<>T2", "compo", "uni");
    b.addRule("M==>T1", "M==>T2", "M==>(T1 && T2)", "T1<>T2", "compo", "int");
    b.addRule("M==>T1", "M==>T2", "M==>(T1 || T2)", "T1<>T2", "compo", "uni");

    /*
     * Higher order conditional strong syllogistic rules (must have different evidential base)
     */
    b.addRule("M==>P", "M", "P", null, "condsyllo", "ded");
    b.addRule("(S && M)==>P", "M", "S==>P", "S!=P", "condsyllo", "ded");
    b.addRule("(Q && M)==>P", "S==>M", "(Q && S)==>P", null, "condsyllo", "ded");
    b.addRule("M<=>P", "M", "P", null, "condsyllo", "ana", true);

    /*
     * Higher order conditional weak syllogistic rules (must have different evidential base)
     */
    b.addRule("P==>M", "M", "P", "", "condsyllo", "abd");
    b.addRule("(C && P)==>M", "C==>M", "P", "", "condsyllo", "abd");
    b.addRule("(C && P)==>M", "(C && S)==>M", "S==>P", "", "condsyllo", "abd");
    b.addRule("M==>P", "S", "(S && M)==>P", "", "condsyllo", "ind");
    b.addRule("(Q && M)==>P", "M==>S", "(Q && S)==>P", "", "condsyllo", "ind");

    /*
     * Higher order conditional strong compositional rules (must have different evidential base and
     * additional condition???? TODO)
     */
    // b.addRule("P.", "S.", "(P && S).",
    // InferenceBB.Type.CONDITIONAL_COMPOSITIONAL_INTERSECTION, false);
    // b.addRule("P.", "S.", "(P || S).",
    // InferenceBB.Type.CONDITIONAL_COMPOSITIONAL_UNION,
    // false);

    /*
     * Higher order conditional weak compositional rules (must have different evidential base and
     * additional condition???? TODO)
     */
    // b.addRule("P.", "S.", "(S==>P).",
    // InferenceBB.Type.CONDITIONAL_COMPOSITIONAL_INDUCTION, false);
    // b.addRule("P.", "S.", "(S<=>P).",
    // InferenceBB.Type.CONDITIONAL_COMPOSITIONAL_COMPARISON, false);

    /*
     * First order weak immediate rules
     */
    b.addRule("P-->S", "S-->P", "S!=P", "conv");

    /*
     * Higher order strong immediate rules
     */
    b.addRule("P", "!P", null, "neg");

    /*
     * Higher order weak immediate rules
     */
    b.addRule("P==>S", "S==>P", "S!=P", "conv");
    b.addRule("S==>P", "(!P)==>(!S)", "S!=P", "cont");

    return b.build();
  }
}
