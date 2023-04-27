package jp.gr.java_conf.nkzw.talendtools;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Talend itemファイルから生成するDBカラム抽象クラス
 */
public abstract class Column {
    private static final Logger LOGGER = LogManager.getLogger(Column.class);
    private String name;
    private String type;
    private int length;
    private int effectiveDigits;
    protected boolean nullable;

    public Column(String name, String type, int length, int effectiveDigits, boolean nullable) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.nullable = nullable;
        this.effectiveDigits = length;
        ColumnType colType = getByName(type);
        if ((colType.getMaxEffectiveDigits() > 0)
                && (this.length > colType.getMaxEffectiveDigits())) {
            this.effectiveDigits = colType.getMaxEffectiveDigits();
        }
    }

    public abstract String getCreateTableSql();

    public abstract List<ColumnType> getColumnTypeList();

    // 桁数関連
    public int getDigitsNum() {
        return getByName(this.type.toString()).getDigitsNum();
    }

    public ColumnType getByName(String type) {
        for (ColumnType colType : getColumnTypeList()) {
            if (type.equalsIgnoreCase(colType.getTypeName())) {
                return colType;
            }
        }
        LOGGER.error(type + " not match");
        return null;
        // throw new RuntimeException();
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
}
