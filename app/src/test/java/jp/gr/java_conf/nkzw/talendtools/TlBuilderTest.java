/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package jp.gr.java_conf.nkzw.talendtools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class TlBuilderTest {
    static private String TEST_DIR = "src/test/resources/";
    // 標準出力チェック用
    // private ByteArrayOutputStream _baos;
    // private PrintStream _out;
    // @BeforeEach
    // public void setUp() {
    // _baos = new ByteArrayOutputStream();
    // _out = System.out;
    // System.setOut(
    // new PrintStream(
    // new BufferedOutputStream(_baos)));
    // }
    // @AfterEach
    // public void tearDown() {
    // System.setOut(_out);
    // }

    /**
     * 正常パターン：統計情報なし
     * directory: test01
     */
    @Test
    void testNormal() {
        String[] args = {
                "-w", Paths.get(TEST_DIR + "testNormal").toString(),
                "-p", "EXAMPLE",
                "-o", Paths.get(TEST_DIR + "testNormal/tmp").toAbsolutePath().toString(),
                // "-show",
                "-out_components"
        };
        try {
            TlBuilder.main(args);

            String expect = Files.readString(Paths.get(TEST_DIR +
            "testNormal/expect/EXAMPLE.txt"));
            String actual = Files.readString(Paths.get(TEST_DIR +
            "testNormal/tmp/EXAMPLE.txt"));
            assertEquals(expect, actual, "result match OK");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * 正常パターン：統計情報付き
     * directory: test02
     */
    @Test
    void testNormalWithStat() {
        String[] args = {
                "-w", Paths.get(TEST_DIR + "testNormalWithStat").toString(),
                "-p", "EXAMPLE",
                "-o", Paths.get(TEST_DIR + "testNormalWithStat/tmp").toAbsolutePath().toString(),
                "-s", TEST_DIR + "testNormalWithStat/stats_file.dat",
                // "-show",
                "-out_components"
        };
        try {
            TlBuilder.main(args);

            String actual = Files.readString(Paths.get(TEST_DIR + "testNormalWithStat/tmp/EXAMPLE.txt"));
            String expect = Files.readString(Paths.get(TEST_DIR + "testNormalWithStat/expect/EXAMPLE.txt"));

            assertEquals(expect, actual, "result match OK");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * DB接続
     */
    @Test
    void testConnectionMssql() {
        String[] args = {
                "-w", Paths.get(TEST_DIR + "testConnectionMssql").toString(),
                "-p", "TALENDTOOLS",
                "-o", Paths.get(TEST_DIR + "testConnectionMssql/tmp").toAbsolutePath().toString(),
                // "-show",
                "-out_connections",
                "-out_ddl"
        };
        try {
            TlBuilder.main(args);
            {
                String actual = Files
                        .readString(Paths.get(TEST_DIR + "testConnectionMssql/tmp/mssql_0.1.item.dat"));
                String expect = Files
                        .readString(Paths.get(TEST_DIR + "testConnectionMssql/expect/mssql_0.1.item.dat"));
                assertEquals(expect, actual, "result match OK");
            }
            {
                String actual = Files
                        .readString(Paths.get(TEST_DIR + "testConnectionMssql/tmp/create_mssql_0.1.item.sql"));
                String expect = Files
                        .readString(
                                Paths.get(TEST_DIR + "testConnectionMssql/expect/create_mssql_0.1.item.sql"));
                assertEquals(expect, actual, "result match OK");
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testConnectionOracle() {
        String[] args = {
                "-w", Paths.get(TEST_DIR + "testConnectionOracle").toString(),
                "-p", "TALENDTOOLS",
                "-o", Paths.get(TEST_DIR + "testConnectionOracle/tmp").toAbsolutePath().toString(),
                // "-show",
                "-out_connections",
                "-out_ddl"
        };
        try {
            TlBuilder.main(args);

            {
                String actual = Files
                        .readString(Paths.get(TEST_DIR + "testConnectionOracle/tmp/oracle_0.1.item.dat"));
                String expect = Files
                        .readString(Paths.get(TEST_DIR + "testConnectionOracle/expect/oracle_0.1.item.dat"));
                assertEquals(expect, actual, "result match OK");
            }
            {
                String actual = Files
                        .readString(
                                Paths.get(TEST_DIR + "testConnectionOracle/tmp/create_oracle_0.1.item.sql"));
                String expect = Files
                        .readString(
                                Paths.get(TEST_DIR + "testConnectionOracle/expect/create_oracle_0.1.item.sql"));
                assertEquals(expect, actual, "result match OK");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
