/**
 * Provides an object model API to process Narsese and a streaming API to parse and generate
 * Narsese.
 *
 * <p>
 * The object model API is a high-level API that provides immutable object models for Narsese
 * structures. These Narsese structures are represented as object models using the Java types. The
 * object model API uses builder patterns to create these object models. These object models can
 * also be created from an input source using the class NarseseReader. Similarly, these object
 * models can be written to an output source using the class NarseseWriter.
 *
 * <p>
 * The streaming API consists of the interfaces NarseseParser and NarseseGenerator. The interface
 * NarseseParser contains methods to parse Narsese in a streaming way. The interface
 * NarseseGenerator contains methods to write Narsese to an output source in a streaming way.
 * NarseseParser provides forward, read-only access to Narsese data using the pull parsing
 * programming model. In this model the application code controls the thread and calls methods in
 * the parser interface to move the parser forward or to obtain Narsese data from the current state
 * of the parser. Conversely, NarseseGenerator provides methods to write Narsese to an output
 * source. The streaming API is a low-level API designed to process large amounts of Narsese data
 * efficiently. Other Narsese frameworks (such as Narsese binding) can be implemented using this
 * API.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0.0")
package org.narson.api.inference;
