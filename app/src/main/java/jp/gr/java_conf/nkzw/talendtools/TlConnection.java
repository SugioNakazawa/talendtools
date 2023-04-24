package jp.gr.java_conf.nkzw.talendtools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Talend メタデータ：DB接続をパースするクラス
 */
public class TlConnection {
    private static final Logger LOGGER = LogManager.getLogger(TlConnection.class);

    private String itemFileName;
    private NamedNodeMap databaseConnectionMap;
    private List<TlTable> tableList;

    /**
     * parse from connection.item file
     * 
     * @param connectinsFile
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    static public TlConnection buildConnection(File connectinsFile)
            throws ParserConfigurationException, SAXException, IOException {

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(connectinsFile);
        Element elementList = document.getDocumentElement();
        NodeList dbcon = elementList.getElementsByTagName("TalendMetadata:DatabaseConnection");
        if (dbcon.getLength() != 1) {
            // 複数ある場合はエラー
            LOGGER.error("TalendMetadata:DatabaseConnection must be One!");
            new RuntimeException();
        }
        // create TlConnectin
        TlConnection connection = new TlConnection(connectinsFile.getName());
        Element connectionNode = (Element) dbcon.item(0);
        connection.setDbConnection(connectionNode.getAttributes());

        NodeList nodes = elementList.getElementsByTagName("ownedElement");
        String schemaName = "";

        for (int i = 0; i < nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            if (!node.hasAttribute("tableType")) {
                schemaName = node.getAttribute("name");
                continue;
            }
            // create table
            TlTable table = new TlTable(
                    node.getAttributes().getNamedItem("name").getTextContent(),
                    node.getAttributes().getNamedItem("tableType").getTextContent(),
                    schemaName);

            NodeList elmParaList = node.getElementsByTagName("feature");
            for (int j = 0; j < elmParaList.getLength(); j++) {
                Node elmPara = elmParaList.item(j);
                // create column
                TlColumn column = new TlColumn(
                        elmPara.getAttributes().getNamedItem("name").getTextContent(),
                        elmPara.getAttributes().getNamedItem("sourceType").getTextContent());
                // length
                if (elmPara.getAttributes().getNamedItem("length") != null) {
                    column.setLength(Integer.parseInt(elmPara.getAttributes().getNamedItem("length").getTextContent()));
                } else {
                    LOGGER.error("lenght is null. use 999. connection=" + connectinsFile.getName()
                            + " table=" + node.getAttribute("name")
                            + " columnName=" + elmPara.getAttributes().getNamedItem("name").getTextContent()
                            + " columnType=" + elmPara.getAttributes().getNamedItem("sourceType").getTextContent());
                }
                // not null
                if (elmPara.getAttributes().getNamedItem("nullable") != null) {
                    column.setNullable(
                            Boolean.parseBoolean(
                                    elmPara.getAttributes().getNamedItem("nullable").getTextContent()));
                } else {
                    column.setNullable(true);

                }

                table.addColumn(column);
            }
            connection.addTable(table);
        }
        return connection;
    }

    public String getItemFileName() {
        return itemFileName;
    }

    public NamedNodeMap getDatabaseConnectionMap() {
        return databaseConnectionMap;
    }

    public List<TlTable> getTableList() {
        return tableList;
    }

    public void addTable(TlTable table) {
        this.tableList.add(table);
    }

    private void setDbConnection(NamedNodeMap attributes) {
        this.databaseConnectionMap = attributes;
    }

    public TlConnection(String itemFileName) {
        this.itemFileName = itemFileName;
        this.tableList = new ArrayList<TlTable>();
    }

    /**
     * テキスト出力用
     * 
     * @param indent
     * @param inc
     * @return
     */
    public String getString(String indent, String inc) {
        StringBuffer sb = new StringBuffer();
        sb.append(indent + "itemFile: " + this.itemFileName
                + ", name: " + this.databaseConnectionMap.getNamedItem("name").getTextContent() + "\n");
        for (TlTable table : this.tableList) {
            sb.append(table.getString(indent + inc, inc));
        }
        return sb.toString();
    }
}
