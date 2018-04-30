package jonl.jutils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for reading and writing to file
 * 
 * @author Jonathan Lacombe
 */
public class FileUtils {

    public static final String NEW_LINE = "\n";
    
    /**
     * Returns a StringBuilder containing the contents
     * of the specified file
     * @param file location/name of file to be read
     * @return a StringBuilder containing the contents
     * of the file
     * @throws IOException
     */
    public static String readFromFile(String file) {
        
        try {
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line=br.readLine())!=null) {
                sb.append(line).append(NEW_LINE);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            System.err.println("Failed to read from file: "+file);
        }
        
        return null;
        
    }
    
    public static String[] linesFromFile(String file) {
        return FileUtils.readFromFile(file).toString().split(NEW_LINE);
    }
    
    /**
     * Writes the array of lines to the specified file
     * <p>
     * Separates each String in the array with a new line
     * @param file location/file to be written to
     * @param lines array of lines to be written
     */
    public static void writeToFile(String file, String[] lines) {
        
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            for (String line : lines) {
                pw.println(line);
            }
            pw.close();
        } catch (IOException e) {
            System.err.println("Failed to write to file: "+file);
        }
        
    }

    public static void writeToFile(String file, String text) {
        writeToFile(file, new String[]{text});
    }
    
    public static void copy(File src, File dst) {
        try {
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
