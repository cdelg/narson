package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * NarseseGenerationException indicates an incorrect Narsese is being generated.
 */
@ProviderType
final public class NarseseGenerationException extends NarseseException
{
  private static final long serialVersionUID = 1L;

  public NarseseGenerationException(String message)
  {
    super(message);
  }

  public NarseseGenerationException(String message, Throwable throwable)
  {
    super(message, throwable);
  }
}
