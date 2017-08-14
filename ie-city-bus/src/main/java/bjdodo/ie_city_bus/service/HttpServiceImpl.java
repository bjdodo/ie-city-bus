package bjdodo.ie_city_bus.service;

import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class HttpServiceImpl implements HttpService {
	@Override
	public String get(String url) {
		Client c = Client.create();
		WebResource service = c.resource(url);
		ClientResponse resp = service.get(ClientResponse.class);
		if (resp.getStatus() == 200) {
			return resp.getEntity(String.class);
		} else {
			return "";
		}

	}
}
