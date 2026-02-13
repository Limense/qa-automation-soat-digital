package com.interseguro.soat.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Runner principal para ejecutar los escenarios de prueba con Cucumber y JUnit 5.
 * Configura el motor de Cucumber, la ubicación de features y el glue code.
 *
 * Ejecución:
 *   mvn test                              → Ejecuta todos los escenarios
 *   mvn test -Dcucumber.filter.tags="@positivo"  → Solo escenarios positivos
 *   mvn test -Dcucumber.filter.tags="@negativo"  → Solo escenarios negativos
 *
 * [IA - GitHub Copilot]: Se utilizó IA para la configuración del runner
 * con JUnit Platform Suite y Cucumber Engine, incluyendo reportes HTML y JSON.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, html:target/cucumber-reports/report.html, json:target/cucumber-reports/report.json"
)
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.interseguro.soat.steps"
)
@ConfigurationParameter(
        key = FILTER_TAGS_PROPERTY_NAME,
        value = "not @ignore"
)
public class TestRunner {
    // Clase vacía - la configuración se realiza mediante anotaciones
}
