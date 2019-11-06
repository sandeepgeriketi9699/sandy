
package com.paas.sms.product.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@RestController
@RequestMapping("/paasimage")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImageUploadController {

	@Value("${spring.data.mongodb.port}")
	private Integer port;

	@Value("${spring.data.mongodb.database}")
	private String database;

	GridFS gfsPhoto;

	DB db;

	Mongo mongo;

	@SuppressWarnings("deprecation")
	@GetMapping("/imageUpload/{imageName}")
	public String imageUpload(@PathVariable String imageName) throws IOException {

		mongo = new Mongo("localhost", port);

		System.out.println("mdbport::" + port);

		db = mongo.getDB(database);
		DBCollection collection = db.getCollection("Images");

		String newFileName = imageName;
		// File imageFile = new
		// File("src/main/resources/static/images/import/"+imageName+".png");
		File imageFile = new File("/home/ec2-user/paas-jars/product/static/images/import/" + imageName + ".png");
		// create a "photo" namespace
		gfsPhoto = new GridFS(db, "photo");

		// get image file from local drive
		GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);

		// set a new filename for identify purpose
		gfsFile.setFilename(newFileName);

		// save the image file into mongoDB
		gfsFile.save();

		return "ImageUploadedSucessfully";

	}

	@SuppressWarnings("deprecation")
	@GetMapping("/getDBImage/{imageName}")
	public byte[] getImage(@PathVariable String imageName) throws Exception {

		mongo = new Mongo("localhost", port);

		System.out.println("mdbport::" + port);

		db = mongo.getDB(database);

		gfsPhoto = new GridFS(db, imageName);
		DBCursor cursor = gfsPhoto.getFileList();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

		// get image file by it's filename
		GridFSDBFile imageForOutput = gfsPhoto.findOne(imageName);
		imageForOutput.writeTo(
				"E:\\new paas backup\\paas backup 22-07-19\\Product_microservice\\src\\main\\resources\\static\\images\\"
						+ imageName + ".png");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		imageForOutput.writeTo(outputStream);
		byte[] bs = outputStream.toByteArray();
		//outputStream.close();
		return bs;
	}

	@SuppressWarnings("deprecation")
	@PostMapping("/uploadImage/{imageName}")
	public String uploadImage(@PathVariable String imageName, @RequestParam("image") MultipartFile image)
			throws IOException {

		mongo = new Mongo("localhost", port);

		System.out.println("mdbport::" + port);

		db = mongo.getDB(database);
		DBCollection collection = db.getCollection("Images");

		String newFileName = imageName;
		gfsPhoto = new GridFS(db, imageName);

		// get image file from local drive
		GridFSInputFile gfsFile = gfsPhoto.createFile(image.getBytes());

		// set a new filename for identify purpose
		gfsFile.setFilename(newFileName);

		// save the image file into mongoDB
		gfsFile.save();

		return "ImageUploadedSucessfully";

	}

}
