package com.deploypilot.domain.smoke;

public interface SmokeTestClient {

	SmokeTestHttpResponse execute(SmokeTestMethod method, String url);
}
