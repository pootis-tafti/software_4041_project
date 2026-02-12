import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import { BrowserRouter } from 'react-router-dom'
import EditRoom from '../../components/room/EditRoom'

vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom')
    return {
        ...actual,
        useParams: () => ({ roomId: '1' })
    }
})

vi.mock('../../components/utils/ApiFunctions', () => ({
    getRoomById: vi.fn().mockResolvedValue({
        id: 1,
        roomType: 'DELUXE',
        roomPrice: 200,
        photo: 'test-photo-data'
    }),
    updateRoom: vi.fn().mockResolvedValue({ status: 200 })
}))

global.URL.createObjectURL = vi.fn(() => 'mocked-url')

describe('EditRoom', () => {
    it('handles form inputs correctly', async () => {
        render(
            <BrowserRouter>
                <EditRoom />
            </BrowserRouter>
        )

        const roomTypeInput = await screen.findByLabelText('Room Type')
        const roomPriceInput = await screen.findByLabelText('Room Price')

        fireEvent.change(roomTypeInput, { target: { name: 'roomType', value: 'SUITE' } })
        fireEvent.change(roomPriceInput, { target: { name: 'roomPrice', value: '300' } })

        expect(roomTypeInput.value).toBe('SUITE')
        expect(roomPriceInput.value).toBe('300')
    })
})
