package io.trestle.example;

import io.trestle.Context;
import io.trestle.TrestleServlet;
import io.trestle.annotation.Route;
import io.trestle.response.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("serial")
public class PairServlet extends TrestleServlet {

	@SuppressWarnings("unused")
	private class Pair {
		public String key;
		public String value;
	}
	
	private static final Map<Integer, Pair> pairs = new ConcurrentHashMap<Integer, Pair>();
	
	/**
	 * Send a json array of all pairs
	 */
	@Route(match = "/")
	public Response list(Context ctx) {
		return json(pairs.entrySet());
	}
	
	/**
	 * Send a single pair as json
	 */
	@Route(match = "/:id")
	public Response show(Context ctx) {
		
		Pair pair = pairs.get(ctx.paramAsInt("id"));
		if(pair == null) {
			return error(404, "Pair not found");			
		}
		
		return json(pair);
	}
	
	/**
	 * Update a pair
	 */
	@Route(match = "/:id", via = "put")
	public Response update(Context ctx) {
		
		Pair pair = ctx.read(Pair.class);
		if(pair != null) {
			return error(422, "Unable to process pair");
		}
		
		pairs.put(ctx.paramAsInt("id"), pair);
		
		return status(204);
	}
	
	/**
	 * Create a new pair
	 */
	@Route(match = "/", via = "post")
	public Response create(Context ctx) {
		
		Pair pair = ctx.read(Pair.class);
		if(pair != null) {
			return error(422, "Unable to process pair");
		}
		
		Integer id = ctx.paramAsInt("id");
		if(pairs.containsKey(id)) {
			return error(400, "Pair already created");
		}
		
		pairs.put(id, pair);
		
		return status(201);
	}
	
	/**
	 * Delete a pair :(
	 */
	@Route(match = "/:id", via = "delete")
	public Response delete(Context ctx) {
		
		Integer id = ctx.paramAsInt("id");
		
		if(!pairs.containsKey(id)) {
			return error(404, "Pair not found");			
		}
		
		pairs.remove(id);
		
		return status(204);
	}
}