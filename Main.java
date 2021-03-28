import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File readFile = new File("I:\\TxtModifier\\src\\ReadHere.txt");
        FileWriter fileWriter = new FileWriter("I:\\TxtModifier\\src\\WriteHere.txt");

        Scanner scanner = new Scanner(readFile);

        Map<String, String> urlToTitleMap = new HashMap<>();

        Integer hashtagCount = 0;
        Integer urlRepetitionsCount = 0;
        Integer forbiddenPageCount = 0;
        Integer encounteredFileCount = 0;
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            if (row.contains("#")) {
                String[] tokens = row.split("#");
                hashtagCount++;
                row = tokens[0];
            }
            if (urlToTitleMap.containsKey(row)) {
                urlRepetitionsCount++;
            } else {
                try {
                    URL url = new URL(row);
                    InputStream is = url.openStream();
                    int ptr = 0;
                    StringBuffer buffer = new StringBuffer();
                    while ((ptr = is.read()) != -1) {
                        buffer.append((char) ptr);
                    }
                    is.close();
                    try {
                        urlToTitleMap.put(row, getTitleFromURL(String.valueOf(buffer)));
                    }catch (Exception e){
                        urlToTitleMap.put(row, "PROBLEM GETTING TITLE: PROBABLY A DOCUMENT");
                        encounteredFileCount++;
                    }
                }catch (IOException e){
                    urlToTitleMap.put(row, "PROBLEM LOADING URL: ERROR 403 FORBIDDEN");
                    forbiddenPageCount++;
                }
            }
        }

        scanner.close();
        for (String key:urlToTitleMap.keySet()) {
            fileWriter.write(urlToTitleMap.get(key) + " - " + key + "\n");
        }
        fileWriter.close();
    }

    public final static String getTitleFromURL(String httpCode) {
        String[] tokens = httpCode.split("<\\btitle\\b>");
        String[] tokens2 = tokens[1].split("</\\btitle\\b>");
        return tokens2[0];
    }

}