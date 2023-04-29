package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.List;

/**
 * Talend itemファイルから生成する MSSQL用のDBテーブルクラス
 */
public class MssqlTable extends Table {

    public MssqlTable(String name, String type, String schemeName) {
        super(name, type, schemeName);
    }

    @Override
    public void addColumn(String name, String type, int length, int precision, int ddlDigits, boolean nullable) {
        this.getColumnList().add(new MssqlColumn(name, type, length, precision, ddlDigits, nullable));
    }

    @Override
    public String getCreateTableSql() {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE [" + this.schemaName + "].["
                + this.tableName + "](\n");
        List<String> columns = new ArrayList<String>();
        for (Column col : this.columnList) {
            columns.add(col.getCreateTableSql());
        }
        sb.append(String.join(",\n", columns));
        sb.append("\n)\n");
        sb.append("GO\n\n");
        return sb.toString();
    }

}
