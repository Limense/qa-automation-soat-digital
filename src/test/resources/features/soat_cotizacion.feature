# language: es
# =====================================================================================
# [IA - GitHub Copilot]: Feature file generado con asistencia de inteligencia artificial.
# Se utilizó GitHub Copilot para:
#   - Diseño y redacción de escenarios de prueba en formato Gherkin (BDD)
#   - Parametrización de datos con Scenario Outline y Examples (particiones de equivalencia)
#   - Identificación de escenarios positivos y negativos
#   - Optimización de la cobertura de pruebas automatizadas
# =====================================================================================

@soat-digital
Característica: Cotización de SOAT Digital en Interseguro
  Como usuario interesado en adquirir un SOAT
  Quiero cotizar mi seguro vehicular en la plataforma digital de Interseguro
  Para elegir el plan más conveniente y completar mi compra

  Antecedentes:
    Dado que el usuario ingresa a la página de SOAT Digital

  # ====================================================================================
  # ESCENARIOS POSITIVOS
  # ====================================================================================

  @positivo @pantalla1 @pantalla2 @pantalla3
  Esquema del escenario: Cotizar SOAT editando datos del vehículo alternando entre marcas y modelos
    Cuando ingresa la placa "<placa>" y hace clic en Cotizar Ahora
    Y edita los datos del vehículo seleccionando marca "<marca>" y modelo "<modelo>"
    Y guarda los cambios del vehículo
    Y selecciona el plan de seguro más económico
    Y continúa con la compra
    Entonces se muestra la sección Resumen de tu Compra y se captura la imagen

    Ejemplos:
      | placa   | marca   | modelo |
      | ABC-123 | TOYOTA  | YARIS  |
      | ABC-123 | HYUNDAI | ACCENT |

  @positivo @pantalla2 @pantalla3
  Esquema del escenario: Cotizar SOAT seleccionando el seguro más económico sin editar datos del vehículo
    Cuando ingresa la placa "<placa>" y hace clic en Cotizar Ahora
    Y selecciona el plan de seguro más económico
    Y continúa con la compra
    Entonces se muestra la sección Resumen de tu Compra y se captura la imagen

    Ejemplos:
      | placa   |
      | ABC-123 |
      | ABC-123 |

  # ====================================================================================
  # ESCENARIOS NEGATIVOS
  # ====================================================================================

  @negativo
  Esquema del escenario: No permite cotizar con placa de formato inválido
    Cuando ingresa la placa "<placa>" y hace clic en Cotizar Ahora
    Entonces se muestra un mensaje de error de placa inválida

    Ejemplos:
      | placa |
      | ABC   |
      | 12345 |

  @negativo
  Esquema del escenario: No permite avanzar con placa vacía o caracteres especiales
    Cuando ingresa la placa "<placa>" y hace clic en Cotizar Ahora
    Entonces el sistema no permite avanzar y muestra error de validación

    Ejemplos:
      | placa  |
      |        |
      | @#$    |
