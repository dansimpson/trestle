package io.trestle.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class BundleUtil {

	public static List<String> hierarchy(File root) {

		LinkedList<String> result = new LinkedList<String>();

		if (root.getName().startsWith(".")) {
			return result;
		}

		if (root.isDirectory()) {
			File[] children = root.listFiles();
			if(children != null) {
				for (File child : children) {
					result.addAll(hierarchy(child));
				}
			}
		} else {
			result.add(root.getPath());
		}

		return result;
	}

	public static List<String> eglob(String path) {

		String base = path.replace("//", "/");
		String root = slap(base.split("/"));

		base = base.replace("**/", "[[\\w\\d\\-\\_]+/]~~~")
				.replace("*.", ".+\\.").replace("*", ".+").replace("~~~", "*");

		Pattern regex = Pattern.compile(base);

		List<String> files = hierarchy(new File(root));
		List<String> result = new ArrayList<String>();

		for (String file : files) {
			if (regex.matcher(file).matches()) {
				result.add(file);
			}
		}

		return result;
	}

	private static String slap(String[] parts) {
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < parts.length; i++) {
			if (parts[i].contains("*")) {
				break;
			}
			buf.append(parts[i]);
			buf.append("/");
		}

		return buf.toString();
	}

}
