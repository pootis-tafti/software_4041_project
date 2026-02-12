import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import { BrowserRouter } from 'react-router-dom'
import AddRoom from '../../components/room/AddRoom'

global.URL.createObjectURL = vi.fn(() => 'mocked-url')

vi.mock('../../components/utils/ApiFunctions', () => ({
    addRoom: vi.fn(),
    getRoomTypes: vi.fn().mockResolvedValue(['SINGLE', 'DOUBLE', 'SUITE'])
}))

describe('AddRoom', () => {
    beforeEach(() => {
        vi.clearAllMocks()
    })

    it('renders form elements and handles input changes', () => {
        render(
            <BrowserRouter>
                <AddRoom />
            </BrowserRouter>
        )

        // Check if main elements are rendered
        expect(screen.getByText('Add a New Room')).toBeInTheDocument()
        expect(screen.getByLabelText('Room Price')).toBeInTheDocument()
        expect(screen.getByLabelText('Room Photo')).toBeInTheDocument()
        expect(screen.getByText('Save Room')).toBeInTheDocument()

        // Test price input
        const priceInput = screen.getByLabelText('Room Price')
        fireEvent.change(priceInput, { target: { name: 'roomPrice', value: '100' } })
        expect(priceInput.value).toBe('100')

        // Test file input
        const file = new File(['test'], 'test.png', { type: 'image/png' })
        const fileInput = screen.getByLabelText('Room Photo')
        fireEvent.change(fileInput, { target: { files: [file] } })
        expect(fileInput.files[0]).toBe(file)
    })
})
