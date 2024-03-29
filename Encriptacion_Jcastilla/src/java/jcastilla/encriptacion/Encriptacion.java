package jcastilla.encriptacion;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Joseth Castilla
 */
public class Encriptacion {

    //VARIABLES 
    private String LETRAS_MAYUSCULAS;
    private String letras_minusculas;

//Metodo constructor
    public Encriptacion(String LETRAS_MAYUSCULAS, String letras_minusculas) {
        this.LETRAS_MAYUSCULAS = LETRAS_MAYUSCULAS;
        this.letras_minusculas = letras_minusculas;
    }

    //METODO CIFRADO CESAR
    public String cifradoCesar(String texto, int desplazamineto) {
        String salida = "Cifrado Cesar";
        for (int i = 0; i < texto.length(); i++) {
            if ((this.LETRAS_MAYUSCULAS.indexOf(texto.charAt(i)) != -1) || (this.letras_minusculas.indexOf(texto.charAt(i)) != -1)) {
                salida += (this.LETRAS_MAYUSCULAS.indexOf(texto.charAt(i)) != -1)
                        ? this.LETRAS_MAYUSCULAS.charAt(((this.LETRAS_MAYUSCULAS.indexOf(texto.charAt(i)))
                                + desplazamineto) % this.LETRAS_MAYUSCULAS.length())
                        : this.letras_minusculas.charAt(((this.letras_minusculas.indexOf(texto.charAt(i)))
                                + desplazamineto) % this.letras_minusculas.length());
            } else {
                salida += texto.charAt(i);
            }
        }
        return salida;
    }

    //METODO PARA DESCIFRAR CESAR
    public String descifradoCesar(String texto, int desplazamineto) {
        String salida = null;
        for (int i = 0; i < texto.length(); i++) {
            if ((this.LETRAS_MAYUSCULAS.indexOf(texto.charAt(i)) != -1) || (this.letras_minusculas.indexOf(texto.charAt(i)) != -1)) {
                if (this.LETRAS_MAYUSCULAS.indexOf(texto.charAt(i)) != -1) {
                    if ((this.LETRAS_MAYUSCULAS.indexOf(texto.charAt(i)) - desplazamineto) < 0) {
                        salida += this.LETRAS_MAYUSCULAS.charAt((this.LETRAS_MAYUSCULAS.length()) + ((this.LETRAS_MAYUSCULAS.indexOf(texto.charAt(i))) - desplazamineto));
                    } else {
                        salida += this.LETRAS_MAYUSCULAS.charAt(((this.LETRAS_MAYUSCULAS.indexOf(texto.charAt(i))) - desplazamineto) % (this.LETRAS_MAYUSCULAS.length()));
                    }
                } else {
                    if (this.letras_minusculas.indexOf(texto.charAt(i)) < 0) {
                        salida += this.letras_minusculas.charAt((this.letras_minusculas.length()) + ((this.letras_minusculas.indexOf(texto.charAt(i))) - desplazamineto));
                    } else {
                        salida += this.letras_minusculas.charAt(((this.letras_minusculas.indexOf(texto.charAt(i))) - desplazamineto) % (this.letras_minusculas.length()));
                    }
                }
            } else {
                salida += texto.charAt(i);
            }
        }
        return salida;
    }

    //METODO PARA CIFRAR AES(ESTANDAR DE CIFRADO AVANZADO)
//     se usa con el fin de cifrar datos y de protegerlos contra cualquier acceso ilícito. 
//    El método criptográfico emplea para este objetivo una clave de longitud variada y 
//    se denomina según la longitud de clave usada AES-128, AES-192 o AES-256.
//    usa el denominado algoritmo Rijndael en combinación con el cifrado bloque simétrico 
//    como método de cifrado. Las longitudes de bloque y de clave están definidas respectivamente.
//    De este modo, la longitud de bloque es p.ej. de 128 bit y la longitud de clave es de 128, 192 ó 256 bit.
    public static String AES(String key, String initVector, String value) {
        try {
            //Creamos una instancia utilizando el algoritmo de cifrado llamado AES
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));

//            Constante utilizado para inicializar el cifrado en modo de cifrado.
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            String s = new String(Base64.getEncoder().encode(encrypted));
            return s;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //METODO PARA DESCRIFRAR AES(ESTANDAR DE CIFRADO AVANZADO)
    public static String desencriptar(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

//            Constante utilizado para inicializar el cifrado al modo de descifrado.
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

//    UTF-8 representa cada valor de punto de código de 21 bits como una secuencia de una a 
//    cuatro unidades de código de 8 bits. Tiene la gran ventaja de que es compatible con
//    versiones anteriores de ASCII y no interrumpe los programas escritos en C. Es más eficiente 
//    que UTF-16 para almacenar caracteres occidentales. 
    private static String encriptar(String s) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(s.getBytes("utf-8"));
    }

    private static String desencriptar(String s) throws UnsupportedEncodingException {
        byte[] decode = Base64.getDecoder().decode(s.getBytes());
        return new String(decode, "utf-8");
    }

    public static void main(String[] args) {
        String LETRAS_MAYUSCULAS = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        String letras_minusculas = "abcdefghijklmnñopqrstuvwxyz";
        String cadenaDeTexto = "Demo";
        String cadenaEncriptada = "";
        try {
            Encriptacion obj = new Encriptacion(LETRAS_MAYUSCULAS, letras_minusculas);
            String initVector = "RandomInitVector";
            String cifradoAes = AES("Bar12345Bar12345", initVector, cadenaDeTexto);

            System.out.println("CESAR");
            System.out.println(obj.cifradoCesar("--->" + cadenaDeTexto, 1));
//            System.out.println(obj.descifradoCesar("Djgsbep Dftbs", 1));
            System.out.println("");
            System.out.println("AES");
            System.out.println("Cifrado AES --->" + cifradoAes);
            System.out.println("AES Desencriptar-->" + desencriptar("Bar12345Bar12345", initVector, cifradoAes));
            System.out.println("");
            System.out.println("UTF-8");
            cadenaEncriptada = encriptar(cadenaDeTexto);
            System.out.println("Encriptar UTF-8 ---> " + cadenaEncriptada);
            String cadenaDesencriptada = desencriptar(cadenaEncriptada);
            System.out.println("Desencriptada UTF-8 ---> " + cadenaDesencriptada);
        } catch (UnsupportedEncodingException uee) {
            System.out.println("Ups!! > " + uee);
        }
    }

}
