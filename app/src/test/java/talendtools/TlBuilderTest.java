/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package talendtools;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

class TlBuilderTest {
    /**
     * 正常パターン：統計情報なし
     */
    @Test
    void testNormal01() {
        try {
            TlBuilder tlManage = new TlBuilder();
            TlBuilder.DEFAULT_WORKSPACE_DIR = "src/test/resources/test01";
            TlProjct project = tlManage.build("EXAMPLE");

            String actualStr = project.getStringAll("");
            actualStr = actualStr.replace('¥', '/');    //  for Windows
            FileWriter fw = new FileWriter("../tmp/actual.dat");
            fw.write(actualStr);
            fw.close();

            String actual = Files.readString(Paths.get("../tmp/actual.dat"));
            String expect = Files.readString(Paths.get("src/test/resources/test01/expect/expect.dat"));

            assertEquals(expect, actual, "result match OK");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 正常パターン：統計情報付き
     */
    @Test
    void testNormal02() {
        String PROJECT = "EXAMPLE";
        try {
            TlBuilder tlManage = new TlBuilder();
            TlBuilder.DEFAULT_WORKSPACE_DIR = "src/test/resources/test02";
            TlProjct project = tlManage.build(PROJECT);
            project.addStatFile("src/test/resources/test02/stats_file.dat");

            String actualStr = project.getStringAll("");
            actualStr = actualStr.replace('¥', '/');    //  for Windows
            FileWriter fw = new FileWriter("../tmp/actual.dat");
            fw.write(actualStr);
            fw.close();

            String actual = Files.readString(Paths.get("../tmp/actual.dat"));
            String expect = Files.readString(Paths.get("src/test/resources/test02/expect/expect.dat"));

            assertEquals(expect, actual, "result match OK");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
/**
 * TODO main のテストケース
 *         String[] args = {
            "-w", "/Users/nakazawasugio/talend/tjtool/app/src/test/resources/test01",
            "-p", "SAMPLE",
            "-o", "tmp",
            "-show"

        };

 */