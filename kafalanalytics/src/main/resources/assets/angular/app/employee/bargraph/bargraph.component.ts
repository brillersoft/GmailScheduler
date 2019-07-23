import { Component, OnInit, APP_INITIALIZER} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ChartdataService } from './chartdata.service';
import { Router ,ActivatedRoute, UrlHandlingStrategy} from '@angular/router';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import {HttpService} from '../../service/http.service';
import { Config } from 'protractor';
import { Observable } from 'rxjs/Observable';
import { ChangeDetectorRef } from '@angular/core';
import { MainDashboard } from './bargraph';
import { Dashboard } from '../dashboard/dashboard';
import { INTERNAL_BROWSER_DYNAMIC_PLATFORM_PROVIDERS } from '@angular/platform-browser-dynamic/src/platform_providers';
declare var $:any;

var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../webapp');

@Component({
  selector: 'app-root',
  // templateUrl: '../../../../../../webapp/templates/employee/bargraph.component.html',
  templateUrl: 'templates/employee/bargraph.component.html'
  // styleUrls: ['./bargraph.component.css'], 
  
})



export class BargraphComponent implements OnInit {

  constructor(
    private chartdataService: ChartdataService,
    private httpService: HttpService,
    private router:Router,
    private route: ActivatedRoute,
    private sharedService:SharedService,
    private cdRef:ChangeDetectorRef) { 

    

    
  }

  public maindashboard:any = {};
  chartData: any[];
  barsLabel: string[];
  lengthOfChart: number;
  noOfPages: number = 1;
  pageLimit: number = 4;
  startFrom: number = 1;
  currentPage: number = 1;
  // initialize data of bar chart
  public barChartLabels:string[]=['a','b','c','d','e','f'];
  public barChartData:any[]=[{data:[1, 2, 3, 4, 5, 6],label:'A'},{data:[1, 2, 3, 4, 5, 6],label:'B'}];
  tempbarmargin:Array<number>;
  tempbarrevenue:Array<number>;
  previousPage:boolean = false;
  nextPage:boolean = false;
  clientPage:boolean = false;
  datadoughnutChart:Array<number[]> = []; 
  // initialize data of doughnut chart
  // map = new Map<number,Array<number>>([[0, [45,55]],[1,[60,40]],[2, [54,46]],[3, [58,42]],[4, [90,10]],[5, [30,70]],[6, [40,60]],[7, [88,12]],[8, [90,10]],[9, [35,65]],[10, [75,25]],[11, [50,50]],[12, [12,88]],[13, [55,45]],[14, [63,37]],[15, [83,17]]]);
  // map = new Map<number, Array<number>>();
  map = new Map<String,Array<number>>([['0', [45,55]],['1',[60,40]],['2', [54,46]],['3', [58,42]],['4', [90,10]],['5', [30,70]],['6', [40,60]],['7', [88,12]],['8', [90,10]],['9', [35,65]],['10', [75,25]],['11', [50,50]],['12', [12,88]],['13', [55,45]],['14', [63,37]],['15', [83,17]]]);
  dataBarLabels:string[]=[];
  dataBarRevenue:Array<number>=[];
  dataBarMargin:Array<number>=[];
  public innetWidth: any;
  private dashboard:Object = {};
  public teamRelation:any = {};
  public clientRelation:any = {};
  private employeeReport:object = {};
  public empId:Object = {};
  public sortData:Object  = {};
  tab:boolean = false;

  ngOnInit() {

    var  requestObj = new BaseContainer();  
    this.httpService.postRequest<Dashboard>(this.dashboard,'app/auth/dislaydashboard').subscribe(
      dashboard=> {
          if(dashboard.rsBody.result=='success')
          {
              this.dashboard = dashboard.rsBody.msg;
              this.teamRelation = this.dashboard['relationWithTeam'];
              this.clientRelation = this.dashboard['relationWithClient'];
              this.empId =  this.dashboard['employeeId'];
          }
        });
    
    this.innetWidth = window.innerWidth;
    // console.log(this.innetWidth);
    if(this.innetWidth<440){
      this.pageLimit = 3;
    }

    if(this.innetWidth<800){
      this.tab = true;
    }
    // this.setChartData();
    // this.loadInitialCharts();

    this.httpService.postRequest<MainDashboard>(this.maindashboard,'app/orgdata').subscribe(
      data => {
        // console.log(data);
        this.dataBarLabels = data.dataBarLabels;
        this.dataBarMargin = data.dataBarMargin;
        this.dataBarRevenue = data.dataBarRevenue;
        this.map = this.xah_obj_to_map(data.map);
        
        this.loadInitialCharts();
      }
    );

    
    
    
  }

  

  

