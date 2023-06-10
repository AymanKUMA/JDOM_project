import org.jdom2.Attribute;
import org.jdom2.Element;

import java.util.List;

public class CompositionBuilder {
    public static StringBuilder compositionAttributeBuilder(Element associations) {
        StringBuilder compositionAttributeCode = new StringBuilder();

        // Get the compositions of the current class
        Element compositions = associations.getChild("compositions");

        if (compositions != null) {
            List<Element> compositionAttributeList = compositions.getChildren();
            // Loop through the list of attributes
            for (Element compositionAttribute : compositionAttributeList) {
                Element compositionName = compositionAttribute.getChild("name");
                if (compositionName != null) {
                    // Get each of the compositionAttribute Type and Multiplicity
                    Attribute compositionType = compositionName.getAttribute("type");
                    Attribute compositionMultiplicity = compositionName.getAttribute("multiplicity");

                    // Append the visibility first to the code
                    compositionAttributeCode.append("\tprivate ");

                    // Get the multiplicity value to test upon it
                    String multiplicityValue = compositionMultiplicity.getValue();

                    // Determine whether the compositionAttribute becomes a List or an Array based on its multiplicity
                    if ("*".equals(multiplicityValue)) {
                        compositionAttributeCode.append("List<" + compositionType.getValue().toUpperCase().charAt(0) +
                                compositionType.getValue().substring(1) +
                                "> ");
                        compositionAttributeCode.append(compositionName.getValue() + ";\n");
                    } else if ("1".equals(multiplicityValue)) {
                        compositionAttributeCode.append(compositionType.getValue() + " ");
                        compositionAttributeCode.append(compositionName.getValue() + ";\n");
                    } else {
                        compositionAttributeCode.append(compositionType.getValue() + "[] ");
                        compositionAttributeCode.append(compositionName.getValue() + " = new " +
                                compositionType.getValue() + "[" + multiplicityValue + "];\n");
                    }
                }
            }
        }

        if (compositionAttributeCode.length() == 0)
            compositionAttributeCode.append("\t//No compositions\n");

        return compositionAttributeCode;
    }
}
