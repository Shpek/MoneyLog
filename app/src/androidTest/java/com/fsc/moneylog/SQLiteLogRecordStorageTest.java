package com.fsc.moneylog;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by Sergey on 2015.08.12
 */
public class SQLiteLogRecordStorageTest extends ApplicationTestCase<Application> {

	private SQLiteLogRecordStorage storage;

	public SQLiteLogRecordStorageTest() {
		super(Application.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		storage = new SQLiteLogRecordStorage(getContext());
	}

	public void tearDown() throws Exception {
		if (storage != null)
			storage.close();
	}

	public void testStoreRecord() throws Exception {
		storage.clearAllRecords();

		LogRecord record = new LogRecord();
		record.setCategory("Rent");
		record.setAmount(100);
		record.setTimestamp(new Date());
		record.setComment("Tralala");

		storage.storeRecord(record);
		boolean recordRead = false;

		for (LogRecord record1 : storage.getAllRecords()) {
			if (recordRead)
				Assert.fail("More than one record returned, only one expected.");

			recordRead = true;
			Assert.assertTrue(record.getCategory().equals(record1.getCategory()));
			Assert.assertTrue(record.getAmount() == record1.getAmount());
			Assert.assertTrue(record.getTimestamp().equals(record1.getTimestamp()));
			Assert.assertTrue(record.getComment().equals(record1.getComment()));
			Assert.assertTrue(record1.getId() != 0);
		}

		record.setCategory("BoomBoom");
		storage.storeRecord(record);
	}

	public void testGetAllRecords() throws Exception {

	}
}