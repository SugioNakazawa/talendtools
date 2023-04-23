package jp.gr.java_conf.nkzw.talendtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    /** Talendで作成したjobItemファイルリスト */
    private List<File> itemFileList;

    // read properties file
    static {
        Properties properties = new Properties();
        String file1 = "./TlBuilder.properties";
        try {
            FileInputStream fis = new FileInputStream(file1);
            try {
                properties.load(fis);
                DEFAULT_WORKSPACE_DIR = properties.getProperty("DEFAULT_WORKSPACE_DIR");
                DEFAULT_PROJECT_NAME = properties.getProperty("DEFAULT_PROJECT_NAME");
                DEFAULT_OUTPUT_DIR = properties.getProperty("DEFAULT_OUTPUT_DIR");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            LOGGER.warn("no properties file. please create TlBuilder.properties.\n"
                    + "DEFAULT_WORKSPACE_DIR=xxxxxx\n"
                    + "DEFAULT_PROJECT_NAME\n"
                    + "DEFAULT_OUTPUT_DIR\n");
        }
    }

    public TlBuilder() {
        this.itemFileList = new ArrayList<File>();
    }

    public TlProjct build(String projectName) throws ParserConfigurationException, SAXException, IOException {
        TlProjct projct = new TlProjct(projectName);
        // ジョブのターゲットディレクトリ
        File processDir = new File(DEFAULT_WORKSPACE_DIR + "/" + projectName + "/process");
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
        File connectinsDir = new File(DEFAULT_WORKSPACE_DIR + "/" + projectName + "/metadata/connections");
        if (connectinsDir.exists()) {
            // DB接続ファイルリスト取得
            itemFileList.clear();
            List<File> connectinFileList = recursiveSearchItemFile(connectinsDir);
            // create connection obj
            for (File file : connectinFileList) {
                projct.addConnection(TlConnection.buildConnection(file));
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

    public static void main(String[] args) throws Exception {
        LOGGER.info("TlBuilder start...");
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
                Option.builder("t").longOpt("targetType").hasArg().desc("targetType ( component | db_chema )").build());
        options.addOption(
                Option.builder("w").longOpt("workspaceDir").hasArg().desc("talend_workspace_directory").build());
        options.addOption("out_components", false, "output [project].xlsx and [project].txt");
        options.addOption("out_connections", false, "output [db_connection.item].txt");
        options.addOption("show", false, "show to concole");
        // コマンド・パース
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("引数解析エラー");
            (new HelpFormatter()).printHelp("[opts]", options);
            return;
        }
        // help 表示
        if (commandLine.hasOption("h")) {
            // オプションのヘルプ情報を表示する。
            (new HelpFormatter()).printHelp("[opts]", options);
            return;
        }
        // 引数
        if (commandLine.hasOption("w")) {
            DEFAULT_WORKSPACE_DIR = commandLine.getOptionValue("w");
        }
        // commandLine.getOptionValue("w");
        String projectName = commandLine.getOptionValue("p", DEFAULT_PROJECT_NAME);
        String statFilePath = commandLine.getOptionValue("s");
        String outputDir = commandLine.getOptionValue("o", DEFAULT_OUTPUT_DIR);
        String targetType = commandLine.getOptionValue("t", "component");
        System.out.println("WORKSPACE_DIR : " + DEFAULT_WORKSPACE_DIR);
        System.out.println("projectName : " + projectName);
        System.out.println("targetType : " + targetType);
        System.out.println("statFilePath : " + statFilePath);
        System.out.println("outputDir : " + outputDir);

        // // ビルダー実行
        TlBuilder builder = new TlBuilder();
        TlProjct project = builder.build(projectName);
        // 統計情報更新
        if (commandLine.hasOption("s")) {
            project.addStatFile(statFilePath);
        }
        // コンソール表示
        if (commandLine.hasOption("show")) {
            System.out.println(project.getAllComponentStr("", "\t"));
            System.out.println(project.getAllConnectionStr("", "\t"));
        }
        // structure,txt出力
        if (commandLine.hasOption("out_components")) {
            // エクセル出力
            (new TlWorkbook()).outputWorkBook(project, outputDir);
            // テキスト出力
            String contents = project.getAllComponentStr("", "");
            contents = contents.replace('\\', '/'); // for Windows
            FileWriter fw = new FileWriter(outputDir + "/" + project.getProjectName() + ".txt");
            fw.write(contents);
            fw.close();
        }
        // DB接続 txt出力
        if (commandLine.hasOption("out_connectins")) {
            for (TlConnection con : project.getConnectionList()) {
                String fileName = con.getItemFileName();
                String contents = con.getString("", "");
                contents = contents.replace('\\', '/'); // for Windows
                FileWriter fw = new FileWriter(outputDir + "/" + fileName + ".txt");
                fw.write(contents);
                fw.close();
            }
        }
    }
}
