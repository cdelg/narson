package org.narson.narsese.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.Tense;
import org.narson.api.narsese.Term;

public class AbstractSentenceTest
{
  class Sentence extends AbstractSentence
  {
    public Sentence(ValueType valueType, int bufferSize, int prefixThreshold, Term statement)
    {
      super(valueType, bufferSize, prefixThreshold, statement);
    }
  }

  private final Sentence judgment1 =
      new Sentence(ValueType.JUDGMENT, 100, 1, new ConstantImpl(1, "one"));
  private final Sentence judgment2 =
      new Sentence(ValueType.JUDGMENT, 100, 1, new ConstantImpl(1, "one"));
  private final Sentence goal1 = new Sentence(ValueType.GOAL, 100, 1, new ConstantImpl(1, "one"));
  private final Sentence goal2 = new Sentence(ValueType.GOAL, 100, 1, new ConstantImpl(1, "two"));

  @Test
  public void testTermIsRight()
  {
    assertThat(judgment1.getStatement(), equalTo(new ConstantImpl(1, "one")));
    assertThat(judgment2.getStatement(), equalTo(new ConstantImpl(1, "one")));
    assertThat(goal1.getStatement(), equalTo(new ConstantImpl(1, "one")));
    assertThat(goal2.getStatement(), equalTo(new ConstantImpl(1, "two")));
  }

  @Test
  public void testEquals()
  {
    assertThat(judgment1.equals(judgment2), is(true));
    assertThat(judgment2.equals(judgment1), is(true));
    assertThat(judgment2.equals(goal1), is(false));
    assertThat(goal1.equals(goal2), is(false));
    assertThat(goal2.equals(judgment1), is(false));
  }

  @Test
  public void testToStringJustWorks()
  {
    assertThat(new JudgmentImpl(100, 2, new ConstantImpl(1, "one"), new TruthValueImpl(0.5, 0.5),
        Tense.NONE), hasToString("one. %0.5;0.5%"));
  }
}
