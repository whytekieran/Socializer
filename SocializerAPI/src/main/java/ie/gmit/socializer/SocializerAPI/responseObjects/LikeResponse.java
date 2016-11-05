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
public class LikeResponse extends Response {
    
    private int amount;
    
    public LikeResponse(String success, int amount){
        
        super.success = success;
        this.amount = amount;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
}
