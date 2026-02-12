import { render, screen, waitFor } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import { BrowserRouter } from 'react-router-dom'
import RoomCarousel from "../../components/common/RoomCarousel"
import * as ApiFunctions from "../../components/utils/ApiFunctions"

const mockRooms = [
    {
        id: "1",
        roomType: "Deluxe Room",
        roomPrice: "200",
        photo: "base64EncodedString1"
    },
    {
        id: "2",
        roomType: "Suite",
        roomPrice: "300",
        photo: "base64EncodedString2"
    }
]

describe("RoomCarousel Component", () => {
    beforeEach(() => {
        vi.clearAllMocks()
    })

    test("displays rooms after successful API call", async () => {
        vi.spyOn(ApiFunctions, 'getAllRooms').mockResolvedValue(mockRooms)

        render(
            <BrowserRouter>
                <RoomCarousel />
            </BrowserRouter>
        )

        await waitFor(() => {
            expect(screen.getByText("Deluxe Room")).toBeInTheDocument()
            expect(screen.getByText("$200/night")).toBeInTheDocument()
            expect(screen.getByText("Suite")).toBeInTheDocument()
            expect(screen.getByText("$300/night")).toBeInTheDocument()
        })
    })

    test("displays error message when API call fails", async () => {
        const errorMessage = "Failed to fetch rooms"
        vi.spyOn(ApiFunctions, 'getAllRooms').mockRejectedValue(new Error(errorMessage))

        render(
            <BrowserRouter>
                <RoomCarousel />
            </BrowserRouter>
        )

        await waitFor(() => {
            expect(screen.getByText(`Error : ${errorMessage}`)).toBeInTheDocument()
        })
    })

    test("renders browse all rooms link", async () => {
        vi.spyOn(ApiFunctions, 'getAllRooms').mockResolvedValue(mockRooms)

        render(
            <BrowserRouter>
                <RoomCarousel />
            </BrowserRouter>
        )

        const browseLink = await screen.findByText(/browse all rooms/i)
        expect(browseLink).toBeInTheDocument()
        expect(browseLink).toHaveAttribute('href', '/browse-all-rooms')
    })

    test("renders correct number of book now buttons", async () => {
        vi.spyOn(ApiFunctions, 'getAllRooms').mockResolvedValue(mockRooms)

        render(
            <BrowserRouter>
                <RoomCarousel />
            </BrowserRouter>
        )

        const bookButtons = await screen.findAllByText("Book Now")
        expect(bookButtons).toHaveLength(mockRooms.length)
    })
})
