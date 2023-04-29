package jp.gr.java_conf.nkzw.talendtools;

/**
 * カラムの型クラス
 * Talendから取得したlengthは表示を示しているため、
 * DDL作成時にはこのクラスのオブジェクトを使用して桁数を設定。
 */
public class ColumnType {
    /** 型名 */
    private String typeName;
    /** DDLで桁数に指定する引数の数 */
    private int digitsNum;
    /** DDLで指定する最大引数 */
    private int maxDdlDigits;

    public ColumnType(String typeName, int digitsNum, int maxDdlDigits) {
        this.typeName = typeName;
        this.digitsNum = digitsNum;
        this.maxDdlDigits = maxDdlDigits;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getDigitsNum() {
        return digitsNum;
    }

    public int getMaxDdlDigits() {
        return maxDdlDigits;
    }

}