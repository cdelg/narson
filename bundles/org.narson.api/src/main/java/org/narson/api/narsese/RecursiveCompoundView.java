package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;


@ProviderType
public interface RecursiveCompoundView
{
  Term getFirst();

  Term getSecond();
}
