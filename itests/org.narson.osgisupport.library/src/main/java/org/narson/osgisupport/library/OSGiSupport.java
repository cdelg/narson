package org.narson.osgisupport.library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class OSGiSupport
{
  private final BundleContext context;
  private final Map<String, ServiceTracker<?, ?>> trackers = new ConcurrentHashMap<>();
  private final Map<Object, ServiceRegistration<?>> registered = new ConcurrentHashMap<>();

  private OSGiSupport(final BundleContext context)
  {
    this.context = context;
  }

  static public OSGiSupport create(final Class<?> clazz)
  {
    return new OSGiSupport(FrameworkUtil.getBundle(clazz).getBundleContext());
  }

  public <T> T getService(final Class<T> clazz, final int timeout)
  {
    @SuppressWarnings("unchecked")
    final ServiceTracker<T, T> st =
        (ServiceTracker<T, T>) this.trackers.computeIfAbsent(clazz.getCanonicalName(), (c) -> {
          final ServiceTracker<T, T> t =
              (ServiceTracker<T, T>) new ServiceTracker<>(this.context, clazz, null);
          t.open();
          return t;
        });

    T s = null;
    try
    {
      s = st.waitForService(timeout);
    }
    catch (final InterruptedException exception)
    {
      throw new RuntimeException(exception);
    }
    if (s == null)
    {
      throw new IllegalStateException("No service found for " + clazz.getCanonicalName());
    }

    return s;
  }

  @SuppressWarnings("unchecked")
  public <T> T getService(final Class<T> clazz, final String filter, final int timeout)
  {
    final ServiceTracker<T, T> st = (ServiceTracker<T, T>) this.trackers
        .computeIfAbsent(clazz.getCanonicalName() + filter, (c) -> {
          try
          {
            final ServiceTracker<T, T> t =
                (ServiceTracker<T, T>) new ServiceTracker<>(this.context, this.context.createFilter(
                    "(&(objectClass=" + clazz.getCanonicalName() + ")" + filter + ")"), null);
            t.open();
            return t;
          }
          catch (final InvalidSyntaxException exception)
          {
            throw new RuntimeException(exception);
          }
        });

    T s = null;
    try
    {
      s = st.waitForService(timeout);
    }
    catch (final InterruptedException exception)
    {
      throw new RuntimeException(exception);
    }
    if (s == null)
    {
      throw new IllegalStateException(
          "No service found for " + clazz.getCanonicalName() + " with filter " + filter);
    }

    return s;
  }

  public void ungetService(final Class<?> clazz)
  {
    final ServiceTracker<?, ?> t = this.trackers.remove(clazz.getCanonicalName());
    if (t != null)
    {
      t.close();
    }
  }

  public void ungetService(final Class<?> clazz, final String filter)
  {
    final ServiceTracker<?, ?> t = this.trackers.remove(clazz.getCanonicalName() + filter);
    if (t != null)
    {
      t.close();
    }
  }

  public void configureFactory(final String factoryPid, final String name, final Object... prop)
  {
    final ConfigurationAdmin admin = getService(ConfigurationAdmin.class, 1000);

    try
    {
      final Configuration conf = admin.getFactoryConfiguration(factoryPid, name, "?");

      final Dictionary<String, Object> properties = new Hashtable<>();

      if (prop != null)
      {
        final Iterator<Object> it = Arrays.asList(prop).iterator();
        while (it.hasNext())
        {
          properties.put(it.next().toString(), it.next());
        }
      }

      conf.updateIfDifferent(properties);
    }
    catch (final IOException exception)
    {
      throw new RuntimeException(exception);
    }
  }

  public void configure(final String pid, final Object... prop)
  {
    final ConfigurationAdmin admin = getService(ConfigurationAdmin.class, 1000);

    try
    {
      final Configuration conf = admin.getConfiguration(pid, "?");

      final Dictionary<String, Object> properties = new Hashtable<>();

      if (prop != null)
      {
        final Iterator<Object> it = Arrays.asList(prop).iterator();
        while (it.hasNext())
        {
          properties.put(it.next().toString(), it.next());
        }
      }

      conf.updateIfDifferent(properties);
    }
    catch (final IOException exception)
    {
      throw new RuntimeException(exception);
    }
  }

  public void register(final Object service, final Object... classThenProp)
  {
    final List<String> clazz = new ArrayList<>();

    if (classThenProp == null)
    {
      throw new IllegalArgumentException("classThenProp must not be null.");
    }

    final Dictionary<String, Object> properties = new Hashtable<>();
    boolean lookForclass = true;
    final Iterator<Object> it = Arrays.asList(classThenProp).iterator();
    while (it.hasNext())
    {
      if (lookForclass)
      {
        final Object c = it.next();

        if (c instanceof Class)
        {
          clazz.add(((Class<?>) c).getCanonicalName());
        }
        else
        {
          properties.put(c.toString(), it.next());
          lookForclass = false;
        }
      }
      else
      {
        properties.put(it.next().toString(), it.next());
      }
    }

    if (clazz.isEmpty())
    {
      throw new IllegalArgumentException(
          "classThenProp must contain class object at the begginning.");
    }

    this.registered.put(service,
        this.context.registerService(clazz.toArray(new String[0]), service, properties));
  }

  public void unregister(final Object service)
  {
    final ServiceRegistration<?> s = this.registered.remove(service);

    if (s != null)
    {
      s.unregister();
    }
  }

  public void deleteFactoryConfiguration(final String factoryPid, final String name)
  {
    final ConfigurationAdmin admin = getService(ConfigurationAdmin.class, 1000);

    try
    {
      final Configuration conf = admin.getFactoryConfiguration(factoryPid, name, "?");
      conf.delete();
    }
    catch (final IOException exception)
    {
      throw new RuntimeException(exception);
    }
  }

  public void deleteConfiguration(final String pid)
  {
    final ConfigurationAdmin admin = getService(ConfigurationAdmin.class, 1000);

    try
    {
      final Configuration conf = admin.getConfiguration(pid, "?");
      conf.delete();
    }
    catch (final IOException exception)
    {
      throw new RuntimeException(exception);
    }
  }

  public static <T> ListBuilder<T> list()
  {
    return new ListBuilder<>();
  }

  @SafeVarargs
  public static <T> ListBuilder<T> list(final T... o)
  {
    final ListBuilder<T> l = new ListBuilder<>();

    if (o != null)
    {
      for (final T oo : o)
      {
        l.add(oo);
      }
    }

    return l;
  }

  public static ListBuilder<Object> listO()
  {
    return new ListBuilder<>();
  }

  static class ListBuilder<T> extends LinkedList<T>
  {
    private static final long serialVersionUID = 1L;

    public ListBuilder<T> set(final T e)
    {
      this.add(e);
      return this;
    }
  }

  public static <K, T> MapBuilder<K, T> map()
  {
    return new MapBuilder<>();
  }

  public static MapBuilder<String, Object> mapO()
  {
    return new MapBuilder<>();
  }

  static class MapBuilder<K, T> extends LinkedHashMap<K, T>
  {
    private static final long serialVersionUID = 1L;

    public MapBuilder<K, T> set(final K k, final T v)
    {
      this.put(k, v);
      return this;
    }
  }
}
