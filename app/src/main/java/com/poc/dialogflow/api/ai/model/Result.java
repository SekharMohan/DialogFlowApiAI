package com.poc.dialogflow.api.ai.model;

import java.util.List;

public class Result {
    public String source;
    public String resolvedQuery;
    public String action;
    public boolean actionIncomplete;
    public Parameters parameters;
    public List<Contexts> contexts;
    public Metadata metadata;
    public Fulfillment fulfillment;
    public int score;
}
