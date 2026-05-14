package com.yapp.ndgl.common.util;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TextUtils {

	/**
	 * 입력 텍스트가 한도를 초과하면 끝을 ellipsis(…)로 잘라낸다.
	 *
	 * @param text 원본 텍스트 (nullable)
	 * @param max  허용 최대 길이
	 * @return 한도를 만족하는 텍스트, 입력이 null이면 null
	 */
	public static String truncate(final String text, final int max) {
		if (text == null) {
			return null;
		}
		if (text.length() <= max) {
			return text;
		}
		return text.substring(0, max - 1) + "…";
	}

	/**
	 * 공백/빈 문자열이면 fallback을 반환한다.
	 *
	 * @param value    원본 텍스트 (nullable)
	 * @param fallback 비어 있을 때 반환할 값
	 */
	public static String defaultIfBlank(final String value, final String fallback) {
		return (value != null && !value.isBlank()) ? value : fallback;
	}

	/**
	 * 컬렉션을 구분자로 join한다. null이거나 비어 있으면 fallback을 반환한다.
	 *
	 * @param items     join 대상 (nullable)
	 * @param delimiter 구분자
	 * @param fallback  비어 있을 때 반환할 값
	 */
	public static String joinOrDefault(
		final Collection<? extends CharSequence> items,
		final String delimiter,
		final String fallback
	) {
		if (items == null || items.isEmpty()) {
			return fallback;
		}
		return String.join(delimiter, items);
	}
}
