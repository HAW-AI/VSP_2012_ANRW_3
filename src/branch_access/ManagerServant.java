package branch_access;

import java.io.IOException;
import java.io.Serializable;

import mware_lib.Servant;

public class ManagerServant extends Servant {

	public ManagerServant(Object servant) throws IOException {
		super(servant);
	}

	@Override
	public Serializable callMethod(String methodName, Serializable parametersObject) {
		final Manager manager = (Manager) servant;

		if (methodName.equals("createAccount")) {
			final String result = manager.createAccount((String) parametersObject);
			return result;
		} else if (methodName.equals("getBalance")) {
			final double result = manager.getBalance((String) parametersObject);
			return new Double(result);
		} else {
			final String message = "Unknown method " + methodName + " for class " + servant.getClass().getName() + " was called by the remote client!";
			System.err.println(message);
			// throw an exception which is interpreted by the client
			return new RuntimeException(message);
		}
	}

}
