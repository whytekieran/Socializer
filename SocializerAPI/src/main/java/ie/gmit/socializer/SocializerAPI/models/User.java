package ie.gmit.socializer.SocializerAPI.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.datastax.driver.core.TimestampGenerator;
import com.datastax.driver.core.utils.UUIDs;

public class User {

	//Instance variables
	private UUIDs uuid;
	private String email;
	private String password_hash;
	private String hash_secret;
	private String firstname;
	private String surname;
	private String slug;
	private Date dob;
	private String phone_number;
	private String address1;
	private String address2;
	private String address_city;
	private String address_county;
	private String address_country;
	private String profilePicUuid;
	private String backgroundPicUuid;
	private List<String> workplaces;
	private List<String> professionalSkills;
	private List<String> livedIn;
	private List<String> connections;
	private long created;    //hold long time stamps when user is created and updated
	//private long updated;	//used to hold value when user updated his/her details
	private DateFormat dateFormat;	//Used to format string 'dob' into a valid date format
	//private Timeline userTL; //User has a Timeline
	
	//Empty constructor needed if we are returning JSON or XML responses
	//Important for XML or JSON conversion
	//Jersey framework needs to be able to create a new instance of the class hence the empty constructor
	public User()
	{
		
	}
		
	//Constructor ...could maybe add more? Multiple overloaded constructors?
	public User(String email, String firstname, String surname, String dob, String phone_number, 
			    String address1, String address2, String address_city, 
			    String address_county, String address_country) {
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		this.email = email;
		this.firstname = firstname;
		this.surname = surname;
		this.address1 = address1;
		this.address2 = address2;
		this.address_city = address_city;
		this.address_county = address_county;
		this.address_country = address_country;
		this.phone_number = phone_number;
		try {
			this.dob = dateFormat.parse(dob);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//Getters and Setters
	public UUIDs getUuid() {
		return uuid;
	}

	public void setUuid(UUIDs uuid) {
		this.uuid = uuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}

	public String getHash_secret() {
		return hash_secret;
	}

	public void setHash_secret(String hash_secret) {
		this.hash_secret = hash_secret;
	}

	public String getFirstName() {
		return firstname;
	}

	public void setFirstName(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddressCity() {
		return address_city;
	}

	public void setAddressCity(String addressCity) {
		this.address_city = addressCity;
	}

	public String getAddressCounty() {
		return address_county;
	}

	public void setAddressCounty(String addressCounty) {
		this.address_county = addressCounty;
	}

	public String getAddressCountry() {
		return address_country;
	}

	public void setAddressCountry(String addressCountry) {
		this.address_country = addressCountry;
	}

	public String getProfilePicUuid() {
		return profilePicUuid;
	}

	public void setProfilePicUuid(String profilePicUuid) {
		this.profilePicUuid = profilePicUuid;
	}

	public String getBackgroundPicUuid() {
		return backgroundPicUuid;
	}

	public void setBackgroundPicUuid(String backgroundPicUuid) {
		this.backgroundPicUuid = backgroundPicUuid;
	}

	public List<String> getWorkplaces() {
		return workplaces;
	}

	public void setWorkplaces(List<String> workplaces) {
		this.workplaces = workplaces;
	}

	public List<String> getProfessionalSkills() {
		return professionalSkills;
	}

	public void setProfessionalSkills(List<String> professionalSkills) {
		this.professionalSkills = professionalSkills;
	}

	public List<String> getLivedIn() {
		return livedIn;
	}

	public void setLivedIn(List<String> livedIn) {
		this.livedIn = livedIn;
	}

	public List<String> getConnections() {
		return connections;
	}
	
	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public void setConnections(List<String> connections) {
		this.connections = connections;
	}
}
