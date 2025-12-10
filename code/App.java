/*
 * App.java    Version 3.0    14 Feb 2025
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
 * Clase principal del programa.
 * Ejecuta el flujo general llamando a la clase Logic.
 */
public class App {

    /**
     * Metodo principal para ejecutar en consola de Linux.
     *
     * @param args argumentos de linea de comandos (no se usan).
     */
    public static void main(String[] args) {

        // Se crea el objeto que contiene la logica principal.
        Logic logic = new Logic();

        // Se ejecuta el metodo que controla el flujo del programa.
        logic.logic1a();
    }
}

