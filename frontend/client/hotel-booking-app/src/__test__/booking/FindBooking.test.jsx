import { render, screen, fireEvent, act } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import FindBooking from "../../components/booking/FindBooking"

vi.mock("../../components/utils/ApiFunctions")

describe("FindBooking Component", () => {
    beforeEach(() => {
        vi.clearAllMocks()
    })

    test("renders initial form", () => {
        render(<FindBooking />)

        const heading = screen.getByRole('heading', { name: "Find My Booking" })
        const input = screen.getByRole('textbox')
        const searchButton = screen.getByRole('button')

        expect(heading).toBeInTheDocument()
        expect(input).toBeInTheDocument()
        expect(searchButton).toBeInTheDocument()
    })

    test("shows loading state", async () => {
        render(<FindBooking />)

        const input = screen.getByRole('textbox')
        const searchButton = screen.getByRole('button')

        await act(async () => {
            fireEvent.change(input, { target: { value: "TEST123" } })
            fireEvent.click(searchButton)
        })

        expect(screen.getByText("Finding your booking...")).toBeInTheDocument()
    })

    test("handles form input change", async () => {
        render(<FindBooking />)

        const input = screen.getByRole('textbox')

        await act(async () => {
            fireEvent.change(input, { target: { value: "TEST123" } })
        })

        expect(input.value).toBe("TEST123")
    })

    test("shows initial find booking message", () => {
        render(<FindBooking />)
        expect(screen.getByText("find booking...")).toBeInTheDocument()
    })

    test("handles form submission", async () => {
        render(<FindBooking />)

        const input = screen.getByRole('textbox')
        const submitButton = screen.getByRole('button')

        await act(async () => {
            fireEvent.change(input, { target: { value: "TEST123" } })
            fireEvent.click(submitButton)
        })

        expect(screen.getByText("Finding your booking...")).toBeInTheDocument()
    })
})
