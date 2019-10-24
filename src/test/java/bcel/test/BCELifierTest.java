package bcel.test;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.bcel.Repository;
import org.apache.bcel.util.BCELifier;

public class BCELifierTest {

	public static void main(String[] args) throws Exception {

		OutputStream out = new FileOutputStream("bcel");
		BCELifier bceLifier = new BCELifier(Repository.lookupClass("bcel.test.Multiplier"), out);
		bceLifier.start();
		out.close();
	}
}
