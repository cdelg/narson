package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the location information of a Narsese event in an input source. The NarseseLocation
 * information can be used to identify incorrect Narsese. All the information are optional.
 *
 */
@ProviderType
public interface NarseseLocation
{
  /**
   * Returns the column number of the current Narsese event or -1 if the information is not
   * available.
   * 
   * @return the column number of the current Narsese event or -1 if the information is not
   *         available
   */
  long getColumnNumber();

  /**
   * Returns the line number of the current Narsese event or -1 if the information is not available.
   * 
   * @return the line number of the current Narsese event or -1 if the information is not available
   */
  long getLineNumber();

  /**
   * Returns the stream offset into the input source this location is pointing to or -1 if the
   * information is not available.
   * 
   * @return the stream offset into the input source this location is pointing to or -1 if the
   *         information is not available
   */
  long getStreamOffset();
}
