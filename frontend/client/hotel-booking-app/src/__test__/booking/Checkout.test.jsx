import { render, screen } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import { BrowserRouter } from 'react-router-dom'
import Checkout from "../../components/booking/Checkout"

vi.mock('../../components/utils/ApiFunctions', () => ({
    getRoomById: vi.fn(() => Promise.resolve({
        photo: "base64EncodedString",
        roomType: "Deluxe Suite",
        roomPrice: "200"
    }))
}))

vi.mock('../../components/booking/BookingForm', () => ({
    default: () => <div data-testid="booking-form">Booking Form</div>
}))

vi.mock('../../components/common/RoomCarousel', () => ({
    default: () => <div data-testid="room-carousel">Room Carousel</div>
}))

describe("Checkout Component", () => {
    test("renders initial loading state and components", () => {
        render(
            <BrowserRouter>
                <Checkout />
            </BrowserRouter>
        )

        expect(screen.getByText("Loading room information...")).toBeInTheDocument()
        expect(screen.getByTestId("booking-form")).toBeInTheDocument()
        expect(screen.getByTestId("room-carousel")).toBeInTheDocument()
    })
})
