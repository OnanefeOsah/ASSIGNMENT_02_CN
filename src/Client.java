import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Seeker{

    static Creator c = new Creator();

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
            System.out.println("Available jobs: ");
            for (int i = 0; i < jobs.length; i++) {
                System.out.println(i + ". " + jobs[i]);
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

                for (int i = 0; i < jobs.length; i++) {
                    if (str.equals(jobs[i])) {
                        str = "Client has picked " + jobs[i];
                    }
                }
                // send to the server
                dos.writeBytes(str + "\n");

                // receive from the server
                str1 = br.readLine();
                if (str1.equals("Accept")) {
                    setJ(new Job("Fishing"));
                }else if(str1.equals("Decline")){
                    jobs.
                }

                System.out.println(str1);
            }

            // close connection.
            dos.close();
            br.close();
            kb.close();
            s.close();
        }catch(Exception e){

        }
    }








    /**
    //Seeker seek  = new Seeker();
    InputStreamReader reader;
    BufferedReader read;
    PrintStream stream;
    Scanner scan;
    Socket s;
    String inputLine;
    String outputLine;

    public static void main(String[] args) throws Exception {
        Client c = new Client();
        c.run();
    }

    public void run() {
        try{
            try {
                s = new Socket("localhost", 4999);
                System.out.println("Connected to server!!");
                reader = new InputStreamReader(s.getInputStream());
                read = new BufferedReader(reader);
                scan = new Scanner(s.getInputStream());
                stream = new PrintStream(s.getOutputStream());
                stream.println("New Client selecting from jobs");
                stream.flush();
                checkStream();


                while ((inputLine = read.readLine()) != null) {
                    //outputLine = kkp.processInput(inputLine);
                    stream.println(inputLine);
                    if (outputLine.equals("End"))
                        s.close();
                        break;
                }
            }
            finally{
                s.close();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void checkStream(){
        while(true){
            receive();
        }
    }

    public void receive(){
        if(scan.hasNext()){
            String message = scan.nextLine();
            System.out.println(message);
        }
    }
     **/
}
