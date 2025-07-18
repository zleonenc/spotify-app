import React from 'react';

import { Box, Typography } from '@mui/material';

import SearchCard from './SearchResultItem';

type SearchResultType = 'track' | 'artist' | 'album';

interface SearchResultSectionProps {
    title: string;
    items: any[];
    type: SearchResultType;
}

const SearchResultSection: React.FC<SearchResultSectionProps> = ({ title, items, type }) => {
    if (items.length === 0) return null;

    return (
        <Box sx={{ width: '100%', overflow: 'hidden' }}>
            <Typography variant="h5" sx={{ fontWeight: 600, mb: 1, px: 2 }}>
                {title}
            </Typography>
            <Box sx={{ width: '100%' }}>
                {items.map((item) => (
                    <SearchCard
                        key={item.id}
                        item={item}
                        type={type}
                    />
                ))}
            </Box>
        </Box>
    );
};

export default SearchResultSection;
