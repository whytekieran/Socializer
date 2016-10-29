/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.gmit.socializer.SocializerAPI.responseObjects;

/**
 *
 * @author ciaran
 */
//Error response object for failed user retrieval
public class ResponseWithMessage extends Response {
		
	private String result;
	
	//Constructor
	public ResponseWithMessage(String success, String result){
		super.success = success;
		this.result = result;
	}

	//Getters and Setters
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
