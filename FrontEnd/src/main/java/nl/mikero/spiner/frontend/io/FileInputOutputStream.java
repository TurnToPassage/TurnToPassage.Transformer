package nl.mikero.spiner.frontend.io;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileInputOutputStream implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileInputOutputStream.class);

    private final FileInputStream input;
    private final FileOutputStream output;

    public FileInputOutputStream(final File inputFile, final File outputFile) throws IOException {
        input = FileUtils.openInputStream(inputFile);

        try {
            output = FileUtils.openOutputStream(outputFile);
        } catch (IOException e) {
            input.close();
            throw e;
        }
    }

    public final FileInputStream getInputStream() {
        return input;
    }

    public final FileOutputStream getOutputStream() {
        return output;
    }

    @Override
    public void close() throws IOException {
        try {
            input.close();
            output.close();
        } catch (IOException e) {
            LOGGER.warn("Error closing FileInputOutputStream.", e);
            output.close();
        }
    }
}
