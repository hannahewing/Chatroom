//Hannah Ewing
package NetworksP5;

import java.io.BufferedReader;
//import java.util.Collection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

class Flag
{	private boolean theflag;
	Flag()
	{
		theflag=true;
	}
	
	public void setFlag()
	{
		theflag=false;
	}
	
	public boolean getFlag()
	{
		return theflag;
	}
}

	class server extends Thread
	{
		private Socket clientSocket;
		private String clientSentence;
    	private Flag flag;
    	private BufferedReader inFromClient;
    	private DataOutputStream outToServer;
    	private LinkedList <server> chatRoom;
    	
    	public DataOutputStream getDataOutputStream () throws IOException
    	{
    		return outToServer;
		}
    	

    	
    	server (Socket newsocket, Flag newflag, LinkedList <server> chatroom)
    	{
    		clientSocket = newsocket;
    		flag= newflag;
    		chatRoom = chatroom;
    		chatRoom.add(this);
    		try {
				inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			
    		outToServer = new DataOutputStream(clientSocket.getOutputStream());
    		} catch (IOException e)
    		{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        public void run()
        {    	
        		try{
        		 

		        		do {
				        		clientSentence = inFromClient.readLine();
				        		
				        		if (clientSentence.contains("EXITEXIT"))
				        		{
				        			outToServer.writeBytes(clientSentence+'\n');	
				        		}
				        		
				        		else
				        		{
					        		//////////////////loop through your linked list and writeBytes to each client data output stream	
					        		for (int i = 0;i< chatRoom.size(); i++)//////for loop?
					        		{
					        			//chatRoom.writeBytes(clientSentence);//////use the .get(forloopcontrolvar) method of the LinkedList class to get the server class object at that location then call the getDataOutputStream () method then call the writeBytes(~~) method
					        			
					        			
					        			
					        			chatRoom.get(i).getDataOutputStream().writeBytes(clientSentence+'\n');
					        		}//for
				        		}//else
		        		} while (!clientSentence.contains("EXITEXIT") && flag.getFlag());
        		
        		}catch (Exception e)
            	{
            		System.out.println("Something went terribly wrong:" + e);
            	}
        		
        		chatRoom.remove(this);
        		
        }
    
	}
	
	class serverKbd extends Thread
	{
    	private ServerSocket welcomeSocket;
    	//private Socket serverSocket;
    	private Flag flag;
    	serverKbd (ServerSocket newsocket, /*Socket newSocket,*/ Flag newflag)
    	{
    		welcomeSocket = newsocket;
    		//serverSocket = newSocket;
    		flag= newflag;
    	}
        public void run()
        {
        	try{
        		
        		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        		
        	            	String finished;	
        		do
        		{     		
        			finished = inFromUser.readLine();
        			
        			
        		
        		if (finished.contains("EXITEXIT"))
        		{
        			flag.setFlag();
        			welcomeSocket.close();
        		}
        		} while (!finished.contains("EXITEXIT") && flag.getFlag());
        	}catch (Exception e)
        	{
        		System.out.println("Something went terribly wrong." + e);
        	}
         	}
     }

        
        public class runServer
        {
        	public static void main(String argv[]) throws Exception
        	{
        		ServerSocket welcomeSocket = new ServerSocket(18082);
        		Flag stopFlag = new Flag();
        		LinkedList <server> chatRoom = new LinkedList <server>();
        		serverKbd k = new serverKbd(welcomeSocket, /*ServerSocket,*/ stopFlag);
        		k.start();
        		///////start a loop while flag true
        		while (stopFlag.getFlag() == true)
        		{
        			try{
			        		Socket clientSocket = welcomeSocket.accept();
		
			        		
			        		server p = new server(clientSocket, stopFlag, chatRoom);
			        		p.start();
        				}catch(IOException e)
        			{
        				System.out.println("Server has closed the chatroom.");
        			}
	        		/////////////////insert that p into the linked list
        		}
        		
        		welcomeSocket.close();	
        	}/////end main method
        }
   
