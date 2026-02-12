import { describe, test, expect, vi } from 'vitest'
import { render, act } from '@testing-library/react'
import '@testing-library/jest-dom'
import { AuthProvider, useAuth } from '../../components/auth/AuthProvider'

vi.mock('jwt-decode', () => ({
    default: (token) => ({
        sub: 'user123',
        roles: 'ROLE_USER'
    })
}))

describe('AuthProvider', () => {
    beforeEach(() => {
        localStorage.clear()
    })

    const TestComponent = () => {
        const { user, handleLogin, handleLogout } = useAuth()
        return (
            <div>
                <div data-testid="user-info">{JSON.stringify(user)}</div>
                <button data-testid="login" onClick={() => handleLogin('test-token')}>Login</button>
                <button data-testid="logout" onClick={handleLogout}>Logout</button>
            </div>
        )
    }

    test('provides authentication context to children', () => {
        const { getByTestId } = render(
            <AuthProvider>
                <TestComponent />
            </AuthProvider>
        )
        expect(getByTestId('user-info')).toHaveTextContent('null')
    })

    test('handles login with JWT token', () => {
        const { getByTestId } = render(
            <AuthProvider>
                <TestComponent />
            </AuthProvider>
        )

        act(() => {
            getByTestId('login').click()
        })

        expect(localStorage.getItem('token')).toBe('test-token')
        expect(localStorage.getItem('userId')).toBe('user123')
        expect(localStorage.getItem('userRole')).toBe('ROLE_USER')
    })

    test('handles logout and clears storage', () => {
        const { getByTestId } = render(
            <AuthProvider>
                <TestComponent />
            </AuthProvider>
        )

        act(() => {
            getByTestId('login').click()
            getByTestId('logout').click()
        })

        expect(localStorage.getItem('token')).toBeNull()
        expect(localStorage.getItem('userId')).toBeNull()
        expect(localStorage.getItem('userRole')).toBeNull()
    })
})
