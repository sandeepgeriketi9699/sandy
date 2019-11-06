package com.paas.license.controller;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.client.RestTemplate;

import com.paas.license.document.License;
import com.paas.license.dto.ProductDTO;
import com.paas.license.exceptions.CustomException;
import com.paas.license.service.LicenseService;

@RestController
@RequestMapping("/license")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LicenseController {

	@Autowired
	LicenseService licenseService;

	RestTemplate restTemplate;

	String uri = "http://localhost:2170/paascart/cart/";

	@PostMapping("/{emailId}/{role}")
	public License activateSubscribedLicense(@RequestBody Set<ProductDTO> prds, @PathVariable String emailId,
			@PathVariable String role, HttpServletRequest request)// @PathVariable String paymentMode,@PathVariable
																	// String transactionNumber,
			throws Exception {

		Set<ProductDTO> products = new HashSet<>();
		List<Long> prdIds = new ArrayList<>();

		for (ProductDTO prd : prds) {
			prd.setStartDate(setStartDate());
			prd.setPaymentMode("Online");
			String transactionNumber = new DecimalFormat("000000").format(new Random().nextInt(999999));
			prd.setTransactionNumber(transactionNumber);
			products.add(prd);
			prdIds.add(prd.getProductId());
		}

		License license = licenseService.findByEmail(emailId);
		if (license != null) {
			Set<ProductDTO> dbProducts = license.getUniqueProducts();
			if (dbProducts != null) {
				for (ProductDTO dbprd : dbProducts) {
					if (prdIds.contains(dbprd.getProductId()) && !dbprd.isTrial()) {
						throw new Exception("Product : " + dbprd.getProductName() + " already subscribed");
						
					}
				}
				products.addAll(dbProducts);
			}
			license = setLicenseProducts(license, products, emailId);
		} else {
			license = new License();

			license = setLicenseProducts(license, products, emailId);
		}
		if (license != null) {
			deleteProductFromCart(request, prdIds, emailId, license, role);
		}
		return license;
	}

	private License setLicenseProducts(License license, Set<ProductDTO> products, String emailId) {

		license.setUniqueProducts(products);
		license.setEmail(emailId);
		license = licenseService.activateLicense(license);
		return license;
	}

	@PostMapping("/addProduct/{emailId}/{role}/{paymentMode}/{transactionNumber}")
	public License addProduct(@RequestBody Set<ProductDTO> prds, @PathVariable String emailId,
			@PathVariable String role, HttpServletRequest request, @PathVariable String paymentMode,
			@PathVariable String transactionNumber) throws Exception {
		/*
		 * String regex="[0-9]"; if(!transactionNumber.matches(regex)) { throw new
		 * Exception("Invalid transaction number"); }
		 */
		Set<ProductDTO> products = new HashSet<>();
		List<Long> prdIds = new ArrayList<>();

		for (ProductDTO prd : prds) {
			prd.setStartDate(setStartDate());
			prd.setPaymentMode(paymentMode);

			prd.setTransactionNumber(transactionNumber);

			products.add(prd);
			prdIds.add(prd.getProductId());
		}

		License license = licenseService.findByEmail(emailId);
		if (license != null) {
			Set<ProductDTO> dbProducts = license.getUniqueProducts();
			if (dbProducts != null) {
				for (ProductDTO dbprd : dbProducts) {
					if (prdIds.contains(dbprd.getProductId()) && !dbprd.isTrial()) {
						throw new Exception("Product : " + dbprd.getProductName() + " already subscribed");
					}
				}
				products.addAll(dbProducts);
			}
			license = setLicenseProducts(license, products, emailId);
		} else {
			license = new License();

			license = setLicenseProducts(license, products, emailId);
		}
		if (license != null) {
			deleteProductFromCart(request, prdIds, emailId, license, role);
		}
		return license;
	}

	@GetMapping("/{emailId}")
	public License getLicenseBasedOnEmail(@PathVariable String emailId, HttpServletResponse res) {
		License license = licenseService.findByEmail(emailId);
		if (license != null) {
			Set<ProductDTO> prds = license.getUniqueProducts();
			prds = (prds != null) ? prds : Collections.emptySet();
			Set<ProductDTO> subscribedProducts = prds.stream().filter(prd -> !prd.isTrial())
					.collect(Collectors.toSet());
			license.setUniqueProducts(subscribedProducts);
			license.setEmail(emailId);
			return license;
		}
		// throw new Exception("No license activated for this user :" + emailId +
		// HttpStatus.EXPECTATION_FAILED);
		// throw new CustomException("No license activated for this user :" + emailId,
		res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		// res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED, "No license
		// activated for this user :" + emailId);
		return null;
	}

	@GetMapping("/trial/{emailId}")
	public License getTrialLicenseBasedOnEmail(@PathVariable String emailId) {
		License license = licenseService.findByEmail(emailId);
		Set<ProductDTO> prds = (license != null) ? license.getUniqueProducts() : Collections.emptySet();
		Set<ProductDTO> trialProducts = prds.stream().filter(prd -> prd.isTrial()).collect(Collectors.toSet());
		license.setUniqueProducts(trialProducts);
		license.setEmail(emailId);
		return license;
	}

	@PutMapping("/update/{newMail}/{oldMail}")
	public void update(@PathVariable String newMail, @PathVariable String oldMail) {
		System.out.println("*********Entered into update method in licence**********");
		License license = licenseService.findByEmail(oldMail);
		if (license != null) {

			license.setEmail(newMail);
			license.setUniqueProducts(license.getUniqueProducts());
			licenseService.activateLicense(license);
			License oldLicense = licenseService.findByEmail(oldMail);
			if (oldLicense != null) {
				licenseService.deleteLicenseByEmail(oldLicense);
			}
		}
		System.out.println("*********exit update method in licence**********");
	}

	@DeleteMapping("/deleteProduct/{productId}")
	public String deleteProductUsingProductId(@PathVariable long productId) throws Exception {
		List<License> licenseList = licenseService.getAllActiveLicenses();
		if (licenseList != null) {
			for (License license : licenseList) {
				String emailId = license.getEmail();
				Set<ProductDTO> prds = license.getUniqueProducts();
				if (!CollectionUtils.isEmpty(prds)) {
					Set<ProductDTO> updatedProducts = prds.stream().filter(prd -> productId != prd.getProductId())
							.collect(Collectors.toSet());
					setLicenseProducts(license, updatedProducts, emailId);
				}
			}
		}
		return "Product Deleted From Cart";
	}

	@GetMapping("/findAllProducts/{emailId}")
	public Set<ProductDTO> getAllLicensedProducts(@PathVariable String emailId) {
		License license = licenseService.findByEmail(emailId);
		return license.getUniqueProducts();
	}

	@PostMapping("/exchangeProduct/{productId}/{emailId}")
	public String exchangeProduct(@PathVariable long productId, @PathVariable String emailId,
			@RequestBody ProductDTO destProd) throws Exception {
		License license = licenseService.findByEmail(emailId);
		Set<ProductDTO> list = license.getUniqueProducts();
		List<Long> prdIds = list.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
		if (prdIds.contains(destProd.getProductId())) {
			throw new Exception(destProd.getProductName() + " already subscribed");
		}
		for (ProductDTO prod : list) {
			if (prod.getProductId() == productId) {
				if (destProd.getProductPrice() >= prod.getProductPrice()) {
					Set<ProductDTO> updatedPrds = list.stream().filter(prd -> prd.getProductId() != productId)
							.collect(Collectors.toSet());
					System.out.println("start date :: " + setStartDate());
					//destProd.setPaymentMode("Exchange");
					//String transactionnumber = new DecimalFormat("000000").format(new Random().nextInt(999999));
					//destProd.setTransactionNumber(transactionnumber);
					destProd.setStartDate(setStartDate());
					System.out.println("Product :: " + destProd);
					updatedPrds.add(destProd);
					license.setUniqueProducts(updatedPrds);
					licenseService.activateLicense(license);
					return "Product exchanged for user " + license.getEmail();
				} else {
					throw new Exception(
							destProd.getProductName() + " price should not be less than " + prod.getProductName());
				}
			}
		}
		return null;
	}

	@GetMapping("/findAllProductIds/{emailId}")
	public List<Long> getAllLicensedProductIds(@PathVariable String emailId) {
		License license = licenseService.findByEmail(emailId);

		Set<ProductDTO> prds = (license != null) ? license.getUniqueProducts() : null;
		if (prds != null) {
			return prds.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private String setStartDate() {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		final LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	@PostMapping("/trial/{emailId}")
	public License activateTrialLicense(@RequestBody Set<ProductDTO> prds, @PathVariable String emailId,
			HttpServletResponse res) throws Exception {
		Set<ProductDTO> products = new HashSet<>();
		List<Long> prdIds = new ArrayList<>();
		for (ProductDTO prd : prds) {
			prd.setStartDate(setStartDate());
			products.add(prd);
			prdIds.add(prd.getProductId());
		}
		Set<ProductDTO> trialProducts = licenseService.trailfindByEmail(emailId);
		if (trialProducts == null || trialProducts.size() <= 0) {
			res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		}
		License license = licenseService.findByEmail(emailId);
		Set<ProductDTO> subscribedProducts = licenseService.subscribedfindByEmail(emailId);
		if (license != null) {
			if (trialProducts != null) {
				for (ProductDTO dbprd : trialProducts) {
					if (prdIds.contains(dbprd.getProductId())) {
						throw new Exception("Product : " + dbprd.getProductName() + " already in trial period");
					}
				}
				products.addAll(trialProducts);
				products.addAll(subscribedProducts);

				license = setLicenseProducts(license, products, emailId);

			}

		} else {
			license = new License();

			license = setLicenseProducts(license, products, emailId);
		}
		return license;
	}

	@GetMapping("/unSubscribedProducts/{emailId}")
	public License getUnsubscribedProducts(@PathVariable String emailId, HttpServletResponse res) {
		Set<ProductDTO> trialProducts = licenseService.trailfindByEmail(emailId);

		if (trialProducts == null || trialProducts.size() <= 0) {
			// throw new CustomException("No license activated for this user :" + emailId,
			// HttpStatus.EXPECTATION_FAILED);
			res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		}

		Set<ProductDTO> subscribedProducts = licenseService.subscribedfindByEmail(emailId);
		Set<Long> sprdIds = new HashSet<>();
		if (subscribedProducts != null) {
			for (ProductDTO prd : subscribedProducts) {
				sprdIds.add(prd.getProductId());
			}
		}
		Set<ProductDTO> uprds = trialProducts != null ? trialProducts.stream()
				.filter(prd -> !(sprdIds.contains(prd.getProductId()))).collect(Collectors.toSet()) : null;
		License license = new License();
		license.setEmail(emailId);
		license.setUniqueProducts(uprds);
		return license;
	}

	@DeleteMapping("/deactivate/{emailId}")
	public String deleteLicenseBasedOnEmail(@PathVariable String emailId) {
		License license = licenseService.findByEmail(emailId);
		if (license != null) {
			licenseService.deleteLicenseByEmail(license);
			return "License Deleted for :" + emailId;
		}
		return "License not deleted";

	}

	@PutMapping("/extendLicense/{emailId}/{productId}/{endDate}")
	public void extendLicense(@PathVariable String emailId, @PathVariable long productId,
			@PathVariable String endDate) {

		License license = licenseService.findByEmail(emailId);

		Set<ProductDTO> dbPrds = license.getUniqueProducts();
		System.out.println("Sudha...." + endDate);

		Instant instant = Instant.parse(endDate);
		LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));

		/*
		 * final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/MMM/yyyy");
		 * 
		 * LocalDate localDate = LocalDate.parse(endDate, dtf);
		 */

		System.out.println("date...: " + result.toLocalDate());
		for (ProductDTO prd : dbPrds) {
			if (productId == prd.getProductId()) {
				prd.setEndDate(result.toLocalDate().toString());
				// System.out.println("local date : " + localDate);
			}
		}
		license.setUniqueProducts(dbPrds);

		licenseService.activateLicense(license);

	}

	private void deleteProductFromCart(HttpServletRequest request, List<Long> prdIds, String emailId, License license,
			String role) {
		if (license != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpSession session = request.getSession();
			String token = (String) session.getAttribute("token");

			headers.set("Authorization", token);
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			restTemplate = new RestTemplate();

			Set<ProductDTO> lprds = license.getUniqueProducts();

			if (lprds != null) {
				for (ProductDTO prd : lprds) {
					long prdId = prd.getProductId();
					if (prdIds.contains(prdId) && !prd.isTrial() && !role.equalsIgnoreCase("ROLE_ADMIN")) {
						restTemplate.exchange(uri + "product/" + emailId + "/" + prdId, HttpMethod.DELETE, entity,
								String.class);
					}

				}
			}
		}
	}
}