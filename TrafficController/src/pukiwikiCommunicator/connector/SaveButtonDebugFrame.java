package pukiwikiCommunicator.connector;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import pukiwikiCommunicator.language.HtmlTokenizer;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


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
public class SaveButtonDebugFrame extends JFrame implements AuthDialogListener
{
	private JButton saveButton;
	private String baseUrl;
	private String pageName;
	private String plugInName;
	private String pageCharSet;
	private String charset;

	private static final int AUTH_MODE_PREEMPTIVE = 0;
    private static final int AUTH_MODE_NOT_PREEMPTIVE = 1;
    private static final int AUTH_MODE_CONSOLE = 2;
    private Properties setting;

	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
			}
			{
				saveButton = new JButton();
				getContentPane().add(saveButton);
				saveButton.setText("save");
				saveButton.setBounds(5, 0, 75, 31);
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						saveButtonActionPerformed(evt);
					}
				});
			}
			{
				clearButton = new JButton();
				getContentPane().add(clearButton);
				clearButton.setText("clear message");
				clearButton.setBounds(4, 34, 132, 31);
				clearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						clearButtonActionPerformed(evt);
					}
				});
			}
			{
				messagePane = new JScrollPane();
				getContentPane().add(messagePane);
				messagePane.setBounds(5, 140, 527, 469);
				{
					messageTextArea = new JTextArea();
					messagePane.setViewportView(messageTextArea);
//					messageTextArea.setBounds(236, 172, 61, 12);
//					messageTextArea.setPreferredSize(new java.awt.Dimension(521, 501));
					messagePane.setAutoscrolls(true);
				}
			}
			{
				inputField = new JTextField();
				getContentPane().add(inputField);
				inputField.setBounds(5, 101, 455, 39);
			}
			{
				sendButton = new JButton();
				getContentPane().add(sendButton);
				sendButton.setText("send");
				sendButton.setBounds(459, 101, 73, 38);
				sendButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						sendButtonActionPerformed(evt);
					}
				});
			}
			{
				urlField = new JTextField();
				getContentPane().add(urlField);
				urlField.setBounds(5, 65, 454, 34);
			}
			{
				connectButton = new JButton();
				getContentPane().add(connectButton);
				connectButton.setText("con");
				connectButton.setBounds(459, 65, 74, 36);
				connectButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						(new Thread(){
							public void run(){
						       connectButtonActionPerformed(null);
							}
						}).start();
					}
				});
			}
			{
				editPageButton = new JButton();
				getContentPane().add(editPageButton);
				editPageButton.setText("edit");
				editPageButton.setBounds(158, 0, 75, 31);
				editPageButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						editPageButtonActionPerformed(evt);
					}
				});
			}
			{
				updateButton = new JButton();
				getContentPane().add(updateButton);
				updateButton.setText("update");
				updateButton.setBounds(233, 0, 84, 31);
				updateButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						updateButtonActionPerformed(evt);
					}
				});
			}
			{
				paramButton = new JButton();
				getContentPane().add(paramButton);
				paramButton.setText("params");
				paramButton.setBounds(317, 0, 91, 31);
				paramButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
					}
				});
			}
			{
				readFromPukiWikiButton = new JButton();
				getContentPane().add(readFromPukiWikiButton);
				readFromPukiWikiButton.setText("read");
				readFromPukiWikiButton.setBounds(80, 1, 78, 30);
				readFromPukiWikiButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
					}
				});
			}
			{
				showMessagesCheckBox = new JCheckBox();
				getContentPane().add(showMessagesCheckBox);
				showMessagesCheckBox.setText("Show messages");
				showMessagesCheckBox.setBounds(147, 38, 154, 23);
				showMessagesCheckBox.setSelected(false);
			}
			{
				this.setSize(551, 650);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveButtonActionPerformed(ActionEvent evt) {
//		System.out.println("saveButton.actionPerformed, event="+evt);
		//TODO add your code for saveButton.actionPerformed
		this.editPageButtonActionPerformed(null);
		this.updateButtonActionPerformed(null);
	}
	public void saveText(String x){
		this.editPageButtonActionPerformed(null);
		this.replaceTextWith(x);
	}
	
	private JButton clearButton;
	private JTextArea messageTextArea;
	private JCheckBox showMessagesCheckBox;
	private JButton readFromPukiWikiButton;
	private JButton paramButton;
	private JButton updateButton;
	private JScrollPane messagePane;
	private JButton editPageButton;
	private JButton connectButton;
	private JTextField urlField;
	private JButton sendButton;
	private JTextField inputField;
	private boolean authInputFlag;
	private boolean loginButtonPressed;
	private PukiwikiJavaApplication application;

	public SaveButtonDebugFrame(PukiwikiJavaApplication a,Properties p){
		this.application=a;
		this.setting=p;
//		this.messageTextArea.append("documentBase="+this.applet.getDocumentBase().toString()+"\n");
		this.println("y2");
		this.initGUI();
		this.println("saveButtonDebugFrame");
		this.setName("saveButtonFrame");
		charset="UTF-8";
	}
		
	private void clearButtonActionPerformed(ActionEvent evt) {
		this.println("clearButton.actionPerformed, event="+evt);
		//TODO add your code for clearButton.actionPerformed
		this.messageTextArea.setText("");
	}
	private String getUrlWithoutParameters(String url){
		int i=url.indexOf("?");
		if(i<0) return url;
		String rtn=url.substring(0,i);
		return rtn;
	}
	
	HttpClient client=null;
	
	AuthDialog authDialog=null;
	private void connectButtonActionPerformed(ActionEvent evt){
		String url=this.urlField.getText();
		this.println("url="+url);
		connectToURL(url);
	}
	private String connectToURL(String url) {
		this.println("connectButton.actionPerformed, url="+url);
		//TODO add your code for connectButton.actionPerformed
		String pageText=null;
		client=new HttpClient();
//		this.messageTextArea.append(url+"\n");
		String urlWithoutParameters="";
		if(authDialog!=null){
			this.println("authDialog is not null");
			urlWithoutParameters=getUrlWithoutParameters(url);
			String registeredUrl=authDialog.getProperty("auth-url");
			this.println("urlWithoutParamaters="+urlWithoutParameters);
			this.println("registeredUrl="+registeredUrl);
			if(registeredUrl==null){
				this.authDialog=null;
				pageText=this.connectToURL(url);
				return pageText;
			}
			if(registeredUrl.equals(urlWithoutParameters)){
				this.println("registeredUrl == urlWithoutParameters");
				client.getParams().setAuthenticationPreemptive(true);
			    // �F�؏��(���[�U���ƃp�X���[�h)�̍쐬.
				String uname=authDialog.getID();
				char[] pwd=authDialog.getPassword();
				String pwdx=new String(pwd);
				String idPass=uname+":"+pwdx;
      	        String authUrl="basicAuth-"+urlWithoutParameters;
    	        this.setting.setProperty(authUrl,idPass);
//			    Credentials defaultcreds1 = new UsernamePasswordCredentials(uname, pwdx);
				Credentials defaultcreds1 = new UsernamePasswordCredentials(idPass);
			    // �F�؂̃X�R�[�v.
			    AuthScope scope1 = new AuthScope(null, -1, null);
			    // �X�R�[�v�ƔF�؏��̑g�������Z�b�g.
			    client.getState().setCredentials(scope1, defaultcreds1);				
			}
			else{
				this.authDialog=null;
				pageText=this.connectToURL(url);
				return pageText;
			}
		}
		try{
			this.println("new getMethiod("+url+")");
    		HttpMethod method = new GetMethod(url);
//    		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//					new DefaultHttpMethodRetryHandler(3, false));
    		if(this.authDialog!=null){
        	   method.setDoAuthentication(true);
    		}

//	    	method.getParams().setContentCharset("UTF-8");
			int status=client.executeMethod(method);
		    if (status != HttpStatus.SC_OK) {
		          this.println("Method failed: " + method.getStatusLine());
		          if((method.getStatusLine()).toString().indexOf("401")>=0){
		        	  this.authDialog=new AuthDialog(this);
		        	  this.authInputFlag=false;
		        	  urlWithoutParameters=getUrlWithoutParameters(url);
		        	  this.authDialog.setProperty("auth-url", urlWithoutParameters);
		        	  this.println("before waitUntilMessageIsReturned");
//		        	  this.authDialog.start();
		        	  this.authDialog.setVisible(true);
		        	  if(this.setting!=null){
			        	  urlWithoutParameters=getUrlWithoutParameters(url);
		        	      String authUrl="basicAuth-"+urlWithoutParameters;
		        	      String idPass=this.setting.getProperty(authUrl);
		        	      StringTokenizer st1 =new StringTokenizer(idPass,":");
		        	      String id="";
		        	      String pas="";
		        	      if(st1!=null){
		        	        id=st1.nextToken();
		        	        pas=st1.nextToken();
		        	        if(id!=null)
		        	          this.authDialog.setID(id);
		        	        if(pas!=null)
		        	          this.authDialog.setPassword(pas);
		        	      }
		        	  }

		        	  this.waitUntilMessageIsReturned(); //!!
//		        	  this.authDialog.stop(); //!!
		        	  this.authDialog.setVisible(false);
		        	  this.println("after waitUntilMessageIsReturned");
		        	  if(this.loginButtonPressed){
		        		  this.println("loginButtonPressed");
		        	     pageText=this.connectToURL(url);
		        	  }
		        	  return pageText;
		          }
		    }
		    else{
		    	pageText=this.getText(method);
		    	/*
//    		String txt=method.getResponseBodyAsString();
     		    InputStream is=method.getResponseBodyAsStream();
     		    InputStreamReader isr=new InputStreamReader(is,this.charset);
//	     	    InputStreamReader isr=new InputStreamReader(is,"UTF-8");
//     		    InputStreamReader isr=new InputStreamReader(is);
		         BufferedReader br=new BufferedReader(isr);
		        String line="";
		        pageText="";
		        while(true){
		    	   line=br.readLine();
		    	   pageText=pageText+line+"\n";
		        	if(line==null) break;
		    	    this.messageTextArea.append(line+"\n");
//		    	    System.out.println(line);
		        }
		        */
		    }
		}
		catch(Exception e){
			this.println(e.toString()+"\n");
			e.printStackTrace();
		}
		return pageText;
	}
	
	private String getText(HttpMethod method){
		String pageText="";
	    try{
		   InputStream is=method.getResponseBodyAsStream();
 		   InputStreamReader isr=new InputStreamReader(is,this.charset);
//     	    InputStreamReader isr=new InputStreamReader(is,"UTF-8");
// 		    InputStreamReader isr=new InputStreamReader(is);
	       BufferedReader br=new BufferedReader(isr);
	       String line="";
	       pageText="";
	       while(true){
	    	   line=br.readLine();
	    	   pageText=pageText+line+"\n";
	        	if(line==null) break;
	    	    this.messageTextArea.append(line+"\n");
//	    	    System.out.println(line);
	       }
	       method.releaseConnection();		
	    }
	    catch(Exception e){}
	    return pageText;
	}
	
	private void waitUntilMessageIsReturned(){
		while(!this.authInputFlag){
			try{
				Thread.sleep(100);
			}
			catch(InterruptedException e){
				
			}
		}
	}
	private void sendButtonActionPerformed(ActionEvent evt) {
		this.println("sendButton.actionPerformed, event="+evt);
		//TODO add your code for sendButton.actionPerformed
	}
	String updateText;
	String actionUrl;
	String editCmd;
	String editPage;
	String editDigest;
	String editWriteValue;
	String editEncodeHint;
    public void setBaseUrl(String url){
    	this.baseUrl=url;
    }
	private void editPageButtonActionPerformed(ActionEvent evt) {
		this.println("editPageButton.actionPerformed, event="+evt);
		//TODO add your code for editPageButton.actionPerformed
		   String editUrl=baseUrl+"?cmd=edit&page="+pageName;
		this.println("editUrl="+editUrl);
//		this.messageTextArea.append(baseUrl+"\n");
		this.println(baseUrl+"\n");
//		this.messageTextArea.append(editUrl+"\n");
		this.println(editUrl+"\n");
		this.urlField.setText(editUrl);
		this.messageTextArea.setText("");
		String x=this.connectToURL(editUrl);
//		String x=this.messageTextArea.getText();
		
		/* get the first form from the url*/
		String form=this.getBetween(x,"<form", "</form>");
		if(form==null) return;
		this.println("form="+form);
		/* get the head part in the text area from the form*/
		int i=form.indexOf("<textarea ");
		if(i<0) {
			this.println("Could not find out textarea");
			return;
		}
//		this.messageTextArea.setText("");
//		this.messageTextArea.append("i="+i+"\n");
		String y="";
		String z="";
		try{
		    y=form.substring(i);
		    z=y.substring(y.indexOf(">")+1);		
		}
		catch(Exception e){
			
		}
		this.println("z="+z);
//		this.println("plugInName="+this.plugInName);
//		int j=z.indexOf("#"+this.plugInName);
		int j=z.indexOf("result:");
		if(j<0){
			this.println("Could not find out result:");
			return;
		}
		int k=j+("result:").length();

		//String head=x.substring(0,k-1);
		this.println("j="+j+" k="+k);
		// has the command #netpaint argument? 
		String dataStart=z.substring(j);
		this.println("dataStart="+dataStart);
		/*
		if(dataStart.startsWith("result:")){
			String theCommand="result:" ;
			this.println("theCommand="+theCommand);
			k=j+theCommand.length();
		}
		*/
		this.println("j="+j+" k="+k);
		
		String head=z.substring(0,k);
		String w=z.substring(head.length());
		int l=w.indexOf("</textarea");
		String tail=w.substring(l);
		String body=w.substring(0,l-1);
		this.println("head="+head);
		this.println("body="+body);
		this.println("tail="+tail);
		this.updateText=head;
		String actionwork1=form.substring(0,form.indexOf(">"));
		this.println("actionwork1="+actionwork1);
		this.actionUrl=this.getTagProperty(actionwork1, "action");
		this.println("action url="+this.actionUrl);
		
		/* 
		 getting input properties
		 */
		HtmlTokenizer htmlt=new HtmlTokenizer(form);
		while(htmlt.hasMoreTokens()){
			String t=htmlt.nextToken();
			if(t.startsWith("<input")){
				String ttype=getTagProperty(t,"type");
				if(ttype.equals("hidden")){
				   String tname=getTagProperty(t,"name");
				   String tvalue=getTagProperty(t,"value");
				   this.println(" "+tname+"="+tvalue);
				   if(tname.equals("cmd")){
					   this.editCmd=tvalue;
				   }
				   if(tname.equals("page")){
					   this.editPage=tvalue;
				   }
				   if(tname.equals("digest")){
				   		this.editDigest=tvalue;
				   }
				   if(tname.equals("encode_hint")){
					   this.editEncodeHint=tvalue;
				   }
				}
				if(ttype.equals("submit")){
				   String tname=getTagProperty(t,"name");
				   String tvalue=getTagProperty(t,"value");
				   this.println(" "+tname+"="+tvalue);
				   if(tname.equals("write")){
						this.editWriteValue=tvalue;
			       }
				}
			}
		}
		
	}
	String insertSpaceAfterNewLine(String x){
		StringTokenizer st=new StringTokenizer(x,"\n",true);
		String rtn="";
		while(st.hasMoreTokens()){
			String t=st.nextToken();
			if(t.equals("\n")){
				rtn=rtn+t+" ";
			}
			else{
				rtn=rtn+t;
			}
		}
		return rtn;
	}
	
	private String getTagProperty(String tag, String key){
//		System.out.println("tag="+tag);
//		System.out.println("key="+key);
		StringTokenizer st=new StringTokenizer(tag," =",true);
		String t=st.nextToken();
//		System.out.println(" first token="+t);
		while(st.hasMoreTokens()){
			t=st.nextToken();
			if(t.equals(" ")){        // skip space
				while(t.equals(" ")){
					if(!st.hasMoreTokens()) return "";
					t=st.nextToken();
				}
			}
			String keyx=t;
//			System.out.println(" key?="+keyx);
			if(!st.hasMoreTokens()) return "";
			t=st.nextToken();
			if(t.equals(" ")){        // skip space
				while(t.equals(" ")){
					if(!st.hasMoreTokens()) return "";
					t=st.nextToken();
				}
			}
			if(t.equals("=")){
//				System.out.println("...=");
				if(!st.hasMoreTokens()) return "";
				t=st.nextToken();
				if(t.equals(" ")){
					if(!st.hasMoreTokens()) return "";
					t=st.nextToken();
				}
			    if(keyx.equals(key)){
//			    	System.out.println(" keyx="+key+" t="+t);
			    	if(t.startsWith("\"")){
			    			t=t.substring(1);
			    	}
			    	if(t.endsWith("\"")){
			    		t=t.substring(0,t.length()-1);
			    	}
			    	return t;
			    }
			}
			
		}
		return "";
	}
	
	private void updateButtonActionPerformed(ActionEvent evt) {
		String output=application.getOutput();
		replaceTextWith(output);
	}
	private void replaceTextWith(String x){
		// System.out.println("updateButton.actionPerformed, event="+evt);
		//TODO add your code for updateButton.actionPerformed
		
		/* make the body (fig) */
		/*
		try{
			fig=new String(fig.getBytes("UTF-8"),  "UTF-8");
		}
		catch(Exception e){
			return;
		}
		*/
		this.updateText=this.updateText+"\n "+insertSpaceAfterNewLine(x);
		
		this.urlField.setText(this.actionUrl);
		String url=this.urlField.getText();
		this.println("url="+url);
//		this.messageTextArea.append(this.updateText);
		   BufferedReader br = null;
//		System.out.println("updateText="+this.updateText);
		this.println("editWriteValue="+this.editWriteValue);
		this.println("editCmd="+this.editCmd);
		this.println("editPage="+this.editPage);
		this.println("digest="+this.editDigest);
		try{
    		PostMethod method = new PostMethod(url);
    		if(this.client==null) return;
    		method.getParams().setContentCharset(this.pageCharSet);
    		method.setParameter("msg",this.updateText);
//    		method.setParameter("encode_hint",this.editEncodeHint);
    		method.addParameter("write",this.editWriteValue);
    		method.addParameter("cmd",this.editCmd);
    		method.addParameter("page",this.editPage);
    		method.addParameter("digest",this.editDigest);
			int status=client.executeMethod(method);
		    if (status != HttpStatus.SC_OK) {
		          this.println("Method failed: " + method.getStatusLine());
		          method.getResponseBodyAsString();
		    }
		    else {
		          br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		          String readLine;
		          while(((readLine = br.readLine()) != null)) {
		            this.println(readLine);
		          }
		   }
		    method.releaseConnection();
		}
		catch(Exception e){
//			this.messageTextArea.append(e.toString()+"\n");
			System.out.println(""+e);
			e.printStackTrace();
		}
		
	}
	
	boolean isInStringConst(String x, int p){
		
		int px=0;
		int py=0;
		boolean isIn=false;
		while(px<x.length()){
			if(px>p) return false;
			char cx=x.charAt(px);
			char cy=0;
			py=px+1;
			if(cx=='"'){
				isIn=true;
				while(py<x.length()){
					cy=x.charAt(py);
					if(cy=='"'){
						if(px<p && p<py)
							return true;
						else{
							isIn=false;
							px=py;
							break;
						}
					}
					if(cy=='\\'){
						py=py+1;
					}
					py=py+1;
				}
				if(isIn)
					return true;
			}
			if(cx=='\\'){
				px=px+1;
			}
			px=px+1;
		}
		return false;
	}
	public void readFromPukiwikiPageAndSetData(String url){
		this.println("editUrl="+url+"\n");
		this.urlField.setText(url);
		String x=connectToURL(url);
//		String x=this.messageTextArea.getText();
		
		// extract charSet from <?xml= ...?> which contains charSet;
		String firstXmltag=getBetween(x,"<?xml","?>");
		if(firstXmltag==null) return;
		this.pageCharSet=this.getTagProperty(firstXmltag,"encoding");
		if(this.pageCharSet==null)
			this.pageCharSet="UTF-8";
		this.println("pageCharSet="+this.pageCharSet);
		x=getBetween(x,"<body>","</body>");
		
		// exclude until <applet
//		int i=x.indexOf("<applet");
//		if(i<0) return;
//		x=x.substring(i);
		String headerPart=getBetween(x,"<div id=\"header\">","</div>");
		String pageNamePart=getBetween(headerPart,"<span class=\"small\">","</span>");
		StringTokenizer st=new StringTokenizer(pageNamePart,"?");
		this.baseUrl=st.nextToken();
//		System.out.println("baseUrl="+baseUrl);
		this.pageName=st.nextToken();
//		System.out.println("pageName="+pageName);
		// extract <pre>...</pre> where the figure is.
		String inw=getBetween(x,"<pre>","</pre>");
		/*
		String fig="";
		try{
			fig=new String(figw.getBytes(this.pageCharSet),"UTF-8");
		}
		catch(Exception e){
			System.out.println("decoding error..."+e.toString());
			fig=figw;
		}
		*/
		String input=inw;
		if(input==null) return;
        input=input.replaceAll("&quot;", "\"");
        input=input.replaceAll("&lt;", "<");
        input=input.replaceAll("&gt;", ">");
        application.setInput(input+"\n");
		
	}

	/*
	 *  get the first string which is in from l to r in the x
	 */
	String getBetween(String x, String l, String r){
//		System.out.println("x="+x);
		this.println("l="+l);
		this.println("r="+r);
		if(x==null) return null;
		int i=0;
		while(i<=0){
			i=x.indexOf(l,i);
    		if(i<0) return null;
    		if(i==0) break;
	    	if(isInStringConst(x,i)){
		    	i=i+l.length();
		    }
		}
		
		i=i+l.length();
		int j=i;
		while(j<=i){
		    j=x.indexOf(r,j);
		    if(j<0) return null;
		    if(isInStringConst(x,j)){
		    	j=j+r.length();
		    }
		}
		String rtn="";
		try{
			rtn=x.substring(i,j);
		}
		catch(Exception e){
			return null;
		}
//		System.out.println("rtn="+rtn);
		return rtn;
	}
    public void loadPage(String url){
    	this.readFromPukiwikiPageAndSetData(url) ;   	
    }

	@Override
	public void whenCancelButtonClicked(AuthDialog x) {
		// TODO Auto-generated method stub
		this.loginButtonPressed=false;
		this.authInputFlag=true;
		x.setVisible(false);
	}

	@Override
	public void whenLoginButtonClicked(AuthDialog x) {
		// TODO Auto-generated method stub
		this.loginButtonPressed=true;
		this.authInputFlag=true;
		x.setVisible(false);
	}
	public void println(String x){
		if(this.showMessagesCheckBox==null) return;
		if(this.showMessagesCheckBox.isSelected()){
			/*
			if(this.messageTextArea==null) return;
			this.messageTextArea.append(x+"\n");
			this.messageTextArea.setCaretPosition((this.messageTextArea.getText()).length());
			*/
			System.out.println(x);
		}
	}
}
