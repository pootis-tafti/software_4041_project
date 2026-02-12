import React from 'react'
import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import { RouterProvider, createMemoryRouter } from 'react-router-dom'
import { AuthProvider } from '../../components/auth/AuthProvider'
import Login from '../../components/auth/Login'
import { describe, test, expect } from 'vitest'

const routes = [
    {
        path: '/',
        element: <AuthProvider><Login /></AuthProvider>
    }
]

const router = createMemoryRouter(routes, {
    initialEntries: ['/'],
    initialIndex: 0
})

describe('Login Component', () => {
    test('renders form fields', () => {
        render(<RouterProvider router={router} />)

        expect(screen.getByLabelText(/email/i)).toBeInTheDocument()
        expect(screen.getByLabelText(/password/i)).toBeInTheDocument()
        expect(screen.getByRole('button', { name: /login/i })).toBeInTheDocument()
    })

    test('allows input', () => {
        render(<RouterProvider router={router} />)

        const emailInput = screen.getByLabelText(/email/i)
        fireEvent.change(emailInput, { target: { value: 'test@example.com' } })
        expect(emailInput.value).toBe('test@example.com')
    })
})
