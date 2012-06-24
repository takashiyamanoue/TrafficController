package pukiwikiCommunicator;

import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.util.resolver.IpResolver;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

public class ParsePacket {
	public Ip4 ip = new Ip4();
	public Ethernet eth = new Ethernet();
//	public PcapHeader hdr = new PcapHeader(JMemory.POINTER);
//	public JBuffer buf = new JBuffer(JMemory.POINTER);
	public Tcp tcp = new Tcp();
    public Udp udp = new Udp();
    public Arp arp = new Arp();
    public Icmp icmp = new Icmp();
	public final Http http = new Http();
	public PcapPacket packet;
	public String sourceMacString="";
	public String destinationMacString="";
	public String sourceIpString="";
	public String destinationIpString="";
	public String etherString="";
	public String protocol="";
	public byte[] payload;
	public String ipString="";
	public String payloadString="";
	public String l4String="";
	public int sport;
	public int dport;
	public int[] address = new int[14];
	public String[] states = new String[]{"","","","","",
		                                     "","","","",""};
    public void reParse(){
//    	packet.scan(Ethernet.ID);
		states[0]="unknown";
		try{	
			  if (packet.hasHeader(eth)) {
				   packet.getHeader(eth);
				   sourceMacString = FormatUtils.mac(eth.source());
				   destinationMacString = FormatUtils.mac(eth.destination());
//				System.out.printf("#%d: eth.src=%s\n", packet.getFrameNumber(), str);
//				System.out.printf("#%d: eth.src=%s\n", n, smac);
				   address[0]= 0xff & (eth.source()[2]);
				   address[1]= 0xff & (eth.source()[3]);
				   address[2]= 0xff & (eth.source()[4]);
				   address[3]= 0xff & (eth.source()[5]);
				   address[6]= 0xff & (eth.destination()[2]);
				   address[7]= 0xff & (eth.destination()[3]);
				   address[8]= 0xff & (eth.destination()[4]);
				   address[9]= 0xff & (eth.destination()[5]);	
	    	       sport=0;
	    	       dport=0;
				   etherString=sourceMacString+"->"+destinationMacString+" ";
			   }
			   if (packet.hasHeader(ip)) {
				   packet.getHeader(ip);
				   sourceIpString = FormatUtils.ip(ip.source());
				   destinationIpString = FormatUtils.ip(ip.destination());
//				System.out.printf("#%d: ip.src=%s\n", packet.getFrameNumber(), str);
//				System.out.printf("#%d: ip.src=%s\n", n, sip);
					address[0]= 0xff & (ip.source()[0]);
					address[1]= 0xff & (ip.source()[1]);
					address[2]= 0xff & (ip.source()[2]);
					address[3]= 0xff & (ip.source()[3]);
					address[6]= 0xff & (ip.destination()[0]);
					address[7]= 0xff & (ip.destination()[1]);
					address[8]= 0xff & (ip.destination()[2]);
					address[9]= 0xff & (ip.destination()[3]);	
				    ipString=sourceIpString+"->"+destinationIpString+" ";
		            if(packet.hasHeader(tcp)){
		            try{
		    	          packet.getHeader(tcp);
		    	          protocol="tcp";
		    	          sport=tcp.source();
		    	          dport=tcp.destination();
		  				  address[4]= 0xff & (sport>>8);
						  address[5]= 0xff & sport;
						  address[10]= 0xff & (dport>>8);
						  address[11]= 0xff & dport;
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
		    	          payloadString=SBUtil.showAsciiInBinary(payload);
		    	          l4String="tcp "+sport+"->"+dport+" "+flags+" "+payloadString;
		  				  states[0]=flags+" "+payloadString;
		            }
		            catch(Exception e){System.out.println("packet error:tcp :"+e);}
		            }
		            else
		            if(packet.hasHeader(udp)){
		            try{
		    	          packet.getHeader(udp);
		    	          protocol="udp";
		    	          sport=udp.source();
		    	          dport=udp.destination();
		  				  address[4]= 0xff & (udp.source()>>8);
						  address[5]= 0xff & udp.source();
						  address[10]= 0xff & (udp.destination()>>8);
						  address[11]= 0xff & udp.destination();
		    	          try{
		    	              payload=udp.getPayload();
		    	          }
		    	          catch(Exception e){
		    		          System.out.println("PacketFilter.exec error:"+e);
		    		          payload=new byte[]{'e','r','r','o','r','-','g','e','t','P','a','y','l','o','a','d'};
		    	          }
		    	          l4String="udp "+sport+"->"+dport+" "+SBUtil.showAsciiInBinary(payload);
		    	          payloadString=SBUtil.showAsciiInBinary(payload);
		    	          states[0]=payloadString;
		            }
		            catch(Exception e){System.out.println("packet error udp:"+e);}
		            }
		            else
		  			if(packet.hasHeader(icmp)){
		  			try{
		  				  packet.getHeader(icmp);
						  protocol ="icmp";
						  address[4]= 0;
						  address[5]= 0;
						  address[10]= 0;
						  address[11]= 0;
						  String icmpString=icmp.checksumDescription();
						  payload=icmp.getPayload();
						  payloadString=SBUtil.showAsciiInBinary(payload);
						  states[0]=payloadString;
		  			}
		  			catch(Exception e){System.out.println("packet error icmp:"+e);}
					}
					else{
					try{
						  protocol ="ip-N/A";
						  sport=0;
						  dport=0;
						  payload=ip.getPayload();
						  payloadString=SBUtil.showAsciiInBinary(payload);
						  states[0]=payloadString;
					}
					catch(Exception e){System.out.println("packet error ip-n/a:"+e);}
				   }
			   }
			   else  // packet is not ip
			   if(packet.hasHeader(arp)){
	  			    protocol ="arp";
	  			    packet.getHeader(arp);
					address[4]= 0;
					address[5]= 0;
					address[10]= 0;
					address[11]= 0;
					sourceIpString=FormatUtils.ip(arp.spa());
					destinationIpString=FormatUtils.ip(arp.tpa());
					String arpString=arp.hardwareTypeDescription()+
					            " "+arp.operationDescription()+" "
					            +arp.protocolTypeDescription()
					            +" spa-"+sourceIpString
					            +" tpa-"+destinationIpString;
					states[0]=arpString;			   }
			   else{
				   protocol = "ether n/a";
				   states[0]="?";
			   }
				states[2]=protocol;
//				states[3]=IP[1];//�ｽ�ｽ�ｽM�ｽ�ｽ
				states[3]=sourceIpString;
//				states[4]=IP[2];//�ｽ�ｽ�ｽM�ｽ�ｽ              
				states[4]=destinationIpString;
//				sport=(address[4]<<8)|(address[5]);
//				dport=(address[10]<<8)|(address[11]);
				states[5]=""+sport;
				states[6]=""+dport;
				states[7]=sourceMacString;
				states[8]=destinationMacString;
			}
			catch(Exception e){System.out.println("packet error eth or ip:"+e); };		    	
    }
	
	public ParsePacket(PcapPacket p){
		packet=p;
		try{
    	packet.scan(Ethernet.ID);
		}
		catch(Exception e){System.out.println("ParsePacket can failed:"+e);}
		reParse();
		/*
		try{
		packet.scan(Ethernet.ID);		
		}
		catch(Exception e){
			System.out.println("error, PasePacket ... scan:"+e);
		}
		*/

	}

}
