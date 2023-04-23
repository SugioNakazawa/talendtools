package jp.gr.java_conf.nkzw.talendtools;

import java.math.BigDecimal;

/**
 * Talend metadata database connectin Column define
 */
public class TlColumn {
    private String name;
    private String type;
    private BigDecimal length;
    private boolean nullable;

    public TlColumn(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

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
     * DDLで桁数指定の引数の数。
     * 
     * @return
     */
    public int getDigitsNum() {
        if ("decimal".equalsIgnoreCase(this.type)
                || "numeric".equalsIgnoreCase(this.type)) {
            return 2;
        }
        if ("datetime2".equalsIgnoreCase(this.type)
                || "datetimeoffset".equalsIgnoreCase(this.type)
                || "time".equalsIgnoreCase(this.type)
                || "char".equalsIgnoreCase(this.type)
                || "varchar".equalsIgnoreCase(this.type)
                || "nchar".equalsIgnoreCase(this.type)
                || "nvarchar".equalsIgnoreCase(this.type)) {
            return 1;
        }
        return 0;
    }

}
