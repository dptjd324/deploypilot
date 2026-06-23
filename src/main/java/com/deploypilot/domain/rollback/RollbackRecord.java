package com.deploypilot.domain.rollback;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.user.User;
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
import java.time.LocalDateTime;

@Entity
@Table(name = "rollback_record")
public class RollbackRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "release_id", nullable = false)
	private Release release;

	@Column(name = "target_version", nullable = false, length = 100)
	private String targetVersion;

	@Column(nullable = false, length = 500)
	private String reason;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "created_by", nullable = false)
	private User createdBy;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	protected RollbackRecord() {
	}

	private RollbackRecord(Release release, String targetVersion, String reason, User createdBy) {
		this.release = release;
		this.targetVersion = targetVersion;
		this.reason = reason;
		this.createdBy = createdBy;
	}

	public static RollbackRecord create(Release release, String targetVersion, String reason, User createdBy) {
		return new RollbackRecord(release, targetVersion, reason, createdBy);
	}

	@PrePersist
	void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Release getRelease() {
		return release;
	}

	public String getTargetVersion() {
		return targetVersion;
	}

	public String getReason() {
		return reason;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
