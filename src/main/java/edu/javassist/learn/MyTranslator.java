package edu.javassist.learn;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.Translator;

public class MyTranslator implements Translator {

	public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

	}

	public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
		if (classname.contains("Application")) {
			pool.get(classname).setSuperclass(pool.getCtClass("edu.javassist.learn.SuperApp"));
		}

	}

}
