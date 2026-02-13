package com.interseguro.soat.steps;

import com.interseguro.soat.utils.DriverFactory;
import com.interseguro.soat.utils.ScreenshotHelper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Hooks de Cucumber para configuración y limpieza de cada escenario.
 * Se ejecutan automáticamente antes y después de cada escenario.
 *
 * [IA - GitHub Copilot]: Se utilizó IA para implementar los hooks
 * de setup/teardown y la captura automática de screenshots en caso de fallo.
 */
public class Hooks {

    /**
     * Se ejecuta ANTES de cada escenario.
     * Inicializa el WebDriver.
     */
    @Before
    public void setUp() {
        System.out.println("========================================");
        System.out.println("[Setup] Inicializando navegador Chrome...");
        System.out.println("========================================");
        DriverFactory.getDriver();
    }

    /**
     * Se ejecuta DESPUÉS de cada escenario.
     * Captura screenshot si el escenario falló y cierra el navegador.
     *
     * @param scenario Información del escenario ejecutado
     */
    @After
    public void tearDown(Scenario scenario) {
        try {
            // Si el escenario falló, capturar evidencia
            if (scenario.isFailed()) {
                String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
                String screenshotPath = ScreenshotHelper.captureFullPage(
                        DriverFactory.getDriver(),
                        "FALLO_" + scenarioName
                );
                System.out.println("[FALLO] Screenshot de evidencia: " + screenshotPath);
            }
        } catch (Exception e) {
            System.err.println("[Error] No se pudo capturar screenshot de fallo: " + e.getMessage());
        } finally {
            // Cerrar el navegador después de cada escenario
            System.out.println("========================================");
            System.out.println("[Teardown] Cerrando navegador...");
            System.out.println("========================================");
            DriverFactory.quitDriver();
        }
    }
}
