package com.interseguro.soat.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object: Página de selección de planes SOAT (Paso 1/2).
 * Contiene los elementos y acciones para:
 * - Editar datos del vehículo (Pantalla 1: alternar marcas y modelos)
 * - Seleccionar el plan más económico (Pantalla 2)
 *
 * URL: https://test.interseguro.pe/soat-digital/cotizacion/planes
 *
 * [IA - GitHub Copilot]: Se utilizó IA para la generación de locators,
 * la lógica de interacción con dropdowns custom de Vue.js y la
 * estrategia de waits explícitos para elementos dinámicos.
 */
public class PlanSelectionPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ==================== LOCATORS - Edición de Vehículo ====================

    /** Botón "Editar" para abrir modal de datos del vehículo */
    @FindBy(xpath = "//span[contains(text(),'Editar') and contains(@class,'text-primary')]")
    private WebElement btnEditar;

    /** Campo de Marca (readonly - abre dropdown custom al hacer clic) */
    @FindBy(id = "make")
    private WebElement inputMarca;

    /** Campo de Modelo (readonly - abre dropdown custom al hacer clic) */
    @FindBy(id = "model")
    private WebElement inputModelo;

    /** Botón "Guardar cambios" dentro del modal de edición */
    @FindBy(xpath = "//span[text()='Guardar cambios']/ancestor::button")
    private WebElement btnGuardarCambios;

    /** Enlace "Cancelar" dentro del modal de edición */
    @FindBy(xpath = "//span[contains(text(),'Cancelar')]")
    private WebElement btnCancelar;

    // ==================== LOCATORS - Selección de Plan ====================

    /** Primer botón "Seleccionar plan" (SOAT Básico = más económico) */
    @FindBy(xpath = "(//span[contains(text(),'Seleccionar plan')]/ancestor::button)[1]")
    private WebElement btnSeleccionarPlanBasico;

    // ==================== LOCATORS - Renovación ====================

    /** Botón "NO ACTIVAR" en la sección de renovación automática */
    @FindBy(xpath = "//button[contains(text(),'NO ACTIVAR')]")
    private WebElement btnNoActivar;

    // ==================== LOCATORS - Continuar ====================

    /** Botón "CONTINUAR CON S/XX" para avanzar al paso de pago */
    @FindBy(xpath = "//button[contains(text(),'CONTINUAR CON')]")
    private WebElement btnContinuar;

    // ==================== CONSTRUCTOR ====================

    public PlanSelectionPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // ==================== ACCIONES - Carga de Página ====================

    /**
     * Espera a que la página de planes cargue completamente (Paso 1/2).
     */
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.urlContains("cotizacion/planes"));
        // Esperar a que al menos el botón Editar o los planes sean visibles
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(btnEditar),
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//span[contains(text(),'Seleccionar plan')]"))
        ));
    }

    // ==================== ACCIONES - Edición de Vehículo (Pantalla 1) ====================

    /**
     * Hace clic en "Editar" para abrir el modal de edición del vehículo.
     */
    public void clickEditar() {
        wait.until(ExpectedConditions.elementToBeClickable(btnEditar));
        btnEditar.click();
        // Esperar a que el modal se abra (verificar que el input de marca sea visible)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("make")));
        pause(1000);
    }

    /**
     * Selecciona una marca del dropdown custom de Vue.js.
     * Abre el dropdown, busca la marca en el campo de búsqueda y la selecciona.
     *
     * @param marca Nombre de la marca a seleccionar (ej: TOYOTA)
     */
    public void selectMarca(String marca) {
        // Clic en el campo Marca para abrir el dropdown
        wait.until(ExpectedConditions.elementToBeClickable(inputMarca));
        inputMarca.click();

        // Esperar y usar el campo de búsqueda dentro del dropdown
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='buscar']")
        ));
        searchInput.clear();
        searchInput.sendKeys(marca);

        // Esperar que las opciones se filtren y seleccionar la marca
        pause(500);
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space(text())='" + marca + "'] | " +
                         "//div[normalize-space(text())='" + marca + "' " +
                         "and not(contains(@class,'input'))][not(ancestor::label)] | " +
                         "//span[normalize-space(text())='" + marca + "']/ancestor::li")
        ));
        option.click();
        pause(500);
    }

    /**
     * Selecciona un modelo del dropdown custom de Vue.js.
     * El dropdown de modelo se actualiza según la marca seleccionada previamente.
     *
     * @param modelo Nombre del modelo a seleccionar (ej: YARIS)
     */
    public void selectModelo(String modelo) {
        // Esperar a que el dropdown de modelo se actualice tras seleccionar la marca
        pause(1000);

        // Clic en el campo Modelo para abrir el dropdown
        wait.until(ExpectedConditions.elementToBeClickable(inputModelo));
        inputModelo.click();

        // Usar el campo de búsqueda dentro del dropdown
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='buscar']")
        ));
        searchInput.clear();
        searchInput.sendKeys(modelo);

        // Esperar que las opciones se filtren y seleccionar el modelo
        pause(500);
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space(text())='" + modelo + "'] | " +
                         "//div[normalize-space(text())='" + modelo + "' " +
                         "and not(contains(@class,'input'))][not(ancestor::label)] | " +
                         "//span[normalize-space(text())='" + modelo + "']/ancestor::li")
        ));
        option.click();
        pause(500);
    }

    /**
     * Hace clic en "Guardar cambios" y espera a que el modal de edición se cierre.
     */
    public void clickGuardarCambios() {
        wait.until(ExpectedConditions.elementToBeClickable(btnGuardarCambios));
        btnGuardarCambios.click();
        // Esperar a que el modal se cierre (el input make ya no sea visible)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("make")));
        pause(1500);
    }

    // ==================== ACCIONES - Selección de Plan (Pantalla 2) ====================

    /**
     * Selecciona el plan de seguro más económico (SOAT Básico).
     * El SOAT Básico es siempre el primer plan y el más económico.
     */
    public void selectPlanMasEconomico() {
        // Scroll hasta la sección de planes
        scrollToElement(btnSeleccionarPlanBasico);
        wait.until(ExpectedConditions.elementToBeClickable(btnSeleccionarPlanBasico));
        btnSeleccionarPlanBasico.click();
        pause(1000);
    }

    // ==================== ACCIONES - Renovación y Continuar ====================

    /**
     * Gestiona la sección de renovación automática seleccionando "NO ACTIVAR".
     * Si la sección no aparece, continúa sin error.
     */
    public void handleRenewalPopup() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement noActivarBtn = shortWait.until(
                    ExpectedConditions.elementToBeClickable(btnNoActivar));
            scrollToElement(noActivarBtn);
            noActivarBtn.click();
            pause(500);
        } catch (TimeoutException | NoSuchElementException e) {
            System.out.println("[Info] Sección de renovación no encontrada, continuando...");
        }
    }

    /**
     * Hace clic en el botón "CONTINUAR CON S/XX" para avanzar al paso de pago.
     */
    public void clickContinuar() {
        scrollToElement(btnContinuar);
        wait.until(ExpectedConditions.elementToBeClickable(btnContinuar));
        btnContinuar.click();
    }

    // ==================== UTILIDADES PRIVADAS ====================

    /**
     * Hace scroll suave hasta un elemento para asegurar su visibilidad.
     */
    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        pause(500);
    }

    /**
     * Pausa la ejecución por un tiempo determinado.
     * Necesario para esperar animaciones y actualizaciones de dropdowns Vue.js.
     */
    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
