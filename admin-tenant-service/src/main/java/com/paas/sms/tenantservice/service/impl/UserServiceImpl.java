package com.paas.sms.tenantservice.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.paas.sms.tenantservice.document.Authority;
import com.paas.sms.tenantservice.document.ConfirmationToken;
import com.paas.sms.tenantservice.document.User;
import com.paas.sms.tenantservice.exceptions.CustomException;
import com.paas.sms.tenantservice.model.OTP;
import com.paas.sms.tenantservice.model.SigninDTO;
import com.paas.sms.tenantservice.repository.ConfirmationTokenRepository;
import com.paas.sms.tenantservice.repository.UserRepository;
import com.paas.sms.tenantservice.security.JwtTokenProvider;
import com.paas.sms.tenantservice.service.EmailSenderService;
import com.paas.sms.tenantservice.service.Sequence;
import com.paas.sms.tenantservice.service.UserService;

@PropertySource("classpath:application.properties")
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private Sequence sequence;

	private static final String HOSTING_SEQ_KEY = "tokenId";

	RestTemplate restTemplate2;

	@Value("${email}")
	String Email_Id;

	@Value("${password}")
	String password;

	@Override
	public User save(User user) {
		return userRepository.save(user);

	}

	@Override
	public User search(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new CustomException("User doesn't exist", HttpStatus.NOT_FOUND);
		}

		return user;
	}

	public String generateToken(User user) {
		return jwtTokenProvider.createToken(user.getUsername(), user.getAuthorities());
	}

	@Override
	public SigninDTO signin(String username, String password) {

		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new CustomException("Invalid credentials", HttpStatus.UNPROCESSABLE_ENTITY);
		}

		if (!user.isVerified() && "ROLE_USER".equals(user.getRole())) {
			throw new CustomException("Email verification is required to login", HttpStatus.UNPROCESSABLE_ENTITY);
		}

		if (username.equals(user.getUsername()) && password.equals(user.getPasswordConfirm())) {
			String email = user.getEmail();
			List<Authority> authorities = user.getAuthorities();
			String token = jwtTokenProvider.createToken(username,
					userRepository.findByUsername(username).getAuthorities());
			SigninDTO dto = new SigninDTO();
			dto.setAuthorities(authorities);
			dto.setToken(token);
			dto.setEmail(email);

			return dto;
		} else {
			throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public User findByEmail(String emailId) {

		User user = userRepository.findByEmail(emailId);
		if (user != null) {
			return user;
		}
		return null;
	}

	public String isEnabled(@RequestBody User user) {
		System.out.println("entered into isenabled method");
		user.setEnabled(true);
		userRepository.save(user);
		return "User mail is verified";
	}

	// String otp= new DecimalFormat("0000").format(new Random().nextInt(9999));

	public OTP forgotPassword(String userName) throws Exception {

		// new OTP();
		System.out.println("Entered into forgot password");
		User user = userRepository.findByUsername(userName);
		String otp = new DecimalFormat("0000").format(new Random().nextInt(9999));
		if (user != null) {
			OTP otp1 = new OTP();
			try {

				// String Email_Id = "paas.team2019@gmail.com";
				// "spsoft.krishnareddy@gmail.com";
				// String password = "vytqiietngjpytfw";
				String mail_subject = "Forgot Password";

				// Set mail properties
				Properties props = System.getProperties();
				String host_name = "smtp.gmail.com";
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", host_name);
				props.put("mail.smtp.user", Email_Id);
				props.put("mail.smtp.password", password);
				props.put("mail.smtp.port", "587");
				props.put("mail.smtp.auth", "true");

				Session session = Session.getDefaultInstance(props);

				MimeMessage message = new MimeMessage(session);

				try {
					// Set email data
					message.setFrom(new InternetAddress(Email_Id, "PaaS"));
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
					message.setSubject(mail_subject);
					message.setHeader("content-Type", "text/html;charset=\"ISO-8859-1\"");
					MimeMultipart multipart = new MimeMultipart();
					BodyPart messageBodyPart = new MimeBodyPart();

					// Set key values"
					Map<String, String> input = new HashMap<String, String>();
					input.put("Author", "spsoftglobal.com");
					input.put("Topic", "HTML Template for Email");
					input.put("Content In", "English");
					// HTML mail content
					// String htmlText = readEmailFromHtml("E:\\latest backend
					// code\\admin-tenant-service\\src\\main\\resources\\templates\\reset.jsp",input);
					// messageBodyPart.setContent(htmlText, "text/html");
					messageBodyPart.setText("Hi " + user.getFirstname().substring(0, 1).toUpperCase()
							+ user.getFirstname().substring(1) + "," + "\n" + "\n" + "Your verification code is: " + otp
							+ "\n" + "This OTP is valid for only 5 minutes." + "\n" + "\n"
							+ "Note : Do not Refresh the page." + "\n"
							+ "If you are having any issues with your account, please don't hesitate to contact us."
							+ "\n" + "\n" + "Thank You," + "\n" + "Team PaaS.");
					multipart.addBodyPart(messageBodyPart);
					// getOTP(otp);
					otp1.setEmail(user.getEmail());
					otp1.setOTP(otp);
					message.setContent(multipart);
					// Conect to smtp server and send Email

					Transport transport = session.getTransport("smtp");
					transport.connect(host_name, Email_Id, password);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();

					// Transport.send(message);
					System.out.println("Mail sent successfully...");

				} catch (MessagingException ex) {
					ex.printStackTrace();
					// Logger.getLogger(emailHtmlTemp.class.getName()).log(Level.SEVERE, null, ex);
				} catch (Exception ae) {
					ae.printStackTrace();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return otp1;
		} else {
			throw new Exception("Username doesn't exist");
		}

	}

	// Method to replace the values for keys
	public final String readEmailFromHtml(String filePath, Map<String, String> input) {
		String msg = readContentFromFile(filePath);

		try {
			Set<Entry<String, String>> entries = input.entrySet();
			for (Map.Entry<String, String> entry : entries) {
				msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return msg;
	}

	// Method to read HTML file as a String
	public String readContentFromFile(String fileName) {
		StringBuffer contents = new StringBuffer();

		try {
			// use buffering, reading one line at a time
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				reader.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return contents.toString();
	}

	@Override
	public User deleteUser(User user) {
		userRepository.delete(user);
		return user;
	}

	@Override
	public String signup(User user) throws Exception {

		System.out.println("*************** Entered into UserServiceImpl *************");

		if (!userRepository.existsByUsername(user.getUsername())) {
			System.out.println("*************** Entered into UserServiceImpl if block*************");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			ConfirmationToken confirmationToken = new ConfirmationToken(user);
			confirmationToken.setTokenid(sequence.getNextSequenceId(HOSTING_SEQ_KEY));
			confirmationTokenRepository.save(confirmationToken);
			/*
			 * SimpleMailMessage mailMessage = new SimpleMailMessage();
			 * mailMessage.setTo(user.getEmail());
			 * mailMessage.setSubject("Complete Registration!"); mailMessage.setText("Hi " +
			 * user.getFirstname().substring(0, 1).toUpperCase() +
			 * user.getFirstname().substring(1) + "," + "\n" + "\n" +
			 * "To confirm your account, please click here : " +
			 * "http://18.215.4.80:2137/admintask/users/confirm?token=" +
			 * confirmationToken.getConfirmationToken() + "\n" + "\n" + "Thank You," + "\n"
			 * + "Team SPSoft."); System.out.
			 * println("********************* Before Save Method *********************");
			 */

			// String Email_Id = "paas.team2019@gmail.com";
			// String password = "vytqiietngjpytfw";
			Properties prop = new Properties();
			prop.put("mail.smtp.host", "smtp.gmail.com");
			prop.put("mail.smtp.port", "587");
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.starttls.enable", "true"); // TLS

			Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Email_Id, password);
				}
			});

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(Email_Id, "PaaS"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
				message.setSubject("PaaS-Registration!");
				message.setText("Hi " + user.getFirstname().substring(0, 1).toUpperCase()
						+ user.getFirstname().substring(1) + "," + "\n" + "\n"
						+ "To confirm your account, please click here : "
						+ "http://18.215.4.80:2137/admintask/users/confirm?token="
						+ confirmationToken.getConfirmationToken() + "\n" + "\n" + "Thank You," + "\n" + "Team PaaS.");
				System.out.println("********************* Before Send Email *********************");
				Transport.send(message);
				System.out.println("********************* Email sent successfully *********************");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			userRepository.save(user);

			// emailSenderService.sendEmail(mailMessage);

			return jwtTokenProvider.createToken(user.getUsername(), user.getAuthorities());
		} else {
			throw new CustomException("User name is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
		}

	}

	public void emailUpdate(User user) throws Exception {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		ConfirmationToken confirmationToken = new ConfirmationToken(user);
		confirmationToken.setTokenid(sequence.getNextSequenceId(HOSTING_SEQ_KEY));
		confirmationTokenRepository.save(confirmationToken);
		/*
		 * SimpleMailMessage mailMessage = new SimpleMailMessage();
		 * mailMessage.setTo(user.getEmail());
		 * mailMessage.setSubject("Email Confirmation!"); mailMessage.setText("Hi " +
		 * user.getFirstname().substring(0, 1).toUpperCase() +
		 * user.getFirstname().substring(1) + "," + "\n" + "\n" +
		 * "To verify your email, please click here : " +
		 * "http://18.215.4.80:2137/admintask/users/confirm?token=" +
		 * confirmationToken.getConfirmationToken() + "\n" + "\n" + "Thank You," + "\n"
		 * + "Team SPSoft."); emailSenderService.sendEmail(mailMessage);
		 */

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Email_Id, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Email_Id, "PaaS"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
			message.setSubject("Email Confirmation!");
			message.setText("Hi " + user.getFirstname().substring(0, 1).toUpperCase() + user.getFirstname().substring(1)
					+ "," + "\n" + "\n" + "To verify your email, please click here : "
					+ "http://18.215.4.80:2137/admintask/users/confirm?token="
					+ confirmationToken.getConfirmationToken() + "\n" + "\n" + "Thank You," + "\n" + "Team PaaS.");
			System.out.println("********************* Before Send Email *********************");
			Transport.send(message);
			System.out.println("********************* Email sent successfully *********************");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	public String confirmUserAccount(String confirmationToken) {

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

		System.out.println("confirmationToken is ....:" + confirmationToken);
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		System.out.println("Token is ....:" + token);
		if (token != null) {
			User user = userRepository.findByUsername(token.getUser().getUsername());
			HttpEntity<?> entity = new HttpEntity<Object>(user, headers);

			System.out.println("User response is...: " + user);
			if (user != null) {
				user.setVerified(true);
				userRepository.save(user);
				confirmationTokenRepository.delete(token);
			}
			return "Your Email Verified Successfully : " + user.getEmail();
		} else {
			return null;
		}
	}

	@Override
	public Object whoami(HttpServletRequest req) {

		return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));

	}

	@Override
	public String refresh(String username) {
		return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAuthorities());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				getAuthority());

	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	@Override
	public User findByUsername(String userName) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(userName);
	}

	public User savePassword(User user) {

		return userRepository.save(user);
	}

	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User findById(String userId) {
		Optional optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent())
			return (User) optionalUser.get();
		else
			return null;
	}

}
