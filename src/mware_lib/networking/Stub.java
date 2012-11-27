package mware_lib.networking;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class Stub implements MethodCaller {
	private final Connection connection;

	public Stub(final String host, final int port) throws UnknownHostException, IOException {
		final Socket clientSocket = new Socket(host, port);
		this.connection = new Connection(clientSocket);
	}

	
	public final Serializable callMethod(final Serializable parametersObject) {
		final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		
		return callMethod(methodName, parametersObject);
	}
	
	
	public synchronized final Serializable callMethod(final String methodName, final Serializable parametersObject) {
		final MethodCallMessage message = new MethodCallMessage(methodName, parametersObject);
		
		try {
			connection.send(message);

			final Serializable reply = connection.receive();
			
			if (reply instanceof RuntimeException) {
				throw (RuntimeException)reply;
			} else {
				return reply;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
	}
}
