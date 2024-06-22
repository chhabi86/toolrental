package com.example.toolrental.repository;

import com.example.toolrental.model.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository for managing tool data.
 * Author: Chhabi Sharma
 */
@Repository
public class ToolRepository {

    private static final Logger logger = LoggerFactory.getLogger(ToolRepository.class);
    private final Map<String, Tool> tools = new HashMap<>();

    public ToolRepository() {
        tools.put("CHNS", new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
        tools.put("LADW", new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
        tools.put("JAKD", new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
        tools.put("JAKR", new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
        logger.info("Initialized ToolRepository with tools: {}", tools);
    }

    /**
     * Finds a tool by its code.
     *
     * @param code the code of the tool to be found
     * @return the Tool instance if found, otherwise null
     */
    public Tool findByCode(String code) {
        Tool tool = tools.get(code);
        logger.debug("Found tool by code: code={}, tool={}", code, tool);
        return tool;
    }
}
