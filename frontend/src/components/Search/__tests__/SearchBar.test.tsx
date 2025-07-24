import { MemoryRouter } from 'react-router-dom';
import {
    render,
    screen
} from '@testing-library/react';
import {
    describe,
    it,
    expect,
    vi
} from 'vitest';

import SearchBar from '../Bar/SearchBar';

// Mock useSearch context to avoid loading all MUI icons and context logic
vi.mock('../../../context', () => ({
    useSearch: () => ({
        search: vi.fn(),
        searchResults: [],
        filters: { tracks: true, artists: true, albums: true },
        toggleFilter: vi.fn(),
    })
}));

describe('SearchBar', () => {
    it('RENDERS INPUT and BUTTON', () => {
        render(
            <MemoryRouter>
                <SearchBar />
            </MemoryRouter>
        );
        expect(screen.getByRole('textbox')).toBeInTheDocument();
        expect(screen.getByRole('button')).toBeInTheDocument();
    });
});
