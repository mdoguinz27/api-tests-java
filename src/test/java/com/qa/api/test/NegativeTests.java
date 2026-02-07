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

public class NegativeTests {

    private PostCreateUser postCreateUser;
    private ObjectMapper objectMapper;

    @BeforeClass
    public void setup() {
        postCreateUser = new PostCreateUser();
        objectMapper = new ObjectMapper();
        System.out.println("=== Iniciando Suite de Tests Negativos ===");
    }

    @AfterClass
    public void teardown() {
        System.out.println("=== Finalizando Suite de Tests Negativos ===");
    }

    @Test(priority = 1)
    @Description("Verificar que crear un usuario sin autenticación falla con 401")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUserWithoutAuth() throws Exception {
        System.out.println("\n--- Test Negativo: Crear Usuario Sin Autenticación ---");

        APIResponse response = postCreateUser.createUserWithoutAuth(
                "Test User",
                "test" + System.currentTimeMillis() + "@example.com",
                "male",
                "active");

        Assert.assertEquals(response.status(), 401,
                "Debe fallar con 401 (Unauthorized) cuando no hay autenticación");

        JsonNode errorResponse = objectMapper.readTree(response.text());
        Assert.assertTrue(errorResponse.has("message"), "La respuesta debe contener un mensaje de error");

        System.out.println("Test pasado: La API rechaza correctamente requests sin autenticación");
    }

    @Test(priority = 2)
    @Description("Verificar que crear un usuario con email duplicado falla con 422")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUserDuplicateEmail() throws Exception {
        System.out.println("\n--- Test Negativo: Email Duplicado ---");

        // Crear primer usuario
        String uniqueEmail = "duplicate" + System.currentTimeMillis() + "@example.com";
        APIResponse firstResponse = postCreateUser.createUserWithData(
                "First User",
                uniqueEmail,
                "male",
                "active");

        Assert.assertEquals(firstResponse.status(), 201, "El primer usuario debe ser creado exitosamente");

        // Intentar crear segundo usuario con el mismo email
        APIResponse duplicateResponse = postCreateUser.createUserWithData(
                "Second User",
                uniqueEmail,
                "female",
                "active");

        Assert.assertEquals(duplicateResponse.status(), 422,
                "Debe fallar con 422 (Unprocessable Entity) cuando el email está duplicado");

        JsonNode errorResponse = objectMapper.readTree(duplicateResponse.text());
        Assert.assertTrue(errorResponse.isArray(), "La respuesta debe ser un array de errores");

        System.out.println("Test pasado: La API rechaza correctamente emails duplicados");
    }

    @Test(priority = 3)
    @Description("Verificar que crear un usuario con email inválido falla con 422")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserInvalidEmail() throws Exception {
        System.out.println("\n--- Test Negativo: Email Inválido ---");

        APIResponse response = postCreateUser.createUserWithData(
                "Test User",
                "invalid-email-format", // Email sin formato válido
                "male",
                "active");

        Assert.assertEquals(response.status(), 422,
                "Debe fallar con 422 cuando el email tiene formato inválido");

        JsonNode errorResponse = objectMapper.readTree(response.text());
        Assert.assertTrue(errorResponse.isArray(), "La respuesta debe contener errores de validación");

        System.out.println("Test pasado: La API valida correctamente el formato del email");
    }

    @Test(priority = 4)
    @Description("Verificar que crear un usuario sin campos requeridos falla con 422")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserMissingRequiredFields() throws Exception {
        System.out.println("\n--- Test Negativo: Campos Requeridos Faltantes ---");

        APIResponse response = postCreateUser.createUserWithData(
                "", // Nombre vacío
                "", // Email vacío
                "male",
                "active");

        Assert.assertEquals(response.status(), 422,
                "Debe fallar con 422 cuando faltan campos requeridos");

        JsonNode errorResponse = objectMapper.readTree(response.text());
        Assert.assertTrue(errorResponse.isArray(), "La respuesta debe contener errores de validación");

        System.out.println("Test pasado: La API valida correctamente los campos requeridos");
    }

    @Test(priority = 5)
    @Description("Verificar que obtener un usuario inexistente falla con 404")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetNonExistentUser() throws Exception {
        System.out.println("\n--- Test Negativo: Obtener Usuario Inexistente ---");

        String nonExistentId = "999999999";
        APIResponse response = postCreateUser.getUserDetails(nonExistentId);

        Assert.assertEquals(response.status(), 404,
                "Debe fallar con 404 (Not Found) cuando el usuario no existe");

        JsonNode errorResponse = objectMapper.readTree(response.text());
        Assert.assertTrue(errorResponse.has("message"), "La respuesta debe contener un mensaje de error");

        System.out.println("Test pasado: La API retorna 404 correctamente para usuarios inexistentes");
    }

