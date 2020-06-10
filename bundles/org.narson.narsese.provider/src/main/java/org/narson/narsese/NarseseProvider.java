package org.narson.narsese;

import static org.narson.tools.PredChecker.checkNotNull;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.NarseseGenerator;
import org.narson.api.narsese.NarseseParser;
import org.narson.api.narsese.NarseseReader;
import org.narson.api.narsese.NarseseReading;
import org.narson.api.narsese.NarseseReadingBuilder;
import org.narson.api.narsese.NarseseWriter;
import org.narson.api.narsese.NarseseWriting;
import org.narson.api.narsese.NarseseWritingBuilder;
import org.narson.api.narsese.Sentence;
import org.narson.narsese.provider.codec.NarseseReaderImpl;
import org.narson.narsese.provider.codec.NarseseReadingImpl;
import org.narson.narsese.provider.codec.NarseseSentenceWritingBuilder;
import org.narson.narsese.provider.codec.NarseseSingleWritingImpl;
import org.narson.narsese.provider.codec.NarseseStringReadingBuilder;
import org.narson.narsese.provider.codec.NarseseWriterImpl;
import org.narson.narsese.provider.codec.NarseseWritingImpl;
import org.narson.narsese.provider.model.NarseseFactoryImpl;
import org.narson.narsese.provider.stream.NarseseGeneratorImpl;
import org.narson.narsese.provider.stream.NarseseParserImpl;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.Logger;
import org.osgi.service.log.LoggerFactory;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.util.converter.Converter;
import org.osgi.util.converter.Converters;
import org.osgi.util.converter.TypeReference;

@Designate(ocd = NarseseProvider.Config.class, factory = true)
@Component(configurationPid = "org.narson.narsese",
    configurationPolicy = ConfigurationPolicy.OPTIONAL)
final public class NarseseProvider implements Narsese
{
  @ObjectClassDefinition
  protected @interface Config
  {
    int prefix_thresold() default 3;

    int buffer_size() default 1000;

    String charset() default "UTF-8";

    double truthvalue_frequency() default 1;

    double truthvalue_confidence() default 0.9;

    double desirevalue_frequency() default 1;

    double desirevalue_confidence() default 0.9;
  }

  @Reference(service = LoggerFactory.class)
  private Logger logger;

  private Config baseConf;

  private Charset charset;

  private NarseseFactory narseseFactory;

  private final Converter converter = Converters.standardConverter();

  public NarseseProvider()
  {
    this(new HashMap<>());
  }

  public NarseseProvider(Map<String, Object> configuration)
  {
    checkNotNull(configuration, "configuration");

    baseConf = adapt(configuration, true);
    init();
  }

  private NarseseProvider(Logger logger, Config conf)
  {
    this.logger = logger;
    baseConf = conf;
    init();
  }

  @Activate
  private void activate(Config configuration)
  {
    baseConf = adapt(configuration);
    init();
  }

  private void init()
  {
    charset = Charset.forName(baseConf.charset());
    narseseFactory = new NarseseFactoryImpl(this, baseConf.truthvalue_frequency(),
        baseConf.truthvalue_confidence(), baseConf.desirevalue_frequency(),
        baseConf.desirevalue_confidence());
  }

  @Override
  public NarseseFactory getNarseseFactory()
  {
    return narseseFactory;
  }

  @Override
  public Narsese createNarseseProvider(Map<String, Object> configuration)
  {
    return new NarseseProvider(logger, adapt(checkNotNull(configuration, "configuration"), false));
  }

  @Override
  public NarseseGenerator createGenerator(OutputStream out)
  {
    return new NarseseGeneratorImpl(checkNotNull(out, "out"), charset, baseConf.buffer_size(),
        baseConf.prefix_thresold());
  }

  @Override
  public NarseseGenerator createGenerator(Writer out)
  {
    return new NarseseGeneratorImpl(checkNotNull(out, "out"), baseConf.buffer_size(),
        baseConf.prefix_thresold());
  }

  @Override
  public NarseseWriter createWriter(OutputStream out)
  {
    return new NarseseWriterImpl(new NarseseGeneratorImpl(checkNotNull(out, "out"), charset,
        baseConf.buffer_size(), baseConf.prefix_thresold()));
  }

  @Override
  public NarseseWriter createWriter(Writer out)
  {
    return new NarseseWriterImpl(new NarseseGeneratorImpl(checkNotNull(out, "out"),
        baseConf.buffer_size(), baseConf.prefix_thresold()));
  }

  @Override
  public NarseseWriting write(Collection<Sentence> sentences) throws NullPointerException
  {
    checkNotNull(sentences, "sentences");
    return new NarseseWritingImpl(new ArrayList<>(sentences),
        out -> new NarseseWriterImpl(new NarseseGeneratorImpl(out, charset, baseConf.buffer_size(),
            baseConf.prefix_thresold())),
        out -> new NarseseWriterImpl(
            new NarseseGeneratorImpl(out, baseConf.buffer_size(), baseConf.prefix_thresold())));
  }

  @Override
  public NarseseWriting write(Sentence... sentences) throws NullPointerException
  {
    checkNotNull(sentences, "sentences");
    return new NarseseWritingImpl(sentences,
        out -> new NarseseWriterImpl(new NarseseGeneratorImpl(out, charset, baseConf.buffer_size(),
            baseConf.prefix_thresold())),
        out -> new NarseseWriterImpl(
            new NarseseGeneratorImpl(out, baseConf.buffer_size(), baseConf.prefix_thresold())));
  }

