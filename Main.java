import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        File readFile = new File("I:\\TxtModifier\\src\\ReadHere.txt");
        File readKeys = new File("I:\\TxtModifier\\src\\Keys.txt");
        FileWriter fileWriter = new FileWriter("I:\\TxtModifier\\src\\WriteHere.txt");
        FileWriter keyWriter = new FileWriter("I:\\TxtModifier\\src\\Keys.txt", true);
        FileWriter exceptionWriter = new FileWriter("I:\\TxtModifier\\src\\ManualExceptionHandler.txt");
        FileWriter pdfWriter = new FileWriter("I:\\TxtModifier\\src\\PDFHandler.txt", true);

        Scanner scanner = new Scanner(readFile);
        List<String> keys = getKeys(readKeys);
        Map<String, String> urlToTitleMap = new LinkedHashMap<>();

        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();

            if (!keys.contains(row.toLowerCase())) {
                keys.add(row.toLowerCase());
                keyWriter.write(row.toLowerCase() + '\n');
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
                        System.out.println("Success");
                    }catch (Exception e){
                        System.out.println("Fail");
                        if(row.toLowerCase().contains("pdf") || row.toLowerCase().contains("doc") || row.toLowerCase().contains("xls")){
                            pdfWriter.write(row + '\n');
                        }else {
                            exceptionWriter.write(row + '\n');
                        }
                    }
                }catch (IOException e){
                    urlToTitleMap.put(row, "PROBLEM LOADING URL: ERROR 403 FORBIDDEN");
                    System.out.println("Fail");
                }
            }else {
                System.out.println("Repetition");
            }
        }

        for (String key:urlToTitleMap.keySet()) {
            fileWriter.write(urlToTitleMap.get(key) + " - " + key + "\n");
        }
        scanner.close();
        pdfWriter.close();
        exceptionWriter.close();
        keyWriter.close();
        fileWriter.close();
    }

    public static String getTitleFromURL(String httpCode) {
        String[] tokens = httpCode.split("<\\btitle\\b>");
        String[] tokens2 = tokens[1].split("</\\btitle\\b>");
        return tokens2[0];
    }

    public static List<String> getKeys(File readKeys) throws FileNotFoundException {
        Scanner scanner = new Scanner(readKeys);
        List<String> keys = new LinkedList<>();
        while(scanner.hasNextLine()){
            keys.add(scanner.nextLine().toLowerCase());
        }
        scanner.close();
        return keys;
    }
}