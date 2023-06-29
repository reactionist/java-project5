package clientserver;

import java.nio.ByteBuffer;

public class Sending
{
    private byte clientId;

    public Sending(int clientId) {
        this.clientId = (byte) clientId;
    }

    public byte getClientId() {
        return clientId;
    }

    public byte[] sendPacket(Packet pack) {

        ByteBuffer byteBuffer1 = ByteBuffer.allocate(8 + pack.getbMsg().getencryptMessage().length);
        byteBuffer1.putInt(pack.getbMsg().getcType());
        byteBuffer1.putInt(pack.getbMsg().getbUserId());

        for (byte decryptedMessageByte : pack.getbMsg().getencryptMessage()) {
            byteBuffer1.put(decryptedMessageByte);
        }
        byte[] encryptMessage = byteBuffer1.array();

        ByteBuffer byteBufferRes = ByteBuffer.allocate(18 + encryptMessage.length);

        ByteBuffer byteBuffer = ByteBuffer.allocate(14);
        byteBuffer.put(pack.getbMagic());
        byteBuffer.put(clientId);
        byteBuffer.putLong(pack.getbPkId());
        byteBuffer.putInt(pack.getwLen());
        for (byte b : byteBuffer.array()) {
            byteBufferRes.put(b);
        }
        byteBufferRes.putShort(pack.getwCrc16());
        for (byte b : encryptMessage) {
            byteBufferRes.put(b);
        }
        byteBufferRes.putShort(pack.getwCrc16_2());

        return byteBufferRes.array();
    }
}
