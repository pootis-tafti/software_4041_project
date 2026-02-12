import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import '@testing-library/jest-dom'
import { BrowserRouter } from 'react-router-dom'
import Profile from '../../components/auth/Profile'
import { getUser, getBookingsByUserId, deleteUser } from '../../components/utils/ApiFunctions'
import { act } from 'react-dom/test-utils'

const mockReload = vi.fn()
Object.defineProperty(window, 'location', {
    value: { reload: mockReload }
})

const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom')
    return {
        ...actual,
        useNavigate: () => mockNavigate
    }
})

vi.mock('../../components/utils/ApiFunctions', () => ({
    getUser: vi.fn(),
    getBookingsByUserId: vi.fn(),
    deleteUser: vi.fn()
}))

describe('Profile', () => {
    beforeEach(() => {
        vi.clearAllMocks()
        localStorage.setItem('userId', '1')
        localStorage.setItem('token', 'fake-token')

        getUser.mockResolvedValue({
            id: '1',
            email: 'test@test.com',
            firstName: 'John',
            lastName: 'Doe',
            roles: [{ id: '1', name: 'ROLE_USER' }]
        })

        getBookingsByUserId.mockResolvedValue([{
            id: '1',
            room: { id: '101', roomType: 'DELUXE' },
            checkInDate: '2024-02-01',
            checkOutDate: '2024-02-05',
            bookingConfirmationCode: 'ABC123'
        }])
    })

    it('renders profile information', async () => {
        await act(async () => {
            render(
                <BrowserRouter>
                    <Profile />
                </BrowserRouter>
            )
        })

        await waitFor(() => {
            expect(screen.getByText('User Information')).toBeInTheDocument()
            expect(screen.getByText('Booking History')).toBeInTheDocument()
        })
    })

    it('handles delete account action', async () => {
        window.confirm = vi.fn(() => true)
        deleteUser.mockResolvedValue({ data: 'Success' })

        await act(async () => {
            render(
                <BrowserRouter>
                    <Profile />
                </BrowserRouter>
            )
        })

        const deleteButton = screen.getByText('Close account')
        await act(async () => {
            fireEvent.click(deleteButton)
        })

        expect(deleteUser).toHaveBeenCalled()
        expect(mockNavigate).toHaveBeenCalledWith('/')
        expect(mockReload).toHaveBeenCalled()
    })
})
