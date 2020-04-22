package org.narson.inference.provider;

import java.util.function.BiConsumer;
import org.narson.api.narsese.TruthValue;

final class TruthValueFunctions
{
  private final TruthValue t;

  public TruthValueFunctions(TruthValue truthValue)
  {
    t = truthValue;
  }

  public void applyRevision(BiConsumer<Double, Double> builder, TruthValue truthValue)
  {
    builder.accept(
        (t.getFrequency() * t.getConfidence() * (1 - truthValue.getConfidence())
            + truthValue.getFrequency() * truthValue.getConfidence() * (1 - t.getConfidence()))
            / (t.getConfidence() * (1 - truthValue.getConfidence())
                + truthValue.getConfidence() * (1 - t.getConfidence())),
        (t.getConfidence() * (1 - truthValue.getConfidence())
            + truthValue.getConfidence() * (1 - t.getConfidence()))
            / (t.getConfidence() * (1 - truthValue.getConfidence())
                + truthValue.getConfidence() * (1 - t.getConfidence())
                + (1 - t.getConfidence()) * (1 - truthValue.getConfidence())));
  }

  public void applyNegation(BiConsumer<Double, Double> builder)
  {
    builder.accept(1 - t.getFrequency(), t.getConfidence());
  }

  public void applyConversion(BiConsumer<Double, Double> builder, double evidentialHorizon)
  {
    final double w = t.getFrequency() * t.getConfidence();
    builder.accept(1.0, w / (w + evidentialHorizon));
  }

  public void applyContraposition(BiConsumer<Double, Double> builder, double evidentialHorizon)
  {
    final double a = (1 - t.getFrequency()) * t.getConfidence();
    final double w = a / (a + evidentialHorizon);

    builder.accept(0.0, w / (w + evidentialHorizon));
  }

  public void applyDeduction(BiConsumer<Double, Double> builder, TruthValue truthValue)
  {
    builder.accept(t.getFrequency() * truthValue.getFrequency(), t.getFrequency()
        * truthValue.getFrequency() * t.getConfidence() * truthValue.getConfidence());
  }

  public void applyAnalogy(BiConsumer<Double, Double> builder, TruthValue truthValue)
  {
    builder.accept(t.getFrequency() * truthValue.getFrequency(),
        truthValue.getFrequency() * t.getConfidence() * truthValue.getConfidence());
  }

  public void applyResemblance(BiConsumer<Double, Double> builder, TruthValue truthValue)
  {
    builder.accept(t.getFrequency() * truthValue.getFrequency(),
        (t.getFrequency() + truthValue.getFrequency()
            - t.getFrequency() * truthValue.getFrequency()) * t.getConfidence()
            * truthValue.getConfidence());
  }

  public void applyAbduction(BiConsumer<Double, Double> builder, TruthValue truthValue,
      double evidentialHorizon)
  {
    final double pw = t.getFrequency() * truthValue.getFrequency() * t.getConfidence()
        * truthValue.getConfidence();
    final double w = t.getFrequency() * t.getConfidence() * truthValue.getConfidence();
    builder.accept(pw / w, w / (w + evidentialHorizon));
  }

  public void applyInduction(BiConsumer<Double, Double> builder, TruthValue truthValue,
      double evidentialHorizon)
  {
    final double pw = t.getFrequency() * truthValue.getFrequency() * t.getConfidence()
        * truthValue.getConfidence();
    final double w = truthValue.getFrequency() * t.getConfidence() * truthValue.getConfidence();
    builder.accept(pw / w, w / (w + evidentialHorizon));
  }

  public void applyExemplification(BiConsumer<Double, Double> builder, TruthValue truthValue,
      double evidentialHorizon)
  {
    final double w = t.getFrequency() * truthValue.getFrequency() * t.getConfidence()
        * truthValue.getConfidence();
    builder.accept(1.0, w / (w + evidentialHorizon));
  }

  public void applyComparison(BiConsumer<Double, Double> builder, TruthValue truthValue,
      double evidentialHorizon)
  {
    final double pw = t.getFrequency() * truthValue.getFrequency() * t.getConfidence()
        * truthValue.getConfidence();
    final double w = (t.getFrequency() + truthValue.getFrequency()
        - t.getFrequency() * truthValue.getFrequency()) * t.getConfidence()
        * truthValue.getConfidence();
    builder.accept(pw / w, w / (w + evidentialHorizon));
  }

  public void applyIntersection(BiConsumer<Double, Double> builder, TruthValue truthValue)
  {
    builder.accept(t.getFrequency() * truthValue.getFrequency(),
        t.getConfidence() * truthValue.getConfidence());
  }

  public void applyUnion(BiConsumer<Double, Double> builder, TruthValue truthValue)
  {
    builder.accept(1 - (1 - t.getFrequency()) * (1 - truthValue.getFrequency()),
        t.getConfidence() * truthValue.getConfidence());
  }

  public void applyDifference(BiConsumer<Double, Double> builder, TruthValue truthValue)
  {
    builder.accept(t.getFrequency() * (1 - truthValue.getFrequency()),
        t.getConfidence() * truthValue.getConfidence());
  }
}
