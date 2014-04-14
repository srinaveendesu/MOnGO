package project.finalyear.database;

public class Users {
	  private long id;
	  private String name;
	  private String encryptedPwd;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }
	  
	  public String getPwd() {
		  return encryptedPwd;
	  }
	  
	  public void setPwd(String pwd) {
		  this.encryptedPwd = pwd;
	  }

	  @Override
	  public String toString() {
	    return name;
	  }

	public String toSyncString() {
		return getName()+"\t"+getPwd()+"\n";
	}

} 
