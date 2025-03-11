package com.dtos;

import lombok.Getter;

/**
 * Classe représentant une réponse API générique.
 *
 * @param <T> le type des données contenues dans la réponse
 */
@Getter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * Crée une réponse de succès.
     *
     * @param data    les données retournées
     * @param message le message de succès
     * @param <T>     le type des données
     * @return une réponse API de succès
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Crée une réponse d'erreur.
     *
     * @param message le message d'erreur
     * @param <T>     le type des données
     * @return une réponse API d'erreur
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
