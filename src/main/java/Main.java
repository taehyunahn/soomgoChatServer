import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

//        try {
//
//        Connection connection =  DriverManager.getConnection("jdbc:mariadb://54.180.133.35:3306/soomgo", "peter", "1234");
//
//        Statement statement  = connection.createStatement();
//
//        ResultSet resultSet = statement.executeQuery("select * from userInfo");
//
//        while (resultSet.next()){
//            System.out.println(resultSet.getString("seq"));
//        }
//        } catch (Exception e){
//            e.printStackTrace();
//
//         }

        new ObjectServerThread();


    }

    static class ObjectServerThread
    {
        private ServerSocket serverSocket;
        private List<ObjectThread> list;
        public static List <ObjectThread.UserClass> user_list;


        public ObjectServerThread(){
            try{
                serverSocket= new ServerSocket (5002);
                System.out.println("Main : serverSocket 생성");
                list = new ArrayList<ObjectThread>();
                user_list = new  ArrayList<ObjectThread.UserClass>();
                System.out.println("Main : ObjectThread → list, arrayList 생성");
                System.out.println("Main : ObjectThread.UserClass → user_list, arrayList 생성");
                while(true){
                    Socket socket = serverSocket.accept();
                    System.out.println("Main : serverSocket으로 클라이언트 상대 socket 생성");
                    ObjectThread handler = new  ObjectThread(socket, list, user_list);  //스레드를 생성한 것이랑 동일함!
                    handler.start();  //스레드 시작- 스레드 실행
                    System.out.println("Main : ObjectThread 생성 후 실행");
                    list.add(handler);  //핸들러를 담음( 이 리스트의 개수가 클라이언트의 갯수!!)
                }//while
            }catch(IOException e){
                e.printStackTrace();
            }
        }




    }


}
