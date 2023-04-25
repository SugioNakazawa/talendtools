package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Talend metadata database connectin Column define
 */
public class TlColumnOracle extends TlColumn {
    public TlColumnOracle(String name, String type, int length) {
        super(name, type, length);
    }

    /**
     * テキスト出力用
     * 
     * @param indent
     * @return
     */
    public String getString(String indent) {
        StringBuffer sb = new StringBuffer();
        sb.append(indent + "columnName: " + this.name
                + ", type: " + this.type
                + ", length: " + this.length
                + ", nullable: " + this.nullable
                + "\n");
        return sb.toString();
    }

    /**
     * DDLで指定する桁数の引数の数を型から取得。
     * 
     * @return
     */
    // private int getDigitsNum() {
    // return getByName(this.type).getDigitsNum();
    // }

    /**
     * CREATE TABLE 文のカラム定義。
     * 
     * @return
     */
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
            colSb.append(" NOT NULL ENABLE");
        }
        return colSb.toString();
    }

    public List<TlColumnType> getColumnTypeList() {
        return COLUMN_TYPE_LIST;
    }

    static final private List<TlColumnType> COLUMN_TYPE_LIST = new ArrayList<TlColumnType>(Arrays.asList(
            new TlColumnType("NUMBER", 0, 0),
            new TlColumnType("CHAR", 1, 7),
            new TlColumnType("VARCHAR2", 1, 0),
            new TlColumnType("NCHAR", 1, 0),
            new TlColumnType("NVARCHAR2", 1, 0),
            new TlColumnType("DATE", 0, 0),
            new TlColumnType("TIMESTAMP", 1, 6)));
}