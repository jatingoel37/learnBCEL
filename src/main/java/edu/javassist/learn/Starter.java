package edu.javassist.learn;

import javassist.ClassPool;
import javassist.Loader;

public class Starter {

	public static void main(String[] args) throws Throwable {
		runApplicationAfterChangingSuperClass(args);
	}

	/**
	 * The class loader will be used to load other classes as well.
	 * 
	 * @param args
	 * @throws Throwable
	 */
	private static void runApplicationAfterChangingSuperClass(String[] args) throws Throwable {
		MyTranslator myTrans = new MyTranslator();
		ClassPool cp = ClassPool.getDefault();
		Loader cl = new Loader(cp);
		cl.addTranslator(cp, myTrans);
		cl.run("edu.javassist.learn.Application", args);
	}
}
