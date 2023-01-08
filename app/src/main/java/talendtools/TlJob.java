package talendtools;

import java.util.ArrayList;
import java.util.List;

/**
 * Talendジョブ
 */
public class TlJob {
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
        return componentList;
    }

    public void addComponent(TlComponent comp){
        this.componentList.add(comp);
    }

    public String getString(String prefix){
        StringBuffer sb = new StringBuffer();
        sb.append(prefix + "job: " + jobName + "\n");
        sb.append(prefix + "jobuFileName: " + jobuFileName + "\n");
        for(TlComponent component:this.componentList){
            sb.append(component.getString(prefix + prefix));
        }
        return sb.toString();
    }

}
