package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.List;

/**
 * Talend itemファイルから生成するDBテーブル抽象クラス
 */
public abstract class Table {
    protected String tableName;
    protected String tableType;
    protected List<Column> columnList;
    protected String schemaName;

    public Table(String tableName, String tableType, String schemeName) {
        this.tableName = tableName;
        this.tableType = tableType;
        this.schemaName = schemeName;
        this.columnList = new ArrayList<Column>();
    }

    /**
     * カラム追加
     * 
     * @param name
     * @param Type
     * @param length
     * @param precision
     * @param ddlDigits
     * @param nullable
     */
    public abstract void addColumn(String name, String Type, int length, int precision, int ddlDigits, boolean nullable);

    /**
     * CREATE TABLE SQL
     * 
     * @return
     */
    public abstract String getCreateTableSql();

    /**
     * テキスト出力
     * 
     * @param indent
     * @param inc
     * @return
     */
    public String getString(String indent, String inc) {
        StringBuffer sb = new StringBuffer();
        sb.append(indent + "tableName: " + this.tableName
                + ", tableType: " + this.tableType
                + ", schemaName: " + this.schemaName + "\n");
        for (Column col : this.columnList) {
            sb.append(col.getString(indent + inc));
        }
        return sb.toString();
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

}
