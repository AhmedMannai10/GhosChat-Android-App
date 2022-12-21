import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import entity.User;

import static java.lang.System.exit;

public class AuthServer {
    ArrayList<User> users = new ArrayList<User>(
            Arrays.asList(
                new User("Ahmed", "ahmedmannai@gmail.com", "ahmed"),
                new User("Khaled", "khaledbna@gmail.com", "khaled")
            )
    );

    ArrayList<ObjectOutputStream> clientOutPutStreams;


    public static void main(String[] args){
        new AuthServer().go();
    }

    public void go(){
        clientOutPutStreams = new ArrayList<>();

        try{
            ServerSocket serverSocket = new ServerSocket(5000) ;
            while(true){
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());
                clientOutPutStreams.add(objectWriter);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("Got a Connection" + clientSocket);
                System.gc();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable{
        ObjectInputStream objIS;
        PrintWriter writer;
        Socket sock;

        public ClientHandler(Socket clientSocket){
            try{
                sock = clientSocket;
                writer = new PrintWriter(sock.getOutputStream());
                objIS = new ObjectInputStream(sock.getInputStream());

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            User user = null;

            /**
            try {
                while ((user = objIS.readObject()) != null) {
                    user = objIS.readObject();
                    System.out.println("read object" + user);

                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
               **/

            // fix check for object input stream before reading
            while(sock.isConnected()){
                try{
                    user = (User) objIS.readUnshared();
                    System.out.println("Read object" + user);
                    sendAuthResponse(user);

                }catch (Exception ex){
                    ex.printStackTrace();
                    continue;

                }

            }
        }// end of run function
        public void sendAuthResponse(User user) throws InterruptedException {
            // login
            System.out.println(user);
            // TODO connect to database
            if(user.getName().equals("LOGIN")){
                // send True or False for logging
                boolean isValid =  users.contains(user);
                if(isValid){
                    writer.println("TRUE");
                    writer.flush();
                    System.out.print(isValid);
                    // The server auth server should shut down after login
                    exit(1);
                }else{
                    writer.println("FALSE");
                    writer.flush();
                    System.out.print(isValid);
                }
//                writer.println("");
            }else{
                // Sign Up
                // TODO insert to database
                users.add(user);
                writer.println("TRUE");
                writer.flush();
                System.out.println("adding user to data base");
                exit(2);
            }
    }
    }

}
