package com.deploypilot.domain.metric;

import com.deploypilot.domain.release.Release;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "release_metric")
public class ReleaseMetric {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "release_id", nullable = false)
	private Release release;

	@Column(name = "total_requests", nullable = false)
	private long totalRequests;

	@Column(name = "error_5xx_count", nullable = false)
	private long error5xxCount;

	@Column(name = "error_rate", nullable = false, precision = 6, scale = 2)
	private BigDecimal errorRate;

	@Column(name = "avg_response_time_ms", nullable = false)
	private long avgResponseTimeMs;

	@Column(name = "p95_response_time_ms", nullable = false)
	private long p95ResponseTimeMs;

	@Column(name = "collected_at", nullable = false)
	private LocalDateTime collectedAt;

	protected ReleaseMetric() {
	}

	private ReleaseMetric(
			Release release,
			long totalRequests,
			long error5xxCount,
			long avgResponseTimeMs,
			long p95ResponseTimeMs
	) {
		this.release = release;
		this.totalRequests = totalRequests;
		this.error5xxCount = error5xxCount;
		this.errorRate = calculateErrorRate(totalRequests, error5xxCount);
		this.avgResponseTimeMs = avgResponseTimeMs;
		this.p95ResponseTimeMs = p95ResponseTimeMs;
	}

	public static ReleaseMetric create(
			Release release,
			long totalRequests,
			long error5xxCount,
			long avgResponseTimeMs,
			long p95ResponseTimeMs
	) {
		return new ReleaseMetric(release, totalRequests, error5xxCount, avgResponseTimeMs, p95ResponseTimeMs);
	}

	@PrePersist
	void prePersist() {
		this.collectedAt = LocalDateTime.now();
	}

	private BigDecimal calculateErrorRate(long totalRequests, long error5xxCount) {
		if (totalRequests <= 0) {
			return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		}
		return BigDecimal.valueOf(error5xxCount)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(totalRequests), 2, RoundingMode.HALF_UP);
	}

	public Long getId() {
		return id;
	}

	public Release getRelease() {
		return release;
	}

	public long getTotalRequests() {
		return totalRequests;
	}

	public long getError5xxCount() {
		return error5xxCount;
	}

	public BigDecimal getErrorRate() {
		return errorRate;
	}

	public long getAvgResponseTimeMs() {
		return avgResponseTimeMs;
	}

	public long getP95ResponseTimeMs() {
		return p95ResponseTimeMs;
	}

	public LocalDateTime getCollectedAt() {
		return collectedAt;
	}
}
