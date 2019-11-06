package com.iaas.sms.multitenant.config;

import java.util.ArrayList;
import java.util.Collections;

public class RandomNumGenerator {
	 ArrayList numbersList = new ArrayList ();
	    public RandomNumGenerator(int start,int end) {
	        for(int x=start;x<=end;x++)
	            numbersList.add(x);
	        Collections.shuffle(numbersList);
	    }
	    public int generateNewRandom(int n) {
	        return (Integer) numbersList.get(n);
	    }
}