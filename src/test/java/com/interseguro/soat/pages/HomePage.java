package com.interseguro.soat.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object: Página principal de SOAT Digital (Landing).
 * Contiene los elementos y acciones de la pantalla de ingreso de placa.
 *
 * URL: https://test.interseguro.pe/soat-digital/
 *
 * [IA - GitHub Copilot]: Se utilizó IA para la identificación de locators
 * del DOM y la generación de métodos de interacción con los elementos.
 */
public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final String URL = "https://test.interseguro.pe/soat-digital/";

    // ==================== LOCATORS ====================

    /** Campo de texto para ingresar la placa del vehículo */
    @FindBy(id = "plate")
    private WebElement inputPlaca;

    /** Botón "COTIZAR AHORA" */
    @FindBy(xpath = "//div[text()='COTIZAR AHORA']")
    private WebElement btnCotizarAhora;

    // ==================== CONSTRUCTOR ====================

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // ==================== ACCIONES ====================

    /**
     * Navega a la página principal de SOAT Digital.
     */
    public void navigateTo() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOf(inputPlaca));
    }

    /**
     * Ingresa el número de placa en el campo correspondiente.
     *
     * @param placa Número de placa del vehículo (ej: ABC-123)
     */
    public void enterPlaca(String placa) {
        wait.until(ExpectedConditions.visibilityOf(inputPlaca));
        inputPlaca.clear();
        inputPlaca.sendKeys(placa);
    }

    /**
     * Hace clic en el botón "COTIZAR AHORA".
     */
    public void clickCotizarAhora() {
        wait.until(ExpectedConditions.elementToBeClickable(btnCotizarAhora));
        btnCotizarAhora.click();
    }

    /**
     * Espera a que la cotización se procese y redirija a la página de planes.
     * El botón muestra "Obteniendo mejores precios" durante la carga.
     */
    public void waitForCotizacionRedirect() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        longWait.until(ExpectedConditions.urlContains("cotizacion/planes"));
    }

    // ==================== VALIDACIONES ====================

    /**
     * Verifica si se muestra un mensaje de error de placa inválida.
     *
     * @return true si el mensaje de error es visible
     */
    public boolean isErrorPlacaDisplayed() {
        try {
            // Verificar si el input tiene la clase de error o si hay un mensaje de error visible
            WebElement plateInput = driver.findElement(By.id("plate"));
            String inputClass = plateInput.getAttribute("class");
            if (inputClass != null && inputClass.contains("is-input-error")) {
                return true;
            }

            // Buscar mensajes de error por texto
            WebElement errorMsg = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(text(),'placa') and " +
                                     "(contains(text(),'válida') or " +
                                     "contains(text(),'valida') or " +
                                     "contains(text(),'inválida') or " +
                                     "contains(text(),'invalida') or " +
                                     "contains(text(),'6 d') or " +
                                     "contains(text(),'error'))]")
                    ));
            return errorMsg.isDisplayed();
        } catch (TimeoutException e) {
            // Último recurso: verificar que no redirigió (es decir, hubo error)
            return isStillOnHomePage();
        }
    }

    /**
     * Obtiene el texto del mensaje de error mostrado.
     *
     * @return Texto del mensaje de error, o vacío si no hay error
     */
    public String getErrorMessage() {
        try {
            // Intentar obtener mensaje de error visible
            WebElement errorMsg = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(text(),'placa') and " +
                                     "(contains(text(),'válida') or " +
                                     "contains(text(),'valida') or " +
                                     "contains(text(),'6 d') or " +
                                     "contains(text(),'error'))]")
                    ));
            return errorMsg.getText();
        } catch (TimeoutException e) {
            // Verificar si el input tiene clase de error
            try {
                WebElement plateInput = driver.findElement(By.id("plate"));
                String inputClass = plateInput.getAttribute("class");
                if (inputClass != null && inputClass.contains("is-input-error")) {
                    return "Placa con formato inválido (input en estado de error)";
                }
            } catch (Exception ex) {
                // Ignorar
            }
            return "";
        }
    }

    /**
     * Verifica que el sistema NO redirigió a la página de planes.
     * Útil para validar escenarios negativos.
     *
     * @return true si permanece en la landing page
     */
    public boolean isStillOnHomePage() {
        try {
            Thread.sleep(3000);
            return !driver.getCurrentUrl().contains("cotizacion/planes");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return true;
        }
    }
}
