
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import logFile.BlockedFileManager;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.JScanner;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import pukiwikiCommunicator.PacketMonitorFilter;
import pukiwikiCommunicator.ParsePacket;


public class TrafficLogManager {
	public String realstream;//�ｽﾈま�ｿｽ�ｽO
	public String prt;//�ｽv�ｽ�ｽ�ｽg�ｽR�ｽ�ｽ�ｽ�ｽ
	public int[] srcIP;//�ｽ�ｽ�ｽM�ｽ�ｽ[0]:IP�ｽA�ｽh�ｽ�ｽ�ｽX�ｽ�ｽ�ｽ�ｽ8bit [1]:port�ｽ�ｽ�ｽﾊ８�ｽr�ｽb�ｽg
	public int[] dstIP;//�ｽ�ｽ�ｽ�ｽ[0];IP�ｽA�ｽh�ｽ�ｽ�ｽX�ｽ�ｽ�ｽ�ｽ8bit [1]:port�ｽ�ｽ�ｽ�ｽ8bit
	public String[] IP;//[0]:�ｽ�ｽ�ｽﾔス�ｽ^�ｽ�ｽ�ｽv�ｽ@[1]:�ｽ�ｽ�ｽM�ｽ�ｽIP�ｽA�ｽh�ｽ�ｽ�ｽX�ｽ@[2]:�ｽ�ｽ�ｽ�ｽIP�ｽA�ｽh�ｽ�ｽ�ｽX
	public String[] detailIP;//
//	public OrgLog orgnaize;
	
	PacketMonitorFilter packetFilter;
	int currentHour;
    Calendar calendar;
	TrafficLogManager logManager;
	BlockedFileManager logFileManager;
	long firstTime=-1;
	long lastTime=0;
	long packetNumber=0;

	public long getFirstTime(){
		return firstTime;
	}
	public long getLatestTime(){
		return lastTime;
	}
	
	public TrafficLogManager(PacketMonitorFilter f){
		this.packetFilter=f;
		calendar=Calendar.getInstance();
		currentHour=calendar.get(Calendar.HOUR);
		logFileManager=new BlockedFileManager("TempLog-"+currentHour);
		this.packetNumber=0;
	}
	MainFrame main;
	String wmessage;
//	public void logDetail(MainFrame m,PcapPacket packet, int id){
	public synchronized void logDetail(MainFrame m, ParsePacket p, int itf){
		main=m;
		int h=calendar.get(Calendar.HOUR);
		if(h!=currentHour){
			/*
			logFileManager.update();
			logFileManager=new BlockedFileManager("TempLog-"+h);
			*/
			currentHour=h;
			main.clearButtonActionPerformed(null);
			JScanner.getThreadLocal().setFrameNumber(0);  
			firstTime=-1;
			lastTime=0;
			packetNumber=0;
		}
//		long t=p.packet.getCaptureHeader().timestampInMillis();
		long t=p.ptime;
		if(this.firstTime<0) this.firstTime=t;
		if(t>this.lastTime) this.lastTime=t;
		/*
		if(logFileManager!=null){
		       this.logFileManager.putMessageAt(packet, this.packetNumber);
		       this.packetNumber++;
		}
		*/
		if(main!=null){
			/* */
			if(main.mainWatch!=null)
				this.main.mainWatch.setTermScrollBar();
		   /* */
		}				

//		packet.scan(id);
		String rtn=packetFilter.exec(p);
		String match="";
		if(rtn!=null) match=rtn;
		String smac="", dmac="", sip="", dip="";
		int sport=0, dport=0;
		/*
		try{
		}
		catch(Exception e){
			return;
		}
		*/
		String date=p.ptimes;
		/*
		*/
		
		String[] states=p.states;
		states[1]=date;
		int[] address=p.address;

		wmessage="#"+packetNumber+" if="+itf+" date="+date+" "+
		         p.sourceMacString+" -> "+p.destinationMacString+
		         " prtcl="+p.protocol+" "+p.sourceIpString+" -> "+p.destinationIpString+
		         " "+p.sport+"->"+p.dport+" "+states[0];
		main.writePacketMessage(wmessage);
		this.orgLog(packetNumber, t,states, address,match);		
		packetNumber++;
	}
	VisualTrf vt;
	MainWatch mainWatch;
	
