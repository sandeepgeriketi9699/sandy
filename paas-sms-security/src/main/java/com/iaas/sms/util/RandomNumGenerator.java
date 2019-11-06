package com.iaas.sms.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomNumGenerator {

	public static String setUniqueID(String nameInput) {
		StringBuilder builder = new StringBuilder();
		DateFormat dateFormat = new SimpleDateFormat("yydd");
		Date date = new Date();
		SimpleDateFormat time = new SimpleDateFormat("HHmm");
		builder.append(nameInput.substring(0, Math.min(nameInput.length(), 3)));
		builder.append(String.valueOf(dateFormat.format(date)));
		builder.append(String.valueOf(time.format(new Date())));

		return builder.toString();

	}

	
}
