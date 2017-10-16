#include "ocr_jni.h"
#include <opencv2/core/core.hpp>
#include <android/log.h>

#include <string>
#include <vector>
#include "common.hpp"
#include "TextRecognizer.hpp"
#include "OCRData.hpp"

using namespace std;
using namespace cv;

using namespace std;


JNI_POSREC *jniPosRec = NULL;
string className;
bool isActive;


void LoadJniPosRec(JNIEnv *env) {
    if (jniPosRec != NULL)
        return;
    jniPosRec = new JNI_POSREC;
    jclass temp = env->FindClass("com/ndanh/mytranslator/model/OCRResult");
    jniPosRec->cls = (jclass) env->NewGlobalRef(temp);
    jniPosRec->constructortorID = env->GetMethodID(jniPosRec->cls, "<init>", "()V");

    jniPosRec->xID = env->GetFieldID(jniPosRec->cls, "x", "I");
    jniPosRec->yID = env->GetFieldID(jniPosRec->cls, "y", "I");
    jniPosRec->widthID = env->GetFieldID(jniPosRec->cls, "width", "I");
    jniPosRec->heightID = env->GetFieldID(jniPosRec->cls, "height", "I");
    jniPosRec->textID = env->GetFieldID(jniPosRec->cls, "text", "Ljava/lang/String;");;

}


JNIEXPORT jlong JNICALL Java_com_ndanh_mytranslator_jni_NativeMarkerDetector_nativeCreateObject(
        JNIEnv *jenv, jobject, jstring jtessFolder, jstring jlang, jstring jnm1, jstring jnm2,
        jstring clazzname) {
    jlong detector;
    try {
        if (!isActive) {
            className = jenv->GetStringUTFChars(clazzname, NULL);
            LoadJniPosRec(jenv);
        }
        isActive = true;
        const char *tessFolder = jenv->GetStringUTFChars(jtessFolder, NULL);
        const char *lang = jenv->GetStringUTFChars(jlang, NULL);
        const char *nm1 = jenv->GetStringUTFChars(jnm1, NULL);
        const char *nm2 = jenv->GetStringUTFChars(jnm2, NULL);
        detector = (jlong) new TextRecognizer(string(tessFolder), string(lang), string(nm1),
                                              string(nm2));
    } catch (...) {
        LOGD("nativeCreateObject caught unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je,
                       "Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
        return 0;
    }

    return detector;
}

void FillStudentRecValuesToJni(JNIEnv *env, jobject jPosRec, OCRResult cPosRec) {
    env->SetIntField(jPosRec, jniPosRec->xID, cPosRec.x);
    env->SetIntField(jPosRec, jniPosRec->yID, cPosRec.y);
    env->SetIntField(jPosRec, jniPosRec->widthID, cPosRec.width);
    env->SetIntField(jPosRec, jniPosRec->heightID, cPosRec.height);
    env->SetObjectField(jPosRec, jniPosRec->textID, env->NewStringUTF(cPosRec.text.c_str()));
}

JNIEXPORT jobjectArray JNICALL
Java_com_ndanh_mytranslator_jni_NativeMarkerDetector_nativeFindMarkers(
        JNIEnv *jenv, jobject, jlong thiz, jlong imageBgra, jlong transMat, jfloat scale) {
    jniPosRec = NULL;
    LoadJniPosRec(jenv);
    try {
        TextRecognizer *markerDetector = (TextRecognizer *) thiz;
        Mat *image = (Mat *) imageBgra;
        vector<OCRResult> vector1 = markerDetector->processFrame(*image, (float) scale);
        if (vector1.size() != 0) {
            jobjectArray jPosRecArray = jenv->NewObjectArray(vector1.size(), jniPosRec->cls, NULL);
            for (size_t i = 0; i < vector1.size(); i++) {
                jobject jPosRec = jenv->NewObject(jniPosRec->cls, jniPosRec->constructortorID);
                FillStudentRecValuesToJni(jenv, jPosRec, vector1[i]);
                jenv->SetObjectArrayElement(jPosRecArray, i, jPosRec);
            }
            return jPosRecArray;
        }
    } catch (...) {
        LOGD("nativeDetect caught unknown exception");
    }
    return NULL;
}

JNIEXPORT void JNICALL Java_com_ndanh_mytranslator_jni_NativeMarkerDetector_nativeDestroyObject(
        JNIEnv *jenv, jobject, jlong thiz) {
    try {
        LOGD("Destroy");
    } catch (...) {
        LOGD("nativeDestroyObject caught unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je,
                       "Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
    }
}
