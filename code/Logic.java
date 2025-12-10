/*
 * Logic.java    Version 4.0    14 Feb 2025
 *
 * Copyright (c) 2025 Bridget Mendez.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Bridget Mendez ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * Bridget Mendez.
 *
 * BRIDGET MENDEZ MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. BRIDGET
 * MENDEZ SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING, OR DISTRIBUTING THIS SOFTWARE OR
 * ITS DERIVATIVES.
 */

import java.util.Scanner;

/**
 * Clase que controla la interacción con el usuario.
 *
 * Aquí se muestra el menú principal, se leen los datos desde teclado
 * y se llama a las clases numéricas:
 *  - SimpsonIntegration  -> para integrar la distribución t.
 *  - Busqueda            -> para encontrar x dado un p objetivo.
 *
 * El método principal que se usa desde App es logic1a(), tal como
 * lo pide el UML del proyecto.
 */
public class Logic {

    /**
     * Número inicial de segmentos para la Regla de Simpson.
     * Debe ser un número par. Se usa como valor de arranque,
     * y luego SimpsonIntegration lo va refinando.
     */
    private int intNumSeg;

    /**
     * Error permitido (tolerancia) para el criterio de paro
     * dentro de SimpsonIntegration.
     * Mientras la diferencia entre integrales consecutivas
     * sea mayor que este valor, se siguen agregando segmentos.
     */
    private double dblE;

    /**
     * Grados de libertad (degrees of freedom) de la
     * distribución t-Student.
     */
    private int intDOF;

    /**
     * Límite superior de integración.
     * En el problema de "calcular p dado x" representa
     * el valor hasta donde se integra la t-Student.
     */
    private double dblX;

    /**
     * Constructor por defecto.
     * Asigna valores iniciales a los atributos de la clase.
     */
    public Logic() {
        // Número inicial de segmentos que usará Simpson.
        this.intNumSeg = 10;

        // Valor por defecto del error permitido para Simpson.
        // Este valor se puede ajustar dependiendo del modo
        // (rápido / profundo), pero aquí dejamos 1E-5.
        this.dblE = 1E-5;
    }

