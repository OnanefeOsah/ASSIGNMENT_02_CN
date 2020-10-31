public class Job {

    String job;
    String[] job_types; // = {"Fishing", "Boxing", "Dancing", "Rapping"};

    public Job(String j){
        this.job = j;
    }

    public Job(String[] j){
        this.job_types = j;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String j) {
        this.job = j;
    }
}
