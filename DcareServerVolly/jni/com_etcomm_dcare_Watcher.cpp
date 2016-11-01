#include "process.h"
#include "android/sensor.h"
#include <android/looper.h>
#include <sys/types.h>
#include <sys/time.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define LOOPER_ID  1
/**
 * 全局变量，代表应用程序进程.
 */
ProcessBase *g_process = NULL;

/**
 * 应用进程的UID.
 */
const char* g_userId = NULL;
const char* nativefilepath = NULL;
/**
 * 是否开启手机计步，（可以不开启）
 */
bool isSoftPedometer = true;
/**
 *
 */
//void createnewfile();
long getCurrentSecTime();
timeval getTime();
long long getCurrentMilSecTime();
static int get_sensor_events(int fd, int events, void* data);
long getCurrentTime();
void initparams();
int pedometer(float x, float y, float z, long time);
void calculateActiveTime();
void calculateActiveCalory();
void calculateActiveMile();
void calculateActiveMileperHour();
long calculateActivitepaceChanged();
ASensorEventQueue* queue;
ASensorManager* sensorManager;
ASensorRef accelerometerSensor;
ALooper* looper;
//void* sensor_data = NULL;
void* sensor_data = malloc(1024);
int accCounter = 0;
int64_t lastAccTime = 0;
int gsensordatacount = 0;
int fd = -1;
/**
 * 全局的JNIEnv，子进程有时会用到它.
 */
JNIEnv* g_env = NULL;

