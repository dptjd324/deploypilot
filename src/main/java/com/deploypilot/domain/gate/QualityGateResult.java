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
@Table(name = "quality_gate_result")
public class QualityGateResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "release_id", nullable = false)
	private Release release;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private GateResult result;

	@Column(nullable = false, length = 500)
	private String reason;

	@Column(name = "checked_at", nullable = false)
	private LocalDateTime checkedAt;

	protected QualityGateResult() {
	}

	private QualityGateResult(Release release, GateResult result, String reason) {
		this.release = release;
		this.result = result;
		this.reason = reason;
	}

	public static QualityGateResult create(Release release, GateResult result, String reason) {
		return new QualityGateResult(release, result, reason);
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

	public GateResult getResult() {
		return result;
	}

	public String getReason() {
		return reason;
	}

	public LocalDateTime getCheckedAt() {
		return checkedAt;
	}
}
