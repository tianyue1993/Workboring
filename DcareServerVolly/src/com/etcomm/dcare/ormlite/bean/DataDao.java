package com.etcomm.dcare.ormlite.bean;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.etcomm.dcare.ormlite.db.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import android.content.Context;
import android.util.Log;

public class DataDao {
	private static final String tag = "DcareDBDataDao";
	private Dao<Data, Integer> dataDaoOpe;
	private DatabaseHelper helper;

	@SuppressWarnings("unchecked")
	public DataDao(Context context) {
		try {
			helper = DatabaseHelper.getHelper(context);
			dataDaoOpe = helper.getDao(Data.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void dropAllData(){
//		dataDaoOpe.d
//		dataDaoOpe.
		Log.i(tag, "Start dropAllData");
		try {
			dataDaoOpe.executeRaw("delete from tb_data;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e(tag, "SQLException  delete from tb_data;");
			e.printStackTrace();
		}
		Log.i(tag, " dropAllData  Finished");
	}
	public void add(Data data) {
		try {
			Log.i(tag, "dataDao create data: "+data.toString());
			dataDaoOpe.create(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public Data getDataById(int id) {
//		Data data = null;
//		try {
//			data = dataDaoOpe.queryForId(id);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return data;
//	}
	/**
	 * 通过UserId获取所有的文章
	 * @param userId
	 * @return
	 */
	public List<Data> listByUserId(int userId)
	{
		try
		{
			return dataDaoOpe.queryBuilder().where().eq("user_id", userId)
					.query();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Data> getDataFromTimeMillis(long todaystarttime, String user_id) {
		// TODO Auto-generated method stub
		try
		{
			QueryBuilder qb = dataDaoOpe.queryBuilder();
			qb.where().gt("time", todaystarttime);
			qb.where().eq("user_id", user_id);
			//.where().("user_id", todaystarttime)
			return qb.orderBy("time", true).query();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Data> getDataFromTimeMillisToTimeMillis(long start,long end,String user_id){
		try
		{
			Log.i(tag, "getDataFromTimeMillisToTimeMillis start: "+start+" end: "+end+"  user_id: "+user_id);
			QueryBuilder qb = dataDaoOpe.queryBuilder();
			qb.where().between("time", start, end).and().eq("user_id", user_id);
//	    	qb.where().eq("user_id", user_id);
			//.where().("user_id", todaystarttime)
//            return dataDaoOpe.queryBuilder().orderBy("time", true).where().eq("user_id", user_id).and().where().between("time", start, end)//.gt("time", start).le("time", end)//.where().("user_id", todaystarttime)  
//                    .query();  
			return qb.orderBy("time", true).query();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
