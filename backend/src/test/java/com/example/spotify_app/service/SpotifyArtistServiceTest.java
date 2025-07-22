package com.example.spotify_app.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.example.spotify_app.model.Artist.Artist;
import com.example.spotify_app.model.Artist.ArtistTopTracksResponse;
import com.example.spotify_app.model.Artist.ArtistAlbumsResponse;

@ExtendWith(MockitoExtension.class)
class SpotifyArtistServiceTest {

    @Mock
    private SpotifyApiClient spotifyApiClient;

    private SpotifyArtistService spotifyArtistService;

    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_ARTIST_ID = "test-artist-456";
    private static final Integer TEST_LIMIT = 20;
    private static final Integer TEST_OFFSET = 0;

    @BeforeEach
    void setUp() {
        spotifyArtistService = new SpotifyArtistService(spotifyApiClient);
    }

    // Tests for getArtistById method

    @Test
    void getArtistById_ValidUserIdAndArtistId_returnsArtistResponse() {
        // Given
        String expectedEndpoint = "/artists/test-artist-456";
        Artist expectedArtist = new Artist();
        ResponseEntity<Artist> mockResponseEntity = ResponseEntity.ok(expectedArtist);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Artist.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<Artist> result = spotifyArtistService.getArtistById(TEST_USER_ID, TEST_ARTIST_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedArtist, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Artist.class);
    }

    @Test
    void getArtistById_NullUserId_delegatesToApiClient() {
        // Given
        String expectedEndpoint = "/artists/test-artist-456";
        ResponseEntity<Artist> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        when(spotifyApiClient.makeRequest(null, expectedEndpoint, Artist.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<Artist> result = spotifyArtistService.getArtistById(null, TEST_ARTIST_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, expectedEndpoint, Artist.class);
    }

    @Test
    void getArtistById_ApiClientReturnsError_returnsErrorResponse() {
        // Given
        String expectedEndpoint = "/artists/test-artist-456";
        ResponseEntity<Artist> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Artist.class))
                .thenReturn(errorResponse);

        // When
        ResponseEntity<Artist> result = spotifyArtistService.getArtistById(TEST_USER_ID, TEST_ARTIST_ID);

        // Then
        assertEquals(errorResponse, result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Artist.class);
    }

    // Tests for getTopTracks method

    @Test
    void getTopTracks_ValidUserIdAndArtistId_returnsTopTracksResponse() {
        // Given
        String expectedEndpoint = "/artists/test-artist-456/top-tracks";
        ArtistTopTracksResponse expectedResponse = new ArtistTopTracksResponse();
        ResponseEntity<ArtistTopTracksResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, ArtistTopTracksResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<ArtistTopTracksResponse> result = spotifyArtistService.getTopTracks(TEST_USER_ID,
                TEST_ARTIST_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, ArtistTopTracksResponse.class);
    }

    @Test
    void getTopTracks_NullUserId_delegatesToApiClient() {
        // Given
        String expectedEndpoint = "/artists/test-artist-456/top-tracks";
        ResponseEntity<ArtistTopTracksResponse> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
        when(spotifyApiClient.makeRequest(null, expectedEndpoint, ArtistTopTracksResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<ArtistTopTracksResponse> result = spotifyArtistService.getTopTracks(null, TEST_ARTIST_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, expectedEndpoint, ArtistTopTracksResponse.class);
    }

    // Tests for getAlbums method

    @Test
    void getAlbums_ValidParameters_returnsArtistAlbumsResponse() {
        // Given
        String expectedEndpoint = "/artists/test-artist-456/albums?limit=20&offset=0";
        ArtistAlbumsResponse expectedResponse = new ArtistAlbumsResponse();
        ResponseEntity<ArtistAlbumsResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, ArtistAlbumsResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<ArtistAlbumsResponse> result = spotifyArtistService.getAlbums(TEST_USER_ID, TEST_ARTIST_ID,
                TEST_LIMIT, TEST_OFFSET);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, ArtistAlbumsResponse.class);
    }

    @Test
    void getAlbums_DifferentLimitAndOffset_buildsCorrectEndpoint() {
        // Given
        Integer customLimit = 50;
        Integer customOffset = 10;
        String expectedEndpoint = "/artists/test-artist-456/albums?limit=50&offset=10";
        ArtistAlbumsResponse expectedResponse = new ArtistAlbumsResponse();
        ResponseEntity<ArtistAlbumsResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, ArtistAlbumsResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<ArtistAlbumsResponse> result = spotifyArtistService.getAlbums(TEST_USER_ID, TEST_ARTIST_ID,
                customLimit, customOffset);

        // Then
        assertEquals(mockResponseEntity, result);
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, ArtistAlbumsResponse.class);
    }

    @Test
    void getAlbums_NullUserId_delegatesToApiClient() {
        // Given
        String expectedEndpoint = "/artists/test-artist-456/albums?limit=20&offset=0";
        ResponseEntity<ArtistAlbumsResponse> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
        when(spotifyApiClient.makeRequest(null, expectedEndpoint, ArtistAlbumsResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<ArtistAlbumsResponse> result = spotifyArtistService.getAlbums(null, TEST_ARTIST_ID, TEST_LIMIT,
                TEST_OFFSET);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, expectedEndpoint, ArtistAlbumsResponse.class);
    }
}
