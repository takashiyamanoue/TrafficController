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

public String addrPort(byte[] a, int p){
    String as=bytes2s(a);
	as=as+":";
	as=as+p;
	return as;
}

private Vector <Filter> filters;
PukiwikiCommunicator pukiwiki;
String myName;
public PacketFilter(PukiwikiCommunicator pw,String n){
	myName=n;
	filters=new Vector();
	this.pukiwiki=pw;
	if(pukiwiki!=null)
	if(resultQueue==null){
		resultQueue=new Vector();
	}
	this.nat=new Hashtable();
}
public void addFilter(String cmd, String[] args){
	Filter f=new Filter(cmd, args);
	filters.add(f);
}
/*
public Filter elementAt(int i){
	return filters.elementAt(i);
}
*/
   
Ip4 ip = new Ip4();
Ethernet eth = new Ethernet();
//PcapHeader hdr = new PcapHeader(JMemory.POINTER);
//JBuffer buf = new JBuffer(JMemory.POINTER);
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
Hashtable <String, String> nat;
private PcapPacket exec(PcapPacket p){
	packet=p;
	if(p==null) return p;
	ptime=""+(new Date(packet.getCaptureHeader().timestampInMillis()));
//	ptime=(new Date()).toString();
	long n=packet.getFrameNumber();
	try{	
	if (packet.hasHeader(eth)) {
		packet.getHeader(eth);
		smac = FormatUtils.mac(eth.source());
		dmac = FormatUtils.mac(eth.destination());
//		System.out.printf("#%d: eth.src=%s\n", packet.getFrameNumber(), str);
//		System.out.printf("#%d: eth.src=%s\n", n, smac);
		etherString=smac+"->"+dmac+" ";
	}
	if (packet.hasHeader(ip)) {
		packet.getHeader(ip);
		sip = FormatUtils.ip(ip.source());
		dip = FormatUtils.ip(ip.destination());
//		System.out.printf("#%d: ip.src=%s\n", packet.getFrameNumber(), str);
//		System.out.printf("#%d: ip.src=%s\n", n, sip);
		ipString=sip+"->"+dip+" ";
	}
	else{
		
	}
	}
	catch(Exception e){System.out.println("packet error eth or ip:"+e); return null; };
	try{
    if(packet.hasHeader(tcp)){
    	packet.getHeader(tcp);
    	protocol="tcp";
    	sport=tcp.source();
    	dport=tcp.destination();
    	try{
    	payload=tcp.getPayload();
    	}
    	catch(Exception e){
    		payload=new byte[]{'e','r','r','o','r'};
    		System.out.println("error tcp, PacketFilter.exec..."+e);
    	}
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
    	packet.getHeader(udp);
    	protocol="udp";
    	sport=udp.source();
    	dport=udp.destination();
    	try{
    	payload=udp.getPayload();
    	}
    	catch(Exception e){
    		System.out.println("PacketFilter.exec error:"+e);
    		payload=new byte[]{'e','r','r','o','r','-','g','e','t','P','a','y','l','o','a','d'};
    	}
    	l4String="udp "+sport+"->"+dport+" "+showAsciiInBinary(payload);
    	payloadString=showAsciiInBinary(payload);
    }
    else{
    	
    }
	}
	catch(Exception e){
		System.out.println("packet error tcp/udp :"+e);
		return null;
	}
	for(int i=0;i<filters.size();i++){
		Filter f=filters.elementAt(i);
		boolean rtn=execCommand(f.getCommand(),f.getArgs(), p);
		if(rtn) {
			return null;
		}
		else{

		}
	}
	if(packet.hasHeader(ip)){
        if(isInNat(ip.source(), sport, ip.destination(), dport)){
        	return restoreNatedPacket(packet);
        }
        if(isDnsAnswer(packet)){
    	   byte[] dnsr=getDnsAnswerAddr(packet);
           if(isInNat(dnsr, 0, ip.destination(), dport)){
        	  this.writeResultToBuffer("substitute-destination to "+bytes2s(dnsr));
    	      return setDnsReturn(packet,dnsr);
           }
        }
	}
	return p;
}
private boolean isInNat(byte[] x, int y, byte[] u, int w){
	String ap=addrPort(x,y);
	String sp=addrPort(u,w);
	int nc=nat.size();
	if(nc==0)return false;
	String key=sp+"-"+ap;
	System.out.println("isInNat key="+key);
	String rtn=nat.get(key);
	if(rtn==null) return false;
	System.out.println("rewriting source-ip from "+ap+" to "+ rtn);
	return true;
}

