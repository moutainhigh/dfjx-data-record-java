package reportinggroup.bean;

public class ReportingGroup {

   private int  job_unit_id;
   private String  job_unit_name;
   private String  job_unit_active;

    public int getJob_unit_id() {
        return job_unit_id;
    }

    public void setJob_unit_id(int job_unit_id) {
        this.job_unit_id = job_unit_id;
    }

    public String getJob_unit_name() {
        return job_unit_name;
    }

    public void setJob_unit_name(String job_unit_name) {
        this.job_unit_name = job_unit_name;
    }

    public String getJob_unit_active() {
        return job_unit_active;
    }

    public void setJob_unit_active(String job_unit_active) {
        this.job_unit_active = job_unit_active;
    }


    @Override
    public String toString() {
        return "ReportingGroup{" +
                "job_unit_id=" + job_unit_id +
                ", job_unit_name='" + job_unit_name + '\'' +
                ", job_unit_active='" + job_unit_active + '\'' +
                '}';
    }
}