  public getCurrentPage(){
    return true;
  }  

  public setChartData(){
    this.clientPage = false;
    this.httpService.postRequest<MainDashboard>(this.maindashboard,'app/orgdata').subscribe(
      data => {
        // console.log(data);
        this.dataBarLabels = data.dataBarLabels;
        this.dataBarMargin = data.dataBarMargin;
        this.dataBarRevenue = data.dataBarRevenue;
        this.map = this.xah_obj_to_map(data.map);
        
        this.loadInitialCharts();
      }
    );
  
  }

  public setChartDataClient(){
    this.clientPage = true;
    this.httpService.postRequest<MainDashboard>(this.maindashboard,'app/orgdataclient')
    .subscribe(data => {
      this.map = this.xah_obj_to_map(data.map);
      this.loadInitialCharts();
    });
  }

  xah_obj_to_map  = ( obj => {
    const mp = new Map;
    Object.keys(obj).forEach(k => { mp.set(k, obj[k])  });
    
    return mp;
  });

  getMyStyles() {
    let myStyles = {
       'width': this.tab ? '600px' : '800px'
    };
    return myStyles;
}  

getMyStylesDonut() {
  let myStyles = {
     'width': this.tab ? '580px' : '730px'
  };
  return myStyles;
}  


  public barChartOptions: any = {
    scaleShowVerticalLines: false,
    responsive: true,
    tooltipItem: true,
    maintainAspectRatio: false,
    // maintainAspectRatio: false,
    scales:{
      xAxes:[{
          stacked:true,
          barThickness:45,
          gridLines: { 
            borderDash: [5], 
            lineWidth: 1.4
           }, 
          ticks: { beginAtZero: true} 
         
      }],
      yAxes:[{
          stacked:true
      }]
      // legend: {position: 'bottom'}
  }
  };

  // dataBarLabels:string[] = ['Bank of America', 'City Bank', 'RBS', 'TCS', 'HCL', 'Wipro','DELL','Digimoksha','Kafal','Bricon','ABC','DEF','GHI','JKL','LMN','OPQ'];
  // dataBarRevenue:Array<number> = [65, 59, 80, 81, 56, 55, 40, 60, 85, 70, 65, 50, 82, 55, 59, 73];
  // dataBarMargin:Array<number> = [28, 48, 40, 19, 86, 27, 90, 14, 20, 25, 18, 15, 20, 29, 21, 5];
    

  public loadInitialCharts(){
    this.clearCharts();
    this.lengthOfChart = this.dataBarLabels.length;
    this.noOfPages = Math.ceil(this.lengthOfChart/this.pageLimit);
    this.currentPage = 1;
    this.startFrom = 0;
    this.previousPage = false;
    this.nextPage = true;
    this.tempbarmargin = this.dataBarMargin.slice(this.startFrom,this.startFrom+this.pageLimit);
    this.tempbarrevenue = this.dataBarRevenue.slice(this.startFrom,this.startFrom+this.pageLimit);
    this.barChartData = [{data: this.tempbarrevenue,label: 'Customer Total Revenue in Millions'},
                        {data: this.tempbarmargin,label: 'Gross Margin in Percentage'}];
    setTimeout(() => this.barChartLabels = this.dataBarLabels.slice(this.startFrom,this.startFrom+this.pageLimit), 0);
    let j = 0;
    for(var i=this.startFrom;i<this.pageLimit;i++){
      
      this.datadoughnutChart[j]=this.map.get(i.toString());
      j++;
    }
    // console.log(this.datadoughnutChart);
  }
  
