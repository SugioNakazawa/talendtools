package jp.gr.java_conf.nkzw.talendtools;

/**
 * Talend metadata database connectin Column define
 */
public class TlColumn {
    private String name;
    private String type;
    private String length;

    public TlColumn(String name, String type) {
        this.name = name;
        this.type = type;
        this.length = "0";
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLength() {
        return length;
    }

    public String getString(String indent) {
        StringBuffer sb = new StringBuffer();
        sb.append(indent + "columnName: " + this.name
                + ", type: " + this.type
                + ", length: " + this.length + "\n");
        return sb.toString();
    }
}
