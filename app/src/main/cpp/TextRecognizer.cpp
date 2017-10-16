////////////////////////////////////////////////////////////////////
// Standard includes:
#include <iostream>
#include <sstream>

////////////////////////////////////////////////////////////////////
// File includes:
#include "TextRecognizer.hpp"

using namespace std;
using namespace cv;
using namespace cv::text;

TextRecognizer::TextRecognizer(string tessFolder, string lang, string nm1File, string nm2File) {
    er_filter1 = createERFilterNM1(loadClassifierNM1(nm1File), 16, 0.00015f, 0.13f, 0.2f, true,
                                   0.1f);
    er_filter2 = createERFilterNM2(loadClassifierNM2(nm2File), 0.5);
    tess = new tesseract::TessBaseAPI();
    if (tess->Init(tessFolder.c_str(), lang.c_str())) {
        LOGD("OCRTesseract: Could not initialize tesseract.");
        throw 1;
    }
    tess->SetPageSegMode(tesseract::PSM_AUTO);
//    tess->SetVariable("tessedit_char_whitelist",
//                      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    tess->SetVariable("tessedit_char_blacklist","_");
    tess->SetVariable("save_best_choices", "T");

    mLang = string(lang);
}

TextRecognizer::~TextRecognizer() {
    er_filter1.release();
    er_filter2.release();
    tess->End();
}


bool isRepetitive(const string &s) {
    int count = 0;
    for (int i = 0; i < (int) s.size(); i++) {
        if ((s[i] == 'i') ||
            (s[i] == 'l') ||
            (s[i] == 'I'))
            count++;
    }
    if (count > ((int) s.size() + 1) / 2) {
        return true;
    }
    return false;
}


void er_draw(vector<Mat> &channels, vector<vector<ERStat> > &regions, vector<Vec2i> group,
             Mat &segmentation) {
    for (int r = 0; r < (int) group.size(); r++) {
        ERStat er = regions[group[r][0]][group[r][1]];
        if (er.parent != NULL) {
            int newMaskVal = 255;
            int flags = 4 + (newMaskVal << 8) + FLOODFILL_FIXED_RANGE + FLOODFILL_MASK_ONLY;
            floodFill(channels[group[r][0]], segmentation,
                      Point(er.pixel % channels[group[r][0]].cols,
                            er.pixel / channels[group[r][0]].cols),
                      Scalar(255), 0, Scalar(er.level), Scalar(0), flags);
        }
    }
}


vector<OCRResult> TextRecognizer::processFrame(const cv::Mat &input, float scale) {

    double t_rs = (double) getTickCount();
    double t_start = (double) getTickCount();
    vector<OCRResult> vector1;
    Size srcSize = input.size();
    Size smallSize(srcSize.width / 2, srcSize.height / 2);
    Mat small(smallSize, CV_8UC4);
    resize(input, small, smallSize);
    Mat src(smallSize, CV_8UC3);
    cvtColor(small, src, COLOR_RGBA2RGB);
    vector<Mat> channels;
    Mat grey;
    cvtColor(src, grey, COLOR_RGBA2GRAY);
    channels.push_back(grey);
    channels.push_back(255 - grey);
    LOGD("TIME_INIT %f", ((double) getTickCount() - t_rs) * 1000 / getTickFrequency());


    // Region Detection
    double t_d = (double) getTickCount();
    vector<vector<ERStat> > regions(channels.size());
    for (int c = 0; c < (int) channels.size(); c++) {
        er_filter1->run(channels[c], regions[c]);
        er_filter2->run(channels[c], regions[c]);
    }
    LOGD("TIME_REGION_DETECTION %f", ((double) getTickCount() - t_d) * 1000 / getTickFrequency());


    // Detect character groups
    double t_g = (double) getTickCount();
    vector<vector<Vec2i> > nm_region_groups;
    vector<Rect> nm_boxes;
    erGrouping(src, channels, regions, nm_region_groups, nm_boxes, ERGROUPING_ORIENTATION_HORIZ);
    LOGD("TIME_GROUPING  %f", ((double) getTickCount() - t_g) * 1000 / getTickFrequency());


    /*Text Recognition (OCR)*/
    double t_r = (double) getTickCount();
    string output;
    string result;
    for (int i = 0; i < (int) nm_boxes.size(); i++) {
        Mat group_img = Mat::zeros(src.rows + 2, src.cols + 2, CV_8UC1);
        er_draw(channels, regions, nm_region_groups[i], group_img);
        Mat group_segmentation;
        group_img.copyTo(group_segmentation);
        group_img(nm_boxes[i]).copyTo(group_img);
        copyMakeBorder(group_img, group_img, 15, 15, 15, 15, BORDER_CONSTANT, Scalar(0));

        // Use tesseract raw API instead of opencv tesseract wrapper.
        tess->SetImage(group_img.data, group_img.size().width, group_img.size().height,
                       group_img.channels(), group_img.step1());
        tess->Recognize(0);
        output = string(tess->GetUTF8Text());
        tesseract::ResultIterator *ri = tess->GetIterator();
        tesseract::PageIteratorLevel level = tesseract::RIL_WORD;
        result = "";
        if (ri != 0) {
            do {
                const char *word = ri->GetUTF8Text(level);
                if (word != NULL) {
                    result += string(word) + ' ';
                    delete[] word;
                }
            } while (ri->Next(level));
        }
        tess->Clear();
        LOGD("%s", result.c_str());
        OCRResult ocrResult;
        ocrResult.x = nm_boxes[i].tl().x * 2;
        ocrResult.y = nm_boxes[i].tl().y * 2;
        ocrResult.width = nm_boxes[i].br().x * 2 - nm_boxes[i].tl().x * 2;
        ocrResult.height = nm_boxes[i].br().y * 2 - nm_boxes[i].tl().y * 2;
        ocrResult.text = result;

        vector1.push_back(ocrResult);

    }

    LOGD("TIME_OCR  %f", ((double) getTickCount() - t_r) * 1000 / getTickFrequency());
    LOGD("=================================================");
    LOGD("TOTAL  %f", ((double) getTickCount() - t_start) * 1000 / getTickFrequency());
    LOGD(" Lang %s\n", mLang.c_str());
    return vector1;
}



