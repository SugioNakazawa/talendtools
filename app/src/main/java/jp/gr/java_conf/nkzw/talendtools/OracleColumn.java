package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Talend itemファイルから生成する ORACLE用のDBカラムクラス
 */
public class OracleColumn extends Column {
    static final private List<ColumnType> COLUMN_TYPE_LIST = new ArrayList<ColumnType>(Arrays.asList(
            new ColumnType("NUMBER", 0, 0),
            new ColumnType("CHAR", 1, 0),
            new ColumnType("VARCHAR2", 1, 0),
            new ColumnType("NCHAR", 1, 0),
            new ColumnType("NVARCHAR2", 1, 0),
            new ColumnType("DATE", 0, 0),
            new ColumnType("TIMESTAMP", 1, 6)));

    public OracleColumn(String name, String type, int length, int effectiveDigits, boolean nullable) {
        super(name, type, length, effectiveDigits, nullable);
    }

    @Override
    public List<ColumnType> getColumnTypeList() {
        return OracleColumn.COLUMN_TYPE_LIST;
    }

    @Override
    public String getCreateTableSql() {
        StringBuffer colSb = new StringBuffer("\t");
        // カラム名
        colSb.append("\"" + getName() + "\"");
        colSb.append(" " + getType());
        // 桁数
        if (getDigitsNum() == 2) {
        colSb.append("(");
        colSb.append(getEffectiveDigits());
        colSb.append(", 0)");
        } else if (getDigitsNum() == 1) {
        colSb.append("(");
        colSb.append(getEffectiveDigits());
        colSb.append(")");
        }
        // NOT NULL 制約
        if (!isNullable()) {
            colSb.append(" NOT NULL");
        }
        return colSb.toString();
    }
}
