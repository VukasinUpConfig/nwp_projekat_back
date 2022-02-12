package com.example.RafCloud.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static Date parseStringAsDate(String date, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(date);
	}
}
