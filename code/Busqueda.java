/*
 * Busqueda.java    Version 1.0    14 Feb 2025
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

/**
 * Clase que implementa el algoritmo de búsqueda numérica para encontrar
 * el valor de x tal que la integral de la distribución t, desde 0 hasta x,
 * sea igual a una probabilidad objetivo p.
 *
 * Esta clase reutiliza SimpsonIntegration como "caja negra" para calcular
 * p(x, dof) = ∫₀ˣ t(u, dof) du, tal como se indica en el enunciado del
 * programa 6.
 */
public class Busqueda {

    /** Probabilidad objetivo: valor de p que se desea alcanzar. */
    private double objetivoP;

    /** Grados de libertad de la distribución t. */
    private int dof;

    /**
     * Tolerancia máxima permitida para la diferencia |pCalculado - objetivoP|.
     * Corresponde al valor e del enunciado (por ejemplo 0.00001).
     */
    private double toleranciaP;

    /**
     * Error permitido para el refinamiento interno de la Regla de Simpson.
     * Se pasa directamente al objeto SimpsonIntegration.
     */
    private double errorSimpson;

    /** Número inicial de segmentos para Simpson. Debe ser par. */
    private int numSegInicial;

    /** Paso inicial para la búsqueda incremental sobre x (variable d del enunciado). */
    private double pasoInicial;

    /** Límite superior de iteraciones para evitar ciclos infinitos. */
    private int maxIter;

    /**
     * Constructor principal. Inicializa todos los parámetros de búsqueda.
     *
     * @param objetivoP   probabilidad objetivo (0 < p < 1).
     * @param dof         grados de libertad.
     * @param toleranciaP tolerancia para |pCalculado - objetivoP|.
     */
    public Busqueda(double objetivoP, int dof, double toleranciaP) {
        this.objetivoP   = objetivoP;
        this.dof         = dof;
        this.toleranciaP = toleranciaP;

        // Valores por defecto coherentes con el resto del programa.
        this.errorSimpson = 1E-5;
        this.numSegInicial = 10;   // mismo valor de arranque que Logic
        this.pasoInicial   = 0.5;  // valor sugerido en el documento
        this.maxIter       = 1000; // límite de seguridad
    }

    /**
     * Devuelve el valor aproximado de x tal que la integral de la distribución t
     * (con los grados de libertad indicados) es igual a la probabilidad objetivo.
     *
     * Implementa el algoritmo iterativo descrito en el documento 5a:
     *
     *  1. Se inicia con x = 1.0 y d = pasoInicial.
     *  2. Se calcula p(x) mediante SimpsonIntegration.
     *  3. Mientras el error sea mayor que la tolerancia:
     *     3.1 Si p(x) < objetivoP, aumentar x en d.
     *     3.2 Si p(x) > objetivoP, disminuir x en d.
     *     3.3 Si el signo del error cambia, se reduce d a la mitad.
     *  4. El bucle termina cuando |p(x) - objetivoP| <= toleranciaP
     *     o cuando se llega a maxIter iteraciones.
     *
     * @return valor aproximado de x.
     */
    public double buscarX() {

        double x = 1.0;             // valor inicial sugerido
        double d = this.pasoInicial;

        // Calcular p(x) inicial
        double pActual = calcularP(x);
        double errorActual = pActual - this.objetivoP;
        double errorPrevio = errorActual;

        int iter = 0;

        System.out.println("=== INICIO BUSQUEDA DE x ===");
        System.out.println("p objetivo = " + this.objetivoP);
        System.out.println("dof = " + this.dof);
        System.out.println("tolerancia p = " + this.toleranciaP);
        System.out.println();

        while (Math.abs(errorActual) > this.toleranciaP && iter < this.maxIter) {

            System.out.printf(
                "Iter %d  x = %.6f  p(x) = %.6f  error = %.6f  d = %.6f%n",
                iter, x, pActual, errorActual, d
            );

            // Decidir dirección del movimiento en x
            if (errorActual < 0.0) {
                // p(x) < objetivo → necesitamos un x más grande
                x = x + d;
            } else {
                // p(x) > objetivo → necesitamos un x más pequeño
                x = x - d;
            }

            // Evitar que x llegue a cero o sea negativa
            if (x <= 0.0) {
                x = d / 2.0;
            }

            // Recalcular p(x) y error
            pActual = calcularP(x);
            errorPrevio = errorActual;
            errorActual = pActual - this.objetivoP;

            // Si el error cambió de signo, reducir el tamaño del paso
            if (errorPrevio * errorActual < 0.0) {
                d = d / 2.0;
            }

            iter++;
        }

        System.out.println();
        System.out.println("=== FIN BUSQUEDA DE x ===");
        System.out.println("Iteraciones realizadas: " + iter);
        System.out.println("x aproximado = " + x);
        System.out.println("p(x) aproximado = " + pActual);
        System.out.println("error final = " + errorActual);

        return x;
    }

    /**
     * Calcula p(x) = ∫₀ˣ t(u, dof) du utilizando la clase SimpsonIntegration.
     *
     * Reutiliza toda la infraestructura que ya tienes (GammaFunction,
     * SimpsonIntegration y la fórmula de la t-Student).
     *
     * @param x límite superior de integración.
     * @return valor aproximado de la integral.
     */
    private double calcularP(double x) {

        // Crear un nuevo objeto Simpson con el x actual.
        SimpsonIntegration simpson = new SimpsonIntegration(
                this.numSegInicial,
                this.errorSimpson,
                this.dof,
                x
        );

        // Se reutiliza el método integrateWithDebug para mantener la traza
        // de num_seg y p, tal como en el resto del programa.
        return simpson.integrateWithDebug();
    }
}
