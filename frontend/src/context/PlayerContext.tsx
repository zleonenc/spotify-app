import React,
{
    createContext,
    useContext,
    useState,
    type ReactNode
} from 'react';

interface PlayerContextType {
    currentTrackId: string | null;
    isPlayerVisible: boolean;
    playTrack: (trackId: string) => void;
    hidePlayer: () => void;
}

const PlayerContext = createContext<PlayerContextType | undefined>(undefined);

interface PlayerProviderProps {
    children: ReactNode;
}

export const PlayerProvider: React.FC<PlayerProviderProps> = ({ children }) => {
    const [currentTrackId, setCurrentTrackId] = useState<string | null>(null);
    const [isPlayerVisible, setIsPlayerVisible] = useState<boolean>(false);

    const playTrack = (trackId: string) => {
        setCurrentTrackId(trackId);
        setIsPlayerVisible(true);
    };

    const hidePlayer = () => {
        setIsPlayerVisible(false);
        setCurrentTrackId(null);
    };

    return (
        <PlayerContext.Provider value={{
            currentTrackId,
            isPlayerVisible,
            playTrack,
            hidePlayer
        }}>
            {children}
        </PlayerContext.Provider>
    );
};

export const usePlayer = (): PlayerContextType => {
    const context = useContext(PlayerContext);
    if (!context) {
        throw new Error('usePlayer must be used within a PlayerProvider');
    }
    return context;
};