    /**
     * Método principal de la lógica del programa.
     * Este es el método que se invoca desde App.main()
     * con:  logic.logic1a();
     *
     * Dentro de este método:
     *  - Se muestra un menú principal con dos problemas:
     *      1) Calcular p dado x.
     *      2) Buscar x dado p.
     *  - Según la opción elegida, se piden los datos
     *    al usuario y se llama a SimpsonIntegration
     *    o a Busqueda.
     */
    public void logic1a() {

        // Objeto para leer datos desde el teclado (System.in).
        Scanner scanner = new Scanner(System.in);

        // -----------------------------------------------
        // MENÚ PRINCIPAL
        // -----------------------------------------------
        System.out.println("=== MENU PRINCIPAL ===");
        System.out.println("1. Calcular p dado x (integración Simpson)");
        System.out.println("2. Buscar x dado p (clase Busqueda)");
        System.out.print("Selecciona una opcion: ");

        // Leer la opción elegida por el usuario.
        int opcion = scanner.nextInt();

        // =====================================================================
        // OPCION 1: Calcular p dado x y dof usando SimpsonIntegration
        // =====================================================================
        if (opcion == 1) {

            System.out.println("\n=== INTEGRACION SIMPSON: p = f(x, dof) ===");
            System.out.println("1. Integración rápida (pocas iteraciones)");
            System.out.println("2. Integración profunda (muchas iteraciones)");
            System.out.print("Selecciona una opcion: ");

            // Modo de integración: rápido (1) o profundo (2).
            int modo = scanner.nextInt();

            // Pedimos el valor de x (límite superior de integración).
            System.out.print("Introduce el valor de x: ");
            this.dblX = scanner.nextDouble();

            // Pedimos los grados de libertad.
            System.out.print("Introduce los grados de libertad (dof): ");
            this.intDOF = scanner.nextInt();

            // Según el modo, ajustamos la tolerancia de Simpson.
            if (modo == 1) {
                // Integración rápida: tolerancia menos estricta.
                this.dblE = 1E-5;
                System.out.println("\nModo seleccionado: Integración rápida");
            } else if (modo == 2) {
                // Integración profunda: tolerancia muy pequeña.
                this.dblE = 1E-15;
                System.out.println("\nModo seleccionado: Integración profunda");
            } else {
                // Si el usuario tecleó algo incorrecto, se cancela.
                System.out.println("Opción de integración inválida. Saliendo del programa.");
                scanner.close();
                return; // Salimos del método logic1a().
            }

            // Mostramos el error permitido que se usará.
            System.out.println("error permitido = " + this.dblE);
            System.out.println();

            // Creamos el objeto de SimpsonIntegration con:
            //  - número inicial de segmentos,
            //  - error permitido (dblE),
            //  - dof,
            //  - valor de x.
            SimpsonIntegration simpson =
                    new SimpsonIntegration(
                        this.intNumSeg,
                        this.dblE,
                        this.intDOF,
                        this.dblX
                    );

            // Llamamos al método que hace la integración y
            // que además muestra la "traza" del refinamiento.
            double resultado = simpson.integrateWithDebug();

            // Imprimimos el resultado final en pantalla con formato.
            System.out.printf(
                "x = %.4f    dof = %d    p = %.5f%n",
                this.dblX,
                this.intDOF,
                resultado
            );

            // Creamos un objeto Output para escribir el resultado en archivo.
            Output out = new Output();

            // Construimos la cadena de texto que se va a guardar.
            String texto = "x=" + this.dblX +
                           ", dof=" + this.intDOF +
                           ", p=" + resultado;

            // Guardamos en "resultado.txt".
            out.writeData("resultado.txt", texto);

        // =====================================================================
        // OPCION 2: Buscar x dado p y dof usando la clase Busqueda
        // =====================================================================
        } else if (opcion == 2) {

            System.out.println("\n=== BUSQUEDA DE x: p objetivo y dof ===");

            // Pedimos la probabilidad objetivo.
            System.out.print("Introduce el valor de p objetivo (0 < p < 1): ");
            double pObjetivo = scanner.nextDouble();

            // Pedimos los grados de libertad.
            System.out.print("Introduce los grados de libertad (dof): ");
            this.intDOF = scanner.nextInt();

            // Tolerancia para que la diferencia |p(x) - pObjetivo|
            // se considere suficientemente pequeña (criterio de paro
            // de la búsqueda numérica).
            double toleranciaP = 1E-7;

            // Creamos un objeto de la clase Busqueda, que se encargará de:
            //  - mover x hacia arriba/abajo,
            //  - llamar internamente a SimpsonIntegration,
            //  - detenerse cuando |p(x) - pObjetivo| <= toleranciaP.
            Busqueda busqueda = new Busqueda(pObjetivo, this.intDOF, toleranciaP);

            // Ejecutamos la búsqueda y obtenemos el x aproximado.
            double xEncontrado = busqueda.buscarX();

            // -----------------------------------------------------------------
            // Verificación: calculamos p(xEncontrado) con SimpsonIntegration
            // para ver qué tan cerca estamos del p objetivo.
            // -----------------------------------------------------------------
            SimpsonIntegration simpsonVerif =
                    new SimpsonIntegration(
                        this.intNumSeg,
                        this.dblE,   // dblE vale 1E-5 por defecto.
                        this.intDOF,
                        xEncontrado
                    );

            // Calculamos p(xEncontrado) para imprimirlo y guardarlo.
            double pVerificado = simpsonVerif.integrateWithDebug();

            // Mostramos en pantalla el resumen de la búsqueda.
            System.out.printf(
                "p objetivo = %.5f    dof = %d    x encontrado = %.5f    p(x) verificado = %.5f%n",
                pObjetivo,
                this.intDOF,
                xEncontrado,
                pVerificado
            );

            // Guardamos también estos datos en el archivo de resultados.
            Output out = new Output();
            String texto = "p_objetivo=" + pObjetivo +
                           ", dof=" + this.intDOF +
                           ", x=" + xEncontrado +
                           ", p(x)=" + pVerificado;
            out.writeData("resultado.txt", texto);

        // =====================================================================
        // OPCION INVALIDA
        // =====================================================================
        } else {

            // El usuario no eligió 1 ni 2.
            System.out.println("Opción inválida. Saliendo del programa.");
            scanner.close();
            return; // Salimos del método.
        }

        // Cerramos el scanner al final del método, cuando ya no lo necesitamos.
        scanner.close();
    }
}
