package com.freshersworld;

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

public class Freshersworld implements JobCollection {
    private final String URL;
    private List<JobCard> jobs;
    private final String pref;
    public Freshersworld(String url,String pref){
        this.URL = url;
        this.pref = pref;
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
        Document document = null,main = null;
        JobCard jc;
        try {
            main = Jsoup.connect(this.URL).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int totalJobs = Integer.parseInt(main.select(".number-of-jobs").text());
        //indexing from 0
        // Representing offset
        for(int page = 0; page <= totalJobs; page+= 30){
            try {
                document = Jsoup.connect(this.URL + "?&limit=30&offset=" + page).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Elements jobCards = document.select(".col-md-12.col-lg-12.col-xs-12.padding-none.job-container.jobs-on-hover.top_space");
            for(Element jobCard : jobCards){
                String companyName = jobCard.select(".col-md-12.col-xs-12.col-lg-12.padding-none.left_move_up > a").attr("title");
                String link = jobCard.select(".col-md-12.col-xs-12.col-lg-12.padding-none.left_move_up > a").attr("href");
                String role = jobCard.select(".col-md-12.col-xs-12.col-lg-12.padding-none.left_move_up > div").first().text();
                String[] locations = jobCard.select(".job-location.display-block.modal-open > a").text().split(" ");
                String location = "";
                for(int i = 0; i < locations.length; i++){
                    if(i == locations.length - 1) location += locations[i] + ".";
                    else location += locations[i] + ", ";
                }
                if(location == ".") location = "Not Provided";
                String deadline = jobCard.select(".col-md-4.col-xs-4.col-lg-4.padding-none > .padding-left-4").text();
                String type = (role.contains("Internship") || role.contains("INTERNSHIP") || role.contains("Intern") || role.contains("Interns")) ? "INTERNSHIP" : "JOB";
                String estimatedSalary = "Not disclosed";
                if(pref.equals("INTERN") && type != "INTERNSHIP") continue;
                jc = new JobCard(role,companyName,link,type,estimatedSalary,deadline,location);
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
