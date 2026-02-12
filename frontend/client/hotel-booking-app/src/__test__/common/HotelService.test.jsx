import { render, screen } from "@testing-library/react"
import { describe, test, expect } from 'vitest'
import HotelService from "../../components/common/HotelService"

describe("HotelService Component", () => {
    test("renders the main heading and subheading", () => {
        render(<HotelService />)

        expect(screen.getByText("Our Services")).toBeInTheDocument()
        expect(screen.getByText("Hotel Booking App")).toBeInTheDocument()
        expect(screen.getByText("24-Hour Front Desk")).toBeInTheDocument()
    })

    test("renders all service cards with correct titles", () => {
        render(<HotelService />)

        const services = [
            "WiFi",
            "Breakfast",
            "Laundry",
            "Mini-bar",
            "Parking",
            "Air conditioning"
        ]

        services.forEach(service => {
            expect(screen.getByText(service)).toBeInTheDocument()
        })
    })

    test("renders all service descriptions", () => {
        render(<HotelService />)

        const descriptions = [
            "Stay connected with high-speed internet access.",
            "Start your day with a delicious breakfast buffet.",
            "Keep your clothes clean and fresh with our laundry service.",
            "Enjoy a refreshing drink or snack from our in-room mini-bar.",
            "Park your car conveniently in our on-site parking lot.",
            "Stay cool and comfortable with our air conditioning system."
        ]

        descriptions.forEach(description => {
            expect(screen.getByText(description)).toBeInTheDocument()
        })
    })

    test("renders all service cards", () => {
        const { container } = render(<HotelService />)
        const cards = container.getElementsByClassName('card')
        expect(cards.length).toBe(6)
    })

    test("renders all service icons", () => {
        const { container } = render(<HotelService />)
        const icons = container.getElementsByTagName('svg')
        // 7 icons: 6 services + 1 clock icon
        expect(icons.length).toBe(7)
    })
})
