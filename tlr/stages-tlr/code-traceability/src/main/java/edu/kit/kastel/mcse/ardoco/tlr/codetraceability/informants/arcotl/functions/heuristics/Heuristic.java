/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.functions.heuristics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.models.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.architecture.ArchitectureComponent;
import edu.kit.kastel.mcse.ardoco.core.api.models.architecture.ArchitectureInterface;
import edu.kit.kastel.mcse.ardoco.core.api.models.architecture.ArchitectureItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.code.CodeCompilationUnit;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.CodeTraceabilityHelper;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.Confidence;
import edu.kit.kastel.mcse.ardoco.tlr.codetraceability.informants.arcotl.computation.NodeResult;

/**
 * A heuristic.
 */
public abstract class Heuristic {
    protected static final Logger logger = LoggerFactory.getLogger(Heuristic.class);

    protected final NodeResult getNodeResult(ArchitectureModel archModel, CodeModel codeModel) {
        NodeResult confidences = new NodeResult();
        for (var endpointTuple : CodeTraceabilityHelper.crossProductFromArchitectureItemsToCompilationUnits(archModel, codeModel)) {
            ArchitectureItem archEndpoint = endpointTuple.first();
            CodeCompilationUnit compUnit = endpointTuple.second();
            Confidence confidence = new Confidence();
            if (archEndpoint instanceof ArchitectureInterface archInterface) {
                confidence = this.calculateConfidence(archInterface, compUnit);
            }
            if (archEndpoint instanceof ArchitectureComponent archComponent) {
                confidence = this.calculateConfidence(archComponent, compUnit);
            }
            confidences.add(endpointTuple, confidence);
        }
        return confidences;
    }

    protected Confidence calculateConfidence(ArchitectureComponent archComponent, CodeCompilationUnit compUnit) {
        if (archComponent == null || compUnit == null) {
            logger.warn("null values when calculating confidence (component)");
        }
        return new Confidence();
    }

    protected Confidence calculateConfidence(ArchitectureInterface archInterface, CodeCompilationUnit compUnit) {
        if (archInterface == null || compUnit == null) {
            logger.warn("null values when calculating confidence (interface)");
        }
        return new Confidence();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof Heuristic);
    }
}
