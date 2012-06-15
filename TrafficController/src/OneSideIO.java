import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JTextArea;

import logFile.BlockedFileManager;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.JScanner;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;

import pukiwikiCommunicator.ForwardInterface;
import pukiwikiCommunicator.PacketFilter;
import pukiwikiCommunicator.PacketMonitorFilter;

public class OneSideIO implements Runnable, ForwardInterface
{
	Pcap pcap;
	Thread me=null;
	
	// tcpdump�ｽﾌ確�ｽF�ｽﾌフ�ｽ�ｽ�ｽ[�ｽ�ｽ
//	public JScrollPane tcpdump_log;
//	private JScrollPane scroll; //tcpdump�ｽﾌ出�ｽﾍ（ScrollPane)
		
	String line;
	String rn ="\n";
//	LogManager logManager;
	
	Ip4 ip = new Ip4();
	Ethernet eth = new Ethernet();
	PcapHeader hdr = new PcapHeader(JMemory.POINTER);
	JBuffer buf = new JBuffer(JMemory.POINTER);
	MainFrame main;
	
//	PacketMonitorFilter packetFilter;
//	BlockedFileManager logFileManager;
	

	/***************************************************************************
	 * Third - we must map pcap's data-link-type to jNetPcap's protocol IDs.
	 * This is needed by the scanner so that it knows what the first header in
	 * the packet is.
	 **************************************************************************/
	int id ;
	int currentHour;
    Calendar calendar=Calendar.getInstance();
    PacketFilter forwardFilter;
    PcapIf myIf;
    byte[] ifMac;
	public OneSideIO(MainFrame m,PcapIf pif, Pcap p,PacketFilter fl){
		main=m;
		myIf=pif;
		try{
 	        ifMac = myIf.getHardwareAddress();
		}
		catch(Exception e){
			ifMac=null;
		}
		forwardFilter=fl;
		forwardFilter.setReturnInterface(this);
		pcap = p;
		id= JRegistry.mapDLTToId(pcap.datalink());
	}
	public void setNewPcap(Pcap p){
		pcap = p;
		id= JRegistry.mapDLTToId(pcap.datalink());
	}
	public void run(){
		PcapPacketHandler<Queue<PcapPacket>> handler = new PcapPacketHandler<Queue<PcapPacket>>() {  
		  public void nextPacket(PcapPacket packet, Queue<PcapPacket> queue) {  
//				while(me!=null){
				int h=calendar.get(Calendar.HOUR);
				if(h!=currentHour){
//					logFileManager.update();
//					logFileManager=new BlockedFileManager("TempLog-"+h);
					currentHour=h;
					main.clearButtonActionPerformed(null);
					JScanner.getThreadLocal().setFrameNumber(0);  
				}
				PcapPacket permanent = new PcapPacket(packet);
				packet.scan(id);
				if(isFromOtherIf(packet)){
				    queue.offer(packet);  
					}
				}
		} ; 
		  
		int rtn=pcap.loop(-1, handler, queue);  
		System.out.println("exiting pcap.loop of if-"+interfaceNo+" due to "+rtn);    
		pcap.close();  
	}
	public void start(){
		if(me==null){
			me=new Thread(this,"WanSideIO");
			me.start();
		}
	}
	
	public void stop(){
		me=null;
		System.out.println("WanSideIO loop stop");
		try{
			this.pcap.breakloop();
		    this.pcap.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
    public void sendPacket(PcapPacket p){
    	PcapPacket px=new PcapPacket(p);
    	synchronized(pcap){
    	    if(this.pcap.sendPacket(px)!=Pcap.OK){
    	    	System.out.println("error @ sendPacket, WanSideIO.");
    	    }
    	}
	    if(logManager!=null)
		      synchronized(logManager){
			          logManager.logDetail(main,p,interfaceNo);	
		     }
    }
    public boolean isFromOtherIf(PcapPacket p){
    	if(myIf==null) return true;
    	if(ifMac==null) return true;
		if (p.hasHeader(eth)) {
//			System.out.printf("#%d: eth.src=%s\n", n, smac);
		     if(!(eth.source().equals(ifMac))){
		    	 return true;
		     }
		     return false;
	     }
         return true;
    }
	Queue<PcapPacket> queue = new ArrayBlockingQueue<PcapPacket>(10);  	
    public Queue<PcapPacket> getPacketQueue(){
    	return queue;
    }
	TrafficLogManager logManager;
	public void setLogManager(TrafficLogManager m){
		logManager=m;
	}
    int interfaceNo;
    public void setInterfaceNo(int i){
    	interfaceNo=i;
    }
}
