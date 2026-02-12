import { describe, test, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import '@testing-library/jest-dom'
import { BrowserRouter } from 'react-router-dom'
import Admin from '../../components/admin/Admin'

const renderWithRouter = () => {
    return render(
        <BrowserRouter>
            <Admin />
        </BrowserRouter>
    )
}

describe('Admin Component', () => {
    test('renders welcome message', () => {
        renderWithRouter()
        expect(screen.getByText('Welcome to Admin Panel')).toBeInTheDocument()
    })

    test('renders manage rooms link', () => {
        renderWithRouter()
        const roomsLink = screen.getByText('Manage Rooms')
        expect(roomsLink).toBeInTheDocument()
        expect(roomsLink.getAttribute('href')).toBe('/existing-rooms')
    })

    test('renders manage bookings link', () => {
        renderWithRouter()
        const bookingsLink = screen.getByText('Manage Bookings')
        expect(bookingsLink).toBeInTheDocument()
        expect(bookingsLink.getAttribute('href')).toBe('/existing-bookings')
    })

    test('renders within a container section', () => {
        const { container } = renderWithRouter()
        const section = container.querySelector('section')
        expect(section).toHaveClass('container', 'mt-5')
    })
})
