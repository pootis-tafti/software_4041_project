import { describe, test, expect, vi } from 'vitest'
import { render, screen, act, waitFor, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import Room from '../../components/room/Room'

const mockRooms = [
    { id: 1, roomType: 'SINGLE', roomPrice: 100 },
    { id: 2, roomType: 'DOUBLE', roomPrice: 200 },
    { id: 3, roomType: 'SUITE', roomPrice: 300 },
    { id: 4, roomType: 'DELUXE', roomPrice: 400 },
    { id: 5, roomType: 'SINGLE', roomPrice: 150 },
    { id: 6, roomType: 'DOUBLE', roomPrice: 250 },
    { id: 7, roomType: 'SUITE', roomPrice: 350 }
]

vi.mock('../../components/utils/ApiFunctions', () => ({
    getAllRooms: () => Promise.resolve(mockRooms)
}))

vi.mock('../../components/room/RoomCard', () => ({
    default: (props) => <div data-testid="room-card">{props.room.roomType}</div>
}))

vi.mock('../../components/common/RoomFilter', () => ({
    default: (props) => (
        <select
            data-testid="room-filter"
            onChange={(e) => {
                const filtered = e.target.value === 'all'
                    ? props.data
                    : props.data.filter(room => room.roomType === e.target.value)
                props.setFilteredData(filtered)
            }}
        >
            <option value="all">All</option>
            <option value="SINGLE">Single</option>
            <option value="DOUBLE">Double</option>
        </select>
    )
}))

describe('Room Component', () => {
    test('renders loading state initially', () => {
        render(<Room />)
        expect(screen.getByText('Loading rooms.....')).toBeInTheDocument()
    })

    test('renders rooms after loading', async () => {
        await act(async () => {
            render(<Room />)
        })

        await waitFor(() => {
            expect(screen.queryByText('Loading rooms.....')).not.toBeInTheDocument()
            expect(screen.getAllByTestId('room-card')).toHaveLength(6)
        })
    })

    test('handles pagination correctly', async () => {
        await act(async () => {
            render(<Room />)
        })

        await waitFor(() => {
            expect(screen.getAllByTestId('room-card')).toHaveLength(6)
        })

        const pageButtons = screen.getAllByText('2')
        fireEvent.click(pageButtons[0])

        await waitFor(() => {
            expect(screen.getAllByTestId('room-card')).toHaveLength(1)
        })
    })

    test('filters rooms correctly', async () => {
        await act(async () => {
            render(<Room />)
        })

        const filter = screen.getByTestId('room-filter')
        fireEvent.change(filter, { target: { value: 'SINGLE' } })

        await waitFor(() => {
            const roomCards = screen.getAllByTestId('room-card')
            expect(roomCards.every(card => card.textContent === 'SINGLE')).toBe(true)
        })
    })
})
