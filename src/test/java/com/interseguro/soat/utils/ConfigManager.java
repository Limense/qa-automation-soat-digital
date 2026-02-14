package com.interseguro.soat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gestor centralizado de configuración del framework.
 * Carga las propiedades desde config.properties y proporciona acceso
 * tipado a cada parámetro de configuración.
 *
 * Patrón: Singleton + Carga Lazy
 *
 * [IA - GitHub Copilot]: Se utilizó IA para implementar la carga de
 * propiedades con valores por defecto y el patrón de acceso tipado.
 */
public final class ConfigManager {

    private static final String CONFIG_FILE = "config.properties";
    private static ConfigManager instance;
    private final Properties properties;

    private ConfigManager() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("[Config] Archivo " + CONFIG_FILE + " no encontrado, usando valores por defecto.");
            }
        } catch (IOException e) {
            System.err.println("[Config] Error al leer " + CONFIG_FILE + ": " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única del ConfigManager.
     *
     * @return Instancia del ConfigManager
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    // ==================== Propiedades de URL ====================

    /** @return URL base de la aplicación SOAT Digital */
    public String getBaseUrl() {
        return properties.getProperty("app.base.url", "https://test.interseguro.pe/soat-digital/");
    }

    // ==================== Propiedades del Browser ====================

    /** @return Navegador a utilizar (chrome, firefox, edge) */
    public String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    /** @return true si debe ejecutar en modo headless */
    public boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("browser.headless", "false"));
    }

    /** @return true si debe maximizar la ventana del navegador */
    public boolean isMaximize() {
        return Boolean.parseBoolean(properties.getProperty("browser.maximize", "true"));
    }

    // ==================== Propiedades de Timeouts ====================

    /** @return Timeout implícito en segundos */
    public int getImplicitTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.implicit", "10"));
    }

    /** @return Timeout de waits explícitos en segundos */
    public int getExplicitTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.explicit", "20"));
    }

    /** @return Timeout de carga de página en segundos */
    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.page.load", "60"));
    }

    // ==================== Propiedades de Screenshots ====================

    /** @return Directorio de almacenamiento de screenshots */
    public String getScreenshotsDir() {
        return properties.getProperty("screenshots.dir", "target/screenshots/");
    }

    /** @return true si debe capturar screenshot en caso de fallo */
    public boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(properties.getProperty("screenshots.on.failure", "true"));
    }

    // ==================== Propiedades de Reintentos ====================

    /** @return Número máximo de reintentos para carga de página */
    public int getMaxRetryAttempts() {
        return Integer.parseInt(properties.getProperty("retry.max.attempts", "2"));
    }

    /** @return Tiempo de espera entre reintentos en milisegundos */
    public int getRetryDelayMs() {
        return Integer.parseInt(properties.getProperty("retry.delay.ms", "2000"));
    }
}
