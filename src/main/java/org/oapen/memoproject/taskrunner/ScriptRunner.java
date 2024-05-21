package org.oapen.memoproject.taskrunner;

import java.io.ByteArrayOutputStream;

import io.vavr.control.Either;

public interface ScriptRunner {
	
	/**
	 * Run a script and either return its output on success, or a message stating the reason of failure 
	 * 
	 * @param scriptBundle
	 * @return Either a String containing an error message or a ByteArrayOutputStream containing the scripts output
	 */
	Either<String, ByteArrayOutputStream> run(ScriptBundler scriptBundle);

}
