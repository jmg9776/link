package com.link.back.entity;

import static com.link.back.config.AppConstant.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Hackathon {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long hackathonId;

	@Column(nullable = false, length = HACKATHON_NAME_LENGTH)
	private String hackathonName;

	@Column(nullable = false)
	private LocalDate registerDate;

	@Column(nullable = false)
	private LocalDate teamDeadlineDate;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column(nullable = false)
	private Integer maxPoint;

	@Builder
	public Hackathon(Long hackathonId, String hackathonName, LocalDate registerDate, LocalDate teamDeadlineDate,
		LocalDate startDate, LocalDate endDate, Integer maxPoint) {
		this.hackathonId = hackathonId;
		this.hackathonName = hackathonName;
		this.registerDate = registerDate;
		this.teamDeadlineDate = teamDeadlineDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.maxPoint = maxPoint;
	}
}
