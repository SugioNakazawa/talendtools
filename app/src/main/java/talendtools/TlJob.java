package talendtools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        Collections.sort(this.componentList, new Comparator<TlComponent>() {
            @Override
            public int compare(TlComponent o1, TlComponent o2) {
                return o1.getId().compareTo(o2.getId());
            }
            
        });
        return this.componentList;
    }

    public void addComponent(TlComponent comp){
        this.componentList.add(comp);
    }

    public String getString(String prefix){
        StringBuffer sb = new StringBuffer();
        sb.append(prefix + "job: " + jobName + "\n");
        sb.append(prefix + "jobFileName: " + jobuFileName + "\n");
        for(TlComponent component:getComponentList()){
            sb.append(component.getString(prefix + prefix));
        }
        return sb.toString();
    }

}
