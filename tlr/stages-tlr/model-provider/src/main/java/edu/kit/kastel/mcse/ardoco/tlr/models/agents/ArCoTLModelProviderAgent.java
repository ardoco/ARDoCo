/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.agents;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;

import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.PipelineAgent;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.Extractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.informants.ArCoTLModelProviderInformant;

/**
 * Agent that provides information from models.
 */
public class ArCoTLModelProviderAgent extends PipelineAgent {

    /**
     * Instantiates a new model provider agent. The constructor takes a list of ModelConnectors that are executed and used to extract information from models.
     * You can specify the extractors xor the code model file.
     *
     * @param data                      the DataRepository
     * @param architectureConfiguration the architecture configuration
     * @param codeConfiguration         the code configuration
     */
    public ArCoTLModelProviderAgent(DataRepository data, ArchitectureConfiguration architectureConfiguration, CodeConfiguration codeConfiguration) {
        super(informants(data, architectureConfiguration, codeConfiguration), ArCoTLModelProviderAgent.class.getSimpleName(), data);
    }

    private static List<? extends Informant> informants(DataRepository data, ArchitectureConfiguration architectureConfiguration,
            CodeConfiguration codeConfiguration) {
        List<Informant> informants = new ArrayList<>();
        if (architectureConfiguration != null) {
            informants.add(new ArCoTLModelProviderInformant(data, architectureConfiguration.extractor()));
        }

        if (codeConfiguration != null && codeConfiguration.type() == CodeConfiguration.CodeConfigurationType.ACM_FILE) {
            informants.add(new ArCoTLModelProviderInformant(data, codeConfiguration.code(), codeConfiguration.metamodel()));
        }

        if (codeConfiguration != null && codeConfiguration.type() == CodeConfiguration.CodeConfigurationType.DIRECTORY) {
            for (Extractor e : codeConfiguration.extractors()) {
                informants.add(new ArCoTLModelProviderInformant(data, e));
            }
        }

        return informants;
    }

    public static ArCoTLModelProviderAgent getArCoTLModelProviderAgent(DataRepository dataRepository, ImmutableSortedMap<String, String> additionalConfigs,
            ArchitectureConfiguration architectureConfiguration, CodeConfiguration codeConfiguration) {
        if (architectureConfiguration == null && codeConfiguration == null) {
            throw new IllegalArgumentException("At least one configuration must be provided");
        }

        var agent = new ArCoTLModelProviderAgent(dataRepository, architectureConfiguration, codeConfiguration);
        agent.applyConfiguration(additionalConfigs);
        return agent;
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(ImmutableSortedMap<String, String> additionalConfiguration) {
        // empty
    }
}
