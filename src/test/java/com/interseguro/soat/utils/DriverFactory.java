package com.interseguro.soat.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Factoría para la gestión del WebDriver (Singleton con ThreadLocal).
 * Configura e inicializa el navegador Chrome para las pruebas automatizadas.
 *
 * [IA - GitHub Copilot]: Se utilizó IA para generar la configuración óptima
 * del ChromeDriver incluyendo opciones de rendimiento y estabilidad.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
        // Constructor privado - patrón Singleton
    }

    /**
     * Obtiene la instancia del WebDriver. Si no existe, crea una nueva.
     *
     * @return WebDriver configurado con Chrome
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            WebDriverManager.chromedriver().setup();
            ConfigManager config = ConfigManager.getInstance();

            ChromeOptions options = new ChromeOptions();
            if (config.isMaximize()) {
                options.addArguments("--start-maximized");
            }
            if (config.isHeadless()) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled");

            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitTimeout()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
            driver.manage().window().maximize();

            driverThreadLocal.set(driver);
        }
        return driverThreadLocal.get();
    }

    /**
     * Cierra el navegador y limpia la instancia del driver.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
