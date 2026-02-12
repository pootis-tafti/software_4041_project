import { describe, test, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import '@testing-library/jest-dom'
import { BrowserRouter } from 'react-router-dom'
import RoomListing from '../../components/room/RoomListing'

vi.mock('../../components/room/Room', () => ({
    default: () => <div data-testid="room-component">Room Component</div>
}))

const renderWithRouter = (component) => {
    return render(
        <BrowserRouter>
            {component}
        </BrowserRouter>
    )
}

describe('RoomListing Component', () => {
    test('renders with correct styling classes', () => {
        const { container } = renderWithRouter(<RoomListing />)
        const sectionElement = container.querySelector('section')
        expect(sectionElement).toHaveClass('bg-light', 'p-2', 'mb-5', 'mt-5', 'shadow')
    })

    test('contains Room component', () => {
        renderWithRouter(<RoomListing />)
        expect(screen.getByTestId('room-component')).toBeInTheDocument()
    })

    test('renders within a section element', () => {
        const { container } = renderWithRouter(<RoomListing />)
        const sectionElement = container.querySelector('section')
        expect(sectionElement.tagName.toLowerCase()).toBe('section')
    })
})
