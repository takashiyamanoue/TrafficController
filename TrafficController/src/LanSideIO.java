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
	LogManager logManager;
		
	PacketMonitorFilter monitorFilter;
	BlockedFileManager logFileManager;
	

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

	public void setMonitorFilter(PacketMonitorFilter f){
		monitorFilter=f;
		calendar=Calendar.getInstance();
		logManager = new LogManager(f);
		
	}
	private long firstTime=-1;
	private long lastTime=0;
	
	public void run(){
		while(me!=null){
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
				long t=packet.getCaptureHeader().timestampInMillis();
				if(this.firstTime<0) this.firstTime=t;
				if(t>this.lastTime) this.lastTime=t;
				if(logManager!=null)
			    	  synchronized(logManager){
				          logManager.logDetail(main,packet,0);	
			    	  }
				long n=packet.getFrameNumber();
				if(logFileManager!=null)
				       this.logFileManager.putMessageAt(packet, n);
				if(main!=null){
					/* */
					if(main.mainWatch!=null)
						this.main.mainWatch.setTermScrollBar();
				   /* */
				}
			}
		}
		System.out.println("exitting LogOut loop");
	}
	public void start(){
		if(me==null){
			me=new Thread(this,"LanSideIO");
			this.main.mainWatch.setStartButtonValue(true);
			currentHour=calendar.get(Calendar.HOUR);
			logFileManager=new BlockedFileManager("TempLog-"+currentHour);
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
    public long getFirstTime(){
    	return this.firstTime;
    }
    public long getLatestTime(){
    	return this.lastTime;
    }

}