private boolean execCommand(String command, String[] args, PcapPacket p){
//    System.out.println("ex. "+command);
	/*
    for(int i=0;i<args.length;i++){
       if(args[i]!=null) System.out.println(args[0]);
    }
    */
//    System.out.println("\n");
	PcapPacket rtn=null;
    if(command.equals("drop ip=")){
    	if(isMatchIpV4Address(args[0],sip)){
        	this.writeResultToBuffer(command);
        	return true;
        }
    	if(isMatchIpV4Address(args[0],dip)){
        	this.writeResultToBuffer(command);
        	return true;
        }
    	return false;
	}
    if(command.equals("drop includes ")){
    	if(0<=l4String.indexOf(args[0])){
        	this.writeResultToBuffer(command);
        	return true;
        }
    	else
    	    return false;
	}
    if(command.equals("drop startsWith ")){
    	if(payloadString.startsWith(args[0])){
        	this.writeResultToBuffer(command);
        	return true;
        }
    	else
//    	return p;
    		return false;
	}
    if(command.equals("return-syn-ack ip=")){
     	if(isMatchIpV4Address(args[0],dip)){
     		if(packet.hasHeader(tcp)){
     		    packet.getHeader(tcp);
     		    if(tcp.flags_SYN() && !tcp.flags_ACK()){
    		       PcapPacket pr=makeSynAckReturn(p);
    		       this.returnInterface.sendPacket(pr);
    		       this.writeResultToBuffer(command);
    		       return true;
     	        }
     		    return false;
            }
     		return false;
     	}
//    	return p;
     	return false;
	}
    if(command.equals("forward ip=")){
    	if(isMatchIpV4Address(args[0],dip)){
    		String faddr=args[1];
    		PcapPacket pr=makeForward(p,faddr,args[2]);
            if(pr==null) return true;
    		otherIO.sendPacket(pr);
    		this.writeResultToBuffer(command);
    		return true;
        }
    	else{
    		return false;
    	}
	}
    if(command.equals("forward sip=")){
    	if(!protocol.equals("tcp")) return false;
    	if(isMatchIpV4Address(args[0],sip)){
    		String faddr=args[1];
    		PcapPacket pr=makeForward(p,faddr,args[2]);
    		if(pr==null) return true;
    		otherIO.sendPacket(pr);
    		this.writeResultToBuffer(command);
    		return true;
        }
    	else{
    		return false;
    	}
	}
    if(command.equals("dns-intercept ip=")){
    	if(!protocol.equals("udp")) return false;
        p.getHeader(udp);
    	int dp=udp.destination();
    	if(dp==53){
    		   PcapPacket pr=makeDnsInterCeption(p,args[0],args[1]);
    		   if(pr==null) return true;
    		   otherIO.sendPacket(pr);
    		   this.writeResultToBuffer(command);
    		   return true;
    	}
    	else{
    		return false;
    	}
	}
    return false;
}
private String bytes2s(byte[] x){
	int ax=0;
	ax=x[0];
	if(ax<0)ax=256+ax;
	String rtn=""+ax;
	int len=x.length;
	for(int i=1;i<len;i++){
		ax=x[i];
		if(ax<0) ax=256+ax;
		rtn=rtn+"."+ax;
	}
	return rtn;
}

