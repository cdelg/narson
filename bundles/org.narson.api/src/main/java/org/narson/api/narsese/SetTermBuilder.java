package org.narson.api.narsese;

import java.util.Collection;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A builder for creating {@link CompoundTerm} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface SetTermBuilder extends TermBuilder
{
  @Override
  SetTermBuilder of(Term term) throws NullPointerException;

  @Override
  SetTermBuilder of(Term... terms) throws NullPointerException;

  @Override
  SetTermBuilder of(Collection<Term> terms) throws NullPointerException;

  @Override
  SetTerm build() throws IllegalStateException;
}
