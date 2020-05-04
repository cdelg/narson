package org.narson.inference.provider;

import org.junit.jupiter.api.Test;
import org.narson.api.inference.BasicInferenciable;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.NarseseFactory;
import org.narson.narsese.provider.NarseseProvider;

public class InferenceEngineTest
{
  @Test
  public void testValueIsRight()
  {
    final NarseseFactory nf = new NarseseProvider().getNarseseFactory();
    final DefaultInferenceEngine engine = new DefaultInferenceEngine(new NarseseProvider());
    engine
        .reason(
            new BasicInferenciable(
                nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("b")))
                    .build()),
            new BasicInferenciable(
                nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("c")))
                    .build()),
            1)
        .forEach(i -> System.out.println(i.getType() + ": " + i.getInferenciable().getJudgment()));

  }
}
