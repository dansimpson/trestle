package io.trestle.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleUtil {

	private static Logger log = LoggerFactory.getLogger(Bundle.class);

	public static List<File> hierarchy(File root) {

		LinkedList<File> result = new LinkedList<File>();

		if (root.getName().startsWith(".")) {
			return result;
		}

		if (root.isDirectory()) {
			File[] children = root.listFiles();
			if (children != null) {
				for (File child : children) {
					result.addAll(hierarchy(child));
				}
			}
		} else {
			result.add(root);
		}

		return result;
	}

	public static List<File> eglob(String path) {

		String base = path.replace("//", "/");
		String root = slap(base.split("/"));

		base = base.replace("**/", "[[\\w\\d\\-\\_]+/]~~~").replace("*.", ".+\\.").replace("*", ".+").replace("~~~", "*");

		Pattern regex = Pattern.compile(base);

		List<File> files = hierarchy(new File(root));
		List<File> result = new ArrayList<File>();

		for (File file : files) {
			if (regex.matcher(file.getPath().replace('\\', '/')).matches()) {
				result.add(file);
			}
		}

		// Sort based on depth
		Collections.sort(result, new Comparator<File>() {

			@Override
			public int compare(File f1, File f2) {
				String p1 = f1.getPath().replace('\\', '/');
				String p2 = f2.getPath().replace('\\', '/');
				Integer l1 = p1.split("/").length;
				Integer l2 = p2.split("/").length;
				return l1.compareTo(l2);
			}
		});

		if (log.isDebugEnabled()) {
			log.debug("Built Bundle");
			for (File f : result) {
				log.debug(f.getPath());
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
