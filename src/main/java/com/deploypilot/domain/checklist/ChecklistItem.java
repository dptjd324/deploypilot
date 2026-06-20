package com.deploypilot.domain.checklist;

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
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_item")
public class ChecklistItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "release_id", nullable = false)
	private Release release;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(length = 500)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private ChecklistPhase phase;

	@Column(nullable = false)
	private boolean required;

	@Column(nullable = false)
	private boolean checked;

	@Column(name = "checked_by", length = 100)
	private String checkedBy;

	@Column(name = "checked_at")
	private LocalDateTime checkedAt;

	protected ChecklistItem() {
	}

	private ChecklistItem(
			Release release,
			String title,
			String description,
			ChecklistPhase phase,
			boolean required
	) {
		this.release = release;
		this.title = title;
		this.description = description;
		this.phase = phase;
		this.required = required;
		this.checked = false;
	}

	public static ChecklistItem create(
			Release release,
			String title,
			String description,
			ChecklistPhase phase,
			boolean required
	) {
		return new ChecklistItem(release, title, description, phase, required);
	}

	public void check(String checkedBy) {
		this.checked = true;
		this.checkedBy = checkedBy;
		this.checkedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Release getRelease() {
		return release;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public ChecklistPhase getPhase() {
		return phase;
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isChecked() {
		return checked;
	}

	public String getCheckedBy() {
		return checkedBy;
	}

	public LocalDateTime getCheckedAt() {
		return checkedAt;
	}
}
