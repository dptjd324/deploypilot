package com.deploypilot.domain.gate;

import com.deploypilot.domain.release.Release;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "quality_check_result")
public class QualityCheckResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "release_id", nullable = false)
	private Release release;

	@Enumerated(EnumType.STRING)
	@Column(name = "check_type", nullable = false, length = 50)
	private CheckType checkType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private CheckStatus status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private Severity severity;

	@Column(nullable = false, length = 500)
	private String message;

	@Column(name = "detail_json", columnDefinition = "TEXT")
	private String detailJson;

	@Column(name = "checked_at", nullable = false)
	private LocalDateTime checkedAt;

	protected QualityCheckResult() {
	}

	private QualityCheckResult(
			Release release,
			CheckType checkType,
			CheckStatus status,
			Severity severity,
			String message,
			String detailJson
	) {
		this.release = release;
		this.checkType = checkType;
		this.status = status;
		this.severity = severity;
		this.message = message;
		this.detailJson = detailJson;
	}

	public static QualityCheckResult create(
			Release release,
			CheckType checkType,
			CheckStatus status,
			Severity severity,
			String message,
			String detailJson
	) {
		return new QualityCheckResult(release, checkType, status, severity, message, detailJson);
	}

	@PrePersist
	void prePersist() {
		this.checkedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Release getRelease() {
		return release;
	}

	public CheckType getCheckType() {
		return checkType;
	}

	public CheckStatus getStatus() {
		return status;
	}

	public Severity getSeverity() {
		return severity;
	}

	public String getMessage() {
		return message;
	}

	public String getDetailJson() {
		return detailJson;
	}

	public LocalDateTime getCheckedAt() {
		return checkedAt;
	}
}
