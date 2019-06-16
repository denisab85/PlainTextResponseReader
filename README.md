#PlainTextResponseReader
This sample demonstrates a workaround for MessageBodyProviderNotFoundException when handling Content-Type: text/plain responses containing JSON objects using Jersey JAX-RS Client.

##Background
When using Jersey JAX-RS library to build a REST client, you want to make sure your RESTful service returns correct Content-Type.
However, some web services respond with Content-Type: text/plain, even when returned content is a valid JSON format.

##Problem
When Jersey JAX-RS attempts to parse such a malformed response, it will throw an error:

```java
javax.ws.rs.client.ResponseProcessingException: org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException: MessageBodyReader not found for media type=text/plain, type=class MyCustomClass, genericType=class MyCustomClass
```

##Solution
Let's first look at how Jersey JAX-RS  will parse a JSON response with a correct Content-Type: application/json.
When it gets such a response, it looks for any available implementation of the javax.ws.rs.ext.MessageBodyReader interface which is annotated as:

```java
@Consumes(MediaType.APPLICATION_JSON)
```

This can be any implementation, but for our example we added MOXyJsonProvider as a dependency to the project.

Next, we want to make it also handle Content-Type: text/plain responses.
For this, we inherit our custom response reader from MOXyJsonProvider at the same time annotating it with MediaType.TEXT_PLAIN:

```java
@Provider
@Consumes(MediaType.TEXT_PLAIN)
public class MyCustomResponseReader extends MOXyJsonProvider {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return genericType.equals(MyCustomClass.class);
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException {
		return super.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream);
	}

}
```

Finally, we need to register our custom reader in the instance of javax.ws.rs.client.Client that will be querying our web service:

```java
Client client = ClientBuilder.newBuilder().build().register(MyCustomResponseReader.class);
```
Now a text/plain response will be parsed like any application/json one.

##Credits
I based this solution on the information found in the following resources:

**Stack Overflow**
- [Create a text/plain Jersey response](https://stackoverflow.com/questions/22611105/create-a-text-plain-jersey-response).
- [How to register my custom MessageBodyReader in my CLIENT?](https://stackoverflow.com/questions/24197076/how-to-register-my-custom-messagebodyreader-in-my-client).

**Other**
- [How to register my custom MessageBodyReader in my CLIENT?](https://www.igorkromin.net/index.php/2017/10/26/writing-a-custom-messagebodyreader-to-process-post-body-data-with-jersey/).

