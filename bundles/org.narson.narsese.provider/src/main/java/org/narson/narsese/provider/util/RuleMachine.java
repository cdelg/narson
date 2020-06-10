package org.narson.narsese.provider.util;

import java.util.function.Function;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseFactory;
import org.narson.api.narsese.Sentence;

final public class RuleMachine
{
  private RuleMachine()
  {}



  public static RuleMachineBuilder createBuilder(Narsese narsese)
  {
    return new RuleMachineBuilder(narsese);
  }

  static public class RuleMachineBuilder
  {
    private final Function<String, Sentence> reading;
    private final NarseseFactory nf;

    private RuleMachineBuilder(Narsese narsese)
    {
      reading = narsese.reading().toSentence();
      nf = narsese.getNarseseFactory();
    }

    public RuleBuilder addRule(String j1, String j2, String j)
    {
      return new RuleBuilder(j1, j2, j);
    }

    public ImmediateRuleBuilder addRule(String j1, String j)
    {
      return new ImmediateRuleBuilder(j1, j);
    }

    public RuleMachine build()
    {
      return new RuleMachine();
    }
  }

  static public class RuleBuilder
  {
    private final String j1;
    private final String j2;
    private final String j;

    public RuleBuilder(String j1, String j2, String j)
    {
      this.j1 = j1;
      this.j2 = j2;
      this.j = j;
    }

    public RuleBuilder equals(String c1, String c2)
    {
      return this;
    }

    public RuleBuilder diff(String c1, String c2)
    {
      return this;
    }

    public RuleBuilder ded()
    {
      return this;
    }

    public RuleBuilder exe()
    {
      return this;
    }

    public RuleBuilder abd()
    {
      return this;
    }

    public RuleBuilder ind()
    {
      return this;
    }

    public RuleBuilder com()
    {
      return this;
    }

    public RuleBuilder ana()
    {
      return this;
    }

    public RuleBuilder res()
    {
      return this;
    }

    public RuleBuilder inte()
    {
      return this;
    }

    public RuleBuilder uni()
    {
      return this;
    }

    public RuleBuilder dif()
    {
      return this;
    }

    public RuleBuilder switchable()
    {
      return this;
    }

    public void done()
    {}
  }

  static public class ImmediateRuleBuilder
  {

    private final String j1;
    private final String j;

    public ImmediateRuleBuilder(String j1, String j)
    {
      this.j1 = j1;
      this.j = j;
    }

    public ImmediateRuleBuilder neg()
    {
      return this;
    }

    public ImmediateRuleBuilder cnv()
    {
      return this;
    }

    public ImmediateRuleBuilder cnt()
    {
      return this;
    }

    public void done()
    {}
  }

  static public class StructuralRuleBuilder
  {

    public StructuralRuleBuilder j1(String j1)
    {
      return this;
    }
  }
}
