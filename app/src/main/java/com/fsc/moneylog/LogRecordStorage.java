package com.fsc.moneylog;

import java.util.Collection;

/**
 * Created by Sergey on 2015.08.12
 *
 * Methods for storing and retrieving log records
 */
public interface LogRecordStorage {

	void storeRecord(LogRecord record);
	Collection<LogRecord> getAllRecords();

}
