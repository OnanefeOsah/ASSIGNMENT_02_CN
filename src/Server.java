import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


//Job creator
public class Server {


    private ServerSocket ss;
    private Socket s;
    private BufferedReader in;
    private PrintStream out;
    private Scanner scan;
    private String chat = " ";
    private ArrayList<String> createdJobs = new ArrayList<>();
    private String delimiter = ",";
    private String choiceDelimiter = ";";
    private ArrayList<String> clientJobSeekList = new ArrayList<>();
    //private ArrayList<String> fff = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Server();
    }

    Server(){

        try {
        ss = new ServerSocket(4999);
        System.out.println("Waiting for Client....");

        // connect it to client socket
        s = ss.accept();
        System.out.println("Connection established");
        scan = new Scanner(System.in);
        createJobs(); //TODO

        while(!s.isClosed()){
            //menu();
            receive();
            send("", 0);
            /**
            System.out.print(">>>");
            int choice  = scan.nextInt();
            while(choice<1 || choice> 3){
                System.out.print("Wrong input");
                System.out.print(">>>");
                choice = scan.nextInt();
            }
            switch(choice){
                case 1:

                    break;
                case 2:

                    break;
                case 3:
                    System.out.println("Thank you for connecting...BYE");
                    s.close();
                    break;
            }
        **/

        }


        }catch(IOException e) {
            System.out.println(e);
        }
    }

    private void createOneToOneJobs(){

    }

    private void createOneToManyJobs(){

    }

    private void createJobs() {
        //this.createdJobs.add("One-To-One");
        this.createdJobs.add("Fishing");
        this.createdJobs.add("Teaching");
        //this.createdJobs.add("One-To-Many");
        this.createdJobs.add("Game Dev");
    }

    public void send(String chat, int numChoice) throws IOException {
        this.out = new PrintStream(s.getOutputStream());
        chat = chat.concat(this.choiceDelimiter + numChoice);
        this.out.println(chat);
    }

    public void receive() throws IOException {
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String receiveFromServer = this.in.readLine();
        if(receiveFromServer.contains(this.choiceDelimiter)){
            String[] broken = receiveFromServer.split(this.choiceDelimiter);
            String brokenSentence = broken[0];
            int numChoiceFromServer = Integer.parseInt(broken[1]);

            //client case 1
            if(numChoiceFromServer == 1){
                if(this.createdJobs.size()> 1) {
                    StringBuilder sb = new StringBuilder();
                    for (String j : this.createdJobs) {
                        sb.append(j);
                        sb.append(this.delimiter);
                    }
                    send(sb.toString(), 1);
                }
                else{

                }
            }
            //client case 1,1
            if(numChoiceFromServer == 11){
                System.out.println("Job request from client");
                System.out.println("1. Approve");
                System.out.println("2. Decline");
                System.out.println("3. Later");
                int jobStatChoice = scan.nextInt();

                if(jobStatChoice == 1) {
                    send("Job Approved," + brokenSentence, 11);
                    this.createdJobs.remove(brokenSentence);
                }
                else if(jobStatChoice == 2){
                    send("Job Declined," + brokenSentence, 11);
                }
                else{
                    send("Job request is in process", 11);
                    this.clientJobSeekList.add(brokenSentence);
                }
            }
            //client case 2
            if(numChoiceFromServer == 2){
                if(this.clientJobSeekList.size() < 1){
                    System.out.println("The Client is looking for a job");
                    System.out.println("Is there a job available???");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    int jobChoice = scan.nextInt();
                    while(jobChoice<1 || jobChoice> 2){
                        System.out.println("Wrong Input!!!");
                        jobChoice = scan.nextInt();
                    }
                    scan.nextLine();
                    if(jobChoice == 1){
                        System.out.println("What job corresponds to the client: ");
                        String job = scan.nextLine();
                        this.clientJobSeekList.add(job);
                        send(job, 21); //TODO
                    }
                    else if(jobChoice == 2){
                        send("There are no jobs available currently. Check again later", 22);
                    }
                }
                else{

                }

            }
            //client case 2,1
            if(numChoiceFromServer == 21){
                this.clientJobSeekList.add(brokenSentence);
            }
            //client case 2,2
            if(numChoiceFromServer == 22){
                //TODO
            }

            //client case 3
            if(numChoiceFromServer == 3){
                String[] delimBrokenSentence = brokenSentence.split(this.delimiter);
                if(delimBrokenSentence[0].equalsIgnoreCase("done")){
                    this.createdJobs.remove(delimBrokenSentence[1]);
                }
            }

            //client case 4
            if(numChoiceFromServer == 4){
                s.close();
            }


        }
        else if(receiveFromServer.contains(this.delimiter)){

        }

    }

    public void menu(){
        //System.out.println("Welcome...this is the Job menu");
        System.out.println("1. Available Job Seekers");
        System.out.println("2. Assign Jobs");
        System.out.println("3. Terminate Connection..");
    }





}