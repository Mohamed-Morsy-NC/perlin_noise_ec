package backend;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StateManager {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void saveState(SaveState state, String filename) {
        try {
            File readFile = new File(filename);
            File directory = readFile.getParentFile();

            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(readFile, state);
            System.out.println("Save state saved to " + filename);
        } catch (Exception e) {
            System.err.println("Error saving save state: " + e.getMessage());
        }
    }

    public static SaveState loadState(String filename) {
        try {
            return mapper.readValue(new File(filename), SaveState.class);
        } catch (Exception e) {
            System.err.println("Error loading save state: " + e.getMessage());  
            return null;
        }
    }
}
