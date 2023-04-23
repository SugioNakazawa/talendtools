package jp.gr.java_conf.nkzw.talendtools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Talendプロジェクト
 */
public class TlProjct {

    private String projectName;
    private List<TlJob> jobList;
    private List<TlStat> statList;
    private List<TlConnection> connectionList;

    public TlProjct(String projectName) {
        this.projectName = projectName;
        this.jobList = new ArrayList<TlJob>();
        this.statList = new ArrayList<TlStat>();
        this.connectionList = new ArrayList<TlConnection>();
    }

    public String getProjectName() {
        return projectName;
    }

    /**
     * コンポーネント名でソートされたリスト
     * 
     * @return sorted list
     */
    public List<TlJob> getJobList() {
        Collections.sort(this.jobList, new Comparator<TlJob>() {
            @Override
            public int compare(TlJob o1, TlJob o2) {
                return o1.getJobName().compareTo(o2.getJobName());
            }

        });
        return this.jobList;
    }

    public void addJob(TlJob job) {
        this.jobList.add(job);
    }

    /**
     * テキスト化したジョブコンポーネント構造
     * 
     * @param indent
     * @return
     */
    public String getAllComponentStr(String indent, String inc) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent + "project: " + this.projectName + "\n");
        for (TlJob job : getJobList()) {
            sb.append(job.getString(indent + inc, inc));
        }
        return sb.toString();
    }

    public String getAllConnectionStr(String indent, String inc) {
        StringBuffer sb = new StringBuffer();
        sb.append(indent + "connections\n");
        for (TlConnection con : this.connectionList) {
            sb.append(con.getString(indent + inc, inc));
        }
        return sb.toString();
    }

    public List<TlConnection> getConnectionList() {
        return connectionList;
    }

    /**
     * 統計情報ファイルを読み込み、コンポーネント情報更新。
     * 
     * @param filepath
     */
    public void addStatFile(String filepath) {
        try {
            this.statList = TlStat.readStatFile(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (TlStat stat : this.statList) {
            TlComponent comp = search(stat.getJob() + "_" + stat.getJob_version(), stat.getOrigin());
            if (comp != null) {
                comp.setExecNum(comp.getExecNum() + 1);
                try {
                    comp.setErapsmsec(comp.getErapsmsec() + Integer.parseInt(stat.getDuration()));
                } catch (NumberFormatException nfe) {

                }
            }
        }
    }

    private TlComponent search(String jobName, String componentId) {
        for (TlJob job : this.jobList) {
            if (jobName.equalsIgnoreCase(job.getJobName())) {
                for (TlComponent component : job.getComponentList()) {
                    if (componentId.equalsIgnoreCase(component.getId())) {
                        return component;
                    }
                }
            }
        }
        return null;
    }

    public void addConnection(TlConnection connection) {
        this.connectionList.add(connection);
    }

}
