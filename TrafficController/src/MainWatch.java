import javax.swing.*;

import pukiwikiCommunicator.PukiwikiCommunicator;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Enumeration;

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
public class MainWatch extends JFrame {
	Container gcp = getContentPane();
	private JScrollBar showTermScrollBar;
	JPanel basepanel;
	MatrixPanel mpanel;
	JScrollPane scrollpanel;
	JMenuBar jmb1;
	JMenu jmFile;
	JMenu jmProtocol;
	JMenu jmDirection;
	JMenu jmTerm;
//	JMenu jmClear;
	JMenuItem fileSave;
	JMenuItem fileOpen;
	private JRadioButton tcp;
	private JRadioButton udp;
	private JRadioButton otherProtocol;
	private JRadioButton allProtocol;
	private ButtonGroup protocolGroup;
	private JRadioButton inDirection;
	private JRadioButton outDirection;
	private JRadioButton allDirection;
	private JRadioButton insideOnly;
	private ButtonGroup directionGroup;
	private JToggleButton watchingButton;
	private JToggleButton showSendingToggleButton;
//	public JTextField messageField;
	private JButton exitButton;
	private JButton settingButton;
	private JButton clearButton;
	private JButton stopButton;
	private JToggleButton startButton;
	private JLabel toLabel;
	private JTextField endTimeField;
	private JTextField statTimeField;
	private ButtonGroup termGroup;
	private JRadioButton allTerm;
	private JRadioButton oneHourTerm;
	private JRadioButton tenMinTerm;
	private JRadioButton oneMinTerm;
	private JRadioButton tenSecTerm;
	public String iv,pt;
	MainFrame main;
	public VisualTrf vtraffic[][];
	long showTerm;
	long showStartTime;
	long showEndTime;
	int termBarMax=600;
	
	public MainWatch(){
		super();
	}
	public MainWatch(MainFrame m){
		main=m;
		this.initGUI();
	}
	private void initGUI(){
		setTitle("Traffic Viewer   Yamanoue Laboratory, Kagoshima University");
		this.setSize(1119, 747);
		setVisible(true);
		gcp = getContentPane();
		gcp.setLayout(null);
		gcp.setBackground(Color.GRAY);

		jmb1 = new JMenuBar();
		jmFile = new JMenu("Interval");
		jmProtocol = new JMenu("Protocol");
		jmDirection = new JMenu("Direction");
		jmTerm = new JMenu("Term");
//		jmClear = new JMenu("clear");
		fileSave = new JMenuItem("all");
		fileOpen = new JMenuItem("10sec");
		allProtocol = new JRadioButton("all");
		tcp = new JRadioButton("tcp");
		udp = new JRadioButton("udp");
		otherProtocol = new JRadioButton("other protocol");
		protocolGroup = new ButtonGroup();
		protocolGroup.add(tcp);
		protocolGroup.add(udp);
		protocolGroup.add(otherProtocol);
		protocolGroup.add(allProtocol);
		allProtocol.setSelected(true);
    	protocolBits=0x07;
//
		allTerm= new JRadioButton("all");
		oneHourTerm= new JRadioButton("1H");
		oneHourTerm.setActionCommand("3600000");
		tenMinTerm= new JRadioButton("10Min");
		tenMinTerm.setActionCommand(  "600000");
		oneMinTerm= new JRadioButton("1Min");	
		oneMinTerm.setActionCommand(   "60000");
		tenSecTerm= new JRadioButton("10Sec");
		tenSecTerm.setActionCommand(   "10000");
	    termGroup = new ButtonGroup();
	    termGroup.add(allTerm);
	    termGroup.add(oneHourTerm);
	    termGroup.add(tenMinTerm);
	    termGroup.add(oneMinTerm);
	    termGroup.add(tenSecTerm);
	    oneMinTerm.setSelected(true);
    	showTerm=60*1000;
//
		allDirection= new JRadioButton("all");
		inDirection=new JRadioButton("from");
		outDirection=new JRadioButton("to");
		insideOnly = new JRadioButton("inside");
		directionGroup = new ButtonGroup();
		directionGroup.add(allDirection);
		directionGroup.add(inDirection);
		directionGroup.add(outDirection);
		directionGroup.add(insideOnly);
		allDirection.setSelected(true);
    	directionBits=0x07;
		//
		iv = "all";
		pt = "all";
		//inbounds = new JMenuItem("inbounds");
		//outbounds = new JMenuItem("outbounds");
		
		setJMenuBar(jmb1);
		jmb1.add(jmFile);
		jmFile.setText("File");
		jmb1.add(jmProtocol);
		jmb1.add(jmDirection);
		jmb1.add(jmTerm);
		/*
		jmb1.add(jmClear);
		jmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jmClearActionPerformed(evt);
			}
		});
        */
		jmFile.add(fileOpen);
		fileOpen.setText("Open");
        fileOpen.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent evt) {
        		e10secMouseClicked(evt);
        	}
        });
		jmFile.add(fileSave);
		fileSave.setText("Save");
		fileSave.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				all1MouseClicked(evt);
			}
		});
		//
		jmProtocol.add(allProtocol);
		allProtocol.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				allProtocolMouseClicked(evt);
			}
		});
		jmProtocol.add(tcp);
		tcp.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				tcpMouseClicked(evt);
			}
		});
		jmProtocol.add(udp);
		udp.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				udpMouseClicked(evt);
			}
		});
		jmProtocol.add(otherProtocol);
		otherProtocol.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				otherProtocolMouseClicked(evt);
			}
		});

