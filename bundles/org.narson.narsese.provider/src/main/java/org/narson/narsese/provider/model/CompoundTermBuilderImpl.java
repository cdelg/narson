package org.narson.narsese.provider.model;

import static org.narson.tools.PredChecker.checkState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.narson.api.narsese.CompoundConnector;
import org.narson.api.narsese.CompoundTerm;
import org.narson.api.narsese.CompoundTermBuilder;
import org.narson.api.narsese.Narsese;
import org.narson.api.narsese.NarseseValue.ValueType;
import org.narson.api.narsese.RecursiveCompoundView;
import org.narson.api.narsese.Term;

final class CompoundTermBuilderImpl extends AbstractTermBuilder implements CompoundTermBuilder
{
  private final CompoundConnector connector;

  public CompoundTermBuilderImpl(Narsese narsese, CompoundConnector connector)
  {
    super(narsese);
    this.connector = connector;
  }

  @Override
  public CompoundTermBuilder of(Term... terms) throws NullPointerException
  {
    add(terms);
    return this;
  }

  @Override
  public CompoundTermBuilder of(Term term) throws NullPointerException
  {
    add(term);
    return this;
  }

  @Override
  public CompoundTermBuilder of(Collection<Term> terms)
  {
    add(terms);
    return this;
  }

  @Override
  public CompoundTerm build()
  {
    if (connector.isOrdered())
    {
      final List<Term> effectiveTerms;
      if (connector.minCardinality() >= 2 && connector.maxCardinality() > 2)
      {
        effectiveTerms = new ArrayList<>();
        terms.forEach(t -> flatTerms(t, effectiveTerms));
      } else
      {
        effectiveTerms = new ArrayList<>(terms);
      }

      checkState(connector.minCardinality() <= effectiveTerms.size(),
          () -> "terms.size() < " + connector.minCardinality());
      checkState(effectiveTerms.size() <= connector.maxCardinality(),
          () -> "terms.size() > " + connector.maxCardinality());

      return new CompoundTermImpl(narsese, connector, Collections.unmodifiableList(effectiveTerms),
          Collections
              .unmodifiableList(Collections.singletonList(computeRecursiveView(effectiveTerms))));
    } else
    {
      final Set<Term> effectiveTerms;
      if (connector.minCardinality() >= 2 && connector.maxCardinality() > 2)
      {
        effectiveTerms = new HashSet<>();
        terms.forEach(t -> flatTerms(t, effectiveTerms));
      } else
      {
        effectiveTerms = new HashSet<>(terms);
      }

      checkState(connector.minCardinality() <= effectiveTerms.size(),
          () -> "terms.size() < " + connector.minCardinality());
      checkState(effectiveTerms.size() <= connector.maxCardinality(),
          () -> "terms.size() > " + connector.maxCardinality());

      return new CompoundTermImpl(narsese, connector, Collections.unmodifiableSet(effectiveTerms),
          Collections.unmodifiableList(computePermutations(effectiveTerms).stream()
              .map(this::computeRecursiveView).collect(Collectors.toList())));
    }
  }

  private List<Collection<Term>> computePermutations(Collection<Term> terms)
  {
    final List<Collection<Term>> permutations = new ArrayList<>();
    IntStream.range(0, terms.size()).forEach(i -> {
      final List<Term> l = new ArrayList<>(terms);
      final Term removed = l.remove(i);
      l.add(removed);
      permutations.add(l);
    });
    return permutations;
  }

  private RecursiveCompoundView computeRecursiveView(Collection<Term> terms)
  {
    Term first, second;

    if (terms.size() == 2)
    {
      final Iterator<Term> it = terms.iterator();
      first = it.next();
      second = it.next();
    } else
    {
      final List<Term> firstTerms = new ArrayList<>(terms);
      second = firstTerms.remove(firstTerms.size() - 1);
      if (connector.isOrdered())
      {
        first = new CompoundTermImpl(narsese, connector, Collections.unmodifiableList(firstTerms),
            Collections
                .unmodifiableList(Collections.singletonList(computeRecursiveView(firstTerms))));
      } else
      {
        first = new CompoundTermImpl(narsese, connector,
            Collections.unmodifiableSet(new HashSet<>(firstTerms)),
            Collections.unmodifiableList(computePermutations(firstTerms).stream()
                .map(this::computeRecursiveView).collect(Collectors.toList())));
      }
    }

    return new RecursiveCompoundViewImpl(first, second);
  }

  private void flatTerms(Term term, Collection<Term> terms)
  {
    if (term.getValueType() == ValueType.COMPOUND_TERM)
    {
      flatTerms(term.asCompoundTerm(), terms);
    } else
    {
      terms.add(term);
    }
  }

  private void flatTerms(CompoundTerm term, Collection<Term> terms)
  {
    if (term.getConnector() == connector)
    {
      term.getTerms().forEach(t -> flatTerms(t, terms));
    } else
    {
      terms.add(term);
    }
  }
}