  @Override
  public NarseseWriting write(Sentence sentence) throws NullPointerException
  {
    checkNotNull(sentence, "sentence");
    return new NarseseSingleWritingImpl(sentence,
        out -> new NarseseWriterImpl(new NarseseGeneratorImpl(out, charset, baseConf.buffer_size(),
            baseConf.prefix_thresold())),
        out -> new NarseseWriterImpl(
            new NarseseGeneratorImpl(out, baseConf.buffer_size(), baseConf.prefix_thresold())));
  }

  @Override
  public NarseseWritingBuilder<Sentence> writing()
  {
    return new NarseseSentenceWritingBuilder(this);
  }

  @Override
  public NarseseParser createParser(InputStream in)
  {
    return new NarseseParserImpl(checkNotNull(in, "in"), charset, baseConf.buffer_size());
  }

  @Override
  public NarseseParser createParser(Reader in)
  {
    return new NarseseParserImpl(checkNotNull(in, "in"), baseConf.buffer_size());
  }

  @Override
  public NarseseReader createReader(InputStream in)
  {
    return new NarseseReaderImpl(
        new NarseseParserImpl(checkNotNull(in, "in"), charset, baseConf.buffer_size()),
        narseseFactory);
  }

  @Override
  public NarseseReader createReader(Reader in)
  {
    return new NarseseReaderImpl(
        new NarseseParserImpl(checkNotNull(in, "in"), baseConf.buffer_size()), narseseFactory);
  }

  @Override
  public NarseseReading read(InputStream in) throws NullPointerException
  {
    checkNotNull(in, "in");
    return new NarseseReadingImpl(() -> new NarseseReaderImpl(
        new NarseseParserImpl(in, charset, baseConf.buffer_size()), narseseFactory));
  }

  @Override
  public NarseseReading read(Reader in) throws NullPointerException
  {
    checkNotNull(in, "in");
    return new NarseseReadingImpl(() -> new NarseseReaderImpl(
        new NarseseParserImpl(in, baseConf.buffer_size()), narseseFactory));
  }

  @Override
  public NarseseReading read(String in) throws NullPointerException
  {
    checkNotNull(in, "in");
    return new NarseseReadingImpl(() -> new NarseseReaderImpl(
        new NarseseParserImpl(new StringReader(in), baseConf.buffer_size()), narseseFactory));
  }

  @Override
  public NarseseReadingBuilder<String> reading()
  {
    return new NarseseStringReadingBuilder(this);
  }

  private Config adapt(Map<String, Object> configuration, boolean tryLog)
  {
    final Map<String, Object> dfltConf =
        baseConf != null ? converter.convert(baseConf).to(new TypeReference<Map<String, Object>>()
        {}) : new HashMap<>();

    dfltConf.putAll(checkConf(configuration, tryLog));

    return converter.convert(dfltConf).to(Config.class);
  }

  private Config adapt(Config configuration)
  {
    return adapt(converter.convert(configuration).to(new TypeReference<Map<String, Object>>()
    {}), true);
  }

  private Map<String, Object> checkConf(Map<String, Object> configuration, boolean tryLog)
  {
    final Map<String, Object> result = new HashMap<>();

    validate(CONFIG_KEY_BUFFER_SIZE, configuration, result, tryLog, Integer.class, v -> v > 0);
    validate(CONFIG_KEY_DESIREVALUE_CONFIDENCE, configuration, result, tryLog, Double.class,
        v -> 0 < v && v < 1);
    validate(CONFIG_KEY_DESIREVALUE_FREQUENCY, configuration, result, tryLog, Double.class,
        v -> 0 <= v && v <= 1);
    validate(CONFIG_KEY_PREFIX_THRESOLD, configuration, result, tryLog, Integer.class, v -> true);
    validate(CONFIG_KEY_TRUTHVALUE_CONFIDENCE, configuration, result, tryLog, Double.class,
        v -> 0 < v && v < 1);
    validate(CONFIG_KEY_TRUTHVALUE_FREQUENCY, configuration, result, tryLog, Double.class,
        v -> 0 <= v && v <= 1);
    validate(CONFIG_KEY_CHARSET, configuration, result, tryLog, String.class, v -> {
      try
      {
        Charset.forName(v);
        return true;
      } catch (final Exception ignore)
      {
        return false;
      }
    });

    return result.entrySet().stream().filter(e -> e.getValue() != null)
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  private <T> void validate(String key, Map<String, Object> inputConf,
      Map<String, Object> outputConf, boolean tryLog, Class<T> toType,
      Function<T, Boolean> validate)
  {
    final Object toConvert = inputConf.get(key);
    try
    {
      if (toConvert != null)
      {
        final T prop = converter.convert(toConvert).to(toType);
        if (validate.apply(prop))
        {
          outputConf.put(key, prop);
        } else
        {
          if (tryLog && logger != null)
          {
            logger.warn("Invalide configuration {}={}, a default value will be used.", key, prop);
          } else
          {
            throw new IllegalArgumentException("Invalide configuration " + key + "=" + prop);
          }
        }
      }
    } catch (final Exception exception)
    {
      if (tryLog && logger != null)
      {
        logger.warn("Invalide configuration {}, a default value will be used.", key, exception);
      } else
      {
        throw new IllegalArgumentException("Invalide configuration " + key, exception);
      }
    }
  }
}
