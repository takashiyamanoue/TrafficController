package pukiwikiCommunicator;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.PcapPacket;


public interface ForwardInterface {
    public void sendPacketPP(ParsePacket x);
    public void sendPacketJM(JMemoryPacket x, ParsePacket m);
    public void sendPacket(PcapPacket x, ParsePacket m);
    public byte[] getIPAddr();
    public void setIpMac(byte[] ip, byte[] mac);
}
