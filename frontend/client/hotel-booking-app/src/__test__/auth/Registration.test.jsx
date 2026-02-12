import { describe, test, expect, vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import '@testing-library/jest-dom'
import { MemoryRouter } from 'react-router-dom'
import Registration from '../../components/auth/Registration'
import { registerUser } from '../../components/utils/ApiFunctions'

vi.mock('../../components/utils/ApiFunctions')

const renderRegistration = () => {
    render(
        <MemoryRouter>
            <Registration />
        </MemoryRouter>
    )
}

describe('Registration Form', () => {
    test('shows all form fields', () => {
        renderRegistration()

        expect(screen.getByLabelText(/first name/i)).toBeInTheDocument()
        expect(screen.getByLabelText(/last name/i)).toBeInTheDocument()
        expect(screen.getByLabelText(/email/i)).toBeInTheDocument()
        expect(screen.getByLabelText(/password/i)).toBeInTheDocument()
        expect(screen.getByRole('button', { name: /register/i })).toBeInTheDocument()
    })

    test('updates input values on change', () => {
        renderRegistration()

        const firstNameInput = screen.getByLabelText(/first name/i)
        const lastNameInput = screen.getByLabelText(/last name/i)
        const emailInput = screen.getByLabelText(/email/i)
        const passwordInput = screen.getByLabelText(/password/i)

        fireEvent.change(firstNameInput, { target: { value: 'John' } })
        fireEvent.change(lastNameInput, { target: { value: 'Doe' } })
        fireEvent.change(emailInput, { target: { value: 'john@example.com' } })
        fireEvent.change(passwordInput, { target: { value: 'password123' } })

        expect(firstNameInput.value).toBe('John')
        expect(lastNameInput.value).toBe('Doe')
        expect(emailInput.value).toBe('john@example.com')
        expect(passwordInput.value).toBe('password123')
    })

    test('shows success message on successful registration', async () => {
        registerUser.mockResolvedValueOnce('Registration successful')
        renderRegistration()

        fireEvent.change(screen.getByLabelText(/first name/i), { target: { value: 'John' } })
        fireEvent.change(screen.getByLabelText(/last name/i), { target: { value: 'Doe' } })
        fireEvent.change(screen.getByLabelText(/email/i), { target: { value: 'john@example.com' } })
        fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'password123' } })

        fireEvent.click(screen.getByRole('button', { name: /register/i }))

        await waitFor(() => {
            expect(screen.getByText('Registration successful')).toBeInTheDocument()
        })
    })

    test('shows error message on failed registration', async () => {
        registerUser.mockRejectedValueOnce(new Error('Registration failed'))
        renderRegistration()

        fireEvent.click(screen.getByRole('button', { name: /register/i }))

        await waitFor(() => {
            expect(screen.getByText(/Registration error/i)).toBeInTheDocument()
        })
    })

    test('clears form after successful registration', async () => {
        registerUser.mockResolvedValueOnce('Registration successful')
        renderRegistration()

        const firstNameInput = screen.getByLabelText(/first name/i)
        const lastNameInput = screen.getByLabelText(/last name/i)
        const emailInput = screen.getByLabelText(/email/i)
        const passwordInput = screen.getByLabelText(/password/i)

        fireEvent.change(firstNameInput, { target: { value: 'John' } })
        fireEvent.change(lastNameInput, { target: { value: 'Doe' } })
        fireEvent.change(emailInput, { target: { value: 'john@example.com' } })
        fireEvent.change(passwordInput, { target: { value: 'password123' } })

        fireEvent.click(screen.getByRole('button', { name: /register/i }))

        await waitFor(() => {
            expect(firstNameInput.value).toBe('')
            expect(lastNameInput.value).toBe('')
            expect(emailInput.value).toBe('')
            expect(passwordInput.value).toBe('')
        })
    })
})
