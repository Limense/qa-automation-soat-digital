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

### Patrones y Buenas Prácticas Aplicadas

- **Page Object Model (POM)** con `BasePage` abstracta
- **Singleton con ThreadLocal** para WebDriver (thread-safe)
- **Configuración externalizada** en `config.properties`
- **Waits explícitos** (sin Thread.sleep para esperas de elementos)
- **Constructores privados** en clases utilitarias
- **Javadoc** en todas las clases y métodos públicos
- **Separación de responsabilidades** (pages, steps, utils, runners)
- **Screenshots automáticos** en caso de fallo
- **Reintentos automáticos** para carga de página

---

## 🧪 Escenarios de Prueba

### Positivos (2 Scenario Outlines con Examples)
1. **Cotizar SOAT editando datos del vehículo** → Alterna marcas (TOYOTA, HYUNDAI) y modelos (YARIS, ACCENT)
2. **Cotizar SOAT sin editar datos** → Selecciona plan más económico directamente

### Negativos (2 Scenario Outlines con Examples)
3. **Placa con formato inválido** → ABC, 12345 (menos de 6 caracteres)
4. **Placa vacía o caracteres especiales** → "", @#$

### Pantallas Cubiertas
- **Pantalla 1:** Alternar entre marcas y modelos de carros (modal de edición)
- **Pantalla 2:** Elegir el seguro más económico (SOAT Básico)
- **Pantalla 3:** Capturar imagen de la sección "Resumen de tu Compra"

---

## 🚀 Ejecución

### Prerrequisitos
- Java 17+
- Maven 3.6+
- Google Chrome instalado

### Comandos

```bash
# Ejecutar todos los escenarios
mvn clean test

# Solo escenarios positivos
mvn test -Dcucumber.filter.tags="@positivo"

# Solo escenarios negativos
mvn test -Dcucumber.filter.tags="@negativo"

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

## 👤 Autor

Reto técnico - QA Automation para Interseguro  
Febrero 2026
