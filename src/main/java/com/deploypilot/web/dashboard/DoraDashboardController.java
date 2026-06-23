package com.deploypilot.web.dashboard;

import com.deploypilot.domain.dashboard.DoraDashboardService;
import com.deploypilot.domain.dashboard.dto.DoraDashboardResponse;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard/dora")
public class DoraDashboardController {

	private final DoraDashboardService doraDashboardService;

	public DoraDashboardController(DoraDashboardService doraDashboardService) {
		this.doraDashboardService = doraDashboardService;
	}

	@GetMapping
	public DoraDashboardResponse calculate(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
	) {
		return doraDashboardService.calculate(from, to);
	}
}
