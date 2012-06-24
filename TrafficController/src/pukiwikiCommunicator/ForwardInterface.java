package pukiwikiCommunicator;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.PcapPacket;


public interface ForwardInterface {
    public void sendPacket(ParsePacket x);
    public void sendPacket(JMemoryPacket x, ParsePacket m);
    public void sendPacket(PcapPacket x, ParsePacket m);
    public byte[] getIPAddr();
    public void setIpMac(byte[] ip, byte[] mac);
}
