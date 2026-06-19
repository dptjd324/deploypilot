package com.deploypilot.domain.release;

public enum ReleaseStatus {
	CREATED,
	CI_RUNNING,
	CI_SUCCESS,
	CI_FAILED,
	APPROVED,
	DEPLOYED,
	STABLE,
	FAILED,
	ROLLED_BACK
}
