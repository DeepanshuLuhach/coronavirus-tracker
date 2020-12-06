package com.luhach.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import com.luhach.coronavirustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CoronaVirusDataService {

    private static String ConfirmedCaseURL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> allStats = new ArrayList<>();
    private int totalReportedCases, deltaSum;
    // Tells spring to execute this method once the instance of the class is constructed
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *") // Setup a cron job to run this job every day
    public void fetchData() throws IOException, InterruptedException {
        // Create a newStat list of LocationStats objects which can be later assigned to all Stats to elimnate the concurrency issues
        List<LocationStats> newStats = new ArrayList<>();
        int newtotalReportedCases=0, newDeltaSum=0, delta;
        // Created a request and sent it to the URL to fecth the data
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ConfirmedCaseURL)).build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Reading the CSV data and fetch the relevant information
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStats = new LocationStats();
            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));
            int todaysCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            delta=todaysCases - prevDayCases;
            locationStats.setDelta(delta); 
            locationStats.setLatestTotalCases(todaysCases);
            newtotalReportedCases=newtotalReportedCases+todaysCases ;
            newDeltaSum=newDeltaSum+delta;
            newStats.add(locationStats);
        }
        this.allStats = newStats;
        totalReportedCases=newtotalReportedCases;
        deltaSum=newDeltaSum;
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    public int getTotalReportedCases() {
        return totalReportedCases;
    }

    public int getDeltaSum() {
        return deltaSum;
    }
    
    
}