  getUrl1(){
    if(this.datadoughnutChart[0] != null){
    if(this.datadoughnutChart[0]['0'] == 0 && this.datadoughnutChart[0]['1'] == 0 && this.datadoughnutChart[0]['2'] == 0){
      return "url()";
    }else if(this.datadoughnutChart[0]['0'] == this.datadoughnutChart[0]['1'] && this.datadoughnutChart[0]['0'] == this.datadoughnutChart[0]['2']){
      return "url(images/smilies-03.png)";
    }
    else if(this.datadoughnutChart[0]['0']> this.datadoughnutChart[0]['1'] && this.datadoughnutChart[0]['0'] > this.datadoughnutChart[0]['2']){
    return "url(images/smilies-02.png)";
    }
    else if(this.datadoughnutChart[0]['1']> this.datadoughnutChart[0]['0'] && this.datadoughnutChart[0]['1'] > this.datadoughnutChart[0]['2']){
      return "url(images/smilies-01.png)";
    }
    else if(this.datadoughnutChart[0]['2']> this.datadoughnutChart[0]['0'] && this.datadoughnutChart[0]['2'] > this.datadoughnutChart[0]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[0]['0'] == this.datadoughnutChart[0]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[0]['0'] == this.datadoughnutChart[0]['2']){
      return "url(images/smilies-02.png)";
    }else if(this.datadoughnutChart[0]['1'] == this.datadoughnutChart[0]['2']){
      return "url(images/smilies-01.png)";
    }
  }else{
    return "url()";
  }

  }

  getUrl2(){
    if(this.datadoughnutChart[1] != null){
    if(this.datadoughnutChart[1]['0'] == 0 && this.datadoughnutChart[1]['1'] == 0 && this.datadoughnutChart[1]['2'] == 0){
      return "url()";
    }else if(this.datadoughnutChart[1]['0'] == this.datadoughnutChart[1]['1'] && this.datadoughnutChart[1]['0'] == this.datadoughnutChart[1]['2']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[1]['0']> this.datadoughnutChart[1]['1'] && this.datadoughnutChart[1]['0'] > this.datadoughnutChart[1]['2']){
    return "url(images/smilies-02.png)";
    }
    else if(this.datadoughnutChart[1]['1']> this.datadoughnutChart[1]['0'] && this.datadoughnutChart[1]['1'] > this.datadoughnutChart[1]['2']){
      return "url(images/smilies-01.png)";
    }
    else if(this.datadoughnutChart[1]['2']> this.datadoughnutChart[1]['0'] && this.datadoughnutChart[1]['2'] > this.datadoughnutChart[1]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[1]['0'] == this.datadoughnutChart[1]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[1]['0'] == this.datadoughnutChart[1]['2']){
      return "url(images/smilies-02.png)";
    }else if(this.datadoughnutChart[1]['1'] == this.datadoughnutChart[1]['2']){
      return "url(images/smilies-01.png)";
    }
    }else{
      return "url()";
    }

  }

  getUrl3(){
    if(this.datadoughnutChart[2] != null){
    if(this.datadoughnutChart[2]['0'] == 0 && this.datadoughnutChart[2]['1'] == 0 && this.datadoughnutChart[2]['2'] == 0){
      return "url()";
    }else if(this.datadoughnutChart[2]['0'] == this.datadoughnutChart[2]['1'] && this.datadoughnutChart[2]['0'] == this.datadoughnutChart[2]['2']){
      return "url(images/smilies-03.png)";
    }
    else if(this.datadoughnutChart[2]['0']> this.datadoughnutChart[2]['1'] && this.datadoughnutChart[2]['0'] > this.datadoughnutChart[2]['2']){
    return "url(images/smilies-02.png)";
    }
    else if(this.datadoughnutChart[2]['1']> this.datadoughnutChart[2]['0'] && this.datadoughnutChart[2]['1'] > this.datadoughnutChart[2]['2']){
      return "url(images/smilies-01.png)";
    }
    else if(this.datadoughnutChart[2]['2']> this.datadoughnutChart[2]['0'] && this.datadoughnutChart[2]['2'] > this.datadoughnutChart[2]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[2]['0'] == this.datadoughnutChart[2]['1'] ){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[2]['0'] == this.datadoughnutChart[2]['2'] ){
      return "url(images/smilies-02.png)";
    }else if(this.datadoughnutChart[2]['1'] == this.datadoughnutChart[2]['2'] ){
      return "url(images/smilies-01.png)";
    }else{
      return "url()";
    }
  }else{
    return "url()";
  }

  }

