package mware_lib.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Connection {
	private final InputStream in;
	private final ObjectOutputStream out;
	private ObjectInputStream in2;
	
	
	public Connection(Socket socket) throws IOException {
		in = socket.getInputStream();
		out = new ObjectOutputStream( socket.getOutputStream() );
	}
	
	
	public final Serializable receive() throws IOException, ClassNotFoundException {
		if (in2 == null) {
			// this is blocking if no data is available
			in2 = new ObjectInputStream(in);
		}
		
		final Serializable object = (Serializable) in2.readObject();
		
		return object;
	}
	
	
	public void send(final Serializable object) throws IOException {
		out.writeObject(object);
		out.flush();
	}
	
	
	public void close() throws IOException {
		in.close();
		out.close();
	}
}
