package org.narson.api.narsese;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Narsese class is the main service for Narsese processing. It can built various object related to
 * the Narsese language processing.
 */
@ProviderType
public interface Narsese
{
  /**
   * Property to indicate the number of terms at which the compound terms will be generated using
   * the prefix format.
   */
  String CONFIG_KEY_PREFIX_THRESOLD = "prefix.thresold";

  /**
   * Property to indicate the buffer size used to read and write data to an input/ouput stream.
   */
  String CONFIG_KEY_BUFFER_SIZE = "buffer.size";

  /**
   * Property to indicate the default charset that should be used to read or write data to an
   * input/ouput stream.
   */
  String CONFIG_KEY_CHARSET = "charset";

  /**
   * Property to indicate the default frequency of a truth value when this latter is missing.
   */
  String CONFIG_KEY_TRUTHVALUE_FREQUENCY = "truthvalue.frequency";

  /**
   * Property to indicate the default confidence of a truth value when this latter is missing.
   */
  String CONFIG_KEY_TRUTHVALUE_CONFIDENCE = "truthvalue.confidence";

  /**
   * Property to indicate the default frequency of a desire value when this latter is missing.
   */
  String CONFIG_KEY_DESIREVALUE_FREQUENCY = "desirevalue.frequency";

  /**
   * Property to indicate the default confidence of a desire value when this latter is missing.
   */
  String CONFIG_KEY_DESIREVALUE_CONFIDENCE = "desirevalue.confidence";

  /**
   * Returns the default Narsese factory.
   *
   * @return the default Narsese factory
   */
  NarseseFactory getNarseseFactory();

  /**
   * Create a new Narsese factory using the provided configuration.
   * <p>
   * The generator will use the specified configuration.
   *
   * @param configuration a map of properties to configure the Narsese language factory.
   * @return a new Narsese factory
   * @throws NullPointerException if configuration is null
   */
  NarseseFactory createNarseseFactory(Map<String, Object> configuration)
      throws NullPointerException;

  /**
   * Create a new generator for writing Narsese the provided i/o stream.
   *
   * @param out i/o stream to which Narsese is written
   * @return a new Narsese generator
   * @throws NullPointerException if out is null
   */
  NarseseGenerator createGenerator(OutputStream out) throws NullPointerException;

  /**
   * Create a new generator for writing Narsese the provided i/o stream.
   * <p>
   * The generator will use the specified configuration.
   *
   * @param out i/o stream to which Narsese is written
   * @param configuration a map of properties to configure the Narsese writter.
   * @return a new Narsese generator
   * @throws NullPointerException if out or configuration is null
   */
  NarseseGenerator createGenerator(OutputStream out, Map<String, Object> configuration)
      throws NullPointerException;

  /**
   * Create a new generator for writing Narsese the provided i/o stream.
   *
   * @param out i/o stream to which Narsese is written
   * @return a new Narsese generator
   * @throws NullPointerException if out is null
   */
  NarseseGenerator createGenerator(Writer out) throws NullPointerException;

  /**
   * Create a new generator for writing Narsese the provided i/o stream.
   * <p>
   * The generator will use the specified configuration.
   *
   * @param out i/o stream to which Narsese is written
   * @param configuration a map of properties to configure the Narsese writter.
   * @return a new Narsese generator
   * @throws NullPointerException if out or configuration is null
   */
  NarseseGenerator createGenerator(Writer out, Map<String, Object> configuration)
      throws NullPointerException;

  /**
   * Create a new writer for writing Narsese the provided i/o stream.
   *
   * @param out i/o stream to which Narsese is written
   * @return a new Narsese writter
   * @throws NullPointerException if out is null
   */
  NarseseWriter createWriter(OutputStream out) throws NullPointerException;

