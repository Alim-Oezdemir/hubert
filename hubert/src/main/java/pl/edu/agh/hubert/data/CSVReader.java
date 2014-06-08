package pl.edu.agh.hubert.data;

import pl.edu.agh.hubert.data.container.DataContainer;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * User: koperek
 * Date: 14.02.13
 * Time: 19:43
 */
public class CSVReader {

    private final BufferedReader reader;
    private String[] legend;

    public CSVReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void fillIn(DataContainer dataContainer) throws IOException {
        fillInLegend(dataContainer);
        fillInContent(dataContainer);
    }

    private void fillInContent(DataContainer dataContainer) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (isNotEmpty(line)) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    dataContainer.addValue(legend[i], Double.parseDouble(values[i]));
                }
            }
        }
    }

    private void fillInLegend(DataContainer dataContainer) throws IOException {
        String names = reader.readLine();
        if (names.startsWith("#")) {
            names = names.substring(1);
        }
        legend = names.split(",");

        dataContainer.initializeVariables(legend);
    }

    private boolean isNotEmpty(String line) {
        return line != null && !line.isEmpty();
    }
}