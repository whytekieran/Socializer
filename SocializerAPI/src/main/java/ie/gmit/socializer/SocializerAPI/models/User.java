package ie.gmit.socializer.SocializerAPI.models;

import java.sql.Timestamp;
import java.util.*;

public class User {

	//Instance variables
	private String uuid;
	private String email;
	private String password_hash;
	private String hash_secret;
	private String firstName;
	private String surname;
	private String slug;
	private Date dob;
	private String phoneNumber;
	private String address1;
	private String address2;
	private String addressCity;
	private String addressCounty;
	private String addressCountry;
	private String addressJson;
	private String profilePicUuid;
	private String backgroundPicUuid;
	private List<String> workplaces;
	private List<String> professionalSkills;
	private List<String> livedIn;
	private List<String> connections;
	private Timestamp created;//java.sql  
	private Timestamp updated;
	
	//Constructor ...could maybe add more? Multiple overloaded constructors?
	public User(String email, String firstName, String surname, Date dob, String phoneNumber, 
			    String address1, String address2, String addressCity, 
			    String addressCounty, String addressCountry) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.surname = surname;
		this.dob = dob;
		this.phoneNumber = phoneNumber;
		this.address1 = address1;
		this.address2 = address2;
		this.addressCity = addressCity;
		this.addressCounty = addressCounty;
		this.addressCountry = addressCountry;
	}

	//Getters and Setters
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
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
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressCounty() {
		return addressCounty;
	}

	public void setAddressCounty(String addressCounty) {
		this.addressCounty = addressCounty;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public String getAddressJson() {
		return addressJson;
	}

	public void setAddressJson(String addressJson) {
		this.addressJson = addressJson;
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

	public void setConnections(List<String> connections) {
		this.connections = connections;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
}
