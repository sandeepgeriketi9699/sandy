package com.coupon.service;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.coupon.document.Coupon;
import com.coupon.document.CouponProductList;
import com.coupon.document.CouponUserList;
import com.coupon.document.UserDTO;
import com.coupon.repository.CouponProductListRepository;
import com.coupon.repository.CouponRepository;
import com.coupon.repository.CouponUserListRepository;

@Service
public class CouponService {

	@Autowired
	CouponRepository couponRepository;
	@Autowired
	CouponProductListRepository couponProductListRepository;

	@Autowired
	CouponUserListRepository couponUserListRepository;

	@Value("${email}")
	String Email_Id;

	@Value("${password}")
	String password;

	public Coupon saveCoupon(Coupon c) {
		return couponRepository.save(c);
	}

	public List<Coupon> getCoupons() {
		return couponRepository.findAll();
	}

	public List<Coupon> listCoupon() {
		return couponRepository.findAll();
	}

	public void deleteAllCoupons() {
		couponRepository.deleteAll();
	}

	public CouponProductList saveCouponforProducts(CouponProductList couponProductList) {

		return couponProductListRepository.save(couponProductList);

	}

	public Coupon getCoupon(String couponCode) {
		// TODO Auto-generated method stub
		Coupon coupon = couponRepository.findById(couponCode).get();
		if (coupon != null) {
			return coupon;
		}
		return null;
	}

	public CouponProductList getCouponForProducts(String couponCode) {
		// TODO Auto-generated method stub
		CouponProductList coupon = couponProductListRepository.findById(couponCode).get();
		if (coupon != null) {
			return coupon;
		}
		return null;
	}

	public CouponProductList getCouponByName(String couponCode) throws Exception {
		// TODO Auto-generated method stub
		Optional<CouponProductList> dbCouponProducts = getCouponforProducts(couponCode);
		if (dbCouponProducts.isPresent()) {
			CouponProductList couponPLR = couponProductListRepository.findById(couponCode).get();
			if (couponPLR != null) {
				return couponPLR;
			}
		}
		return null;
	}

	public CouponUserList getUserCouponByName(String couponCode) throws Exception {

		Optional<CouponUserList> dbCouponProducts = getCouponforUsers(couponCode);
		if (dbCouponProducts.isPresent()) {
			CouponUserList couponPLR = couponUserListRepository.findById(couponCode).get();
			if (couponPLR != null) {
				return couponPLR;
			}
		}
		return null;
	}

	public List<String> getCouponForUser(String userName) {
		List<CouponUserList> couponUserList = couponUserListRepository.findAll();
		return couponUserList.stream().map(cul -> cul.getUsers()).flatMap(usr -> usr.stream())
				.filter(user -> userName.equals(user.getUsername())).map(UserDTO::getCouponCode)
				.collect(Collectors.toList());
	}

	public Optional<CouponProductList> getCouponforProducts(String couponCode) {
		// TODO Auto-generated method stub
		return couponProductListRepository.findById(couponCode);
	}

	public Optional<CouponUserList> getCouponforUsers(String couponCode) {
		// TODO Auto-generated method stub
		return couponUserListRepository.findById(couponCode);
	}

	public void deleteCoupon(Coupon coupon) {

		couponRepository.delete(coupon);

	}

	public void deleteByCoupon(CouponProductList coupon) {
		couponProductListRepository.delete(coupon);
	}

	public void deleteByUserCoupon(CouponUserList coupon) {
		couponUserListRepository.delete(coupon);
	}

	public List<CouponProductList> getAllCouponforProducts() {
		// TODO Auto-generated method stub
		return couponProductListRepository.findAll();
	}

	public CouponUserList saveUserCoupon(CouponUserList couponUserList) {

		return couponUserListRepository.save(couponUserList);
	}

	public Optional<CouponUserList> getUserCouponList(String couponCode) {
		return couponUserListRepository.findById(couponCode);
	}

	public List<CouponUserList> getUserCouponList() {
		return couponUserListRepository.findAll();
	}

	public void sendMail(List<UserDTO> users, String couponCode) throws MessagingException {
		try {

			// String Email_Id = "paas.team2019@gmail.com";
			// "spsoft.krishnareddy@gmail.com";
			// String password = "vytqiietngjpytfw";
			// "jrmmoqeqjiyumdgn";
			// "spsoft@123";
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
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			try {
				// Set email data
				String[] to = new String[users.size()];
				message.setFrom(new InternetAddress(Email_Id, "PaaS"));
				for (int i = 0; i < users.size(); i++) {
					UserDTO user = users.get(i);
					to[i] = user.getEmail();
				}

				InternetAddress[] toAddress = new InternetAddress[to.length];

				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(Email_Id));
				for (int i = 0; i < to.length; i++) {
					toAddress[i] = new InternetAddress(to[i]);
				}
				for (int i = 0; i < toAddress.length; i++) {
					message.addRecipient(Message.RecipientType.BCC, toAddress[i]);
				}
				message.setSubject("Congratulations..!! You Got A Coupon For PaaS Products");
				message.setText("Dear PaaS Member, " + "\n" + "\n" + "Your Coupon Code : " + couponCode + "\n"
						+ "To avail this coupon, Please login to http://18.215.4.80" + "\n" + "\n" + "Thank You," + "\n"
						+ "Team PaaS");

				Transport transport = session.getTransport("smtp");
				transport.connect("smtp.gmail.com", 587, Email_Id, password);
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();

				// Transport.send(message);

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

}
