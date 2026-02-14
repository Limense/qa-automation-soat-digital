package com.interseguro.soat.pages;

import com.interseguro.soat.utils.ConfigManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Clase base abstracta para todos los Page Objects.
 * Centraliza la inicialización del WebDriver, WebDriverWait y
 * métodos utilitarios comunes (scroll, pause, JS executor).
 *
 * Patrón: Template Method + Page Object Model
 *
 * [IA - GitHub Copilot]: Se utilizó IA para diseñar la clase base
 * aplicando herencia y reutilización de código entre Page Objects.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final ConfigManager config;

    /**
     * Constructor base que inicializa el driver, wait y PageFactory.
     *
     * @param driver WebDriver activo
     */
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.config = ConfigManager.getInstance();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        PageFactory.initElements(driver, this);
    }

    // ==================== MÉTODOS UTILITARIOS COMUNES ====================

    /**
     * Ejecuta código JavaScript en el contexto del navegador.
     *
     * @param script Script JS a ejecutar
     * @param args   Argumentos opcionales del script
     * @return Resultado de la ejecución del script
     */
    protected Object executeJs(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Hace scroll suave hasta un elemento para asegurar su visibilidad.
     *
     * @param element Elemento al que hacer scroll
     */
    protected void scrollToElement(WebElement element) {
        executeJs("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        pause(500);
    }

    /**
     * Pausa la ejecución por un tiempo determinado.
     * Necesario para esperar animaciones y actualizaciones de componentes Vue.js.
     *
     * @param millis Milisegundos a esperar
     */
    protected void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Obtiene la URL actual del navegador.
     *
     * @return URL actual
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
