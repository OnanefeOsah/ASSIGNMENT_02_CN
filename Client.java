import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client extends Seeker{

    static Creator c = new Creator();
    List<String> jobslist;
    private int check;
    private boolean pick = false;

    public static void main(String args[]) throws Exception {
        Client c = new Client(new Job("null"));
    }

    public Client(Job j) {
        super(j);
        run();
    }

    public void run(){
        try {
            // Create client socket
            Socket s = new Socket("localhost", 4999);
            System.out.println("Connected");
            String[] jobs = c.getCreator_jobs();
            jobslist = Arrays.asList(jobs);
            System.out.println("Available jobs: ");
            for (int i = 0; i < jobslist.size(); i++) {
                System.out.println(i + ". " + jobslist.get(i));
            }

            // to send data to the server
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            // to read data from the keyboard
            BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));


            // to read data coming from the server
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String str, str1;

            // repeat as long as exit
            // is not typed at client
            while (!(str = kb.readLine()).equals("exit")) {

                for (int i = 0; i < jobslist.size(); i++) {
                    if (str.equalsIgnoreCase(jobslist.get(i))) {
                        str = "Client has picked " + jobslist.get(i);
                        check = i;
                        pick = true;
                    }
                }

                // send to the server
                dos.writeBytes(str + "\n");

                // receive from the server
                str1 = br.readLine();
                if (pick && str1.equalsIgnoreCase("Accept")) {
                    setJ(new Job(jobslist.get(check)));
                    str = "Client doing job....";
                    dos.writeBytes(str + "\n");
                    System.out.println("Job accepted..");
                }
                if(pick && str1.equalsIgnoreCase("Decline")){
                    System.out.println("Request Denied");
                    if(jobslist.size() != 0) {
                        jobslist.remove(jobslist.get(check));
                        System.out.println("Available jobs: ");
                        for (int i = 0; i < jobslist.size(); i++) {
                            System.out.println(i + ". " + jobslist.get(i));
                        }
                        for (int i = 0; i < jobslist.size(); i++) {
                            if (str.equals(jobslist.get(i))) {
                                str = "Clients job is " + jobslist.get(i);
                                check = i;
                            }
                        }
                        str = "You have denied clients request";
                        dos.writeBytes(str + "\n");
                    }
                }
                if(jobslist.size() == 0){
                    System.out.println("There are no available jobs");
                    break;
                }
                if(str.equalsIgnoreCase("Done")){
                    System.out.println("fish");
                    str = "Client is done with job...";
                    dos.writeBytes(str + "\n");
                    pick = false;
                    if(jobslist.size() != 0) {
                        jobslist.remove(getJ().getJob());
                        setJ(new Job("null)"));
                        System.out.println("Available jobs: ");
                        for (int i = 0; i < jobslist.size(); i++) {
                            System.out.println(i + ". " + jobslist.get(i));
                        }
                    }
                    else{break;}
                }
                //System.out.println(str1);
            }

            // close connection.
            dos.close();
            br.close();
            kb.close();
            s.close();
        }catch(Exception e){

        }
    }
}
