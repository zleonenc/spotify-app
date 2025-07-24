package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;

import com.example.spotify_app.model.Album.Album;
import com.example.spotify_app.model.Album.AlbumTracksResponse;
import com.example.spotify_app.service.SpotifyAlbumService;
import com.example.spotify_app.util.AuthUtils;

@ExtendWith(MockitoExtension.class)
class SpotifyAlbumControllerTest {

    @Mock
    private SpotifyAlbumService albumService;

    private SpotifyAlbumController spotifyAlbumController;

    private static final String TEST_AUTH_HEADER = "Bearer test-token";
    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_ALBUM_ID = "test-album-456";

    @BeforeEach
    void setUp() {
        spotifyAlbumController = new SpotifyAlbumController(albumService);
    }

    // Tests for getAlbumById method

    @Test
    void getAlbumById_ValidAuthHeaderAndAlbumId_returnsAlbumResponse() {
        // Given
        Album expectedAlbum = new Album();
        ResponseEntity<Album> mockResponseEntity = ResponseEntity.ok(expectedAlbum);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(albumService.getAlbumById(TEST_USER_ID, TEST_ALBUM_ID)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<Album> result = spotifyAlbumController.getAlbumById(TEST_AUTH_HEADER, TEST_ALBUM_ID);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedAlbum, result.getBody());
            verify(albumService).getAlbumById(TEST_USER_ID, TEST_ALBUM_ID);
        }
    }

    @Test
    void getAlbumById_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<Album> result = spotifyAlbumController.getAlbumById(TEST_AUTH_HEADER, TEST_ALBUM_ID);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getAlbumById_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(albumService.getAlbumById(TEST_USER_ID, TEST_ALBUM_ID))
                    .thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<Album> result = spotifyAlbumController.getAlbumById(TEST_AUTH_HEADER, TEST_ALBUM_ID);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(albumService).getAlbumById(TEST_USER_ID, TEST_ALBUM_ID);
        }
    }

    @Test
    void getAlbumById_NullAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(null)).thenReturn(null);

            // When
            ResponseEntity<Album> result = spotifyAlbumController.getAlbumById(null, TEST_ALBUM_ID);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getAlbumById_ServiceReturnsError_returnsServiceError() {
        // Given
        ResponseEntity<Album> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(albumService.getAlbumById(TEST_USER_ID, TEST_ALBUM_ID)).thenReturn(errorResponse);

            // When
            ResponseEntity<Album> result = spotifyAlbumController.getAlbumById(TEST_AUTH_HEADER, TEST_ALBUM_ID);

            // Then
            assertEquals(errorResponse, result);
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            verify(albumService).getAlbumById(TEST_USER_ID, TEST_ALBUM_ID);
        }
    }

    // Tests for getTracksByAlbum method

    @Test
    void getTracksByAlbum_ValidAuthHeaderAndAlbumId_returnsAlbumTracksResponse() {
        // Given
        AlbumTracksResponse expectedResponse = new AlbumTracksResponse();
        ResponseEntity<AlbumTracksResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(albumService.getAlbumTracks(TEST_USER_ID, TEST_ALBUM_ID)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<AlbumTracksResponse> result = spotifyAlbumController.getTracksByAlbum(TEST_AUTH_HEADER,
                    TEST_ALBUM_ID);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedResponse, result.getBody());
            verify(albumService).getAlbumTracks(TEST_USER_ID, TEST_ALBUM_ID);
        }
    }

    @Test
    void getTracksByAlbum_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<AlbumTracksResponse> result = spotifyAlbumController.getTracksByAlbum(TEST_AUTH_HEADER,
                    TEST_ALBUM_ID);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getTracksByAlbum_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(albumService.getAlbumTracks(TEST_USER_ID, TEST_ALBUM_ID))
                    .thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<AlbumTracksResponse> result = spotifyAlbumController.getTracksByAlbum(TEST_AUTH_HEADER,
                    TEST_ALBUM_ID);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(albumService).getAlbumTracks(TEST_USER_ID, TEST_ALBUM_ID);
        }
    }

    @Test
    void getTracksByAlbum_ServiceReturnsError_returnsServiceError() {
        // Given
        ResponseEntity<AlbumTracksResponse> errorResponse = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(albumService.getAlbumTracks(TEST_USER_ID, TEST_ALBUM_ID)).thenReturn(errorResponse);

            // When
            ResponseEntity<AlbumTracksResponse> result = spotifyAlbumController.getTracksByAlbum(TEST_AUTH_HEADER,
                    TEST_ALBUM_ID);

            // Then
            assertEquals(errorResponse, result);
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            verify(albumService).getAlbumTracks(TEST_USER_ID, TEST_ALBUM_ID);
        }
    }

    @Test
    void getTracksByAlbum_EmptyAlbumId_stillCallsService() {
        // Given
        String emptyAlbumId = "";
        ResponseEntity<AlbumTracksResponse> mockResponseEntity = ResponseEntity.badRequest().build();

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(albumService.getAlbumTracks(TEST_USER_ID, emptyAlbumId)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<AlbumTracksResponse> result = spotifyAlbumController.getTracksByAlbum(TEST_AUTH_HEADER,
                    emptyAlbumId);

            // Then
            assertEquals(mockResponseEntity, result);
            verify(albumService).getAlbumTracks(TEST_USER_ID, emptyAlbumId);
        }
    }
}