extern "C" {
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_createWatcher(JNIEnv*,
																	   jobject, jstring);

JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_connectToMonitor(
		JNIEnv*, jobject);
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_startSensorManager(
		JNIEnv*, jobject);
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_restartSensorManager(
		JNIEnv*, jobject);
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_sendMsgToMonitor(JNIEnv*,
																	  jobject, jstring);
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_nativegetCurSteps(JNIEnv*,
																	   jobject);
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_nativeresetCalMileData(
		JNIEnv*, jobject);
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setnativefilepath(JNIEnv *,
																	   jobject, jstring);
JNIEXPORT jfloatArray JNICALL Java_com_etcomm_dcare_Watcher_timerIncreased(
		JNIEnv *, jobject);

JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setnativeSensitivity(
		JNIEnv *, jobject, jfloat);
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setEnablePedometer(
		JNIEnv *, jobject, jboolean);
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_nativeSetWeightHeightSteplenth(
		JNIEnv *, jobject, jfloat, jfloat, jfloat);
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM*, void*);
}
;
//已经弃用，守护进程
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_createWatcher(
		JNIEnv* env, jobject thiz, jstring user) {
	g_process = new Parent(env, thiz); //创建父进程

//	g_userId = (const char*) jstringTostr(env, user);
	g_userId = (const char*) env->GetStringUTFChars(user, 0); //用户ID
	g_process->catch_child_dead_signal(); //接收子线程死掉的信号

	if (!g_process->create_child()) {
		LOGI("<<create child error!>>");
		return JNI_FALSE;
	}

	return JNI_TRUE;
}
//已经弃用，守护进程
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_connectToMonitor(
		JNIEnv* env, jobject thiz) {
	if (g_process != NULL) {
		if (g_process->create_channel()) {
			return JNI_TRUE;
		}

		return JNI_FALSE;
	}
	return JNI_FALSE;
}
//重新开启3D传感器
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_restartSensorManager(
		JNIEnv* env, jobject thiz) {
	if (!isSoftPedometer) {
		return JNI_FALSE;
	}
	if (sensorManager != NULL) {
		if (queue != NULL && accelerometerSensor != NULL) {
			LOGI("queue!=NULL");
			ASensorEventQueue_disableSensor(queue, accelerometerSensor);
		}
		if (accelerometerSensor != NULL) {
			LOGI("accelerometerSensor!=NULL");
		} else {
			accelerometerSensor = ASensorManager_getDefaultSensor(sensorManager,
																  ASENSOR_TYPE_ACCELEROMETER);
		}
		if (looper == NULL) {
			looper = ALooper_forThread();
			LOGE("ALooper_forThread");
			if (looper == NULL) {
				looper = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);
			}
		}
		if (sensor_data == NULL) {
			sensor_data = malloc(1024);
		}
		if(queue==NULL){
			queue = ASensorManager_createEventQueue(sensorManager, looper,
													LOOPER_ID, get_sensor_events, sensor_data);
		}
		ASensorEventQueue_setEventRate(queue, accelerometerSensor, 1000); //ASensor_getMinDelay()
		ASensorEventQueue_enableSensor(queue, accelerometerSensor);
		LOGI("Sensor reenable sensor");
	} else {
		sensorManager = ASensorManager_getInstance();
		LOGE("sensorManager NULL");
		if (queue != NULL && accelerometerSensor != NULL) {
			ASensorEventQueue_disableSensor(queue, accelerometerSensor);
			LOGE("ASensorEventQueue_disableSensor");
		}
		if (looper != NULL) {
			looper = NULL;
		}
		looper = ALooper_forThread();
		LOGE("ALooper_forThread");
		if (looper == NULL) {
			looper = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);
		}
		accelerometerSensor = ASensorManager_getDefaultSensor(sensorManager,
															  ASENSOR_TYPE_ACCELEROMETER);
		LOGE("ASensorManager_getDefaultSensor");
		if (sensor_data == NULL) {
			sensor_data = malloc(1024);
		}
		if(queue==NULL){
			queue = ASensorManager_createEventQueue(sensorManager, looper,
													LOOPER_ID, get_sensor_events, sensor_data);
		}
		LOGE("ASensorManager_createEventQueue");
		ASensorEventQueue_setEventRate(queue, accelerometerSensor, 1000); //ASensor_getMinDelay()
		LOGE("ASensorEventQueue_enableSensor");
		ASensorEventQueue_enableSensor(queue, accelerometerSensor);
	}
	return true;
}
/**
 * 首次启动计步时调用，开启传感器
 */
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_startSensorManager(
		JNIEnv* env, jobject thiz) {
//	char* ch;
//	char* pwd;
//	pid_t pid;
//	pid = fork();
//	if(pid<0){
//
//	}else if(pid == 0){
//
//	pwd  = getcwd(ch,128);
//	LOGI("getcwd:  %s",pwd);
//	LOGI("getcwd    ch:  %s",ch);
//	fd = open(path,);
	LOGI("startsensormanager");
//	createnewfile();
	LOGI("createnewfileend");
//	time_t current_time = time(NULL)*1000;
//	LOGI("%lld",current_time);
	LOGI("c/c++ program: %lld", getCurrentMilSecTime());
	LOGE("c/c++ program: %lld", getCurrentTime());
	if (!isSoftPedometer) {
		return JNI_FALSE;
	}
	sensorManager = ASensorManager_getInstance();
	looper = ALooper_forThread();
	if (looper == NULL) {
		looper = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);
	}
	accelerometerSensor = ASensorManager_getDefaultSensor(sensorManager,
														  ASENSOR_TYPE_ACCELEROMETER);
	LOGI("accelerometerSensor: %s, vendor: %s",
		 ASensor_getName(accelerometerSensor),
		 ASensor_getVendor(accelerometerSensor));
	LOGI("ASensor_getName:  %s", ASensor_getName(accelerometerSensor));
	LOGI("ASensor_getVendor: %s", ASensor_getVendor(accelerometerSensor));
	LOGI("ASensor  Resolution : %f",
		 ASensor_getResolution(accelerometerSensor));
	if (sensor_data == NULL) {
		sensor_data = malloc(1024);
	}
	LOGI("ASensor_getMinDelay:  %d", ASensor_getMinDelay(accelerometerSensor));
//	    ASensorEventQueue* queue = ASensorManager_createEventQueue(sensorManager, looper, LOOPER_ID, NULL, NULL);
	if(queue==NULL){
		queue = ASensorManager_createEventQueue(sensorManager, looper, LOOPER_ID,
												get_sensor_events, sensor_data);

	}
	ASensorEventQueue_setEventRate(queue, accelerometerSensor, 1000);
	ASensorEventQueue_enableSensor(queue, accelerometerSensor);
//	clock();
//	int ident; //identifier
//	int events;
	return true;
//	}else{
//
//	}
//	    while (1) {
//	        while ((ident=ALooper_pollAll(-1, NULL, &events, NULL) >= 0)) {
//	            // If a sensor has data, process it now.
//	            if (ident == LOOPER_ID) {
//	                ASensorEvent event;
//	                while (ASensorEventQueue_getEvents(queue, &event, 1) > 0) {
//	                    LOGI("accelerometer X = %f y = %f z= %f ", event.acceleration.x, event.acceleration.y, event.acceleration.z);
//	                }
//	            }
//	        }
//	    }

