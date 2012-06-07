package pukiwikiCommunicator;
import org.jnetpcap.packet.PcapPacket;


public interface ForwardInterface {
    public void sendPacket(PcapPacket x);
}
