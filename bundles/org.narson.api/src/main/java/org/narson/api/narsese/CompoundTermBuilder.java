package org.narson.api.narsese;

import java.util.Collection;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A builder for creating {@link CompoundTerm} object. This builder can be obtained from a
 * {@link NarseseFactory}.
 *
 */
@ProviderType
public interface CompoundTermBuilder extends TermBuilder
{
  @Override
  CompoundTermBuilder of(Term term) throws NullPointerException;

  @Override
  CompoundTermBuilder of(Term... terms) throws NullPointerException;

  @Override
  CompoundTermBuilder of(Collection<Term> terms) throws NullPointerException;

  @Override
  CompoundTerm build() throws IllegalStateException;
}