//		
		jmTerm.add(allTerm);
        allTerm.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent evt) {
//		       allTermMouseClicked(evt);
	        }
        });
		jmTerm.add(oneHourTerm);
		oneHourTerm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
//				oneHourTermMouseClicked(evt);
			}
		});
		jmTerm.add(tenMinTerm);
		tenMinTerm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
//				tenMinTermMouseClicked(evt);
			}
		});
		jmTerm.add(oneMinTerm);
		oneMinTerm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
//				oneMinTermMouseClicked(evt);
			}
		});
		jmTerm.add(tenSecTerm);
		oneMinTerm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
//				tenSecTermMouseClicked(evt);
			}
		});
		//
		jmDirection.add(allDirection);
		allDirection.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				allDirectionMouseClicked(evt);
			}
		});
		jmDirection.add(inDirection);
		inDirection.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				inDirectionMouseClicked(evt);
			}
		});
		jmDirection.add(outDirection);
		outDirection.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				outDirectionMouseClicked(evt);
			}
		});
		jmDirection.add(insideOnly);
		insideOnly.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				insideOnlyMouseClicked(evt);
			}
		});
		//
		basepanel = new JPanel();
		basepanel.setLayout(null);
		gcp.add(basepanel);
		//
		mpanel = new MatrixPanel(this);
		mpanel.setPreferredSize(new java.awt.Dimension(1010, 1010));
		//mpanel.addMouseListener(new LetShowDetailListener());
		scrollpanel = new JScrollPane();
		scrollpanel.setViewportView(mpanel);
		//basepanel.add(scrollpanel,BorderLayout.CENTER);
		//basepanel.add(scrollpanel);
		basepanel.add(scrollpanel);
		scrollpanel.setBounds(5, 59, 1051, 580);
		{
			showTermScrollBar = new JScrollBar(Scrollbar.HORIZONTAL, 0, this.termBarMax, 0, this.termBarMax);
			basepanel.add(showTermScrollBar);
			showTermScrollBar.setBounds(5, 31, 600, 22);
			showTermScrollBar.addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent evt) {
					showTermScrollBarMouseDragged(evt);
				}
			});
		}
		{
			statTimeField = new JTextField();
			basepanel.add(statTimeField);
			statTimeField.setBounds(610, 30, 180, 25);
		}
		{
			endTimeField = new JTextField();
			basepanel.add(endTimeField);
			endTimeField.setBounds(840, 30, 180, 25);
		}
		{
			toLabel = new JLabel();
			basepanel.add(toLabel);
			toLabel.setText("to");
			toLabel.setBounds(795, 30, 40, 21);
		}
		{
			startButton = new JToggleButton();
			basepanel.add(startButton);
			startButton.setText("Start");
			startButton.setBounds(0, 1, 100, 27);
			startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					startButtonActionPerformed(evt);
				}
			});
		}
		{
			stopButton = new JButton();
			basepanel.add(stopButton);
			stopButton.setText("Stop");
			stopButton.setBounds(100, 1, 100, 27);
			stopButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					stopButtonActionPerformed(evt);
				}
			});
		}
		{
			clearButton = new JButton();
			basepanel.add(clearButton);
			clearButton.setText("Clear");
			clearButton.setBounds(200, 1, 100, 27);
			clearButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					clearButtonActionPerformed(evt);
				}
			});
		}
		{
			watchingButton = new JToggleButton();
			basepanel.add(watchingButton);
			watchingButton.setText("Watching");
			watchingButton.setBounds(300, 1, 110, 27);
			watchingButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					watchingButtonActionPerformed(evt);
				}
			});
		}
		{
			showSendingToggleButton = new JToggleButton();
			basepanel.add(showSendingToggleButton);
			showSendingToggleButton.setText("Sending");
			showSendingToggleButton.setBounds(410, 1, 100, 27);
			showSendingToggleButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					showSendingToggleButtonActionPerformed(evt);
				}
			});
		}
		{
			settingButton = new JButton();
			basepanel.add(settingButton);
			settingButton.setText("Setting");
			settingButton.setBounds(510, 1, 100, 27);
			settingButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					settingButtonActionPerformed(evt);
				}
			});
		}
		{
			exitButton = new JButton();
			basepanel.add(exitButton);
			exitButton.setText("Exit");
			exitButton.setBounds(610, 1, 100, 27);
			exitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					exitButtonActionPerformed(evt);
				}
			});
		}

		basepanel.setBounds(5, 5, 1084, 663);
		vtraffic = new VisualTrf[256][256];
		this.setVisible(true);
		gcp.setBounds(-8, 39, 1184, 643);
		gcp.setPreferredSize(new java.awt.Dimension(1103, 668));
