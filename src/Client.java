import java.io.*;
import java.net.Socket;
import java.util.*;

//Job Seeker
public class Client {

    private Socket s;

    private Scanner scan;
    private String chat  = " ";
    private String clientJob = " ";
    private boolean hasJob;
    private boolean doneJob;
    private BufferedReader in;
    private PrintStream out;
    private String delimiter = ",";
    private String choiceDelimiter = ";";
    private int numChoiceFromServer;
    private ArrayList<String> jobsReceivedByDelim = new ArrayList<>();

    public static void main(String[] args){
        new Client();
    }

    Client(){
        try{
            s = new Socket("localhost", 4999);
            System.out.println("Connected to server " + s.getLocalAddress());
            scan = new Scanner(System.in);
            while(!s.isClosed()){
                send(" ", 0);
                receive();
                menu();
                System.out.print(">>>");
                int choice  = scan.nextInt();
                scan.nextLine();
                while(choice<1 || choice> 4){
                    System.out.print("Wrong input");
                    System.out.print(">>>");
                    choice = scan.nextInt();
                    scan.nextLine();
                }

                switch(choice){
                    case 1:
                        if(hasJob){
                            System.out.println("You currently have a job, Report your job status before selecting another");
                        }
                        else if(this.jobsReceivedByDelim.size()>1){
                            send(" ", 1);
                            receive();
                            /**
                            for(int i=0; i< this.jobsReceivedByDelim.size(); i++){
                                System.out.println((i+1)+". " + this.jobsReceivedByDelim.get(i));
                            }
                             **/
                            System.out.println("Job Available: " + this.jobsReceivedByDelim.get(0));
                            System.out.println("1. Accept Job");
                            System.out.println("2. Reject Job");
                            int jobChoice = scan.nextInt();
                            scan.nextLine();
                            while(jobChoice < 1 || jobChoice > 2){
                                System.out.println("Wrong choice");
                                System.out.println("1. Accept Job");
                                System.out.println("2. Reject Job");
                                jobChoice = scan.nextInt();
                                scan.nextLine();
                            }
                            if(jobChoice == 1){
                                send("Accept", 21);
                            }
                            else if(jobChoice == 2){
                                send("Reject", 22);
                            }
                            System.out.println("Job application submitted....");

                        }

                        else if(this.jobsReceivedByDelim.size()<1) {
                            System.out.println("There are currently no Jobs available");
                            System.out.println("Specify your skill and wait for the job creator");
                        }


                        break;
                    case 2:
                        System.out.println("Specify your service: ");
                        String jobService = scan.nextLine();
                        send(jobService, 2);
                        System.out.println("Your skills have been logged, wait for a response...");
                        break;

                    case 3:
                        if(this.hasJob) {
                            System.out.println("Are you done with your Job");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            int thirdCaseChoice = scan.nextInt();
                            scan.nextLine();
                            while (thirdCaseChoice < 1 || thirdCaseChoice > 2) {
                                System.out.println("Wrong input!!!");
                                thirdCaseChoice = scan.nextInt();
                                scan.nextLine();
                            }
                            if(thirdCaseChoice == 1){
                                setDoneJob(true);
                                setHasJob(false);
                                send("Done," + getClientJob(), 3);
                                setClientJob("No Job");
                            }
                            else{
                                System.out.println("Ok..report back when you are done");
                                continue;
                            }
                        }
                        else{
                            System.out.println("You do not have a job currently");
                            continue;
                        }
                        break;
                    case 4:
                        if(getHasJob() && !getDoneJob()){
                            System.out.println("You have not completed your job, are you sure you want to leave??");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            int fourthCaseChoice = scan.nextInt();
                            while(fourthCaseChoice< 1 || fourthCaseChoice>2){
                                System.out.println("Wrong input!!!");
                                fourthCaseChoice = scan.nextInt();
                            }
                            if(fourthCaseChoice == 1){
                                System.out.println("Thank you for connecting...BYE");
                                s.close();
                                break;
                            }
                            else if(fourthCaseChoice == 2){
                                continue;
                            }


                        }
                        else {
                            System.out.println("Thank you for connecting...BYE");
                            send(" ", 5);
                            s.close();
                            break;
                        }
                    break;
                }

                 //send(" ", 0);
                 //receive();
            }

        }catch (IOException e){

        }

    }

    public void send(String chat, int numChoice) throws IOException {
        this.out = new PrintStream(s.getOutputStream());
        chat = chat.concat(this.choiceDelimiter + numChoice);
        this.out.println(chat);
    }

    public void receive() throws IOException {

        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String receiveFromServer = this.in.readLine();

        //if server response contains both delimiters
        if(receiveFromServer.contains(this.choiceDelimiter)){
            String[] broken = receiveFromServer.split(this.choiceDelimiter);
            String brokenSentence = broken[0];
            this.numChoiceFromServer = Integer.parseInt(broken[1]);

            //response to client case 1 from server
            if(this.numChoiceFromServer == 1){
                this.jobsReceivedByDelim.clear();
                String[] delimBrokenSentence = brokenSentence.split(this.delimiter);
                this.jobsReceivedByDelim.addAll(Arrays.asList(delimBrokenSentence));
            }


            //response to client case 2,1 from server
            if(this.numChoiceFromServer == 21){
                System.out.println("The job " + brokenSentence + " is available");
                System.out.println("Do you accept/reject");
                System.out.println("1. Accept");
                System.out.println("2. Reject");
                int secondCaseChoice = scan.nextInt();
                scan.nextLine();
                while (secondCaseChoice < 1 || secondCaseChoice > 2) {
                    System.out.println("Wrong input!!!");
                    secondCaseChoice = scan.nextInt();
                    scan.nextLine();
                }
                if(secondCaseChoice == 1) {
                    System.out.println("You've been given the job " + brokenSentence);
                    setHasJob(true);
                    setClientJob(brokenSentence);
                }

                setClientJob(brokenSentence);
            }

            if(this.numChoiceFromServer == 22){
                System.out.println(brokenSentence);
            }

            if(this.numChoiceFromServer == 9999){
                System.out.println();
            }

        }
    }

    public void menu(){
         System.out.println("Welcome...this is the Job menu");
         System.out.println("1. Available Jobs");
         System.out.println("2. Specify Seeker Service");
         System.out.println("3. Report Job Status");
         System.out.println("4. Terminate Connection..");

    }

    public String getClientJob() {
        return clientJob;
    }

    public void setClientJob(String clientJob) {
        this.clientJob = clientJob;
    }

    public void setHasJob(boolean hasJob) {
        this.hasJob = hasJob;
    }

    public boolean getHasJob(){
         return this.hasJob;
    }

    public void setDoneJob(boolean doneJob){
         this.doneJob = doneJob;
    }

    public boolean getDoneJob(){
         return this.doneJob;
    }

}