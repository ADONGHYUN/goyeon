package com.dh.goyeon.user.impl;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class State {
	private String state;
	private String ranChar;
	

	public State() {
        this.ranChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    }
	
	public String getState() {
		return state;
	}
	
    public Boolean stateEquals(String newState) {
    	if (this.state.equals(state)) {
    		this.state=null;
    		return true;
    	} 
    	return false;
    }
    
    public String ranString() {
    	Random random = new Random();
    	StringBuilder sb = new StringBuilder(12);
    	for (int i=0; i<12; i++) {
    		 int index = random.nextInt(ranChar.length());
             sb.append(ranChar.charAt(index));
    	}
    	return this.state = sb.toString();
    }
}
