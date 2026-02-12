import { describe, test, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import PropTypes from 'prop-types'
import { AuthContext } from '../../components/auth/AuthProvider'
import Logout from '../../components/auth/Logout'

vi.mock('react-router-dom', () => ({
    useNavigate: () => vi.fn(() => {}),
    Link: ({ children, to }) => <a href={to}>{children}</a>,
    MemoryRouter: ({ children }) => <div>{children}</div>
}))

function MockLink({ children, to }) {
    return <a href={to}>{children}</a>
}

MockLink.propTypes = {
    children: PropTypes.node.isRequired,
    to: PropTypes.string.isRequired
}

function MockRouter({ children }) {
    return <div>{children}</div>
}

MockRouter.propTypes = {
    children: PropTypes.node.isRequired
}

const mockNavigate = vi.fn()
const mockHandleLogout = vi.fn()

function renderLogout() {
    return render(
        <AuthContext.Provider value={{ handleLogout: mockHandleLogout }}>
            <ul>
                <Logout />
            </ul>
        </AuthContext.Provider>
    )
}

describe('Logout Component', () => {
    beforeEach(() => {
        vi.clearAllMocks()
    })

    test('renders profile link and logout button', () => {
        renderLogout()
        expect(screen.getByText('Profile')).toBeInTheDocument()
        expect(screen.getByText('Logout')).toBeInTheDocument()
    })

    test('profile link points to correct route', () => {
        renderLogout()
        const profileLink = screen.getByText('Profile')
        expect(profileLink.closest('a')).toHaveAttribute('href', '/profile')
    })

    test('handles logout click correctly', () => {
        renderLogout()
        fireEvent.click(screen.getByText('Logout'))
        expect(mockHandleLogout).toHaveBeenCalled()
    })

    test('renders divider between profile and logout', () => {
        renderLogout()
        expect(screen.getByRole('separator')).toBeInTheDocument()
    })

    test('maintains correct DOM structure', () => {
        renderLogout()
        const listItems = screen.getAllByRole('listitem')
        expect(listItems).toHaveLength(2)
        expect(listItems[0]).toContainElement(screen.getByText('Profile'))
        expect(screen.getByRole('button')).toHaveTextContent('Logout')
    })

    test('logout button has correct class for styling', () => {
        renderLogout()
        const logoutButton = screen.getByRole('button')
        expect(logoutButton).toHaveClass('dropdown-item')
    })
})
