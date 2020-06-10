package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A connector that affect one or terms in a {@link SetTerm}.
 *
 */
@ProviderType
public enum SetConnector
{
  EXTENSIONAL_SET, INTENSIONAL_SET
}
