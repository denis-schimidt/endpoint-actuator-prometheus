package org.example.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import org.example.actuator.metrics.Metric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

	@Autowired
	private HelloWorldService helloWorldService;

	@Autowired
	private MeterRegistry registry;

	@RequestMapping("/ok")
	@ResponseBody
	@Metric(prefix = "endpoint-actuator.ok")
	public Map<String, String> ok() {
		LOGGER.info(this.helloWorldService.getHelloMessage());

		return Collections.singletonMap("message", this.helloWorldService.getHelloMessage());
	}

	@RequestMapping("/deu-ruim")
	@ResponseBody
	@Metric(prefix = "endpoint-actuator.deu-ruim")
	public ResponseEntity<String> deuRuim() {
		LOGGER.error("Deu ruim no Controller");
		return new ResponseEntity("Server error", INTERNAL_SERVER_ERROR);
	}
}
