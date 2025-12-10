/*
 * GammaFunction.java    Version 1.0    14 Feb 2025
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
 * Clase que implementa la funcion Gamma de forma recursiva.
 * Se usan los siguientes casos base:
 *  - Gamma(1) = 1
 *  - Gamma(0.5) = sqrt(pi)
 * Para otros valores, se usa la relacion:
 *  Gamma(x) = (x - 1) * Gamma(x - 1)
 */
public class GammaFunction {

    // Valor gamma utilizado para almacenar resultados si es necesario.
    private double gammaValue;

    /**
     * Calcula Gamma de un entero positivo usando recursion.
     * Para n entero, Gamma(n) = (n - 1)!.
     *
     * @param intValue valor entero mayor o igual a 1.
     * @return valor de Gamma(intValue) como entero.
     */
    public int computeIntGamma(int intValue) {

        if (intValue <= 1) {
            return 1;
        }

        return (intValue - 1) * computeIntGamma(intValue - 1);
    }

    /**
     * Calcula Gamma de un valor double usando recursion simple.
     * Casos base: 1.0 y 0.5.
     *
     * @param doubleValue valor para el que se quiere calcular Gamma.
     * @return valor de Gamma(doubleValue).
     */
    public double computeDblGamma(double doubleValue) {

        if (doubleValue == 1.0) {
            // Gamma(1) = 1
            return 1.0;
        }

        if (doubleValue == 0.5) {
            // Gamma(0.5) = sqrt(pi)
            return Math.sqrt(Math.PI);
        }

        // Llamada recursiva: Gamma(x) = (x-1) * Gamma(x-1)
        this.gammaValue = (doubleValue - 1.0) * computeDblGamma(doubleValue - 1.0);
        return this.gammaValue;
    }
}
