import { MemoryRouter } from 'react-router-dom';
import {
    render,
    screen,
    fireEvent,
    waitFor
} from '@testing-library/react';
import {
    describe,
    it,
    expect,
    vi
} from 'vitest';

import SearchBar from '../Bar/SearchBar';

const mockSearch = vi.fn();
const mockToggleFilter = vi.fn();

vi.mock('../../../context', () => ({
    useSearch: () => ({
        search: mockSearch,
        searchResults: {
            tracks: { items: [{ id: '1', name: 'Track 1' }] },
            artists: { items: [{ id: '2', name: 'Artist 1' }] },
            albums: { items: [{ id: '3', name: 'Album 1' }] }
        },
        filters: { tracks: true, artists: true, albums: true },
        toggleFilter: mockToggleFilter,
    })
}));

describe('SearchBar', () => {
    it('renders INPUT and at LEAST ONE BUTTOM', () => {
        render(
            <MemoryRouter>
                <SearchBar />
            </MemoryRouter>
        );
        expect(screen.getByRole('textbox')).toBeInTheDocument();
        expect(screen.getAllByRole('button').length).toBeGreaterThan(0);
    });

    it('UPDATES input value ON CHANGE', () => {
        render(
            <MemoryRouter>
                <SearchBar />
            </MemoryRouter>
        );
        const input = screen.getByRole('textbox');
        fireEvent.change(input, { target: { value: 'test' } });
        expect(input).toHaveValue('test');
    });

    it('CALLS SEARCH on ENTER key', async () => {
        render(
            <MemoryRouter>
                <SearchBar />
            </MemoryRouter>
        );
        const input = screen.getByRole('textbox');
        fireEvent.change(input, { target: { value: 'hello' } });
        fireEvent.keyDown(input, { key: 'Enter', code: 'Enter' });
        await waitFor(() => {
            expect(mockSearch).toHaveBeenCalled();
        });
    });
});
