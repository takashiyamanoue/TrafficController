import java.nio.ByteBuffer;
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
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.JScanner;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;

import pukiwikiCommunicator.ForwardInterface;
import pukiwikiCommunicator.PacketFilter;
import pukiwikiCommunicator.PacketMonitorFilter;
import pukiwikiCommunicator.ParsePacket;

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
	MainFrame main;
	byte [] myIpAddr;
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
	public OneSideIO(MainFrame m,PcapIf pif, Pcap p,PacketFilter fl, byte[] ip){
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
		this.myIpAddr=ip;
	}
	public void setNewPcap(Pcap p){
		pcap = p;
		id= JRegistry.mapDLTToId(pcap.datalink());
	}

	/* */
	Queue<PcapPacket> queue = new ArrayBlockingQueue<PcapPacket>(100);  	
    public Queue<PcapPacket> getPacketQueue(){
    	return queue;
    }
	public void run(){
		PcapPacketHandler<Queue<PcapPacket>> handler = new PcapPacketHandler<Queue<PcapPacket>>() {  
		  public synchronized void nextPacket(PcapPacket packet, Queue<PcapPacket> queue) {  
			  try{
			  PcapPacket permanent = new PcapPacket(packet);
			  if(!isFromOtherIf(packet)) return;
//			  queue.offer(permanent);
			  if(forwardFilter!=null)
				  forwardFilter.process(permanent);
			  }
			  catch(Exception e){
				  System.out.println("OneSideIO.run.nextPacket error: "+e);
			  }
		  }
		} ;
		int rtn=0; 
		try{
		 rtn=pcap.loop(-1, handler, queue);  
		}
		catch(Exception e){
			System.out.println("OneSideIO.run pcap.loop error: "+e);
			pcap.close();
			System.out.println("exitting loop, please start again.");
			return;
		}
		System.out.println("exiting pcap.loop of if-"+interfaceNo+" due to "+rtn);    
		pcap.close();  
	}
	/* */
    /* 
	Queue<JMemoryPacket> queue = new ArrayBlockingQueue<JMemoryPacket>(100);  	
    public Queue<JMemoryPacket> getPacketQueue(){
    	return queue;
    }
	
	public void run(){
		PcapPacketHandler<Queue<JMemoryPacket>> handler = new PcapPacketHandler<Queue<JMemoryPacket>>(){
          public void nextPacket(PcapPacket packet, Queue<JMemoryPacket> queue) {
			  byte[] jpb=new byte[2000];
			  JMemoryPacket jp =  new JMemoryPacket(jpb);
			  jp.transferFrom(packet);
			  if(!isFromOtherIf(packet)) return;

			  queue.offer(jp);
			}
		} ;
		int rtn=0; 
		try{
		 rtn=pcap.loop(-1, handler, queue);  
		}
		catch(Exception e){
			System.out.println("OneSideIO.run pcap.loop error: "+e);
			pcap.close();
			System.out.println("exitting loop, please start again.");
			return;
		}
		System.out.println("exiting pcap.loop of if-"+interfaceNo+" due to "+rtn);    
		pcap.close();  
	}
	*/
	public void start(){
		if(me==null){
			me=new Thread(this,"OneSideIO-"+this.interfaceNo);
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
    public void sendPacketPP(ParsePacket p){
    	if(p==null) return;
    	if(!p.succeeded) return;
    	if(p.packet==null) return;
//    	JMemoryPacket pp=new JMemoryPacket(p.packet);
//    	byte[] b=p.packet.getByteArray(0, 2000);
//    	byte[] b=pp.getByteArray(0, pp.getTotalSize());
//    	this.sendByte(b);
    	this.sendPcapPacket(p.packet);
	    if(logManager!=null)
		      synchronized(logManager){
//		    	  p.packet=pp;
//			          logManager.logDetail(main,p,interfaceNo);	
		    	  logManager.logDetail(main, p, interfaceNo);
		     }
    }
    public void sendPacketJM(JMemoryPacket p, ParsePacket m){
    	if(p==null) return;
    	byte[] b= p.getByteArray(0, p.getTotalSize());
    	this.sendByte(b);
    	if(m==null) return;
    	if(!m.succeeded) return;
	    if(logManager!=null)
		      synchronized(logManager){
//		    	  m.packet=pp;
		    	  logManager.logDetail(main, m, interfaceNo);
		     }    	
    }
    public void sendPacket(PcapPacket p, ParsePacket m){
    	if(p==null) return;
//    	PcapPacket pp=new PcapPacket(p);
//    	JMemoryPacket pp=new JMemoryPacket(p);
//    	byte [] b=p.getByteArray(0, p.getTotalSize());
//    	this.sendByte(b);
    	this.sendPcapPacket(p);
    	if(m==null) return;
    	if(!m.succeeded) return;
	    if(logManager!=null)
		      synchronized(logManager){
//		    	  m.packet=pp;
//			          logManager.logDetail(main,p,interfaceNo);	
		    	  logManager.logDetail(main, m, interfaceNo);
		     }    	
    }
    public void sendByte(byte[] b){
    	ByteBuffer bb=ByteBuffer.wrap(b);
    	synchronized(pcap){
    		if(this.pcap.sendPacket(bb)!=Pcap.OK){
    	    	System.out.println("error @ sendPacket(PcapPacket), if="+interfaceNo);
    	    }
    	}     	
    }
    public void sendPcapPacket(PcapPacket p){
    	synchronized(pcap){
    		if(this.pcap.sendPacket(p)!=Pcap.OK){
    	    	System.out.println("error @ sendPacket(PcapPacket), if="+interfaceNo);
    	    }
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
	TrafficLogManager logManager;
	public void setLogManager(TrafficLogManager m){
		logManager=m;
	}
    int interfaceNo;
    public void setInterfaceNo(int i){
    	interfaceNo=i;
    }
	public byte[] getIPAddr(){
		return this.myIpAddr;
	}
	@Override
	public void setIpMac(byte[] ip, byte[] mac) {
		// TODO Auto-generated method stub
		this.forwardFilter.setIpMac(ip,mac);
	}

}
