#ifndef OCRData_H_
#define OCRData_H_

#include <jni.h>
#include <string.h>

using namespace std;

typedef struct _JNI_POSREC {
    jclass cls;
    jmethodID constructortorID;
    jfieldID xID;
    jfieldID yID;
    jfieldID widthID;
    jfieldID heightID;
    jfieldID textID;
} JNI_POSREC;

/**
*   Return Search class.
*/
struct OCRResult {
    int x;
    int y;
    int width;
    int height;
    string text;
};
#endif