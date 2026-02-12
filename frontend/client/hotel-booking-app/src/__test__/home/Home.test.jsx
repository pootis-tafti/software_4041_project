import { describe, test, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import '@testing-library/jest-dom'
import { BrowserRouter } from 'react-router-dom'
import Home from '../../components/home/Home'

// Mock child components
vi.mock('../../components/layout/MainHeader', () => ({
    default: () => <div data-testid="main-header">Main Header</div>
}))

vi.mock('../../components/common/HotelService', () => ({
    default: () => <div data-testid="hotel-service">Hotel Service</div>
}))

vi.mock('../../components/common/Parallax', () => ({
    default: () => <div data-testid="parallax">Parallax</div>
}))

vi.mock('../../components/common/RoomCarousel', () => ({
    default: () => <div data-testid="room-carousel">Room Carousel</div>
}))

vi.mock('../../components/common/RoomSearch', () => ({
    default: () => <div data-testid="room-search">Room Search</div>
}))

// Mock router location
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom')
    return {
        ...actual,
        useLocation: () => ({
            state: { message: 'Test message' }
        })
    }
})

const renderWithRouter = (component, { userId = null } = {}) => {
    if (userId) {
        localStorage.setItem('userId', userId)
    } else {
        localStorage.clear()
    }

    return render(
        <BrowserRouter>
            {component}
        </BrowserRouter>
    )
}

describe('Home Component', () => {
    test('renders all child components', () => {
        renderWithRouter(<Home />)

        expect(screen.getByTestId('main-header')).toBeInTheDocument()
        expect(screen.getByTestId('hotel-service')).toBeInTheDocument()
        expect(screen.getAllByTestId('parallax')).toHaveLength(2)
        expect(screen.getAllByTestId('room-carousel')).toHaveLength(3)
        expect(screen.getByTestId('room-search')).toBeInTheDocument()
    })

    test('displays message from location state', () => {
        renderWithRouter(<Home />)
        expect(screen.getByText('Test message')).toBeInTheDocument()
    })

    test('shows logged-in user message when userId exists', () => {
        renderWithRouter(<Home />, { userId: 'testUser' })
        expect(screen.getByText('You are logged-In as testUser')).toBeInTheDocument()
    })

    test('does not show logged-in message when no userId exists', () => {
        renderWithRouter(<Home />)
        expect(screen.queryByText(/You are logged-In as/)).not.toBeInTheDocument()
    })
})
