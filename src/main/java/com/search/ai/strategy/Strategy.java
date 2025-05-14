package com.search.ai.strategy;

import com.search.ai.model.Note;

import java.util.List;

public interface Strategy {

    List<?> search(String keyword);
}
