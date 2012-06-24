import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.awt.*;
import javax.swing.*;
public class VisualTrf {
	private int number,numberTo,numberFrom;
	private int number10,numberTo10,numberFrom10;
	private int ip;
	private int tcp,tcpTo,tcpFrom;
	private int tcp10,tcpTo10,tcpFrom10;
	private int udp,udpTo,udpFrom;
	private int udp10,udpTo10,udpFrom10;
	private String protocol;
	private int uipa;
	private int uport;
	public String capterform;
	private String time;
	private String bound;
	private String natureform;
	private String me,partner;
	private DetailPix[] depix;
	
	private JFrame detailFrame;
	private JTabbedPane jTab;
	private MainFrame mainFrame;
	private Vector <PData> pdata;
	public int directionMaskTo=0x01;
	public int directionMaskFrom=0x02;
	public int protocolMaskTCP=0x01;
	public int protocolMaskUDP=0x02;
	public int protocolMaskOTR=0x04;
	public int paneNumber;
	
	private class PData{
		public long frameNumber;
		public long longTime;
		public String [] state;
		public int direction;
		public String match;
		public PData(long fNum, long lt, String[] st, int i, String mt){
			frameNumber=fNum;
			longTime=lt;
			state=st;
			direction=i;
			match=mt;
		}
	}
	
	public VisualTrf(MainFrame m,long frameNumber, long lt, String[] state,
			int [] address, int direction, String match){
		pdata=new Vector();
		PData pdatax=new PData(frameNumber,lt,state,direction,match);
		pdata.add(pdatax);
		mainFrame=m;
		if(direction!=2){
          this.uipa=address[9];
          this.uport=address[11];
		}
		else{
			this.uipa=address[3];
			this.uport=address[5];
		}
	}
	
	//�h�o�A�h���X�̏d���m�F
	public boolean existIP(String ipaddress){
		String ip = ipaddress;
		for(int i=0;i<paneNumber;i++){
			String xip = depix[i].partner;
			
			if(xip.equals(ip)){
				return true;
			}
		}
		return false;
	
	}
	// output ip address matrix number
        public int getNum(String ipaddress){
               String ip = ipaddress;
		for(int i=0; i<paneNumber; i++){
			String xip =depix[i].partner;
			if(xip.equals(ip)){
				return i;
			}
		}
		return paneNumber;
	}
	
	public void coutup(long frameNumber,long lt, 
			   String[] state,int direction, String match){
		PData pdatax=new PData(frameNumber,lt,state,direction,match);
		pdata.add(pdatax);
	}

	public void removeThis(){

	}

	//8/15�ύX
	/*
	public synchronized void appendDetail(String b,String t,String p){
		
		capterbuffer= capterbuffer+(b+"\t"+t+"\t"+p+"\n");
	}
	public void removeDetail(){
		capterform = new String(capterbuffer);
		capterbuffer = new String(" states    :   protocol    :    host :\n");
	}
	
	*/
	
	private interface FilterAndMethod{
		public int method(PData x);
	}
	private int packetNumber;
	private int directionBits;
	private int protocolBits;
	private int filter(FilterAndMethod m, long stime, long etime, int directionBits, int protocolBits){
		PData firstPData=pdata.elementAt(0);
		PData lastPData=pdata.elementAt(pdata.size()-1);
		if(firstPData!=null){
		  if(firstPData.longTime>etime) return 0;
		}
		if(lastPData!=null){
		  if(lastPData.longTime<stime) return 0;
		}
		packetNumber=0;
		int flag=1;
		for(int i=0;i<pdata.size();i++){
			PData pdatax=pdata.elementAt(i);
			String protocol=pdatax.state[2];
			int thisProtocol=0;
			if(protocol.equals("tcp"))
				thisProtocol=0x01;
			else
			if(protocol.equals("udp"))
				thisProtocol=0x02;
			else
				thisProtocol=0x04;
			String directionX="";
			String fromx = pdatax.state[3];
			String tox=pdatax.state[4];
			if(pdatax.longTime<=etime){
				if(pdatax.longTime>=stime){
					if(((pdatax.direction & directionBits)!=0) &&
					   ((thisProtocol  & protocolBits)!=0)){
						int x=m.method(pdatax);
						if(x==2) flag=x;
					}
				}
			}
		}
		return flag;		
	}

