package org.narson.narsese.provider.model;

import org.narson.api.narsese.TruthValue;

final public class BiTruthValueFunction
{
  private final double f1;
  private final double c1;
  private final double f2;
  private final double c2;
  private final double evidentialHorizon;
  private final BiTruthValueFunction inv;

  public BiTruthValueFunction(TruthValue truthValue, TruthValue other)
  {
    this(truthValue, other, 0.0);
  }

  public BiTruthValueFunction(TruthValue truthValue, TruthValue other, double evidentialHorizon)
  {
    f1 = truthValue.getFrequency();
    c1 = truthValue.getConfidence();
    f2 = other.getFrequency();
    c2 = other.getConfidence();
    this.evidentialHorizon = evidentialHorizon;
    inv = new BiTruthValueFunction(other, truthValue, evidentialHorizon, this);
  }

  private BiTruthValueFunction(TruthValue truthValue, TruthValue other, double evidentialHorizon,
      BiTruthValueFunction inv)
  {
    f1 = truthValue.getFrequency();
    c1 = truthValue.getConfidence();
    f2 = other.getFrequency();
    c2 = other.getConfidence();
    this.evidentialHorizon = evidentialHorizon;
    this.inv = inv;
  }

  public BiTruthValueFunction invert()
  {
    return inv;
  }

  public TruthValue computeDeduction()
  {
    final double f = f1 * f2;

    return new TruthValueImpl(f, f * c1 * c2);
  }

  public TruthValue computeAnalogy()
  {
    return new TruthValueImpl(f1 * f2, f2 * c1 * c2);
  }

  public TruthValue computeResemblance()
  {
    return new TruthValueImpl(f1 * f2, (f1 + f2 - f1 * f2) * c1 * c2);
  }

  public TruthValue computeAbduction()
  {
    final double pw = f1 * f2 * c1 * c2;
    final double w = f1 * c1 * c2;

    return new TruthValueImpl(pw / w, w / (w + evidentialHorizon));
  }

  public TruthValue computeInduction()
  {
    final double pw = f1 * f2 * c1 * c2;
    final double w = f2 * c1 * c2;

    return new TruthValueImpl(pw / w, w / (w + evidentialHorizon));
  }

  public TruthValue computeExemplification()
  {
    final double w = f1 * f2 * c1 * c2;

    return new TruthValueImpl(1.0, w / (w + evidentialHorizon));
  }

  public TruthValue computeComparison()
  {
    final double pw = f1 * f2 * c1 * c2;
    final double w = (f1 + f2 - f1 * f2) * c1 * c2;

    return new TruthValueImpl(pw / w, w / (w + evidentialHorizon));
  }

  public TruthValue computeIntersection()
  {
    return new TruthValueImpl(f1 * f2, c1 * c2);
  }

  public TruthValue computeUnion()
  {
    return new TruthValueImpl(1 - (1 - f1) * (1 - f2), c1 * c2);
  }

  public TruthValue computeDifference()
  {
    return new TruthValueImpl(f1 * (1 - f2), c1 * c2);
  }

  public TruthValue computeRevision()
  {
    final double c1c2 = c1 * (1 - c2);
    final double c2c1 = c2 * (1 - c1);
    final double d = c1c2 + c2c1;

    return new TruthValueImpl((f1 * c1c2 + f2 * c2c1) / d, d / (c1c2 + c2c1 + (1 - c1) * (1 - c2)));
  }
}
