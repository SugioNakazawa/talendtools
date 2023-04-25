package jp.gr.java_conf.nkzw.talendtools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Talend metadata database connectin Column define
 */
public class TlColumn {
    private static final Logger LOGGER = LogManager.getLogger(TlColumn.class);

    protected String name;
    protected String type;
    protected int length;
    // 有効桁数
    protected int effectiveDigits;
    protected boolean nullable;

    static public TlColumn createColumn(String name, String type, int length, String productId) {
        TlColumn tlColumn;
        if ("ORACLE".equals(productId)) {
            tlColumn = new TlColumnOracle(name, type, length);
        } else {
            tlColumn = new TlColumn(name, type, length);
        }
        tlColumn.resetEffectiveDigits();
        return tlColumn;
    }

    protected TlColumn(String name, String type, int length) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.effectiveDigits = length;
    }

    /**
     * カラム型に応じて有効桁数（DDLでの指定桁数）を設定。
     */
    protected void resetEffectiveDigits() {
        TlColumnType colType = getByName(this.type, getColumnTypeList());
        if ((colType.getMaxEffectiveDigits() > 0)
                && (this.length > colType.getMaxEffectiveDigits())) {
            this.effectiveDigits = colType.getMaxEffectiveDigits();
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public int getEffectiveDigits() {
        return effectiveDigits;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
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
    protected int getDigitsNum() {
        return getByName(type, getColumnTypeList()).getDigitsNum();
    }

    /**
     * CREATE TABLE 文のカラム定義。
     * 
     * @return
     */
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

    public List<TlColumnType> getColumnTypeList() {
        return COLUMN_TYPE_LIST;
    }

    static final private List<TlColumnType> COLUMN_TYPE_LIST = new ArrayList<TlColumnType>(Arrays.asList(
            new TlColumnType("INT", 0, 0),
            new TlColumnType("BIT", 0, 0),
            new TlColumnType("DECIMAL", 2, 0),
            new TlColumnType("MONEY", 0, 0),
            new TlColumnType("NUMERIC", 2, 0),
            new TlColumnType("SMALLINT", 0, 0),
            new TlColumnType("TINYINT", 0, 0),
            new TlColumnType("FLOAT", 0, 0),
            new TlColumnType("REAL", 0, 0),
            new TlColumnType("DATE", 0, 0),
            new TlColumnType("DATETIME2", 1, 7),
            new TlColumnType("DATETIME", 0, 0),
            new TlColumnType("DATETIMEOFFSET", 1, 7),
            new TlColumnType("SMALLDATETIME", 0, 0),
            new TlColumnType("TIME", 1, 7),
            new TlColumnType("CHAR", 1, 7),
            new TlColumnType("TEXT", 0, 0),
            new TlColumnType("VARCHAR", 1, 0),
            new TlColumnType("NCHAR", 1, 0),
            new TlColumnType("NVARCHAR", 1, 0)));

    static public TlColumnType getByName(String type, List<TlColumnType> columnTypes) {
        for (TlColumnType colType : columnTypes) {
            if (type.equalsIgnoreCase(colType.getTypeName())) {
                return colType;
            }
        }
        LOGGER.error(type + " not match");
        return null;
        // throw new RuntimeException();
    }
}