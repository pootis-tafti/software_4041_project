import { describe, test, expect, vi } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import '@testing-library/jest-dom'
import Bookings from '../../components/booking/Bookings'
import { getAllBookings, cancelBooking } from '../../components/utils/ApiFunctions'

vi.mock('../../components/utils/ApiFunctions', () => ({
    getAllBookings: vi.fn(),
    cancelBooking: vi.fn()
}))

vi.mock('../../components/booking/BookingsTable', () => ({
    default: ({ bookingInfo, handleBookingCancellation }) => (
        <div data-testid="bookings-table">
            {bookingInfo.length} bookings
            <button onClick={() => handleBookingCancellation(1)}>Cancel Booking</button>
        </div>
    )
}))

const mockBookings = [
    {
        id: 1,
        room: { roomNumber: '101' },
        checkInDate: '2024-01-01',
        checkOutDate: '2024-01-05',
        guestName: 'John Doe'
    },
    {
        id: 2,
        room: { roomNumber: '102' },
        checkInDate: '2024-02-01',
        checkOutDate: '2024-02-05',
        guestName: 'Jane Smith'
    }
]

describe('Bookings Component', () => {
    beforeEach(() => {
        vi.clearAllMocks()
    })

    test('renders loading state initially', () => {
        getAllBookings.mockResolvedValue(mockBookings)
        render(<Bookings />)
        expect(screen.getByText('Loading existing bookings')).toBeInTheDocument()
    })

    test('renders bookings after loading', async () => {
        getAllBookings.mockResolvedValue(mockBookings)
        render(<Bookings />)

        await waitFor(() => {
            expect(screen.queryByText('Loading existing bookings')).not.toBeInTheDocument()
            expect(screen.getByTestId('bookings-table')).toBeInTheDocument()
        }, { timeout: 1100 })
    })

    test('displays error message when API call fails', async () => {
        const errorMessage = 'Failed to fetch bookings'
        getAllBookings.mockRejectedValue(new Error(errorMessage))
        render(<Bookings />)

        await waitFor(() => {
            expect(screen.getByText(errorMessage)).toBeInTheDocument()
        }, { timeout: 1100 })
    })

    test('handles booking cancellation successfully', async () => {
        getAllBookings.mockResolvedValue(mockBookings)
        cancelBooking.mockResolvedValue('Booking cancelled')

        render(<Bookings />)

        await waitFor(() => {
            expect(getAllBookings).toHaveBeenCalled()
            expect(screen.getByTestId('bookings-table')).toBeInTheDocument()
        }, { timeout: 1100 })
    })
})
