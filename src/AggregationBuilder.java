import org.jdom2.Attribute;
import org.jdom2.Element;

import java.util.List;

public class AggregationBuilder {

    public static StringBuilder aggregationAttributeBuilder(Element associations){
        StringBuilder aggregationAttributeCode = new StringBuilder();

        // Get the compositions of the current class
        Element aggregations = associations.getChild("aggregations");

        if (aggregations != null) {
            List<Element> compositionAttributeList = aggregations.getChildren();
            // Loop through the list of attributes
            for (Element compositionAttribute : compositionAttributeList) {
                Element compositionName = compositionAttribute.getChild("name");
                if (compositionName != null) {
                    // Get each of the compositionAttribute Type and Multiplicity
                    Attribute compositionType = compositionName.getAttribute("type");
                    Attribute compositionMultiplicity = compositionName.getAttribute("multiplicity");

                    // Append the visibility first to the code
                    aggregationAttributeCode.append("\tprivate ");

                    // Get the multiplicity value to test upon it
                    String multiplicityValue = compositionMultiplicity.getValue();

                    // Determine whether the compositionAttribute becomes a List or an Array based on its multiplicity
                    if ("*".equals(multiplicityValue)) {
                        aggregationAttributeCode.append("List<" + compositionType.getValue().toUpperCase().charAt(0) +
                                compositionType.getValue().substring(1) +
                                "> ");
                        aggregationAttributeCode.append(compositionName.getValue() + ";\n");
                    } else if ("1".equals(multiplicityValue)) {
                        aggregationAttributeCode.append(compositionType.getValue() + " ");
                        aggregationAttributeCode.append(compositionName.getValue() + ";\n");
                    } else {
                        aggregationAttributeCode.append(compositionType.getValue() + "[] ");
                        aggregationAttributeCode.append(compositionName.getValue() + " = new " +
                                compositionType.getValue() + "[" + multiplicityValue + "];\n");
                    }
                }
            }
        }

        if (aggregationAttributeCode.length() == 0)
            aggregationAttributeCode.append("\t//No aggregations\n");

        return aggregationAttributeCode;
    }

}
