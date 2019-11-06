/*
 * 
 */
package com.iaas.sms.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.iaas.sms.converter.ConverterFacade;
import com.iaas.sms.dto.UserDTO;
import com.iaas.sms.model.Authority;
import com.iaas.sms.model.User;
import com.iaas.sms.repository.UserRepository;
import com.iaas.sms.security.service.BasicUserDetailsService;
import com.iaas.sms.service.UserService;
import com.iaas.sms.util.ApiResponse;
import com.iaas.sms.util.UserValidator;
// TODO: Auto-generated Javadoc

/**
 * The Class SignUpController.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/registration")
public class SignUpController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

	/** The service. */
	private final UserService service;

	@Autowired
	private UserRepository userRepository;

	/** The converter facade. */
	private final ConverterFacade converterFacade;

	/** The user validator. */
	@Autowired
	private UserValidator userValidator;

	@Autowired
	BasicUserDetailsService basicUserDetailsService;

	@Autowired
	RestTemplate restTemplate;

	RestTemplate restTemplate2;

	/**
	 * Instantiates a new sign up controller.
	 *
	 * @param service         the service
	 * @param converterFacade the converter facade
	 */
	@Autowired
	public SignUpController(final UserService service, final ConverterFacade converterFacade) {
		this.service = service;
		this.converterFacade = converterFacade;
	}

	/**
	 * Registration.
	 *
	 * @param model the model
	 * @return the api response
	 */
	@GetMapping("/registration")
	public ApiResponse<String> registration(Model model) {
		model.addAttribute("userForm", new User());
//		model.setId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));

		return new ApiResponse<>(HttpStatus.CONFLICT.value(), "METHOD type GET", null);
	}

	/**
	 * Sign up.
	 *
	 * @param dto           the dto
	 * @param bindingResult the binding result
	 * @return the api response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ApiResponse<UserDTO> signUp(@RequestBody final User dto, BindingResult bindingResult) throws Exception {
		userValidator.validate(dto, bindingResult);

		if (bindingResult.hasErrors()) {

			return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Bad requast from client.", null);
		}

		Collection<? extends GrantedAuthority> authValue = dto.getAuthorities();

		if (null != authValue) {
			authValue.forEach(role -> {

				if (role.getAuthority().equals("ROLE_ADMIN")) {
					dto.setAuthorities(Arrays.asList(Authority.ROLE_ADMIN));
					dto.setRole("ROLE_ADMIN");
				}
			});
		} else {
			dto.setAuthorities(Arrays.asList(Authority.ROLE_USER));
			dto.setRole("ROLE_USER");
		}

		service.create(dto);
		System.out.println("User saved successfully....");
		logger.info("userForm sucessfully saved");

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		headers.setAccessControlAllowOrigin("*");
		headers.setOrigin("*");
		List<HttpMethod> al = new ArrayList();
		al.add(HttpMethod.DELETE);
		al.add(HttpMethod.PUT);
		al.add(HttpMethod.POST);
		al.add(HttpMethod.GET);
		headers.setAccessControlAllowMethods(al);

		HttpEntity<?> entity = new HttpEntity<Object>(dto, headers);

		restTemplate2 = new RestTemplate();

		/*
		 * ResponseEntity<String> result7 =
		 * restTemplate2.exchange(Constants.ADMIN_APPLICATION, HttpMethod.POST, entity,
		 * String.class);
		 */

		// http://ADMIN-TENANT-SERVICE:2137/admintask/users/signup

		/*
		 * ResponseEntity<String> result1 =
		 * restTemplate2.exchange(Constants.MULTITENANCY_APPLICATION, HttpMethod.POST,
		 * entity, String.class);
		 */

		ResponseEntity<String> result = restTemplate2.exchange("http://localhost:2137/admintask/users/signup",
				HttpMethod.POST, entity, String.class);
		/*
		 * ResponseEntity<String> result1 = restTemplate2.exchange(
		 * "http://localhost:2171/paasmultitenant/iaasmultitenant", HttpMethod.POST,
		 * entity, String.class);
		 */
		return new ApiResponse<>(HttpStatus.OK.value(), "User saved successfully.", result.toString());

	}

	@GetMapping("/user/{userName}")
	public void isUserExist(@PathVariable String userName) throws Exception {

		if (basicUserDetailsService.isUserExist(userName)) {
			throw new Exception("User name already exist");
		}
	}

	@DeleteMapping("/deleteUser/{userName}")
	public String deleteUser(@PathVariable String userName) {
		User user = userRepository.findByUsername(userName);
		userRepository.delete(user);
		return "User Deleted Successfully";
	}

	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
