package data_collection;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmogonDownload {

    //regex benutzen wenn man text parset
    //tabellen .csv .tsv formate umwandeln

    /*
    * html als
    *
    * */

    private static final Pattern pattern = Pattern.compile("(\\d+),(\\w+),([^,]*),([^,]*),([^,]*),([^,]*),.*");
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws IOException {
        Path targetDirectory = Paths.get("Smogon");
        if(! Files.exists(targetDirectory)) {
            Files.createDirectories(targetDirectory);
        }

        List<Integer> alreadyFound = new ArrayList<>();

        for (String line : Files.readAllLines(Paths.get("table.csv"))) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                int id = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);
                String[] urls = { matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6) };

                List<Future<?>> futures = new LinkedList<>();
                for(int i = 0;i < urls.length;i++) {
                    if(! urls[i].isEmpty()) {
                        String fileName = String.valueOf(id);
                        switch (i) {
                            case 0: fileName += "";break;
                            case 1: fileName += "b";break;
                            case 2: fileName += "s";break;
                            case 3: fileName += "sb";break;
                        }
                        if(alreadyFound.contains(id)) {
                            fileName+= "_" + name;
                        }
                        fileName += ".png";

                        while(Files.exists(targetDirectory.resolve(fileName))) {
                            fileName = fileName.replace(".png", "") + "_a_" + ".png";
                        }
                        final Path targetFile = targetDirectory.resolve(fileName);

                        int finalI = i;
                        futures.add(executor.submit(() -> {
                            try {
                                Files.copy(new URL(urls[finalI]).openStream(), targetFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.printf("Download done: %s from %s\n", targetFile.getFileName().toString(), urls[finalI]);
                        }));
                    }
                }
                for (Future<?> future : futures) {
                    try { future.get(); } catch (ExecutionException | InterruptedException e) { e.printStackTrace(); }
                }

                alreadyFound.add(id);
            }
        }

        System.out.println("Done");

//        Files.readAllLines(Paths.get("table.csv")).stream()
//                .map(line -> pattern.matcher(line))
//                .filter(matcher -> matcher.matches())
//                .map(matcher -> matcher.group(1))
//                .forEach(System.out::println);

//        String[] numbers = {"1", "2", "3", "4", "5"};
//        System.out.println(Arrays.stream(numbers).mapToInt(Integer::parseInt));
    }
}
