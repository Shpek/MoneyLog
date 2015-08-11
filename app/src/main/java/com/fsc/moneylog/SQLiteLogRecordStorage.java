package com.fsc.moneylog;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Sergey on 2015.08.11
 */
public class SQLiteLogRecordStorage
		extends SQLiteOpenHelper
		implements LogRecordStorage
{

	private static final String DDL_SCRIPT_NAME = "CreateDatabase.sql";
	private static final String DB_NAME = "MoneyLog.db";

	public SQLiteLogRecordStorage(Context context) {
		super(context, DB_NAME, null, 1);
	}

	public void storeRecord(LogRecord record) {
		SQLiteDatabase db = null;
		Cursor c = null;

		try {
			db = getWritableDatabase();

			Object[] values = new Object[] {
					record.getCategory(),
					record.getAmount(),
					record.getComment(),
					record.getTimestamp().getTime() };

			db.execSQL(
					"INSERT INTO log_records(category_id, amount, comment, date_added) " +
					"VALUES ((SELECT id FROM categories WHERE name = ?), ?, ?, ?)", values);
		} finally {
			if (c != null) c.close();
			if (db != null) db.close();
		}
	}

	public Collection<LogRecord> getAllRecords() {
		SQLiteDatabase db = null;
		Cursor c = null;

		try {
			db = getReadableDatabase();

			c = db.rawQuery(
			  "SELECT lr.id, c.name, amount, comment, date_added " +
			  "FROM log_records lr LEFT JOIN categories c ON lr.category_id = c.id", null);

			ArrayList<LogRecord> records = new ArrayList<>();

			while (c.move(1)) {
				LogRecord record = new LogRecord();
				record.setId(c.getLong(0));
				record.setCategory(c.getString(1));
				record.setAmount(c.getLong(2));
				record.setComment(c.getString(3));
				record.setTimestamp(new Date(c.getLong(4)));
				records.add(record);
			}

			return records;
		} finally {
			if (c != null) c.close();
			if (db != null) db.close();
		}
	}

	public void clearAllRecords() {
		SQLiteDatabase db = null;

		try {
			db = getWritableDatabase();
			db.execSQL("DELETE FROM log_records");
		} finally {
			if (db != null) db.close();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		InputStream stream = SQLiteLogRecordStorage.class.getResourceAsStream(DDL_SCRIPT_NAME);
		InputStreamReader reader = new InputStreamReader(stream);
		StringBuilder builder = new StringBuilder();
		char[] buf = new char[1024];

		try {
			while (true) {
				int charsRead = reader.read(buf);

				if (charsRead == -1)
					break;

				builder.append(buf, 0, charsRead);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try { stream.close(); } catch (IOException e) { /* ignored */ }
		}

		String[] queries = builder.toString().split(";");

		for(String query : queries) {
			query = query.trim();

			if (query.length() > 0)
				db.execSQL(query);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Do nothing for now
	}
}
