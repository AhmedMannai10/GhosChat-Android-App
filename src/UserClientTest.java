import java.io.*;
import java.net.Socket;
import entity.User;
public class UserClientTest {
    BufferedReader reader    ;
    ObjectOutputStream objectWriter;




    public static void main(String[] args)throws InterruptedException {

        try{
            Socket sock = new Socket("192.168.1.12", 5000);
            InputStreamReader inputStream = new InputStreamReader(sock.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            ObjectOutputStream objectOS = new ObjectOutputStream(sock.getOutputStream()) ;
            User ahmed = new User("LOGIN", "ahmedmannai@gmail.com", "testpaswod");
            User eliot = new User("Eliot", "eliot@gmail.com", "eliot123");
            User ahmed1 = new User("LOGIN", "ahmedmannai@gmail.com", "ahmed");
            objectOS.writeObject(ahmed);
            System.out.println("object sent");

//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
//                    1024);
//            byte[] buffer = new byte[1024];
//
//            int bytesRead;
//            InputStream inputStream1 = sock.getInputStream();
//
//            /*
//             * notice: inputStream.read() will block if no data return
//             */
//            String response = "";
//            while ((bytesRead = inputStream1.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//                response += byteArrayOutputStream.toString("UTF-8");
//            }
//            System.out.println(response);
            String message ;
//            while((message = reader.readLine()) != null){
            System.out.println(reader.readLine());

//            }


            Thread.sleep(200);
            objectOS.writeObject(eliot);
            System.out.println("object sent");

            System.out.println(reader.readLine());
//            Thread.sleep(200);
//            objectOS.writeObject(ahmed1);
//            System.out.println("object sent");
//
//
//            System.out.println(reader.readLine());


        }catch(IOException ex){
            ex.printStackTrace();
        }


    }

}
