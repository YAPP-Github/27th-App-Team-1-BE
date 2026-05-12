package com.yapp.ndgl.common.util;

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
}
