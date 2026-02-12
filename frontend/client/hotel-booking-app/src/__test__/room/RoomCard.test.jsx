import { describe, test, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import '@testing-library/jest-dom'
import RoomCard from '../../components/room/RoomCard'

const mockRoom = {
    id: 1,
    roomType: 'Deluxe Suite',
    roomPrice: 200,
    photo: 'base64EncodedString'
}

const renderWithRouter = (component) => {
    return render(
        <BrowserRouter>
            {component}
        </BrowserRouter>
    )
}

describe('RoomCard Component', () => {
    test('renders room information correctly', () => {
        renderWithRouter(<RoomCard room={mockRoom} />)

        expect(screen.getByText('Deluxe Suite')).toBeInTheDocument()
        expect(screen.getByText('200 / night')).toBeInTheDocument()
        expect(screen.getByText('Book Now')).toBeInTheDocument()
        expect(screen.getByAltText('Room Photo')).toBeInTheDocument()
    })

    test('contains correct booking link', () => {
        renderWithRouter(<RoomCard room={mockRoom} />)

        const bookingLinks = screen.getAllByRole('link')
        bookingLinks.forEach(link => {
            expect(link).toHaveAttribute('href', '/book-room/1')
        })
    })

    test('displays room image with correct source', () => {
        renderWithRouter(<RoomCard room={mockRoom} />)

        const image = screen.getByAltText('Room Photo')
        expect(image).toHaveAttribute('src', 'data:image/png;base64, base64EncodedString')
    })

    test('renders room description', () => {
        renderWithRouter(<RoomCard room={mockRoom} />)

        expect(screen.getByText('Some room information goes here for the guest to read through')).toBeInTheDocument()
    })
})
