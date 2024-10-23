
package pasaud.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class UDPServer{
    public static DatagramSocket serverSocket;
    
    public static ThreadPoolExecutor parserExecutor;
    
    private static Thread receiveThread;
    private static Handler receiveHandler;
    
    private static boolean needCryptograph;
    
    private static boolean acceptReceivedCoord;

    public static boolean isNeedCryptograph() {
		return needCryptograph;
	}
    
    public static void setNeedCryptograph(boolean value) {
    	needCryptograph = value;
    }
    
    public static boolean isNeedAcceptReceivedCoord() {
		return acceptReceivedCoord;
	}
    
    public static void setAcceptReceivedCoord(boolean value) {
    	acceptReceivedCoord = value;
    }

	private UDPServer(){
    }
    
    public static void init(int port, int threads, boolean needCryptograph_, boolean acceptReceivedCoord_) throws IOException{
    	serverSocket = new DatagramSocket(port);
        //Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        //cipher.
    	parserExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
    	parserExecutor.submit(() -> {
    	    Thread.sleep(1000);
    	    return null;
    	});
    	receiveHandler = new Handler();
    	receiveThread = new Thread(receiveHandler);
    	receiveThread.start();
    	
    	needCryptograph = needCryptograph_;
		acceptReceivedCoord = acceptReceivedCoord_;
    }
    
    /*
     *Force threadPool to add size, for no reason
     */
    public static void setParserThreadPoolSize(int poolSize) {
    	if(poolSize > 0) {
    		parserExecutor.setCorePoolSize(poolSize);
    	}
    }
    
    /*
     * In dev
     */
    public static void checkThreads() {
    	int poolSize = parserExecutor.getPoolSize();
    	int queueSize = parserExecutor.getQueue().size();
    	int activeCount = parserExecutor.getActiveCount();
    	if(queueSize - poolSize >= 10) {
    		parserExecutor.setCorePoolSize(poolSize + 2);
    	} else if (poolSize - activeCount >= 2) {
    		int newSize = poolSize - 1;
    		if(newSize <= 0) newSize = 1;
    		parserExecutor.setCorePoolSize(newSize);
    	}
    }
    
    public static void close() {
    	receiveHandler.canRun.set(false);
    	serverSocket.close();
    	while(receiveHandler.isRuning.get()) {
    		try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	parserExecutor.shutdownNow();
    	try {
			parserExecutor.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}

  class Handler implements Runnable {
      
      public AtomicBoolean canRun;
      public AtomicBoolean isRuning;

      Handler() {
    	  canRun = new AtomicBoolean();
    	  isRuning = new AtomicBoolean();
    	  canRun.set(true);
    	  isRuning.set(false);
      }
      
      @Override
      public void run() {
          try {
        	  isRuning.set(true);
              while (canRun.get() && !UDPServer.serverSocket.isClosed()) {

                  byte[] buffer = new byte[2048];
                  DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                  
                  try {
                      UDPServer.serverSocket.receive(packet);
                  } catch (SocketException se) {
                      if (canRun.get()) {
                          System.out.println("O socket foi fechado.");
                          break;
                      } else {
                          System.out.println("O servidor está encerrando.");
                          break;
                      }
                  }

                  //System.out.println("Received UDP packet.");
                  
                  UDPServer.parserExecutor.submit(() -> {
              	    ProtoParser parser = new ProtoParser(packet, UDPServer.isNeedCryptograph(), UDPServer.isNeedAcceptReceivedCoord());
              	    parser.run();
              	    return null;
              	  });
                  
                  int poolSize = UDPServer.parserExecutor.getPoolSize();
              	  int queueSize = UDPServer.parserExecutor.getQueue().size();
              	  int activeCount = UDPServer.parserExecutor.getActiveCount();
              	
                  //System.out.println("parserExecutor poolSize: " + poolSize);
                  //System.out.println("parserExecutor queueSize: " + queueSize);
                  //System.out.println("parserExecutor activeCount: " + activeCount);
                  //new Thread(new ClientBufferParser(packet)).start();

              }
              isRuning.set(false);
          } catch (IOException e) {
              Thread t = Thread.currentThread();
              t.getUncaughtExceptionHandler().uncaughtException(t, e);
              isRuning.set(true);
          }
      }
 }
