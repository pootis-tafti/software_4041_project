import { describe, test, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import RoomCard from '../../components/room/RoomCard';

describe('RoomCard Component', () => {
    test('renders room details', () => {
        const room = {
            id: 1,
            roomType: 'Deluxe',
            roomPrice: 100,
            photo: '',
            isBooked: false
        };

        render(
            <BrowserRouter>
                <RoomCard room={room} />
            </BrowserRouter>
        );

        expect(screen.getByText('Deluxe')).toBeInTheDocument();
    });
});