	public class FilterAndCount implements FilterAndMethod {
		public FilterAndCount(){};
		public int method(PData x){
			packetNumber++;
			if(x.match!=null){
				if(x.match!="")
					return 2;
			}
			return 1;
		}
	}
	public class FilterAndShowDetail implements FilterAndMethod {
		public FilterAndShowDetail(){}
		public int method(PData x){
			String tox=x.state[4];
			String fromx=x.state[3];
			if(existIP(tox)==true){
				int s=getNum(tox);
				depix[s].appendDetail(x.frameNumber,x.state,x.direction,x.match);
			}
			else {
				depix[paneNumber]= new DetailPix(x.frameNumber,x.state,x.direction,x.match);
				depix[paneNumber].me=fromx;
				depix[paneNumber].partner=tox;
				paneNumber++;
			}
			return 1;
		}
	}
	FilterAndCount filterAndCount=new FilterAndCount();
	public void showPoint(Graphics g, int directionBits, int protocolBits){
		long stime=mainFrame.getScrollBarStartTime();
		long etime=mainFrame.getScrollBarEndTime();
		packetNumber=0;
		int e=this.filter(filterAndCount,stime,etime,directionBits,protocolBits);
		if(e==0) return;
		
		if(packetNumber != 0){
			if(e==2){
				g.setColor(Color.black);
			}
			else
			if(packetNumber <= 10){
				g.setColor(Color.GREEN);
			}
			else
			if(packetNumber <=30){
					g.setColor(Color.YELLOW);
			}
			else{
					g.setColor(Color.RED);
			}
		
			g.fillRect(uipa*4,uport*4,4,4);
			this.removeThis();
		}

	}	

	//8/5�ɉ��
	/*
	public void showDetail(){
		jf = new JFrame("Detail "+uipa+":"+uport);
		jsp = new JScrollPane();
		//		jta1 = new JTextArea(capterform);
		jta1 = new JTextArea(capterbuffer);
		jta2 = new JTextArea();
		jtp = new JTabbedPane();
		
		jf.setSize(400,400);
		jsp.setPreferredSize(new Dimension(400,400));
		jf.getContentPane().add(jsp);
		jsp.setViewportView(jta1);
		
		jf.pack();
		jf.setVisible(true);
		
		
	}
	*/
	public void showDetail(int protocolBits, int directionBits){
		String message="showDetail ["+uipa+":"+uport+"]";
		this.mainFrame.writeApplicationMessage(message);
		/* */
		if(this.detailFrame!=null){
			this.detailFrame.dispose();
		}
		/* */
		this.detailFrame = new JFrame("Dtail "+uipa+":"+uport);
//		if(this.detailFrame.isVisible()) return;
		
		long stime=mainFrame.getScrollBarStartTime();
		long etime=mainFrame.getScrollBarEndTime();
		depix = new DetailPix[100];
		paneNumber=0;
		int ec=this.filter(new FilterAndShowDetail(),stime,etime,directionBits,protocolBits);
		if(ec==0) return;
        this.mainFrame.writeApplicationMessage("-on");
		jTab = new JTabbedPane(JTabbedPane.TOP);
		for(int i =0; i<paneNumber; i++){
			if(depix[i]!=null){
				String hostaddress=depix[i].me;
				String hostname="";
	            try {
	                InetAddress[] addressList = InetAddress.getAllByName(hostaddress);
	                hostname=addressList[0].getHostName();
	                System.out.println("\t" + hostname);
	                for( int k = 0 ; k < addressList.length ; k++ ){
	                    System.out.println("\t" + addressList[k].getHostAddress());
	                }
	            }
	            catch( UnknownHostException e ){
	                System.err.println("Unable to find address for " + hostaddress);
	            }

//				depix[i].makeaDetail();
//				jTab.add(depix[i].me, depix[i].detailTxt);
//				jTab.add(depix[i].me, depix[i].capterbuffer);
				jTab.add(hostaddress+": "+hostname, depix[i].tablePane);
//				System.out.println(depix[i].detailTxt.getText());
			}
			
		}
		detailFrame.add(jTab);
		detailFrame.setSize(1000,400);
//		detailFrame.pack();
		detailFrame.setVisible(true);
		
	}
	public void clearScreen(){
		
	}
	public void clearData(){
		this.pdata.removeAllElements();
	}
}
