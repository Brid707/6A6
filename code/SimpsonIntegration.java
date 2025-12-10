/*
 * SimpsonIntegration.java    Version 4.0    14 Feb 2025
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
 * Clase que implementa la Regla de Simpson para la integración de la
 * distribución t. Incluye refinamiento adaptativo y una condición adicional
 * para detener el proceso cuando la mejora ya no es significativa.
 */
public class SimpsonIntegration {

    private int intNumSeg;      // número de segmentos
    private double dblW;        // ancho de cada segmento
    private double dblE;        // error permitido
    private int intDOF;         // grados de libertad
    private double dblX;        // límite superior de integración

    private double dblExponent; // exponente de la distribución t
    private double dblCoeff;    // coeficiente de la distribución t

    private GammaFunction gammaFunction;

    public SimpsonIntegration(int intNumSeg, double dblE, int intDOF, double dblX) {
        this.intNumSeg = intNumSeg;
        this.dblE = dblE;
        this.intDOF = intDOF;
        this.dblX = dblX;
        this.gammaFunction = new GammaFunction();
    }

    /**
     * Método que imprime cada refinamiento, duplica los segmentos y se detiene
     * cuando:
     * 1) El error es menor que el permitido (dblE), o
     * 2) La mejora ya NO es significativa (menos de 1e-17).
     */
    public double integrateWithDebug() {

        double anterior;
        double nuevo;

        nuevo = ejecutarSimpson(this.intNumSeg);

        System.out.println("num_seg = " + this.intNumSeg);
        System.out.println("p = " + nuevo);
        System.out.println();

        double diferencia;

        do {
            anterior = nuevo;
            this.intNumSeg *= 2;  // simular refinamiento tipo PSP/Excel
            nuevo = ejecutarSimpson(this.intNumSeg);

            System.out.println("num_seg = " + this.intNumSeg);
            System.out.println("p = " + nuevo);
            System.out.println();

            diferencia = Math.abs(nuevo - anterior);

            // ==============================
            // NUEVA CONDICIÓN IMPORTANTE
            // ==============================
            if (diferencia < 1E-17) {
                System.out.println(">> La diferencia ya no disminuye de forma significativa.");
                System.out.println(">> Deteniendo el refinamiento por límite numérico del tipo double.");
                break;
            }
            // ==============================

        } while (diferencia > this.dblE);

        return nuevo;
    }

    /**
     * Ejecuta la Regla de Simpson para un número fijo de segmentos.
     */
    private double ejecutarSimpson(int numSeg) {

        // 1. Calcular ancho W
        this.dblW = this.dblX / numSeg;

        // 2. Calcular exponentes y coeficiente de la distribución t
        this.dblExponent = computeExponent(this.intDOF);
        this.dblCoeff = computeCoefficient(this.intDOF);

        double suma = 0.0;

        // 3. Recorrer todos los puntos xi
        for (int i = 0; i <= numSeg; i++) {

            double x = i * this.dblW;

            // término base (1 + xi^2/dof)
            double base = 1.0 + (x * x) / this.intDOF;

            // función t-distribution evaluada en xi
            double fxi = this.dblCoeff * Math.pow(base, this.dblExponent);

            // aplicar regla de Simpson
            if (i == 0 || i == numSeg) {
                suma += fxi;
            } else if (i % 2 == 0) {
                suma += 2.0 * fxi;
            } else {
                suma += 4.0 * fxi;
            }
        }

        // 4. Fórmula de Simpson
        return (this.dblW / 3.0) * suma;
    }

    /**
     * Calcula -(dof + 1) / 2
     */
    public double computeExponent(int dof) {
        return -1.0 * (dof + 1.0) / 2.0;
    }

    /**
     * Calcula el coeficiente de la distribución t:
     * Gamma((dof + 1)/2) / (sqrt(dof*pi) * Gamma(dof/2))
     */
    public double computeCoefficient(int dof) {

        double numerador =
                gammaFunction.computeDblGamma((dof + 1.0) / 2.0);

        double denominador =
                Math.sqrt(dof * Math.PI)
                * gammaFunction.computeDblGamma(dof / 2.0);

        return numerador / denominador;
    }
}