  getUrl4(){
    if(this.datadoughnutChart[3] != null){
    if(this.datadoughnutChart[3]['0'] == 0 && this.datadoughnutChart[3]['1'] == 0 && this.datadoughnutChart[3]['2'] == 0){
      return "url()";
    }else if(this.datadoughnutChart[3]['0'] == this.datadoughnutChart[3]['1'] && this.datadoughnutChart[3]['0'] == this.datadoughnutChart[3]['2']){
      return "url(images/smilies-03.png)";
    }
    else if(this.datadoughnutChart[3]['0']> this.datadoughnutChart[3]['1'] && this.datadoughnutChart[3]['0'] > this.datadoughnutChart[3]['2']){
    return "url(images/smilies-02.png)";
    }
    else if(this.datadoughnutChart[3]['1']> this.datadoughnutChart[3]['0'] && this.datadoughnutChart[3]['1'] > this.datadoughnutChart[3]['2']){
      return "url(images/smilies-01.png)";
    }
    else if(this.datadoughnutChart[3]['2']> this.datadoughnutChart[3]['0'] && this.datadoughnutChart[3]['2'] > this.datadoughnutChart[3]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[3]['0'] == this.datadoughnutChart[3]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[3]['0'] == this.datadoughnutChart[3]['2']){
      return "url(images/smilies-02.png)";
    }else if(this.datadoughnutChart[3]['1'] == this.datadoughnutChart[3]['2']){
      return "url(images/smilies-01.png)";
    }
  }else{
    return "url()";
  }
  }

  getUrl5(){
    if(this.datadoughnutChart[4] != null){
    if(this.datadoughnutChart[4]['0'] == 0 && this.datadoughnutChart[4]['1'] == 0 && this.datadoughnutChart[4]['2'] == 0){
      return "url()";
    }else if(this.datadoughnutChart[4]['0'] == this.datadoughnutChart[4]['1'] && this.datadoughnutChart[4]['0'] == this.datadoughnutChart[4]['2']){
      return "url(images/smilies-03.png)";
    }
    else if(this.datadoughnutChart[4]['0']> this.datadoughnutChart[4]['1'] && this.datadoughnutChart[4]['0'] > this.datadoughnutChart[4]['2']){
    return "url(images/smilies-02.png)";
    }
    else if(this.datadoughnutChart[4]['1']> this.datadoughnutChart[4]['0'] && this.datadoughnutChart[4]['1'] > this.datadoughnutChart[4]['2']){
      return "url(images/smilies-01.png)";
    }
    else if(this.datadoughnutChart[4]['2']> this.datadoughnutChart[4]['0'] && this.datadoughnutChart[4]['2'] > this.datadoughnutChart[4]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[4]['0'] == this.datadoughnutChart[4]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[4]['0'] == this.datadoughnutChart[4]['2']){
      return "url(images/smilies-02.png)";
    }else if(this.datadoughnutChart[4]['1'] == this.datadoughnutChart[4]['2']){
      return "url(images/smilies-01.png)";
    }
  }else{
    return "url()";
  }
  }

  getUrl6(){
    if(this.datadoughnutChart[5] != null){
    if(this.datadoughnutChart[5]['0'] == 0 && this.datadoughnutChart[5]['1'] == 0 && this.datadoughnutChart[5]['2'] == 0){
      return "url()";
    }else if(this.datadoughnutChart[5]['0'] == this.datadoughnutChart[5]['1'] && this.datadoughnutChart[5]['0'] == this.datadoughnutChart[5]['2']){
      return "url(images/smilies-03.png)";
    }
    else if(this.datadoughnutChart[5]['0']> this.datadoughnutChart[5]['1'] && this.datadoughnutChart[5]['0'] > this.datadoughnutChart[5]['2']){
    return "url(images/smilies-02.png)";
    }
    else if(this.datadoughnutChart[5]['1']> this.datadoughnutChart[5]['0'] && this.datadoughnutChart[5]['1'] > this.datadoughnutChart[5]['2']){
      return "url(images/smilies-01.png)";
    }
    else if(this.datadoughnutChart[5]['2']> this.datadoughnutChart[5]['0'] && this.datadoughnutChart[5]['2'] > this.datadoughnutChart[5]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[5]['0'] == this.datadoughnutChart[5]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[5]['0'] == this.datadoughnutChart[5]['2']){
      return "url(images/smilies-02.png)";
    }else if(this.datadoughnutChart[5]['1'] == this.datadoughnutChart[5]['2']){
      return "url(images/smilies-01.png)";
    }
  }else{
    return "url()";
  }
  }

