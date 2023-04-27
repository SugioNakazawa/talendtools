package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Talend itemファイルから生成する MSSQL用のDBカラムクラス
 */
public class MssqlColumn extends Column {
    static final private List<ColumnType> COLUMN_TYPE_LIST = new ArrayList<ColumnType>(Arrays.asList(
        new ColumnType("INT", 0, 0),
        new ColumnType("BIT", 0, 0),
        new ColumnType("DECIMAL", 2, 0),
        new ColumnType("MONEY", 0, 0),
        new ColumnType("NUMERIC", 2, 0),
        new ColumnType("SMALLINT", 0, 0),
        new ColumnType("TINYINT", 0, 0),
        new ColumnType("FLOAT", 0, 0),
        new ColumnType("REAL", 0, 0),
        new ColumnType("DATE", 0, 0),
        new ColumnType("DATETIME2", 1, 7),
        new ColumnType("DATETIME", 0, 0),
        new ColumnType("DATETIMEOFFSET", 1, 7),
        new ColumnType("SMALLDATETIME", 0, 0),
        new ColumnType("TIME", 1, 7),
        new ColumnType("CHAR", 1, 7),
        new ColumnType("TEXT", 0, 0),
        new ColumnType("VARCHAR", 1, 0),
        new ColumnType("NCHAR", 1, 0),
        new ColumnType("NVARCHAR", 1, 0),
        new ColumnType("INT IDENTITY", 2, 0),
        new ColumnType("BIGINT", 0, 0),
        new ColumnType("IMAGE", 0, 0)
        ));

    public MssqlColumn(String name, String type, int length, int effectiveDigits, boolean nullable) {
        super(name, type, length, effectiveDigits, nullable);
    }

    @Override
    public List<ColumnType> getColumnTypeList() {
        return MssqlColumn.COLUMN_TYPE_LIST;
    }

    @Override
    public String getCreateTableSql() {
        StringBuffer colSb = new StringBuffer("\t");
        // カラム名
        colSb.append("[" + getName() + "]");
        colSb.append(" [" + getType() + "]");
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
            colSb.append(" NOT");
        }
        colSb.append(" NULL");
        return colSb.toString();
    }
}
