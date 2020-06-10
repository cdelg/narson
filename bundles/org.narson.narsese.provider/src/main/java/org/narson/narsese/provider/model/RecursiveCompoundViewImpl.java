package org.narson.narsese.provider.model;

import org.narson.api.narsese.RecursiveCompoundView;
import org.narson.api.narsese.Term;

final class RecursiveCompoundViewImpl implements RecursiveCompoundView
{
  private final Term first;
  private final Term second;

  public RecursiveCompoundViewImpl(Term first, Term second)
  {
    this.first = first;
    this.second = second;
  }

  @Override
  public Term getFirst()
  {
    return first;
  }

  @Override
  public Term getSecond()
  {
    return second;
  }
}


