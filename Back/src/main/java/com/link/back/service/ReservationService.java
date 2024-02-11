package com.link.back.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.link.back.dto.request.ReservationRequest;
import com.link.back.dto.response.ReservationResponse;
import com.link.back.entity.Reservation;
import com.link.back.entity.User;
import com.link.back.repository.ReservationRepository;
import com.link.back.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;

	public List<ReservationResponse> getMyReservations(Long myUserId) {

		User user = userRepository.getReferenceById(myUserId);
		List<Reservation> reservations = reservationRepository.findByLeaderOrMember(user, user);

		return reservations.stream().map(reservation ->
			ReservationResponse.builder()
				.reservationId(reservation.getReservationId())
				.userId(
					reservation.isLeader(user.getUserId()) ? reservation.getMember().getUserId() :
						reservation.getLeader().getUserId())
				.isLeader(reservation.isLeader(user.getUserId()))
				.reservationDatetime(reservation.getReservationDateTime())
				.build()
		).toList();
	}

	public void createMyReservation(Long myUserId, ReservationRequest reservationRequest) {
		User user = userRepository.getReferenceById(myUserId);
		if (isOverlappedReservation(user, reservationRequest.reservationDateTime())) // 겹치는 예약 시간 확인
			throw new RuntimeException();
		Reservation reservation = Reservation.builder()
			.leader(userRepository.getReferenceById(reservationRequest.userId()))
			.member(user)
			.reservationDateTime(reservationRequest.reservationDateTime())
			.build();
		reservationRepository.save(reservation);
	}

	public void updateMyReservation(Long reservationId, Long myUserId, ReservationRequest reservationRequest) {
		User user = userRepository.getReferenceById(myUserId);
		// if (isUsersReservation(reservationId, myUserId)) // 자신의 예약인지 확인
		// 	throw new RuntimeException();
		if (isOverlappedReservation(user, reservationRequest.reservationDateTime())) // 겹치는 예약 시간 확인
			throw new RuntimeException();
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
		reservation.update(reservationRequest.reservationDateTime());
		reservationRepository.save(reservation);
	}

	public void deleteReservation(Long reservationId) {
		reservationRepository.deleteById(reservationId);
	}

	public boolean checkReservation(Long reservationId, Long userId) {
		Optional<Reservation> reservation = reservationRepository.findById(reservationId);
		return reservation.filter(value -> value.isMyReservation(userId)).isPresent();
	}

	private boolean isOverlappedReservation(User user, LocalDateTime reservationDateTime) {
		List<Reservation> reservations = reservationRepository.findByLeaderOrMember(user, user);
		for (Reservation r : reservations) {
			if (r.getReservationDateTime().equals(reservationDateTime))
				return true;
		}
		return false;
	}

}
