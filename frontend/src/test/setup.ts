import '@testing-library/jest-dom';
import {
    vi,
    beforeEach
} from 'vitest';

// Mock localStorage
Object.defineProperty(window, 'localStorage', {
    value: {
        getItem: vi.fn(),
        setItem: vi.fn(),
        removeItem: vi.fn(),
        clear: vi.fn(),
    },
    writable: true,
});

beforeEach(() => {
    vi.clearAllMocks();
});
