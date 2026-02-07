# 🚀 API Test Automation - GoRest API

<div align="center">

![Java](https://img.shields.io/badge/Java-11+-orange?style=for-the-badge&logo=java)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue?style=for-the-badge&logo=apache-maven)
![Playwright](https://img.shields.io/badge/Playwright-1.48.0-green?style=for-the-badge&logo=playwright)
![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red?style=for-the-badge)
![Allure](https://img.shields.io/badge/Allure-2.24.0-yellow?style=for-the-badge)

**Framework de automatización de pruebas de API REST usando Java, Playwright y TestNG**

[Características](#-características) •
[Requisitos](#-requisitos-previos) •
[Instalación](#-instalación) •
[Ejecutar Tests](#-ejecutar-tests) •
[Reportes](#-reportes)

</div>

---

## 📋 Descripción del Proyecto

Este proyecto implementa un framework completo de automatización de pruebas para la API REST de [GoRest](https://gorest.co.in/), una API pública que permite gestionar usuarios. El framework incluye:

- ✅ **16 casos de prueba** (6 positivos + 10 negativos)
- 📊 **Doble reportería** (Allure + ExtentReports)
- 🏗️ **Arquitectura escalable** con patrón Singleton
- 🔧 **Configuración externalizada** en properties
- 📝 **Logging detallado** con SLF4J
- 🎯 **Anotaciones Allure** para mejor trazabilidad

### Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 11+ | Lenguaje de programación |
| Maven | 3.6+ | Gestión de dependencias y build |
| Playwright | 1.48.0 | Cliente HTTP para API testing |
| TestNG | 7.10.2 | Framework de testing |
| Allure | 2.24.0 | Reportería avanzada |
| ExtentReports | 5.1.1 | Reportería HTML |
| Jackson | 2.18.1 | Procesamiento JSON |
| SLF4J | 2.0.9 | Logging |

---

## 🎯 Características

### Tests Positivos ✅
1. **Crear Usuario** - Valida creación con datos aleatorios
2. **Obtener Lista de Usuarios** - Verifica endpoint GET all users
3. **Obtener Detalle de Usuario** - Consulta usuario específico por ID
4. **Actualizar Usuario** - Modifica datos de usuario existente
5. **Eliminar Usuario** - Elimina usuario y verifica inexistencia
6. **Paginación** - Valida parámetros de paginación

### Tests Negativos ❌
1. **Sin Autenticación** - Verifica rechazo sin token (401)
2. **Email Duplicado** - Valida prevención de duplicados (422)
3. **Email Inválido** - Verifica validación de formato (422)
4. **Campos Requeridos Faltantes** - Valida campos obligatorios (422)
5. **Usuario Inexistente (GET)** - Verifica 404 en consulta
6. **Usuario Inexistente (UPDATE)** - Verifica 404 en actualización
7. **Usuario Inexistente (DELETE)** - Verifica 404 en eliminación
8. **Género Inválido** - Valida campo gender (422)
9. **Status Inválido** - Valida campo status (422)
10. **Actualizar con Email Duplicado** - Previene duplicados en UPDATE (422)

---

## 💻 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

### 1. Java Development Kit (JDK) 11 o superior

```bash
# Verificar instalación
java -version

# Debe mostrar algo como:
# java version "11.0.x" o superior
```

**Instalación en macOS:**
```bash
brew install openjdk@11
```

### 2. Apache Maven 3.6 o superior

```bash
# Verificar instalación
mvn -version

# Debe mostrar algo como:
# Apache Maven 3.6.x o superior
```

**Instalación en macOS:**
```bash
brew install maven
```

### 3. Token de API de GoRest

Obtén tu token gratuito en: https://gorest.co.in/

---

## 📦 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/mdoguinz27/api-tests-java.git
cd api-tests-java
```

### 2. Configurar Token de API

Edita el archivo `src/test/resources/config.properties`:

```properties
api.base.url=https://gorest.co.in/public/v2/users
api.auth.token=Bearer TU_TOKEN_AQUI
api.timeout=30000
extent.report.path=target/extent-reports/
extent.report.name=API Test Report
```

**⚠️ Importante:** Reemplaza `TU_TOKEN_AQUI` con tu token real de GoRest.

### 3. Instalar Dependencias

```bash
mvn clean install -DskipTests
```

Este comando:
- Descarga todas las dependencias del proyecto
- Compila el código fuente
- Prepara el proyecto para ejecución

---

## 🚀 Ejecutar Tests

### Opción 1: Maven CLI (Recomendado)

#### Ejecutar TODOS los tests:
```bash
mvn clean test
```

#### Ejecutar solo tests positivos:
```bash
mvn clean test -Dtest=PostCreateUserTest
```

#### Ejecutar solo tests negativos:
```bash
mvn clean test -Dtest=NegativeTests
```

#### Ejecutar un test específico:
```bash
mvn clean test -Dtest=PostCreateUserTest#testCreateUser
```

### Opción 2: IntelliJ IDEA

#### Ejecutar desde testng.xml:
1. Abre el proyecto en IntelliJ IDEA
2. Navega al archivo `testng.xml` en la raíz del proyecto
3. Haz clic derecho sobre el archivo
4. Selecciona **Run 'testng.xml'**

#### Ejecutar desde una clase de test:
1. Abre `PostCreateUserTest.java` o `NegativeTests.java`
2. Haz clic derecho en la clase
3. Selecciona **Run 'PostCreateUserTest'** o **Run 'NegativeTests'**

#### Ejecutar un test individual:
1. Abre la clase de test
2. Haz clic en el ícono verde ▶️ junto al método `@Test`
3. Selecciona **Run 'testCreateUser()'**

### Opción 3: Desde Terminal con TestNG

```bash
java -cp "target/test-classes:target/classes:$(mvn dependency:build-classpath | grep -v '\[INFO\]')" org.testng.TestNG testng.xml
```

---

## 📊 Reportes

El proyecto genera **dos tipos de reportes** automáticamente después de cada ejecución:

### 1. Allure Report (Recomendado) 🎯

**Características:**
- Reportes interactivos con gráficos
- Timeline de ejecución
- Historial de tests
- Categorización por severidad
- Detalles de requests/responses

**Generar y visualizar:**
```bash
# Generar reporte
mvn allure:serve

# O generar HTML estático
mvn allure:report
# El reporte se genera en: target/site/allure-maven-plugin/index.html
```

**Abrir reporte:**
```bash
open target/site/allure-maven-plugin/index.html
```

### 2. ExtentReports 📈

**Características:**
- Reporte HTML con tema oscuro
- Dashboard con estadísticas
- Logs detallados por test
- Timestamps de ejecución

**Ubicación del reporte:**
```
target/extent-reports/ExtentReport.html
```

**Abrir reporte:**
```bash
open target/extent-reports/ExtentReport.html
```

### Ejemplo de Salida en Consola

```
=== Iniciando Suite de Tests Positivos ===

--- Test: Crear Usuario ---
Creating user with data: {"name":"User12abc","email":"user12abc@example.com","gender":"male","status":"active"}
Response Status: 201
Usuario creado exitosamente con ID: 7654321

--- Test: Obtener Lista de Usuarios ---
Fetching all users from: https://gorest.co.in/public/v2/users
Response Status: 200
Lista de usuarios obtenida exitosamente. Total: 20

=== Finalizando Suite de Tests ===

===============================================
API Test Suite
Total tests run: 16, Passes: 16, Failures: 0, Skips: 0
===============================================
```

---

## 📁 Estructura del Proyecto

```
api-tests-java/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/qa/api/
│   │           ├── test/
│   │           │   └── PostCreateUser.java      # Cliente API con métodos CRUD
│   │           └── utils/
│   │               ├── ConfigReader.java        # Lector de configuración
│   │               ├── ExtentReportManager.java # Gestor de ExtentReports
│   │               └── TestListener.java        # Listener de TestNG
│   └── test/
│       ├── java/
│       │   └── com/qa/api/test/
│       │       ├── PostCreateUserTest.java      # Tests positivos (6 tests)
│       │       └── NegativeTests.java           # Tests negativos (10 tests)
│       └── resources/
│           ├── config.properties                # Configuración del proyecto
│           └── allure.properties                # Configuración de Allure
├── target/
│   ├── allure-results/                          # Resultados de Allure
│   ├── extent-reports/                          # Reportes de ExtentReports
│   └── surefire-reports/                        # Reportes de Surefire
├── pom.xml                                      # Configuración de Maven
├── testng.xml                                   # Suite de TestNG
└── README.md                                    # Este archivo
```

### Descripción de Componentes

#### `PostCreateUser.java`
Clase principal que implementa el cliente API con:
- Patrón Singleton para Playwright
- Métodos CRUD completos (CREATE, READ, UPDATE, DELETE)
- Manejo de paginación
- Métodos para tests negativos (sin auth, datos inválidos)
- Logging detallado con `@Step` de Allure

#### `PostCreateUserTest.java`
Suite de tests positivos que valida:
- Creación de usuarios con datos válidos
- Obtención de listas y detalles
- Actualización de datos
- Eliminación de usuarios
- Funcionalidad de paginación

#### `NegativeTests.java`
Suite de tests negativos que valida:
- Manejo de errores de autenticación
- Validación de datos de entrada
- Prevención de duplicados
- Manejo de recursos inexistentes
- Validación de campos con valores inválidos

#### Utilities
- **ConfigReader**: Lee configuración desde `config.properties`
- **ExtentReportManager**: Configura y gestiona reportes ExtentReports
- **TestListener**: Integra TestNG con ExtentReports

---

## 🔧 Configuración

### Archivo `config.properties`

```properties
# URL base de la API
api.base.url=https://gorest.co.in/public/v2/users

# Token de autenticación (Bearer token)
api.auth.token=Bearer TU_TOKEN_AQUI

# Timeout para requests (en milisegundos)
api.timeout=30000

# Configuración de reportes
extent.report.path=target/extent-reports/
extent.report.name=API Test Report
```

### Variables de Entorno (Opcional)

También puedes configurar el token como variable de entorno:

```bash
export API_AUTH_TOKEN="Bearer tu_token_aqui"
```

Y modificar `ConfigReader.java` para leerlo:

```java
public static String getAuthToken() {
    String envToken = System.getenv("API_AUTH_TOKEN");
    return envToken != null ? envToken : properties.getProperty("api.auth.token");
}
```

---

## 📖 Documentación de la API

### Endpoints Utilizados

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/users` | Crear nuevo usuario | ✅ |
| GET | `/users` | Obtener lista de usuarios | ✅ |
| GET | `/users/{id}` | Obtener detalle de usuario | ✅ |
| PUT | `/users/{id}` | Actualizar usuario | ✅ |
| DELETE | `/users/{id}` | Eliminar usuario | ✅ |

### Estructura de Usuario

```json
{
  "id": 123456,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "gender": "male",
  "status": "active"
}
```

### Campos

| Campo | Tipo | Requerido | Valores Válidos |
|-------|------|-----------|-----------------|
| `name` | String | ✅ | Cualquier texto |
| `email` | String | ✅ | Email válido único |
| `gender` | String | ✅ | `male`, `female` |
| `status` | String | ✅ | `active`, `inactive` |

### Códigos de Estado

| Código | Significado | Cuándo se usa |
|--------|-------------|---------------|
| 200 | OK | GET, PUT exitosos |
| 201 | Created | POST exitoso |
| 204 | No Content | DELETE exitoso |
| 401 | Unauthorized | Sin token o token inválido |
| 404 | Not Found | Recurso no existe |
| 422 | Unprocessable Entity | Datos inválidos o duplicados |

---

## 🐛 Troubleshooting

### Problema: "mvn: command not found"

**Solución:**
```bash
brew install maven
```

### Problema: "Java version not compatible"

**Solución:**
```bash
# Instalar Java 11
brew install openjdk@11

# Configurar JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
```

### Problema: Tests fallan con 401 Unauthorized

**Causa:** Token de API inválido o no configurado

**Solución:**
1. Verifica que el token en `config.properties` sea correcto
2. Asegúrate de incluir el prefijo `Bearer `
3. Obtén un nuevo token en https://gorest.co.in/

### Problema: "Connection timeout"

**Causa:** Problemas de red o API no disponible

**Solución:**
1. Verifica tu conexión a internet
2. Aumenta el timeout en `config.properties`:
   ```properties
   api.timeout=60000
   ```
3. Verifica que la API esté disponible: https://gorest.co.in/

### Problema: Tests fallan con 422 en email duplicado

**Causa:** Email ya existe en la base de datos

**Solución:** Esto es esperado en tests negativos. Los tests positivos usan `System.currentTimeMillis()` para generar emails únicos.

### Problema: No se generan reportes

**Solución:**
```bash
# Limpiar y regenerar
mvn clean test

# Para Allure, asegúrate de tener Allure instalado
brew install allure
mvn allure:serve
```

---

## 🤝 Contribuir

### Agregar Nuevos Tests

1. **Para tests positivos:** Agrega métodos en `PostCreateUserTest.java`
2. **Para tests negativos:** Agrega métodos en `NegativeTests.java`

Ejemplo de nuevo test:

```java
@Test(priority = 7)
@Description("Descripción del test")
@Severity(SeverityLevel.NORMAL)
public void testNuevoTest() throws Exception {
    System.out.println("\n--- Test: Nuevo Test ---");
    
    APIResponse response = postCreateUser.metodoAPI();
    
    Assert.assertEquals(response.status(), 200, "Mensaje de validación");
    
    System.out.println("Test completado exitosamente");
}
```

### Agregar Nuevos Endpoints

1. Agrega el método en `PostCreateUser.java`:

```java
@Step("Descripción del paso")
public APIResponse nuevoMetodo(String param) throws IOException {
    String url = BASE_URL + "/nuevo-endpoint";
    APIResponse response = requestContext.get(url,
            RequestOptions.create().setHeader("Authorization", AUTH_TOKEN));
    return response;
}
```

2. Crea tests correspondientes en las clases de test

---

## 📝 Notas Adicionales

### Mejores Prácticas Implementadas

- ✅ **Patrón Singleton** para Playwright (eficiencia de recursos)
- ✅ **Configuración externalizada** (fácil mantenimiento)
- ✅ **Logging detallado** (debugging simplificado)
- ✅ **Anotaciones Allure** (trazabilidad completa)
- ✅ **Validaciones exhaustivas** (confiabilidad de tests)
- ✅ **Separación de concerns** (código mantenible)

### Próximas Mejoras Sugeridas

- [ ] Integración con CI/CD (Jenkins, GitHub Actions)
- [ ] Tests de performance con JMeter
- [ ] Validación de esquemas JSON con JSON Schema
- [ ] Tests de seguridad (SQL injection, XSS)
- [ ] Paralelización de tests con TestNG
- [ ] Docker containerization

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo `LICENSE` para más detalles.

---

## 👤 Autor

**María Gabriela Doguinz**

- GitHub: [@mdoguinz27](https://github.com/mdoguinz27)
- Email: tu-email@example.com

---

## 🙏 Agradecimientos

- [GoRest API](https://gorest.co.in/) por proporcionar una API pública gratuita
- [Playwright](https://playwright.dev/) por el excelente framework de testing
- [Allure Framework](https://docs.qameta.io/allure/) por la reportería avanzada
- [ExtentReports](https://www.extentreports.com/) por los reportes HTML

---

<div align="center">

**⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub ⭐**

Made with ❤️ by María Gabriela Doguinz

</div>
