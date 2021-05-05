package code;

public class theRecord {

    private String recordID = null; //primary key
    private String username = null;
    private String title = null;
    private String creationdate = null;
    private String passwordprotection = null;


    public theRecord(String recordID, String username, String title, String creationdate, String passwordprotection) {
        this.recordID = recordID;
        this.username = username;
        this.title = title;
        this.creationdate = creationdate;
        this.passwordprotection = passwordprotection;
    }
    public String getRecordID(){return recordID;}
    public String getUsername(){return username;}
    public String getTitle(){return title;}
    public String getCreationdate(){return creationdate;}
    public String getPasswordprotection(){return passwordprotection;}


}
