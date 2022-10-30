package tech.deplant.java4ever.binding.generator;

import java.util.Arrays;

public class ParserUtils {

	// Null-safe length
	public static int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	// Copy of StringUtils.capitalize() of Apache Commons
	public static String capitalize(final String str) {
		final int strLen = length(str);
		if (strLen == 0) {
			return str;
		}

		final int firstCodepoint = str.codePointAt(0);
		final int newCodePoint = Character.toTitleCase(firstCodepoint);
		if (firstCodepoint == newCodePoint) {
			// already capitalized
			return str;
		}

		final int[] newCodePoints = new int[strLen]; // cannot be longer than the char array
		int outOffset = 0;
		newCodePoints[outOffset++] = newCodePoint; // copy the first code point
		for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
			final int codePoint = str.codePointAt(inOffset);
			newCodePoints[outOffset++] = codePoint; // copy the remaining ones
			inOffset += Character.charCount(codePoint);
		}
		return new String(newCodePoints, 0, outOffset);
	}

	public static String toParameterCase(final String str) {
		final String[] wordArray = str.split("_");
		final StringBuilder builder = new StringBuilder();
		builder.append(wordArray[0].toLowerCase());
		Arrays.stream(wordArray)
		      .skip(1)
		      .forEach(w -> {
			      builder.append(w.substring(0, 1).toUpperCase());
			      builder.append(w.substring(1).toLowerCase());
		      });
		return builder.toString();
	}
}
