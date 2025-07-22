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

import com.example.spotify_app.model.Album.Album;
import com.example.spotify_app.model.Album.AlbumTracksResponse;

@ExtendWith(MockitoExtension.class)
class SpotifyAlbumServiceTest {

    @Mock
    private SpotifyApiClient spotifyApiClient;

    private SpotifyAlbumService spotifyAlbumService;

    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_ALBUM_ID = "test-album-456";

    @BeforeEach
    void setUp() {
        spotifyAlbumService = new SpotifyAlbumService(spotifyApiClient);
    }

    // Tests for getAlbumById method

    @Test
    void getAlbumById_ValidUserIdAndAlbumId_returnsAlbumResponse() {
        // Given
        String expectedEndpoint = "/albums/test-album-456";
        Album expectedAlbum = new Album();
        ResponseEntity<Album> mockResponseEntity = ResponseEntity.ok(expectedAlbum);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Album.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<Album> result = spotifyAlbumService.getAlbumById(TEST_USER_ID, TEST_ALBUM_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedAlbum, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Album.class);
    }

    @Test
    void getAlbumById_SpecialCharactersInAlbumId_buildsCorrectEndpoint() {
        // Given
        String specialAlbumId = "album-with-special!@#$%^&*()";
        String expectedEndpoint = "/albums/album-with-special!@#$%^&*()";
        ResponseEntity<Album> mockResponseEntity = ResponseEntity.ok(new Album());
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Album.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<Album> result = spotifyAlbumService.getAlbumById(TEST_USER_ID, specialAlbumId);

        // Then
        assertEquals(mockResponseEntity, result);
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Album.class);
    }

    @Test
    void getAlbumById_NullUserId_delegatesToApiClient() {
        // Given
        String expectedEndpoint = "/albums/test-album-456";
        ResponseEntity<Album> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        when(spotifyApiClient.makeRequest(null, expectedEndpoint, Album.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<Album> result = spotifyAlbumService.getAlbumById(null, TEST_ALBUM_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, expectedEndpoint, Album.class);
    }

    @Test
    void getAlbumById_ApiClientReturnsError_returnsErrorResponse() {
        // Given
        String expectedEndpoint = "/albums/test-album-456";
        ResponseEntity<Album> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Album.class))
                .thenReturn(errorResponse);

        // When
        ResponseEntity<Album> result = spotifyAlbumService.getAlbumById(TEST_USER_ID, TEST_ALBUM_ID);

        // Then
        assertEquals(errorResponse, result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Album.class);
    }

    // Tests for getAlbumTracks method

    @Test
    void getAlbumTracks_ValidUserIdAndAlbumId_returnsAlbumTracksResponse() {
        // Given
        String expectedEndpoint = "/albums/test-album-456/tracks";
        AlbumTracksResponse expectedResponse = new AlbumTracksResponse();
        ResponseEntity<AlbumTracksResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, AlbumTracksResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<AlbumTracksResponse> result = spotifyAlbumService.getAlbumTracks(TEST_USER_ID, TEST_ALBUM_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, AlbumTracksResponse.class);
    }

    @Test
    void getAlbumTracks_NullUserId_delegatesToApiClient() {
        // Given
        String expectedEndpoint = "/albums/test-album-456/tracks";
        ResponseEntity<AlbumTracksResponse> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        when(spotifyApiClient.makeRequest(null, expectedEndpoint, AlbumTracksResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<AlbumTracksResponse> result = spotifyAlbumService.getAlbumTracks(null, TEST_ALBUM_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, expectedEndpoint, AlbumTracksResponse.class);
    }

    @Test
    void getAlbumTracks_ApiClientReturnsError_returnsErrorResponse() {
        // Given
        String expectedEndpoint = "/albums/test-album-456/tracks";
        ResponseEntity<AlbumTracksResponse> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, AlbumTracksResponse.class))
                .thenReturn(errorResponse);

        // When
        ResponseEntity<AlbumTracksResponse> result = spotifyAlbumService.getAlbumTracks(TEST_USER_ID, TEST_ALBUM_ID);

        // Then
        assertEquals(errorResponse, result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, AlbumTracksResponse.class);
    }
}
