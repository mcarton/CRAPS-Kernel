#include <jni.h>

#include "org_mmek_craps_crapsusb_CommThread.h"
#include <iostream>
#include <cstring>

#include "dpcdecl.h"
#include "dpcdefs.h"    /* holds error codes and data types for dpcutil    */
#include "dpcutil.h"    /* holds declaration of DPCUTIL API calls        */


/* Global variables */
ERC erc;


/*
 * Class:     org_mmek_craps_crapsusb_CommThread
 * Method:    init
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_mmek_craps_crapsusb_CommThread_init(JNIEnv *, jclass) {
    if(!DpcInit(&erc)) {
        return -1;
    }
    else {
        return 0;
    }
}

/*
 * Class:     org_mmek_craps_crapsusb_CommThread
 * Method:    getDeviceAliases
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_org_mmek_craps_crapsusb_CommThread_getDeviceAliases(JNIEnv* env, jclass) {
    jclass arrayList = env->FindClass("java/util/ArrayList");
    if(arrayList == NULL) return NULL;

    jmethodID arrayListInit = env->GetMethodID(arrayList, "<init>", "()V");
    if(arrayListInit == NULL) return NULL;

    jobject result = env->NewObject(arrayList, arrayListInit);
    if(result == NULL) return NULL;

    jmethodID arrayListAdd = env->GetMethodID(arrayList, "add", "(Ljava/lang/Object;)Z");
    if(arrayListAdd == NULL) return NULL;

    int nb = DvmgGetDevCount(&erc);
    char alias[1001];

    for(int i = 0; i < nb; i++) {
        DvmgGetDevName(i, alias, &erc);

        jstring jalias = env->NewStringUTF(alias);
        if(jalias == NULL) return NULL;

        jboolean jbool = env->CallBooleanMethod(result, arrayListAdd, jalias);
        if(!jbool) return NULL;
    }

    return result;
}

/*
 * Class:     org_mmek_craps_crapsusb_CommThread
 * Method:    openData
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_org_mmek_craps_crapsusb_CommThread_openData(JNIEnv* env, jclass, jstring jalias) {
    const char *c_alias = env->GetStringUTFChars(jalias, 0);
    char alias[1001];
    strncpy(alias, c_alias, 1000);
    HANDLE hif;

    if(!DpcOpenData(&hif, alias, &erc, NULL)) {
        return -1;
    }
    else {
        return hif;
    }
}

/*
 * Class:     org_mmek_craps_crapsusb_CommThread
 * Method:    closeData
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_mmek_craps_crapsusb_CommThread_closeData(JNIEnv *, jclass, jlong hif) {
    DpcCloseData((HANDLE) hif, &erc);
    return 0;
}

/*
 * Class:     org_mmek_craps_crapsusb_CommThread
 * Method:    writeByte
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_org_mmek_craps_crapsusb_CommThread_writeByte(JNIEnv *, jclass, jlong hif, jint num, jint data) {
    if (!DpcPutReg((HANDLE) hif, (BYTE) num, (BYTE) data, &erc, NULL)) {
        return -1;
    }
    else {
        return 0;
    }
}

/*
 * Class:     org_mmek_craps_crapsusb_CommThread
 * Method:    readByte
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_org_mmek_craps_crapsusb_CommThread_readByte(JNIEnv *, jclass, jlong hif, jint num) {
    BYTE idData;
    if (!DpcGetReg((HANDLE) hif, (BYTE) num, &idData, &erc, NULL)) {
        return -1;
    }
    else {
        return ((jint) idData);
    }
}
