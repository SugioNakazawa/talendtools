package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Talend itemファイルから生成する ORACLE用のDBカラムクラス
 */
public class OracleColumn extends Column {
    static final private List<ColumnType> COLUMN_TYPE_LIST = new ArrayList<ColumnType>(Arrays.asList(
            new ColumnType("NUMBER", 2, 0),
            new ColumnType("CHAR", 1, 0),
            new ColumnType("VARCHAR2", 1, 0),
            new ColumnType("NCHAR", 1, 0),
            new ColumnType("NVARCHAR2", 1, 0),
            new ColumnType("DATE", 0, 0),
            new ColumnType("TIMESTAMP", 1, 6),
            new ColumnType("RAW", 1, 0),
            new ColumnType("LONG", 0, 0),
            new ColumnType("LONG RAW", 0, 0),
            new ColumnType("BLOB", 0, 0),
            new ColumnType("CLOB", 0, 0),
            new ColumnType("NCLOB", 0, 0)
            ));

    public OracleColumn(String name, String type, int length, int precision, int ddlDigits, boolean nullable) {
        super(name, type, length, precision, ddlDigits, nullable);
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
            //  lengthのあるNUERIC
            if (getLength() > 0){
                if (getPrecision() > 0){
                    //  少数
                    colSb.append("(");
                    colSb.append(getLength());
                    colSb.append(", " + getPrecision() + ")");    
                }else{
                    // 整数
                    colSb.append("(");
                    colSb.append(getLength());
                    colSb.append(")");    
                }
            }
        } else if (getDigitsNum() == 1) {
        colSb.append("(");
        colSb.append(getDdlDigits());
        colSb.append(")");
        }
        // NOT NULL 制約
        if (!isNullable()) {
            colSb.append(" NOT NULL");
        }
        return colSb.toString();
    }
}
