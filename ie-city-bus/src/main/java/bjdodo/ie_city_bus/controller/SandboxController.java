package bjdodo.ie_city_bus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.model.customquery.CustomQueries;

@RestController
@RequestMapping("/sandbox")
public class SandboxController {

	@Autowired
	CustomQueries customQueries;

	@RequestMapping(value = "/querytest", method = RequestMethod.POST)
	List<Object> querytest(@RequestBody String body) {
		return customQueries.runRandomQuery(body);
	}
}
