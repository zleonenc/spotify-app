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

import com.example.spotify_app.model.Artist.Artist;
import com.example.spotify_app.model.Artist.ArtistTopTracksResponse;
import com.example.spotify_app.model.Artist.ArtistAlbumsResponse;
import com.example.spotify_app.service.SpotifyArtistService;
import com.example.spotify_app.util.AuthUtils;

@ExtendWith(MockitoExtension.class)
class SpotifyArtistControllerTest {

    @Mock
    private SpotifyArtistService artistService;

    private SpotifyArtistController spotifyArtistController;

    private static final String TEST_AUTH_HEADER = "Bearer test-token";
    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_ARTIST_ID = "test-artist-456";
    private static final Integer TEST_LIMIT = 20;
    private static final Integer TEST_OFFSET = 0;

    @BeforeEach
    void setUp() {
        spotifyArtistController = new SpotifyArtistController(artistService);
    }

    // Tests for getArtistById method

    @Test
    void getArtistById_ValidAuthHeaderAndArtistId_returnsArtistResponse() {
        // Given
        Artist expectedArtist = new Artist();
        ResponseEntity<Artist> mockResponseEntity = ResponseEntity.ok(expectedArtist);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getArtistById(TEST_USER_ID, TEST_ARTIST_ID)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<Artist> result = spotifyArtistController.getArtistById(TEST_AUTH_HEADER, TEST_ARTIST_ID);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedArtist, result.getBody());
            verify(artistService).getArtistById(TEST_USER_ID, TEST_ARTIST_ID);
        }
    }

    @Test
    void getArtistById_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<Artist> result = spotifyArtistController.getArtistById(TEST_AUTH_HEADER, TEST_ARTIST_ID);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getArtistById_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getArtistById(TEST_USER_ID, TEST_ARTIST_ID))
                    .thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<Artist> result = spotifyArtistController.getArtistById(TEST_AUTH_HEADER, TEST_ARTIST_ID);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(artistService).getArtistById(TEST_USER_ID, TEST_ARTIST_ID);
        }
    }

    @Test
    void getArtistById_ServiceReturnsError_returnsServiceError() {
        // Given
        ResponseEntity<Artist> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getArtistById(TEST_USER_ID, TEST_ARTIST_ID)).thenReturn(errorResponse);

            // When
            ResponseEntity<Artist> result = spotifyArtistController.getArtistById(TEST_AUTH_HEADER, TEST_ARTIST_ID);

            // Then
            assertEquals(errorResponse, result);
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            verify(artistService).getArtistById(TEST_USER_ID, TEST_ARTIST_ID);
        }
    }

    // Tests for getTopTracksByArtist method

    @Test
    void getTopTracksByArtist_ValidAuthHeaderAndArtistId_returnsTopTracksResponse() {
        // Given
        ArtistTopTracksResponse expectedResponse = new ArtistTopTracksResponse();
        ResponseEntity<ArtistTopTracksResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getTopTracks(TEST_USER_ID, TEST_ARTIST_ID)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<ArtistTopTracksResponse> result = spotifyArtistController
                    .getTopTracksByArtist(TEST_AUTH_HEADER, TEST_ARTIST_ID);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedResponse, result.getBody());
            verify(artistService).getTopTracks(TEST_USER_ID, TEST_ARTIST_ID);
        }
    }

    @Test
    void getTopTracksByArtist_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<ArtistTopTracksResponse> result = spotifyArtistController
                    .getTopTracksByArtist(TEST_AUTH_HEADER, TEST_ARTIST_ID);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getTopTracksByArtist_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getTopTracks(TEST_USER_ID, TEST_ARTIST_ID))
                    .thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<ArtistTopTracksResponse> result = spotifyArtistController
                    .getTopTracksByArtist(TEST_AUTH_HEADER, TEST_ARTIST_ID);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(artistService).getTopTracks(TEST_USER_ID, TEST_ARTIST_ID);
        }
    }

    // Tests for getAlbumsByArtist method

    @Test
    void getAlbumsByArtist_ValidParametersDefaultLimitOffset_returnsAlbumsResponse() {
        ArtistAlbumsResponse expectedResponse = new ArtistAlbumsResponse();
        ResponseEntity<ArtistAlbumsResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getAlbums(TEST_USER_ID, TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET))
                    .thenReturn(mockResponseEntity);

            // When
            ResponseEntity<ArtistAlbumsResponse> result = spotifyArtistController.getAlbumsByArtist(TEST_AUTH_HEADER,
                    TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedResponse, result.getBody());
            verify(artistService).getAlbums(TEST_USER_ID, TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET);
        }
    }

    @Test
    void getAlbumsByArtist_CustomLimitAndOffset_callsServiceWithCustomParameters() {
        // Given
        Integer customLimit = 10;
        Integer customOffset = 5;
        ArtistAlbumsResponse expectedResponse = new ArtistAlbumsResponse();
        ResponseEntity<ArtistAlbumsResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getAlbums(TEST_USER_ID, TEST_ARTIST_ID, customLimit, customOffset))
                    .thenReturn(mockResponseEntity);

            // When
            ResponseEntity<ArtistAlbumsResponse> result = spotifyArtistController.getAlbumsByArtist(TEST_AUTH_HEADER,
                    TEST_ARTIST_ID, customLimit, customOffset);

            // Then
            assertEquals(mockResponseEntity, result);
            verify(artistService).getAlbums(TEST_USER_ID, TEST_ARTIST_ID, customLimit, customOffset);
        }
    }

    @Test
    void getAlbumsByArtist_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<ArtistAlbumsResponse> result = spotifyArtistController.getAlbumsByArtist(TEST_AUTH_HEADER,
                    TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getAlbumsByArtist_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getAlbums(TEST_USER_ID, TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET))
                    .thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<ArtistAlbumsResponse> result = spotifyArtistController.getAlbumsByArtist(TEST_AUTH_HEADER,
                    TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(artistService).getAlbums(TEST_USER_ID, TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET);
        }
    }

    @Test
    void getTopTracksByArtist_ServiceReturnsError_returnsServiceError() {
        // Given
        ResponseEntity<ArtistTopTracksResponse> errorResponse = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getTopTracks(TEST_USER_ID, TEST_ARTIST_ID)).thenReturn(errorResponse);

            // When
            ResponseEntity<ArtistTopTracksResponse> result = spotifyArtistController
                    .getTopTracksByArtist(TEST_AUTH_HEADER, TEST_ARTIST_ID);

            // Then
            assertEquals(errorResponse, result);
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            verify(artistService).getTopTracks(TEST_USER_ID, TEST_ARTIST_ID);
        }
    }

    @Test
    void getAlbumsByArtist_ServiceReturnsError_returnsServiceError() {
        // Given
        ResponseEntity<ArtistAlbumsResponse> errorResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(artistService.getAlbums(TEST_USER_ID, TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET))
                    .thenReturn(errorResponse);

            // When
            ResponseEntity<ArtistAlbumsResponse> result = spotifyArtistController.getAlbumsByArtist(TEST_AUTH_HEADER,
                    TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(errorResponse, result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            verify(artistService).getAlbums(TEST_USER_ID, TEST_ARTIST_ID, TEST_LIMIT, TEST_OFFSET);
        }
    }
}
