//Hannah Ewing
package NetworksP5;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/*class Flag
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
//class have a boolean data member
//use a constructor to set it to true
// have a get method that returns it
// have a set method that changes it to false   
*/

	class person extends Thread
	{
    	private Socket clientSocket;
    	private Flag flag;
    	person (Socket newsocket, Flag newflag)
    	{
    		clientSocket = newsocket;
    		flag= newflag;
    	}
        public void run()
        {
        	try{
        		BufferedReader chatRoomName = new BufferedReader(new InputStreamReader(System.in));
        		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        		
        	            	String sentence;	
        	            	String chatName = chatRoomName.readLine();
        		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());	
        		do
        		{     		
        			sentence = inFromUser.readLine();
        			
        			
        		outToServer.writeBytes(chatName + sentence + '\n');
        		if (sentence.equals("EXITEXIT"))
        		{
        			flag.setFlag();
        		}
        		} while (!sentence.equals("EXITEXIT") && flag.getFlag());
        	}catch (Exception e)
        	{
        		System.out.println("Something went terribly wrong." + e);
        	}
         	}
     }
        class client extends Thread
    	{
        	private Socket clientSocket;
        	private Flag flag;
        	client (Socket newsocket, Flag newflag)
        	{
        		clientSocket = newsocket;
        		flag= newflag;
        	}
            public void run()
            {

            	String modifiedSentence;
            	try {
            	BufferedReader inFromServer =
        				new BufferedReader(new
        						InputStreamReader(clientSocket.getInputStream()));
        		do
        		{
       		
        		modifiedSentence = inFromServer.readLine();
        		
        		System.out.println("FROM SERVER: " + modifiedSentence);
        		if (modifiedSentence.contains("EXITEXIT"))
        		{
        			flag.setFlag();
        		}
        		} while (!modifiedSentence.contains("EXITEXIT") && flag.getFlag());
            	}catch (Exception e)
            	{
            		System.out.println("Something went terribly wrong:" + e);
            	}
            }
    	}
          public class runClient
        {
        	public static void main(String argv[]) throws Exception
        	{
        		Socket clientSocket = new Socket("localhost", 18082);	
        		System.out.println(clientSocket);
        		Flag stopFlag = new Flag();
        		client c = new client(clientSocket, stopFlag);
        		c.start();
        		person p = new person(clientSocket, stopFlag);
        		p.start();
        		c.join();
        		p.join();
        		clientSocket.close();
        	}
        }
    

