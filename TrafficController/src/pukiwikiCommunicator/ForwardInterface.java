package pukiwikiCommunicator;
import org.jnetpcap.packet.PcapPacket;


public interface ForwardInterface {
    public void sendPacket(ParsePacket x);
    public byte[] getIPAddr();
    public void setIpMac(byte[] ip, byte[] mac);
}
