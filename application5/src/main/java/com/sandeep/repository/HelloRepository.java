package com.sandeep.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class HelloRepository {
	
public List<String>getAllNames(){
	return Arrays.asList("sandeep","jeevan","siva");
}


}


