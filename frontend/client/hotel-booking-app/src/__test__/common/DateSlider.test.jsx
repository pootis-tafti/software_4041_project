import { render, screen, fireEvent } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import DateSlider from "../../components/common/DateSlider"

describe("DateSlider Component", () => {
    const mockOnDateChange = vi.fn()
    const mockOnFilterChange = vi.fn()

    const defaultProps = {
        onDateChange: mockOnDateChange,
        onFilterChange: mockOnFilterChange
    }

    test("renders DateSlider with essential elements", () => {
        render(<DateSlider {...defaultProps} />)

        expect(screen.getByText("Filter bookings by date")).toBeInTheDocument()
        expect(screen.getByText("Today")).toBeInTheDocument()
        expect(screen.getByText("This Month")).toBeInTheDocument()
        expect(screen.getByRole('button', { name: "Clear Filter" })).toBeInTheDocument()
    })

    test("handles clear filter button click", () => {
        render(<DateSlider {...defaultProps} />)

        const clearButton = screen.getByRole('button', { name: "Clear Filter" })
        fireEvent.click(clearButton)

        expect(mockOnDateChange).toHaveBeenCalledWith(null, null)
        expect(mockOnFilterChange).toHaveBeenCalledWith(null, null)
    })

    test("renders date range inputs", () => {
        render(<DateSlider {...defaultProps} />)

        const inputs = screen.getAllByRole('textbox')
        expect(inputs.length).toBeGreaterThan(0)
    })

    test("renders predefined date range options", () => {
        render(<DateSlider {...defaultProps} />)

        const predefinedRanges = ["Today", "Yesterday", "This Week", "Last Week", "This Month", "Last Month"]
        predefinedRanges.forEach(range => {
            expect(screen.getByText(range)).toBeInTheDocument()
        })
    })
})
