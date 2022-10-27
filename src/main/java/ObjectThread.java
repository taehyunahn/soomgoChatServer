import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.*;
import java.util.List;

public class ObjectThread extends Thread {

    private List<ObjectThread> list;
    private List <UserClass> user_list;
    private Socket socket;

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public ObjectThread(Socket socket, List<ObjectThread> list, List<UserClass> user_list) throws IOException {
        this.socket = socket;
        this.list = list;
        this.user_list = user_list;

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
//        ChatMember chatMember = null;
        super.run();
        try {
            // 클라이언트 → 서버 : JSON형식의 String 값 받아서, 객체로 변환한다 (Gson 라이브러리 사용)

            // 클라이언트로부터 첫번째 연결 후 메세지를 받으면, 해당 스레드가 시작된다.
            // 이 부분을 채팅방 접속시로 바꿔야 겠다.
            // 즉, 클라이언트에서 소켓 연결되는 시점에 바로 메세지를 보내지 말고, 채팅방에 들어가면 메세지를 보내라.
            String msgFromClient = bufferedReader.readLine();
            System.out.println("ObjectThread : bufferedReader.readLine() = " + msgFromClient);

            Gson gson = new Gson();
            ChatMember chatMember = gson.fromJson(msgFromClient, ChatMember.class);
            String nickName = chatMember.getNickname();
            String chat_rooms_seq = chatMember.getChat_room_seq();
            String id_userSeq = chatMember.getUserSeq();

            UserClass user= new UserClass(nickName, chat_rooms_seq, id_userSeq, socket, bufferedReader, printWriter);
            user_list.add(user);
//            Notify_JOIN(chatMember); // 해당 채팅방에 접속해서 메세지를 읽었음을 전송함.
            user.start();

//            // 요청서를 보내고 notification 띄우기 위한 Thread
//            UserClass2 user2 = new UserClass2(nickName, id_userSeq, socket, bufferedReader, printWriter);
//            user_list2.add(user2);
//            user2.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 사용자 정보를 관리하는 클래스
    public class UserClass extends Thread {
        String nickName;
        String chat_rooms_seq;
        String id_seq;
        String id_userSeq;
        String id_expertSeq;
        Socket socket;
        FileInputStream fis;
        FileOutputStream fos;

        BufferedReader bufferedReader;
//        BufferedWriter bufferedWriter;
        PrintWriter printWriter;

        public UserClass(String nickName, String chat_rooms_seq, String id_userSeq,
                         Socket socket, BufferedReader bufferedReader, PrintWriter printWriter) {
            try {
                this.nickName = nickName;
                this.chat_rooms_seq = chat_rooms_seq;
                this.id_userSeq = id_userSeq;
                this.socket = socket;
                this.bufferedReader = bufferedReader;
                this.printWriter = printWriter;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 사용자로부터 메세지를 수신받는 스레드
        public void run() {
            try {
                while (true) {
                    String msgFromClient = bufferedReader.readLine();
                    System.out.println("UserClass(Thread) : msgFromClient = " + msgFromClient);

                    // [채팅방에서 메세지 보내기를 통해 받아온 값이라면]
                    Gson gson = new Gson();
                    ChatMember chatMember = gson.fromJson(msgFromClient, ChatMember.class);

                    String orderType = chatMember.getOrderType();
                    String messageF = chatMember.getMessage(); // 메세지
                    String id_userSeqF = chatMember.getUserSeq(); // 채팅 작성자의 seq 번호
                    String chat_room_seqF = chatMember.getChat_room_seq(); // 채팅방의 seq 번호
                    String commandF = chatMember.getCommand(); // 메세지 동작에 대한 설명
                    String recipientF = chatMember.getRecipient();


                    if(orderType.equals("request")){ //요청서를 보낸 경우
                        sendNotificationInfo(chatMember);

                    } else if(orderType.equals("quote")){
                        sendNotificationInfo(chatMember);


                    } else if (orderType.equals("chat")) { // 채팅인 경우
                        setChat_rooms_seq(chat_room_seqF);

                        // case 1. 클라이언트가 채팅방에서 나갔을때
                        if (commandF.equals("EXIT")) {
                            list.remove(ObjectThread.this);
                            user_list.remove(this);
//                            socket.close(); // 확인필요
                            System.out.println("user_list.size() = " + user_list.size());
//                            break;
                        }

                        // case 2. 채팅방 처음 참여시
                        else if (commandF.equals("JOIN")) { //
                            Notify_JOIN(chatMember);
                        }

                        // case 3. 클라이언트의 메세지를 받는 일반적인 상황
                        else if (commandF.equals("SEND")){
                            int type = 1; // 일반 텍스트 전송 시! (아무 조건 없음)
                            // JDBC 연결 부분
                            try {
                                Connection connection = DriverManager.getConnection("jdbc:mariadb://54.180.133.35:3306/soomgo", "peter", "1234");
                                Statement statement = connection.createStatement();
                                ResultSet resultSet = statement.executeQuery(
                                        "INSERT INTO chat_messages (chat_rooms_seq, userIdWhoSent, userIdWhoReceive, messages, type) " +
                                                "VALUES (" + chat_room_seqF + ", " + id_userSeqF + " , '" + recipientF + " ', '" + messageF + "', '" + type + "')");

                                ResultSet resultSet1 = statement.executeQuery(
                                  "SELECT seq, chat_rooms_seq, userIdWhoSent, userIdWhoReceive FROM chat_messages ORDER BY sentDate DESC LIMIT 1"
                                );
                                String seq = "";
                                String roomNumber = "";
                                String userIdWhoSent = "";
                                String userIdWhoReceive = "";
                                while(resultSet1.next()){
                                    seq = resultSet1.getString(1);
                                    roomNumber = resultSet1.getString(2);
                                    userIdWhoSent = resultSet1.getString(3);
                                    userIdWhoReceive = resultSet1.getString(4);

                                    System.out.println(seq + " " + roomNumber + " " + userIdWhoSent + " " + userIdWhoReceive);
                                }

                                ResultSet resultSet2 = statement.executeQuery(
                                        "INSERT INTO unread (message_Id, room_Id, sender_Id, receiver_Id) " +
                                                "VALUES (" + seq + ", " + roomNumber + " , '" + userIdWhoSent + "' , '" + userIdWhoReceive + "')");


                                statement.close(); // PreparedStatement stmt
                                connection.close(); // Connection conn

                                oneChat(chatMember);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
//                        try {
//                            oneChat(chatMember);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
            } catch (EOFException e) {
                // ... this is fine
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



    public void setChat_rooms_seq(String chat_rooms_seq) {
        this.chat_rooms_seq = chat_rooms_seq;
    }
    }

    public void oneChat(ChatMember chatMember) throws IOException {
        System.out.println("oneChat : user_list.size() = " + user_list.size());
        for(UserClass user : user_list){
            try{
//                if(!user.id_userSeq.equals(chatMember.getUserSeq())){
                    Gson gson = new Gson();
                    String userJson = gson.toJson(chatMember);


                    user.printWriter.println(userJson);
                    System.out.println("oneChat : 보내는 값 = " + userJson);
//                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public void Notify_JOIN(ChatMember chatMember) throws IOException {
        System.out.println("Notify_JOIN : user_list.size() = " + user_list.size());
        for(UserClass user : user_list){
            try{
                if(user.chat_rooms_seq != null){
                    if(!user.id_userSeq.equals(chatMember.getUserSeq())){
                        Gson gson = new Gson();
                        String userJson = gson.toJson(chatMember);
                        user.printWriter.println(userJson);
                        System.out.println("Notify_JOIN : JSON 형태로 보내는 값 = " + userJson);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public void sendNotificationInfo(ChatMember chatMember) throws IOException {
        for(UserClass user : user_list){
            try{
                if(!user.id_userSeq.equals(chatMember.getUserSeq())){ // 메세지를 받는 Thread의 userSeq와 메세지를 서버로 보낸 userSeq가 다르다면, 메세지를 보내라.
                    Gson gson = new Gson();
                    String userJson = gson.toJson(chatMember);
                    user.printWriter.println(userJson);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


}