  getUrl7(){
    if(this.datadoughnutChart[6] != null){
    if(this.datadoughnutChart[6]['0'] == 0 && this.datadoughnutChart[6]['1'] == 0 && this.datadoughnutChart[6]['2'] == 0){
      return "url()";
    }else if(this.datadoughnutChart[6]['0'] == this.datadoughnutChart[6]['1'] && this.datadoughnutChart[6]['0'] == this.datadoughnutChart[6]['2']){
      return "url(images/smilies-03.png)";
    }
    else if(this.datadoughnutChart[6]['0']> this.datadoughnutChart[6]['1'] && this.datadoughnutChart[6]['0'] > this.datadoughnutChart[6]['2']){
    return "url(images/smilies-02.png)";
    }
    else if(this.datadoughnutChart[6]['1']> this.datadoughnutChart[6]['0'] && this.datadoughnutChart[6]['1'] > this.datadoughnutChart[6]['2']){
      return "url(images/smilies-01.png)";
    }
    else if(this.datadoughnutChart[6]['2']> this.datadoughnutChart[6]['0'] && this.datadoughnutChart[6]['2'] > this.datadoughnutChart[6]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[6]['0'] == this.datadoughnutChart[6]['1']){
      return "url(images/smilies-03.png)";
    }else if(this.datadoughnutChart[6]['0'] == this.datadoughnutChart[6]['2']){
      return "url(images/smilies-02.png)";
    }else if(this.datadoughnutChart[6]['1'] == this.datadoughnutChart[6]['2']){
      return "url(images/smilies-01.png)";
    }
  }else{
    return "url()";
  }
  }

  
  
  //static data test
  public loadNextCharts(){
    this.clearCharts();
    this.lengthOfChart = this.dataBarLabels.length;
    this.noOfPages = Math.ceil(this.lengthOfChart/this.pageLimit);
    this.currentPage = this.currentPage + 1;
    this.startFrom = this.startFrom+this.pageLimit;
    if(this.currentPage==this.noOfPages){
      this.nextPage = false; 
      
     }else{
      this.nextPage = true; 
     }
    this.previousPage = true;
    this.tempbarmargin = this.dataBarMargin.slice(this.startFrom,this.startFrom+this.pageLimit);
    this.tempbarrevenue = this.dataBarRevenue.slice(this.startFrom,this.startFrom+this.pageLimit);
    this.barChartData = [{data: this.tempbarrevenue,label: 'Customer Total Revenue in Millions'},
                        {data: this.tempbarmargin,label: 'Gross Margin in Percentage'}];

    setTimeout(() => this.barChartLabels = this.dataBarLabels.slice(this.startFrom,this.startFrom+this.pageLimit), 0);
    
    let j = 0;
    for(var i=this.startFrom;i<(this.startFrom+this.pageLimit);i++){
      
      this.datadoughnutChart[j]=this.map.get(i.toString());
      j++;
    }
    // this.barChartLabels = ['Bank of Americ', 'City Bank', 'RBS', 'TCS', 'HCL', 'Wipro','DELL'];
    // console.log(this.datadoughnutChart);
  }

  

  public loadPreviousCharts(){
    this.clearCharts();
    this.lengthOfChart = this.dataBarLabels.length;
    this.noOfPages = Math.ceil(this.lengthOfChart/this.pageLimit);
    this.currentPage = this.currentPage - 1;
    this.startFrom = this.startFrom-this.pageLimit;
    if(this.currentPage==1){
     this.previousPage = false; 
    }else{
      this.previousPage = true; 
    }
    this.nextPage=true;
    this.tempbarmargin = this.dataBarMargin.slice(this.startFrom,this.startFrom+this.pageLimit);
    this.tempbarrevenue = this.dataBarRevenue.slice(this.startFrom,this.startFrom+this.pageLimit);
    this.barChartData = [{data: this.tempbarrevenue,label: 'Customer Total Revenue in Millions'},
                        {data: this.tempbarmargin,label: 'Gross Margin in Percentage'}];

    setTimeout(() => this.barChartLabels = this.dataBarLabels.slice(this.startFrom,this.startFrom+this.pageLimit), 0);
    
    let j = 0;
    for(var i=this.startFrom;i<(this.startFrom+this.pageLimit);i++){
      
      this.datadoughnutChart[j]=this.map.get(i.toString());
      j++;
    }
    // console.log(this.datadoughnutChart);
    // this.barChartLabels = ['Bank of Americ', 'City Bank', 'RBS', 'TCS', 'HCL', 'Wipro','DELL'];

  }

