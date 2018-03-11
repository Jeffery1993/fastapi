package com.jeffery.fastapi.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String getCurrentClassName() {
		return this.getClass().getName();
	}

	protected String getCurrentMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

}
