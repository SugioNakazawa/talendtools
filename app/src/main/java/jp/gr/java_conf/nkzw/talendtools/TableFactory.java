package jp.gr.java_conf.nkzw.talendtools;

/**
 * DBMSごとのオブジェクトを生成するファクトリ
 */
public class TableFactory {
    /**
     * 
     * @param dbType DBMSの識別。Talend Item内の　属性ProductIdを利用。DBMS;ORACLE, SQL_SERVER
     * @param tableName
     * @param tableType
     * @param schemeName MSSQLで使用
     * @return
     */
    public Table createTable(String dbType, String tableName, String tableType, String schemeName) {
        if ("ORACLE".equalsIgnoreCase(dbType)) {
            return new OracleTable(tableName, tableType, schemeName);
        } else if ("SQL_SERVER".equalsIgnoreCase(dbType)) {
            return new MssqlTable(tableName, tableType, schemeName);
        } else {
            System.out.println("no match " + dbType);
            throw new UnsupportedOperationException("Unsupported db type : " + dbType);
        }
    };

    public static void main(String[] args) {
        Table table1 = (new TableFactory()).createTable("ORACLE", "table1", "TABLE", "schema");
        System.out.println(table1);
        table1.addColumn("col1", "NUMBER", 1, 0, 0, false);
        table1.addColumn("col2", "CHAR", 2, 0, 0, true);
        table1.addColumn("col3", "DATE", 99, 0, 0, true);
        for (Column column : table1.getColumnList()) {
            System.out.println(
                    column);
        }
        System.out.println(table1.getCreateTableSql());
    }
}
