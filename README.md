# 🚗 QA Automation SOAT Digital - Interseguro

## 📋 Descripción

Framework de automatización de pruebas para la plataforma **SOAT Digital** de Interseguro.  
Desarrollado como parte del reto técnico de QA Automation.

**URL bajo prueba:** https://test.interseguro.pe/soat-digital/

---

## 🛠 Stack Tecnológico

| Tecnología | Versión | Propósito |
|---|---|---|
| Java | 17 LTS | Lenguaje base |
| Maven | 3.9.6 | Gestión de dependencias y build |
| Selenium WebDriver | 4.27.0 | Automatización del navegador |
| Cucumber | 7.18.0 | BDD - Escenarios en Gherkin |
| JUnit 5 | 5.10.2 | Motor de ejecución de tests |
| WebDriverManager | 5.9.2 | Gestión automática de drivers |

---

## 🏗 Arquitectura

```
src/test/
├── java/com/interseguro/soat/
│   ├── pages/                    # Page Objects (POM)
│   │   ├── BasePage.java         # Clase base abstracta
│   │   ├── HomePage.java         # Landing - Ingreso de placa
│   │   ├── PlanSelectionPage.java # Paso 1/2 - Edición y planes
│   │   └── PaymentPage.java      # Paso 2/2 - Resumen de compra
│   ├── steps/                    # Step Definitions
│   │   ├── SoatSteps.java        # Pasos de los escenarios
│   │   └── Hooks.java            # Before/After hooks
│   ├── runners/
│   │   └── TestRunner.java       # JUnit 5 + Cucumber Engine
│   └── utils/                    # Utilidades
│       ├── ConfigManager.java    # Configuración centralizada
│       ├── DriverFactory.java    # Singleton ThreadLocal del driver
│       └── ScreenshotHelper.java # Captura de evidencias
└── resources/
    ├── features/
    │   └── soat_cotizacion.feature # Escenarios Gherkin (BDD)
    └── config.properties           # Configuración externalizada
```

### Patrones de Diseño Aplicados

- **Page Object Model (POM)** con `BasePage` abstracta (herencia y DRY)
- **Singleton con ThreadLocal** para WebDriver (thread-safe)
- **Factory Pattern** en `DriverFactory` para instanciación del driver
- **Template Method** en `BasePage` con métodos utilitarios reutilizables
- **Strategy Pattern** en `selectFromDropdown` con múltiples estrategias de selección

### Buenas Prácticas de QA Automation

- **Configuración externalizada** en `config.properties` (no hardcoded)
- **Waits explícitos** con `WebDriverWait` (sin `Thread.sleep` para esperas de elementos)
- **Constructores privados** en clases utilitarias (previene instanciación)
- **Javadoc** en todas las clases y métodos públicos
- **Separación de responsabilidades** (pages, steps, utils, runners)
- **Screenshots automáticos** en caso de fallo (evidencia)
- **Cucumber Hooks** para setup/teardown del ciclo de vida
- **Tag Strategy** con terminología QA estándar (happy-path, unhappy-path, smoke, e2e)
- **Data-Driven Testing** con `Scenario Outline` + `Examples`
- **Reintentos automáticos** para carga de página (resiliencia)

---

## 🧪 Escenarios de Prueba

### Happy Path (2 Scenario Outlines con Examples)
1. **Cotizar SOAT editando datos del vehículo** → Alterna marcas (TOYOTA, HYUNDAI) y modelos (YARIS, ACCENT)
2. **Cotizar SOAT sin editar datos** → Selecciona plan más económico directamente

### Unhappy Path (2 Scenario Outlines con Examples)
3. **Placa con formato inválido** → ABC, 12345 (boundary values – menos de 6 caracteres)
4. **Placa vacía o caracteres especiales** → "", @#$ (input validation)

### Pantallas Cubiertas
- **Pantalla 1:** Alternar entre marcas y modelos de carros (modal de edición)
- **Pantalla 2:** Elegir el seguro más económico (SOAT Básico)
- **Pantalla 3:** Capturar imagen de la sección "Resumen de tu Compra"

### 🏷 Estrategia de Tags

| Tag | Tipo | Propósito |
|---|---|---|
| `@regression` | Suite | Suite de regresión completa (nivel Feature) |
| `@smoke` | Suite | Pruebas críticas mínimas para CI/CD |
| `@happy-path` | Flujo | Flujos exitosos del usuario |
| `@unhappy-path` | Flujo | Flujos con errores y validaciones |
| `@e2e` | Alcance | End-to-End completo (landing → pago) |
| `@boundary` | Técnica | Valores límite / particiones de equivalencia |
| `@validation` | Técnica | Validaciones de entrada y reglas de negocio |
| `@pantalla1-3` | Pantalla | Filtrar por pantalla/paso específico |

---

## 🚀 Ejecución

### Prerrequisitos
- Java 17+
- Maven 3.6+
- Google Chrome instalado

### Comandos

```bash
# Ejecutar todos los escenarios (regression suite)
mvn clean test

# Solo Happy Path (flujos exitosos)
mvn test -Dcucumber.filter.tags="@happy-path"

# Solo Unhappy Path (validaciones y errores)
mvn test -Dcucumber.filter.tags="@unhappy-path"

# Solo Smoke Tests (escenarios críticos)
mvn test -Dcucumber.filter.tags="@smoke"

# Solo End-to-End (flujo completo)
mvn test -Dcucumber.filter.tags="@e2e"

# Solo escenarios que editan marca/modelo (Pantalla 1)
mvn test -Dcucumber.filter.tags="@pantalla1"
```

### Reportes

Tras la ejecución, los reportes se encuentran en:

| Tipo | Ubicación |
|---|---|
| Reporte HTML | `target/cucumber-reports/report.html` |
| Reporte JSON | `target/cucumber-reports/report.json` |
| Screenshots | `target/screenshots/` |

Abrir el reporte HTML en el navegador:
```bash
start target\cucumber-reports\report.html
```

---

## 🤖 Uso de Inteligencia Artificial

Se utilizó **GitHub Copilot (Claude)** como herramienta de asistencia en:

- Diseño de escenarios BDD en Gherkin
- Generación de locators del DOM
- Implementación de interacciones con componentes Vue.js
- Configuración del framework y dependencias Maven
- Estrategia de waits y manejo de dropdowns custom

Cada archivo contiene la etiqueta `[IA - GitHub Copilot]` indicando específicamente dónde se aplicó IA.

---

## 👤 Autor - Andry

Reto técnico - QA Automation para Interseguro  
Febrero 2026
