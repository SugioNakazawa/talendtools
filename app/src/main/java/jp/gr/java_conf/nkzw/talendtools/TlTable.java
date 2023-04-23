package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.List;

/**
 * Talend metadata database connectin Table define
 */
public class TlTable {
    private String tableName;
    private String tableType;
    private List<TlColumn> columnList;
    private String schemaName;

    public TlTable(String tableName, String tableType, String schemeName) {
        this.tableName = tableName;
        this.tableType = tableType;
        this.schemaName = schemeName;
        this.columnList = new ArrayList<TlColumn>();
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getTableType() {
        return tableType;
    }

    public List<TlColumn> getColumnList() {
        return columnList;
    }

    public void addColumn(TlColumn column) {
        this.columnList.add(column);
    }

    public String getString(String indent, String inc) {
        StringBuffer sb = new StringBuffer();
        sb.append(indent + "tableName: " + this.tableName
                + ", tableType: " + this.tableType
                + ", schemaName: " + this.schemaName + "\n");
        for (TlColumn col : this.columnList) {
            sb.append(col.getString(indent + inc));
        }
        return sb.toString();
    }

    public String getCreateTableSql() {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE [" + this.schemaName + "].["
                + this.tableName + "](\n");
        List<String> columns = new ArrayList<String>();
        for (TlColumn col : this.columnList) {
            StringBuffer colSb = new StringBuffer("\t");
            // カラム名
            colSb.append("[" + col.getName() + "]");
            colSb.append(" [" + col.getType() + "]");
            // 桁数
            if (col.getDigitsNum() == 2) {
                colSb.append("(");
                colSb.append(col.getLength());
                colSb.append(", 0)");
            } else if (col.getDigitsNum() == 1) {
                colSb.append("(");
                colSb.append(col.getLength());
                colSb.append(")");
            }
            // NOT NULL 制約
            if (!col.isNullable()) {
                colSb.append(" NOT");
            }
            colSb.append(" NULL");
            columns.add(colSb.toString());
        }
        sb.append(String.join(",\n", columns));
        sb.append("\n)\n");
        sb.append("GO\n");
        return sb.toString();
    }
}
