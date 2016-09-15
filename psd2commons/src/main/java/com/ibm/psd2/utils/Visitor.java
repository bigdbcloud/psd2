package com.ibm.psd2.utils;

public interface Visitor
{
	public <T, U> T visit(U u);
}
