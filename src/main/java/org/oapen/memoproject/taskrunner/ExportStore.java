package org.oapen.memoproject.taskrunner;

import org.oapen.memoproject.taskrunner.entities.Export;

public interface ExportStore {
	
	boolean save(Export export);
}
