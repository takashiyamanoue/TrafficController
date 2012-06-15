package pukiwikiCommunicator;

import java.util.Date;
import java.util.Hashtable;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;

import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.util.resolver.IpResolver;

import pukiwikiCommunicator.PacketMonitorFilter.Filter;

public class PacketFilter implements FilterInterface, Runnable {

public class Filter{
	String command;
	String args[];
	public Filter(String c, String[] a){
		this.command=c;
		this.args=a;
	}
	public String getCommand(){
		return command;
	}
	public String[] getArgs(){
		return args;
	}
}

public class AddrPort{
	public byte[] addr;
	public int port;
	public AddrPort(byte[] a, int p){
		addr=a; port=p;
	}
}

private Vector <Filter> filters;
PukiwikiCommunicator pukiwiki;
public PacketFilter(PukiwikiCommunicator pw){
	filters=new Vector();
	this.pukiwiki=pw;
	if(pukiwiki!=null)
	if(resultQueue==null){
		resultQueue=new Vector();
	}
}
public void addFilter(String cmd, String[] args){
	Filter f=new Filter(cmd, args);
	filters.add(f);
}
public Filter elementAt(int i){
	return filters.elementAt(i);
}
   
Ip4 ip = new Ip4();
Ethernet eth = new Ethernet();
PcapHeader hdr = new PcapHeader(JMemory.POINTER);
JBuffer buf = new JBuffer(JMemory.POINTER);
Tcp tcp = new Tcp();
Udp udp = new Udp();
String sip="";
String dip="";
String smac="";
String dmac="";
String protocol="";
int sport=0;
int dport=0;
PcapPacket packet;
String etherString="";
String ipString="";
String l4String="";
String ptime="";
byte[] payload;
String payloadString;
Hashtable <AddrPort, AddrPort> nat;
public PcapPacket exec(PcapPacket p){
	packet=p;
	if(p==null) return p;
	ptime=""+(new Date(packet.getCaptureHeader().timestampInMillis()));
//	ptime=(new Date()).toString();
		long n=packet.getFrameNumber();
	if (packet.hasHeader(eth)) {
		smac = FormatUtils.mac(eth.source());
		dmac = FormatUtils.mac(eth.destination());
//		System.out.printf("#%d: eth.src=%s\n", packet.getFrameNumber(), str);
//		System.out.printf("#%d: eth.src=%s\n", n, smac);
		etherString=smac+"->"+dmac+" ";
	}
	if (packet.hasHeader(ip)) {
		sip = FormatUtils.ip(ip.source());
		dip = FormatUtils.ip(ip.destination());
//		System.out.printf("#%d: ip.src=%s\n", packet.getFrameNumber(), str);
//		System.out.printf("#%d: ip.src=%s\n", n, sip);
		ipString=sip+"->"+dip+" ";
	}
	else{
		
	}
    if(packet.hasHeader(tcp)){
    	protocol="tcp";
    	sport=tcp.source();
    	dport=tcp.destination();
    	payload=tcp.getPayload();
		String flags="-";
		if(tcp.flags_SYN()) flags=flags+"SYN-";
		if(tcp.flags_ACK()) flags=flags+"ACK-";
		if(tcp.flags_PSH()) flags=flags+"PSH-";
		if(tcp.flags_FIN()) flags=flags+"FIN-";
		if(tcp.flags_RST()) flags=flags+"RST-";
		if(tcp.flags_CWR()) flags=flags+"CWR-";
		if(tcp.flags_URG()) flags=flags+"URG-";
    	l4String="tcp "+sport+"->"+dport+" "+flags+" "+showAsciiInBinary(payload);
    	payloadString=showAsciiInBinary(payload);
    }
    else
    if(packet.hasHeader(udp)){
    	protocol="udp";
    	sport=udp.source();
    	dport=udp.destination();
    	payload=udp.getPayload();
    	l4String="udp "+sport+"->"+dport+" "+showAsciiInBinary(payload);
    	payloadString=showAsciiInBinary(payload);
    }
    else{
    	
    }
	for(int i=0;i<filters.size();i++){
		Filter f=filters.elementAt(i);
		PcapPacket rtn=execCommand(f.getCommand(),f.getArgs(), p);
		if(rtn!=null) {
			return rtn;
		}
		else{

		}
	}
    if(isInNat(ip.source(), sport)){
    	byte[] x=ip.source();
    	AddrPort ap=new AddrPort(x,sport);
    	ip.source((nat.get(ap)).addr);
    	if(packet.hasHeader(tcp))
    	    tcp.source((nat.get(ap)).port);
    	else
    		udp.source((nat.get(ap)).port);
    	return p;
    }
    if(isDnsAnswer(p)){
    	byte[] dnsr=getDnsAnswerAddr(p);
        if(isInNat(dnsr, 0)){
    	  AddrPort ap=new AddrPort(dnsr,0);
    	  return setDnsReturn(p,ap.addr);
        }
    }
	return p;
}
private boolean isInNat(byte[] x, int y){
	AddrPort ap=new AddrPort(x,y);
	AddrPort rtn=nat.get(ap);
	if(rtn==null) return false;
	else return true;
}

public PcapPacket execCommand(String command, String[] args, PcapPacket p){
//    System.out.println("ex. "+command);
	/*
    for(int i=0;i<args.length;i++){
       if(args[i]!=null) System.out.println(args[0]);
    }
    */
//    System.out.println("\n");
	PcapPacket rtn=null;
    if(command.equals("drop ip=")){
    	String out=ptime+" "+this.etherString+this.ipString+this.l4String+"\n";
//    	System.out.println("matching..."+out);
//        if(args[0].equals(sip)){            	
    	if(isMatchIpV4Address(args[0],sip)){
//        	pukiwiki.writeResult(out);
        	this.writeResultToBuffer(out);
        	return null;
        }
//        if(args[0].equals(dip)){
    	if(isMatchIpV4Address(args[0],dip)){
//        	pukiwiki.writeResult(out);
        	this.writeResultToBuffer(out);
        	return null;
        }
    	return p;
	}
    if(command.equals("drop includes ")){
    	String out=ptime+" "+this.etherString+this.ipString+this.l4String+"\n";
//    	System.out.println("matching..."+out);
//        if(args[0].equals(sip)){            	
    	if(0<=l4String.indexOf(args[0])){
//        	pukiwiki.writeResult(out);
        	this.writeResultToBuffer(out);
        	return null;
        }
    	else
    	return p;
	}
    if(command.equals("drop startsWith ")){
    	String out=ptime+" "+this.etherString+this.ipString+this.l4String+"\n";
//    	System.out.println("matching...startsWith "+payloadString+ " and "+args[0]);
//        if(args[0].equals(sip)){            	
    	if(payloadString.startsWith(args[0])){
//        	pukiwiki.writeResult(out);
        	this.writeResultToBuffer(out);
        	return null;
        }
    	else
    	return p;
	}
    if(command.equals("return-syn-ack ip=")){
    	String out=ptime+" "+this.etherString+this.ipString+this.l4String+"\n";
//    	System.out.println("matching..."+out);
//        if(args[0].equals(sip)){            	
     	if(isMatchIpV4Address(args[0],dip)){
//        	pukiwiki.writeResult(out);
//        	this.writeResultToBuffer(out);
     		p.getHeader(tcp);
     		if(tcp.flags_SYN() && !tcp.flags_ACK()){
    		    PcapPacket pr=makeSynAckReturn(p);
    		    this.returnInterface.sendPacket(pr);
     	    }
        	return null;
        }
    	return p;
	}
    if(command.equals("forward ip=")){
    	String out=ptime+" "+this.etherString+this.ipString+this.l4String+"\n";
//    	System.out.println("matching..."+out);    		PcapPacket pr=makeSynAckReturn(p);
    	if(isMatchIpV4Address(args[0],dip)){
//        	pukiwiki.writeResult(out);
//        	this.writeResultToBuffer(out);
    		String faddr=args[1];
    		PcapPacket pr=makeForward(p,faddr,args[2]);
    		return pr;
        }
    	else{
    		return p;
    	}
	}
    if(command.equals("forward sip=")){
    	String out=ptime+" "+this.etherString+this.ipString+this.l4String+"\n";
//    	System.out.println("matching..."+out);    		PcapPacket pr=makeSynAckReturn(p);
    	if(isMatchIpV4Address(args[0],sip)){
//        	pukiwiki.writeResult(out);
//        	this.writeResultToBuffer(out);
    		String faddr=args[1];
    		PcapPacket pr=makeForward(p,faddr,args[2]);
    		return pr;
        }
    	else{
    		return p;
    	}
	}
    if(command.equals("dns-intercept ip=")){
    	String out=ptime+" "+this.etherString+this.ipString+this.l4String+"\n";
//    	System.out.println("matching..."+out);    		PcapPacket pr=makeSynAckReturn(p);
    	if(!(p.hasHeader(udp))) return null;
        p.getHeader(udp);
    	int dp=udp.destination();
    	if(dp==53){
    		   PcapPacket pr=makeDnsInterCeption(p,args[0],args[1]);
    		   return pr;
    	}
    	else{
    		return null;
    	}
	}
    return p;
}

public boolean isMatchIpV4Address(String x, String y){
//	String ax[]=new String[4];
//	String ay[]=new String[4];
	StringTokenizer stx=new StringTokenizer(x,".");
	if(stx==null) return false;
	StringTokenizer sty=new StringTokenizer(y,".");
	if(sty==null) return false;
	for(int i=0;stx.hasMoreElements();i++){
		String ax="",ay="";
		try{
		   ax=stx.nextToken();
		   ay=sty.nextToken();
		}
		catch(Exception e){
			return false;
		}
		if(!(ax.equals(ay))){
			if((!ax.equals("*")) && (!ay.equals("*"))){
				return false;
			}
		}
	}
	return true;
}
public void clear(){
	this.filters.removeAllElements();
	this.nat=new Hashtable();
}
Vector <String> resultQueue;
int resultQueueMax=10;
public void writeResultToBuffer(String x){
	if(resultQueue==null) return;
	resultQueue.add(x);
	if(resultQueue.size()>resultQueueMax)
		resultQueue.remove(0);
}
public Vector<String> getResults(){
	return resultQueue;
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
   ForwardInterface returnInterface;
   ForwardInterface forwardInterface;
   public void setReturnInterface(ForwardInterface f){
	   returnInterface=f;
   }
   PacketFilter anotherSideFilter;
   public void setAnotherSideFilter(PacketFilter f){
	   anotherSideFilter=f;
   }
   private PcapPacket makeSynAckReturn(PcapPacket p){
	   p.getHeader(tcp);
	   tcp.flags_SYN(true);
	   tcp.flags_ACK(true);
	   p.getHeader(ip);
	   byte [] sao=ip.source();
	   byte [] dao=ip.destination();
	   byte [] sa=new byte[sao.length];
	   byte [] da=new byte[dao.length];
	   for(int i=0;i<sao.length;i++) sa[i]=sao[i];
	   for(int i=0;i<dao.length;i++) da[i]=dao[i];
	   int sp=tcp.source();
	   int dp=tcp.destination();
	   ip.source(da);
	   ip.destination(sa);
	   tcp.source(dp);
	   tcp.destination(sp);
	   return p;
   }
   private PcapPacket makeDnsInterCeption(PcapPacket p, String oaddr, String newAddr){
	   byte[] da=s2byte(oaddr);
	   byte[] na=s2byte(newAddr);
	   if(this.anotherSideFilter!=null){
		   AddrPort a=new AddrPort(da,0);
		   AddrPort b=new AddrPort(na,0);
		   anotherSideFilter.setNatA(a, b);		   
	   }
	   return p;
   }
   private PcapPacket makeForward(PcapPacket p, String faddr, String port){
	   p.getHeader(ip);
	   byte[] da=s2byte(faddr);
	   byte[] oa=ip.destination();
	   int dp=(new Integer(port)).intValue();
	   int op=0;
	   if(p.hasHeader(tcp)){
		   op=tcp.source();
		   p.getHeader(tcp);
	       tcp.destination(dp);		   
	   }
	   else{
		   op=udp.source();
		   p.getHeader(udp);
	       udp.destination(dp);		   
	   }
	   ip.destination(da);
	   if(this.anotherSideFilter!=null){
		   AddrPort a=new AddrPort(da,dp);
		   AddrPort b=new AddrPort(oa,op);
		   anotherSideFilter.setNatA(a, b);
	   }
	   return p;
   }
   private byte[] s2byte(String x){
	   String a="";
		byte b[] = new byte[4]; // ipv4
		StringTokenizer st=new StringTokenizer(x,".");
		for(int i=0;i<4;i++){
			a=st.nextToken();
			if(!(a.equals("*"))){
			   int ax=(new Integer(a)).intValue();
			   b[i]=(byte)(0x00ff & ax);
			}
		}
        return b;
   }
	public String readSpaces(String x){
		while(x.startsWith(" ")){
			x=x.substring(" ".length());
		}
		return x;
	}
	private boolean isNumber(char x){
		if('0'<=x || x<='9') return true;
		return false;
	}
	private boolean isLetter(char x, char y){
		if(x==y) return true;
		return false;
	}
	public void setNatA(AddrPort a, AddrPort b){
	   if(this.nat!=null){
		   nat.put(a, b);
	   }
	}
	private boolean isDnsAnswer(PcapPacket p){
		if(!p.hasHeader(udp)) return false;
		p.getHeader(udp);
		int sp=udp.source();
		if(sp==53) return true;  //DNS
		return false;
	}
	private byte[] getDnsAnswerAddr(PcapPacket p){
		if(!p.hasHeader(udp)) return null;
		p.getHeader(udp);
		int sp=udp.source();
		if(sp!=53) return null;  //DNS
		byte[] pl=udp.getPayload();
		int pls=pl.length;
		byte[] rtn=new byte[4];
		rtn[0]=pl[pls-4];
		rtn[1]=pl[pls-3];
		rtn[2]=pl[pls-2];
		rtn[3]=pl[pls-1];
		return rtn;
	}
	private PcapPacket setDnsReturn(PcapPacket p, byte[] ap){
		if(!p.hasHeader(udp)) return null;
		p.getHeader(udp);
		int sp=udp.source();
		if(sp!=53) return null;  //DNS
		byte[] pl=udp.getPayload();
		int pls=pl.length;
		pl[pls-4]=ap[0];
		pl[pls-3]=ap[1];
		pl[pls-2]=ap[2];
		pl[pls-1]=ap[3];
		return p;
	}
	/*
    if(isDnsAnswer(p)){
    	byte[] dnsr=getDnsAnswerAddr();
        if(isInNat(dnsr, 0)){
    	  AddrPort ap=new AddrPort(dnsr,0);
    	  return setDnsReturn(p,ap);
      }
*/
	Thread me;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(me!=null){
			if(queue!=null){
			   PcapPacket packet=queue.poll();
			   if(packet!=null){
			      PcapPacket forwardPacket=this.exec(packet);
			      if(forwardPacket!=null){
				     if(otherIO!=null){
//					    byte[] fp=forwardPacket.getByteArray(arg0, arg1);
					    otherIO.sendPacket(forwardPacket);
//					    otherIO.sendPacket
				    }
			     }
			   }
			   else{
				   try{
					   me.sleep(1);
				   }
				   catch(InterruptedException e){
					   
				   }
			   }
			}
			else
			{
				try{
					me.sleep(100);
				}
				catch(InterruptedException e){
					
				}
			}
		}
	}
	public void start(){
		if(me==null){
			me=new Thread(this,"PacketFilter");
			me.start();
		}
	}
	public void stop(){
		me=null;
	}
	ForwardInterface otherIO;
	public void setForwardInterface(ForwardInterface fi){
		otherIO=fi;
	}
	Queue<PcapPacket> queue;
	public void setPacketQueue(Queue<PcapPacket> q){
		queue=q;
	}
}