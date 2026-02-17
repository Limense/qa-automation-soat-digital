package com.interseguro.soat.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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
public class PlanSelectionPage extends BasePage {

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
        super(driver);
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
        pause(500);
    }

    /**
     * Selecciona una marca del dropdown custom de Vue.js.
     * Abre el dropdown, busca la marca en el campo de búsqueda y la selecciona.
     *
     * @param marca Nombre de la marca a seleccionar (ej: TOYOTA)
     */
    public void selectMarca(String marca) {
        selectFromDropdown(inputMarca, marca);
    }

    /**
     * Selecciona un modelo del dropdown custom de Vue.js.
     * El dropdown de modelo se actualiza según la marca seleccionada previamente.
     *
     * @param modelo Nombre del modelo a seleccionar (ej: YARIS)
     */
    public void selectModelo(String modelo) {
        // Esperar a que el dropdown de modelo se actualice tras seleccionar la marca
        pause(1200);
        selectFromDropdown(inputModelo, modelo);
    }

    /**
     * Hace clic en "Guardar cambios" y espera a que el modal de edición se cierre.
     */
    public void clickGuardarCambios() {
        wait.until(ExpectedConditions.elementToBeClickable(btnGuardarCambios));
        btnGuardarCambios.click();
        // Esperar a que el modal se cierre (el input make ya no sea visible)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("make")));
        pause(800);
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
        pause(500);
    }

    // ==================== ACCIONES - Renovación y Continuar ====================

    /**
     * Gestiona la sección de renovación automática seleccionando "NO ACTIVAR".
     * Si la sección no aparece, continúa sin error.
     */
    public void handleRenewalPopup() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement noActivarBtn = shortWait.until(
                    ExpectedConditions.elementToBeClickable(btnNoActivar));
            scrollToElement(noActivarBtn);
            noActivarBtn.click();
            pause(300);
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
     * Selecciona una opción de un dropdown custom Vue.js.
     * Estrategia multi-fallback:
     *   A) Busca elemento con texto directo exacto (text())
     *   B) Busca elemento que contiene el texto (contains)
     *   C) Presiona Enter en el campo de búsqueda como último recurso
     *
     * [IA - GitHub Copilot]: Lógica de selección generada con IA para manejar
     * dropdowns custom de Vue.js con campo de búsqueda y opciones dinámicas.
     *
     * @param triggerInput El campo input readonly que abre el dropdown
     * @param value        El valor a seleccionar (ej: TOYOTA)
     */
    private void selectFromDropdown(WebElement triggerInput, String value) {
        // 1. Abrir el dropdown con reintentos (necesario cuando el componente Vue recarga modelos)
        wait.until(ExpectedConditions.elementToBeClickable(triggerInput));

        By searchSelector = By.xpath(
                "//input[(@placeholder='buscar' or @placeholder='Buscar' or @type='search') " +
                "and not(@readonly)]"
        );
        By fallbackSelector = By.xpath(
                "//input[@type='text' and not(@readonly) and not(@id='make') " +
                "and not(@id='model') and not(@id='plate')]"
        );

        WebElement searchInput = null;
        // Polling: intentar abrir el dropdown hasta 8 veces con diferentes métodos de clic
        for (int attempt = 0; attempt < 8 && searchInput == null; attempt++) {
            try {
                // Alternar entre métodos de clic
                switch (attempt % 4) {
                    case 0: triggerInput.click(); break;
                    case 1: executeJs("arguments[0].click()", triggerInput); break;
                    case 2: triggerInput.findElement(By.xpath("./..")).click(); break;
                    case 3: new org.openqa.selenium.interactions.Actions(driver)
                                .moveToElement(triggerInput).click().perform(); break;
                }
            } catch (Exception clickErr) {
                System.out.println("[Dropdown] Error en clic intento " + (attempt + 1) + ": " + clickErr.getMessage());
            }

            pause(800);

            // Verificar si el search input apareció
            List<WebElement> candidates = driver.findElements(searchSelector);
            if (candidates.isEmpty()) {
                candidates = driver.findElements(fallbackSelector);
            }
            for (WebElement c : candidates) {
                try {
                    if (c.isDisplayed()) {
                        searchInput = c;
                        break;
                    }
                } catch (StaleElementReferenceException ignored) {}
            }

            if (searchInput == null) {
                System.out.println("[Dropdown] Intento " + (attempt + 1) + "/8: dropdown no se abrió para '" + value + "'");
            }
        }

        if (searchInput == null) {
            throw new RuntimeException("[Dropdown] No se pudo abrir el dropdown después de 8 intentos para: " + value);
        }

        System.out.println("[Dropdown] Dropdown abierto correctamente para '" + value + "'");

        // 2. Escribir el valor en el campo de búsqueda
        searchInput.click();
        searchInput.sendKeys(Keys.CONTROL, "a");
        searchInput.sendKeys(Keys.DELETE);
        searchInput.sendKeys(value);
        pause(800);

        // 3. Intentar encontrar y hacer clic en la opción filtrada
        boolean selected = false;

        // Estrategia A: Texto directo exacto - normalize-space(text())='VALOR'
        if (!selected) {
            List<WebElement> candidates = driver.findElements(
                    By.xpath("//*[normalize-space(text())='" + value + "']")
            );
            for (WebElement el : candidates) {
                String tag = el.getTagName().toLowerCase();
                if (!tag.equals("input") && !tag.equals("label") && el.isDisplayed()) {
                    executeJs("arguments[0].click()", el);
                    selected = true;
                    System.out.println("[Dropdown] Seleccionado '" + value + "' con estrategia A (texto exacto)");
                    break;
                }
            }
        }

        // Estrategia B: Texto que contiene el valor - contains(text(), 'VALOR')
        if (!selected) {
            List<WebElement> candidates = driver.findElements(
                    By.xpath("//*[contains(normalize-space(text()),'" + value + "')]")
            );
            for (WebElement el : candidates) {
                String tag = el.getTagName().toLowerCase();
                if (!tag.equals("input") && !tag.equals("label") && el.isDisplayed()) {
                    executeJs("arguments[0].click()", el);
                    selected = true;
                    System.out.println("[Dropdown] Seleccionado '" + value + "' con estrategia B (contains)");
                    break;
                }
            }
        }

        // Estrategia C: Presionar Enter en el campo de búsqueda
        if (!selected) {
            System.out.println("[Dropdown] Estrategias A y B fallaron para '" + value + "', intentando Enter");
            searchInput.sendKeys(Keys.ENTER);
        }

        pause(500);
    }
}
