import { describe, it, expect, vi } from 'vitest'
import { render } from '@testing-library/react'
import '@testing-library/jest-dom'
import { MemoryRouter, Routes, Route } from 'react-router-dom'
import RequireAuth from '../../components/auth/RequireAuth'

const TestComponent = () => <div>Protected Content</div>

describe('RequireAuth', () => {
    it('renders children when user is authenticated', () => {
        localStorage.setItem('userId', '123')

        const { getByText } = render(
            <MemoryRouter initialEntries={['/protected']}>
                <Routes>
                    <Route
                        path="/protected"
                        element={
                            <RequireAuth>
                                <TestComponent />
                            </RequireAuth>
                        }
                    />
                </Routes>
            </MemoryRouter>
        )

        expect(getByText('Protected Content')).toBeInTheDocument()
    })

    it('redirects to login when user is not authenticated', () => {
        localStorage.clear()

        const { container } = render(
            <MemoryRouter initialEntries={['/protected']}>
                <Routes>
                    <Route
                        path="/protected"
                        element={
                            <RequireAuth>
                                <TestComponent />
                            </RequireAuth>
                        }
                    />
                    <Route path="/login" element={<div>Login Page</div>} />
                </Routes>
            </MemoryRouter>
        )

        expect(container.innerHTML).toContain('Login Page')
    })
})
