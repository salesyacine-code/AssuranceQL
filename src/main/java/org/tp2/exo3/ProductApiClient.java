package org.tp2.exo3;


import org.tp2.exo3.ApiException;
import org.tp2.exo3.InvalidDataFormatException;
import org.tp2.exo3.Product;

public interface ProductApiClient {
    /**
     * Récupère un produit depuis l'API externe.
     *
     * @param productId identifiant du produit
     * @return le produit correspondant
     * @throws ApiException              si l'API répond avec une erreur (404, 500...)
     * @throws InvalidDataFormatException si la réponse ne peut pas être parsée
     */
    Product getProduct(String productId)
            throws ApiException, InvalidDataFormatException;
}