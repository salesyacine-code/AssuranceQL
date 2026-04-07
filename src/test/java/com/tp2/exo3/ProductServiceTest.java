package com.tp2.exo3;


import org.tp2.exo3.ProductApiClient;
import org.tp2.exo3.ApiException;
import org.tp2.exo3.InvalidDataFormatException;
import org.tp2.exo3.Product;
import  org.tp2.exo3.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductApiClient productApiClient;

    @InjectMocks
    private ProductService productService;

    private Product mockProduct;

    @BeforeEach
    void setUp() {
        mockProduct = new Product("P001", "Laptop Pro", 1299.99, "Informatique");
    }

    // ════════════════════════════════════════════════════════════════════════
    // Scénario 1 — Récupération réussie
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Scénario 1 : Récupération réussie")
    class RecuperationReussie {

        @Test
        @DisplayName("getProduct doit retourner le produit correct")
        void getProduct_shouldReturnProduct_whenApiRespondsSuccessfully()
                throws Exception {
            // ARRANGE
            when(productApiClient.getProduct("P001")).thenReturn(mockProduct);

            // ACT
            Product result = productService.getProduct("P001");

            // ASSERT
            assertNotNull(result);
            assertEquals("P001",         result.getId());
            assertEquals("Laptop Pro",   result.getName());
            assertEquals(1299.99,        result.getPrice(), 0.001);
            assertEquals("Informatique", result.getCategory());
        }

        @Test
        @DisplayName("getProduct doit appeler l'API avec le bon identifiant")
        void getProduct_shouldCallApiClient_withCorrectId() throws Exception {
            // ARRANGE
            when(productApiClient.getProduct("P001")).thenReturn(mockProduct);

            // ACT
            productService.getProduct("P001");

            // ASSERT — vérification de l'argument passé au mock
            verify(productApiClient, times(1)).getProduct("P001");
            verifyNoMoreInteractions(productApiClient);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Scénario 2 — Format de données incompatible
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Scénario 2 : Format de données incompatible")
    class FormatIncompatible {

        @Test
        @DisplayName("getProduct doit lever InvalidDataFormatException si le format est invalide")
        void getProduct_shouldThrow_whenDataFormatIsInvalid() throws Exception {
            // ARRANGE — le mock simule une réponse mal formée
            when(productApiClient.getProduct("P999"))
                    .thenThrow(new InvalidDataFormatException(
                            "JSON invalide reçu de l'API pour le produit P999"
                    ));

            // ACT + ASSERT
            InvalidDataFormatException ex = assertThrows(
                    InvalidDataFormatException.class,
                    () -> productService.getProduct("P999")
            );

            assertTrue(ex.getMessage().contains("P999"),
                    "Le message doit mentionner l'id du produit");

            verify(productApiClient).getProduct("P999");
        }

        @Test
        @DisplayName("getProduct doit lever IllegalArgumentException si l'id est null")
        void getProduct_shouldThrow_whenProductIdIsNull() {
            // ACT + ASSERT — le service valide avant même d'appeler l'API
            assertThrows(
                    IllegalArgumentException.class,
                    () -> productService.getProduct(null)
            );

            // L'API ne doit jamais être appelée avec un id null
            verifyNoInteractions(productApiClient);
        }

        @Test
        @DisplayName("getProduct doit lever IllegalArgumentException si l'id est vide")
        void getProduct_shouldThrow_whenProductIdIsBlank() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> productService.getProduct("   ")
            );

            verifyNoInteractions(productApiClient);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Scénario 3 — Échecs d'appel API
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Scénario 3 : Échecs d'appel API")
    class EchecApi {

        @Test
        @DisplayName("getProduct doit propager ApiException en cas d'erreur 404")
        void getProduct_shouldThrow_whenApiReturns404() throws Exception {
            // ARRANGE
            when(productApiClient.getProduct("P404"))
                    .thenThrow(new ApiException("Produit non trouvé", 404));

            // ACT + ASSERT
            ApiException ex = assertThrows(
                    ApiException.class,
                    () -> productService.getProduct("P404")
            );

            assertEquals(404, ex.getStatusCode());
            assertTrue(ex.getMessage().contains("non trouvé"));

            verify(productApiClient).getProduct("P404");
        }

        @Test
        @DisplayName("getProduct doit propager ApiException en cas d'erreur 500")
        void getProduct_shouldThrow_whenApiReturns500() throws Exception {
            // ARRANGE
            when(productApiClient.getProduct("P500"))
                    .thenThrow(new ApiException("Erreur interne du serveur", 500));

            // ACT + ASSERT
            ApiException ex = assertThrows(
                    ApiException.class,
                    () -> productService.getProduct("P500")
            );

            assertEquals(500, ex.getStatusCode());

            verify(productApiClient).getProduct("P500");
        }

        @Test
        @DisplayName("getProduct doit lever ApiException si l'API retourne null")
        void getProduct_shouldThrow_whenApiReturnsNull() throws Exception {
            // ARRANGE — l'API répond mais sans données
            when(productApiClient.getProduct("P000")).thenReturn(null);

            // ACT + ASSERT
            ApiException ex = assertThrows(
                    ApiException.class,
                    () -> productService.getProduct("P000")
            );

            assertEquals(404, ex.getStatusCode());
            verify(productApiClient).getProduct("P000");
        }
    }
}
