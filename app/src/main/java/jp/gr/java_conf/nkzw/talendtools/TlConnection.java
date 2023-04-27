package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;

/**
 * Talend メタデータ：DB接続をパースするクラス
 */
public class TlConnection {

    private String itemFileName;
    private String productId;
    private NamedNodeMap databaseConnectionMap;
    private List<Table> tableList;

    public TlConnection(String itemFileName, String productId) {
        this.itemFileName = itemFileName;
        this.productId = productId;
        this.tableList = new ArrayList<Table>();
    }

    public String getProductId() {
        return productId;
    }

    public String getItemFileName() {
        return itemFileName;
    }

    /**
     * itemファイルのXML要素をそのまま保持。
     * 
     * @return
     */
    public NamedNodeMap getDatabaseConnectionMap() {
        return databaseConnectionMap;
    }

    public List<Table> getTableList() {
        return tableList;
    }

    public void addTable(Table table) {
        this.tableList.add(table);
    }

    void setDbConnection(NamedNodeMap attributes) {
        this.databaseConnectionMap = attributes;
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
        for (Table table : this.tableList) {
            sb.append(table.getString(indent + inc, inc));
        }
        return sb.toString();
    }
}
