package ir.ac.kntu.hotelbookingapp.repository;

import ir.ac.kntu.hotelbookingapp.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
	void deleteById(Long roomId);

	List<BookedRoom> findByRoomId(Long roomId);

	BookedRoom findByBookingConfirmationCode(String confirmationCode);

	List<BookedRoom> findByGuestEmail(String email);
}
