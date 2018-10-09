import java.io.*; 
import java.net.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.omg.PortableInterceptor.DISCARDING; 
  
public class Client  
{ 
    final static int ServerPort = 1234; 
    public static final int BUFFER_SIZE = 1024 * 50;
    private static byte[] buffer;
    public static int[]response_ara = new int[120];
    public static Map<String,Integer>mp =new HashMap();
    public static int receivedsofar = 0;
    public static File[] listOfFiles;
    
    static long startTime;
	static long endTime;
	public static int cuncurrency_vaue =1;
    
    public static void main(String args[]) throws UnknownHostException, IOException  
    { 
        Scanner scn = new Scanner(System.in);
        buffer = new byte[BUFFER_SIZE];
        for(int i=0;i<100;i++)response_ara[i]=0;
        
        
        if(args.length>0) {
        	if(args[0]!=null) {
        		cuncurrency_vaue =  Integer.valueOf(args[0]);
        	};
        }
        
        // getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost"); 
        
        File folder = new File("files/");
        listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
//            System.out.println("File " + listOfFiles[i].getName());
            mp.put(listOfFiles[i].getName(),i);
            
          }
        }
        
//        System.out.println(mp.size());
//        
//        for (Map.Entry<String, Integer> entry : mp.entrySet()) {
//            System.out.println(entry.getKey()+" : "+entry.getValue());
//        }
          
        // establish the connection 
//        Socket s = new Socket(ip, ServerPort); 
          
        // obtaining input and out streams 
//        DataInputStream dis = new DataInputStream(s.getInputStream()); 
//        DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
  
//        // sendMessage thread 
//        Thread sendMessage = new Thread(new Runnable()  
//        { 
//            @Override
//            public void run() { 
//                while (true) { 
//  
//                    // read the message to deliver. 
//                    String msg = scn.nextLine(); 
//                      
//                    try { 
//                        // write on the output stream 
////                        dos.writeUTF(msg);
//                        
//                        BufferedInputStream in = 
//                                new BufferedInputStream(
//                                     new FileInputStream("hubbard2018.pdf"));
//
//                           BufferedOutputStream out = 
//                                new BufferedOutputStream(s.getOutputStream());
//                                
//                           
//                           int len = 0;
//                           while ((len = in.read(buffer)) > 0) {
//                                out.write(buffer, 0, len);
//                                System.out.print("#");
//                           }
//                           in.close();
//                           out.flush();
//                           out.close();
//                           s.close();
//                           System.out.println("\nDone!");
//                    
//                    } catch (IOException e) { 
//                        e.printStackTrace(); 
//                    } 
//                } 
//            } 
//        }); 
//          
//        // readMessage thread 
//        Thread readMessage = new Thread(new Runnable()  
//        { 
//            @Override
//            public void run() { 
//  
//                while (true) { 
//                    try { 
//                        // read the message sent to this client 
//                        String msg = dis.readUTF(); 
//                        System.out.println("Got the message "+msg); 
//                    } catch (IOException e) { 
//  
//                        e.printStackTrace(); 
//                    } 
//                } 
//            } 
//        }); 
  
//        SendMessage message = new SendMessage(s, "hubbard2018.pdf",dos);
        
        
        
//        for(int i=0;i<8;i++) {
//        	Socket s1 = new Socket(ip, ServerPort); 
//            DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
//            DataInputStream dis = new DataInputStream(s1.getInputStream());
//        	SendMessage message = new SendMessage(s1, listOfFiles[i].getName(),dos1);
//        	Thread t = new Thread(message); 
//        	System.out.println("Adding this client to active client list"); 
//        	t.start();
//        	
//        	ReceiveMessage readMessage = new ReceiveMessage(dis);
//        	Thread r = new Thread(readMessage);
//        	r.start(); 
//        }
        startTime = System.currentTimeMillis();
        sendFiles(ip,cuncurrency_vaue);
