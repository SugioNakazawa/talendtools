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
    /** プロジェクト名 */
    private String projectName;
    /** ジョブリスト */
    private List<TlJob> jobList;
    /** 統計情報 */
    private List<TlStat> statList;

    public TlProjct(String projectName) {
        this.projectName = projectName;
        this.jobList = new ArrayList<TlJob>();
        this.statList = new ArrayList<TlStat>();
    }

    public String getProjectName() {
        return projectName;
    }

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

    public String getStringAll(String prefix){
        StringBuilder sb = new StringBuilder();
        sb.append("project: " + this.projectName + "\n");
        for(TlJob job:getJobList()){
            sb.append(job.getString(prefix));
        }
        return sb.toString();
    }

    /**
     * 統計情報ファイルを読み込み、コンポーネント情報更新。
     * @param filepath
     */
    public void addStatFile(String filepath) {
        try {
            this.statList = TlStat.readStatFile(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(TlStat stat:this.statList){
            TlComponent comp = search(stat.getJob()+"_"+stat.getJob_version(), stat.getOrigin());
            if(comp != null){
                comp.setExecNum(comp.getExecNum()+1);
                try{
                    comp.setErapsmsec(comp.getErapsmsec() + Integer.parseInt(stat.getDuration()));
                }catch (NumberFormatException nfe){

                }
            }
        }
    }
    private TlComponent search(String jobName, String componentId){
        for(TlJob job:this.jobList){
            if(jobName.equalsIgnoreCase(job.getJobName())){
                for(TlComponent component:job.getComponentList()){
                    if(componentId.equalsIgnoreCase(component.getId())){
                        return component;
                    }
                }
            }
        }
        return null;
    }
}
