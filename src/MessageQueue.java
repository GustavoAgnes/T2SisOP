import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class MessageQueue {
    public ArrayList<String> queue = new ArrayList<>();
    private Semaphore messageWait;

    public MessageQueue() {
        messageWait = new Semaphore(0);
    }

    public void removeFirstElement() {
        queue.remove(0);
    }

    public synchronized void addMessage(String Message) throws InterruptedException {
        //messageWait.acquire();
        queue.add(Message);
        //messageWait.release();
    }

    public synchronized void removeMessage() throws InterruptedException {
        //messageWait.acquire();
        queue.remove(queue.get(0));
        //messageWait.release();
    }

    public String getIp() throws SocketException, UnknownHostException {
        final DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        String ip = socket.getLocalAddress().getHostAddress();
        return ip;
    }


}
