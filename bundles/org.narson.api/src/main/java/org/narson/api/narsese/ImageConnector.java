package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A connector that affect one or terms in a {@link ImageTerm}.
 *
 */
@ProviderType
public enum ImageConnector
{
  EXTENSIONAL_IMAGE, INTENSIONAL_IMAGE
}
