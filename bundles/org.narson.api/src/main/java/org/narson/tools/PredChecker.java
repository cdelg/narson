package org.narson.tools;

import java.util.function.Supplier;

public class PredChecker
{
  public static <T> T checkNotNull(T arg) throws NullPointerException
  {
    if (arg == null)
    {
      throw new NullPointerException();
    }
    return arg;
  }

  public static <T> T checkNotNull(T arg, String message) throws NullPointerException
  {
    if (arg == null)
    {
      throw new NullPointerException(message);
    }
    return arg;
  }

  public static <T> T checkNotNull(T arg, Supplier<String> messageSupplier)
      throws NullPointerException
  {
    if (arg == null)
    {
      throw new NullPointerException(messageSupplier.get());
    }
    return arg;
  }

  public static void checkArgument(boolean mustBeTrue) throws IllegalArgumentException
  {
    if (!mustBeTrue)
    {
      throw new IllegalArgumentException();
    }
  }

  public static void checkArgument(boolean mustBeTrue, String message)
      throws IllegalArgumentException
  {
    if (!mustBeTrue)
    {
      throw new IllegalArgumentException(message);
    }
  }

  public static void checkArgument(boolean mustBeTrue, Supplier<String> messageSupplier)
      throws IllegalArgumentException
  {
    if (!mustBeTrue)
    {
      throw new IllegalArgumentException(messageSupplier.get());
    }
  }

  public static void checkNotEmpty(String s) throws IllegalArgumentException
  {
    if (s.isEmpty())
    {
      throw new IllegalArgumentException("Argument empty");
    }
  }

  public static void checkNotEmpty(String s, String message) throws IllegalArgumentException
  {
    if (s.isEmpty())
    {
      throw new IllegalArgumentException("Argument empty: " + message);
    }
  }

  public static void checkNotEmpty(String s, Supplier<String> messageSupplier)
      throws IllegalArgumentException
  {
    if (s.isEmpty())
    {
      throw new IllegalArgumentException("Argument empty: " + messageSupplier.get());
    }
  }

  public static void checkState(boolean mustBeTrue) throws IllegalStateException
  {
    if (!mustBeTrue)
    {
      throw new IllegalStateException();
    }
  }

  public static void checkState(boolean mustBeTrue, String message) throws IllegalStateException
  {
    if (!mustBeTrue)
    {
      throw new IllegalStateException(message);
    }
  }

  public static void checkState(boolean mustBeTrue, Supplier<String> messageSupplier)
      throws IllegalStateException
  {
    if (!mustBeTrue)
    {
      throw new IllegalStateException(messageSupplier.get());
    }
  }
}