//	pid_t pid;
//	LOGI("startSensorManager");
//	sensorManager = ASensorManager_getInstance();
//			if(sensorManager== NULL){
//				LOGI("sensorManager== NULL ");
//			}
//	//		void* sensor_data = malloc(1024);
//	//		ASensorManager_getSensorList(sensorManager, list);
//			//list->
//			const ASensor * accSensor = ASensorManager_getDefaultSensor(
//			sensorManager, ASENSOR_TYPE_ACCELEROMETER);
//			if(accSensor == NULL){
//				LOGI("accSensor == NULL");
//			}
//			LOGI("ASensor_getName:  %s", ASensor_getName(accSensor));
//			LOGI("ASensor_getVendor: %s", ASensor_getVendor(accSensor));
//			LOGI("ASensor  Resolution : %f", ASensor_getResolution(accSensor));
//			LOGI("ASensor_getMinDelay:  %d", ASensor_getMinDelay(accSensor));
////			pid = fork();
////	if (pid < 0) {
////		LOGI("fork error  ");
////		return false;
////	} else if (pid == 0) //子进程
////	{
////		ASensorList* list;
//		LOGI("fork pid : pid ==0  ");
//
////		ALooper_callbackFunc callback;
//		looper = ALooper_prepare(0);
//		LOGI("ALooper_prepare ");
////		ALooper* looper = ALooper_forThread();
//		if (looper == NULL){
//			looper = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);
//		}
//		LOGI("ASensorManager_createEventQueue  start ");
//		int identid = LOOPER_ID;
////		void* data = NULL;
//		accQueue = ASensorManager_createEventQueue(sensorManager, looper,
//		identid, NULL, NULL);
//		//	accQueue = ASensorManager_createEventQueue(sensorManager,
//		//			looper, identid, get_sensor_events, sensor_data);
//		LOGI("accQueue createEventQueue  finished!");
//		ASensorEventQueue_enableSensor(accQueue, accSensor);
//		int events;
//		int ident;
////		while (1) {
////			while ((ident = ALooper_pollAll(-1, NULL, &events, NULL) >= 0)) {
//				// If a sensor has data, process it now.
////				if (ident == LOOPER_ID) {
//					ASensorEvent event;
//				//	size_t size =0;
//					LOGI("ASensorEventQueue_getEvents");
//					while (ASensorEventQueue_getEvents(accQueue, &event, 0) > 0) {
//						LOGI("aaaaaaa accelerometer X = %f y = %f z=%f ",
//						event.acceleration.x, event.acceleration.y,
//						event.acceleration.z);
//					}
////				}
////			}
////		}
//
////	} else if (pid > 0)  //父进程
////	{
////		LOGI("<<In parent process,pid=%d>>", getpid());
////	}
////
////	return pid;
//
}
//char *ch =new char[50];
//char ch[45];
/**
 * 传感器数据回调接口
 */
static int get_sensor_events(int eventfd, int events, void* data) {
	ASensorEvent event;
	//ASensorEventQueue* sensorEventQueue;
	while (ASensorEventQueue_getEvents(queue, &event, 1) > 0) {
		if (event.type == ASENSOR_TYPE_ACCELEROMETER) {
//			LOGI("accl(x,y,z,t): %f %f %f %lld", event.acceleration.x,
//					event.acceleration.y, event.acceleration.z,
//					event.timestamp);
//			ch[49] =0;
			long curtime = getCurrentSecTime(); //getCurrentMilSecTime
//			sprintf(ch, " %f %f %f %ld \0\n", event.acceleration.x,
//					event.acceleration.y, event.acceleration.z, curtime);
			pedometer(event.acceleration.x, event.acceleration.y,
					  event.acceleration.z, curtime);
//              sprintf(ch,"%1.6f %f %f %l \n", event.acceleration.x, event.acceleration.y, event.acceleration.z, curtime);
//			if (fd > 0) {
//				int count = write(fd, ch, sizeof(ch));
////				LOGI("write %d   count:  %d", fd, count);
//				gsensordatacount++;
//				if (gsensordatacount > 30000) {
//					createnewfile();
//					gsensordatacount = 0;
//				}
//			} else {
//				createnewfile();
//				gsensordatacount = 0;
//			}
		}
	}
	//should return 1 to continue receiving callbacks, or 0 to unregister
	return 1;
}

