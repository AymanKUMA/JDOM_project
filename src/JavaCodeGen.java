import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaCodeGen {
    private final String filePath;

    // Constructor has the path as an argument
    public JavaCodeGen(String path) {
        this.filePath = path;
    }

    // Method codeRunner returns the finalCode
    public void codeRunner() throws IOException, JDOMException {
        // Load the XML file
        SAXBuilder builder = new SAXBuilder();
        File sourceFile = new File(this.filePath);
        Document doc = builder.build(sourceFile);

        String xmlFileName = sourceFile.getName();
        String folderName = xmlFileName.replace(".xml", "");
        String javaFolderPath = sourceFile.getParent() + File.separator + folderName;

        // Create the folder if it doesn't exist
        Path javaFolderPathObj = Paths.get(javaFolderPath);
        if (!Files.exists(javaFolderPathObj)) {
            Files.createDirectory(javaFolderPathObj);
        }

        // Build classes and save them individually
        ClassBuilder.buildClasses(doc, javaFolderPath);
    }

    public static void main(String[] args) {
        try {
            String path = "C:\\Users\\aymane\\Desktop\\myfiles\\studies\\univ\\Master\\M1\\S2\\MI\\JDOM_Project\\test.xml";
            JavaCodeGen code = new JavaCodeGen(path);
            code.codeRunner();
            System.out.println("Java code generated successfully.");
        } catch (IOException e) {
            // Exception handling for IOException
            System.err.println("An IO error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (JDOMException e) {
            // Exception handling for JDOMException
            System.err.println("An JDOM error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
