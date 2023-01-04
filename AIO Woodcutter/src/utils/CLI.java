package utils;

import java.util.HashMap;

public class CLI {

	public HashMap<String, String> compileParameters(String parameterString) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (parameterString != null && !parameterString.isEmpty()) {
			String[] contents = parameterString.split("\\.");
			if (contents != null && contents.length > 0) {
				for (int i = 0; i < contents.length; i += 2) {
					String key = contents[i];
					key = key.toUpperCase();
					String value = (i + 1 < contents.length ? contents[i + 1] : "");
					value = value.replaceAll("-", " ");
					parameters.put(key, value);
				}
			}
		}
		return parameters;
	}
}
