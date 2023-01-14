package jp.gr.java_conf.nkzw.talendtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Talendジョブ
 */
public class TlBuilder {
    /** logger */
    private static final Logger LOGGER = LogManager.getLogger(TlBuilder.class);

    /** Talend workspace dir */
    static String DEFAULT_WORKSPACE_DIR;
    /** Talend project name */
    static String DEFAULT_PROJECT_NAME;
    /** Excel output directory */
    static String DEFAULT_OUTPUT_DIR;

    /** Talendで作成したjobItemファイルリスト */
    private List<File> jobItemFileList;

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
        this.jobItemFileList = new ArrayList<File>();
    }

    public TlProjct build(String projectName) throws ParserConfigurationException, SAXException, IOException {
        TlProjct projct = new TlProjct(projectName);
        // ターゲットプロジェクトのルート
        File workspace_dir = new File(DEFAULT_WORKSPACE_DIR + "/" + projectName + "/process");
        // ジョブファイルリスト取得
        List<File> jobFileList = getJobItem(workspace_dir);
        // create job obj
        for (File jobFile : jobFileList) {
            projct.addJob(createJob(jobFile));
        }
        return projct;
    }

    /**
     * ジョブファイルからジョブオブジェクトの生成。
     * job.itemファイルをパースし、コンポーネントのID,typeを取得し、オブジェクトを生成。
     * 
     * @param jobfile
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public TlJob createJob(File jobfile) throws ParserConfigurationException, SAXException, IOException {
        TlJob job = new TlJob(
                jobfile.getName().replaceAll(".item", ""),
                jobfile.toString());

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(jobfile);
        Element elementList = document.getDocumentElement();
        NodeList nodes = elementList.getElementsByTagName("node");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            NodeList elmParaList = node.getChildNodes();
            for (int j = 0; j < elmParaList.getLength(); j++) {
                Node elmPara = elmParaList.item(j);
                if ((elmPara.getAttributes() != null) && (elmPara.getAttributes().getNamedItem("name") != null)) {
                    if (elmPara.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("UNIQUE_NAME")) {
                        job.addComponent(
                                new TlComponent(
                                        elmPara.getAttributes().getNamedItem("value").getNodeValue(),
                                        node.getAttribute("componentName")));
                    }
                }
            }
        }
        return job;
    }

    /**
     * ジョブのItemファイルを再帰的に取得。
     * 
     * @param dir
     * @return
     */
    private List<File> getJobItem(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getJobItem(file);
            } else {
                if (file.getName().endsWith(".item")) {
                    jobItemFileList.add(file);
                }
            }
        }
        return jobItemFileList;
    }

    public static void main(String[] args) throws Exception {
        // コマンド・オプション定義
        Options options = new Options();
        options.addOption("h", "help", false, "help");
        options.addOption(
                Option.builder("w").longOpt("workspaceDir").hasArg().desc("talend_workspace_directory").build());
        options.addOption(
                Option.builder("p").longOpt("projectName").hasArg().desc("talend_project_name").build());
        options.addOption(
                Option.builder("s").longOpt("statFilePath").hasArg().desc("stat_file_path").build());
        options.addOption(
                Option.builder("o").longOpt("outputDir").hasArg().desc("output_directory").build());
        options.addOption("show", false, "show conponent structure to concole");
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
        commandLine.getOptionValue("w");
        String projectName = commandLine.getOptionValue("p", DEFAULT_PROJECT_NAME);
        String statFilePath = commandLine.getOptionValue("s");
        String outputDir = commandLine.getOptionValue("o", DEFAULT_OUTPUT_DIR);
        System.out.println("WORKSPACE_DIR : " + DEFAULT_WORKSPACE_DIR);
        System.out.println("projectName : " + projectName);
        System.out.println("statFilePath : " + statFilePath);
        System.out.println("outputDir : " + outputDir);

        // // ビルダー実行
        TlBuilder builder = new TlBuilder();
        TlProjct project = builder.build(projectName);
        // 統計情報更新
        if (commandLine.hasOption("s")) {
            project.addStatFile(statFilePath);
        }
        // エクセル出力
        (new TlWorkbook()).outputWorkBook(project, outputDir);
        // コンソール表示
        if (commandLine.hasOption("show")) {
            System.out.println(project.getStringAll("\t"));
        }
        // System.out.println(project.getStringAll(""));
    }
}
