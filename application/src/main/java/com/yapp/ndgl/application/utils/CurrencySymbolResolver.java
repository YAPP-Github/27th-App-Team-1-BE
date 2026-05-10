package com.yapp.ndgl.application.utils;

import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencySymbolResolver {

	private static final Map<String, String> SYMBOL_CACHE = new ConcurrentHashMap<>();

	private CurrencySymbolResolver() {
	}

	public static String resolve(final String currencyCode) {
		if (currencyCode == null) {
			return null;
		}

		return SYMBOL_CACHE.computeIfAbsent(currencyCode, code -> {
			try {
				Currency currency = Currency.getInstance(code);
				for (Locale locale : Locale.getAvailableLocales()) {
					try {
						if (currency.equals(Currency.getInstance(locale))) {
							String symbol = currency.getSymbol(locale);
							if (!symbol.equals(code)) {
								return symbol;
							}
						}
					} catch (Exception ignored) {
					}
				}
				return currency.getSymbol();
			} catch (Exception e) {
				return currencyCode;
			}
		});
	}
}
