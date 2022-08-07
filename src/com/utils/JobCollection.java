package com.utils;

import java.util.List;

public interface JobCollection extends Runnable {
    public abstract void createCSV();
    public abstract List<JobCard> fetchData();
}
