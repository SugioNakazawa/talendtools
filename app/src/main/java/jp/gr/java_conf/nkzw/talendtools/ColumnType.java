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
    private int maxEffectiveDigits;

    public ColumnType(String typeName, int digitsNum, int maxEffectiveDigits) {
        this.typeName = typeName;
        this.digitsNum = digitsNum;
        this.maxEffectiveDigits = maxEffectiveDigits;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getDigitsNum() {
        return digitsNum;
    }

    public void setDigitsNum(int digitsNum) {
        this.digitsNum = digitsNum;
    }

    public int getMaxEffectiveDigits() {
        return maxEffectiveDigits;
    }

    public void setMaxEffectiveDigits(int maxEffectiveDigits) {
        this.maxEffectiveDigits = maxEffectiveDigits;
    }

}