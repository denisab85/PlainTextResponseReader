import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class Main {

	private static final String URI = "http://localhost";

	public static void main(String[] args) {

		Client client = ClientBuilder.newBuilder().build().register(MyCustomResponseReader.class);
		MyCustomClass responseObject = client.target(URI).path("service").request(MediaType.TEXT_PLAIN).get(MyCustomClass.class);
		System.out.println(responseObject.getStatus());

	}

}
