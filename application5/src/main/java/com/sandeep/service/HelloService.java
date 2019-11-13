package com.sandeep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandeep.repository.HelloRepository;

@Service
public class HelloService {
	@Autowired
	HelloRepository hellorepository;
	
public List <String> getAllNames(){
	return hellorepository.getAllNames();
}

}
