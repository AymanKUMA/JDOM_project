import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;

public class AttributesBuilder {

    public static StringBuilder buildAttributes(Element currentClass){

        StringBuilder attributesCode = new StringBuilder();
        //Get the attributes of the current class
        List<Element> attributesList;

        Element attributes = currentClass.getChild("attributes");
        attributesList = attributes.getChildren();

        //Loop in the list of attributes
        for (Element attribute : attributesList) {
            //Get the attribute number i
            Element attribute_Name = attribute.getChild("name");

            //Get each of the attribute Visibility, Type and Multiplicity
            Attribute attribute_Visibility = attribute_Name.getAttribute("visibility");
            Attribute attribute_type = attribute_Name.getAttribute("type");
            Attribute attribute_multiplicity = attribute_Name.getAttribute("multiplicity");

            //Append the visibility first to the code
            attributesCode.append("\t" + attribute_Visibility.getValue() + " ");

            //Get the multiplicity value to test upon it
            String multiplicityValue = attribute_multiplicity.getValue();

            //The attribute either becomes a List or an Array based on it's multiplicity
            if ("*".equals(multiplicityValue)) {
                attributesCode.append("List" + "<" + attribute_type.getValue().toUpperCase().charAt(0) +
                        attribute_type.getValue().substring(1)
                        + ">" + " ");
                attributesCode.append(attribute.getChildText("name") + ";\n");
            }
            else if ("1".equals(multiplicityValue)) {
                attributesCode.append(attribute_type.getValue() + " ");
                attributesCode.append(attribute.getChildText("name") + ";\n");
            }
            else {
                attributesCode.append(attribute_type.getValue() + "[" + "] ");
                attributesCode.append(attribute.getChildText("name") + " = new " +
                        attribute_type.getValue() + "[" + multiplicityValue + "]" + ";\n");
            }
        }
        return attributesCode;
    }

    public static StringBuilder buildAttributesGetters(Element currentClass) throws IOException, JDOMException {
        StringBuilder gettersCode = new StringBuilder();

        List<Element> attributesList;

        Element attributes = currentClass.getChild("attributes");
        attributesList = attributes.getChildren();

        //Loop in the list of attributes
        for (Element attribute : attributesList) {
            //this code returns the i getter method for the attribute i
            gettersCode.append(MethodsBuilder.getterBuilder(attribute));
        }
        return gettersCode;
    }

    public static StringBuilder buildAttributesSetters(Element currentClass) throws IOException, JDOMException {
        StringBuilder settersCode = new StringBuilder();

        List<Element> attributesList;

        Element attributes = currentClass.getChild("attributes");
        attributesList = attributes.getChildren();

        //Loop in the list of attributes
        for (Element attribute : attributesList) {
            //this code returns the i getter method for the attribute i
            settersCode.append(MethodsBuilder.setterBuilder(attribute));
        }
        return settersCode;
    }
}
