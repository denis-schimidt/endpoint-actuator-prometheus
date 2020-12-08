package org.example.actuator;

import org.example.actuator.metrics.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
@Description("A controller for handling requests for hello messages")
public class SampleController {

	@Autowired
	private HelloWorldService helloWorldService;

	@RequestMapping("/ok")
	@ResponseBody
	@Metric(prefix = "endpoint-actuator.ok")
	public Map<String, String> ok() {
		return Collections.singletonMap("message", this.helloWorldService.getHelloMessage());
	}

	@RequestMapping("/deu-ruim")
	@ResponseBody
	@Metric(prefix = "endpoint-actuator.deu-ruim")
	public ResponseEntity<String> deuRuim() {
		return new ResponseEntity("Server error", INTERNAL_SERVER_ERROR);
	}
}
