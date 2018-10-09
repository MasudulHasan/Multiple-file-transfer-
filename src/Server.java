
import java.io.*; 
import java.util.*; 
import java.net.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException; 

// Server class 
public class Server 
{ 

	// Vector to store active clients 
	static Vector<ClientHandler> ar = new Vector<>();
	public static Vector<Boolean> response = new Vector<>();

	// counter for clients 
	static int i = 0; 

	public static void main(String[] args) throws IOException 
	{ 
		// server is listening on port 1234 
		ServerSocket ss = new ServerSocket(1234); 

		Socket s; 

		// running infinite loop for getting 
		// client request 
		while (true) 
		{ 
			// Accept the incoming request 
			s = ss.accept(); 

			System.out.println("New client request received : " + s); 

			// obtain input and output streams 
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

			System.out.println("Creating a new handler for this client..."); 

			// Create a new handler object for handling this request. 
			ClientHandler mtch = new ClientHandler(s,"client " + i,i, dis, dos); 

			// Create a new Thread with this object. 
			Thread t = new Thread(mtch); 

			System.out.println("Adding this client to active client list"); 

			// add this client to active clients list 
			ar.add(mtch); 
			response.add(false);

			// start the thread. 
			t.start(); 

			// increment i for new client. 
			// i is used for naming only, and can be replaced 
			// by any naming scheme 
			i++; 

		} 
	} 
} 

// ClientHandler class 
class ClientHandler implements Runnable 
{ 
	Scanner scn = new Scanner(System.in); 
	private String name; 
	final DataInputStream dis; 
	final DataOutputStream dos; 
	Socket s; 
	boolean isloggedin; 
	public static final int BUFFER_SIZE = 1024*50;
	private byte[] buffer;
	int id;

	// constructor 
	public ClientHandler(Socket s, String name,int id, 
			DataInputStream dis, DataOutputStream dos) { 
		this.dis = dis; 
		this.dos = dos; 
		this.name = name; 
		this.s = s; 
		this.id = id;
		this.isloggedin=true; 
		buffer = new byte[BUFFER_SIZE];
	} 

	@Override
	public void run() { 
		String received;
		BufferedOutputStream out = null;
		String outputFile = "out_"+this.name+".mp4";
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			out = new BufferedOutputStream(new FileOutputStream(outputFile));
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		while (true) 
//		{ 
			try
			{ 
				// receive the string 
//				received = dis.readUTF(); 
				
//				System.out.println(received); 
//				
//				
//				
//				if(received.equals("logout")){ 
//					this.isloggedin=false; 
//					this.s.close(); 
//					break; 
//				} 
				
				String output_file_name = dis.readUTF();
				System.out.println(output_file_name);
				
				outputFile = "out_"+id+"_"+output_file_name;
				out = new BufferedOutputStream(new FileOutputStream(outputFile));
				
				long length = -1;
				length = dis.readLong();
				if(length!=-1)System.out.println("length "+length);
				
//				String checkSum = dis.readUTF();
//				System.out.println(checkSum);
				
				int len=0;
				long totalLength = 0;
				
//				int len = 0;
				while ((len = in.read(buffer,0,BUFFER_SIZE)) > 0) {
					out.write(buffer, 0, len);
					totalLength+=len;
					if(totalLength==length)break;
//					System.out.println("length "+len);
					if(len<=0)break;
				}
				
				File file = new File(outputFile);
				
				System.out.println("total length1 "+totalLength+ " dif "+(totalLength-length));
				
				System.out.println("output length "+file.length());
//				in.read(buffer);
//				String message = new String(buffer);
//				System.out.println("message "+message);
				
//				while (1) {
//					dis.readInt();
//					out.write(buffer, 0, len);
//					totalLength+=len;
//					System.out.println("length "+len);
//				}
				
				if(totalLength-length==0) {
					System.out.println("Checksum matched! ");
		        	dos.writeUTF(output_file_name);
				}
				else {
					if(file.delete()) 
		            { 
		                System.out.println("File deleted successfully"); 
		            } 
		            else
		            { 
		                System.out.println("Failed to delete the file"); 
		            } 
		        	dos.writeUTF("failed # "+output_file_name); 
					
				}
				
				
//				System.out.println("total length1 "+totalLength);
//				MessageDigest md;
//				try {
//					md = MessageDigest.getInstance("SHA-256");
//					String hex = checksum(outputFile, md);
//			        System.out.println("output hex "+hex);
//			        if(checkSum.equals(hex)) {
//			        	System.out.println("Checksum matched! ");
//			        	dos.writeUTF(output_file_name);
////			        	Server.response.set(id, true);
//			        	
//			        }
//			        else {
//			        	if(file.delete()) 
//			            { 
//			                System.out.println("File deleted successfully"); 
//			            } 
//			            else
//			            { 
//			                System.out.println("Failed to delete the file"); 
//			            } 
//			        	dos.writeUTF("failed # "+output_file_name); 
//			        }
//			        
//				} catch (NoSuchAlgorithmException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
				
				
//				int len = dis.read(buffer);
//				if(out!=null)out.write(buffer, 0, len);
				System.out.println("total length "+totalLength);
			
				
			} catch (IOException e) { 
				
				e.printStackTrace(); 
			} 
			
//		} 
//		try
//		{ 
//			// closing resources 
//			this.dis.close(); 
//			this.dos.close(); 
//			
//		}catch(IOException e){ 
//			e.printStackTrace(); 
//		} 
		

		
//		while (true) 
//		{ 
//			try
//			{
//				BufferedInputStream in = 
//						new BufferedInputStream(s.getInputStream());
//
//				BufferedOutputStream out = 
//						new BufferedOutputStream(new FileOutputStream("out.pdf"));
//
//				int len = 0;
//				while ((len = in.read(buffer)) > 0) {
//					out.write(buffer, 0, len);
//					System.out.println(new String(buffer));
//				}
//
////				in.close();
//				out.flush();
//				out.close();
////				client.close();
////				s.close();
//				System.out.println("\nfinish!");
//				String  recipient = "client 0";
//				for (ClientHandler mc : Server.ar)  
//                { 
//                    // if the recipient is found, write on its 
//                    // output stream 
//                    if (mc.name.equals(recipient) && mc.isloggedin==true)  
//                    { 
//                        mc.dos.writeUTF(this.name+" : "+"done "+recipient); 
//                        break; 
//                    } 
//                } 
//			} catch (IOException e) { 
//
//				e.printStackTrace(); 
//			} 
 
//			try
//			{ 
//				// closing resources 
////				this.dis.close(); 
////				this.dos.close(); 
//	
//			}catch(IOException e){ 
//				e.printStackTrace(); 
//			} 
//		}
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
