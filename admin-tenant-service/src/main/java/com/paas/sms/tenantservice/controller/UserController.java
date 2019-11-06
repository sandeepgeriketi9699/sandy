package com.paas.sms.tenantservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.paas.sms.tenantservice.document.Tenant;
import com.paas.sms.tenantservice.document.User;
import com.paas.sms.tenantservice.dto.TokenDTO;
import com.paas.sms.tenantservice.dto.UserDataDTO;
import com.paas.sms.tenantservice.exceptions.ExceptionResponse;
import com.paas.sms.tenantservice.model.CountryDetails;
import com.paas.sms.tenantservice.model.Email;
import com.paas.sms.tenantservice.model.OTP;
import com.paas.sms.tenantservice.model.SigninDTO;
import com.paas.sms.tenantservice.repository.SequenceRepository;
import com.paas.sms.tenantservice.security.JwtTokenProvider;
import com.paas.sms.tenantservice.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SequenceRepository sequenceRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Value("${email}")
	String Email_Id;

	@Value("${password}")
	String password;

	String licenseUri = "http://localhost:2175/paaslicense/license/deactivate/";
	String licenceUri = "http://localhost:2175/paaslicense/license/";
	String cartUri = "http://localhost:2170/paascart/cart/";
	String CouponUri = "http://localhost:2246/coupon/deleteUserCoupon/";

	private static final String HOSTING_SEQ_KEY = "userId";

	public static Map<String, Integer> countryDetails = new HashMap();

	static {
		String[] locales = Locale.getISOCountries();
		for (String countryCode : locales) {
			Locale locale = new Locale("", countryCode);
			PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
			Integer extensionNumber = phoneNumberUtil.getCountryCodeForRegion(locale.getCountry());
			countryDetails.put(locale.getDisplayCountry(), extensionNumber);

		}
	}

	@PostMapping("/isEnabled")
	public String isEnabled(@RequestBody User user) {
		return userService.isEnabled(user);
	}

	@GetMapping("/nameByToken/{token}")
	public String getUserName(@PathVariable String token) throws Exception {
		String userName = jwtTokenProvider.getUsername(token);
		if (userName != null) {
			return userName;
		}
		return "Un Authorized user";
	}

	@GetMapping("/generateToken/{userName}")
	public ResponseEntity<TokenDTO> generateToken(@PathVariable String userName) throws Exception {
		User user = userService.findByUsername(userName);
		String token = userService.generateToken(user);

		TokenDTO dto = new TokenDTO();
		dto.setTokenName(token);
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public SigninDTO login(@RequestBody SigninDTO dto) {
		String username = dto.getUsername();
		String pwd = dto.getPassword();
		return userService.signin(username, pwd);
	}

	@PutMapping("/changePassword/{userName}")
	public User changePassword(@RequestBody Tenant tenant, @PathVariable String userName) throws Exception {
		User user2 = userService.findByUsername(userName);
		String oldpwd = user2.getPasswordConfirm();
		if (oldpwd.equals(tenant.getOldPassword())) {
			user2.setPassword(tenant.getNewPassword());
			user2.setPasswordConfirm(tenant.getNewPassword());
			return userService.savePassword(user2);
		} 
			throw new Exception("Invalid old password");
		

	}

	@PutMapping("/resetPassword/{userName}")
	public User resetPassword(@RequestBody Tenant tenant, @PathVariable String userName) {
		User user2 = userService.findByUsername(userName);
		user2.setPassword(tenant.getNewPassword());
		user2.setPasswordConfirm(tenant.getNewPassword());
		return userService.savePassword(user2);

	}

	@PostMapping("/signup")
	public String signup(@RequestBody User user) throws Exception {
		System.out.println("**************** Entered into SignUp in AdminTask***********");
		user.setUserId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));
		return userService.signup(modelMapper.map(user, User.class));
	}

	@RequestMapping(value = "/confirm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView confirmAccount(@RequestParam("token") String confirmationToken) {
		String msg = userService.confirmUserAccount(confirmationToken);
		if (msg != null) {
			return verificationSuccess();
		}
			return verificationFailed();
		
	}

	public ModelAndView verificationSuccess() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accountVerified");
		return modelAndView;
	}

	public ModelAndView verificationFailed() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("invalidLink");
		return modelAndView;
	}

	@PostMapping("/forgotPassword/{userName}")
	public OTP forgotPassword(@PathVariable String userName) throws Exception {
		return  userService.forgotPassword(userName);
	}

	@RequestMapping(value = "/resetPassword", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView PasswordReset() {

		return resetPassword();
	}

	public ModelAndView resetPassword() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("resetpwd");
		return modelAndView;
	}

	@GetMapping("/findAllUsers")
	public List<User> findAllUsers() {
		if (userService.findAllUsers() != null) {
			return userService.findAllUsers();
		}
		return Collections.emptyList();
	}

	@PutMapping("/user/update/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") String userId,
			@Valid @RequestBody User userDetails, HttpServletRequest request) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String token = request.getHeader("Authorization");
		System.out.println(" ********* Token *********** ::" + token);
		headers.set("Authorization", token);

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		User user = userService.findById(userId);

		if (userDetails.getMobilenumber() != null) {
			user.setMobilenumber(userDetails.getMobilenumber());
		}

		if (userDetails.getFirstname() != null) {
			user.setFirstname(userDetails.getFirstname());
		}
		if (userDetails.getLastname() != null) {
			user.setLastname(userDetails.getLastname());
		}
		if (!(user.getEmail().equalsIgnoreCase(userDetails.getEmail()))) {
			user.setVerified(false);
			userService.emailUpdate(userDetails);
			RestTemplate restTemplate = new RestTemplate();
			System.out.println("URL::" + cartUri + "update/" + userDetails.getEmail() + "/" + user.getEmail());
			restTemplate.exchange(cartUri + "update/" + userDetails.getEmail() + "/" + user.getEmail(), HttpMethod.PUT,
					entity, String.class);
			restTemplate.exchange(licenceUri + "update/" + userDetails.getEmail() + "/" + user.getEmail(),
					HttpMethod.PUT, entity, String.class);
		}
		if (userDetails.getEmail() != null) {

			user.setEmail(userDetails.getEmail());
		}

		User updatedTenant = userService.save(user);

		return ResponseEntity.ok(updatedTenant);

	}

	@DeleteMapping("/delete/{userName}")
	public ResponseEntity<?> deleteUser(@PathVariable String userName, HttpServletRequest request) {
		User user = userService.findByUsername(userName);
		String emailId = user.getEmail();

		userService.deleteUser(user);
		System.out.println("User is deleted: " + user);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String token = request.getHeader("Authorization");
		System.out.println(" ********* Token *********** ::" + token);
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(licenseUri + emailId, HttpMethod.DELETE, entity, String.class);
		restTemplate.exchange(CouponUri + emailId, HttpMethod.DELETE, entity, String.class);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(new ExceptionResponse(new Date(), "Record deleted", userName));
		// return result;
	}

	@GetMapping("/tenants/email/{email}")
	public ResponseEntity<User> findByEmail(@PathVariable(value = "email") String emailId) {
		System.out.println("EmailId::" + emailId);
		User tenant = userService.findByEmail(emailId);
		return ResponseEntity.ok().body(tenant);
	}

	@RequestMapping(value = "/sendMail", method = { RequestMethod.POST })
	public void sendMail(@RequestBody Email email) throws MessagingException {
		try {
			System.out.println("Entered into send mail");

			// String Email_Id = "paas.team2019@gmail.com";
			// String password = "vytqiietngjpytfw";
			// Set mail properties
			Properties props = System.getProperties();
			String host_name = "smtp.gmail.com";
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host_name);
			// props.put("mail.smtp.user", Email_Id);
			// props.put("mail.smtp.password", password);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Email_Id, password);
				}
			});

			// Session session = Session.getDefaultInstance(props);
			// MimeMessage message = new MimeMessage(session);
			Message message = new MimeMessage(session);
			// MimeMessageHelper helper = new MimeMessageHelper(message, true);

			try {
				// Set email data
				message.setFrom(new InternetAddress(Email_Id, "PaaS"));
				System.out.println("from address  ::" + Email_Id);

				String[] to = email.getEmails();
				InternetAddress[] toAddress = new InternetAddress[to.length];

				/*
				 * message.addRecipients(Message.RecipientType.TO,
				 * InternetAddress.parse("spsoft.krishnareddy@gmail.com")); for (int i = 0; i <
				 * to.length; i++) { toAddress[i] = new InternetAddress(to[i]); }
				 */

				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Email_Id));
				for (int i = 0; i < to.length; i++) {
					toAddress[i] = new InternetAddress(to[i]);
					System.out.println("To address  ::" + toAddress[i]);
				}

				for (int i = 0; i < toAddress.length; i++) {
					message.addRecipient(Message.RecipientType.BCC, toAddress[i]);
				}
				message.setSubject(email.getSubject());
				message.setText(email.getMessage());
				/*
				 * Transport transport = session.getTransport("smtp");
				 * transport.connect("smtp.gmail.com", 587, Email_Id, password);
				 * transport.sendMessage(message, message.getAllRecipients());
				 * transport.close();
				 */
				Transport.send(message);
				System.out.println("Mail sent successfully");
			} catch (MessagingException ex) {
				// Logger.getLogger(emailHtmlTemp.class.getName()).log(Level.SEVERE, null, ex);
				ex.printStackTrace();
			} catch (Exception ae) {
				ae.printStackTrace();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@GetMapping("/getCountries")
	public List<CountryDetails> getCountryDetails() {
		String[] locales = Locale.getISOCountries();
		CountryDetails countryDetails = null;
		List<CountryDetails> countryList = new ArrayList<CountryDetails>();
		for (String countryCode : locales) {
			Locale locale = new Locale("", countryCode);
			PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
			int extensionNumber = phoneNumberUtil.getCountryCodeForRegion(locale.getCountry());
			countryDetails = new CountryDetails();
			countryDetails.setCountryCode(locale.getCountry());
			countryDetails.setCountryName(locale.getDisplayCountry());
			countryDetails.setExtensionNumber(extensionNumber);
			countryList.add(countryDetails);
		}
		return countryList;
	}

	@GetMapping("/getCountries/{countryName}")
	public Integer getExtnBasedOnCountryName(@PathVariable String countryName) {
		return countryDetails.get(countryName);
	}

	/*
	 * @DeleteMapping(value = "/{username}")
	 * 
	 * @PreAuthorize("hasRole('ROLE_ADMIN')") public String delete(@PathVariable
	 * String username) { userService.deleteUser(username); return username; }
	 */

	@GetMapping(value = "/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public UserDataDTO search(@PathVariable String username) {
		return modelMapper.map(userService.search(username), UserDataDTO.class);
	}

	@GetMapping(value = "/me")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	public UserDataDTO whoami(HttpServletRequest req) {
		return modelMapper.map(userService.whoami(req), UserDataDTO.class);
	}

	@GetMapping("/refresh")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	public String refresh(HttpServletRequest req) {
		return userService.refresh(req.getRemoteUser());
	}

	@GetMapping("/isUser/{userName}")
	public void isUserExist(@PathVariable String userName) throws Exception {

		User user = userService.findByUsername(userName);
		if (user != null) {
			throw new Exception("User name is already exist");
		}

	}

	@GetMapping("/emailExist/{emailId}")
	public void isMailExist(@PathVariable String emailId) throws Exception {

		User user1 = userService.findByEmail(emailId);
		if (user1 != null) {
			throw new Exception("User email is already exist");
		}

	}
}
