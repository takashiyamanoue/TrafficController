import java.io.*;
import java.util.Calendar;

import javax.swing.*;
import java.awt.*;

import logFile.BlockedFileManager;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.JScanner;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;

import pukiwikiCommunicator.ForwardInterface;
import pukiwikiCommunicator.PacketFilter;
import pukiwikiCommunicator.PacketMonitorFilter;

/*
 
 
   NIC-[bridge]-WanSideIO-
                      |
                    forwardFilter
   
   NIC-[bridge]-LanSideIO--------LogManager
   
 */
public class LanSideIO extends WanSideIO implements Runnable, ForwardInterface
{
	public PacketMonitorFilter monitorFilter;	

	/***************************************************************************
	 * Third - we must map pcap's data-link-type to jNetPcap's protocol IDs.
	 * This is needed by the scanner so that it knows what the first header in
	 * the packet is.
	 **************************************************************************/
	int currentHour;
    Calendar calendar;

	public LanSideIO(MainFrame m,PcapIf pif,Pcap p,PacketFilter fl){
		super(m,pif,p,fl);
	}

	public void run(){
		while(me!=null){

			int rtn=0;
			synchronized(pcap){
			   rtn=pcap.nextEx(hdr, buf);
			}
			if(rtn!=Pcap.NEXT_EX_OK) {
//				me=null;
				main.writePacketMessage("pcap.NEXT_EX not OK");
			}
			else
			{
				PcapPacket packet = new PcapPacket(hdr, buf);
	    		packet.scan(id);
				if(isFromOtherIf(packet)){
					PcapPacket forwardPacket=forwardFilter.exec(packet);
				    if(forwardPacket!=null){
					   if(this.otherIO!=null){
					     this.otherIO.sendPacket(forwardPacket);
					   }
				    }
				}
				if(logManager!=null)
			    	  synchronized(logManager){
				          logManager.logDetail(main,packet,0);	
			    	  }

			}
		}
		System.out.println("exitting LogOut loop");
	}
	public void start(){
		if(me==null){
			me=new Thread(this,"LanSideIO");
			this.main.mainWatch.setStartButtonValue(true);
			me.start();
		}
	}
	public void stop(){
		me=null;
		System.out.println("LanSideIO loop stop");
		this.main.mainWatch.setStartButtonValue(false);
		try{
		    this.pcap.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
    LogManager logManager;
    public void setLogManager(LogManager lm){
    	this.logManager=lm;
    }

}
