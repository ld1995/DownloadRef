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
        joiner.add(sign + url + "\r\n");
        info.add(url);
        urlMap.get(url).stream().filter(s -> !info.contains(s)).forEach(s -> search(s, urlMap, (sign + "\t")));
//        Set<String> atUrlContents  = urlMap.get(url);
//        for (String content : atUrlContents) {
//            if (!info.contains(content))
//                search(content, urlMap, (sign + "\t"));
//        }
    }
}