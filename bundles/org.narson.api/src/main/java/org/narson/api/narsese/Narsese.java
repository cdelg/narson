package org.narson.api.narsese;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
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
   * Create a new Narsese provider using the provided configuration.
   *
   * @param configuration a map of properties to configure the Narsese provider.
   * @return a new Narsese provider
   * @throws NullPointerException if configuration is null
   * @throws IllegalArgumentException if the configuration is invalid
   */
  Narsese createNarseseProvider(Map<String, Object> configuration)
      throws NullPointerException, IllegalArgumentException;

  /**
   * Returns the a Narsese factory.
   *
   * @return the a Narsese factory
   */
  NarseseFactory getNarseseFactory();

  /**
   * Create a new generator for writing Narsese to the provided i/o stream.
   *
   * @param out i/o stream to which Narsese is written
   * @return a new Narsese generator
   * @throws NullPointerException if out is null
   */
  NarseseGenerator createGenerator(OutputStream out) throws NullPointerException;

  /**
   * Create a new generator for writing Narsese to the provided i/o stream.
   *
   * @param out i/o stream to which Narsese is written
   * @return a new Narsese generator
   * @throws NullPointerException if out is null
   */
  NarseseGenerator createGenerator(Writer out) throws NullPointerException;

  /**
   * Create a new writer for writing Narsese to the provided i/o stream.
   *
   * @param out i/o stream to which Narsese is written
   * @return a new Narsese writter
   * @throws NullPointerException if out is null
   */
  NarseseWriter createWriter(OutputStream out) throws NullPointerException;

  /**
   * Create a new writer for writing Narsese to the provided i/o stream.
   *
   * @param out i/o stream to which Narsese is written
   * @return a new Narsese writter
   * @throws NullPointerException if out is null
   */
  NarseseWriter createWriter(Writer out) throws NullPointerException;

  NarseseWriting write(Collection<Sentence> sentences) throws NullPointerException;

  NarseseWriting write(Sentence... sentences) throws NullPointerException;

  NarseseWriting write(Sentence sentence) throws NullPointerException;

  NarseseWritingBuilder<Sentence> writing();

  /**
   * Create a new parser for reading Narsese from the provided i/o stream.
   *
   * @param in i/o stream to which Narsese is to be read
   * @return a new Narsese parser
   * @throws NullPointerException if in is null
   */
  NarseseParser createParser(InputStream in) throws NullPointerException;

  /**
   * Create a new parser for reading Narsese from the provided i/o stream.
   *
   * @param in i/o stream to which Narsese is to be read
   * @return a new Narsese parser
   * @throws NullPointerException if in is null
   */
  NarseseParser createParser(Reader in) throws NullPointerException;

  /**
   * Create a new reader for reading Narsese from the provided i/o stream.
   *
   * @param in i/o stream to which Narsese is to be read
   * @return a new Narsese reader
   * @throws NullPointerException if in is null
   */
  NarseseReader createReader(InputStream in) throws NullPointerException;

  /**
   * Create a new reader for reading Narsese from the provided i/o stream.
   *
   * @param in i/o stream to which Narsese is to be read
   * @return a new Narsese reader
   * @throws NullPointerException if in is null
   */
  NarseseReader createReader(Reader in) throws NullPointerException;

  NarseseReading read(InputStream in) throws NullPointerException;

  NarseseReading read(Reader in) throws NullPointerException;

  NarseseReading read(String in) throws NullPointerException;

  NarseseReadingBuilder<String> reading();
}
