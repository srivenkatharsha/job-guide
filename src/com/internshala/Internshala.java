package com.internshala;


import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.utils.JobCard;
import com.utils.JobCollection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Internshala implements JobCollection {
    private final String URL;
    private List<JobCard> jobs;
    public Internshala(String url){
        this.URL = url;
    }
    @Override
    public void createCSV() {
        File file = new File("jobs.csv");
        try {
            if(file.createNewFile()){
                CSVWriter writer = new CSVWriter(new FileWriter("jobs.csv"));
                writer.writeNext(new String[]{"Role","Company Name" ,"Link", "Type", "Estimated Salary","Deadline", "Location"});
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(file,true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ColumnPositionMappingStrategy<JobCard> mappingStrategy  = new ColumnPositionMappingStrategy<>();
        mappingStrategy.setType(JobCard.class);
        String[] columns = {"role","companyName" ,"link", "type", "estimatedSalary","deadline", "location"};
        mappingStrategy.setColumnMapping(columns);
        StatefulBeanToCsvBuilder<JobCard> builder = new StatefulBeanToCsvBuilder<>(writer);
        StatefulBeanToCsv<JobCard> beanWriter = builder.withMappingStrategy(mappingStrategy).build();
        try {
            beanWriter.write(this.jobs);
        } catch (CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        } catch (CsvRequiredFieldEmptyException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<JobCard> fetchData() {
        jobs = new ArrayList<>();
        Document main = null,document = null;
        JobCard jc;
        try {
            main = Jsoup.connect(this.URL).get();
        } catch (Exception e) {
            System.out.println("There's some error in fetching data from the website.");
        }
        int totalPages = Integer.parseInt(main.getElementById("total_pages").text());
        for (int page = 1; page <= totalPages; page++) {
            try{
                document = Jsoup.connect(this.URL + "page-" + page + "/").get();
            }
            catch (Exception e){
                System.out.println("There's some error in fetching data from the website.");
            }
            Elements jobCards = document.select(".container-fluid.individual_internship.visibilityTrackerItem > .internship_meta");
            for (Element jobCard : jobCards) {
                String role = jobCard.select(".company > .heading_4_5.profile > .view_detail_button").text();
                String companyName = jobCard.select(".heading_6.company_name > .link_display_like_text.view_detail_button").text();
                String link = "https://internshala.com/" + jobCard.select(".company > .heading_4_5.profile > .view_detail_button").attr("href");
                String salary = jobCard.select(".stipend").text();
                String deadline;
                Elements detailsRow = jobCard.getElementsByClass("item_body");
                Object[] arr = detailsRow.toArray();
                if (salary.isEmpty()) {
                    salary = ((Element) arr[1]).text();
                    deadline = ((Element) arr[2]).text();
                } else {
                    deadline = ((Element) arr[3]).text();
                }
                Elements locations = jobCard.getElementsByClass("location_link view_detail_button");
                Object[] locationsArray = locations.toArray();
                String location = "";
                for (int j = 0; j < locations.size(); j++) {
                    if (j == locationsArray.length - 1) location += ((Element) locationsArray[j]).text() + ".";
                    else location += ((Element) locationsArray[j]).text() + ", ";
                }
                String type = (salary.contains("month") || salary.contains("Unpaid")) ? "INTERNSHIP" : "JOB";
                jc = new JobCard(role, companyName, link, type, salary, deadline, location);
                jobs.add(jc);
            }
        }
        this.createCSV();
        return jobs;
    }

    @Override
    public void run() {
        this.fetchData();
    }
}