//void createnewfile() {
//	char * filename = new char[256];
//	memset(filename, 0, sizeof(char) * 256);
//	LOGI("start create new File");
//	if (fd > 0) {
//		close(fd);
//		fd = -1;
//	}
//	if (nativefilepath != NULL) {
////		filename = nativefilepath;
//		LOGI("nativefilepath:  %s", nativefilepath);
////		memcpy(filename,nativefilepath,sizeof(nativefilepath));
//		strcpy(filename, nativefilepath);
//	} else {
//		char* ch = "/storage/emulated/0/dcare/";
//		filename = ch;
//	}
//	LOGI("memcpy  filename:  %s", filename);
//	long curtime = getCurrentSecTime(); //getCurrentSecTime();//getCurrentMilSecTime
//	//??11:10  第二次命名时会出现data再次接上命名的问题，将filename 最后一位置成\0
//	LOGI("start strcat %ld", curtime);
//	strcat(filename, "data_");///????////这加\0 　就会出错12-18 11:07:46.560: A/libc(13886): Fatal signal 11 (SIGSEGV), code 1, fault addr 0x35da in tid 13886 (e.dameonservice)
//	LOGI("filename:  %s", filename);
//	char *strBuf = new char[6];
//	sprintf(strBuf, "%ld", curtime);
//	LOGI("strBuf:  %s", strBuf);
//	strcat(filename, strBuf);	/////????
//	LOGI("filename:  %s", filename);
//	int size = strlen(strBuf);
//	strcat(filename, ".txt");
////	free(strBuf);
////	strcat(filename,ltoa(curtime));
////	snprintf(filename,"%ld",ltoa(curtime));
//	LOGI("filename:  %s", filename);
//	fd = open(filename, O_CREAT | O_RDWR);
//	free(strBuf);
//	free(filename);
//	LOGI("finish create new File");
//}

/*
 * 计步算法相关的数据
 */
