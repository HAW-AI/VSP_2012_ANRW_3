package branch_access;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;

import mware_lib.networking.Stub;

public class ManagerStub extends Manager {
	private final Stub client;
	
	
	public ManagerStub(final String host, final int port) throws UnknownHostException, IOException {
		this.client = new Stub(host, port);
	}
	
	
	public String createAccount(String owner) {
		Serializable result = client.callMethod(owner);
		
		return (String)result;
	}

	
	public double getBalance(String accountID) {
		Serializable result = client.callMethod(accountID);
		
		return (Double)result;
	}

}