  public clearCharts(){
    this.barChartLabels=[];
    this.barChartData=[{data:[1, 2, 3, 4, 5, 6],label:'A'},{data:[1, 2, 3, 4, 5, 6],label:'B'}];

  }

  changRoute(name,tabName, companyIndex)
  {
      this.sharedService.setData({'tabName':tabName,'companyIndex':companyIndex});
      this.router.navigate([name]);
  } 

  changRouten(name,tabName, companyName,companyIndex)
  {
      this.sharedService.setData({'tabName':tabName,'companyName':companyName,'companyIndex':companyIndex});
      this.router.navigate([name]);
  } 

  changRouteEmployee(name,tabName){
    this.sharedService.setData({'tabName':tabName,'companyName':"",'companyIndex':0});
    var  requestObj = new BaseContainer();
        
        requestObj['employeeName'] = this.dashboard['employeeName'];
        requestObj['noOfMail'] = this.dashboard['noOfMail'];
        requestObj['department'] = this.dashboard['department'];
        requestObj['reportTo'] = this.dashboard['reportTo'];
        requestObj['noOfTeamMember'] = this.dashboard['noOfTeamMember'];
        this.router.navigate([name,requestObj]);
  }

  changRoutePersonal(name,tabName){
    
      var  requestObj = new BaseContainer();
      var userId = this.dashboard['employeeId'];
      requestObj['userId'] = this.dashboard['employeeId'];
      this.router.navigate([name,userId]);
  }

  changRouteTouch(name){
    this.router.navigate([name]);
  }

  onChangeSwitch(){

    if(!this.clientPage){
      this.setChartDataClient();
    }else{
      this.setChartData();
    }

  }

  teamtoneemployeeAdverse(obj){

    var empId = this.dashboard['employeeId'];
    var adverseFilter = "yes";
    //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
    var  routObj = new BaseContainer();
    routObj['userId'] = empId; 
    routObj['adverseFilter'] = adverseFilter;
    this.router.navigate(['my-team-email',routObj]);
  }

  teamtoneemployeeEscalation(obj){

    var empId = this.dashboard['employeeId'];
    var escalationFilter = "yes";
    //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
    var  routObj = new BaseContainer();
    routObj['userId'] = empId; 
    routObj['escalationFilter'] = escalationFilter;
    this.router.navigate(['my-team-email',routObj]);
  }
  
  // public barChartLabels:string[] = ['Bank of America', 'City Bank', 'RBS', 'TCS', 'HCL', 'Wipro','DELL'];
  public barChartType:string = 'bar';
  public barChartLegend:boolean = true;
  
  // public barChartData:any[] = [
  //   {data: [65, 59, 80, 81, 56, 55, 40], label: 'Customer Total Revenue in Millions'},
  //   {data: [28, 48, 40, 19, 86, 27, 90], label: 'Gross Margin in Percentage'}
  // ];
 
  // events
  public chartClicked(e:any):void {
    // console.log(e);
    var companyIndex = ((this.currentPage-1)*this.pageLimit)+0;
    // console.log(this.dataBarLabels[companyIndex]);
    // this.changRoute('my-client-dashboard','client',companyIndex);
    this.changRouten('my-dashboard','Analysis',this.dataBarLabels[companyIndex],companyIndex);
  }

  public chartClicked1(e:any):void {
    // console.log(e);
    var companyIndex = ((this.currentPage-1)*this.pageLimit)+1;
    // console.log(this.dataBarLabels[companyIndex]);
    // this.sharedService.setData({'tabName':'client','companyIndex':companyIndex});
    // this.changRoute('my-client-dashboard','client',companyIndex);
    this.changRouten('my-dashboard','Analysis',this.dataBarLabels[companyIndex],companyIndex);
  }

  public chartClicked2(e:any):void {
    // console.log(e);
    var companyIndex = ((this.currentPage-1)*this.pageLimit)+2;
    // console.log(this.dataBarLabels[companyIndex]);
    // this.changRoute('my-client-dashboard','client',companyIndex);
    this.changRouten('my-dashboard','Analysis',this.dataBarLabels[companyIndex],companyIndex);
  }