float mLimit = 4.44;
float mLastValues[] = { 0.0, 0.0 };
float mScale[] = { 0.0, 0.0 };
float mYOffset = 0;
int steps = 0;
float mLastDirections[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
float mLastExtremes[6][6] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
							  0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
							  0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
float mLastDiff[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
int mLastMatch = -1;
/**
 * 计步函数初始化比例系数
 */
void initparams() {
	int h = 480;
	mYOffset = h * 0.5f;
	mScale[0] = -(h * 0.5f * (1.0f / (ASENSOR_STANDARD_GRAVITY * 2)));
	mScale[1] = -(h * 0.5f * (1.0f / (ASENSOR_MAGNETIC_FIELD_EARTH_MAX))); //-4.0
}
/**
 * 设置灵敏度
 */
void setSensitivity(float sensitivity) {
	mLimit = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
}

/**
 *计步算法
 */
int pedometer(float x, float y, float z, long time) {
	float v = 0;
	v = mYOffset + mScale[1] * (x + y + z) / 3;
	int k = 0;
	float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
//	LOGW("v: %f mLastValues[k] %f  direction: %f", v, mLastValues[k],
//			direction);
	if (direction == -mLastDirections[k]) {
		int extType = (direction > 0 ? 0 : 1);
		mLastExtremes[extType][k] = mLastValues[k];
		float diff = abs(
				mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);
//		LOGW("cur:diff:  %f  mLimit: %f mLastDiff[0]: %f", diff, mLimit,
//				mLastDiff[k]);
		if (diff > mLimit) {
			bool isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
			bool isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
			bool isNotContra = (mLastMatch != 1 - extType);
			LOGW(
					"cur: isAlmostAsLargeAsPrevious:%d isPreviousLargeEnough: %d isNotContra %d",
					isAlmostAsLargeAsPrevious, isPreviousLargeEnough,
					isNotContra);
			if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough
				&& isNotContra) {
				calculateActivitepaceChanged(); //步数计算 转换到其内部   其他时间  热量 里程也放到其中
//				steps++;
//				calculateActiveTime();
//				calculateActiveCalory();
//				calculateActiveMile();
				LOGW("step++  %d", steps);
//				for (StepListener stepListener : mStepListeners) {
//					stepListener.onStep();
//				}
				mLastMatch = extType;
			} else {
				mLastMatch = -1;
			}
		}
		mLastDiff[k] = diff;
	}
	mLastDirections[k] = direction;
	mLastValues[k] = v;
	return 0;
}
/**
 * 设置灵敏度
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setnativeSensitivity(
		JNIEnv *env, jobject object, jfloat sensitivity) {
	setSensitivity(sensitivity);
	LOGI("mLimit:  %f ", mLimit);
	return 1;
}
/**
 * 设置开户或者关闭计步
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setEnablePedometer(
		JNIEnv *env, jobject object, jboolean isPedometer) {
	isSoftPedometer = isPedometer;
	LOGE("ASensorEventQueue_disableSensor  %d  %d",isPedometer,isSoftPedometer);
	if (!isSoftPedometer) {
		if (queue != NULL && accelerometerSensor != NULL) {
			LOGE("ASensorEventQueue_disableSensor");
			ASensorEventQueue_disableSensor(queue, accelerometerSensor);
//			queue = NULL;
			accelerometerSensor = NULL;
//			free(sensor_data);
//			sensor_data = NULL;
			looper = NULL;
			sensorManager = NULL;

		}
	} else {
	}
	return 1;
}
/**
 * 调试时使用，设置调试数据保存位置
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setnativefilepath(
		JNIEnv *env, jobject object, jstring jstr) {
	nativefilepath = (char*) env->GetStringUTFChars(jstr, 0);
//	strcat();
	LOGI("nativefilepath:  %s ", nativefilepath);
	initparams();
	return 1;
}
/**
 * Java 获取NDK当前计步数据
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_nativegetCurSteps(
		JNIEnv *env, jobject object) {
	LOGI("Cur steps : %d", steps);
//	jint j = new jint();
//	return 1;
	return steps;
}
/**
 * 调试时使用
 */
long getCurrentSecTime() {
	struct timeval tv;
	gettimeofday(&tv, NULL);
	return tv.tv_sec;
}
/**
 * 调试时使用
 */
long long getCurrentMilSecTime() {
	long long time = 0;
	int64_t ltime = 0;
	struct timeval tv;
	gettimeofday(&tv, NULL);
	time = (tv.tv_sec * 1000) + (tv.tv_usec / 1000);
	ltime = (tv.tv_sec * 1000) + (tv.tv_usec / 1000);
	LOGE("time: %lld   timeval_tv_sec %ld  timeval_tv_usec %ld", time,
		 tv.tv_sec, tv.tv_usec);
	LOGE("time: %lld   timeval_tv_sec %ld  timeval_tv_usec %ld", ltime,
		 tv.tv_sec, tv.tv_usec);
	return time;
}
/**
 * 调试时使用
 */
timeval getTime() {
	struct timeval tv;
	gettimeofday(&tv, NULL);
	return tv;
}
/**
 * 调试时使用
 */
long getCurrentTime() {
	struct timeval tv;
	gettimeofday(&tv, NULL);
	LOGI("timeval: tv_sec:  %ld   tv.tv_usec:   %ld  ", tv.tv_sec, tv.tv_usec);
//   localtime();///1450149633301////1450149616     826409
	return tv.tv_sec * 1000 + tv.tv_usec / 1000;
}
/**
 * 调试时使用
 */
JNIEXPORT jlongArray JNICALL Java_com_test_ndkhelloword_MainActivity_getTimesFromJni(
		JNIEnv *env, jobject thiz) {
	jlongArray time = env->NewLongArray(2);
	jlong temp[] = { 0, 0 };
	struct timeval begin;
	gettimeofday(&begin, NULL);
	temp[0] = begin.tv_sec;
	temp[1] = begin.tv_usec;
	env->SetLongArrayRegion(time, 0, 2, temp);
	return time;
}
/**
 *
 */
double mSeconds = 0;
timeval mLastActiveTime;
/**
 * 计算运动时间
 */
void calculateActiveTime() {
	timeval curTime = getTime();
	if (mLastActiveTime.tv_sec != 0) {
		if (curTime.tv_sec != mLastActiveTime.tv_sec) {
			mSeconds++;
			LOGE("mSeconds : %f", mSeconds);
		}
	}
	mLastActiveTime = curTime;
}
/**
 *
 */
double mCalories = 0;
float mStepLength = 66;
float mBodyWeight = 60;
float mBodyHeight = 172;
bool isRunning = false;
static double METRIC_RUNNING_FACTOR = 1.02784823;
static double METRIC_WALKING_FACTOR = 0.708;
/**
 *  * 监听运动卡路里
 */
void calculateActiveCalory() {
	mCalories += (mBodyWeight
				  * (isRunning ? METRIC_RUNNING_FACTOR : METRIC_WALKING_FACTOR))
				 // Distance:
				 * mStepLength // centimeters
				 / 100000.0; // centimeters/kilometer
	LOGE("mCalories : %f", mCalories);
}
/**
 *
 *
 */
float mDistance = 0;
/**
 * 监听运动距离
 */
void calculateActiveMile() {
	mDistance += (float) ( // kilometers
			mStepLength // centimeters
			/ 100000.0); // centimeters/kilometer
	LOGE("mDistance : %f", mDistance);
}
/**
 * miles/hour
 */
float mSpeed = 0;
/**
 * 监听运动速度
 */
void calculateActiveMileperHour(int value) {
	mSpeed = // kilometers / hour   // centimeters / minute
			value * mStepLength / 100000 * 60; // centimeters/kilometer
	LOGE("mSpeed : %f", mSpeed);
}
/**
 * steps / minute
 */
//int mCounter = 0;
long mPace = 0;
timeval mLastStepTime;
long mLastStepDeltas[] = { -1, -1, -1, -1 };
int mLastStepDeltasIndex = 0;
/**
 * 监听发生步数
 */
long calculateActivitepaceChanged() {
	timeval thisStepTime = getTime();
//	mCounter++;

	// Calculate pace based on last x steps
	if (mLastStepTime.tv_sec != 0) {
		long delta = (thisStepTime.tv_sec - mLastStepTime.tv_sec) * 1000
					 + (thisStepTime.tv_usec - mLastStepTime.tv_usec) / 1000;
		LOGE("delta: %ld", delta);
		mLastStepDeltas[mLastStepDeltasIndex] = delta;
		mLastStepDeltasIndex = (mLastStepDeltasIndex + 1) % 4;
		if (delta < 150) { //两步时间间隔小于300ms，0.3s，记录有误，步数减1
//			steps--;
		}
//		else if (delta > 6000) { //两步时间间隔大于6000ms，6s，记录有误，步数减1
////			steps--;
//		}
		else {
			steps++;
			calculateActiveTime();
			calculateActiveCalory();
			calculateActiveMile();
		}
		long sum = 0;
		bool isMeaningfull = true;
		for (int i = 0; i < 4; i++) {
			if (mLastStepDeltas[i] < 0) {
				isMeaningfull = false;
				break;
			}
			sum += mLastStepDeltas[i];
		}
		if (isMeaningfull && sum > 0) {
			long avg = sum / 4; //mLastStepDeltas.length;
			mPace = 60 * 1000 / avg;

		} else {
			mPace = -1;
		}
	}
	mLastStepTime = thisStepTime;
	calculateActiveMileperHour(mPace);

}
/**
 * 设置身高体重步长数据
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_nativeSetWeightHeightSteplenth(
		JNIEnv * env, jobject thiz, jfloat weight, jfloat height,
		jfloat steplenght) {
	mBodyWeight = weight;
	mBodyHeight = height;
	mStepLength = steplenght;
	LOGI("mBodyWeight %f , mBodyHeight %f , mStepLenght  %f", mBodyWeight,
		 mBodyHeight, mStepLength);
	return 1;
}
/**
 * 重置相关数据
 */
JNIEXPORT jfloatArray JNICALL Java_com_etcomm_dcare_Watcher_timerIncreased(
		JNIEnv * env, jobject thiz) {
	jfloatArray ja = env->NewFloatArray(3);
	float msecond = (float) mSeconds;
	float mcalory = (float) mCalories;
	env->SetFloatArrayRegion(ja, 0, 1, &mDistance);
	env->SetFloatArrayRegion(ja, 1, 1, &msecond);
	env->SetFloatArrayRegion(ja, 2, 1, &mcalory);
//	ja[0] = mDistance;
//	ja[1] = mSeconds;
//	ja[2] = mCalories;
	return ja;
}
/**
 * 重置相关数据
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_nativeresetCalMileData(
		JNIEnv *env, jobject object) {
	steps = 0;
	mDistance = 0;
	mSeconds = 0;
	mCalories = 0;
	return 1;
}
