import { render, screen, fireEvent } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import BookingsTable from "../../components/booking/BookingsTable"

const mockBookingInfo = [
    {
        id: 1,
        room: {
            id: "101",
            roomType: "Deluxe"
        },
        checkInDate: "2024-01-01",
        checkOutDate: "2024-01-05",
        guestName: "John Doe",
        guestEmail: "john@example.com",
        numOfAdults: 2,
        numOfChildren: 1,
        totalNumOfGuests: 3,
        bookingConfirmationCode: "ABC123"
    }
]

describe("BookingsTable", () => {
    const mockHandleBookingCancellation = vi.fn()

    test("renders table with booking information", () => {
        render(
            <BookingsTable
                bookingInfo={mockBookingInfo}
                handleBookingCancellation={mockHandleBookingCancellation}
            />
        )

        expect(screen.getByText("John Doe")).toBeInTheDocument()
        expect(screen.getByText("ABC123")).toBeInTheDocument()
        expect(screen.getByText("Deluxe")).toBeInTheDocument()
    })

    test("handles booking cancellation", () => {
        render(
            <BookingsTable
                bookingInfo={mockBookingInfo}
                handleBookingCancellation={mockHandleBookingCancellation}
            />
        )

        const cancelButton = screen.getByText("Cancel")
        fireEvent.click(cancelButton)

        expect(mockHandleBookingCancellation).toHaveBeenCalledWith(1)
    })
})
