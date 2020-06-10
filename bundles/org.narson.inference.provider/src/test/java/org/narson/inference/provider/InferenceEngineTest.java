package org.narson.inference.provider;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.narson.api.inference.BasicInferenciable;
import org.narson.api.narsese.Copula;
import org.narson.api.narsese.NarseseFactory;
import org.narson.narsese.NarseseProvider;

public class InferenceEngineTest
{
  @Test
  public void testValueIsRight()
  {
    final int rep = 1000;
    final int num = 1000;

    final NarseseFactory nf = new NarseseProvider().getNarseseFactory();
    final DefaultInferenceEngine engine = new DefaultInferenceEngine(new NarseseProvider());

    double myO = 0;
    double myN = 0;

    Instant start;
    Instant end;
    for (int r = 0; r < rep; r++)
    {
      start = Instant.now();
      for (int j = 0; j < num; j++)
      {
        engine.reason(
            new BasicInferenciable(
                nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("b")))
                    .build()),
            new BasicInferenciable(
                nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("c")))
                    .build()),
            1);
      }
      end = Instant.now();
      myO += Duration.between(start, end).toMillis();

      start = Instant.now();
      for (int j = 0; j < num; j++)
      {
        nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("b"))).build()
            .reason(
                nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("c")))
                    .build(),
                1);
      }
      end = Instant.now();
      myN += Duration.between(start, end).toMillis();
    }

    for (int r = 0; r < rep; r++)
    {

      start = Instant.now();
      for (int j = 0; j < num; j++)
      {
        nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("b"))).build()
            .reason(
                nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("c")))
                    .build(),
                1);
      }
      end = Instant.now();
      myN += Duration.between(start, end).toMillis();

      start = Instant.now();
      for (int j = 0; j < num; j++)
      {
        engine.reason(
            new BasicInferenciable(
                nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("b")))
                    .build()),
            new BasicInferenciable(
                nf.judgment(nf.copulaTerm(nf.constant("a"), Copula.INHERITANCE, nf.constant("c")))
                    .build()),
            1);
      }
      end = Instant.now();
      myO += Duration.between(start, end).toMillis();

    }
    myO /= (rep * 2);
    myN /= (rep * 2);

    System.out.println(myO);
    System.out.println(myN);
    System.out.println(myO / myN);
  }
}
