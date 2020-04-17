package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * NarseseException indicates that some exception happened during Narsese processing.
 *
 */
@ProviderType
public class NarseseException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public NarseseException(String message)
  {
    super(message);
  }

  public NarseseException(String message, Throwable throwable)
  {
    super(message, throwable);
  }
}
