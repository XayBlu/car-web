package com.carweb.rest.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DataMuseResponse {
    private List<Word> words;

    @Getter
    @AllArgsConstructor
    static class Word {
        private String word;
    }
}
