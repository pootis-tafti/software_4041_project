import { describe, test, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import '@testing-library/jest-dom'
import Footer from '../../components/layout/Footer'

describe('Footer Component', () => {
    test('renders current year in copyright notice', () => {
        render(<Footer />)
        const currentYear = new Date().getFullYear()
        expect(screen.getByText(`© ${currentYear} Hotel Booking App`)).toBeInTheDocument()
    })

    test('has correct styling classes', () => {
        const { container } = render(<Footer />)
        const footer = container.querySelector('footer')
        expect(footer).toHaveClass('bg-dark', 'text-light', 'py-3', 'footer', 'mt-lg-5')
    })

    test('uses Bootstrap Container and Row components', () => {
        const { container } = render(<Footer />)
        expect(container.querySelector('.container')).toBeInTheDocument()
        expect(container.querySelector('.row')).toBeInTheDocument()
    })

    test('uses Bootstrap Column with correct classes', () => {
        const { container } = render(<Footer />)
        const column = container.querySelector('.col-12')
        expect(column).toHaveClass('col-md-12', 'text-center')
    })
})