  /**
   * Create a new writer for writing Narsese the provided i/o stream.
   * <p>
   * The writer will use the specified configuration.
   *
   * @param out i/o stream to which Narsese is written
   * @param configuration a map of properties to configure the Narsese writter.
   * @return a new Narsese writter
   * @throws NullPointerException if out or configuration is null
   */
  NarseseWriter createWriter(OutputStream out, Map<String, Object> configuration)
      throws NullPointerException;

  /**
   * Create a new writer for writing Narsese the provided i/o stream.
   *
   * @param out i/o stream to which Narsese is written
   * @return a new Narsese writter
   * @throws NullPointerException if out is null
   */
  NarseseWriter createWriter(Writer out) throws NullPointerException;

  /**
   * Create a new writer for writing Narsese the provided i/o stream.
   * <p>
   * The writer will use the specified configuration.
   *
   * @param out i/o stream to which Narsese is written
   * @param configuration a map of properties to configure the Narsese writter.
   * @return a new Narsese writter
   * @throws NullPointerException if out or configuration is null
   */
  NarseseWriter createWriter(Writer out, Map<String, Object> configuration)
      throws NullPointerException;

  /**
   * Create a new parser for reading Narsese from provided i/o stream.
   *
   * @param in i/o stream to which Narsese is to be read
   * @return a new Narsese parser
   * @throws NullPointerException if in is null
   */
  NarseseParser createParser(InputStream in) throws NullPointerException;

  /**
   * Create a new parser for reading Narsese from provided i/o stream.
   * <p>
   * The parser will use the specified configuration.
   *
   * @param in i/o stream to which Narsese is to be read
   * @param configuration a map of properties to configure the Narsese reader.
   * @return a new Narsese parser
   * @throws NullPointerException if in or configuration is null
   */
  NarseseParser createParser(InputStream in, Map<String, Object> configuration)
      throws NullPointerException;

  /**
   * Create a new parser for reading Narsese from provided i/o stream.
   *
   * @param in i/o stream to which Narsese is to be read
   * @return a new Narsese parser
   * @throws NullPointerException if in is null
   */
  NarseseParser createParser(Reader in) throws NullPointerException;

  /**
   * Create a new parser for reading Narsese from provided i/o stream.
   * <p>
   * The parser will use the specified configuration.
   *
   * @param in i/o stream to which Narsese is to be read
   * @param configuration a map of properties to configure the Narsese reader.
   * @return a new Narsese parser
   * @throws NullPointerException if in or configuration is null
   */
  NarseseParser createParser(Reader in, Map<String, Object> configuration)
      throws NullPointerException;

  /**
   * Create a new reader for reading Narsese from provided i/o stream.
   *
   * @param in i/o stream to which Narsese is to be read
   * @return a new Narsese reader
   * @throws NullPointerException if in is null
   */
  NarseseReader createReader(InputStream in) throws NullPointerException;

  /**
   * Create a new reader for reading Narsese from provided i/o stream.
   * <p>
   * The reader will use the specified configuration.
   *
   * @param in i/o stream to which Narsese is to be read
   * @param configuration a map of properties to configure the Narsese reader.
   * @return a new Narsese reader
   * @throws NullPointerException if in or configuration is null
   */
  NarseseReader createReader(InputStream in, Map<String, Object> configuration)
      throws NullPointerException;

  /**
   * Create a new reader for reading Narsese from provided i/o stream.
   *
   * @param in i/o stream to which Narsese is to be read
   * @return a new Narsese reader
   * @throws NullPointerException if in is null
   */
  NarseseReader createReader(Reader in) throws NullPointerException;

  /**
   * Create a new reader for reading Narsese from provided i/o stream.
   * <p>
   * The reader will use the specified configuration.
   *
   * @param in i/o stream to which Narsese is to be read
   * @param configuration a map of properties to configure the Narsese reader.
   * @return a new Narsese reader
   * @throws NullPointerException if in or configuration is null
   */
  NarseseReader createReader(Reader in, Map<String, Object> configuration)
      throws NullPointerException;
}
