package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.List;
/**
 * Talend itemファイルから生成する ORACLE用のDBテーブルクラス
 */
public class OracleTable extends Table {

    public OracleTable(String name, String type, String schemeName) {
        super(name, type, schemeName);
    }

    @Override
    public void addColumn(String name, String type, int length, int effectiveDigits, boolean nullable) {
        this.getColumnList().add(new OracleColumn(name, type, length, effectiveDigits, nullable));
    }

    @Override
    public String getCreateTableSql() {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE "
                // + " [" + this.schemaName + "].["
                + this.tableName + "(\n");
        List<String> sqlColumns = new ArrayList<String>();
        for (Column col : this.columnList) {
            sqlColumns.add(col.getCreateTableSql());
        }
        sb.append(String.join(",\n", sqlColumns));
        sb.append("\n);\n\n");
        return sb.toString();
    }

}
