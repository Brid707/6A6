/*
 * Output.java    Version 1.0    14 Feb 2025
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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clase que maneja la escritura de datos en un archivo de texto.
 * Su proposito es almacenar los resultados generados por el programa
 * en un archivo externo especificado por el usuario.
 */
public class Output {

    /**
     * Escribe texto en un archivo indicado. Si el archivo no existe,
     * se crea automaticamente. Si ya existe, se agrega el texto al final.
     *
     * @param outFile Nombre del archivo de salida.
     * @param outText Texto que se escribira dentro del archivo.
     */
    public void writeData(String outFile, String outText) {

        PrintWriter writer = null;

        try {
            // FileWriter en modo "append" para NO sobrescribir el archivo.
            writer = new PrintWriter(new FileWriter(outFile, true));
            writer.println(outText);

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());

        } finally {
            // Cerrar escritor si fue creado correctamente.
            if (writer != null) {
                writer.close();
            }
        }
    }
}
