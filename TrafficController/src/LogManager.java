import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import logFile.BlockedFileManager;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
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

public class LogManager {
	public String realstream;//なまログ
	public String prt;//プロトコル名
	public int[] srcIP;//送信先[0]:IPアドレス下位8bit [1]:port下位８ビット
	public int[] dstIP;//宛先[0];IPアドレス下位8bit [1]:port下位8bit
	public String[] IP;//[0]:時間スタンプ　[1]:送信先IPアドレス　[2]:宛先IPアドレス
	public String[] detailIP;//
//	public OrgLog orgnaize;
	
	Ip4 ip = new Ip4();
	Ethernet eth = new Ethernet();
	PcapHeader hdr = new PcapHeader(JMemory.POINTER);
	JBuffer buf = new JBuffer(JMemory.POINTER);
	Tcp tcp = new Tcp();
    Udp udp = new Udp();
    Arp arp = new Arp();
    Icmp icmp = new Icmp();
	final Http http = new Http();
	PacketMonitorFilter packetFilter;
	int currentHour;
    Calendar calendar;
	LogManager logManager;
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
	
	public LogManager(PacketMonitorFilter f){
		this.packetFilter=f;
		calendar=Calendar.getInstance();
		currentHour=calendar.get(Calendar.HOUR);
		logFileManager=new BlockedFileManager("TempLog-"+currentHour);
	}
	MainFrame main;
	String wmessage;
//	public void logDetail(MainFrame m,PcapPacket packet, int id){
	public synchronized void logDetail(MainFrame m, PcapPacket packet, int itf){
		main=m;
		int h=calendar.get(Calendar.HOUR);
		if(h!=currentHour){
			logFileManager.update();
			logFileManager=new BlockedFileManager("TempLog-"+h);
			currentHour=h;
			main.clearButtonActionPerformed(null);
			JScanner.getThreadLocal().setFrameNumber(0);  
			firstTime=-1;
			lastTime=0;
		}
		long t=packet.getCaptureHeader().timestampInMillis();
		if(this.firstTime<0) this.firstTime=t;
		if(t>this.lastTime) this.lastTime=t;
		if(logFileManager!=null){
		       this.logFileManager.putMessageAt(packet, this.packetNumber);
		       this.packetNumber++;
		}
		if(main!=null){
			/* */
			if(main.mainWatch!=null)
				this.main.mainWatch.setTermScrollBar();
		   /* */
		}				

//		packet.scan(id);
		String rtn=packetFilter.exec(packet);
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
		long time=packet.getCaptureHeader().timestampInMillis();
		String date=""+(new Date(time));
		/*
		*/
		
		String[] states;
		states = new String[10];
		states[1]=date;
		int[] address;
		address = new int[14];
		String prt="";
		try{
		if (packet.hasHeader(eth)) {
//				System.out.printf("#%d: eth.src=%s\n", n, smac);
			smac = FormatUtils.mac(eth.source());
			dmac = FormatUtils.mac(eth.destination());
		}
		if(packet.hasHeader(ip)){
			sip = FormatUtils.ip(ip.source());
			dip = FormatUtils.ip(ip.destination());
			address[0]= 0xff & (ip.source()[0]);
			address[1]= 0xff & (ip.source()[1]);
			address[2]= 0xff & (ip.source()[2]);
			address[3]= 0xff & (ip.source()[3]);
			address[6]= 0xff & (ip.destination()[0]);
			address[7]= 0xff & (ip.destination()[1]);
			address[8]= 0xff & (ip.destination()[2]);
			address[9]= 0xff & (ip.destination()[3]);	
			//
			if(packet.hasHeader(tcp)){
				prt="TCP";
				address[4]= 0xff & (tcp.source()>>8);
				address[5]= 0xff & tcp.source();
				address[10]= 0xff & (tcp.destination()>>8);
				address[11]= 0xff & tcp.destination();
				String flags="-";
				if(tcp.flags_SYN()) flags=flags+"SYN-";
				if(tcp.flags_ACK()) flags=flags+"ACK-";
				if(tcp.flags_PSH()) flags=flags+"PSH-";
				if(tcp.flags_FIN()) flags=flags+"FIN-";
				if(tcp.flags_RST()) flags=flags+"RST-";
				if(tcp.flags_CWR()) flags=flags+"CWR-";
				if(tcp.flags_URG()) flags=flags+"URG-";
				states[0]=flags+" "+showAsciiInBinary(tcp.getPayload());
			}
			else
			if(packet.hasHeader(udp)){
				prt="UDP";
				address[4]= 0xff & (udp.source()>>8);
				address[5]= 0xff & udp.source();
				address[10]= 0xff & (udp.destination()>>8);
				address[11]= 0xff & udp.destination();
				states[0]=showAsciiInBinary(udp.getPayload());
			}
			else
			if(packet.hasHeader(icmp)){
				prt="ICMP";
				address[4]= 0;
				address[5]= 0;
				address[10]= 0;
				address[11]= 0;
				String icmpString=icmp.checksumDescription();
				states[0]=icmpString+" "+showAsciiInBinary(arp.getPayload());
			}
			else{
				prt="ip-N/A";
				address[4]= 0xff & (udp.source()>>8);
				address[5]= 0xff & udp.source();
				address[10]= 0xff & (udp.destination()>>8);
				address[11]= 0xff & udp.destination();
				states[0]=showAsciiInBinary(eth.getPayload());
			}
		}
		else{
			address[0]= 0xff & (eth.source())[2];
			address[1]= 0xff & (eth.source())[3];
			address[2]= 0xff & (eth.source())[4];
			address[3]= 0xff & (eth.source())[5];
			address[6]= 0xff & (eth.destination())[2];
			address[7]= 0xff & (eth.destination())[3];
			address[8]= 0xff & (eth.destination())[4];
			address[9]= 0xff & (eth.destination())[5];
			if(packet.hasHeader(arp)){
				prt="ARP";
				address[4]= 0;
				address[5]= 0;
				address[10]= 0;
				address[11]= 0;
				sip=FormatUtils.ip(arp.spa());
				dip=FormatUtils.ip(arp.tpa());
				String arpString=arp.hardwareTypeDescription()+
				            " "+arp.operationDescription()+" "
				            +arp.protocolTypeDescription()
				            +" spa-"+sip
				            +" tpa-"+dip;
				states[0]=arpString;

			}		
			else{
				prt="ether-N/A";
				address[4]= 0;
				address[5]= 0;
				address[10]= 0;
				address[11]= 0;
				states[0]=eth.getDescription()+" "+showAsciiInBinary(eth.getPayload());
			}
		}
		}
		catch(Exception e){
			System.out.println(e.toString()+"n="+packetNumber+" itf="+itf);
		}
		states[2]=prt;
//		states[3]=IP[1];//送信元
		states[3]=sip;
//		states[4]=IP[2];//送信先              
		states[4]=dip;
		sport=(address[4]<<8)|(address[5]);
		dport=(address[10]<<8)|(address[11]);
		states[5]=""+sport;
		states[6]=""+dport;
		states[7]=smac;
		states[8]=dmac;
		

		wmessage="#"+packetNumber+" date="+date+" "+smac+" -> "+dmac+" prtcl="+prt+" "+sip+" -> "+dip+" "+states[0];
		main.writePacketMessage(wmessage);
		this.orgLog(packetNumber, time,states, address,match);		
	}
	boolean isInChars(char x, char[] y){
		for(int i=0;i<y.length;i++){
			if(x==y[i]) return true;
		}
		return false;
	}
	char[] asciis=new char[]{
			'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g','h','i','j',
			'k','l','m','n','o','p','q','r','s','t',
			'u','v','w','x','y','z',
			'A','B','C','D','E','F','G','H','I','J',
			'K','L','M','N','O','P','Q','R','S','T',
			'U','V','W','X','Y','Z',
			' ','!','"','#','$','%','&','\'','(',')',
			'=','-','+','*','/','~','^','|','{','}',
			'[',']','<','>','?',';',':','.',',','@'};
	String showAsciiInBinary(byte[] b){
		String rtn="";
		for(int i=0;i<80;i++){
			if(i>=b.length) return rtn;
			char x=(char)b[i];
            if(isInChars(x,asciis)) rtn=rtn+x;
            else rtn=rtn+'.';			
		}
		rtn=rtn+"...";
		return rtn;
	}
	private String natureTerm;
	private String protocol;
	private String[] Ipa;
	private int srcIpaunder;
	private int dstIpaunder;
	private int srcPortunder;
	private int dstPortunder;
	private boolean exist;
	private String[] state;
	private int[] address; 
	VisualTrf vt;
	private long frameNumber;
	MainWatch mainWatch;
	
	public void orgLog(long fn, long lt,String[] state, int[] address, String match){
		if(main!=null){
			if(main.mainWatch!=null){
				mainWatch=main.mainWatch;
			}
		}
		srcIpaunder = address[3];
		dstIpaunder = address[9];
		srcPortunder = address[5];
		dstPortunder = address[11];
		this.state = state;
		this.address = address;
		this.frameNumber=fn;

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
			System.out.println("[  "+ srcIpaunder +"  "+srcPortunder+"] が生まれました。");
			
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

