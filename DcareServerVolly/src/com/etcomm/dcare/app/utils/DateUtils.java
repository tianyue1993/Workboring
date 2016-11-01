/**  
 * @Package com.etcomm.dcare.app.utils 
 * @author etc  
 * @date 6 Apr, 2016 4:21:53 PM 
 * @version v 1.9  
 */
package com.etcomm.dcare.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



/**
 * @ClassName: DateUtils
 * @Description: 时间工具类
 * @author etc
 * @date 6 Apr, 2016 4:21:53 PM
 */

public class DateUtils {
	/**
	 * @Title: isToday 
	 * @Description: 临时处理
	 * @param @param date
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public static boolean isToday(String date){
		String now =getSystemTime();
        
		return now.equalsIgnoreCase(date);
	}
	public static String getSystemTime(){ 

		Date date=new Date(); 

		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 

		return df.format(date); 

		} 
}
