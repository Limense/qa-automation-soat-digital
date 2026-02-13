package com.interseguro.soat.steps;

import com.interseguro.soat.pages.HomePage;
import com.interseguro.soat.pages.PaymentPage;
import com.interseguro.soat.pages.PlanSelectionPage;
import com.interseguro.soat.utils.DriverFactory;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step Definitions para los escenarios de cotización SOAT Digital.
 * Implementa los pasos definidos en el feature file soat_cotizacion.feature.
 *
 * [IA - GitHub Copilot]: Se utilizó IA para la generación de step definitions,
 * la parametrización con Cucumber Expressions y las validaciones con JUnit 5 Assertions.
 */
public class SoatSteps {

    private WebDriver driver;
    private HomePage homePage;
    private PlanSelectionPage planSelectionPage;
    private PaymentPage paymentPage;

    // ==================== ANTECEDENTES (Background) ====================

    @Dado("que el usuario ingresa a la página de SOAT Digital")
    public void elUsuarioIngresaALaPaginaDeSOATDigital() {
        driver = DriverFactory.getDriver();
        homePage = new HomePage(driver);
        homePage.navigateTo();
    }

    // ==================== ACCIONES (When/And) ====================

    @Cuando("ingresa la placa {string} y hace clic en Cotizar Ahora")
    public void ingresaLaPlacaYHaceClicEnCotizarAhora(String placa) {
        homePage.enterPlaca(placa);
        homePage.clickCotizarAhora();
    }

    @Y("edita los datos del vehículo seleccionando marca {string} y modelo {string}")
    public void editaLosDatosDelVehiculoSeleccionandoMarcaYModelo(String marca, String modelo) {
        // Esperar a que la página de planes cargue tras la cotización
        planSelectionPage = new PlanSelectionPage(driver);
        planSelectionPage.waitForPageLoad();

        // Abrir modal de edición y alternar marca/modelo (Pantalla 1)
        planSelectionPage.clickEditar();
        planSelectionPage.selectMarca(marca);
        planSelectionPage.selectModelo(modelo);
    }

    @Y("guarda los cambios del vehículo")
    public void guardaLosCambiosDelVehiculo() {
        planSelectionPage.clickGuardarCambios();
    }

    @Y("selecciona el plan de seguro más económico")
    public void seleccionaElPlanDeSeguroMasEconomico() {
        // Si venimos del escenario sin editar, crear la page y esperar carga
        if (planSelectionPage == null) {
            planSelectionPage = new PlanSelectionPage(driver);
            planSelectionPage.waitForPageLoad();
        }
        // Seleccionar SOAT Básico (Pantalla 2: el más económico)
        planSelectionPage.selectPlanMasEconomico();
    }

    @Y("continúa con la compra")
    public void continuaConLaCompra() {
        // Gestionar la sección de renovación automática
        planSelectionPage.handleRenewalPopup();
        // Clic en "CONTINUAR CON S/XX"
        planSelectionPage.clickContinuar();
    }

    // ==================== VALIDACIONES (Then) ====================

    @Entonces("se muestra la sección Resumen de tu Compra y se captura la imagen")
    public void seMuestraLaSeccionResumenDeTuCompraYSeCapturaLaImagen() {
        // Esperar a que la página de pago cargue (Paso 2/2)
        paymentPage = new PaymentPage(driver);
        paymentPage.waitForPageLoad();

        // Verificar que el resumen de compra es visible (Pantalla 3)
        assertTrue(paymentPage.isResumenCompraVisible(),
                "La sección 'Resumen de compra' no es visible en la página de pago");

        // Capturar screenshot de la sección Resumen de Compra
        String screenshotPath = paymentPage.captureResumenCompra();
        assertNotNull(screenshotPath,
                "No se pudo capturar el screenshot del resumen de compra");

        System.out.println("================================================");
        System.out.println("[EVIDENCIA] Screenshot del Resumen de Compra:");
        System.out.println("  Ruta: " + screenshotPath);
        System.out.println("================================================");
    }

    @Entonces("se muestra un mensaje de error de placa inválida")
    public void seMuestraUnMensajeDeErrorDePlacaInvalida() {
        // Verificar que aparece el mensaje de error de placa inválida
        assertTrue(homePage.isErrorPlacaDisplayed(),
                "No se mostró el mensaje de error de placa inválida");

        // Verificar que permanecemos en la landing (no redirigió)
        assertTrue(homePage.isStillOnHomePage(),
                "El sistema redirigió a otra página a pesar de la placa inválida");

        String errorMsg = homePage.getErrorMessage();
        System.out.println("[Validación] Mensaje de error mostrado: " + errorMsg);
    }

    @Entonces("el sistema no permite avanzar y muestra error de validación")
    public void elSistemaNoPermiteAvanzarYMuestraErrorDeValidacion() {
        // Verificar que permanecemos en la página de inicio
        assertTrue(homePage.isStillOnHomePage(),
                "El sistema no debería permitir avanzar con datos inválidos o vacíos");

        System.out.println("[Validación] El sistema bloqueó correctamente el avance con datos inválidos");
    }
}
