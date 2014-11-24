package br.com.reembolsofacil.free.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberUtil {
	
	static public Double parseNumber(String number, Locale locale) {
		Number ret;
		Double r1 = 0.00;
		try {
			ret = NumberFormat.getNumberInstance(locale).parse(number);
			if (ret instanceof Long) {
				r1 = new Double((Long)ret);
			} else {
				r1 = (Double) ret;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return r1;
	}
	
	static public Double parseNumber(String number) {
		int point = number.lastIndexOf(".");
		int comma = number.lastIndexOf(",");
		int separator = (point >= comma ? point : comma);
		StringBuilder sb = new StringBuilder("");
		if (separator > -1) {
			sb.append(number.substring(0, separator).replace(".", "").replace(",", ""));
			sb.append(".");
			sb.append(number.substring(separator + 1));
		} else {
			sb.append(number);
		}
		return new Double(sb.toString());
	}
	
	static public String format(Number number) {
		if (number == null) {
			number = 0;
		}
		NumberFormat formatter = new DecimalFormat("#,##0.00");
		String string = formatter.format(number);
		return string;
	}
}
