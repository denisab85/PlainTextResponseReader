import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

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
