
#include <stdio.h>
#include <jni.h>

#include "org_jcb_shdl_CommThread.h" 
#include <iostream>

#include "dpcdecl.h"
#include "dpcdefs.h"	/* holds error codes and data types for dpcutil	*/
#include "dpcutil.h"	/* holds declaration of DPCUTIL API calls		*/

using namespace std;

/* ------------------------------------------------------------ */
/*					Local Type Definitions						*/
/* ------------------------------------------------------------ */


/* ------------------------------------------------------------ */
/*					Global Variables							*/
/* ------------------------------------------------------------ */

const int	cbBlockSize		= 1000;
const int	cchShortString	= 16;
const float	idVer			= 1.0;

/* ------------------------------------------------------------ */
/*					Local Variables								*/
/* ------------------------------------------------------------ */

char			szDefDvc[cchShortString+1];
ERC				erc;
HANDLE			hif;


/* ------------------------------------------------------------ */
/*					Forward Declarations						*/
/* ------------------------------------------------------------ */

static BOOL		FInit();


/***	FInit
**
**	Synopsis
**		void FInit()
**
**	Input:
**
**	Output:
**		none
**
**	Errors:
**		none
**
**	Description:
**		Initialize application local variables.
**
*/

static BOOL FInit() {

	ERC erc;

	/* DPCUTIL API CALL : DpcInit
	*/
	if (!DpcInit(&erc)) {
		return fFalse;
	} else {
		return fTrue;
	}
}


JNIEXPORT jint JNICALL Java_org_jcb_shdl_CommThread_init(JNIEnv *, jclass) {
	if (!FInit()) {
		cout << "init failed" << endl; 
		return -1;
	} else {
		cout << "init succeeded" << endl; 
		return 0;
	}
}

JNIEXPORT jint JNICALL Java_org_jcb_shdl_CommThread_openData(JNIEnv *, jclass) {
	int nbDev, i, valRet;

	valRet = -1;
	nbDev = DvmgGetDevCount(&erc);
	for (i = 0; i < nbDev; i++) {
		DvmgGetDevName(i, szDefDvc, &erc);
		printf("device found: %s\n", szDefDvc);
		if (!DpcOpenData(&hif, szDefDvc, &erc, NULL)) continue;
		valRet = 0;
		break;
	}
	if (valRet < 0) printf("DpcOpenData failed for all listed devices.\n");
	return valRet;
}

JNIEXPORT jint JNICALL Java_org_jcb_shdl_CommThread_closeData(JNIEnv *, jclass) {
	DpcCloseData(hif, &erc);
	return 0;
}

JNIEXPORT jint JNICALL Java_org_jcb_shdl_CommThread_writeByte(JNIEnv *, jclass, jint num, jint data) {
	if (!DpcPutReg(hif, (BYTE) num, (BYTE) data, &erc, NULL)) {
		printf("DpcPutReg failed, erc=%d\n", erc);
		return -1;
	} else {
		return 0;
	}
}

JNIEXPORT jint JNICALL Java_org_jcb_shdl_CommThread_readByte(JNIEnv *, jclass, jint num) {
	BYTE	idData;
	if (!DpcGetReg(hif, (BYTE) num, &idData, &erc, NULL)) {
		printf("DpcGetReg failed, erc=%d\n", erc);
		return -1;
	} else {
		return ((jint) idData);
	}
}

