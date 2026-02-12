import { render, screen, fireEvent } from "@testing-library/react"
import { describe, test, expect, vi } from 'vitest'
import RoomPaginator from "../../components/common/RoomPaginator"

describe("RoomPaginator Component", () => {
    test("renders correct number of page buttons", () => {
        const mockOnPageChange = vi.fn()
        render(
            <RoomPaginator
                currentPage={1}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        const pageButtons = screen.getAllByRole("button")
        expect(pageButtons).toHaveLength(5)
    })

    test("marks current page as active", () => {
        const mockOnPageChange = vi.fn()
        const currentPage = 2
        render(
            <RoomPaginator
                currentPage={currentPage}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        const activePageItem = screen.getByText(currentPage).closest('li')
        expect(activePageItem).toHaveClass('active')
    })

    test("calls onPageChange with correct page number when clicked", () => {
        const mockOnPageChange = vi.fn()
        render(
            <RoomPaginator
                currentPage={1}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        const pageButton = screen.getByText("3")
        fireEvent.click(pageButton)
        expect(mockOnPageChange).toHaveBeenCalledWith(3)
    })

    test("renders navigation with correct aria-label", () => {
        const mockOnPageChange = vi.fn()
        render(
            <RoomPaginator
                currentPage={1}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        const nav = screen.getByRole("navigation")
        expect(nav).toHaveAttribute("aria-label", "Page navigation")
    })

    test("renders pagination with correct styling classes", () => {
        const mockOnPageChange = vi.fn()
        render(
            <RoomPaginator
                currentPage={1}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        const pagination = screen.getByRole("list")
        expect(pagination).toHaveClass("pagination", "justify-content-center")
    })
})
