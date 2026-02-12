import { describe, test, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import '@testing-library/jest-dom'
import Header from '../../components/common/Header'

describe('Header Component', () => {
    test('renders with provided title', () => {
        render(<Header title="Welcome to Hotel" />)
        expect(screen.getByText('Welcome to Hotel')).toBeInTheDocument()
    })

    test('has correct CSS classes', () => {
        render(<Header title="Test Title" />)
        expect(screen.getByRole('banner')).toHaveClass('header')
        expect(screen.getByText('Test Title')).toHaveClass('header-title', 'text-center')
    })

    test('contains overlay div', () => {
        render(<Header title="Test" />)
        expect(screen.getByRole('banner').querySelector('.overlay')).toBeInTheDocument()
    })

    test('renders title in container div', () => {
        render(<Header title="Test" />)
        const container = screen.getByRole('banner').querySelector('.container')
        expect(container).toBeInTheDocument()
        expect(container).toContainElement(screen.getByText('Test'))
    })
})
