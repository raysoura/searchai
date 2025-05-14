package com.search.ai.strategy;

import com.search.ai.annotations.StrategyType;
import com.search.ai.constants.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Configuration
public class StrategyRegistry {

    Map<SearchStrategy, Strategy> strategyMap;

    @Autowired
    StrategyRegistry(List<Strategy> strategyList) {

        this.strategyMap = new EnumMap<>(SearchStrategy.class);
        for(Strategy strategy: strategyList) {
            StrategyType strategyType = strategy.getClass().getAnnotation(StrategyType.class);
            this.strategyMap.put(strategyType.value(), strategy);
        }
    }

    public Strategy get(SearchStrategy searchStrategy) {
        return strategyMap.get(searchStrategy);
    }
}