	public void orgLog(long fn, long lt,String[] state, int[] address, String match){
		if(main!=null){
			if(main.mainWatch!=null){
				mainWatch=main.mainWatch;
			}
		}
		int srcIpaunder = address[3];
		int dstIpaunder = address[9];
		int srcPortunder = address[5];
		int dstPortunder = address[11];
//		this.state = state;
//		this.address = address;
		long frameNumber=fn;

		if(mainWatch==null) return;
//		byte[] netAddr=this.main.getCurrentLanInterfaceNetworkAddr();
//		byte[] netMask = this.main.getCurrentLanInterfaceNetmask();
		byte[] netAddr=this.main.getCurrentWanInterfaceNetworkAddr();
		byte[] netMask = this.main.getCurrentWanInterfaceNetmask();
		
		byte[] destinAddr=new byte[4];
		byte[] sourceAddr=new byte[4];
		destinAddr[0]=(byte)address[6]; destinAddr[1]=(byte)address[7]; 
		destinAddr[2]=(byte)address[8]; destinAddr[3]=(byte)address[9];
		sourceAddr[0]=(byte)address[0]; sourceAddr[1]=(byte)address[1]; 
		sourceAddr[2]=(byte)address[2]; sourceAddr[3]=(byte)address[3];
		int fromTo=0;
//		System.out.print("isInTheNet s-addr="+xsaddr+" d-addr="+xdaddr+" netaddr="+xnetAddr);

		if(isInTheNet(sourceAddr,netAddr,netMask)){ // is destenation Ip address is local?
		    if(isInTheNet(destinAddr,netAddr,netMask)){ // is source Ip Address is local?
			   fromTo=4;
		    }
		    else{
		    	fromTo=1;
		    }
		}
		else{
			fromTo=2;
		}
//		System.out.println(" fromTo="+fromTo);
		
		/*
		if(checkExistSrc(srcIpaunder,srcPortunder) == true){
			vt = new VisualTrf(main,frameNumber,lt,state,address,2);
		    mainWatch.vtraffic[srcIpaunder][srcPortunder] = vt; 
			System.out.println("[  "+ srcIpaunder +"  "+srcPortunder+"] �ｽ�ｽ�ｽ�ｽ�ｽﾜゑｿｽﾜゑｿｽ�ｽ�ｽ�ｽB");
			
		}
		else{
			mainWatch.vtraffic[srcIpaunder][srcPortunder].coutup(frameNumber,lt,state,2);
		}
		*/
		if(fromTo==1||fromTo==4){
		  if(checkExistDst(dstIpaunder,dstPortunder) == true){
			vt =new VisualTrf(main,frameNumber,lt,state,address,fromTo,match);
 		    mainWatch.vtraffic[dstIpaunder][dstPortunder] = vt;
			System.out.println("new point [  "+ dstIpaunder +"  "+dstPortunder+"] ");
		  }
		  else{
			mainWatch.vtraffic[dstIpaunder][dstPortunder].coutup(frameNumber,lt,state,fromTo,match);
		  }
		}
		else{
		  if(checkExistDst(srcIpaunder,srcPortunder) == true){
			vt =new VisualTrf(main,frameNumber,lt,state,address,fromTo,match);
		    mainWatch.vtraffic[srcIpaunder][srcPortunder] = vt;
			System.out.println("new point [  "+ srcIpaunder +"  "+srcPortunder+"] ");
		  }
		  else{
			mainWatch.vtraffic[srcIpaunder][srcPortunder].coutup(frameNumber,lt,state,fromTo,match);
		  }
			
		}
			
	}
	private boolean isInTheNet(byte[] addr, byte[] netAddr, byte[] netMask){
		/*
		String xaddr=FormatUtils.ip(addr);
		String xnetAddr=FormatUtils.ip(netAddr);
		String xnetMask=FormatUtils.ip(netMask);
		System.out.println("isInTheNet addr="+xaddr+" netaddr="+xnetAddr+" netMask="+xnetMask);
		*/
		if(addr==null) return false;
		for(int i=0;i<addr.length;i++){
			byte b=(byte)(addr[i]&netMask[i]);
			if(b!=netAddr[i]) return false;
		}
		return true;
	}
	/*
	public int custom8bit(int i){
		int b1 = i;
		int under8 = 255;
		int eight = b1 & under8;
		return eight;
	}
	*/
	
	public boolean checkExistSrc(int i,int p){
		boolean b;
		if(mainWatch==null)return false;
		if (mainWatch.vtraffic[i][p] == null){
			b = true;
			return b ;
		}
		else {
			b = false;
			return b;
		}
	}
	public boolean checkExistDst(int i,int p){
		boolean b;
		if(mainWatch==null) return false;
		if (mainWatch.vtraffic[i][p]== null){
			b =true;
			return b;
		}
		else{
			b = false;
			return b;
		}
	}
	public void updateLogFileManager(){
		this.logFileManager.update();
	}
}

