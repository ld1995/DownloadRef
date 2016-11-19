package gui;

import java.io.*;
import java.util.*;

public class WriteData {

    private File file;
    private Map<String, Set<String>> urlMap = new HashMap<>();
    private StringJoiner joiner = new StringJoiner("");
    private String startUrl;
    private Set<String> info = new TreeSet<>();

    public WriteData(File file, Map<String, Set<String>> urlMap, String startUrl) {
        this.file = file;
        this.urlMap = urlMap;
        this.startUrl = startUrl;
    }

    public void write() {
        search(startUrl, urlMap, "");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF-8"))) {
            writer.write(joiner.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void search(String url, Map<String, Set<String>> urlMap , String sign) {
        for (Map.Entry<String, Set<String>> stringSetEntry : urlMap.entrySet()) {
            if (stringSetEntry.getKey().equals(url)) {
                joiner.add(sign + stringSetEntry.getKey() + "\r\n");
                info.add(stringSetEntry.getKey());
                for (String urlForMap : stringSetEntry.getValue())
                    if (!info.contains(urlForMap))
                        search(urlForMap, urlMap, (sign + "\t"));
            }
        }
    }
}