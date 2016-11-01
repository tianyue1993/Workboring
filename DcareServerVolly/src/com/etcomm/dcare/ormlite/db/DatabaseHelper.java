package com.etcomm.dcare.ormlite.db;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.etcomm.dcare.ormlite.bean.Data;
import com.etcomm.dcare.ormlite.bean.Device5MinData;
import com.etcomm.dcare.ormlite.bean.DeviceDailyData;
import com.etcomm.dcare.ormlite.bean.User;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	private static final String TABLE_NAME = "dcaredata.db";
	/**
	 * @Time 2016.3.1 重新设计数据结构，data数据表中保存  单位时间（1分钟或者若干分钟，若没有数据，则不保存）内的步数以及相关信息 
	 * 用户表中，严格按照User_id进行保存区分，data数据表中保存user_id作为多账号唯一区分，
	 * DBVersion = 5 犯错，导致计步数据一直未计入
	 * DBVersion = 7 重新改表名为tb_stepdata
	 * DBVersion = 8 新增加两个表  DeviceDailyData  Device5MinData
	 * DBVersion = 9 数据库没有创建表？ 重新创建  
	 */
	private static final int DBVersion = 9;
	//	private static final int DBVersion = 8;
//	private static final int DBVersion = 7;
//	private static final int DBVersion = 6;
//	private static final int DBVersion = 5;
	private static final String tag = "DatabaseHelper";

	private Map<String, Dao> daos = new HashMap<String, Dao>();
	private Context mContext;


	private DatabaseHelper(Context context)
	{
		super(context, TABLE_NAME, null, DBVersion);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database,
						 ConnectionSource connectionSource)
	{
		Log.i(tag,"onCreate");
		try
		{
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, Data.class);
			TableUtils.createTable(connectionSource, Device5MinData.class);
			TableUtils.createTable(connectionSource, DeviceDailyData.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onUpgrade(SQLiteDatabase database,
						  ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		if(newVersion==9){
			try {
				Log.e(tag, "newVersion 9 onUpgrade");
				TableUtils.dropTable(connectionSource, User.class, true);
				TableUtils.dropTable(connectionSource, Data.class, true);
				TableUtils.dropTable(connectionSource, Device5MinData.class, true);
				TableUtils.dropTable(connectionSource, DeviceDailyData.class, true);
				boolean isdeleted = SQLiteDatabase.deleteDatabase(mContext.getDatabasePath(TABLE_NAME));
				Log.e(tag, "newVersion 9 onUpgrade  deleteDatabase: "+isdeleted);
//				TableUtils.dropTable(connectionSource, Data.class, true);
				onCreate(database, connectionSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if (oldVersion == 4 && (newVersion == 5||newVersion == 6)) {
			try {
				TableUtils.dropTable(connectionSource, User.class, true);
				TableUtils.dropTable(connectionSource, Data.class, true);
				onCreate(database, connectionSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(newVersion == 9){
			onCreate(database, connectionSource);
		}else if(newVersion == 7){
			try {
				Log.e(tag, "newVersion 7 onUpgrade");
				TableUtils.dropTable(connectionSource, User.class, true);
				TableUtils.dropTable(connectionSource, Data.class, true);
				TableUtils.dropTable(connectionSource, Device5MinData.class, true);
				TableUtils.dropTable(connectionSource, DeviceDailyData.class, true);
				boolean isdeleted = SQLiteDatabase.deleteDatabase(mContext.getDatabasePath(TABLE_NAME));
				Log.e(tag, "newVersion 7 onUpgrade  deleteDatabase: "+isdeleted);
//				TableUtils.dropTable(connectionSource, Data.class, true);
				onCreate(database, connectionSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(newVersion == 8){
			Log.i(tag,"newVersion   8");
			if(oldVersion == 7){
				Log.i(tag,"oldVersion   7");
				try
				{
					TableUtils.createTable(connectionSource, Device5MinData.class);
					TableUtils.createTable(connectionSource, DeviceDailyData.class);
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}else if(oldVersion<7){
				Log.i(tag,"oldVersion <  7");
				try {
					TableUtils.dropTable(connectionSource, User.class, true);
					TableUtils.dropTable(connectionSource, Data.class, true);
					onCreate(database, connectionSource);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				onCreate(database, connectionSource);
				try
				{
					TableUtils.createTable(connectionSource, Device5MinData.class);
					TableUtils.createTable(connectionSource, DeviceDailyData.class);
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private static DatabaseHelper instance;

	/**
	 * 鍗曚緥鑾峰彇璇elper
	 *
	 * @param context
	 * @return
	 */
	public static synchronized DatabaseHelper getHelper(Context context)
	{
		context = context.getApplicationContext();
		if (instance == null)
		{
			synchronized (DatabaseHelper.class)
			{
				if (instance == null)
					instance = new DatabaseHelper(context);
			}
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	public synchronized Dao getDao(Class clazz) throws SQLException
	{
		Dao dao = null;
		String className = clazz.getSimpleName();

		if (daos.containsKey(className))
		{
			dao = daos.get(className);
		}
		if (dao == null)
		{
			dao = super.getDao(clazz);
			daos.put(className, dao);
		}
		return dao;
	}

	/**
	 * 閲婃斁璧勬簮
	 */
	@Override
	public void close()
	{
		super.close();

		for (String key : daos.keySet())
		{
			Dao dao = daos.get(key);
			dao = null;
		}
	}

}
