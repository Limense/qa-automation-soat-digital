package com.interseguro.soat.pages;

import com.interseguro.soat.utils.ScreenshotHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object: Página de pago (Paso 2/2).
 * Contiene los elementos y acciones para:
 * - Verificar la sección "Resumen de compra"
 * - Capturar en imagen la sección Resumen de tu Compra (Pantalla 3)
 *
 * [IA - GitHub Copilot]: Se utilizó IA para la implementación de la
 * captura de screenshot del elemento "Resumen de Compra" usando
 * las capacidades de Selenium 4 para screenshots de elementos.
 */
public class PaymentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ==================== LOCATORS ====================

    /** Etiqueta "Resumen de compra:" en el panel lateral derecho */
    @FindBy(xpath = "//p[contains(text(),'Resumen de compra')]")
    private WebElement lblResumenCompra;

    // ==================== CONSTRUCTOR ====================

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // ==================== ACCIONES ====================

    /**
     * Espera a que la página de pago (Paso 2/2) cargue completamente.
     */
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOf(lblResumenCompra));
    }

    /**
     * Verifica que la sección "Resumen de compra" esté visible.
     *
     * @return true si la sección es visible
     */
    public boolean isResumenCompraVisible() {
        try {
            return lblResumenCompra.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el contenedor principal de la sección "Resumen de compra".
     * Busca el elemento padre que contiene toda la información del resumen.
     *
     * @return WebElement del contenedor del resumen
     */
    public WebElement getResumenCompraSection() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Resumen de compra')]" +
                         "/ancestor::div[contains(@class,'rounded') or " +
                         "contains(@class,'shadow') or " +
                         "contains(@class,'border') or " +
                         "contains(@class,'bg-white')][1]")
        ));
    }

    /**
     * Captura un screenshot de la sección "Resumen de tu Compra".
     * Intenta capturar el contenedor específico; si falla, captura la página completa.
     *
     * @return La ruta del archivo de imagen generado
     */
    public String captureResumenCompra() {
        waitForPageLoad();
        try {
            // Intentar capturar el contenedor completo del resumen
            WebElement resumenSection = getResumenCompraSection();
            return ScreenshotHelper.captureElement(resumenSection, "resumen_de_compra");
        } catch (Exception e) {
            System.out.println("[Info] No se pudo capturar el elemento específico, " +
                             "capturando página completa como respaldo...");
            return ScreenshotHelper.captureFullPage(driver, "resumen_de_compra_fullpage");
        }
    }
}
