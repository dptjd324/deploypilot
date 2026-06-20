package com.deploypilot.domain.health;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClientHealthCheckClient implements HealthCheckClient {

	private final RestTemplate restTemplate;

	public RestClientHealthCheckClient(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder
				.connectTimeout(Duration.ofSeconds(3))
				.readTimeout(Duration.ofSeconds(5))
				.build();
	}

	@Override
	public HealthCheckResponse get(String url) {
		long startedAt = System.nanoTime();
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			long responseTimeMs = Duration.ofNanos(System.nanoTime() - startedAt).toMillis();
			return new HealthCheckResponse(
					response.getStatusCode().value(),
					responseTimeMs,
					response.getBody()
			);
		} catch (RestClientException exception) {
			long responseTimeMs = Duration.ofNanos(System.nanoTime() - startedAt).toMillis();
			throw new HealthCheckClientException(responseTimeMs, exception.getMessage(), exception);
		}
	}
}
