import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ClassBuilder {
    public static void buildClasses(Document document, String javaFolderPath) throws IOException, JDOMException {
        // Get the root of the file
        Element root = document.getRootElement();

        // Get the list of class and association of the classes
        Element classes = root.getChild("classes");

        // Get the list of class names
        List<Element> classList = classes.getChildren();

        for (Element currentClass : classList) {
            // Get the class name
            Attribute name = currentClass.getAttribute("name");
            String className = name.getValue();

            // Get the methods
            Element methods = currentClass.getChild("methods");

            // String Builder for the class code
            StringBuilder classCode = new StringBuilder();

            //inheritance
            boolean isInheritance = false;

            classCode.append(currentClass.getAttributeValue("visibility"))
                    .append(" class ")
                    .append(className);

            // Check if there is any inheritance
            Element associations = currentClass.getChild("associations");
            Element inheritanceElement = associations.getChild("inheritance");
            if (inheritanceElement != null) {
                String inheritanceClassName = inheritanceElement.getTextTrim();
                if (!inheritanceClassName.isEmpty()) {
                    classCode.append(" extends ").append(inheritanceClassName);
                    isInheritance = true;
                }
            }

            classCode.append(" {\n");

            //Here we append the attributes, composition, aggregations, and the methods

            classCode.append("\n\n\t//Attributes \n");
            classCode.append(AttributesBuilder.buildAttributes(currentClass));

            classCode.append("\n\n\t//compositions\n");
            classCode.append(CompositionBuilder.compositionAttributeBuilder(associations));

            classCode.append("\n\n\t//aggregations\n");
            classCode.append(AggregationBuilder.aggregationAttributeBuilder(associations));

            //the constructor
            classCode.append("\n\t//the Constructors\n");
            classCode.append(MethodsBuilder.constructorBuilder(currentClass, className, isInheritance));

            //attributes getters
            classCode.append("\n\n\t//getters \n");
            classCode.append(AttributesBuilder.buildAttributesGetters(currentClass));

            //attributes setters
            classCode.append("\n\n\t//setters \n");
            classCode.append(AttributesBuilder.buildAttributesSetters(currentClass));

            //Methods
            classCode.append("\n\n\t//methods\n");
            classCode.append(MethodsBuilder.methodsBuilder(methods));

            classCode.append("}\n");

            // Save the class code into a .java file
            String javaFilePath = javaFolderPath + "/" + className + ".java";
            FileWriter writer = new FileWriter(javaFilePath);
            writer.write(classCode.toString());
            writer.close();
        }
    }
}
