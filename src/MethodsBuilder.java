import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MethodsBuilder {
    //methods builder
    public static StringBuilder methodsBuilder(Element methods){
        StringBuilder methodsCode = new StringBuilder();

        List<Element> methodsList = methods.getChildren();
        for (Element method : methodsList ){
            Element method_Name = method.getChild("name");
            //Get each of the attribute Visibility, Type and Multiplicity
            Attribute method_type = method_Name.getAttribute("type");
            Attribute method_visibility = method_Name.getAttribute("visibility");
            Attribute method_multiplicity = method_Name.getAttribute("multiplicity");

            methodsCode.append("\t" + method_visibility.getValue() + " ");

            //Get the multiplicity value to test upon it
            String multiplicityValue = method_multiplicity.getValue();

            //The attribute either becomes a List or an Array based on it's multiplicity
            if ("*".equals(multiplicityValue))
                methodsCode.append("List" + "<" + method_type.getValue().toUpperCase().charAt(0) +
                        method_type.getValue().substring(1) +
                        ">" + " ");
            else if ("1".equals(multiplicityValue))
                methodsCode.append(method_type.getValue() + " ");
            else
                methodsCode.append(method_type.getValue()
                        + "[]" + " ");

            Element method_arguments = method.getChild("arguments");
            //Append the visibility first to the code
            methodsCode.append(method.getChildText("name")).
                    append("(").append(argumentsBuilder(method_arguments)).
                    append("){\n").
                    append("\t\t//Method's body make sure to add the necessary code").
                    append("\n\t}\n");
        }
        return methodsCode;
    }

    public static StringBuilder argumentsBuilder(Element arguments){
        StringBuilder argumentsCode = new StringBuilder();

        List<Element> argumentsList = arguments.getChildren();
        for (int i = 0; i < argumentsList.size(); i++) {
            Element argument = argumentsList.get(i);
            Attribute argument_type = argument.getAttribute("type");
            Attribute argument_multiplicity = argument.getAttribute("multiplicity");

            // Get the multiplicity value to test upon it
            String multiplicityValue = argument_multiplicity.getValue();

            if ("*".equals(multiplicityValue)) {
                argumentsCode.append("List<" + argument_type.getValue().toUpperCase().charAt(0) +
                        argument_type.getValue().substring(1)
                        + "> ");
                argumentsCode.append(argument.getText());
            } else if ("1".equals(multiplicityValue)) {
                argumentsCode.append(argument_type.getValue() + " ");
                argumentsCode.append(argument.getText());
            } else {
                argumentsCode.append(argument_type.getValue() + "[] ");
                argumentsCode.append(argument.getText());
            }

            if (i < argumentsList.size() - 1) {
                argumentsCode.append(", ");
            }
        }

        return argumentsCode;
    }

    //constructor builder
    public static StringBuilder constructorBuilder(Element currentClass,Attribute name, Boolean isInheritance) throws IOException, JDOMException{
        StringBuilder constructorCode = new StringBuilder();
        Element aggregations = currentClass.getChild("associations").
                getChild("aggregations");

        constructorCode.append("\npublic ").
                append(name).append("(");
        if (aggregations != null){
            constructorCode.append(argumentsBuilder(aggregations)).
                    append("){\n");
        }

        return constructorCode;
    }
    public static StringBuilder getterBuilder(Element attribute) throws IOException, JDOMException {
        StringBuilder getterCode = new StringBuilder();

        Element attribute_Name = attribute.getChild("name");
        //Get each of the attribute Visibility, Type and Multiplicity
        Attribute attribute_type = attribute_Name.getAttribute("type");
        Attribute attribute_multiplicity = attribute_Name.getAttribute("multiplicity");

        getterCode.append("\n\t//The " + attribute.getChildText("name")
                + "'s getter\n");
        getterCode.append("\tpublic ");

        //Get the multiplicity value to test upon it
        String multiplicityValue = attribute_multiplicity.getValue();

        //The attribute either becomes a List or an Array based on it's multiplicity
        if ("*".equals(multiplicityValue))
            getterCode.append("List" + "<" + attribute_type.getValue().toUpperCase().charAt(0) +
                    attribute_type.getValue().substring(1) +
                    ">" + " ");
        else if ("1".equals(multiplicityValue))
            getterCode.append(attribute_type.getValue() + " ");
        else
            getterCode.append(attribute_type.getValue()
                    + "[]" + " ");

        //Append the visibility first to the code
        getterCode.append("get" +
                attribute.getChildText("name").toUpperCase().charAt(0) +
                attribute.getChildText("name").substring(1) + "(){\n" +
                "\t\treturn " + attribute.getChildText("name") + ";\n\t}\n");

        return getterCode;
    }

    public static StringBuilder setterBuilder(Element attribute) throws IOException, JDOMException {
        StringBuilder setterCode = new StringBuilder();

        Element attributeNameElement = attribute.getChild("name");
        Attribute attributeType = attributeNameElement.getAttribute("type");
        Attribute attributeMultiplicity = attributeNameElement.getAttribute("multiplicity");

        setterCode.append("\n\t// The ").append(attribute.getChildText("name"))
                .append("'s setter\n");
        setterCode.append("\tpublic void ").append("set")
                .append(attribute.getChildText("name").toUpperCase().charAt(0))
                .append(attribute.getChildText("name").substring(1)).append("(");

        String multiplicityValue = attributeMultiplicity.getValue();

        if ("*".equals(multiplicityValue)) {
            setterCode.append("List").append("<")
                    .append(attributeType.getValue().toUpperCase().charAt(0))
                    .append(attributeType.getValue().substring(1)).append(">");
        } else if ("1".equals(multiplicityValue)) {
            setterCode.append(attributeType.getValue());
        } else {
            setterCode.append(attributeType.getValue()).append("[]");
        }

        setterCode.append(" ").append(attribute.getChildText("name")).append("){\n");
        setterCode.append(generateMethodBody(attribute, attributeMultiplicity));
        setterCode.append("\t}\n");

        return setterCode;
    }

    private static String generateMethodBody(Element attribute, Attribute attributeMultiplicity) {
        StringBuilder bodyCode = new StringBuilder();

        String multiplicityValue = attributeMultiplicity.getValue();

        if ("*".equals(multiplicityValue)) {
            // Handle List type
            bodyCode.append("\t\tthis.").append(attribute.getChildText("name"))
                    .append(" = ").append(attribute.getChildText("name"))
                    .append(";\n");
        } else if ("1".equals(multiplicityValue)) {
            // Handle single object type
            bodyCode.append("\t\tthis.").append(attribute.getChildText("name"))
                    .append(" = ").append(attribute.getChildText("name"))
                    .append(";\n");
        } else {
            // Handle array type
            bodyCode.append("\t\tif(").append(attribute.getChildText("name"))
                    .append(".length == ").append(multiplicityValue).append("){\n")
                    .append("\t\t\tthis.").append(attribute.getChildText("name"))
                    .append(" = ").append(attribute.getChildText("name"))
                    .append(";\n")
                    .append("\t\t} else {\n")
                    .append("\t\t\tthrow new IllegalArgumentException")
                    .append("(\"The provided array must have exactly ").append(multiplicityValue)
                    .append(" elements.\");\n")
                    .append("\t\t}\n");
        }

        return bodyCode.toString();
    }

}
