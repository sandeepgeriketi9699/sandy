package com.sandeep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandeep.service.HelloService;

@RestController
@RequestMapping("/rest")
public class HelloController {
	@Autowired
	HelloService helloservice;

	@GetMapping("/names")
	public List<String> getAllNames() {
		System.out.println("************ Entered getAllNames()************");
		return helloservice.getAllNames();

	}
}
