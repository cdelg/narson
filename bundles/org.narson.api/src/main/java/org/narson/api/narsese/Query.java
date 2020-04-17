package org.narson.api.narsese;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Query class represents an immutable Narsese query sentence.
 * <p>
 * A Query can be obtained from a {@link NarseseFactory}.
 *
 */
@ProviderType
public interface Query extends Sentence
{

}
