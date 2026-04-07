package org.tp2.exo3;



import org.tp2.exo3.ProductApiClient;
import org.tp2.exo3.ApiException;
import org.tp2.exo3.InvalidDataFormatException;
import org.tp2.exo3.Product;

public class ProductService {

    private final ProductApiClient productApiClient;

    public ProductService(ProductApiClient productApiClient) {
        this.productApiClient = productApiClient;
    }

    /**
     * Récupère les détails d'un produit via le client API.
     *
     * @param productId identifiant du produit
     * @return le produit trouvé
     * @throws IllegalArgumentException   si l'id est null ou vide
     * @throws ApiException               si l'API échoue
     * @throws InvalidDataFormatException si les données reçues sont invalides
     */
    public Product getProduct(String productId) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("L'identifiant du produit est requis");
        }

        try {
            Product product = productApiClient.getProduct(productId);

            if (product == null) {
                throw new ApiException("Produit introuvable : " + productId, 404);
            }

            return product;

        } catch (ApiException | InvalidDataFormatException e) {
            // On laisse remonter les exceptions métier
            throw e;
        }
    }
}