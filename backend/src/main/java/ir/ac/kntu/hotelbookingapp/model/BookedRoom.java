package ir.ac.kntu.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
	@Id
	@GeneratedValue
	private Long bookingId;

	@Column(name = "check_in")
	private LocalDate checkInDate;

	@Column(name = "check_out")
	private LocalDate checkOutDate;

	@Column(name = "guest_full_name")
	private String guestFullName;

	@Column(name = "guest_email")
	private String guestEmail;

	@Column(name = "adults")
	private int numberOfAdults;

	@Column(name = "children")
	private int numberOfChildren;

	@Column(name = "total_guests")
	private int totalNumberOfGuests;

	@Setter
	@Column(name = "confirmation")
	private String bookingConfirmationCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	public void calculateTotalNumberOfGuests() {
		this.totalNumberOfGuests = this.numberOfAdults + this.numberOfChildren;
	}

	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
		calculateTotalNumberOfGuests();
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
		calculateTotalNumberOfGuests();
	}
}
