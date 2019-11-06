package com.paas.sms.product.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.paas.sms.product.exceptions.CustomException;
import com.paas.sms.product.model.Product;
import com.paas.sms.product.model.Suite;
import com.paas.sms.product.repository.ProductRepository;
import com.paas.sms.product.repository.SequenceRepository;
import com.paas.sms.product.repository.SuiteRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/paassuite")
public class SuiteController {

	/* private static AtomicInteger ID_GENERATOR = new AtomicInteger(100); */

	@Autowired
	private SuiteRepository suiteRepository;

	private static final String HOSTING_SEQ_KEY = "hosting";
	private static final String PRODUCT_SEQ_KEY = "productId";

	@Autowired
	private SequenceRepository sequenceRepository;

	@Autowired
	private ProductRepository productRepository;

	@PostMapping("/createSuite")
	public String createSuite(@RequestBody Suite suite) throws CustomException {

		Suite s = null;
		List<Suite> suiets = suiteRepository.findAll();
		for (int i = 0; i < suiets.size(); i++) {
			s = suiets.get(i);
			if (s.getSuiteName().equalsIgnoreCase(suite.getSuiteName())) {
				throw new CustomException("Suite name is already exist!", HttpStatus.UNPROCESSABLE_ENTITY);
			}
		}
		suite.setSuiteId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));

		List<Product> products = suite.getProducts();
		if (products != null) {
			for (Product p : products) {
				p.setProductId(sequenceRepository.getNextSequenceId(PRODUCT_SEQ_KEY));
			}
		}

		suiteRepository.save(suite);
		return "Suite created with id: " + suite.getSuiteId();
	}

	@GetMapping("/findAllSuites")
	public List<Suite> getSuites() throws Exception {
		List<Suite> suites = suiteRepository.findAll();
		if (CollectionUtils.isEmpty(suites)) {
			throw new Exception("No suites available");
		} else {
			for (Suite s : suites) {
				List<Product> prds = s.getProducts();
				if (prds == null) {
					s.setProducts(Collections.emptyList());
				}
			}
			return suites;
		}
	}

	@GetMapping("/welcomeSuites")
	public List<Suite> welcomeSuites() {
		List<Suite> suites = suiteRepository.findAll();
		if (null != suites) {
			List<Suite> suites1 = new ArrayList<Suite>();
			for (Suite s : suites) {
				List<Product> products = s.getProducts();
				if (null != products) {
					products.sort(Comparator.comparing(Product::getProductId).reversed());
					int count = 0;
					if (products.size() > 10) {
						List<Product> products1 = new ArrayList<Product>();
						for (Product p : products) {
							if (count != 10) {
								products1.add(p);
								count++;

							}

							if (count == 10) {
								s.setProducts(products1);
								suites1.add(s);
								break;
							}
						}

					} else {
						s.setProducts(products);
						suites1.add(s);
					}
				}

			}

			return suites1;
		} else {
			return null;
		}
	}

	@PostMapping("/addProductIntoSuite/{suiteId}")
	public String addProductIntoSuite(@PathVariable long suiteId, @RequestBody Product product) throws CustomException {
		Product p1 = null;
		Optional<Suite> optional = suiteRepository.findById(suiteId);

		Suite suite = optional.get();

		List<Product> products = new ArrayList<>();
		List<Product> products1 = suite.getProducts();

		if (null != products1) {
			for (int i = 0; i < products1.size(); i++) {
				p1 = products1.get(i);

				if (p1.getProductName().equalsIgnoreCase(product.getProductName())) {
					throw new CustomException("Product name is already in use in this suite",
							HttpStatus.UNPROCESSABLE_ENTITY);
				}

			}
			String pname = product.getProductName();
			String first = pname.substring(0, 1).toUpperCase();
			String last = pname.substring(1).toLowerCase();
			String prod = first + last;
			product.setProductName(prod);
			products1.add(product);
			suite.setProducts(products1);

		}

		else {
			String pname = product.getProductName();
			String first = pname.substring(0, 1).toUpperCase();
			String last = pname.substring(1).toLowerCase();
			String prod = first + last;
			product.setProductName(prod);
			products.add(product);
			suite.setProducts(products);
		}

		Suite response = suiteRepository.save(suite);
		if (response != null) {
			return "Product added into suite succesfully";
		}
		return "Product not saved";
	}

	@GetMapping("/searchSuites/{suiteName}")
	public List<Suite> getProductsByName(@PathVariable("suiteName") String suiteName) {
		List<Suite> suites = this.suiteRepository.findBySuiteNameLike(suiteName);
		return suites;
	}

	@PutMapping("/updateSuite/{suiteId}")
	public String updateSuite(@RequestBody Suite suite, @PathVariable long suiteId) throws CustomException {

		List<Suite> suites = suiteRepository.findAll();
		Optional<Suite> optional = suiteRepository.findById(suiteId);
		Suite s = optional.get();
		List<String> suiteNames = new ArrayList<String>();
		for (Suite s1 : suites) {
			suiteNames.add(s1.getSuiteName());
		}

		if (!(s.getSuiteName().equalsIgnoreCase(suite.getSuiteName()))) {
			if (suiteNames.contains(suite.getSuiteName())) {
				throw new CustomException("Suite name is already exist", HttpStatus.CONFLICT);
			}
		}
		this.suiteRepository.save(suite);
		List <Product> products=s.getProducts();
		for(Product p: products) {
			p.setSuiteName(suite.getSuiteName());
			p.setSuiteId(suite.getSuiteId());
			productRepository.save(p);
		}
		
		return "Suite Updated with id : " + suite.getSuiteId();
	}

	@GetMapping("/getProducts/{suiteId}")
	public Optional<Suite> getProductsBySuiteId(@PathVariable long suiteId) {
		return suiteRepository.findById(suiteId);
	}

	@DeleteMapping("/delete/{suiteid}")
	public String deleteSuite(@PathVariable long suiteid) {
		suiteRepository.deleteById(suiteid);
		return "Deleted suite with Id: " + suiteid;
	}

	@PostMapping("/addProductIntoNewSuite/{newSuiteId}/{oldSuiteId}")
	public String addProductIntoNewSuite(@PathVariable long newSuiteId, @PathVariable long oldSuiteId,
			@RequestBody Product product) throws CustomException {
		String pname = product.getProductName();
		String first = pname.substring(0, 1).toUpperCase();
		String last = pname.substring(1).toLowerCase();
		String prod = first + last;
		product.setProductName(prod);
		Product p1 = null;
		Optional<Suite> optional = suiteRepository.findById(oldSuiteId);
		long productId = product.getProductId();
		if (optional.isPresent()) {
			List<Product> prds = optional.get().getProducts();
			if (!CollectionUtils.isEmpty(prds)) {
				List<Product> result = prds.stream().filter(dp -> productId != dp.getProductId())
						.collect(Collectors.toList());

				Suite suite = optional.get();
				suite.setProducts(result);
				Suite response = suiteRepository.save(suite);
			}
		}
		Optional<Suite> newOptional = suiteRepository.findById(newSuiteId);
		Suite newSuite = newOptional.get();

		List<Product> products = new ArrayList<>();
		List<Product> products1 = newSuite.getProducts();

		if (null != products1) {
			for (int i = 0; i < products1.size(); i++) {
				p1 = products1.get(i);

				if (p1.getProductName().equalsIgnoreCase(product.getProductName())) {
					throw new CustomException("Product name is already in use in this suite",
							HttpStatus.UNPROCESSABLE_ENTITY);
				}

			}
			product.setSuiteName(newSuite.getSuiteName());
			products1.add(product);
			newSuite.setProducts(products1);
			productRepository.save(product);
		}

		else {
			products.add(product);
			newSuite.setProducts(products);
			productRepository.save(product);
		}

		Suite response = suiteRepository.save(newSuite);
		if (response != null) {
			return "Product added into suite succesfully";
		}
		return "Product not saved";
	}
}
