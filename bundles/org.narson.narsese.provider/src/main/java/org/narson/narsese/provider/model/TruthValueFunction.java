package org.narson.narsese.provider.model;

import org.narson.api.narsese.TruthValue;

final public class TruthValueFunction
{
  private final double f1;
  private final double c1;
  private final double evidentialHorizon;

  public TruthValueFunction(TruthValue truthValue, double evidentialHorizon)
  {
    f1 = truthValue.getFrequency();
    c1 = truthValue.getConfidence();
    this.evidentialHorizon = evidentialHorizon;
  }

  public TruthValue computeConversion()
  {
    final double w = f1 * c1;

    return new TruthValueImpl(1.0, w / (w + evidentialHorizon));
  }

  public TruthValue computeContraposition()
  {
    final double w = (1 - f1) * c1;

    return new TruthValueImpl(0.0, w / (w + evidentialHorizon));
  }

  public TruthValue computeNegation()
  {
    return new TruthValueImpl(1 - f1, c1);
  }
}
