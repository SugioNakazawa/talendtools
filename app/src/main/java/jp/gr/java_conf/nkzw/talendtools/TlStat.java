package jp.gr.java_conf.nkzw.talendtools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Talend 統計情報
 * 例
 * 2023-01-02 21:20:41;
 * YQ9AYq;
 * YQ9AYq;
 * YQ9AYq;
 * 38316;
 * SAMPLE;
 * copyFiles;
 * _x3qkwH3wEe2uw7c4-6rRVA;
 * 0.1;
 * Default;
 * ;
 * end;
 * success;
 * 58
 */
public class TlStat {
    private String moment;
    private String pid;
    private String father_pid;
    private String root_pid;
    private String system_pid;
    private String project;
    private String job;
    private String job_repository_id;
    private String job_version;
    private String context;
    private String origin;
    private String message_type;
    private String message;
    private String duration;

    public TlStat(String moment, String pid, String father_pid, String root_pid, String system_pid, String project,
            String job, String job_repository_id, String job_version, String origin, String context,
            String message_type,
            String message, String duration) {
        this.moment = moment;
        this.pid = pid;
        this.father_pid = father_pid;
        this.root_pid = root_pid;
        this.system_pid = system_pid;
        this.project = project;
        this.job = job;
        this.job_repository_id = job_repository_id;
        this.job_version = job_version;
        this.context = context;
        this.origin = origin;
        this.message_type = message_type;
        this.message = message;
        this.duration = duration;
    }

    public TlStat(String line) {
        String[] cols = line.split(";", 14);
        if (cols.length != 14) {
            new RuntimeException("");
        }
        this.moment = cols[0];
        this.pid = cols[1];
        this.father_pid = cols[2];
        this.root_pid = cols[3];
        this.system_pid = cols[4];
        this.project = cols[5];
        this.job = cols[6];
        this.job_repository_id = cols[7];
        this.job_version = cols[8];
        this.context = cols[9];
        this.origin = cols[10];
        this.message_type = cols[11];
        this.message = cols[12];
        this.duration = cols[13];
    }

    public String getMoment() {
        return moment;
    }

    public String getPid() {
        return pid;
    }

    public String getFather_pid() {
        return father_pid;
    }

    public String getRoot_pid() {
        return root_pid;
    }

    public String getSystem_pid() {
        return system_pid;
    }

    public String getProject() {
        return project;
    }

    public String getJob() {
        return job;
    }

    public String getJob_repository_id() {
        return job_repository_id;
    }

    public String getJob_version() {
        return job_version;
    }

    public String getContext() {
        return context;
    }

    public String getOrigin() {
        return origin;
    }

    public String getMessage_type() {
        return message_type;
    }

    public String getMessage() {
        return message;
    }

    public String getDuration() {
        return duration;
    }

    /**
     * テキスト出力用
     * 
     * @return
     */
    public String getString() {
        StringBuilder sb = new StringBuilder();
        sb.append(job + ";");
        sb.append(origin + ";");
        sb.append(message_type + ";");
        sb.append(message + ";");
        sb.append(duration + ";");
        return sb.toString();
    }

    static List<TlStat> readStatFile(String filepath) throws IOException {
        ArrayList<TlStat> retList = new ArrayList<TlStat>();
        File file = new File(filepath);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String data;
        while ((data = br.readLine()) != null) {
            retList.add(new TlStat(data));
        }
        br.close();

        return retList;
    }

    public static void main(String[] args) throws IOException {
        List<TlStat> retList = TlStat.readStatFile("app/src/test/resources/stats_file.txt");
        for (TlStat stat : retList) {
            System.out.println(stat.getString());
        }
    }
}
