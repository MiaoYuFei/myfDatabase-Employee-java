package myfDatabase;

public class DBValueBag {
	public boolean byes = false;
	public String rID;
	public String rName;
	public String rAge;
	public String rBirthday;
	public String rAddress;
	
	public DBValueBag(boolean byes, String rID, String rName, String rAge, String rBirthday, String rAddress) {
		this.byes = byes;
		this.rID = rID;
		this.rName = rName;
		this.rAge = rAge;
		this.rBirthday = rBirthday;
		this.rAddress = rAddress;
	}
	
}
