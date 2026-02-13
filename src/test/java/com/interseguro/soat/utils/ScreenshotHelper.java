package com.interseguro.soat.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para la captura de screenshots durante la ejecución de pruebas.
 * Soporta captura a nivel de elemento específico y página completa.
 *
 * [IA - GitHub Copilot]: Se utilizó IA para implementar la captura de
 * screenshots aprovechando las capacidades de Selenium 4 para elementos
 * individuales y como fallback la captura de página completa.
 */
public class ScreenshotHelper {

    private static final String SCREENSHOTS_DIR = "target/screenshots/";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotHelper() {
        // Constructor privado - clase utilitaria
    }

    /**
     * Captura un screenshot de un elemento web específico (Selenium 4).
     *
     * @param element  El WebElement a capturar
     * @param fileName Nombre base del archivo
     * @return La ruta del archivo generado
     */
    public static String captureElement(WebElement element, String fileName) {
        try {
            createDirectoryIfNotExists();
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String filePath = SCREENSHOTS_DIR + fileName + "_" + timestamp + ".png";

            // Selenium 4 soporta screenshots a nivel de elemento
            File screenshot = element.getScreenshotAs(OutputType.FILE);
            Path destination = Paths.get(filePath);
            Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("[Screenshot] Elemento capturado en: " + filePath);
            return filePath;
        } catch (IOException e) {
            System.err.println("[Screenshot] Error al capturar elemento: " + e.getMessage());
            // Fallback: capturar página completa
            return captureFullPage(DriverFactory.getDriver(), fileName + "_fullpage");
        }
    }

    /**
     * Captura un screenshot de la página completa.
     *
     * @param driver   WebDriver activo
     * @param fileName Nombre base del archivo
     * @return La ruta del archivo generado
     */
    public static String captureFullPage(WebDriver driver, String fileName) {
        try {
            createDirectoryIfNotExists();
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String filePath = SCREENSHOTS_DIR + fileName + "_" + timestamp + ".png";

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destination = Paths.get(filePath);
            Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("[Screenshot] Página capturada en: " + filePath);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("[Screenshot] Error al capturar página: " + e.getMessage(), e);
        }
    }

    /**
     * Crea el directorio de screenshots si no existe.
     */
    private static void createDirectoryIfNotExists() throws IOException {
        Path dir = Paths.get(SCREENSHOTS_DIR);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
    }
}
