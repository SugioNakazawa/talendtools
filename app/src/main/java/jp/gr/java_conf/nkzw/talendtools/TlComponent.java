package jp.gr.java_conf.nkzw.talendtools;

/**
 * Talend process component define
 */
public class TlComponent {
    /** id: ジョブ内でのユニークID */
    private String id;
    /** コンポーネントのタイプ */
    private String type;
    /** 実行回数 */
    private int execNum = 0;
    /** 実行時間累計 */
    private int erapsmsec = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TlComponent(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getExecNum() {
        return execNum;
    }

    public void setExecNum(int execNum) {
        this.execNum = execNum;
    }

    public int getErapsmsec() {
        return erapsmsec;
    }

    public void setErapsmsec(int erapsmsec) {
        this.erapsmsec = erapsmsec;
    }

    public String getString(String indent) {
        StringBuffer sb = new StringBuffer();
        sb.append(indent + "id: " + id + "  type: " + type);
        if (execNum > 0) {
            sb.append("  execNum: " + execNum);
            if (erapsmsec > 0) {
                sb.append("  erapsmsec: " + erapsmsec);
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
