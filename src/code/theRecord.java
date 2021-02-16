package code;

public class theRecord {

    private Integer recordID = null; //primary key
    private String username = null;
    private String title = null;
    private String date = null;
    private Boolean viewStatus = null;


    public theRecord(Integer recordID, String username, String title, String date, Boolean viewStatus) {
        this.recordID = recordID;
        this.username = username;
        this.title = title;
        this.date = date;
        this.viewStatus = viewStatus;
    }
    public String getRecordID(){return recordID.toString();}
    public String getUsername(){return username;}
    public String getTitle(){return title;}
    public String getDate(){return date;}
    public String getViewStatus(){return viewStatus.toString();}

    //may have to do setters as well as getters! not sure yet...

}
