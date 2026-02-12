import { render, screen, fireEvent, act } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import BookingSummary from "../../components/booking/BookingSummary"

// Mock react-router-dom properly
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom')
    return {
        ...actual,
        useNavigate: () => mockNavigate
    }
})

const mockNavigate = vi.fn()

const mockBooking = {
    guestFullName: "John Doe",
    guestEmail: "john@example.com",
    checkInDate: "2024-01-01",
    checkOutDate: "2024-01-05",
    numOfAdults: 2,
    numOfChildren: 1
}

const renderBookingSummary = (props) => {
    return render(
        <BookingSummary
            booking={mockBooking}
            payment={100}
            isFormValid={true}
            onConfirm={() => {}}
            {...props}
        />
    )
}

describe("BookingSummary", () => {
    test("displays booking information correctly", () => {
        renderBookingSummary()

        expect(screen.getByText(/John Doe/)).toBeInTheDocument()
        expect(screen.getByText(/john@example.com/)).toBeInTheDocument()
        expect(screen.getByText(/Jan 1st 2024/)).toBeInTheDocument()
        expect(screen.getByText(/Jan 5th 2024/)).toBeInTheDocument()
        expect(screen.getByText(/Adults : 2/)).toBeInTheDocument()
        expect(screen.getByText(/Children : 1/)).toBeInTheDocument()
    })

    test("shows payment amount when payment is greater than 0", () => {
        renderBookingSummary({ payment: 100 })
        expect(screen.getByText(/\$100/)).toBeInTheDocument()
    })

    test("shows error message when payment is 0", () => {
        renderBookingSummary({ payment: 0 })
        expect(screen.getByText(/Check-out date must be after check-in date./)).toBeInTheDocument()
    })

    test("handles booking confirmation process", async () => {
        vi.useFakeTimers()
        const onConfirm = vi.fn()
        renderBookingSummary({ onConfirm })

        const confirmButton = screen.getByText(/Confirm Booking & proceed to payment/)
        fireEvent.click(confirmButton)

        expect(screen.getByText(/Booking Confirmed, redirecting to payment.../)).toBeInTheDocument()

        await act(async () => {
            vi.advanceTimersByTime(3000)
        })

        expect(onConfirm).toHaveBeenCalled()
        expect(mockNavigate).toHaveBeenCalledWith("/booking-success")

        vi.useRealTimers()
    })

    test("disables confirm button when form is invalid", () => {
        renderBookingSummary({ isFormValid: false })
        expect(screen.queryByText(/Confirm Booking & proceed to payment/)).not.toBeInTheDocument()
    })
})
