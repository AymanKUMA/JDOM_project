import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class xml_to_java {
    public static void main(String[] args) throws JDOMException, IOException {
        //Load and read the XML file
        SAXBuilder Builder = new SAXBuilder();
        File sourceFile = new File("C:\\Users\\aymane\\Desktop\\myfiles\\studies\\univ\\Master\\M1\\S2\\MI\\project\\test.xml");
        Document doc = Builder.build(sourceFile);

        //Get the root of the file
        Element root = doc.getRootElement();


        //Get the list of class and association of the classes
        Element classes = root.getChild("classes");

        //get the list of class names
        List<Element> Class = classes.getChildren();

        List<Element> attributesList;
        List<Element> methodelist;
        List<Element> methodeparams;

        ArrayList<String> ConstAttributes = new ArrayList<String>();
        StringBuilder Constructor = new StringBuilder();

        for(int i=0 ;i<Class.size(); i++) {
            //Get the classes of the classes element
            Element currentClass = Class.get(i);

            //Get the visibility and Class name attributes
            Attribute visibility = currentClass.getAttribute("visibility");
            Attribute name = currentClass.getAttribute("name");

            StringBuilder main_code = new StringBuilder();
            main_code.append(visibility.getValue() +" class "+name.getValue()+" {\n\n\t//Attributes \n");

            //Get the attributes element
            Element attributes = currentClass.getChild("attributes");

            //Get the attribut of attributes element
            attributesList = attributes.getChildren();


            Constructor.append("\n\t//Constrecteur\n\tpublic "+name.getValue()+"(");
            for(int j=0; j<attributesList.size(); j++) {

                Element attribute = attributesList.get(j);
                Element attribute_Name = attribute.getChild("name");

                Attribute attribute_Visibility = attribute_Name.getAttribute("visibility");
                Attribute attribute_type = attribute_Name.getAttribute("type");
                Attribute attribute_multiplicity = attribute_Name.getAttribute("multiplicity");
                String multiplicityValue = attribute_multiplicity.getValue();
                //System.out.println("Attribute multiplicity is 1");

                main_code.append("\t"+attribute_Visibility.getValue()+" ");
                if ("*".equals(multiplicityValue))
                    main_code.append("List"+"<"+attribute_type.getValue()+">"+" ");
                else if ("1".equals(multiplicityValue))
                    main_code.append(attribute_type.getValue()+" ");
                else
                    main_code.append(attribute_type.getValue()+"["+ multiplicityValue +"]"+" ");

                main_code.append(attribute.getChildText("name")+";\n");
                if(j!=attributesList.size()-1) {
                    if ("*".equals(multiplicityValue))
                        Constructor.append("List" + "<" + attribute_type.getValue() + ">" + " ");
                    else if("1".equals(multiplicityValue))
                        Constructor.append(attribute_type.getValue() + " ");
                    else
                        Constructor.append(attribute_type.getValue()+ "[]" + " ");
                    Constructor.append(attribute.getChildText("name")+", ");
                }
                else {
                    if ("*".equals(multiplicityValue))
                        Constructor.append("List" + "<" + attribute_type.getValue() + ">" + " ");
                    else if("1".equals(multiplicityValue))
                        Constructor.append(attribute_type.getValue() + " ");
                    else
                        Constructor.append(attribute_type.getValue()+ "[]" + " ");
                    Constructor.append(attribute.getChildText("name") + "){\n");
                }

                ConstAttributes.add(attribute.getChildText("name"));

            }

            for(int constlist=0;constlist<ConstAttributes.size();constlist++) {
                Constructor.append("\t\tthis."+ConstAttributes.get(constlist)+" = "+ConstAttributes.get(constlist)+";\n");
            }

            Constructor.append("\t}\n");
            main_code.append(Constructor);
            Constructor = new StringBuilder();
            ConstAttributes.clear();

            //Get the methodes inside the methodes element
            Element methodes = currentClass.getChild("methods");
            methodelist = methodes.getChildren();
            for(int k=0;k<methodelist.size();k++) {

                Element method = methodelist.get(k);
                Element method_name = method.getChild("name");

                methodeparams = method.getChild("parameters").getChildren();
                if (methodeparams!=null){

                    main_code.append("\n\t"+method_name.getAttribute("visibility").getValue()+" "+method_name.getAttribute("return_type").getValue()+" "+method_name.getText()+"(");

                    for(int param=0;param<methodeparams.size();param++) {
                        Element parameter = methodeparams.get(param);
                        String parameter_Type = parameter.getChild("name").getText();
                        String parameter_Name = parameter.getChild("name").getAttribute("type").getValue();

                        if(!parameter_Type.isEmpty() && !parameter_Name.isEmpty()) {
                            if(param != methodeparams.size()-1) main_code.append(parameter_Type+" "+parameter_Name+", ");
                            else main_code.append(parameter_Type+" "+parameter_Name);
                        }
                    }
                    if(k!=methodelist.size()-1) main_code.append("){\n\t//method body\n\t}\n");
                    else main_code.append("){\n\t//method body\n\t}\n}\n");
                }
            }
            System.out.println(main_code);
        }
    }
}