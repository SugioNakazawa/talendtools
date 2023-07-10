package jp.gr.java_conf.nkzw.talendtools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Talendジョブ
 */
public class TlJob {
    /**
     * Talendジョブファイルからジョブオブジェクト生成
     * 
     * @param jobfile
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    static public TlJob createJob(File jobfile) throws ParserConfigurationException, SAXException, IOException {
        TlJob job = new TlJob(
                jobfile.getName().replaceAll(".item", ""),
                jobfile.toString());

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(jobfile);
        Element elementList = document.getDocumentElement();
        NodeList nodes = elementList.getElementsByTagName("node");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            NodeList elmParaList = node.getChildNodes();
            String uniqueName = getValueByName(elmParaList, "UNIQUE_NAME");
            String messageName = getValueByName(elmParaList, "MESSAGE");
            if (uniqueName != null) {
                job.addComponent(
                        new TlComponent(
                                uniqueName,
                                node.getAttribute("componentName"),
                                messageName));
            }
        }
        return job;
    }

    static private String getValueByName(NodeList nodeList, String name) {
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node node = nodeList.item(j);
            if ((node.getAttributes() != null) 
                && (node.getAttributes().getNamedItem("name") != null)) {
                if (node.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase(name)) {
                    return node.getAttributes().getNamedItem("value").getNodeValue();
                }
            }
        }
        return null;
    }

    /** ジョブ名 */
    private String jobName;
    /** ジョブファイル名(.item) */
    private String jobuFileName;
    /** コンポーネントリスト */
    private List<TlComponent> componentList;

    public TlJob(String jobName, String jobuFileName) {
        this.jobName = jobName;
        this.jobuFileName = jobuFileName;
        this.componentList = new ArrayList<TlComponent>();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobuFileName() {
        return jobuFileName;
    }

    public void setJobuFileName(String jobuFileName) {
        this.jobuFileName = jobuFileName;
    }

    public List<TlComponent> getComponentList() {
        Collections.sort(this.componentList, new Comparator<TlComponent>() {
            @Override
            public int compare(TlComponent o1, TlComponent o2) {
                return o1.getId().compareTo(o2.getId());
            }

        });
        return this.componentList;
    }

    public void addComponent(TlComponent comp) {
        this.componentList.add(comp);
    }

    /**
     * テキスト出力用
     * 
     * @param indent
     * @param inc
     * @return
     */
    public String getString(String indent, String inc) {
        StringBuffer sb = new StringBuffer();
        sb.append(indent + "job: " + jobName + "\n");
        sb.append(indent + "jobFileName: " + jobuFileName + "\n");
        for (TlComponent component : getComponentList()) {
            sb.append(component.getString(indent + inc));
        }
        return sb.toString();
    }

}
