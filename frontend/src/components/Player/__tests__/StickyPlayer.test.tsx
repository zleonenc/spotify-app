import {
    render,
    screen,
    fireEvent
} from '@testing-library/react';
import {
    vi,
    describe,
    it, expect
} from 'vitest';

import { PlayerProvider } from '../../../context';
import * as PlayerContextModule from '../../../context';

import StickyPlayer from '../StickyPlayer';

const renderWithMockedPlayer = (contextOverrides = {}) => {
    vi.spyOn(PlayerContextModule, 'usePlayer').mockReturnValue({
        currentTrackId: 'track123',
        isPlayerVisible: true,
        hidePlayer: vi.fn(),
        playTrack: vi.fn(),
        ...contextOverrides,
    });
    return render(
        <PlayerProvider>
            <StickyPlayer />
        </PlayerProvider>
    );
};

describe('StickyPlayer', () => {
    it('RENDERS IFRAME with correct src when visible', () => {
        renderWithMockedPlayer();
        const iframe = screen.getByTitle('Spotify Player for track track123');
        expect(iframe).toBeInTheDocument();
        expect(iframe).toHaveAttribute('src', expect.stringContaining('track123'));
    });

    it('does NOT RENDER when NOT VISIBLE', () => {
        renderWithMockedPlayer({ isPlayerVisible: false });
        expect(screen.queryByTitle(/Spotify Player/)).toBeNull();
    });

    it('CALLS hidePlayer WHEN CLOSEBUTTON is CLICKED', () => {
        const hidePlayer = vi.fn();
        renderWithMockedPlayer({ hidePlayer });
        fireEvent.click(screen.getByLabelText('Close player'));
        expect(hidePlayer).toHaveBeenCalled();
    });
});
