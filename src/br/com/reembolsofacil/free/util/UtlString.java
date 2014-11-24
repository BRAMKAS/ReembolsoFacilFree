package br.com.reembolsofacil.free.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class UtlString {

	static public String getNumber(double value, Locale currentLocale) {

		NumberFormat numberFormatter;
		String amountOut;

		numberFormatter = NumberFormat.getNumberInstance(currentLocale);
		amountOut = numberFormatter.format(value);
		return amountOut;
	}
	
	static public String getNumber(BigDecimal value, Locale currentLocale) {

		NumberFormat numberFormatter;
		String amountOut;

		numberFormatter = NumberFormat.getNumberInstance(currentLocale);
		
		amountOut = numberFormatter.format(value);
		return amountOut;
	}

	static public String getCurrency(double value, Locale currentLocale) {

		NumberFormat currencyFormatter;
		String currencyOut;

		currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
		currencyOut = currencyFormatter.format(value);
		return currencyOut;
	}
	
	static public String getCurrency(BigDecimal value, Locale currentLocale) {

		NumberFormat currencyFormatter;
		String currencyOut;

		currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
		currencyOut = currencyFormatter.format(value);
		return currencyOut;
	}

	static public String getPercent(double value, Locale currentLocale) {

		NumberFormat percentFormatter;
		String percentOut;

		percentFormatter = NumberFormat.getPercentInstance(currentLocale);
		percentOut = percentFormatter.format(value);
		return percentOut;
	}
	
}
