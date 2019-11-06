package com.paas.sms.tenantservice.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.paas.sms.tenantservice.document.User;
import com.paas.sms.tenantservice.model.OTP;
import com.paas.sms.tenantservice.model.SigninDTO;

public interface UserService {

	public User save(User user);

	public User search(String username);

	public SigninDTO signin(String username, String password);

	public User deleteUser(User user);

	public String signup(User admin) throws Exception;

	public Object whoami(HttpServletRequest req);

	public String refresh(String Username);

	public User findByEmail(String emailId);

	public User findByUsername(String userName);

	public List<User> findAllUsers();

	public User findById(String userId);

	public void emailUpdate(User user) throws Exception;

	public User savePassword(User user);

	public String isEnabled(User user);

	public String confirmUserAccount(String confirmationToken);

	public String generateToken(User user);

	public OTP forgotPassword(String userName) throws Exception;

}
