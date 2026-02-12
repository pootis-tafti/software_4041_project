import { render, screen } from "@testing-library/react"
import { describe, test, expect } from 'vitest'
import Parallax from "../../components/common/Parallax"

describe("Parallax Component", () => {
    test("renders main heading with hotel name", () => {
        render(<Parallax />)

        const heading = screen.getByText(/Experience the Best hospitality at/i)
        const hotelName = screen.getByText("Hotel Booking App")

        expect(heading).toBeInTheDocument()
        expect(hotelName).toBeInTheDocument()
    })

    test("renders subheading with service message", () => {
        render(<Parallax />)

        const subheading = screen.getByText("We offer the best services for all your needs.")
        expect(subheading).toBeInTheDocument()
    })

    test("contains correct CSS classes for styling", () => {
        const { container } = render(<Parallax />)

        expect(container.querySelector('.parallax')).toBeInTheDocument()
        expect(container.querySelector('.animated-texts')).toBeInTheDocument()
        expect(container.querySelector('.bounceIn')).toBeInTheDocument()
        expect(container.querySelector('.hotel-color')).toBeInTheDocument()
    })

    test("uses Bootstrap container for layout", () => {
        const { container } = render(<Parallax />)

        const bootstrapContainer = container.querySelector('.container')
        expect(bootstrapContainer).toHaveClass('text-center', 'px-5', 'py-5', 'justify-content-center')
    })
})
