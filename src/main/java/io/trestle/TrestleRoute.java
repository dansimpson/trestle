package io.trestle;


//public class TrestleRoute<T> implements Comparable<TrestleRoute<T>> {
//
//	private T context;
//	
//	private String method = "GET";
//
//	/**
//	 * Priority of route Eg: /apps/test > /apps/:id since test is explicit
//	 */
//	private Integer priority = 0;
//	private Pattern pattern;
//
//	/**
//	 * Navigation
//	 */
//	private TrestleRoute<T> parent;
//	private ConcurrentSkipListSet<TrestleRoute<T>> children;
//
//	/**
//	 * Is this part of the route matching?
//	 * 
//	 * @param fragment
//	 * @return
//	 */
//	private boolean matches(String fragment) {
//		return pattern.matcher(fragment).matches();
//	}
//
//	/**
//	 * Get a child based on path part expression
//	 * 
//	 * @param expression
//	 * @return
//	 */
//	public TrestleRoute<T> getChild(String fragment) {
//		if (!isLeaf()) {
//			for (TrestleRoute<T> node : children) {
//				if (node.matches(fragment)) {
//					return node;
//				}
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * Add a child to to
//	 * 
//	 * @param child
//	 * @return
//	 */
//	public boolean addChild(TrestleRoute<T> child) {
//		return children.add(child);
//	}
//
//	/**
//	 * Get a sorted path with each route as an element
//	 * 
//	 * @return
//	 */
//	public LinkedList<TrestleRoute<T>> getPath() {
//		LinkedList<TrestleRoute<T>> items = new LinkedList<TrestleRoute<T>>();
//		TrestleRoute<T> target = this;
//		while (target != null) {
//			items.addFirst(target);
//			target = target.parent;
//		}
//		return items;
//	}
//
//	public TrestleRoute<T> route(HttpServletRequest path) {
//		return route(path.getRequestURI());
//	}
//
//	public TrestleRoute<T> route(String path) {
//
//		// build list
//		// build
//
//		// LinkedList<RouterNode<T>> path = getPath();
//		return new TrestleRoute<T>();
//	}
//
//	public boolean isLeaf() {
//		return children == null;
//	}
//
//	public LinkedList<TrestleRoute<T>> getPath(LinkedList<String> steps) {
//		steps.removeFirst();
//		LinkedList<TrestleRoute<T>> result = new LinkedList<TrestleRoute<T>>();
//		result.add(this);
//		if (steps.size() == 1) {
//			return result;
//		}
//		result.addAll(getPath(steps));
//		return result;
//	}
//
//	public void add(String path, T context) {
//
//		if ("".equals(path) || "/".equals(path)) {
//			this.context = context;
//			this.pattern = Pattern.compile("/");
//		} else {
//
//			int split = path.indexOf('/');
//			if (split < 0) {
//				//addChild(new TrestleRoute(path, context));
//			}
//
//			String next = path.substring(0, split);
//			String rest = path.substring(split);
//
//			/*if (hasChild(next)) {
//				child(next).add(rest, context);
//			} else {
//
//			}*/
//
//			//path.split();
//
//			// build(path).setContext(context);
//
//		}
//
//	}
//
//	@Override
//	public int compareTo(TrestleRoute<T> o) {
//		return priority.compareTo(o.priority);
//	}
//
//}