//        readMessage.start(); 
  
    }
    
    public static synchronized void chageStatus(int index, int value){
    	response_ara[index]=value;
    }
    
    public static synchronized void sendFiles(InetAddress ip, int concurrency) throws IOException {
    	int selectedfiles =0;
    	boolean hasSelected = false;
//    	for(int i=0;i<listOfFiles.length;i++) {
//    		System.out.println(response_ara[i]+" ");
//    	}
//    	
//    	System.out.println();
    	
    	for(int i=0;i<listOfFiles.length;i++){
    		int index = Client.mp.get(listOfFiles[i].getName());
    		if(response_ara[index]==0) {
	        	Socket s1 = null;
				try {
					s1 = new Socket(ip, ServerPort);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	            DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
	            DataInputStream dis = new DataInputStream(s1.getInputStream());
	        	SendMessage message = new SendMessage(s1, "files/"+listOfFiles[i].getName(),dos1);
	        	Thread t = new Thread(message); 
//	        	System.out.println("Adding this client to active client list"); 
	        	t.start();
	        	
	        	ReceiveMessage readMessage = new ReceiveMessage(dis,ip,dos1);
	        	Thread r = new Thread(readMessage);
	        	r.start();
	        	
	        	selectedfiles++;
	        	hasSelected = true;
	        	response_ara[index]=1;
//	        	chageStatus(index,1);
    		}
    		
    		if(selectedfiles==concurrency)break;
    	}
    	
    	if(!hasSelected) {
    		System.out.println("Program Ended!!");
    	}
    }
    
    
    public static synchronized void sendOneFiles(InetAddress ip) throws IOException {
    	int selectedfiles =0;
    	boolean hasSelected = false;
    	boolean allCompleted = true;
    	
    	for(int i=0;i<listOfFiles.length;i++) {
    		System.out.print(response_ara[i]+" ");
    	}
    	
    	System.out.println();
    	
    	for(int i=0;i<listOfFiles.length;i++){
    		int index = Client.mp.get(listOfFiles[i].getName());
    		if(response_ara[index]!=2) {
    			allCompleted = false;
	    		if(response_ara[index]==0) {
		        	Socket s1 = null;
					try {
						s1 = new Socket(ip, ServerPort);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
		            DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
		            DataInputStream dis = new DataInputStream(s1.getInputStream());
		        	SendMessage message = new SendMessage(s1, "files/"+listOfFiles[i].getName(),dos1);
		        	Thread t = new Thread(message); 
	//	        	System.out.println("Adding this client to active client list"); 
		        	t.start();
		        	
		        	ReceiveMessage readMessage = new ReceiveMessage(dis,ip,dos1);
		        	Thread r = new Thread(readMessage);
		        	r.start();
		        	
		        	selectedfiles++;
		        	hasSelected = true;
		        	response_ara[index]=1;
	//	        	chageStatus(index,1);
		        	break;
	    		}
    		}
    		
    		
//    		if(selectedfiles==8)break;
    	}
    	
    	if(allCompleted) {
    		System.out.println("Program Ended!!");
    		endTime   = System.currentTimeMillis();
    		long totalTime = endTime - startTime;
    		String outputFile = "output_for_combined.txt";
    		File file = new File(outputFile);
    		FileWriter fr = new FileWriter(file, true);
    		BufferedWriter br = new BufferedWriter(fr);
    		br.append("\n"+cuncurrency_vaue+ " "+totalTime);

    		br.close();
    		fr.close();
    		
//    		FileWriter fw=new FileWriter(outputFile);    
//            fw.append("\n"+cuncurrency_vaue+ " "+totalTime);    
//            fw.close();    

    		System.out.println(totalTime);
    		System.exit(0);
    	
    	}
    }
    
}




class ReceiveMessage implements Runnable{
	DataInputStream dis ;
	InetAddress ip;
	DataOutputStream dos;
	public ReceiveMessage(DataInputStream dis,InetAddress ip,DataOutputStream dos) {
		this.dis = dis;
		this.ip = ip;
		this.dos=dos;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 while (true) { 
             try { 
                 // read the message sent to this client 
                 String msg = dis.readUTF(); 
                 System.out.println("Got the message "+msg); 
                 if(!msg.contains("failed")) {
                	 int index = Client.mp.get(msg);
                	 Client.chageStatus(index,2);
                 }
                 else {
                	 String[] parts =msg.split("#");
                	 int index = Client.mp.get(parts[1].trim());
                	 Client.chageStatus(index,0);
                 }
                 
//                 Client.receivedsofar++;
//                 System.out.println("number "+Client.receivedsofar);
//                 if(Client.receivedsofar==8) {
//                	System.out.println("GOT 8 files!!");
//                	Client.receivedsofar=0;
//                	Client.sendFiles(ip);
//              
//                 }
//                 dis.close();
//                 dos.close();
                 Client.sendOneFiles(ip);
                 
                 
               
             } catch (IOException e) { 

                 e.printStackTrace(); 
             } 
         } 
		
	}
}
    
class SendMessage implements Runnable{
	String file_name;
	Socket s;
	public static final int BUFFER_SIZE = 1024 * 50;
    private byte[] buffer;
    DataOutputStream dos;
	
	public SendMessage(Socket sc,String file_name,DataOutputStream dos) {
		this.file_name = file_name;
		this.s=sc;
		buffer = new byte[BUFFER_SIZE];
		this.dos = dos;
	}
	
	@Override
    public void run() { 
		File file = new File(file_name);
//		System.out.println("file name "+file_name);
		try {
//			MessageDigest md;
//			try {
//				md = MessageDigest.getInstance("SHA-256");
//				String hex = checksum(file_name, md);
//		        System.out.println("hex "+hex);
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	        
			sendFile(file, dos);
//			dos.close();
			while(true) {
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//        while (true) { 
            
//            try { 
//                
//                BufferedInputStream in = 
//                        new BufferedInputStream(
//                             new FileInputStream(file_name));
//
//                   BufferedOutputStream out = 
//                        new BufferedOutputStream(s.getOutputStream());
//                        
//                   
//                   int len = 0;
//                   while ((len = in.read(buffer)) > 0) {
////                        out.write(buffer, 0, len);
////                        System.out.println("#");
//                   }
////                   in.close();
////                   out.flush();
////                   out.close();
////                   s.close();
////                   System.out.println("\nDone!");
//                   String ack = "finish";
////                   out.write(ack.getBytes());
//                   System.out.println("\nDone!");
            
//            } catch (IOException e) { 
//                e.printStackTrace(); 
//            } 
//        } 
    } 
	
	
	public void sendFile(File file, DataOutputStream dos) throws IOException {
//		int BUFFER_SIZE = 1024*5;
	    byte[] buffer = new byte[BUFFER_SIZE+1];
	    if(dos!=null&&file.exists()&&file.isFile())
	    {
	    	String[] parts = file_name.split("/");
	    	dos.writeUTF(parts[1]);
	    	
	    	FileInputStream input = new FileInputStream(file);
	        dos.writeLong(file.length());
	        
	        
//	        MessageDigest md;
//			try {
//				md = MessageDigest.getInstance("SHA-256");
//				String hex = checksum(file_name, md);
////		        System.out.println("hex "+hex);
//		        dos.writeUTF(hex);
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	        
	        
//	        System.out.println(file.getAbsolutePath());
	        int read = 0;
	        int totalLength = 0;
	        while ((read = input.read(buffer)) != -1) {
	        	dos.write(buffer,0,read);
	            totalLength +=read;
//	            System.out.println("length "+read);
	        }
//	        String message = "finsh";
//	        dos.write(message.getBytes());
	        input.close();
//	        System.out.println("File successfully sent! "+totalLength);
	    }
	}
	
	private static String checksum(String filepath, MessageDigest md) throws IOException {
		
		byte[] buffer = new byte[SendMessage.BUFFER_SIZE+1];

        // file hashing with DigestInputStream
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read(buffer) != -1) ; //empty loop to clear the data
            md = dis.getMessageDigest();
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();

    }
}
    
