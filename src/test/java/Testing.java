import clientserver.*;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static clientserver.Coding.decrypt;
import static clientserver.Coding.encrypt;

public class Testing
{
    private static Coding receiving;
    private static Sending sending;
    private static Message message;

    @Test
    public void message1()
    {
        String ms1 = "Wowa";
        ByteBuffer byteBuffer = ByteBuffer.allocate(8+ms1.length());
        Message msg1 = new Message(byteBuffer.putInt(1).putInt(228).put(ms1.getBytes()), ms1.length() );
        byte[] encryptMsg1 = encrypt(ms1.getBytes());
        Assert.assertNotEquals(msg1.getMessage(), encryptMsg1);
    }
    @Test
    public void message2()
    {
        String ms1 = "Lizuha";
        ByteBuffer byteBuffer = ByteBuffer.allocate(8+ms1.length());
        Message msg2 = new Message(byteBuffer.putInt(1).putInt(228).put(ms1.getBytes()), ms1.length() );
        byte[] encryptMsg1 = encrypt(ms1.getBytes());
        byte[] decryptMsg1 = decrypt(encryptMsg1);
        Assert.assertNotEquals(msg2.getencryptMessage(), decryptMsg1);
    }

    @Test
    public void sending()
    {
        receiving = new Coding();
        sending = new Sending(33);
        String ms13 = "Heee";
        int cType=1;
        int bUserId=28;

        ByteBuffer byteBuffer3 = ByteBuffer.allocate(8+ms13.length());
        byteBuffer3.putInt(cType).putInt(bUserId).put(ms13.getBytes());
        message = new Message(byteBuffer3, ms13.length());

        int wln = ms13.length()+8;
        ByteBuffer byteBufferLeft = ByteBuffer.allocate(18+ byteBuffer3.capacity());
        byteBufferLeft.put((byte) 0x13).put(sending.getClientId()).putLong(334).putInt(wln);
        ByteBuffer byteBufferRight=ByteBuffer.allocate(14);
        byteBufferRight.put(byteBufferLeft.array(),0,14);
        short crc1 = CRC16.getCRC(byteBufferRight.array());
        short crc2= CRC16.getCRC(byteBuffer3.array());
        byteBufferLeft.putShort(crc1);
        for (byte b:byteBuffer3.array()) {
            byteBufferLeft.put(b);
        }
        byteBufferLeft.putShort(crc2);
        Packet pack1 = new Packet(byteBufferLeft.array());
        Packet pack2 = receiving.receivePacket(sending.sendPacket(pack1));
        Assert.assertTrue(Arrays.equals(pack1.getPack_in_bytes(), pack2.getPack_in_bytes()));
    }
}
