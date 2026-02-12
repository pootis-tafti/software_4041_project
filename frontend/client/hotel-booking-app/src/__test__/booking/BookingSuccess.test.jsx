import { render, screen } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import { MemoryRouter, Route, Routes } from "react-router-dom"
import BookingSuccess from "../../components/booking/BookingSuccess"

const renderWithRouter = (state) => {
    return render(
        <MemoryRouter initialEntries={[{ pathname: "/booking-success", state }]}>
            <Routes>
                <Route path="/booking-success" element={<BookingSuccess />} />
            </Routes>
        </MemoryRouter>
    )
}

describe("BookingSuccess", () => {
    test("displays success message when booking is successful", () => {
        const successMessage = "Your booking has been confirmed!"
        renderWithRouter({ message: successMessage })

        expect(screen.getByText("Booking Success!")).toBeInTheDocument()
        expect(screen.getByText(successMessage)).toBeInTheDocument()
    })

    test("displays error message when booking fails", () => {
        const errorMessage = "Something went wrong with your booking"
        renderWithRouter({ error: errorMessage })

        expect(screen.getByText("Error Booking Room!")).toBeInTheDocument()
        expect(screen.getByText(errorMessage)).toBeInTheDocument()
    })

    test("displays no booking information message when no state is provided", () => {
        renderWithRouter({})

        expect(screen.getByText("No Booking Information Available")).toBeInTheDocument()
    })
})
