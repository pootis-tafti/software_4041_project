import { describe, test, expect, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import { BrowserRouter } from 'react-router-dom'
import NavBar from '../../components/layout/NavBar'

const renderWithRouter = () => {
    return render(
        <BrowserRouter>
            <NavBar />
        </BrowserRouter>
    )
}

describe('NavBar Component', () => {
    beforeEach(() => {
        localStorage.clear()
    })

    test('renders brand name', () => {
        renderWithRouter()
        expect(screen.getByText('Hotel Booking App')).toHaveClass('hotel-color')
    })

    test('displays browse rooms link', () => {
        renderWithRouter()
        expect(screen.getByText('Browse all rooms')).toHaveAttribute('href', '/browse-all-rooms')
    })

    test('shows admin link when user is admin', () => {
        localStorage.setItem('token', 'fake-token')
        localStorage.setItem('userRole', 'ROLE_ADMIN')
        renderWithRouter()
        expect(screen.getByText('Admin')).toBeInTheDocument()
    })

    test('hides admin link for non-admin users', () => {
        localStorage.setItem('token', 'fake-token')
        localStorage.setItem('userRole', 'ROLE_USER')
        renderWithRouter()
        expect(screen.queryByText('Admin')).not.toBeInTheDocument()
    })

    test('shows find booking link', () => {
        renderWithRouter()
        expect(screen.getByText('Find my booking')).toHaveAttribute('href', '/find-booking')
    })

    test('toggles account dropdown on click', () => {
        renderWithRouter()
        const accountButton = screen.getByText('Account')
        fireEvent.click(accountButton)
        expect(accountButton).toHaveClass('show')
    })

    test('shows login link when user is not logged in', () => {
        renderWithRouter()
        const accountButton = screen.getByText('Account')
        fireEvent.click(accountButton)
        expect(screen.getByText('Login')).toHaveAttribute('href', '/login')
    })

    test('shows logout when user is logged in', () => {
        localStorage.setItem('token', 'fake-token')
        renderWithRouter()
        const accountButton = screen.getByText('Account')
        fireEvent.click(accountButton)
        expect(screen.getByText('Logout')).toBeInTheDocument()
    })
})
