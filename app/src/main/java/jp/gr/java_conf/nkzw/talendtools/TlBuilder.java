package jp.gr.java_conf.nkzw.talendtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Talendソース解析オブジェクトビルダー
 */
public class TlBuilder {
    private static final Logger LOGGER = LogManager.getLogger(TlBuilder.class);

    /** Talend workspace dir */
    static String DEFAULT_WORKSPACE_DIR;
    /** Talend project name */
    static String DEFAULT_PROJECT_NAME;
    /** Excel output directory */
    static String DEFAULT_OUTPUT_DIR;

    /** workspace directory of Talend Studio */
    private String workspaceDir;
    /** project name of Talend Studio */
    private String projectName;

    /** Talendで作成したjobItemファイルリスト */
    private List<File> itemFileList;

    // read properties file
    static {
        Properties properties = new Properties();
        String file1 = "./TlBuilder.properties";
        try (FileInputStream fis = new FileInputStream(file1);) {
            properties.load(fis);
            DEFAULT_WORKSPACE_DIR = properties.getProperty("DEFAULT_WORKSPACE_DIR");
            DEFAULT_PROJECT_NAME = properties.getProperty("DEFAULT_PROJECT_NAME");
            DEFAULT_OUTPUT_DIR = properties.getProperty("DEFAULT_OUTPUT_DIR");
        } catch (FileNotFoundException e) {
            LOGGER.warn("no properties file. You can use as default values, by creating TlBuilder.properties file." + System.lineSeparator()
                    + "DEFAULT_WORKSPACE_DIR=xxxxxx" + System.lineSeparator()
                    + "DEFAULT_PROJECT_NAME=XXX" + System.lineSeparator()
                    + "DEFAULT_OUTPUT_DIR=XXX" + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CommandLine parseCommandLine(String[] args) throws ParseException {
        // コマンド・オプション定義
        Options options = new Options();
        options.addOption("h", "help", false, "help");
        options.addOption(
                Option.builder("o").longOpt("outputDir").hasArg().desc("output_directory").build());
        options.addOption(
                Option.builder("p").longOpt("projectName").hasArg().desc("talend_project_name").build());
        options.addOption(
                Option.builder("s").longOpt("statFilePath").hasArg().desc("stat_file_path").build());
        options.addOption(
                Option.builder("w").longOpt("workspaceDir").hasArg().desc("talend_workspace_directory").build());
        options.addOption("out_components", false, "output [project].xlsx and [project].txt");
        options.addOption("out_connections", false, "output [db_connection.item].dat");
        options.addOption("out_ddl", false, "output create_[db_connection.item].sql");
        options.addOption("show", false, "show to concole");
        // コマンド・パース
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("h")) {
                // オプションのヘルプ情報を表示する。
                (new HelpFormatter()).printHelp("[opts]", options);
            }
            return commandLine;
        } catch (ParseException e) {
            System.err.println("引数解析エラー");
            (new HelpFormatter()).printHelp("[opts]", options);
            throw e;
        }
    }

    public TlBuilder(String workspaceDir, String projectName) {
        this.workspaceDir = workspaceDir;
        this.projectName = projectName;

        this.itemFileList = new ArrayList<File>();

        LOGGER.info("WORKSPACE_DIR: " + workspaceDir
                + " projectName: " + projectName);
    }

    public TlProjct build()
            throws ParserConfigurationException, SAXException, IOException {
        TlProjct projct = new TlProjct(projectName);
        // ジョブのターゲットディレクトリ
        File processDir = new File(workspaceDir + "/" + projectName + "/process");
        if (processDir.exists()) {
            // ジョブファイルリスト取得
            itemFileList.clear();
            List<File> jobFileList = recursiveSearchItemFile(processDir);
            // create job obj
            for (File file : jobFileList) {
                projct.addJob(TlJob.createJob(file));
            }
        } else {
            LOGGER.warn("not found process directory");
        }

        // DB接続のターゲットディレクトリ
        File connectinsDir = new File(workspaceDir + "/" + projectName + "/metadata/connections");
        if (connectinsDir.exists()) {
            // DB接続ファイルリスト取得
            itemFileList.clear();
            List<File> connectinFileList = recursiveSearchItemFile(connectinsDir);
            // create connection obj
            for (File file : connectinFileList) {
                projct.addConnection(buildConnection(file));
            }
        } else {
            LOGGER.warn("not found conctions directory");
        }

        return projct;
    }

    /**
     * Itemファイルを再帰的に取得。
     * 
     * @param dir
     * @return
     */
    private List<File> recursiveSearchItemFile(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                recursiveSearchItemFile(file);
            } else {
                if (file.getName().endsWith(".item")) {
                    itemFileList.add(file);
                }
            }
        }
        return itemFileList;
    }

    /**
     * parse from connection.item file
     * 
     * @param connectinsFile
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public TlConnection buildConnection(File connectinsFile)
            throws ParserConfigurationException, SAXException, IOException {

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(connectinsFile);
        Element elementList = document.getDocumentElement();
        NodeList dbcon = elementList.getElementsByTagName("TalendMetadata:DatabaseConnection");
        if (dbcon.getLength() != 1) {
            // 複数ある場合はエラー
            LOGGER.error("TalendMetadata:DatabaseConnection must be One!");
            new RuntimeException();
        }
        Element dbConNode = (Element) elementList.getElementsByTagName("TalendMetadata:DatabaseConnection").item(0);
        // create TlConnectin
        TlConnection connection = new TlConnection(
                connectinsFile.getName(),
                dbConNode.getAttribute("ProductId"));
        connection.setDbConnection(dbConNode.getAttributes());

        NodeList nodes = elementList.getElementsByTagName("ownedElement");
        String schemaName = "";

        TableFactory tableFactory = new TableFactory();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            if (!node.hasAttribute("tableType")) {
                schemaName = node.getAttribute("name");
                continue;
            }
            // create table
            Table table = tableFactory.createTable(
                    connection.getProductId(),
                    node.getAttributes().getNamedItem("name").getTextContent(),
                    node.getAttributes().getNamedItem("tableType").getTextContent(),
                    schemaName);

            NodeList elmParaList = node.getElementsByTagName("feature");
            for (int j = 0; j < elmParaList.getLength(); j++) {
                Node elmPara = elmParaList.item(j);
                // 長さ
                int length = 0;
                if (elmPara.getAttributes().getNamedItem("length") != null) {
                    length = (Integer.parseInt(elmPara.getAttributes().getNamedItem("length").getTextContent()));
                }
                // not null
                boolean nullable = true;
                if (elmPara.getAttributes().getNamedItem("nullable") != null) {
                    nullable = Boolean.parseBoolean(
                            elmPara.getAttributes().getNamedItem("nullable")
                                    .getTextContent());
                }
                // precision
                int precision = -1;
                if (elmPara.getAttributes().getNamedItem("precision") != null) {
                    precision = Integer.parseInt(
                            elmPara.getAttributes().getNamedItem("precision")
                                    .getTextContent());
                }
                // create column
                table.addColumn(
                        elmPara.getAttributes().getNamedItem("name").getTextContent(),
                        elmPara.getAttributes().getNamedItem("sourceType").getTextContent(),
                        length,
                        precision,
                        length,
                        nullable);
            }
            connection.addTable(table);
        }
        return connection;
    }

    public static void main(String[] args) throws Exception {
        LOGGER.info("TlBuilder start...");
        // コマンド・ラインのパース
        CommandLine commandLine = null;
        try {
            commandLine = parseCommandLine(args);
            if (commandLine.hasOption("h")) {
                // help の時には処理終了。
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        // ビルダー
        TlBuilder builder = new TlBuilder(
                commandLine.getOptionValue("w", DEFAULT_WORKSPACE_DIR),
                commandLine.getOptionValue("p", DEFAULT_PROJECT_NAME));
        TlProjct project = builder.build();

        // add stat info ( option )
        if (commandLine.hasOption("s")) {
            project.addStatFile(commandLine.getOptionValue("s"));
            LOGGER.info("statDilePath = " + commandLine.getOptionValue("s"));
        }

        var outputDir = commandLine.getOptionValue("o", DEFAULT_OUTPUT_DIR);
        LOGGER.info("output dir = " + outputDir);
        // prepare output dir
        var dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (commandLine.hasOption("out_components")) {
            // コンポーネント エクセル出力
            (new TlWorkbook()).outputWorkBook(project, outputDir);
            // コンポーネント テキスト出力
            outputText(outputDir + "/" + project.getProjectName() + ".txt", project.getAllComponentStr("", ""));
            // コンポーネント コンソール表示
            if (commandLine.hasOption("show")) {
                System.out.println(project.getAllComponentStr("", "\t"));
            }
        }
        if (commandLine.hasOption("out_connections")) {
            // DB接続 txt出力
            for (var con : project.getConnectionList()) {
                outputText(outputDir + "/" + con.getItemFileName() + ".dat", con.getString("", ""));
            }
            // DB接続 コンソール表示
            if (commandLine.hasOption("show")) {
                System.out.println(project.getAllConnectionStr("", "\t"));
            }
        }
        // DDL 出力
        if (commandLine.hasOption("out_ddl")) {
            for (TlConnection con : project.getConnectionList()) {
                StringBuffer sb = new StringBuffer();
                for (var table : con.getTableList()) {
                    sb.append(table.getCreateTableSql());
                }
                outputText(outputDir + "/create_" + con.getItemFileName() + ".sql", sb.toString());
            }
        }
    }

    private static void outputText(String filePath, String contents) {
        contents = contents.replace('\\', '/'); // for Windows
        try (FileWriter fw = new FileWriter(filePath);) {
            fw.write(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
