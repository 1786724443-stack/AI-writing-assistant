package com.example.aiwriter.service;

import com.example.aiwriter.dto.request.*;
import com.example.aiwriter.dto.response.WritingResponse;

public interface WritingService {

    WritingResponse generate(GenerateRequest request);

    WritingResponse polish(PolishRequest request);

    WritingResponse expand(ExpandRequest request);

    WritingResponse summarize(SummarizeRequest request);

    WritingResponse translate(TranslateRequest request);
}
