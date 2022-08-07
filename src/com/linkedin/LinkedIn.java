package com.linkedin;

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

public class LinkedIn implements JobCollection {
    private final String url;
    private final String pref;
    public LinkedIn(String url,String pref) {
        this.url = url;
        this.pref = pref;
    }
    private List<JobCard> jobs;


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

        for (int page = 1; page <= 1; page++) {
            try{
                document = Jsoup.connect(this.url + "/").get();
            }
            catch (Exception e){
                System.out.println("There's some error in fetching data from the website.");
            }
            Elements jobCards = document.select("div.base-card");
            for (Element jobCard : jobCards) {
                String role = jobCard.select("h3.base-search-card__title").text();
                String companyName = jobCard.select("h4.base-search-card__subtitle").text();
                String link = jobCard.select("a.base-card__full-link").attr("abs:href");
                String type = (role.contains("Internship") || role.contains("INTERNSHIP") || role.contains("Intern") || role.contains("Interns")) ? "INTERNSHIP" : "JOB";
                String salary = "Not provided";
                String deadline = "Not provided";
                String location = jobCard.select("span.job-search-card__location").text();
                if(pref.equals("INTERN") && type != "INTERNSHIP") continue;
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
