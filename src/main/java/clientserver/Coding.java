package clientserver;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Coding {
    private static String Key = "ABCDEFGHIJKLLLLL";

    public static byte[] encrypt(byte[] message)
    {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] encryptKeyBytes = Key.getBytes();
            SecretKey secretKey = new SecretKeySpec(encryptKeyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            message = cipher.doFinal(message);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static byte[] decrypt(byte[] message) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] decryptKeyBytes = Key.getBytes();
            SecretKey secretKey = new SecretKeySpec(decryptKeyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            message = cipher.doFinal(message);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return message;
    }


    public Packet receivePacket(byte[] receivedPacket)
    {
        byte[] decoded_message_array = new byte[receivedPacket.length-16-8-2];
        System.arraycopy(receivedPacket, 24, decoded_message_array, 0, decoded_message_array.length);
        byte[] decoded_message = decrypt(decoded_message_array);
        byte[] decodedPacket = new byte[16+8+decoded_message.length+2];
        System.arraycopy(receivedPacket, 0, decodedPacket, 0, 16 + 8);
        for (int i=0;i<decoded_message.length;i++){
            decodedPacket[16+8+i] = decoded_message[i];
        }
        decodedPacket[decodedPacket.length-2] = receivedPacket[receivedPacket.length-2];
        decodedPacket[decodedPacket.length-1] = receivedPacket[receivedPacket.length-1];
        return new Packet(decodedPacket);
    }
}