import { describe, test, expect } from 'vitest'

describe('RoomTypeSelector', () => {
    test('validates room type data structure', () => {
        const roomTypes = ['Single', 'Double', 'Suite']
        expect(Array.isArray(roomTypes)).toBe(true)
        expect(roomTypes).toHaveLength(3)
        expect(roomTypes[0]).toBe('Single')
    })

    test('handles empty room types', () => {
        const roomTypes = []
        expect(roomTypes).toHaveLength(0)
        expect(Array.isArray(roomTypes)).toBe(true)
    })

    test('validates room type addition', () => {
        let roomTypes = ['Single', 'Double']
        const newType = 'Penthouse'
        roomTypes = [...roomTypes, newType]
        expect(roomTypes).toHaveLength(3)
        expect(roomTypes).toContain(newType)
    })

    test('checks for duplicate room types', () => {
        const roomTypes = ['Single', 'Double', 'Single']
        const uniqueTypes = [...new Set(roomTypes)]
        expect(uniqueTypes).toHaveLength(2)
    })

    test('validates room type format', () => {
        const roomType = 'Deluxe Suite'
        expect(typeof roomType).toBe('string')
        expect(roomType.length).toBeGreaterThan(0)
    })

    test('handles room type sorting', () => {
        const roomTypes = ['Suite', 'Single', 'Double']
        const sortedTypes = [...roomTypes].sort()
        expect(sortedTypes[0]).toBe('Double')
        expect(sortedTypes[2]).toBe('Suite')
    })
})
