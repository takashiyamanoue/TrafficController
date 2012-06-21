//
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapSockAddr;
import org.jnetpcap.packet.format.FormatUtils;

import pukiwikiCommunicator.PacketFilter;
import pukiwikiCommunicator.PacketMonitorFilter;
import pukiwikiCommunicator.PukiwikiCommunicator;

import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import logFile.BlockedFileManager;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MainFrame extends JFrame 
{
	private JScrollPane interfaceTablePane;
	private JRadioButton logFileButton;
	private JButton startButton;
	public JTabbedPane mainTabPane;
	private JButton exitButton;
	private JButton dumpClearButton;
	private JButton saveSettingButton;
	private JButton ipClearButton;
	private JButton browseFileButton;
	private JTextField logFileField;
	private JRadioButton interfaceTableButton;
	static private JTable interfaceTable;
	int lanSideInterface;
	int wanSideInterface;
	Vector <PcapIf> networkInterfaces;
	private PacketMonitorFilter packetMonitorFilter;
	public PukiwikiCommunicator pukiwikiCommunicator;
//    public VisualTrf vtraffic[][];
    public MainWatch mainWatch;
    private JButton clearButton;
    private OneSideIO lanSideIO;
    private OneSideIO wanSideIO;
//    private ErrOut errout;
    private JPanel mainPanel;
    private JPanel tcpDumpPanel;
    private JPanel messagePanel;
	private JScrollPane tcpdump_log;
	private JScrollPane appliMessagePane;
    public JTextArea logtext;
    public JTextArea appliMessageArea;
	public Properties setting;
	String settingFileName="traffic-viewer-settings.properties";
	private JFileChooser fileChooser;
	private FirstMessage firstMessage;
	private MessageDialog messageDialog;
	private PacketFilter lan2Wan;
	private PacketFilter wan2Lan;
	private JLabel regularExpressionFieldLabel;
	private JTextField regularExpressionField;
	private JCheckBox grepCheckBox;

	public MainFrame(){
		initGUI();
		this.setVisible(false);
		try{
		this.setNetworkInterfaces();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		this.loadProperties();
		this.pukiwikiCommunicator=new PukiwikiCommunicator(mainTabPane,this.setting);
		this.packetMonitorFilter=new PacketMonitorFilter(pukiwikiCommunicator);
		
//		vtraffic = new VisualTrf[256][256];
		this.setProperties();
		firstMessage =new FirstMessage(this);
		this.setMonitorFilter(packetMonitorFilter);
	}
	private void initGUI(){
		{
			getContentPane().setLayout(null);
			{
				mainTabPane = new JTabbedPane();
				getContentPane().add(mainTabPane);
				mainTabPane.setBounds(3, 43, 779, 472);
			}
			{
				exitButton = new JButton();
				getContentPane().add(exitButton);
				exitButton.setText("exit");
				exitButton.setBounds(516, 12, 66, 26);
				exitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						exitButtonActionPerformed(evt);
					}
				});
			}
			{
				saveSettingButton = new JButton();
				getContentPane().add(saveSettingButton);
				saveSettingButton.setText("save setting");
				saveSettingButton.setBounds(247, 12, 133, 26);
				saveSettingButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						saveSettingButtonActionPerformed(evt);
					}
				});
			}
			{
				hideButton = new JButton();
				getContentPane().add(hideButton);
				hideButton.setText("hide");
				hideButton.setBounds(385, 12, 66, 26);
				hideButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						hideButtonActionPerformed(evt);
					}
				});
			}
			{
				mainPanel = new JPanel();
				mainPanel.setLayout(null);
				mainTabPane.add("main-tab",mainPanel);
				mainPanel.setSize(580,350);
				mainPanel.setPreferredSize(new java.awt.Dimension(712, 369));
				{
					interfaceTablePane = new JScrollPane();
					mainPanel.add(interfaceTablePane);
					interfaceTablePane.setBounds(36, 125, 698, 170);
					{
						TableModel interfaceTableModel = 
							new DefaultTableModel(
									new String[][] { 
											{ "","", "" ,"","","",""}, { "","", "" ,"","","",""},
											{ "","", "","" ,"","",""}, 
											{ "", "", "" ,"","","",""}, { "", "", "","","","","" },
											{ "", "", "" ,"","","",""}, { "", "", "","","","","" },
											{ "", "", "" ,"","","",""}, { "", "", "","","","","" },
											{ "", "", "" ,"","","",""}, { "", "", "","","","","" }},
									new String[] { "lan","wan","if-name", "description", "mac address", "ip address","mask" });
						interfaceTable = new JTable();
						interfaceTablePane.setViewportView(interfaceTable);
						interfaceTable.setModel(interfaceTableModel);
						interfaceTable.setPreferredSize(new java.awt.Dimension(639, 176));
						interfaceTable.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								interfaceTableMouseClicked(evt);
							}
						});
					}
				}
			}
			{
				interfaceTableButton = new JRadioButton();
				mainPanel.add(interfaceTableButton);
				interfaceTableButton.setText("interfaceTable");
				interfaceTableButton.setBounds(36, 98, 148, 25);
			}
			{
				logFileButton = new JRadioButton();
				mainPanel.add(logFileButton);
				logFileButton.setText("logFile");
				logFileButton.setBounds(36, 76, 81, 22);
			}
			{
				logFileField = new JTextField();
				mainPanel.add(logFileField);
				logFileField.setBounds(127, 75, 252, 22);
			}
			{
				browseFileButton = new JButton();
				mainPanel.add(browseFileButton);
				browseFileButton.setText("browse");
				browseFileButton.setBounds(379, 73, 95, 26);
				browseFileButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						browseFileButtonActionPerformed(evt);
					}
				});
			}
			{
				startButton = new JButton();
				mainPanel.add(startButton);
				startButton.setText("start");
				startButton.setBounds(41, 6, 112, 28);
				startButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						startButtonActionPerformed(evt);
					}
				});
			}
			{
				clearButton = new JButton();
				mainPanel.add(clearButton);
				clearButton.setText("clear");
				clearButton.setBounds(220, 7, 92, 25);
				clearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						clearButtonActionPerformed(evt);
					}
				});
			}
			{
				logPathField = new JTextField();
				mainPanel.add(logPathField);
				logPathField.setBounds(127, 45, 252, 26);
			}
			{
				stopButton = new JButton();
				mainPanel.add(stopButton);
				stopButton.setText("stop");
				stopButton.setBounds(153, 7, 67, 26);
				stopButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						stopButtonActionPerformed(evt);
					}
				});
			}
			{
				tcpDumpPanel = new JPanel();
				tcpDumpPanel.setLayout(null);
				mainTabPane.add("tcpdump",tcpDumpPanel);
				tcpDumpPanel.setSize(580,350);
				tcpDumpPanel.setPreferredSize(new java.awt.Dimension(558, 369));
				messagePanel = new JPanel();
				messagePanel.setLayout(null);
				mainTabPane.add("message",messagePanel);
				messagePanel.setSize(580,350);
				messagePanel.setPreferredSize(new java.awt.Dimension(558, 369));

			{
				tcpdump_log = new JScrollPane();
				tcpdump_log.setBounds(5, 40, 769, 396);
				tcpDumpPanel.add(tcpdump_log);
				{
					logtext = new JTextArea();
					tcpdump_log.setViewportView(logtext);
				}
			}
			{
				dumpClearButton = new JButton();
				tcpDumpPanel.add(dumpClearButton);
				tcpDumpPanel.add(getGrepCheckBox());
				tcpDumpPanel.add(getRegularExpressionField());
				tcpDumpPanel.add(getRegularExpressionFieldLabel());
				dumpClearButton.setText("clear");
				dumpClearButton.setBounds(39, 9, 82, 26);
				dumpClearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dumpClearButtonActionPerformed(evt);
					}
				});
			}
			{
			    appliMessagePane = new JScrollPane();
			    appliMessagePane.setBounds(5, 40, 686, 260);
			    messagePanel.add(appliMessagePane);
//				ip_log = new JFrame("IP�ｽA�ｽh�ｽ�ｽ�ｽX�ｽA�ｽ|�ｽ[�ｽg�ｽﾏ奇ｿｽ�ｽﾌ確�ｽF");
			    {
					appliMessageArea = new JTextArea();
					appliMessagePane.setViewportView(appliMessageArea);
					appliMessageArea.setBounds(0, 0, 190, 95);
				}
			}
			{
				ipClearButton = new JButton();
				messagePanel.add(ipClearButton);
				ipClearButton.setText("clear");
				ipClearButton.setBounds(33, 9, 81, 26);
				ipClearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						ipClearButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			 ButtonGroup gr = new ButtonGroup();
		     gr.add(logFileButton);
		     gr.add(interfaceTableButton);
		     interfaceTableButton.setSelected(true);
		}
		{
			this.setSize(798, 553);
		}		
		}
	    networkInterfaces=new Vector();

	}
	public void setNetworkInterfaces()throws IOException {
            networkInterfaces.removeAllElements();
			List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
			StringBuilder errbuf = new StringBuilder(); // For any error msgs

			/***************************************************************************
			 * First get a list of devices on this system
			 **************************************************************************/
			int r = Pcap.findAllDevs(alldevs, errbuf);
			if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
				System.err.printf("Can't read list of devices, error is %s", errbuf
				    .toString());
				return;
			}

			/***************************************************************************
			 * Second iterate through all the interface and get the HW addresses
			 **************************************************************************/
			int row=0;
			for (final PcapIf i : alldevs) {
				final byte[] mac = i.getHardwareAddress();
				if (mac == null) {
					continue; // Interface doesn't have a hardware address
				}
	            String description =  
	                (i.getDescription() != null) ? i.getDescription()  
	                    : "No description available";  
	             /*
	            List<PcapAddr> alist=i.getAddresses();
	            PcapAddr addr=null;
	            if(alist.size()>=1)
	               addr=alist.get(0);
	            */
	            PcapAddr addr=this.getIpV4Address(i);
	            PcapSockAddr psockaddr=null;
	            PcapSockAddr pmaskaddr=null;
	            if(addr!=null){
	            	psockaddr=addr.getAddr();
	                String addrx=FormatUtils.ip(psockaddr.getData());
	                interfaceTable.setValueAt(addrx,row, 5);
	                pmaskaddr=addr.getNetmask();
	                String maskx=FormatUtils.ip(pmaskaddr.getData());
	                interfaceTable.setValueAt(maskx,row, 6);
	            }
				System.out.printf("%s=%s\n", i.getName(), asString(mac));
				interfaceTable.setValueAt(i.getName(), row, 2);
				interfaceTable.setValueAt(asString(mac),row,4);
				interfaceTable.setValueAt(description, row, 3);
				interfaceTable.setValueAt("",row,0);
				networkInterfaces.addElement(i);
				row++;
			}
            interfaceTable.setValueAt("!", 0, 0);
            this.lanSideInterface=0;

		/**
		 * @param hardwareAddress
		 * @return
		 */
		
	}
	private static String asString(final byte[] mac) {
		final StringBuilder buf = new StringBuilder();
		for (byte b : mac) {
			if (buf.length() != 0) {
				buf.append(':');
			}
			if (b >= 0 && b < 16) {
				buf.append('0');
			}
			buf.append(Integer.toHexString((b < 0) ? b + 256 : b).toUpperCase());
		}

		return buf.toString();
	}
	public static void main(String arg[]){
		new MainFrame();
	}
	
	private void interfaceTableMouseClicked(MouseEvent evt) {
		System.out.println("interfaceTable.mouseClicked, event="+evt);
		//TODO add your code for interfaceTable.mouseClicked
		Point p=evt.getPoint();
		int row = interfaceTable.rowAtPoint(p);
		int column = interfaceTable.columnAtPoint(p);
		int is=interfaceTable.getRowCount();
		if(column==0){
		  for(int i=0;i<is;i++){
			 interfaceTable.setValueAt("",i,0);		
		  }
          interfaceTable.setValueAt("!", row, 0);
          this.lanSideInterface=row;
          if(this.setting!=null){
        	setting.setProperty("lanIterfaceTableNumber", ""+lanSideInterface);
          }
          this.setCurrentLanInterfaceNetworkAddr();
		}
		if(column==1){
			  for(int i=0;i<is;i++){
				 interfaceTable.setValueAt("",i,1);		
			  }
	          interfaceTable.setValueAt("!", row, 1);
	          this.wanSideInterface=row;
	          if(this.setting!=null){
	        	setting.setProperty("wanIterfaceTableNumber", ""+wanSideInterface);
	          }
	          this.setCurrentWanInterfaceNetworkAddr();
			}
	}
	
	public void startButtonActionPerformed(ActionEvent evt) {
//		System.out.println("startButton.actionPerformed, event="+evt);
		if(lanSideInterface==wanSideInterface){
			if(this.messageDialog==null){
				messageDialog=new MessageDialog();
			}
			messageDialog.setMessage1("Input and output interface must be different");
			return;
		}
		//TODO add your code for startButton.actionPerformed
		Pcap lanPcap=null;
		Pcap wanPcap=null;
		StringBuilder errbuf = new StringBuilder(); // For any error msgs
		if(this.logFileButton.isSelected()){
			/***************************************************************************
			 * First - we open up the selected device
			 **************************************************************************/
			String logpath=this.logFileField.getText();
			if(logpath==null||logpath==""){
				logpath="test.pcap";
			}
			lanPcap = Pcap.openOffline(logpath, errbuf);

			if (lanPcap == null) {
				System.out.printf("Error while opening file for capture: "
				    + errbuf.toString());
				return;
			}
		}
		else
		if(this.interfaceTableButton.isSelected()){
	        /*************************************************************************** 
	         * Second we open up the selected device 
	         **************************************************************************/  
	        int snaplen = 64 * 1024;           // Capture all packets, no trucation  
	        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
	        int timeout = 10 * 1000;           // 10 seconds in millis  
	        lanPcap =  
	            Pcap.openLive((String)(interfaceTable.getValueAt(this.lanSideInterface, 2)), snaplen, flags, timeout, errbuf);  
	        if (lanPcap == null) {  
	            System.out.printf("Error while opening device for capture: "  
	                + errbuf.toString());  
	            return;  
	        }  
	        wanPcap =  
		            Pcap.openLive((String)(interfaceTable.getValueAt(this.wanSideInterface, 2)), snaplen, flags, timeout, errbuf);  
		        if (wanPcap == null) {  
		            System.out.printf("Error while opening device for capture: "  
		                + errbuf.toString());  
		            return;  
		        }  
		}
		else{
			return;
		}
		/*
		if(errout==null)
		errout = new ErrOut(pcap);
		*/
        this.setCurrentLanInterfaceNetworkAddr();
        this.setCurrentWanInterfaceNetworkAddr();
        byte[] zeroAddr=new byte[]{0,0,0,0}; // assume ipv4 only
        if(this.equalAddr(zeroAddr, this.getCurrentWanInterfaceNetworkAddr())){
        	copyAddr(this.currentLanInterfaceNetworkAddr,this.currentWanInterfaceNetworkAddr);
        }
        else
        if(this.equalAddr(zeroAddr, this.getCurrentLanInterfaceNetworkAddr())){
        	copyAddr(this.currentWanInterfaceNetworkAddr,this.currentLanInterfaceNetworkAddr);
        	
        }
        
		String wanMac=(String)(interfaceTable.getValueAt(this.wanSideInterface,4));
		String lanMac=(String)(interfaceTable.getValueAt(this.lanSideInterface,4));
		this.wan2Lan=new PacketFilter(pukiwikiCommunicator,"wan2Lan", wanMac,
				currentWanInterfaceNetworkAddr, currentWanInterfaceNetmask, this.getCurrentWanInterfaceIpAddr());        
		this.lan2Wan=new PacketFilter(pukiwikiCommunicator,"lan2Wan", lanMac,
				currentLanInterfaceNetworkAddr, currentLanInterfaceNetmask, this.getCurrentLanInterfaceIpAddr());
		pukiwikiCommunicator.setPacketFilterWan(wan2Lan);
		pukiwikiCommunicator.setPacketFilterLan(lan2Wan);
        
		if(lanSideIO==null){
		   lanSideIO = new OneSideIO(this, 
			       networkInterfaces.elementAt(this.lanSideInterface),
				   lanPcap,lan2Wan, null);
		   lanSideIO.setInterfaceNo(this.lanSideInterface);
		   lanSideIO.setLogManager(this.logManager);
		   lanSideIO.setNewPcap(lanPcap);
		   lanSideIO.start();
		}
		else{
		   lanSideIO.stop();
		   lan2Wan.stop();
		   lanSideIO.setNewPcap(lanPcap);
		   lan2Wan.start();
		   lanSideIO.start();
		}
		if(wanSideIO==null){
		   wanSideIO = new OneSideIO(this, 
				   networkInterfaces.elementAt(this.wanSideInterface),
				   wanPcap,wan2Lan, this.getCurrentWanInterfaceIpAddr());
		   wanSideIO.setInterfaceNo(this.wanSideInterface);
		   wanSideIO.setLogManager(this.logManager);
		   wanSideIO.setNewPcap(wanPcap);
		   wan2Lan.setForwardInterface(lanSideIO);
		   wan2Lan.setAnotherSideFilter(lan2Wan);
		   wan2Lan.setPacketQueue(wanSideIO.getPacketQueue());
		   lan2Wan.setForwardInterface(wanSideIO);
		   lan2Wan.setAnotherSideFilter(wan2Lan);
		   lan2Wan.setPacketQueue(lanSideIO.getPacketQueue());
		   lan2Wan.start();
		   wan2Lan.start();
		   wanSideIO.start();
		}
		else{
		   wanSideIO.stop();
		   wan2Lan.stop();
		   wanSideIO.setNewPcap(wanPcap);
		   wan2Lan.start();
		   wanSideIO.start();
		}
	}
	
	public void exitButtonActionPerformed(ActionEvent evt) {
//		System.out.println("exitButton.actionPerformed, event="+evt);
		//TODO add your code for exitButton.actionPerformed
		if(pukiwikiCommunicator!=null) pukiwikiCommunicator.stop();
		System.exit(0);
	}
	
	public void clearButtonActionPerformed(ActionEvent evt) {
		System.out.println("clearButton.actionPerformed, event="+evt);
		//TODO add your code for clearButton.actionPerformed
//		this.mainWatch.dispose();
//		this.mainWatch=new MainWatch(this);
//		this.logout.stop();
		this.mainWatch.clearData();
		this.mainWatch.clearScreen();
//		this.logout.start();
	}
	
	private void dumpClearButtonActionPerformed(ActionEvent evt) {
		System.out.println("dumpClearButton.actionPerformed, event="+evt);
		//TODO add your code for dumpClearButton.actionPerformed
		this.logtext.setText("");
	}
	
	private void ipClearButtonActionPerformed(ActionEvent evt) {
		System.out.println("ipClearButton.actionPerformed, event="+evt);
		//TODO add your code for ipClearButton.actionPerformed
		this.appliMessageArea.setText("");
	}
	private void saveSettingButtonActionPerformed(ActionEvent evt) {
		System.out.println("saveSettingButton.actionPerformed, event="+evt);
		//TODO add your code for saveSettingButton.actionPerformed
		this.saveProperties();
	}
	public void saveProperties(){
       
	       try {
	           FileOutputStream saveS = new FileOutputStream(settingFileName);
	           if(setting==null){
	        	   setting=new Properties();
	           }
	           setting.store(saveS,"--- traffic-viewer settings ---");

	        } catch( Exception e){
	           System.err.println(e);
	        } 
	}
	public void loadProperties(){
	       try {
	           setting = new Properties() ;
	           FileInputStream appS = new FileInputStream( settingFileName);
	           setting.load(appS);

	        } catch( Exception e){
//	           System.err.println(e);
	        	this.saveProperties();
//	        	return;
	        } 
	}
	public void setProperties(){
		if(setting==null) return;
	        String w=setting.getProperty("logFilePath");
	        if(w!=null) this.logFilePath=w;
	        w=setting.getProperty("logFileName");
	        if(w!=null) this.logFileField.setText(w);
	        w=setting.getProperty("lanIterfaceTableNumber");
        	try{
        		int x=(new Integer(w)).intValue();
        		this.lanSideInterface=x;
        		int is=interfaceTable.getRowCount();
        		for(int i=0;i<is;i++){
        			interfaceTable.setValueAt("",i,0);			
        		}
                interfaceTable.setValueAt("!", x, 0);
        	}
        	catch(Exception e){
        		System.out.println("...");
        	}        	
	        w=setting.getProperty("wanIterfaceTableNumber");
        	try{
        		int x=(new Integer(w)).intValue();
        		this.wanSideInterface=x;
        		int is=interfaceTable.getRowCount();
        		for(int i=0;i<is;i++){
        			interfaceTable.setValueAt("",i,1);			
        		}
                interfaceTable.setValueAt("!", x, 1);
        	}
        	catch(Exception e){
        		System.out.println("...");
        	}        	
	        
	}
	public void writePacketMessage(String x){
		if(this.grepCheckBox.isSelected()){
			boolean b=false;
			try{
			    b = Pattern.matches(this.regularExpressionField.getText(), x);
			}
			catch(Exception e){
				this.logtext.append("\n!wrong regular expression.\n");
			}
			if(!b) return;
		}
		String w=this.logtext.getText();
		if(w.length()>10000)
		     w=w.substring(5000);
		w=w+x+"\n";
	    logtext.setText(w);
		JScrollBar sb=tcpdump_log.getVerticalScrollBar();
		sb.setValue(sb.getMaximum());
		repaint();
	}
	public void writeApplicationMessage(String x){
		String w=this.appliMessageArea.getText();
		if(w.length()>10000)
		     w=w.substring(5000);
		w=w+x+"\n";
	    appliMessageArea.setText(w);
		JScrollBar sb=this.appliMessagePane.getVerticalScrollBar();
		sb.setValue(sb.getMaximum());
		repaint();
	}
	String logFilePath="C:\\";
	private JButton hideButton;
	private JButton stopButton;
	private JTextField logPathField;
	private void browseFileButtonActionPerformed(ActionEvent evt) {
		System.out.println("browseFileButton.actionPerformed, event="+evt);
		//TODO add your code for browseFileButton.actionPerformed
	    if(this.fileChooser==null){
	    	this.fileChooser=new JFileChooser();
	    }
	    if(this.logFilePath!=null)
		    this.fileChooser.setCurrentDirectory(new File(this.logFilePath));
		this.fileChooser.setFileSelectionMode(fileChooser.FILES_AND_DIRECTORIES);
        int returnVal = this.fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.fileChooser.getSelectedFile();
            String fn=file.toString();
            String fx=file.getName();
            //This is where a real application would open the file.
//            this.fileNameField.setText(fn);
            this.logFilePath=file.getParent();
            this.logPathField.setText(this.logFilePath);
            this.logFileField.setText(fx);
            this.setting.setProperty("logFilePath", logFilePath);
            this.setting.setProperty("logFileName", fx);
        }	    

	}
	
	public void stopButtonActionPerformed(ActionEvent evt) {
		System.out.println("stopButton.actionPerformed, event="+evt);
		//TODO add your code for stopButton.actionPerformed
		
//		if(errout!=null)errout.stop();
		if(mainWatch!=null) mainWatch.stop();
	}
	public long getLatestTime(){
		if(this.logManager==null) return 0;
		return logManager.getLatestTime();
	}
	public long getFirstTime(){
		if(this.logManager==null) return 0;
		return logManager.getFirstTime();		
	}
	public long getScrollBarEndTime(){
		return this.mainWatch.getScrollBarEndTime();
	}
	public long getScrollBarStartTime(){
		return this.mainWatch.getScrollBarStartTime();
	}
	
	private void hideButtonActionPerformed(ActionEvent evt) {
		System.out.println("hideButton.actionPerformed, event="+evt);
		//TODO add your code for hideButton.actionPerformed
		this.setVisible(false);
	}
	public PcapAddr getCurrentLanInterfacePcapAddr(){
        PcapIf i=networkInterfaces.elementAt(this.lanSideInterface);
        List<PcapAddr> alist=i.getAddresses();
        PcapAddr addr=null;
        if(alist.size()>=1)
           addr=alist.get(0);
        return addr;

	}
	public byte[] getCurrentLanInterfaceIpAddr(){
//		PcapAddr addr=this.getCurrentInterfacePcapAddr();
        PcapIf i=networkInterfaces.elementAt(this.lanSideInterface);
		PcapAddr addr=this.getIpV4Address(i);
        PcapSockAddr psockaddr=null;
         if(addr!=null){
        	psockaddr=addr.getAddr();
    		return psockaddr.getData();
        }
        else{
        	 return null;
        }
	}
	public byte[] getCurrentWanInterfaceIpAddr(){
//		PcapAddr addr=this.getCurrentInterfacePcapAddr();
        PcapIf i=networkInterfaces.elementAt(this.wanSideInterface);
		PcapAddr addr=this.getIpV4Address(i);
        PcapSockAddr psockaddr=null;
         if(addr!=null){
        	psockaddr=addr.getAddr();
    		return psockaddr.getData();
        }
        else{
        	 return null;
        }
	}
	public PcapAddr getIpV4Address(PcapIf iface){
        List<PcapAddr> alist=iface.getAddresses();
        PcapAddr addr=null;
        PcapSockAddr sockaddr=null;
        System.out.println("address-number="+alist.size());
        if(alist.size()>=1)
        	for(int j=0;j<alist.size();j++){
                addr=alist.get(j);
                sockaddr=addr.getAddr();
                if(sockaddr.getFamily()==PcapSockAddr.AF_INET){
                	return addr;
                }
        	}
        return null;
	}
	byte[] currentLanInterfaceNetworkAddr;
	byte[] currentLanInterfaceNetmask;
	private void setCurrentLanInterfaceNetworkAddr(){
		byte[] addr=getCurrentLanInterfaceIpAddr();
		if(addr==null){
			// assume ipv4 only
			addr=new byte[4];
			addr[0]=0;
			addr[1]=0;
			addr[2]=0;
			addr[3]=0;
		}
		int size=addr.length;
		currentLanInterfaceNetworkAddr=new byte[size];
        PcapIf ifc=networkInterfaces.elementAt(this.lanSideInterface);
		PcapAddr paddr=this.getIpV4Address(ifc);
//		PcapAddr addr=this.getCurrentInterfacePcapAddr();
        PcapSockAddr pmaskaddr=null;
		currentLanInterfaceNetmask=null;
        if(paddr!=null){
            pmaskaddr=paddr.getNetmask();
            currentLanInterfaceNetmask=pmaskaddr.getData();
		    for(int i=0;i<size;i++){
			   currentLanInterfaceNetworkAddr[i]=
				  (byte)(0xff & ((0xff & addr[i])&( 0xff & currentLanInterfaceNetmask[i])));
		   }	
        }
        else{
        	currentLanInterfaceNetmask=new byte[size];
        	for(int i=0;i<size;i++) currentLanInterfaceNetmask[i]=0x00;
        }
	}
	public byte[] getCurrentLanInterfaceNetworkAddr(){
		return currentLanInterfaceNetworkAddr;
	}
	public byte[] getCurrentLanInterfaceNetmask(){
		return currentLanInterfaceNetmask;
	}
	
	byte[] currentWanInterfaceNetworkAddr;
	byte[] currentWanInterfaceNetmask;
	private void setCurrentWanInterfaceNetworkAddr(){
		byte[] addr=getCurrentWanInterfaceIpAddr();
		if(addr==null){
			// assume ipv4 only
			addr=new byte[4];
			addr[0]=0;
			addr[1]=0;
			addr[2]=0;
			addr[3]=0;
		}
		int size=addr.length;
		currentWanInterfaceNetworkAddr=new byte[size];
        PcapIf ifc=networkInterfaces.elementAt(this.wanSideInterface);
		PcapAddr paddr=this.getIpV4Address(ifc);
//		PcapAddr addr=this.getCurrentInterfacePcapAddr();
        PcapSockAddr pmaskaddr=null;
		currentWanInterfaceNetmask=null;
        if(paddr!=null){
            pmaskaddr=paddr.getNetmask();
            currentWanInterfaceNetmask=pmaskaddr.getData();
		    for(int i=0;i<size;i++){
			   currentWanInterfaceNetworkAddr[i]=
				(byte)(0xff & ((0xff & addr[i])&( 0xff & currentWanInterfaceNetmask[i])));
		    }	
        }
        else{
        	currentWanInterfaceNetmask=new byte[size];
        	for(int i=0;i<size;i++) currentWanInterfaceNetmask[i]=0x00;        	
        }

	}
	public byte[] getCurrentWanInterfaceNetworkAddr(){
		return currentWanInterfaceNetworkAddr;
	}
	public byte[] getCurrentWanInterfaceNetmask(){
		return currentWanInterfaceNetmask;
	}
	public boolean equalAddr(byte[] a1, byte[] a2){
		int l1=a1.length;
		int l2=a2.length;
		if(l1!=l2) return false;
		for(int i=0;i<l1;i++){
			byte c1=a1[i];
			byte c2=a2[i];
			if(c1!=c2) return false;
		}
		return true;
	}
	public void copyAddr(byte[] a1, byte[] a2){
		int l1=a1.length;
		int l2=a2.length;
		if(l2>=l1) {
		   for(int i=0;i<l1;i++){
			byte c1=a1[i];
			a2[i]=c1;
		   }
		}
		else{
			   for(int i=0;i<l2;i++){
					byte c1=a1[i];
					a2[i]=c1;
				   }
			
		}
	}
	PacketMonitorFilter monitorFilter;
	TrafficLogManager logManager;
	public void setMonitorFilter(PacketMonitorFilter f){
		monitorFilter=f;
		logManager = new TrafficLogManager(f);		
	}
	
	private JCheckBox getGrepCheckBox() {
		if(grepCheckBox == null) {
			grepCheckBox = new JCheckBox();
			grepCheckBox.setText("grep");
			grepCheckBox.setBounds(148, 10, 68, 23);
		}
		return grepCheckBox;
	}
	
	private JTextField getRegularExpressionField() {
		if(regularExpressionField == null) {
			regularExpressionField = new JTextField();
			regularExpressionField.setBounds(216, 9, 221, 26);
		}
		return regularExpressionField;
	}
	
	private JLabel getRegularExpressionFieldLabel() {
		if(regularExpressionFieldLabel == null) {
			regularExpressionFieldLabel = new JLabel();
			regularExpressionFieldLabel.setText("(regular expression)");
			regularExpressionFieldLabel.setBounds(443, 12, 141, 19);
		}
		return regularExpressionFieldLabel;
	}

}


