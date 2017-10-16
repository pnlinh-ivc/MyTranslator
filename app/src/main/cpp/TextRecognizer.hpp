#ifndef TextRecognizer_hpp
#define TextRecognizer_hpp

#include <opencv2/core/utility.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/text.hpp>
#include <baseapi.h>
#include "common.hpp"
#include "OCRData.hpp"

using namespace std;

class TextRecognizer {
public:

    TextRecognizer(std::string tessFolder, std::string lang, std::string nm1File,
                   std::string nm2File);

    ~TextRecognizer();

    vector<OCRResult> processFrame(const cv::Mat &bgraMat, float scale = 1.0F);

private:
    cv::Ptr<cv::text::ERFilter> er_filter1;
    cv::Ptr<cv::text::ERFilter> er_filter2;
    tesseract::TessBaseAPI *tess;
    string mLang;
};

#endif
