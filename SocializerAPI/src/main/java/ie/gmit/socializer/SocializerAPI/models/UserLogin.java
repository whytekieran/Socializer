package ie.gmit.socializer.SocializerAPI.models;

//Simple class used as a JSON placeholder when client sends login request
public class UserLogin {
	
	private String email;
	private String password;
	
	//Constructors
	public UserLogin(){
		
	}

	public UserLogin(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	//Getters and Setters
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
