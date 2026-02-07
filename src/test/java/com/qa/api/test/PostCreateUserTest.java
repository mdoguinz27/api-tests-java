package com.qa.api.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PostCreateUserTest {

    private PostCreateUser postCreateUser;
    private ObjectMapper objectMapper;

    @BeforeClass
    public void setup() {
        postCreateUser = new PostCreateUser();
        objectMapper = new ObjectMapper();
        System.out.println("=== Iniciando Suite de Tests Positivos ===");
    }

    @AfterClass
    public void teardown() {
        System.out.println("=== Finalizando Suite de Tests ===");
    }

    @Test(priority = 1)
    @Description("Verificar que se puede crear un usuario con datos aleatorios válidos")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUser() throws Exception {
        System.out.println("\n--- Test: Crear Usuario ---");

        // Llama al método que crea el usuario
        APIResponse response = postCreateUser.postCreateUserApi();

        // Verifica el código de estado
        Assert.assertEquals(response.status(), 201, "El código de estado debe ser 201 (Created)");

        // Verifica que el cuerpo de la respuesta no esté vacío
        String responseBody = response.text();
        Assert.assertNotNull(responseBody, "El cuerpo de la respuesta no debe ser nulo");
        Assert.assertFalse(responseBody.isEmpty(), "El cuerpo de la respuesta no debe estar vacío");

        // Parsear y verificar el ID del usuario
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        Assert.assertTrue(jsonResponse.has("id"), "La respuesta debe contener un campo 'id'");
        Assert.assertTrue(jsonResponse.has("name"), "La respuesta debe contener un campo 'name'");
        Assert.assertTrue(jsonResponse.has("email"), "La respuesta debe contener un campo 'email'");
        Assert.assertTrue(jsonResponse.has("gender"), "La respuesta debe contener un campo 'gender'");
        Assert.assertTrue(jsonResponse.has("status"), "La respuesta debe contener un campo 'status'");

        String userId = jsonResponse.get("id").asText();
        System.out.println("Usuario creado exitosamente con ID: " + userId);
    }

    @Test(priority = 2)
    @Description("Verificar que se puede obtener la lista de usuarios")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUsers() throws Exception {
        System.out.println("\n--- Test: Obtener Lista de Usuarios ---");

        // Llama al método que obtiene la lista de usuarios
        APIResponse response = postCreateUser.getUsers();

        // Verifica el código de estado
        Assert.assertEquals(response.status(), 200, "El código de estado debe ser 200 (OK)");

        // Verifica que la respuesta no esté vacía
        String responseBody = response.text();
        Assert.assertNotNull(responseBody, "La lista de usuarios no debe ser nula");
        Assert.assertFalse(responseBody.isEmpty(), "La lista de usuarios no debe estar vacía");

        // Parsear y verificar que es un array
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        Assert.assertTrue(jsonResponse.isArray(), "La respuesta debe ser un array de usuarios");
        Assert.assertTrue(jsonResponse.size() > 0, "El array debe contener al menos un usuario");

        System.out.println("Lista de usuarios obtenida exitosamente. Total: " + jsonResponse.size());
    }

    @Test(priority = 3)
    @Description("Verificar que se puede obtener el detalle de un usuario específico")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserDetails() throws Exception {
        System.out.println("\n--- Test: Obtener Detalle de Usuario ---");

        // Primero crea un nuevo usuario
        APIResponse createResponse = postCreateUser.postCreateUserApi();
        Assert.assertEquals(createResponse.status(), 201, "El usuario debe ser creado exitosamente");

        // Extrae el ID del usuario creado
        String userId = postCreateUser.extractUserId(createResponse);
        Assert.assertNotNull(userId, "El ID del usuario no debe ser nulo");
        System.out.println("Usuario creado con ID: " + userId);

        // Obtiene los detalles del usuario
        APIResponse detailsResponse = postCreateUser.getUserDetails(userId);

        // Verifica el código de estado
        Assert.assertEquals(detailsResponse.status(), 200, "El código de estado debe ser 200 (OK)");

        // Verifica que los detalles no estén vacíos
        String responseBody = detailsResponse.text();
        Assert.assertNotNull(responseBody, "Los detalles del usuario no deben ser nulos");
        Assert.assertFalse(responseBody.isEmpty(), "Los detalles del usuario no deben estar vacíos");

        // Parsear y verificar los campos
        JsonNode userDetails = objectMapper.readTree(responseBody);
        Assert.assertEquals(userDetails.get("id").asText(), userId, "El ID debe coincidir");
        Assert.assertTrue(userDetails.has("name"), "Debe tener campo 'name'");
        Assert.assertTrue(userDetails.has("email"), "Debe tener campo 'email'");
        Assert.assertTrue(userDetails.has("gender"), "Debe tener campo 'gender'");
        Assert.assertTrue(userDetails.has("status"), "Debe tener campo 'status'");

        System.out.println("Detalles del usuario obtenidos exitosamente");
    }

    @Test(priority = 4)
    @Description("Verificar que se puede actualizar un usuario existente")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateUser() throws Exception {
        System.out.println("\n--- Test: Actualizar Usuario ---");

        // Crear un usuario
        APIResponse createResponse = postCreateUser.postCreateUserApi();
        Assert.assertEquals(createResponse.status(), 201, "El usuario debe ser creado");
        String userId = postCreateUser.extractUserId(createResponse);

        // Actualizar el usuario
        String newName = "Updated User";
        String newEmail = "updated" + System.currentTimeMillis() + "@example.com";
        APIResponse updateResponse = postCreateUser.updateUser(userId, newName, newEmail);

        // Verificar la actualización
        Assert.assertEquals(updateResponse.status(), 200, "El código de estado debe ser 200 (OK)");

        JsonNode updatedUser = objectMapper.readTree(updateResponse.text());
        Assert.assertEquals(updatedUser.get("name").asText(), newName, "El nombre debe estar actualizado");
        Assert.assertEquals(updatedUser.get("email").asText(), newEmail, "El email debe estar actualizado");

        System.out.println("Usuario actualizado exitosamente");
    }

    @Test(priority = 5)
    @Description("Verificar que se puede eliminar un usuario existente")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteUser() throws Exception {
        System.out.println("\n--- Test: Eliminar Usuario ---");

        // Crear un usuario
        APIResponse createResponse = postCreateUser.postCreateUserApi();
        Assert.assertEquals(createResponse.status(), 201, "El usuario debe ser creado");
        String userId = postCreateUser.extractUserId(createResponse);

        // Eliminar el usuario
        APIResponse deleteResponse = postCreateUser.deleteUser(userId);
        Assert.assertEquals(deleteResponse.status(), 204, "El código de estado debe ser 204 (No Content)");

        // Verificar que el usuario ya no existe
        APIResponse getResponse = postCreateUser.getUserDetails(userId);
        Assert.assertEquals(getResponse.status(), 404, "El usuario eliminado no debe existir (404)");

        System.out.println("Usuario eliminado exitosamente");
    }

    @Test(priority = 6)
    @Description("Verificar que la paginación funciona correctamente")
    @Severity(SeverityLevel.MINOR)
    public void testGetUsersWithPagination() throws Exception {
        System.out.println("\n--- Test: Paginación de Usuarios ---");

        // Obtener primera página con 5 usuarios
        APIResponse response = postCreateUser.getUsersWithPagination(1, 5);

        Assert.assertEquals(response.status(), 200, "El código de estado debe ser 200");

        JsonNode users = objectMapper.readTree(response.text());
        Assert.assertTrue(users.isArray(), "La respuesta debe ser un array");
        Assert.assertTrue(users.size() <= 5, "Debe retornar máximo 5 usuarios");

        System.out.println("Paginación funciona correctamente. Usuarios en página 1: " + users.size());
    }
}
