package jp.gr.java_conf.nkzw.talendtools;

/**
 * Talend metadata database connectin Column define
 */
public class TlColumn {
    private String name;
    private ColumnType type;
    private int length;
    // 有効桁数
    private int effectiveDigits;
    private boolean nullable;

    public TlColumn(String name, String type) {
        this.name = name;
        this.type = getByName(type);
    }

    public String getName() {
        return name;
    }

    public ColumnType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public int getEffectiveDigits() {
        return effectiveDigits;
    }

    /**
     * 型の長さをセット。特定の方は有効桁数を固定。
     * 
     * @param length
     */
    public void setLength(int length) {
        // 長さ
        this.length = length;
        // 有効桁数は特定の型では固定
        switch (type) {
            case DATETIME2:
            case DATETIMEOFFSET:
            case TIME:
                if (length > 7) {
                    this.effectiveDigits = 7;
                } else {
                    this.effectiveDigits = length;
                }
                break;
            default:
                this.effectiveDigits = length;
                break;
        }
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
     * DDLで桁数指定の引数の数。
     * 
     * @return
     */
    private int getDigitsNum() {
        switch (type) {
            case DECIMAL:
            case NUMERIC:
                return 2;
            case DATETIME2:
            case DATETIMEOFFSET:
            case TIME:
            case CHAR:
            case VARCHAR:
            case NCHAR:
            case NVARCHAR:
                return 1;
            default:
                return 0;
        }
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

    public enum ColumnType {
        INT,
        BIT,
        DECIMAL,
        MONEY,
        NUMERIC,
        SMALLINT,
        TINYINT,
        FLOAT,
        REAL,
        DATE,
        DATETIME2,
        DATETIME,
        DATETIMEOFFSET,
        SMALLDATETIME,
        TIME,
        CHAR,
        TEXT,
        VARCHAR,
        NCHAR,
        NVARCHAR
    }

    static public ColumnType getByName(String name) {
        for (ColumnType colType : ColumnType.values()) {
            if (name.equalsIgnoreCase(colType.toString())) {
                return colType;
            }
        }
        return null;
    }
}