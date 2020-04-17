package org.narson.api.narsese;

import java.io.Closeable;
import java.io.Flushable;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface NarseseGenerator extends Flushable, Closeable
{
  NarseseGenerator write(NarseseValue value) throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeStartJudgment() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeStartGoal() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeStartQuestion() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeStartQuery() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writePastTense() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writePresentTense() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeFutureTense() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeTruthValue(double frequency, double confidence)
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeConstant(String name) throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeQueryVariable(String name)
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeStartDependentVariable(String name)
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeDependentIndependentVariable(String name)
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeIndependentVariable(String name)
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeStartCompoundTerm() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeInheritanceCopula() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeSimilarityCopula() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeImplicationCopula() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeEquivalenceCopula() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeInstanceCopula() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writePropertyCopula() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeInstancePropertyCopula()
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writePredictiveImplicationCopula()
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeRetrospectiveImplicationCopula()
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeConcurrentImplicationCopula()
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writePredictiveEquivalenceCopula()
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeConcurrentEquivalenceCopula()
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeOperation(String name) throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeStartExtensionalSet() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeStartIntensionalSet() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeExtensionalIntersection()
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeIntensionalIntersection()
      throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeExtensionalDifference() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeIntensionalDifference() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeProduct() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeExtensionalImage() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeIntensionalImage() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writePlaceHolder() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeNegation() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeConjunction() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeDisjunction() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeSequentialConjunction() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeParallelConjunction() throws NarseseException, NarseseGenerationException;

  NarseseGenerator writeEnd() throws NarseseException, NarseseGenerationException;

  @Override
  void flush() throws NarseseException;

  @Override
  void close() throws NarseseException, NarseseGenerationException;
}
