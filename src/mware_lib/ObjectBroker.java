package mware_lib;

import java.io.IOException;

import mware_lib.nameServer.RemoteMethodConection;
import mware_lib.networking.Stub;

import branch_access.Manager;
import branch_access.ManagerServant;
import branch_access.ManagerStub;
import cash_access.Account;
import cash_access.AccountServant;
import cash_access.AccountStub;

public class ObjectBroker {
	private final String serviceHost;
	private final int listenPort;
	private final NameService socket;
	
	private ObjectBroker(String serviceHost, int listenPort) {
		this.serviceHost = serviceHost;
		this.listenPort = listenPort;
		this.socket = new NameServiceStub(serviceHost, listenPort);
	}

	// Das hier zurückgelieferte Objekt soll der zentrale Einstiegspunkt
	// der Middleware aus Anwendersicht sein.
	// Parameter: Host und Port, bei dem die Dienste (Namensdienst)
	// kontaktiert werden sollen.
	public static ObjectBroker getBroker(String serviceHost, int listenPort) {
		return new ObjectBroker(serviceHost, listenPort);
	}

	// Liefert den Namensdienst (Stellvetreterobjekt).
	public NameService getNameService() {
		return socket;
	}
	//Create new ServantServer for this Object:
	public static Servant getServant(Object servant){
		try {
			if(servant instanceof Account)
				return new AccountServant(servant);
			else if(servant instanceof Manager)
				return new ManagerServant(servant);
		} catch (IOException e) {
			System.err.println("Couldnt build up a servant for "+servant);
		}
		return null;
	}
	public static ServantTypeAssoziation getAssoziationType(Servant servant){
		ServantTypeAssoziation tmpType = ServantTypeAssoziation.none;
		if(servant instanceof ManagerServant)
			tmpType = ServantTypeAssoziation.Manger;
		if(servant instanceof AccountServant)
			tmpType = ServantTypeAssoziation.Account;
		return tmpType;
	}

	//create Stubs
	public static Object getStub(RemoteMethodConection remoteMethod){
		Object stub = null;
		if(remoteMethod.getServantType().equals(ServantTypeAssoziation.Manger)) {
			try {
				stub = new ManagerStub(remoteMethod.getHostname(), remoteMethod.getPort());
			} catch (Exception e) {
				System.err.println("Couldnt build a Stub for a Manager!");
			}
		}
		else if(remoteMethod.getServantType().equals(ServantTypeAssoziation.Account)) {
			try {
				stub = new AccountStub(remoteMethod.getHostname(), remoteMethod.getPort());
			} catch (Exception e) {
				System.err.println("Couldnt build a Stub for a Account!");
			}
		}
		
		if(stub==null){
			System.err.println("Couldnt build a Stub!");
		}
		return stub;
	}
}
