package jp.gr.java_conf.nkzw.talendtools;

public class TlColumnType {
    private String typeName;
    private int digitsNum;
    private int maxEffectiveDigits;

    public TlColumnType(String typeName, int digitsNum, int maxEffectiveDigits) {
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