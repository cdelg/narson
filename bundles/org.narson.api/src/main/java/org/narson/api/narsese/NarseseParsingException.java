package org.narson.api.narsese;

import static org.narson.tools.PredChecker.checkNotNull;
import org.osgi.annotation.versioning.ProviderType;


/**
 * NarseseParsingException is used when an incorrect Narsese is being parsed.
 */
@ProviderType
final public class NarseseParsingException extends NarseseException
{
  private static final long serialVersionUID = 1L;

  private final NarseseLocation location;

  public NarseseParsingException(String message, NarseseLocation location)
  {
    super(message);
    this.location = checkNotNull(location, "location");
  }

  public NarseseParsingException(String message, Throwable throwable, NarseseLocation location)
  {
    super(message, throwable);
    this.location = checkNotNull(location, "location");
  }

  /**
   * Returns the location of the incorrect Narsese.
   * 
   * @return the location of the incorrect Narsese
   */
  public NarseseLocation getLocation()
  {
    return location;
  }
}
