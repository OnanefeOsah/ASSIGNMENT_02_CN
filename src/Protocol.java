public class Protocol {


    private static final int WAITING = 0;
    private static final int SENT = 1;
    private static final int DONE = 2;
    private int state = WAITING;



    public String processInput(String theInput) {
        String theOutput = null;

        if (state == WAITING) {
            theOutput = "Doing job!!";
            state = SENT;
        } else if (state == SENT) {

        }else if(state == DONE){
            theOutput = "Job done";
        }

        return theOutput;
    }
}
