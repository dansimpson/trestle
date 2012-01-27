# Trestle
Inspired by Sinatra, Sprockets, and modern Javascript micro-frameworks such as Backbone.js, Trestle bridges the gap between your backend services and your html5/javascript applications.

## Features
* Annotation based routing for servlets
* JSON entity serialization/deserialization via Jackson
* Asset pipeline
* LessCSS support
* YUI Compressor support
* Asset servlet for serving processed assets

## Example Restful Servlet
The below example shows a simple resource servlet that deals with json.

```java
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
```

## Asset Pipeline - AKA Bundles
Creating bundles is fairly simple.  Set the root directory, add paths (globs or eglobs work),
and optionally add processors.

### Example: Vendor Javascript Bundle
Create bundle and add all files in the javascripts/vendor directory, with
some explicit ordering.

```java
Bundle core = new Bundle("vendor");
core.setRoot("javascripts/vendor");
core.addPath("/jquery.js");
core.addPath("/underscore.js");
core.addPath("/backbone.js");
core.addPath("/**/*.js");
if(production) {
  core.enableCaching();
}
```

Including vendor.js:

```html
<script src="/assets/vendor.js" type="text/javascript"></script>
```

### Example: Create a Template Bundle
Trestle can wrap your templates and combine them into a single
file to simplify client-side template processing.

```java
Bundle templates = new Bundle("templates");
templates.setRoot("javascripts/templates");
templates.addPath("/**/*.mustache");
templates.addProcessor(new TemplateProcessor("Tpls"));
```

Including the templates:

```html
<script src="/assets/templates.mustache" type="text/javascript"></script>
```

Accessing the templates:

```javascript
var output = Mustache.render(Tpls["pairs/index.mustache"], context);
```

### Example: LessCSS Bundle

```java
Bundle styles = new Bundle("styles");
styles.setRoot("stylesheets");
styles.addPath("/structure.less");
styles.addPath("/**/*.less");
styles.addProcessor(new LessProcessor(false));
```

Including the templates:

```html
<script src="/assets/styles.less" type="text/javascript"></script>
```

## Wiring it all up with Jetty
Take what we have done above and make it work... on port 4000.  This kind of
servlet container (embedded jetty) works nicely.

```java
Server server = new Server(4000);
ServletContextHandler root = new ServletContextHandler(
    ServletContextHandler.SESSIONS);

root.setContextPath("/");
root.setResourceBase("./");

root.addServlet(new ServletHolder(new PairServlet()), "/pairs/*");
root.addServlet(new ServletHolder(new BundleServlet()), "/assets/*");
root.addServlet(new ServletHolder(new DefaultServlet()), "/*");

server.setHandler(root);
server.start();
```

## Maven
```xml
<dependency>
  <groupId>io.trestle</groupId>
  <artifactId>trestle</artifactId>
  <version>0.9.1</version>
</dependency>
```

## Contributing - Guidlines
* Fork
* Do Work
* Rebase upstream/master
* Run Tests
* Send Pull Request
