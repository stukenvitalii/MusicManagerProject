import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLfilesHandler {
    public static void importXML() throws ParserConfigurationException, IOException, SAXException {
        Stage chooseFileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        File xml = fileChooser.showOpenDialog(chooseFileStage);

        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = dBuilder.parse(xml);

        document.getDocumentElement().normalize();

        NodeList groupsNodeList = document.getElementsByTagName("group");

        for (int i = 0; i < groupsNodeList.getLength(); i++) {
            Node elem = groupsNodeList.item(i);
            NamedNodeMap attributes = elem.getAttributes();
            String name = attributes.getNamedItem("name").getNodeValue();
            String yearOfFoundation = attributes.getNamedItem("yearOfFoundation").getNodeValue();
            String genre = attributes.getNamedItem("genre").getNodeValue();
            String place = attributes.getNamedItem("place").getNodeValue();

            Group group = new Group();
            group.setName(name);
            group.setYearOfFoundation(Integer.valueOf(yearOfFoundation));
            group.setMainGenre(genre);
            group.setPlaceInChart(Integer.valueOf(place));
            DataBaseHandler.saveGroupToDB(group);
        }
    }
    public static void exportXML(List<Group> groupsSaved) throws ParserConfigurationException, IOException, TransformerException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        Node groupsList = document.createElement("groups");
        document.appendChild(groupsList);
        for (Group group : groupsSaved) {
            Element groupEl = document.createElement("group");
            groupsList.appendChild(groupEl);
            groupEl.setAttribute("name", group.getName());
            groupEl.setAttribute("yearOfFoundation", group.getYearOfFoundation().toString());
            groupEl.setAttribute("genre", group.getMainGenre());
            groupEl.setAttribute("place", group.getPlaceInChart().toString());
        }
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        try(FileWriter fileWriter = new FileWriter("groups.xml")) {
            trans.transform(new DOMSource(document), new StreamResult(fileWriter));
        }
    }
}
