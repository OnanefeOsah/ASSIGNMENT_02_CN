public class Seeker {

    private Job j;
    private int id;

    public Seeker(Job j){
        this.j = j;
    }

    public void setJ(Job j) {
        this.j = j;
    }

    public Job getJ() {
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
