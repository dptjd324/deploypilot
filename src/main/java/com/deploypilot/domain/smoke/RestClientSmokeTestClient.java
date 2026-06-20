package com.deploypilot.domain.smoke;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClientSmokeTestClient implements SmokeTestClient {

	private final RestTemplate restTemplate;

	public RestClientSmokeTestClient(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder
				.connectTimeout(Duration.ofSeconds(3))
				.readTimeout(Duration.ofSeconds(5))
				.build();
	}

	@Override
	public SmokeTestHttpResponse execute(SmokeTestMethod method, String url) {
		long startedAt = System.nanoTime();
		try {
			ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.valueOf(method.name()), null, Void.class);
			long responseTimeMs = Duration.ofNanos(System.nanoTime() - startedAt).toMillis();
			return new SmokeTestHttpResponse(response.getStatusCode().value(), responseTimeMs);
		} catch (RestClientException exception) {
			long responseTimeMs = Duration.ofNanos(System.nanoTime() - startedAt).toMillis();
			throw new SmokeTestClientException(responseTimeMs, exception.getMessage(), exception);
		}
	}
}
