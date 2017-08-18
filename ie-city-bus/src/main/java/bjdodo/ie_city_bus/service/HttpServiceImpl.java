package bjdodo.ie_city_bus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class HttpServiceImpl implements HttpService {
	
	private static final Logger log = LoggerFactory.getLogger(HttpServiceImpl.class);
	
	@Override
	public String get(String url) {
		Client c = Client.create();
		WebResource service = c.resource(url);
		ClientResponse resp = service.get(ClientResponse.class);
		if (resp.getStatus() == HttpStatus.OK.value()) {
			return resp.getEntity(String.class);
		} else {
			log.warn("http get request to url '%s' returned HTTP code %d", url, resp.getStatus());
			return "";
		}

	}
}
