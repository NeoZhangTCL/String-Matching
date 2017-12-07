import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Test {

    private void writeCsv(String fileName, long[][] record)
            throws IOException{
        PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
        writer.println("Naive, Randomized, KMP, Boyer-Mooer, String.indexOf");
        for (long[] r: record) {
            String line = Arrays.stream(r)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(","));
            writer.println(line);
        }
        writer.close();
    }


}
