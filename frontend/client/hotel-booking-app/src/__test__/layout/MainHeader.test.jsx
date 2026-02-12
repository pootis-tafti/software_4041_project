import { describe, test, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import '@testing-library/jest-dom'
import MainHeader from '../../components/layout/MainHeader'

describe('MainHeader Component', () => {
    test('renders welcome message with styled hotel name', () => {
        render(<MainHeader />)
        const hotelName = screen.getByText('Hotel Booking App')
        expect(hotelName).toHaveClass('hotel-color')
    })

    test('displays hospitality tagline', () => {
        render(<MainHeader />)
        expect(screen.getByText('Experience the Best Hospitality in Town')).toBeInTheDocument()
    })

    test('contains header with correct classes', () => {
        const { container } = render(<MainHeader />)
        const header = container.querySelector('header')
        expect(header).toHaveClass('header-banner')
    })

    test('includes overlay div', () => {
        const { container } = render(<MainHeader />)
        const overlay = container.querySelector('.overlay')
        expect(overlay).toBeInTheDocument()
    })

    test('contains animated texts section', () => {
        const { container } = render(<MainHeader />)
        const animatedTexts = container.querySelector('.animated-texts')
        expect(animatedTexts).toHaveClass('overlay-content')
    })
})
