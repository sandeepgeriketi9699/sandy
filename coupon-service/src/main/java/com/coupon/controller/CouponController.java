package com.coupon.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coupon.document.Coupon;
import com.coupon.document.CouponProductList;
import com.coupon.document.CouponUserList;
import com.coupon.document.Product;
import com.coupon.document.UserDTO;
import com.coupon.service.CouponService;

@RestController
@RequestMapping("/coupon")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CouponController {

	@Autowired
	CouponService couponService;

	@PostMapping("/saveCoupon")
	public Coupon saveCoupon(@RequestBody Coupon c) throws Exception {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		final LocalDateTime now = LocalDateTime.now();
		c.setCreatedDate(dtf.format(now));
		if (c.getAmount() == 0 || c.getAmount() < 0) {
			throw new Exception("Invalid amount");
		}
		return couponService.saveCoupon(c);
	}

	@GetMapping("/getCoupons")
	public List<Coupon> getCoupons() {
		return couponService.getCoupons();
	}

	@GetMapping("/getAllCoupons")
	public List<CouponProductList> getAllCouponforProducts() {
		return couponService.getAllCouponforProducts();
	}

	@PostMapping("/saveCouponforProducts/{couponCode}")
	public List<Product> saveCouponforProducts(@PathVariable String couponCode, @RequestBody List<Product> products)
			throws Exception {

		Optional<CouponProductList> dbCouponProducts = couponService.getCouponforProducts(couponCode);
		if (dbCouponProducts.isPresent()) {
			CouponProductList cprdl = dbCouponProducts.get();
			List<Product> dbPrds = cprdl.getListOfProducts();
			for (Product prods : dbPrds) {
				for (Product prod : products) {

					if (prods.getProductId() == prod.getProductId()) {
						throw new Exception("Coupon code already exist for this product  :" + prod.getProductName());
					}
				}
			}
			dbPrds.addAll(products);
			cprdl.setListOfProducts(dbPrds);
			return couponService.saveCouponforProducts(cprdl).getListOfProducts();
		} else {

			CouponProductList cpl = new CouponProductList();
			cpl.setCouponCode(couponCode);
			cpl.setListOfProducts(products);
			return couponService.saveCouponforProducts(cpl).getListOfProducts();

		}
	}

	@GetMapping("/getCouponforProducts/{couponCode}")
	public List<Product> getCouponforProducts(@PathVariable String couponCode) {

		Optional<CouponProductList> optional = couponService.getCouponforProducts(couponCode);
		if (optional.isPresent()) {
			return optional.get().getListOfProducts();
		}

		return Collections.emptyList();

	}

	@PostMapping("/saveUserCoupon/{couponCode}")
	public CouponUserList saveCouponUsers(@PathVariable String couponCode, @RequestBody List<UserDTO> users)
			throws Exception {
		Optional<CouponUserList> optional = couponService.getUserCouponList(couponCode);
		if (optional.isPresent()) {
			CouponUserList couponUserList = optional.get();
			List<UserDTO> dbUsers = couponUserList.getUsers();
			if (!CollectionUtils.isEmpty(dbUsers)) {
				for (UserDTO dbusers : dbUsers) {
					for (UserDTO userlist : users) {
						if (dbusers.getUsername().equalsIgnoreCase(userlist.getUsername())) {
							// .getEmail().equalsIgnoreCase(userlist.getEmail())) {
							throw new Exception("This coupon already exist for user : " + userlist.getUsername());
						}

					}

				}

				users.addAll(dbUsers);
				couponService.sendMail(dbUsers, couponCode);
			}
			couponUserList.setUsers(users);
			return couponService.saveUserCoupon(couponUserList);

		} else {

			CouponUserList couponUserList = new CouponUserList();
			couponUserList.setCouponCode(couponCode);
			couponUserList.setUsers(users);
			couponService.sendMail(users, couponCode);
			return couponService.saveUserCoupon(couponUserList);
		}
	}

	@GetMapping("/getUserCouponList")
	public List<CouponUserList> getUserCouponList() {
		List<CouponUserList> couponUserList = couponService.getUserCouponList();
		if (CollectionUtils.isEmpty(couponUserList)) {
			return Collections.emptyList();
		}
		return couponUserList;
	}

	@DeleteMapping("/deleteUserCoupon/{email}")
	public String deleteUserCoupon(@PathVariable String email) {
		List<CouponUserList> couponUserList = couponService.getUserCouponList();
		if (couponUserList != null) {
			for (CouponUserList cul : couponUserList) {
				List<UserDTO> users = cul.getUsers();
				for (UserDTO user : users) {
					if (user.getEmail().equalsIgnoreCase(email)) {
						users.remove(user);
						cul.setUsers(users);
						couponService.saveUserCoupon(cul);
						if (users.size() < 1) {
							couponService.deleteByUserCoupon(cul);
						}
						return "User removed from coupon list:" + email;
					}
				}

			}
		}

		return null;

	}

	@GetMapping("/getUserCouponList/{couponCode}")
	public List<UserDTO> getUserCouponList(@PathVariable String couponCode) {
		Optional<CouponUserList> couponUserList = couponService.getUserCouponList(couponCode);
		if (couponUserList.isPresent()) {
			CouponUserList cul = couponUserList.get();
			return cul.getUsers();
		}
		return Collections.emptyList();
	}

	@GetMapping("/getCouponForUser/{userName}")
	public List<String> getCouponForUser(@PathVariable String userName) {
		return couponService.getCouponForUser(userName);

	}
	
	@GetMapping("/getUserCoupon/{couponCode}")
	public Optional<CouponUserList> getUserCoupon(@PathVariable String couponCode) {
		
		return couponService.getCouponforUsers(couponCode);
	}

	@PutMapping("/update")
	public void update(@RequestBody Product product) {
		List<CouponProductList> cpls = couponService.getAllCouponforProducts();

		/*
		 * List<Product> updatedPrds= cpls.stream().map(cpl ->
		 * cpl.getListOfProducts()).flatMap(prds -> prds.stream()).filter(prd ->
		 * prd.getProductId()!=product.getProductId()).collect(Collectors.toList());
		 * updatedPrds.add(product);
		 */

		for (CouponProductList cp : cpls) {
			List<Product> prods = cp.getListOfProducts();
			List<Product> prds = new ArrayList<>();
			for (Product prod : prods) {
				if (prod.getProductId() != product.getProductId()) {

					prds.add(prod);

				}
				if (prod.getProductId() == product.getProductId()) {
					prds.add(product);
				}
				cp.setListOfProducts(prds);
				couponService.saveCouponforProducts(cp);
			}

		}
	}

	@GetMapping("/getUserEmailList/{couponCode}")
	public List<String> getUserEmailList(@PathVariable String couponCode) {
		System.out.println("************ Entered Into getUserEmailList **************");
		Optional<CouponUserList> optional = couponService.getUserCouponList(couponCode);
		if (optional.isPresent()) {
			System.out.println("********* Entered into if block *********");
			CouponUserList couponUserList = optional.get();
			return couponUserList.getUsers().stream().map(UserDTO::getEmail).collect(Collectors.toList());
		}
		System.out.println("********* Before Returning Empty List *************");
		return Collections.emptyList();
	}

	@GetMapping("/getProductIdsforCoupon/{couponCode}")
	public List<Long> getCouponforProductIds(@PathVariable String couponCode) {

		Optional<CouponProductList> optional = couponService.getCouponforProducts(couponCode);
		if (optional.isPresent()) {

			CouponProductList couponProductList = optional.get();
			List<Product> prds = couponProductList.getListOfProducts();
			return (prds != null) ? prds.stream().map(Product::getProductId).collect(Collectors.toList())
					: Collections.emptyList();

		}

		return Collections.emptyList();

	}

	@GetMapping("/getCoupon/{couponCode}")
	public Coupon getCouponBasedOnCode(@PathVariable String couponCode) {
		return couponService.getCoupon(couponCode);
	}

	@DeleteMapping("/deleteAllCoupons")
	public void deleteAllCoupons() {
		couponService.deleteAllCoupons();
	}

	@DeleteMapping("/deleteCoupon/{couponCode}")
	public String deleteCoupon(@PathVariable String couponCode) throws Exception {
		Coupon coupon = couponService.getCoupon(couponCode);
		CouponProductList cpl = couponService.getCouponByName(couponCode);
		CouponUserList cul = couponService.getUserCouponByName(couponCode);
		if (cpl != null) {
			couponService.deleteByCoupon(cpl);
		}
		if (cul != null) {
			couponService.deleteByUserCoupon(cul);
		}

		couponService.deleteCoupon(coupon);

		return "Coupon deleted with code :" + couponCode;
	}

	@DeleteMapping("/delete/{productId}")
	public String deleteProduct(@PathVariable long productId) {
		List<CouponProductList> cpl = couponService.getAllCouponforProducts();
		for (CouponProductList cp : cpl) {
			List<Product> prods = cp.getListOfProducts();
			for (Product prod : prods) {
				if (prod.getProductId() == productId) {
					prods.remove(prod);
					cp.setListOfProducts(prods);
					couponService.saveCouponforProducts(cp);
					return "Product deleted";

				}
			}

		}

		return null;

	}

	@PutMapping("/updateCoupon")
	public Coupon editCoupon(@RequestBody Coupon coupon) {
		return couponService.saveCoupon(coupon);
	}
}
