package com.deploypilot.domain.audit;

import com.deploypilot.domain.user.User;
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
@Table(name = "audit_log")
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "actor_id", nullable = false)
	private User actor;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 100)
	private AuditAction action;

	@Enumerated(EnumType.STRING)
	@Column(name = "target_type", nullable = false, length = 100)
	private AuditTargetType targetType;

	@Column(name = "target_id", nullable = false)
	private Long targetId;

	@Column(nullable = false, length = 500)
	private String message;

	@Column(name = "ip_address", length = 100)
	private String ipAddress;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	protected AuditLog() {
	}

	private AuditLog(
			User actor,
			AuditAction action,
			AuditTargetType targetType,
			Long targetId,
			String message,
			String ipAddress
	) {
		this.actor = actor;
		this.action = action;
		this.targetType = targetType;
		this.targetId = targetId;
		this.message = message;
		this.ipAddress = ipAddress;
	}

	public static AuditLog createRollbackLog(User actor, Long releaseId, String targetVersion, String reason, String ipAddress) {
		return new AuditLog(
				actor,
				AuditAction.ROLLBACK_CREATED,
				AuditTargetType.RELEASE,
				releaseId,
				"Rollback to " + targetVersion + ": " + reason,
				ipAddress
		);
	}

	@PrePersist
	void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public User getActor() {
		return actor;
	}

	public AuditAction getAction() {
		return action;
	}

	public AuditTargetType getTargetType() {
		return targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public String getMessage() {
		return message;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
