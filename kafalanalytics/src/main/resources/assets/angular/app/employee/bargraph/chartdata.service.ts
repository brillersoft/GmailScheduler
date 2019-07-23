import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ChartdataService {

  constructor(private http: HttpClient) { }

  chartUrl = 'http://127.0.0.1:8080/KafalAnalytics/app/orgdata'
  chartUrlClient = 'http://127.0.0.1:8080/KafalAnalytics/app/orgdataclient'
  path = 'Content-Type' + 'application/json';
  headers = new HttpHeaders().append('Access-Control-Allow-Origin','*');

  // headers.append('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');

  options = {
    headers: this.headers.append('Content-Type','application/json')
  };

  getChartData(){
    return this.http.get(this.chartUrl, this.options);
  }

  getChartDataClient(){
    return this.http.get(this.chartUrlClient, this.options);
  }

}
