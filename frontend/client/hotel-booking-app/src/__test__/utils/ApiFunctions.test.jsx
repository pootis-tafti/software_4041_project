import { describe, test, expect, vi, beforeEach } from 'vitest'
import { getHeader } from '../../components/utils/ApiFunctions'

// Keep the mock simple and focused
vi.mock('axios', () => ({
    default: {
        create: () => ({
            get: vi.fn(),
            post: vi.fn(),
            defaults: { baseURL: 'http://localhost:9192' }
        })
    }
}))

describe('API Functions', () => {
    beforeEach(() => {
        localStorage.clear()
    })

    describe('Header Tests', () => {
        test('getHeader returns empty object without token', () => {
            const headers = getHeader()
            expect(headers).toEqual({})
        })

        test('getHeader returns auth headers with token', () => {
            localStorage.setItem('token', 'test-token')
            const headers = getHeader()
            expect(headers).toEqual({
                Authorization: 'Bearer test-token',
                'Content-Type': 'application/json'
            })
        })
    })
})