  public chartClicked3(e:any):void {
    // console.log(e);
    var companyIndex = ((this.currentPage-1)*this.pageLimit)+3;
    // console.log(this.dataBarLabels[companyIndex]);
    // this.changRoute('my-client-dashboard','client',companyIndex);
    this.changRouten('my-dashboard','Analysis',this.dataBarLabels[companyIndex],companyIndex);
  }

  public chartClicked4(e:any):void {
    // console.log(e);
    var companyIndex = ((this.currentPage-1)*this.pageLimit)+4;
    // console.log(this.dataBarLabels[companyIndex]);
    // this.changRoute('my-client-dashboard','client',companyIndex);
    this.changRouten('my-dashboard','Analysis',this.dataBarLabels[companyIndex],companyIndex);
  }

  public chartClicked5(e:any):void {
    // console.log(e);
    var companyIndex = ((this.currentPage-1)*this.pageLimit)+5;
    // console.log(this.dataBarLabels[companyIndex]);
    // this.changRoute('my-client-dashboard','client',companyIndex);
    this.changRouten('my-dashboard','Analysis',this.dataBarLabels[companyIndex],companyIndex);
  }

  public chartClicked6(e:any):void {
    // console.log(e);
    var companyIndex = ((this.currentPage-1)*this.pageLimit)+6;
    // console.log(this.dataBarLabels[companyIndex]);
    // this.changRoute('my-client-dashboard','client',companyIndex);
    this.changRouten('my-dashboard','Analysis',this.dataBarLabels[companyIndex],companyIndex);
  }
 
  public chartHovered(e:any):void {
    // console.log(e);
  }
 
  public chartColors: Array<any> = [
    { // first color
      backgroundColor: 'rgba(182,230,217,1)',
      borderColor: 'rgba(182,230,217,0.8)',
      pointBackgroundColor: 'rgba(182,230,217,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(182,230,217,1)'
    },
    { // second color
      backgroundColor: 'rgba(23,174,162,1)',
      borderColor: 'rgba(23,174,162,0.8)',
      pointBackgroundColor: 'rgba(23,174,162,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(23,174,162,1)'
    }];

    public doughnutChartLabels:string[] = ['Unhappy', 'Happy', 'Neutral'];
    public doughnutChartData:number[] = [350, 450, 250];
    public doughnutChartType:string = 'doughnut';

    public donutChartOptions: any = {
      cutoutPercentage: 70,
      responsive: true,
      maintainAspectRatio: false,
      legend: {
        display: false
        },
      //   tooltips: {
      //       callbacks: {
      //         label: function(tooltipItem) {
      //                 return tooltipItem.yLabel;
      //         }
      //       }
      //   }
    }

    public donutChartData = [{
      id: 0, // number
      label: 'label name',  // string
      value: 10,  // number
      color: 'color of slice',  // string,
      iconImage: 'path of image' // string
      },{
      id: 1, // number
      label: 'label name',  // string
      value: 20,  // number
      color: 'color of slice',  // string,
      iconImage: 'path of image' // string
       },
       ,{
        id: 2, // number
        label: 'label two',  // string
        value: 30,  // number
        color: 'color of slice',  // string,
        iconImage: 'path of image' // string
         }
      ]
  
    public doughnutColors: Array<any> = [
      { // first color
        backgroundColor: ['rgba(245,118,96,0.8)', 'rgba(70,202,139,0.8)','rgba(124,123,124,0.8)'],
        borderColor: ['rgba(245,118,96,0.8)', 'rgba(70,202,139,0.8)','rgba(124,123,124,0.8)'],
        pointBackgroundColor: 'rgba(225,10,24,0.2)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(225,10,24,0.2)'
      }];
    
    
      
    

  public randomize():void {
    // Only Change 3 values
    let data = [
      Math.round(Math.random() * 100),
      59,
      80,
      (Math.random() * 100),
      56,
      (Math.random() * 100),
      40];
    let clone = JSON.parse(JSON.stringify(this.barChartData));
    clone[0].data = data;
    this.barChartData = clone;
    /**
     * (My guess), for Angular to recognize the change in the dataset
     * it has to change the dataset variable directly,
     * so one way around it, is to clone the data, change it and then
     * assign it;
     */
  }
}
