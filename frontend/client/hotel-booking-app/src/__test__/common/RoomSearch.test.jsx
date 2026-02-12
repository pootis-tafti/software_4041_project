import { render, screen, fireEvent } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import RoomSearch from "../../components/common/RoomSearch"
import moment from "moment"

// Mock the RoomTypeSelector component
vi.mock("../../components/common/RoomTypeSelector", () => ({
    default: () => <div data-testid="mock-room-selector">Room Type Selector</div>
}))

describe("RoomSearch Component", () => {
    test("renders search form with essential elements", () => {
        render(<RoomSearch />)

        expect(screen.getByLabelText(/check-in date/i)).toBeInTheDocument()
        expect(screen.getByLabelText(/check-out date/i)).toBeInTheDocument()
        expect(screen.getByTestId("mock-room-selector")).toBeInTheDocument()
        expect(screen.getByRole("button", { name: /search/i })).toBeInTheDocument()
    })

    test("validates date inputs correctly", () => {
        render(<RoomSearch />)

        const checkInInput = screen.getByLabelText(/check-in date/i)
        const checkOutInput = screen.getByLabelText(/check-out date/i)
        const searchButton = screen.getByRole("button", { name: /search/i })

        // Set invalid date range
        fireEvent.change(checkInInput, {
            target: {
                value: moment().add(2, 'days').format("YYYY-MM-DD"),
                name: "checkInDate"
            }
        })
        fireEvent.change(checkOutInput, {
            target: {
                value: moment().add(1, 'days').format("YYYY-MM-DD"),
                name: "checkOutDate"
            }
        })

        fireEvent.click(searchButton)
        expect(screen.getByText("Check-out date must be after check-in date")).toBeInTheDocument()
    })

    test("accepts valid date inputs", () => {
        render(<RoomSearch />)

        const checkInInput = screen.getByLabelText(/check-in date/i)
        const checkOutInput = screen.getByLabelText(/check-out date/i)

        const today = moment().format("YYYY-MM-DD")
        const tomorrow = moment().add(1, 'days').format("YYYY-MM-DD")

        fireEvent.change(checkInInput, {
            target: { value: today, name: "checkInDate" }
        })
        fireEvent.change(checkOutInput, {
            target: { value: tomorrow, name: "checkOutDate" }
        })

        expect(checkInInput.value).toBe(today)
        expect(checkOutInput.value).toBe(tomorrow)
    })
})
