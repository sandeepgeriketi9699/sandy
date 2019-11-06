package com.paas.sms.product.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

import com.paas.sms.product.dto.CouponDTO;
import com.paas.sms.product.dto.CouponUserListDTO;
import com.paas.sms.product.model.Product;
import com.paas.sms.product.model.Suite;
import com.paas.sms.product.model.UserProductList;
import com.paas.sms.product.repository.ProductRepository;
import com.paas.sms.product.repository.SequenceRepository;
import com.paas.sms.product.repository.SuiteRepository;
import com.paas.sms.product.service.ProductService;

@RestController
@RequestMapping("/paasproduct")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

	/*
	 * long product_id; private static AtomicInteger ID_GENERATOR = new
	 * AtomicInteger(1001);
	 */

	@Autowired
	private SequenceRepository sequenceRepository;

	@Autowired
	private ProductRepository productRepository;

	/*
	 * @Autowired private UserProductRepository userProductRepository;
	 */

	@Autowired
	private SuiteRepository suiteRepository;

	@Autowired
	ProductService productService;

	private static final String HOSTING_SEQ_KEY = "productId";

	RestTemplate restTemplate;

	String cartUri = "http://localhost:2170/paascart/cart/";

	String licenseUri = "http://localhost:2175/paaslicense/license/";

	String couponUri = "http://localhost:2246/coupon/";
	String couponProductUri = "http://localhost:2246/coupon/update";
	String cartProductUri = "http://localhost:2170/paascart/cart/update";

	@PutMapping("/update")
	public Product update(@RequestBody Product p) {
		String pname = p.getProductName();
		String first = pname.substring(0, 1).toUpperCase();
		String last = pname.substring(1).toLowerCase();
		String prod = first + last;
		p.setProductName(prod);
		Product p1 = productRepository.save(p);
		if (p1 != null) {
			return p1;
		} else {
			Product p2 = new Product();
			p2.setMessage("Product Not saved ");
			return p2;
		}
	}

	@PostMapping("/addProduct")
	public Product saveProduct(@RequestBody Product product) throws Exception {

		List<Product> products = productRepository.findAll();
		if (!CollectionUtils.isEmpty(products)) {
			for (Product prd : products) {
				if (prd.getProductName().equalsIgnoreCase(product.getProductName())) {
					throw new Exception("Product name already exist in product list..!!");
				}
			}
		}
		product.setProductId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));
		String pname = product.getProductName();
		String first = pname.substring(0, 1).toUpperCase();
		String last = pname.substring(1).toLowerCase();
		String prod = first + last;
		product.setProductName(prod);
		Product p = productRepository.save(product);
		if (p != null) {
			return p;
		} else {
			Product p1 = new Product();
			p1.setMessage("Product Not saved ");
			return p1;
		}
	}

	@GetMapping("/findAllProducts")
	public List<Product> getProducts() throws Exception {
		List<Product> products = productRepository.findAll();
		if (CollectionUtils.isEmpty(products)) {
			throw new Exception("No products available");
		}
		return products;
	}

	@GetMapping("/newProducts")
	public List<Product> newProducts() {
		List<Product> products = productRepository.findAll();
		if (products != null) {
			products.sort(Comparator.comparing(Product::getProductId).reversed());
			return products;
		} else {
			return null;
		}
	}

	@GetMapping("/getDate")
	private String getDate() {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		final LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	@PostMapping("/saveAllUserProducts/{userEmailId}")
	public UserProductList saveUserProducts(@RequestBody List<Product> products, @PathVariable String userEmailId) {

		UserProductList list = productService.findByEmail(userEmailId);
		List<Product> productList = new ArrayList<Product>();
		if (list != null) {
			List<Product> p = list.getProducts();
			System.out.println("List ::");
			productService.delete(list);
			SimpleDateFormat simpleDateFormat = null;
			if (products != null) {
				for (Product pro : products) {
					String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
					simpleDateFormat = new SimpleDateFormat(pattern, new Locale("da", "DK"));

					String date = simpleDateFormat.format(new Date());
					pro.setStartDate(date);
					productList.add(pro);
				}
			}
			p.addAll(productList);
			list.setProducts(p);
			list.setEmail(userEmailId);
			return productService.save(list);
		} else {
			list = new UserProductList();
			list.setEmail(userEmailId);
			if (products != null) {
				for (Product pro : products) {
					String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("da", "DK"));

					String date = simpleDateFormat.format(new Date());
					pro.setStartDate(date);
					productList.add(pro);
				}
			}
			list.setProducts(productList);
			return productService.save(list);
		}

	}

	@GetMapping("/findAllUserProducts/{userEmailId}")
	public List<Product> getUserProductList(@PathVariable String userEmailId) {
		System.out.println("User Email Id ::" + userEmailId);
		UserProductList list = productService.findByEmail(userEmailId);
		System.out.println("List ::" + list);
		if (list != null) {
			return list.getProducts();
		}
		return Collections.emptyList();
	}

	@GetMapping("/search/{productName}")
	public List<Product> getProductsByName(@PathVariable("productName") String productName) {
		String first = productName.substring(0, 1).toUpperCase();
		String last = productName.substring(1).toLowerCase();
		String prod = first + last;
		List<Product> products = this.productRepository.findByProductNameLike(prod);
		return products;
	}

	@GetMapping("/findById/{id}")
	public Optional<Product> getProduct(@PathVariable long id) {
		return productRepository.findById(id);

	}

	@DeleteMapping("/delete/{id}")
	public String deleteProduct(@PathVariable long id) {
		productRepository.deleteById(id);

		// suiteRepository.findById(id)
		return "Product deleted with id: " + id;
	}

	@DeleteMapping("/delete/{productId}/{suiteId}")
	public String deleteProduct(HttpServletRequest request, @PathVariable long productId, @PathVariable long suiteId) {
		productRepository.deleteById(productId);
		Optional<Suite> suiteInfo = suiteRepository.findById(suiteId);
		Suite s = null;
		if (suiteInfo != null) {
			s = suiteInfo.get();
			List<Product> prds = s.getProducts();
			Product p = null;
			for (int i = 0; i < prds.size(); i++) {
				p = prds.get(i);
				if (p.getProductId() == productId) {
					prds.remove(i);
				}
			}
			s.setProducts(prds);
			suiteRepository.save(s);
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			String token = request.getHeader("Authorization");
			headers.set("Authorization", token);
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			restTemplate = new RestTemplate();
			restTemplate.exchange(couponUri + "delete/" + productId, HttpMethod.DELETE, entity, String.class);
			restTemplate.exchange(cartUri + "delete/" + productId, HttpMethod.DELETE, entity, String.class);
			restTemplate.exchange(licenseUri + "deleteProduct/" + productId, HttpMethod.DELETE, entity, String.class);
		}
		// suiteRepository.findById(id)
		return "Product deleted with id: " + productId;
	}

	@GetMapping("/getSuite/{suiteId}")
	public Suite getSuiteById(@PathVariable long suiteId) {
		Optional<Suite> o = suiteRepository.findById(suiteId);

		System.out.println("Suite ::" + o);

		// suiteRepository.findById(id)
		return o.get();
	}

	@PutMapping("/applyCoupon/{couponCode}")
	public Product applyCoupon(@RequestBody Product product, @PathVariable String couponCode,
			HttpServletRequest request) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		int prdId = (int) product.getProductId();
		// HttpEntity<String> entity = getHttpEntity();

		RestTemplate template = new RestTemplate();

		CouponDTO coupon = template.exchange("http://localhost:2246/coupon/getCoupon/" + couponCode, HttpMethod.GET,
				entity, CouponDTO.class).getBody();
		/*
		 * CouponUserListDTO
		 * culd=template.exchange("http://localhost:2246/coupon/getUserCoupon/" +
		 * couponCode, HttpMethod.GET, entity, CouponUserListDTO.class).getBody();
		 * if(culd !=null) { throw new
		 * Exception("User coupon cannot be applied to product"); }
		 */
		if (product.getProductPrice() < coupon.getAmount()) {
			throw new Exception("Coupon price is high compare to product price try with another coupon");
		}
		List<Long> prdIds = template.exchange("http://localhost:2246/coupon/getProductIdsforCoupon/" + couponCode,
				HttpMethod.GET, entity, List.class).getBody();
		Double deductAmount = coupon.getAmount();

		if (prdIds.contains(prdId)) {
			product.setProductPrice(product.getProductPrice() - deductAmount);
			return product;
		}

		throw new Exception("Coupon code is not applicable for this product");

	}

	/*
	 * private HttpEntity<String> getHttpEntity() { HttpHeaders headers = new
	 * HttpHeaders(); headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	 * HttpSession session= request.getSession(); String token
	 * =(String)session.getAttribute("token"); headers.set("Authorization", token);
	 * HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
	 * 
	 * return new HttpEntity<>("parameters", headers); }
	 */

	@GetMapping("/findAllUnSubscribedProducts/{emailId}")
	public List<Product> getunSubscribedProducts(@PathVariable String emailId, HttpServletRequest request) {
		List<Product> dbProducts = productRepository.findAll();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		restTemplate = new RestTemplate();

		List<Long> licensedProductIds = restTemplate
				.exchange(licenseUri + "findAllProductIds/" + emailId, HttpMethod.GET, entity, List.class).getBody();

		List<Product> updatedProducts = new ArrayList<Product>();
		if (licensedProductIds != null) {
			for (Product prd : dbProducts) {
				int id = (int) prd.getProductId();
				if (!(licensedProductIds.contains(id))) {
					updatedProducts.add(prd);
				}
			}

			return updatedProducts;
		}
		return dbProducts;
	}

	@PutMapping("/updateProduct/{suiteId}")
	public Product updateProductIntoSuite(@RequestBody Product p, @PathVariable long suiteId,
			HttpServletRequest request) {
		String pname = p.getProductName();
		String first = pname.substring(0, 1).toUpperCase();
		String last = pname.substring(1).toLowerCase();
		String prod = first + last;
		p.setProductName(prod);
		Product p1 = productRepository.save(p);

		Optional<Suite> o = suiteRepository.findById(suiteId);

		Suite suite = o.get();
		List<Product> products = suite.getProducts();

		if (suite.getProducts() != null) {

			List<Product> result = products.stream().filter(dp -> p.getProductId() != dp.getProductId())
					.collect(Collectors.toList());
			System.out.println("result ::" + result);
			result.add(p);
			suite.setProducts(result);
			/*
			 * for (Product dp : products) { if (p.getProductId() == dp.getProductId()) {
			 * products.remove(dp); products.add(p); }
			 * 
			 * }
			 */
		} else {
			List<Product> al = new ArrayList<Product>();
			p.setProductName(prod);
			al.add(p);
			suite.setProducts(al);

			suite.setProducts(products);
		}
		suiteRepository.save(suite);

		/*
		 * Map<String, Long> params = new HashMap<String, Long>();
		 * params.put("productId", p.getProductId()); restTemplate.put(couponProductUri,
		 * p, params);
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String token = request.getHeader("Authorization");
		headers.set("Authorization", token);
		HttpEntity<Product> entity = new HttpEntity<>(p, headers);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.put(couponProductUri, p);

		restTemplate.exchange(cartProductUri, HttpMethod.PUT, entity, String.class);

		if (p1 != null) {
			return p1;
		} else {
			Product p2 = new Product();
			p2.setMessage("Product Not saved ");
			return p2;
		}
	}
}
