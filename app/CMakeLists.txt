#https://stackoverflow.com/questions/41892544/android-cmake-and-static-linking
cmake_minimum_required(VERSION 2.8.7)

set(LIB_PATH ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})
set(OPENCV_INCLUDE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/opencv/include)
set(TESSERACT_PATH src/main/cpp/com_googlecode_tesseract_android/src)
set(LEPTONICA_PATH src/main/cpp/com_googlecode_leptonica_android/src)
set(OPENCV_OCR_PATH src/main/cpp)

set(TESSERACT_INCLUDE_PATH	  ${TESSERACT_PATH}/api
							  ${TESSERACT_PATH}/ccmain
							  ${TESSERACT_PATH}/ccstruct
							  ${TESSERACT_PATH}/ccutil
							  ${TESSERACT_PATH}/classify
							  ${TESSERACT_PATH}/cube
							  ${TESSERACT_PATH}/cutil
							  ${TESSERACT_PATH}/dict
							  ${TESSERACT_PATH}/opencl
							  ${TESSERACT_PATH}/neural_networks/runtime
							  ${TESSERACT_PATH}/textord
							  ${TESSERACT_PATH}/viewer
							  ${TESSERACT_PATH}/wordrec)

set(LEPTONICA_INCLUDE_PATH	  ${LEPTONICA_PATH}
							  ${LEPTONICA_PATH}/src)

include_directories(${OPENCV_INCLUDE_PATH} ${TESSERACT_INCLUDE_PATH} ${LEPTONICA_INCLUDE_PATH} ${OPENCV_OCR_PATH})


add_library( lib_opencv SHARED IMPORTED )
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${LIB_PATH}/libopencv_java3.so)

add_library( lib_lept SHARED IMPORTED )
set_target_properties(lib_lept PROPERTIES IMPORTED_LOCATION ${LIB_PATH}/liblept.so)

add_library( lib_tess SHARED IMPORTED )
set_target_properties(lib_tess PROPERTIES IMPORTED_LOCATION ${LIB_PATH}/libtess.so)

add_library(opencvocr SHARED src/main/cpp/TextRecognizer.cpp src/main/cpp/ocr_jni.cpp)

#Link library needed by hello-jni, in this case log and android.
target_link_libraries(opencvocr lib_opencv lib_lept lib_tess log android )

