LOCAL_PATH := $(call my-dir)

#include $(CLEAR_VARS)
#LOCAL_MODULE := getuiext
#LOCAL_SRC_FILES := libgetuiext.so
#include $(PREBUILT_STATIC_LIBRARY)
 
LOCAL_SHARED_LIBRARIES := getuiext
LOCAL_STATIC_LIBRARIES := getuiext 