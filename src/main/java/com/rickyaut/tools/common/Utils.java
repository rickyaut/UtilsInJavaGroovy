package com.rickyaut.tools.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class Utils {

	public static String getResourceContentFromClassPath(String resourcePath)
			throws IOException {
		ClassLoader cl = Utils.class.getClassLoader();
		InputStream is = cl.getResourceAsStream(resourcePath);
		return IOUtils.toString(is);
	}

	public static String[] findRegularExpressionMatcher(String s, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(s);
		List<String> matches = new ArrayList<String>();
		while (m.find()) {
			matches.add(m.group());
		}
		return matches.toArray(new String[matches.size()]);
	}

	public static String askForConfirm(String confirmMsg,
			String... allowedAnswers) {
		int index = 0;
		for (String answ : allowedAnswers) {
			allowedAnswers[index++] = answ.toLowerCase();
		}
		System.out.print(confirmMsg);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		do {
			try {
				s = in.readLine().toLowerCase();
			} catch (IOException e) {
				break;
			}
		} while (!ArrayUtils.contains(allowedAnswers, s));
		return s;
	}

	public static String askForInput(String confirmMsg) {
		System.out.print(confirmMsg);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		try {
			s = in.readLine().toLowerCase();
		} catch (IOException e) {
		}
		return s;
	}

	public static String doubleCheckDefaultAnswer(String question, String answer) {
		System.out.print(String.format("%s(%s):", question, answer));
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		try {
			s = in.readLine().toLowerCase();
			if (StringUtils.isNotBlank(s)) {
				return s;
			}
		} catch (IOException e) {
		}
		return answer;
	}

	public static String encodeString(String s) {
		return String.format("%x", new BigInteger(s.getBytes()));
	}

}
