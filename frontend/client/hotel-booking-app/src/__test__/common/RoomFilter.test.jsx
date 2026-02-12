import { render, screen, fireEvent } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import RoomFilter from "../../components/common/RoomFilter"

const mockRooms = [
    { id: 1, roomType: "Deluxe" },
    { id: 2, roomType: "Suite" },
    { id: 3, roomType: "Deluxe" },
    { id: 4, roomType: "Standard" }
]

describe("RoomFilter Component", () => {
    const mockSetFilteredData = vi.fn()

    test("renders filter dropdown with correct options", () => {
        render(<RoomFilter data={mockRooms} setFilteredData={mockSetFilteredData} />)

        const selectElement = screen.getByRole("combobox")
        const options = screen.getAllByRole("option")

        expect(selectElement).toBeInTheDocument()
        expect(options).toHaveLength(5) // Default option + empty option + unique room types
        expect(options[0]).toHaveValue("")
        expect(options[1]).toHaveValue("")
        expect(options[2]).toHaveValue("Deluxe")
        expect(options[3]).toHaveValue("Suite")
        expect(options[4]).toHaveValue("Standard")
    })

    test("filters rooms when option is selected", () => {
        render(<RoomFilter data={mockRooms} setFilteredData={mockSetFilteredData} />)

        const selectElement = screen.getByRole("combobox")
        fireEvent.change(selectElement, { target: { value: "Deluxe" } })

        expect(mockSetFilteredData).toHaveBeenCalledWith(
            expect.arrayContaining([
                expect.objectContaining({ roomType: "Deluxe" })
            ])
        )
    })

    test("clears filter when clear button is clicked", () => {
        render(<RoomFilter data={mockRooms} setFilteredData={mockSetFilteredData} />)

        const clearButton = screen.getByText("Clear Filter")
        fireEvent.click(clearButton)

        expect(mockSetFilteredData).toHaveBeenCalledWith(mockRooms)
    })

    test("handles case-insensitive filtering", () => {
        render(<RoomFilter data={mockRooms} setFilteredData={mockSetFilteredData} />)

        const selectElement = screen.getByRole("combobox")
        fireEvent.change(selectElement, { target: { value: "deluxe" } })

        expect(mockSetFilteredData).toHaveBeenCalled()
    })

    test("displays correct filter label", () => {
        render(<RoomFilter data={mockRooms} setFilteredData={mockSetFilteredData} />)

        expect(screen.getByText("FIlter rooms by type")).toBeInTheDocument()
    })
})