private boolean isMatchIpV4Address(String x, String y){
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
private void writeResultToBuffer(String x){
	String out=ptime+" "+x+" "+this.etherString+this.ipString+this.l4String+"\n";
	if(resultQueue==null) return;
	resultQueue.add(out);
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
private String showAsciiInBinary(byte[] b){
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
	   Ethernet eth=new Ethernet();
	   Ip4 ip = new Ip4();
	   Tcp tcp = new Tcp();
       PcapPacket px=new PcapPacket(p);
	   px.getHeader(eth);
	   px.getHeader(ip);
	   px.getHeader(tcp);
	   tcp.flags_SYN(true);
	   tcp.flags_ACK(true);
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
	   tcp.checksum(tcp.calculateChecksum());
	   ip.checksum(ip.calculateChecksum());
	   eth.checksum(eth.calculateChecksum());
	   return px;
   }
   private PcapPacket makeDnsInterCeption(PcapPacket p, String oaddr, String newAddr){
	   if(this.anotherSideFilter!=null){
		   Ethernet eth=new Ethernet();
		   Ip4 ip = new Ip4();
		   Udp udp = new Udp();
		   PcapPacket px=new PcapPacket(p);
		   px.getHeader(eth);
		   px.getHeader(ip);
		   px.getHeader(udp);
		   byte[] da=s2byte(oaddr);
		   byte[] na=s2byte(newAddr);
		   String a=addrPort(da,0);
		   String s=addrPort(ip.source(),udp.source());
		   String b=addrPort(na,0);
		   anotherSideFilter.setNatA(a+"-"+s, b);		   
 		   udp.checksum(udp.calculateChecksum());
		   ip.checksum(ip.calculateChecksum());
		   eth.checksum(eth.calculateChecksum());
		   return px;
	   }
	   return null;
   }
   private PcapPacket makeForward(PcapPacket p, String faddr, String port){
	   if(p.hasHeader(ip)){
		   Ethernet eth=new Ethernet();
		   Ip4 ip = new Ip4();
		   Tcp tcp = new Tcp();
		   Udp udp = new Udp();
		   PcapPacket px=new PcapPacket(p);
		   px.getHeader(eth);
		   px.getHeader(ip);
	       byte[] da=s2byte(faddr);
	       byte[] oa=ip.destination();
	       byte[] sa=ip.source();
	       int sp=0;
	       int dp=(new Integer(port)).intValue();
	       int op=0;
	       if(px.hasHeader(tcp)){
		       px.getHeader(tcp);
		       op=tcp.destination();
		       sp=tcp.source();
	           tcp.destination(dp);		   
	   		   tcp.checksum(tcp.calculateChecksum());
	       }
	       else
	       if(px.hasHeader(udp)) {
		       px.getHeader(udp);
		       op=udp.destination();
		       sp=udp.source();
	           udp.destination(dp);		   
	   		   udp.checksum(udp.calculateChecksum());
	       }
	       else{
	    	   return null;
	       }
	       ip.destination(da);
	       if(this.anotherSideFilter!=null){
		       String a=addrPort(da,dp);
		       String s=addrPort(sa,sp);
		       String b=addrPort(oa,op);
		       String key=a+"-"+s;
		       System.out.println("make forward key="+key+" info="+b);
		       anotherSideFilter.setNatA(key, b);
	       }
   		   ip.checksum(ip.calculateChecksum());
		   eth.checksum(eth.calculateChecksum());
	       return px;
	   }
	   return null;
   }
   private PcapPacket restoreNatedPacket(PcapPacket p){
	   Ethernet eth=new Ethernet();
	   Ip4 ip = new Ip4();
	   Tcp tcp = new Tcp();
	   Udp udp = new Udp();
	   PcapPacket px=new PcapPacket(p);
	   px.getHeader(eth);
	   px.getHeader(ip);
	   int dp=0;
	   if(px.hasHeader(tcp)){
		   dp=tcp.destination();
	   }
	   else
	   if(px.hasHeader(udp)){
		   dp=udp.destination();
	   }
	   byte[] x=ip.source();
	   byte[] y=ip.destination();
	   String ap=addrPort(x,sport);
	   String sp=addrPort(y,dp);
	   String key=ap+"-"+sp;
	   String op=nat.get(ap+"-"+sp);
	   System.out.println("restoreNated key="+key+" info="+op);
	   System.out.println("substitute-source "+ap+"->"+op);
	   this.writeResultToBuffer("substitute-source "+ap+"->"+op);
	   byte[] opa=getAddressPartOfAP(op);
	   ip.source(opa);
	   if(px.hasHeader(tcp)){
		   px.getHeader(tcp);
		   int opp=getPortPartOfAP(op);
	       tcp.source(opp);
	       tcp.checksum(tcp.calculateChecksum());
	   }
	   else
	   if(px.hasHeader(udp))
	   {
		   px.getHeader(udp);
		   int opp=getPortPartOfAP(op);
		   udp.source(opp);
		   udp.checksum(udp.calculateChecksum());
	   }
	   ip.checksum(ip.calculateChecksum());
       eth.checksum(eth.calculateChecksum());
	   return px;	   
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

	public void setNatA(String a, String b){
	   if(this.nat!=null){
		   int nc1=nat.size();
		   nat.put(a, b);
		   int nc2=nat.size();
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
		   Ethernet eth=new Ethernet();
		   Ip4 ip = new Ip4();
		   Tcp tcp = new Tcp();
		   Udp udp = new Udp();
		   PcapPacket px=new PcapPacket(p);
		   px.getHeader(eth);
		   px.getHeader(ip);
		   px.getHeader(udp);
		   int sp=udp.source();
		   if(sp!=53) return null;  //DNS
		   byte[] pl=udp.getPayload();
		   int pls=pl.length;
		   pl[pls-4]=ap[0];
		   pl[pls-3]=ap[1];
		   pl[pls-2]=ap[2];
		   pl[pls-1]=ap[3];
		   udp.checksum(udp.calculateChecksum());
		   ip.checksum(ip.calculateChecksum());
	       eth.checksum(eth.calculateChecksum());
		   return px;
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
	
	private byte[] getAddressPartOfAP(String ap){
		byte[] rtn=new byte[4];
		StringTokenizer st=new StringTokenizer(ap,".:");
		for(int i=0;i<4;i++){
			int x=(new Integer(st.nextToken())).intValue();
			rtn[i]=(byte)(0xff & x);
		}
		return rtn;
	}
	
	private int getPortPartOfAP(String ap){
		StringTokenizer st=new StringTokenizer(ap,":");
		String dmy=st.nextToken();
		String pt=st.nextToken();
		int x=(new Integer(pt)).intValue();
		return x;
	}
}