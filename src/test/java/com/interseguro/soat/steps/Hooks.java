package com.interseguro.soat.steps;

import com.interseguro.soat.utils.DriverFactory;
import com.interseguro.soat.utils.ScreenshotHelper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Hooks de Cucumber para configuración y limpieza de cada escenario.
 * Se ejecutan automáticamente antes y después de cada escenario.
 *
 * Funcionalidades:
 * - Inicialización del WebDriver antes de cada escenario
 * - Captura de screenshot embebido en el reporte Cucumber (pass y fail)
 * - Cierre del navegador después de cada escenario
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
    public void setUp(Scenario scenario) {
        System.out.println("========================================");
        System.out.println("[Setup] Escenario: " + scenario.getName());
        System.out.println("[Setup] Inicializando navegador Chrome...");
        System.out.println("========================================");
        DriverFactory.getDriver();
    }

    /**
     * Se ejecuta DESPUÉS de cada escenario.
     * Embebe screenshot en el reporte Cucumber HTML y cierra el navegador.
     *
     * @param scenario Información del escenario ejecutado
     */
    @After
    public void tearDown(Scenario scenario) {
        try {
            WebDriver driver = DriverFactory.getDriver();

            // Embeber screenshot en el reporte Cucumber HTML (visible en report.html)
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png",
                    scenario.isFailed() ? "FALLO - " + scenario.getName() : "EVIDENCIA - " + scenario.getName());

            // Si falló, guardar también en disco
            if (scenario.isFailed()) {
                String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
                String screenshotPath = ScreenshotHelper.captureFullPage(driver, "FALLO_" + scenarioName);
                System.out.println("[FALLO] Screenshot guardado en: " + screenshotPath);
            }

            // Log del resultado
            System.out.println("========================================");
            System.out.println("[Resultado] " + scenario.getName() + " → " + scenario.getStatus());
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("[Error] No se pudo capturar screenshot: " + e.getMessage());
        } finally {
            System.out.println("[Teardown] Cerrando navegador...");
            DriverFactory.quitDriver();
        }
    }
}