//        this.allTermMouseClicked(null);
		this.oneMinTermMouseClicked(null);
        this.allDirectionMouseClicked(null);
        this.allProtocolMouseClicked(null);
	}

	public void stop(){
		if(this.mpanel!=null) mpanel.stop();
	}
	public long getScrollBarEndTime(){
		long t1=this.main.getFirstTime();
		long t2=this.main.getLatestTime();
		if(this.showTerm<0){
			return t2;
		}
		int v=this.showTermScrollBar.getValue();
		int l=this.showTermScrollBar.getVisibleAmount();
		double endTimeRatio=((double)(v+l)/((double)this.termBarMax));
		long rtn=t1+(long)((t2-t1)*endTimeRatio);
		if(endTimeRatio>0.95)
		   rtn=t2;
		return rtn;
	}
	public long getScrollBarStartTime(){
		long t1=this.main.getFirstTime();
		long t2=this.main.getLatestTime();
		if(this.showTerm<0){
			return t1;
		}
		int v=this.showTermScrollBar.getValue();
		int l=this.showTermScrollBar.getVisibleAmount();
		long rtn=t1+(long)((t2-t1)*((double)v)/((double)this.termBarMax));
		return rtn;
	}
	public void showTermScrollBarMouseDragged(MouseEvent evt) {
//		System.out.println("showTermScrollBar.mouseDragged, event="+evt);
		//TODO add your code for showTermScrollBar.mouseDragged
		this.statTimeField.setText(""+new Date(this.getScrollBarStartTime()));
		this.endTimeField.setText(""+new Date(this.getScrollBarEndTime()));
		this.repaint();
	}
    public void setTermScrollBar(){
		long t1=this.main.getFirstTime();
		long t2=this.main.getLatestTime();
		this.setShowTerm();
		if(t2-t1<=0) return;
		if(this.showTerm<0){
			this.showTermScrollBar.setVisibleAmount(this.termBarMax);
			this.showTermScrollBar.setValue(0);
			this.statTimeField.setText(""+new Date(t1));
			this.endTimeField.setText(""+new Date(t2));
//			repaint();
			return;
		}
		int v=this.showTermScrollBar.getValue();
		int l=this.showTermScrollBar.getVisibleAmount();
		double endTimeRatio=((double)(v+l)/((double)this.termBarMax));
 	    int nl=(int)(((double)this.termBarMax)*
   			((double)showTerm)/((double)(t2-t1)));
 	    if(nl>this.termBarMax){
 		   this.showTermScrollBar.setVisibleAmount(this.termBarMax);
 		   nl=this.showTermScrollBar.getVisibleAmount();
 	    }
    	this.showTermScrollBar.setVisibleAmount(nl);
    	int nv=0;
		if(endTimeRatio>0.98){
			nv=this.termBarMax-nl;
		}
		else{
	    	nv=(int)(endTimeRatio*(this.termBarMax))-nl;
		}
    	this.showTermScrollBar.setValue(nv);
		this.statTimeField.setText(""+new Date(this.getScrollBarStartTime()));
		this.endTimeField.setText(""+new Date(this.getScrollBarEndTime()));
		/*
		this.main.writeApplicationMessage("endTimeRatio="+endTimeRatio
				+" v="+v+" l"+l
				+" nv="+nv+" nl="+nl+" barMax="+this.termBarMax);
				*/
//		this.repaint();
    }

    private void oneMinTermMouseClicked(MouseEvent evt) {
    	System.out.println("oneMinDuration.mouseClicked, event="+evt);
    	//TODO add your code for oneMinDuration.mouseClicked
    	this.showTerm=60*1000;
    	this.setTermScrollBar();
    	this.repaint();
    }

    public int protocolBits=0;
    private void tcpMouseClicked(MouseEvent evt) {
//    	System.out.println("tcp.mouseClicked, event="+evt);
    	//TODO add your code for tcp.mouseClicked
    	protocolBits=0x01;
    }
    
    private void udpMouseClicked(MouseEvent evt) {
//    	System.out.println("udp.mouseClicked, event="+evt);
    	//TODO add your code for udp.mouseClicked
    	protocolBits=0x02;
    }
	private void otherProtocolMouseClicked(MouseEvent evt){
    	protocolBits=0x04;		
	}

    private void allProtocolMouseClicked(MouseEvent evt) {
//    	System.out.println("all2.mouseClicked, event="+evt);
    	//TODO add your code for all2.mouseClicked
    	protocolBits=0x07;
    }
    
    private void e10secMouseClicked(MouseEvent evt) {
//    	System.out.println("e10sec.mouseClicked, event="+evt);
    	//TODO add your code for e10sec.mouseClicked
    }
    
    private void all1MouseClicked(MouseEvent evt) {
//    	System.out.println("all1.mouseClicked, event="+evt);
    	//TODO add your code for all1.mouseClicked
    }
    
    public int directionBits=0;    
    private void allDirectionMouseClicked(MouseEvent evt) {
    	System.out.println("allDirection.mouseClicked, event="+evt);
    	//TODO add your code for allDirection.mouseClicked
    	directionBits=0x07;
    }
    
    private void inDirectionMouseClicked(MouseEvent evt) {
    	System.out.println("inDirection.mouseClicked, event="+evt);
    	//TODO add your code for inDirection.mouseClicked
    	directionBits=0x02;
    }
    
    private void outDirectionMouseClicked(MouseEvent evt) {
    	System.out.println("outDirection.mouseClicked, event="+evt);
    	//TODO add your code for outDirection.mouseClicked
    	directionBits=0x01;
    }
    public void insideOnlyMouseClicked(MouseEvent evt){
    	System.out.println("outDirection.mouseClicked, event="+evt);
    	//TODO add your code for outDirection.mouseClicked
    	directionBits=0x04;
    }
    private void startButtonActionPerformed(ActionEvent evt) {
    	System.out.println("startButton.actionPerformed, event="+evt);
    	//TODO add your code for startButton.actionPerformed
    	this.main.startButtonActionPerformed(null);
    }
    
    private void stopButtonActionPerformed(ActionEvent evt) {
    	System.out.println("stopButton.actionPerformed, event="+evt);
    	//TODO add your code for stopButton.actionPerformed
    	this.main.stopButtonActionPerformed(null);
    }
    
    private void clearButtonActionPerformed(ActionEvent evt) {
    	System.out.println("clearButton.actionPerformed, event="+evt);
    	//TODO add your code for clearButton.actionPerformed
    	this.main.clearButtonActionPerformed(null);
    }
    
    public void clearScreen(){
    	for(int i=0;i<256;i++){
    		for(int j=0;j<256;j++){
    			VisualTrf vt=this.vtraffic[i][j];
    			if(vt!=null) this.vtraffic[i][j]=null;
    		}
    	}
    }
    public void clearData(){
    	for(int i=0;i<256;i++){
    		for(int j=0;j<256;j++){
    			VisualTrf vt=this.vtraffic[i][j];
    			if(vt!=null) vt.clearData();
    		}
    	}    	
    }
    
    private void settingButtonActionPerformed(ActionEvent evt) {
    	System.out.println("settingButton.actionPerformed, event="+evt);
    	//TODO add your code for settingButton.actionPerformed
    	this.main.setVisible(true);
    }
    
    private void exitButtonActionPerformed(ActionEvent evt) {
    	System.out.println("exitButton.actionPerformed, event="+evt);
    	//TODO add your code for exitButton.actionPerformed
    	this.main.exitButtonActionPerformed(null);
    }
    public void setStartButtonValue(boolean x){
    	this.startButton.setSelected(x);
    }
    public void setShowTerm(){
    	Enumeration em=termGroup.getElements();
    	while(em.hasMoreElements()){
    		JRadioButton b=(JRadioButton)(em.nextElement());
    		if(b.isSelected()){
    		   String act=b.getActionCommand();
    		   this.showTerm=(new Long(act)).longValue();
    		}
    	}
    }
    
    private void watchingButtonActionPerformed(ActionEvent evt) {
    	System.out.println("watchingButton.actionPerformed, event="+evt);
    	//TODO add your code for watchingButton.actionPerformed
    	if(this.watchingButton.isSelected()){
    		if(this.main!=null){
    			PukiwikiCommunicator com=this.main.pukiwikiCommunicator;
    			com.start();
    			com.startWatchingButton.setSelected(true);
    		}
    	}
    	else{
    		if(this.main!=null){
    			PukiwikiCommunicator com=this.main.pukiwikiCommunicator;
    			com.stop();
    			com.startWatchingButton.setSelected(false);
    		}
    	}
    }
    
    private void showSendingToggleButtonActionPerformed(ActionEvent evt) {
//    	System.out.println("showSendingToggleButton.actionPerformed, event="+evt);
    	//TODO add your code for showSendingToggleButton.actionPerformed
    	if(this.showSendingToggleButton.isSelected()){
    		if(this.main!=null){
    			PukiwikiCommunicator com=this.main.pukiwikiCommunicator;
    			if(com.onlineCommandRefreshButton.isSelected()){
    				com.connectButton.setSelected(true);
    				com.connectButtonActionPerformed(null);
//    				com.start();
//    				com.startWatchingButton.setSelected(true);
    			}
    		}
     	}
    	else{
    		if(this.main!=null){
    			PukiwikiCommunicator com=this.main.pukiwikiCommunicator;
//    			com.onlineCommandRefreshButton.setSelected(false);
    			com.connectButton.setSelected(false);
    		}
    	}
    }
    public void repaint(){
    	this.setTermScrollBar();
    }
}
