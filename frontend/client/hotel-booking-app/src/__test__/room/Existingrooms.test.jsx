import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import '@testing-library/jest-dom'
import { BrowserRouter } from 'react-router-dom'
import ExistingRooms from '../../components/room/ExistingRooms'
import { getAllRooms, deleteRoom } from '../../components/utils/ApiFunctions'

vi.mock('../../components/utils/ApiFunctions', () => ({
    getAllRooms: vi.fn(),
    deleteRoom: vi.fn()
}))

const mockRooms = [
    { id: 1, roomType: 'SINGLE', roomPrice: 100 },
    { id: 2, roomType: 'DOUBLE', roomPrice: 200 },
    { id: 3, roomType: 'SUITE', roomPrice: 300 }
]

describe('ExistingRooms', () => {
    it('renders room list and handles pagination', async () => {
        getAllRooms.mockResolvedValue(mockRooms)

        render(
            <BrowserRouter>
                <ExistingRooms />
            </BrowserRouter>
        )

        await waitFor(() => {
            const tableRows = screen.getAllByRole('row')
            expect(tableRows.length).toBe(mockRooms.length + 1) // +1 for header row
            expect(screen.getByRole('heading', { name: 'Existing Rooms' })).toBeInTheDocument()
            expect(screen.getAllByText('SINGLE')).toHaveLength(2) // One in table, one in filter
        })
    })

    it('handles room deletion', async () => {
        getAllRooms.mockResolvedValue(mockRooms)
        deleteRoom.mockResolvedValue('')

        render(
            <BrowserRouter>
                <ExistingRooms />
            </BrowserRouter>
        )

        await waitFor(() => {
            const deleteButtons = screen.getAllByRole('button')
            fireEvent.click(deleteButtons[1]) // Click first delete button
            expect(deleteRoom).toHaveBeenCalled()
        })
    })
})
