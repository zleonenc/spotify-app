import {
    describe,
    it,
    expect,
    vi,
    beforeEach,
    afterEach
} from 'vitest';
import {
    render,
    screen,
    act,
    waitFor
} from '@testing-library/react';

import { authService } from '../../services';

import {
    AuthProvider,
    useAuth
} from '../AuthContext';

// Mock the authService
vi.mock('../../services', () => ({
    authService: {
        logout: vi.fn(),
    },
}));

// LocalStorage mock
const localStorageMock = (() => {
    let store: Record<string, string> = {};
    return {
        getItem: (key: string) => store[key] || null,
        setItem: (key: string, value: string) => {
            store[key] = value;
        },
        removeItem: (key: string) => {
            delete store[key];
        },
        clear: () => {
            store = {};
        },
    };
})();

const mockOpen = vi.fn();

Object.defineProperty(window, 'localStorage', {
    value: localStorageMock,
    writable: true,
});

Object.defineProperty(window, 'open', {
    value: mockOpen,
    writable: true,
});

Object.defineProperty(window, 'location', {
    value: { href: '' },
    configurable: true,
});

// Test component to use the context
const TestComponent = () => {
    const { userId, setUserId, isAuthenticated, logout } = useAuth();

    return (
        <div>
            <div data-testid="userId">{userId || 'null'}</div>
            <div data-testid="isAuthenticated">{isAuthenticated.toString()}</div>
            <button data-testid="setUserId" onClick={() => setUserId('test-user-123')}>
                Set User ID
            </button>
            <button data-testid="clearUserId" onClick={() => setUserId(null)}>
                Clear User ID
            </button>
            <button data-testid="logout" onClick={logout}>
                Logout
            </button>
        </div>
    );
};

const renderWithAuthProvider = () => {
    return render(
        <AuthProvider>
            <TestComponent />
        </AuthProvider>
    );
};

describe('AuthContext', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        localStorageMock.clear();
        window.location.href = '';
    });

    afterEach(() => {
        localStorageMock.clear();
    });

    describe('AuthProvider initialization', () => {
        it('AuthProvider INITIALIZES with NULL if no userId in storage', () => {
            // Given
            localStorageMock.clear();

            // When
            renderWithAuthProvider();

            // Then
            expect(screen.getByTestId('userId')).toHaveTextContent('null');
            expect(screen.getByTestId('isAuthenticated')).toHaveTextContent('false');
        });

        it('AuthProvider INITIALIZES with STORED userId if present', () => {
            // Given
            localStorageMock.setItem('userId', 'stored-user-123');

            // When
            renderWithAuthProvider();

            // Then
            expect(screen.getByTestId('userId')).toHaveTextContent('stored-user-123');
            expect(screen.getByTestId('isAuthenticated')).toHaveTextContent('true');
        });
    });

    describe('setUserId', () => {
        it('setUserId SETS userId and UPDATES state and storage', async () => {
            // Given
            renderWithAuthProvider();

            // When
            await act(async () => {
                screen.getByTestId('setUserId').click();
            });

            // Then
            expect(screen.getByTestId('userId')).toHaveTextContent('test-user-123');
            expect(screen.getByTestId('isAuthenticated')).toHaveTextContent('true');
            expect(localStorageMock.getItem('userId')).toBe('test-user-123');
        });

        it('setUserId CLEARS userId and storage if null', async () => {
            // Given
            localStorageMock.setItem('userId', 'existing-user');
            renderWithAuthProvider();

            // When
            await act(async () => {
                screen.getByTestId('clearUserId').click();
            });

            // Then
            expect(screen.getByTestId('userId')).toHaveTextContent('null');
            expect(screen.getByTestId('isAuthenticated')).toHaveTextContent('false');
            expect(localStorageMock.getItem('userId')).toBeNull();
        });
    });

    describe('isAuthenticated', () => {
        it('isAuthenticated RETURNS true if userId exists', () => {
            // Given
            localStorageMock.setItem('userId', 'user123');

            // When
            renderWithAuthProvider();

            // Then
            expect(screen.getByTestId('isAuthenticated')).toHaveTextContent('true');
        });

        it('isAuthenticated RETURNS false if no userId', () => {
            // Given
            localStorageMock.clear();

            // When
            renderWithAuthProvider();

            // Then
            expect(screen.getByTestId('isAuthenticated')).toHaveTextContent('false');
        });
    });

    describe('logout', () => {
        it('logout CALLS authService and CLEARS state if userId exists', async () => {
            // Given
            localStorageMock.setItem('userId', 'user123');
            const mockLogout = vi.mocked(authService.logout);
            mockLogout.mockResolvedValue();
            renderWithAuthProvider();

            // When
            await act(async () => {
                screen.getByTestId('logout').click();
            });

            // Then
            await waitFor(() => {
                expect(mockLogout).toHaveBeenCalledTimes(1);
            });
            expect(screen.getByTestId('userId')).toHaveTextContent('null');
            expect(screen.getByTestId('isAuthenticated')).toHaveTextContent('false');
            expect(localStorageMock.getItem('userId')).toBeNull();
        });


        it('logout OPENS Spotify logout and REDIRECTS on success', async () => {
            // Given
            localStorageMock.setItem('userId', 'user123');
            const mockLogout = vi.mocked(authService.logout);
            mockLogout.mockResolvedValue();

            const mockLocationSetter = vi.fn();
            Object.defineProperty(window, 'location', {
                value: {
                    get href() {
                        return '';
                    },
                    set href(url) {
                        mockLocationSetter(url);
                    }
                },
                configurable: true,
            });

            renderWithAuthProvider();

            // When
            await act(async () => {
                screen.getByTestId('logout').click();
            });

            // Then
            await waitFor(() => {
                expect(mockOpen).toHaveBeenCalledWith('https://accounts.spotify.com/logout', '_blank');
                expect(mockLocationSetter).toHaveBeenCalledWith('/login?logged_out=true');
            });
        });
    });
});
