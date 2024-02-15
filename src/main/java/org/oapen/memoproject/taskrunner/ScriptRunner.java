package org.oapen.memoproject.taskrunner;

import io.vavr.control.Either;

public interface ScriptRunner {
	
	Either<String,String> run(ScriptBundler scriptBundle);

}
