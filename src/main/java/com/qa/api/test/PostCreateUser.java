package com.qa.api.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qa.api.utils.ConfigReader;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.UUID;
import java.util.Random;

public class PostCreateUser {

    private static Playwright playwright;
    private static APIRequestContext requestContext;
    private final String BASE_URL;
    private final String AUTH_TOKEN;

    // Singleton pattern para Playwright
    static {
        playwright = Playwright.create();
    }

    public PostCreateUser() {
        this.BASE_URL = ConfigReader.getBaseUrl();
        this.AUTH_TOKEN = ConfigReader.getAuthToken();
        if (requestContext == null) {
            requestContext = playwright.request().newContext();
        }
    }

    // Método para crear un nuevo usuario
    @Step("Crear nuevo usuario con datos aleatorios")
    public APIResponse postCreateUserApi() throws IOException {
        // Generar datos aleatorios para el usuario
        String randomName = "User" + UUID.randomUUID().toString().substring(0, 5);
        String randomEmail = "user" + UUID.randomUUID().toString().substring(0, 5) + "@example.com";
        String[] genders = { "male", "female" };
        String randomGender = genders[new Random().nextInt(genders.length)];
        String status = "active";

        // Crear el cuerpo de la solicitud POST para crear el nuevo usuario
        String requestBody = "{\n" +
                "    \"name\": \"" + randomName + "\",\n" +
                "    \"email\": \"" + randomEmail + "\",\n" +
                "    \"gender\": \"" + randomGender + "\",\n" +
                "    \"status\": \"" + status + "\"\n" +
                "}";

        System.out.println("Creating user with data: " + requestBody);

        // Enviar la solicitud POST
        APIResponse createResponse = requestContext.post(BASE_URL,
                RequestOptions.create()
                        .setData(requestBody)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", AUTH_TOKEN));

        System.out.println("Response Status: " + createResponse.status());
        System.out.println("Response Body: " + createResponse.text());

        return createResponse;
    }

    // Método para crear usuario con datos específicos
    @Step("Crear usuario con datos específicos: {name}, {email}, {gender}, {status}")
    public APIResponse createUserWithData(String name, String email, String gender, String status) throws IOException {
        String requestBody = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"gender\": \"" + gender + "\",\n" +
                "    \"status\": \"" + status + "\"\n" +
                "}";

        System.out.println("Creating user with specific data: " + requestBody);

        APIResponse createResponse = requestContext.post(BASE_URL,
                RequestOptions.create()
                        .setData(requestBody)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", AUTH_TOKEN));

        System.out.println("Response Status: " + createResponse.status());
        return createResponse;
    }

    // Método para obtener todos los usuarios
    @Step("Obtener lista de todos los usuarios")
    public APIResponse getUsers() throws IOException {
        System.out.println("Fetching all users from: " + BASE_URL);

        APIResponse getUsersResponse = requestContext.get(BASE_URL,
                RequestOptions.create().setHeader("Authorization", AUTH_TOKEN));

        System.out.println("Response Status: " + getUsersResponse.status());
        return getUsersResponse;
    }

    // Método para obtener usuarios con paginación
    @Step("Obtener usuarios con paginación - Página: {page}, Por página: {perPage}")
    public APIResponse getUsersWithPagination(int page, int perPage) throws IOException {
        String url = BASE_URL + "?page=" + page + "&per_page=" + perPage;
        System.out.println("Fetching users with pagination from: " + url);

        APIResponse response = requestContext.get(url,
                RequestOptions.create().setHeader("Authorization", AUTH_TOKEN));

        System.out.println("Response Status: " + response.status());
        return response;
    }

    // Método para obtener los detalles de un usuario específico
    @Step("Obtener detalles del usuario con ID: {userId}")
    public APIResponse getUserDetails(String userId) throws IOException {
        String url = BASE_URL + "/" + userId;
        System.out.println("Fetching user details from: " + url);

        APIResponse userDetailsResponse = requestContext.get(url,
                RequestOptions.create().setHeader("Authorization", AUTH_TOKEN));

        System.out.println("Response Status: " + userDetailsResponse.status());
        return userDetailsResponse;
    }

    // Método para actualizar un usuario
    @Step("Actualizar usuario con ID: {userId}")
    public APIResponse updateUser(String userId, String name, String email) throws IOException {
        String requestBody = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\"\n" +
                "}";

        String url = BASE_URL + "/" + userId;
        System.out.println("Updating user at: " + url);
        System.out.println("Update data: " + requestBody);

        APIResponse updateResponse = requestContext.put(url,
                RequestOptions.create()
                        .setData(requestBody)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", AUTH_TOKEN));

        System.out.println("Response Status: " + updateResponse.status());
        return updateResponse;
    }

    // Método para eliminar un usuario
    @Step("Eliminar usuario con ID: {userId}")
    public APIResponse deleteUser(String userId) throws IOException {
        String url = BASE_URL + "/" + userId;
        System.out.println("Deleting user at: " + url);

        APIResponse deleteResponse = requestContext.delete(url,
                RequestOptions.create().setHeader("Authorization", AUTH_TOKEN));

        System.out.println("Response Status: " + deleteResponse.status());
        return deleteResponse;
    }

    // Método para crear usuario sin autenticación (para tests negativos)
    @Step("Crear usuario sin autenticación")
    public APIResponse createUserWithoutAuth(String name, String email, String gender, String status)
            throws IOException {
        String requestBody = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"gender\": \"" + gender + "\",\n" +
                "    \"status\": \"" + status + "\"\n" +
                "}";

        APIResponse createResponse = requestContext.post(BASE_URL,
                RequestOptions.create()
                        .setData(requestBody)
                        .setHeader("Content-Type", "application/json"));

        System.out.println("Response Status (No Auth): " + createResponse.status());
        return createResponse;
    }

    // Método para extraer el ID del usuario de la respuesta
    public String extractUserId(APIResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.body());
        return jsonResponse.get("id").asText();
    }

    // Método para cerrar recursos
    public static void cleanup() {
        if (requestContext != null) {
            requestContext.dispose();
            requestContext = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}
