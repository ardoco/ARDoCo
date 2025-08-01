/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.ElementStorageRegistry;

/**
 * Is responsible for extracting elements from an directory. The extracted
 * elements are then stored in an ElementStorageRegistry.
 * The extraction process is implemented by the implementing class. The
 * extraction process is done by building a token stream from
 * a file via ANTLR and extracting the elements from the token stream.
 * After the extraction of structural elements, the CommentExtractor is called
 * to extract comments from the file and added to the
 * ElementStorageRegistry.
 */
public abstract class ElementExtractor {
    protected CommentExtractor commentExtractor;
    protected final Logger logger = LoggerFactory.getLogger(ElementExtractor.class);

    public abstract ElementStorageRegistry getElements();

    /**
     * Extracts elements from the given directory
     * 
     * @param directoryPath, the path of the directory as string
     */
    public void extract(String directoryPath) {
        List<Path> files = getFiles(directoryPath);
        for (Path file : files) {
            extractContent(file);
        }
    }

    /**
     * Extracts the content of the given file
     * 
     * @param file, the path of the file
     */
    protected void extractContent(Path file) {
        CommonTokenStream tokens;
        try {
            tokens = buildTokens(file);
        } catch (IOException e) {
            logger.error("I/O operation failed", e);
            return;
        }
        extractElements(tokens);
        extractComments(file, tokens);
    }

    /**
     * Extracts comments from the given file, using the given token stream
     * 
     * @param file,   the path of the file
     * @param tokens, the token stream generated by the ANTLR parser
     */
    protected void extractComments(Path file, CommonTokenStream tokens) {
        String path = file.toString();
        commentExtractor.extract(path, tokens);
    }

    /**
     * Returns a list of files in the given directory
     * 
     * @param directoryPath the path of the directory as String
     * @return a list of files in the directory as Path objects
     */
    protected abstract List<Path> getFiles(String directoryPath);

    /**
     * Builds a token stream from the given file
     * 
     * @param file, the path of the file
     * @return the token stream generated by the ANTLR parser
     * @throws IOException if the file cannot be read
     */
    protected abstract CommonTokenStream buildTokens(Path file) throws IOException;

    /**
     * Extracts structural elements from the given token stream
     * 
     * @param tokens, the token stream generated by the ANTLR parser
     */
    protected abstract void extractElements(CommonTokenStream tokens);
}
