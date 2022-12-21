import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import entity.User;

import static java.lang.System.exit;

public class AuthServer {

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


            Connection conn = DataBaseHelper.connect(UserTable.NAME);

            if(user.getName().equals("LOGIN")){
                // send True or False for logging
                User dbUser = DataBaseHelper.getUserFromEmail(conn, user.email);
                System.out.println(dbUser);
                if(dbUser == null){
                    writer.println("false");
                    return;
                }

                boolean isValid =  dbUser.equals(user);
                if(isValid){
                    writer.println("True");
                    writer.flush();
                    System.out.print(isValid);
                    // The server auth server should shut down after login
                    exit(1);
                }else{
                    writer.println("False");
                    writer.flush();
                    System.out.print(isValid);
                }
            }else{
                // Sign Up
                // TODO insert to database

                Boolean result = DataBaseHelper.insert(conn, user);
//                users.add(user);
                if(result){
                    // insert successfully
                    writer.println("True");
                    System.out.println("adding user to data base");
                }else{
                  // There is already another user with these details
                    writer.println("false");
                }
                writer.flush();
                exit(2);
            }
        }
    }

}
