/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;

/**
 * A collection of CodeItem mappers that can be used to build CodeItems from extracted Elements.
 * The collection used is determined by the concrete implementation.
 */
public abstract class CodeItemMapperCollection {
    protected List<CodeItemMapper> mappers;

    public CodeItem buildCodeItem(Element element) {
        var mapper = mappers.stream().filter(strategy -> strategy.supports(element)).findFirst();
        if (mapper.isEmpty()) {
            return null;
        }
        return mapper.get().buildCodeItem(element);
    }
}
