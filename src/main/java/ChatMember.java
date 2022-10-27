import java.io.Serializable;


public class ChatMember implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nickname;
//    private String seq;
    private String userSeq;
    private String userIdWhoSent;
    private String expertSeq;

    private String message;
    private String post_seq;
    private String command;
    private String recipient;
    private String chat_room_seq;
    private String profile_img;
    private int viewType;
    private String now_time;
    private int msg_count;

    private String userProfileImage;

    private String clientOrExpert;

    private String orderType;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

//    public String getSeq() {
//        return seq;
//    }
//
//    public void setSeq(String seq) {
//        this.seq = seq;
//    }


    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getNow_time() {
        return now_time;
    }

    public String getClientOrExpert() {
        return clientOrExpert;
    }

    public void setClientOrExpert(String clientOrExpert) {
        this.clientOrExpert = clientOrExpert;
    }

    public void setNow_time(String now_time) {
        this.now_time = now_time;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserIdWhoSent() {
        return userIdWhoSent;
    }

    public void setUserIdWhoSent(String userIdWhoSent) {
        this.userIdWhoSent = userIdWhoSent;
    }

    public String getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(String userSeq) {
        this.userSeq = userSeq;
    }

    public String getExpertSeq() {
        return expertSeq;
    }

    public void setExpertSeq(String expertSeq) {
        this.expertSeq = expertSeq;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPost_seq() {
        return post_seq;
    }

    public void setPost_seq(String post_seq) {
        this.post_seq = post_seq;
    }

    public String getCommand(){
        return command;
    }

    public void setCommand(String command){
        this.command= command;
    }

    public String getRecipient(){
        return recipient;
    }

    public void setRecipient(String recipient){
        this.recipient= recipient;
    }

    public String getChat_room_seq(){
        return chat_room_seq;
    }

    public void setChat_room_seq(String chat_room_seq){
        this.chat_room_seq= chat_room_seq;
    }

    public String getProfile_img(){
        return profile_img;
    }

    public void setProfile_img(String profile_img){
        this.profile_img= profile_img;
    }

    public int getViewType(){
        return viewType;
    }

    public void setViewType(int viewType){
        this.viewType= viewType;
    }


    public int getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(int msg_count){
        this.msg_count= msg_count;
    }

}