    @Test(priority = 6)
    @Description("Verificar que actualizar un usuario inexistente falla con 404")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateNonExistentUser() throws Exception {
        System.out.println("\n--- Test Negativo: Actualizar Usuario Inexistente ---");

        String nonExistentId = "999999999";
        APIResponse response = postCreateUser.updateUser(
                nonExistentId,
                "Updated Name",
                "updated@example.com");

        Assert.assertEquals(response.status(), 404,
                "Debe fallar con 404 cuando se intenta actualizar un usuario inexistente");

        JsonNode errorResponse = objectMapper.readTree(response.text());
        Assert.assertTrue(errorResponse.has("message"), "La respuesta debe contener un mensaje de error");

        System.out.println("Test pasado: La API rechaza correctamente actualizaciones a usuarios inexistentes");
    }

    @Test(priority = 7)
    @Description("Verificar que eliminar un usuario inexistente falla con 404")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistentUser() throws Exception {
        System.out.println("\n--- Test Negativo: Eliminar Usuario Inexistente ---");

        String nonExistentId = "999999999";
        APIResponse response = postCreateUser.deleteUser(nonExistentId);

        Assert.assertEquals(response.status(), 404,
                "Debe fallar con 404 cuando se intenta eliminar un usuario inexistente");

        JsonNode errorResponse = objectMapper.readTree(response.text());
        Assert.assertTrue(errorResponse.has("message"), "La respuesta debe contener un mensaje de error");

        System.out.println("Test pasado: La API rechaza correctamente eliminaciones de usuarios inexistentes");
    }

    @Test(priority = 8)
    @Description("Verificar que crear un usuario con género inválido falla con 422")
    @Severity(SeverityLevel.MINOR)
    public void testCreateUserInvalidGender() throws Exception {
        System.out.println("\n--- Test Negativo: Género Inválido ---");

        APIResponse response = postCreateUser.createUserWithData(
                "Test User",
                "test" + System.currentTimeMillis() + "@example.com",
                "invalid_gender", // Género inválido
                "active");

        Assert.assertEquals(response.status(), 422,
                "Debe fallar con 422 cuando el género es inválido");

        JsonNode errorResponse = objectMapper.readTree(response.text());
        Assert.assertTrue(errorResponse.isArray(), "La respuesta debe contener errores de validación");

        System.out.println("Test pasado: La API valida correctamente el campo gender");
    }

    @Test(priority = 9)
    @Description("Verificar que crear un usuario con status inválido falla con 422")
    @Severity(SeverityLevel.MINOR)
    public void testCreateUserInvalidStatus() throws Exception {
        System.out.println("\n--- Test Negativo: Status Inválido ---");

        APIResponse response = postCreateUser.createUserWithData(
                "Test User",
                "test" + System.currentTimeMillis() + "@example.com",
                "male",
                "invalid_status" // Status inválido
        );

        Assert.assertEquals(response.status(), 422,
                "Debe fallar con 422 cuando el status es inválido");

        JsonNode errorResponse = objectMapper.readTree(response.text());
        Assert.assertTrue(errorResponse.isArray(), "La respuesta debe contener errores de validación");

        System.out.println("Test pasado: La API valida correctamente el campo status");
    }

    @Test(priority = 10)
    @Description("Verificar que actualizar con email duplicado falla con 422")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateUserDuplicateEmail() throws Exception {
        System.out.println("\n--- Test Negativo: Actualizar con Email Duplicado ---");

        // Crear dos usuarios
        String email1 = "user1_" + System.currentTimeMillis() + "@example.com";
        String email2 = "user2_" + System.currentTimeMillis() + "@example.com";

        APIResponse user1Response = postCreateUser.createUserWithData("User 1", email1, "male", "active");
        APIResponse user2Response = postCreateUser.createUserWithData("User 2", email2, "female", "active");

        Assert.assertEquals(user1Response.status(), 201, "Usuario 1 debe ser creado");
        Assert.assertEquals(user2Response.status(), 201, "Usuario 2 debe ser creado");

        String user2Id = postCreateUser.extractUserId(user2Response);

        // Intentar actualizar user2 con el email de user1
        APIResponse updateResponse = postCreateUser.updateUser(user2Id, "User 2 Updated", email1);

        Assert.assertEquals(updateResponse.status(), 422,
                "Debe fallar con 422 cuando se intenta actualizar con un email duplicado");

        System.out.println("Test pasado: La API previene actualizaciones con emails duplicados");
    }
}
