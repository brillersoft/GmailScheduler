import { Component ,OnInit,OnDestroy,AfterViewInit} from '@angular/core';
import {HttpService} from '../../service/http.service';
import { Router ,ActivatedRoute, Route} from '@angular/router';
import { EmployeeDashboard } from './employeedashboard';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { ChangeDetectorRef } from '@angular/core';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { DataService } from "../data.service";
declare var $:any;

var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL =   'templates/employee/employeedashboard.component.html' ;

@Component({
  selector: 'app-root',
  templateUrl: _templateURL,
})
export class EmployeeDashboardComponent implements OnInit,OnDestroy,AfterViewInit {

  //selectedYears: any[];
  //designationList: any[];
  private employeedashboard:Object = {};
  private empdashboard:any = [];
  private empReportToHierarchy:any = [];
  private empReportToId:any = [];
  private empdashboardDATA:any = [];
  private empdashboardItemDATA:any = [];  
  private listOfEmployee:any = [];  
  private employeeHierachy:any = [];
  private resHierachy:any = [];
  private employeeReport:any = [];
  private employeeTeam:any = [];
  private emplistofemp:any = [];
  private resPersonalReport:object = {};
  private employeePersonalReport:object = {};
  private emlAlldata:object = {};
  private employeeFilter:object = {};
  private toneOfAllMail:object ={};
  private sortData:object = {};
  private typeObj:Object = {};
  public checked:boolean = false;
  public centerConfig:Object = {};
  public searchData:Object = {};
  private searchList:any = [];
  public setFlag:boolean = true;
  public resObj:Object = {};
  public serachList:any = [];
  public serachteamtone:any = [];
  public resultList:any = [];
  public flag:boolean = true; 
  public empHir:Object = {};
  public empHirList:any = [];
  public setUpdate:boolean = false;
  public setCreate:boolean = true;
  public nameflag:boolean = true;
  public orderflag:boolean = true;
  public teamtoneflag:boolean = true;
  public setsearchbyname:boolean = true;
  public currentprogressflag:boolean = true;
  public orderTeamflag:boolean = true;
  public empHirReFlag:boolean = false;
  public backFlag:boolean = true;
  public activeindex:Object = {};
  public resAllMail:object = {};
  public temp:Object = {};
  public empDeObj:Object = {};
  public employeeResData:Object = {};
  public empId:Object = {};
  public currentprogressList:any = [];
  public temData:Object = {};
  public widthstyle:Object = {};
  public backList:any = [];
  public sharSerList:any = [];
  public dashType:Object = {};
  private dashboard:Object = {};
  public teamRelation:Object = {};
  public clientRelation:any = {};
  public message:Object;
  resultFlag:boolean = true;
    


  private toneOfSentMail:object ={};
    
  private toneOfReceiveMail:object ={};

  private empfilterdesignation:any = [];

   
  constructor(
    private httpService: HttpService,
    private router:Router,
    private sharedService:SharedService,
    private _sanitizer: DomSanitizer,
    private route: ActivatedRoute,
    private cdRef:ChangeDetectorRef)
  {}
  ngOnInit() {
    this.teamRelation  =  this.route.snapshot.params;
    this.toneOfAllMail = {};
    this.toneOfAllMail['anger'] = 0;
    this.toneOfAllMail['joy'] = 0;
    this.toneOfAllMail['sadness'] = 0;
    this.toneOfAllMail['tentative'] = 0;
    this.toneOfAllMail['analytical'] = 0;
    this.toneOfAllMail['confident'] = 0;
    this.toneOfAllMail['fear'] = 0;
    //this.teamRelation = this.dashboard['teamRelation'];
    //this.clientRelation = this.dashboard['clientRelation'];
         if(this.sharedService.getData() != undefined && this.sharedService.getData().tabName != undefined)
          {
         
          var tabName = this.sharedService.getData().tabName;
          if(tabName=='Client')
          {
              $('#emp-dasboard a[href="#client-tab"]').tab('show');
              if(this.sharedService.getData().searchCriteria != null)
              {
                  this.sortData['searchCriteria'] = this.sharedService.getData().searchCriteria;
                  this.sortData['sortType'] = this.sharedService.getData().sortType;
                  this.sortData['employeeId'] = this.sharedService.getData().employeeId;
              }
              else
              {
                  this.sortData['searchCriteria'] = null;
              }
              
          }else
           {
             //   $('#emp-dasboard a[href="#employee-tab"]').tab('show');
              if(this.sharedService.getData().searchCriteria != null)
              {
                  this.sortData['searchCriteria'] = this.sharedService.getData().searchCriteria;
                  this.sortData['sortType'] = this.sharedService.getData().sortType;
                  this.sortData['employeeId'] = this.sharedService.getData().employeeId;
              }
              else
              {
                  this.sortData['searchCriteria'] = null;
              }
           }
          }
      else if(this.sharedService.getData() != undefined && this.sharedService.getData().teamemail != undefined)
      {
             
             this.dashType['employeeId'] = this.sharedService.getData().userId;
             this.dashType['type'] = 'teamEmail';
             this.backList = this.sharedService.getData().empHir;
            // this.getEmployeeDetails(reqSharObj);
             
             
      }
      
      
      
     
  }
 
  ngAfterViewInit(){
       


       var  requestObj1 = new BaseContainer();   
     
       if(this.sortData['searchCriteria'] != null)
              {
                  requestObj1 = this.sortData;
                  this.httpService.postRequest<EmployeeDashboard>(requestObj1,'app/auth/getSortEmployee').subscribe(
                        employeedashboard=> {
                            if(employeedashboard.rsBody.result=='success')
                            { 

                                    this.resPersonalReport = Object.assign({}, employeedashboard.rsBody.msg);                                    
                                    this.employeePersonalReport = employeedashboard.rsBody.msg;
                                    //this.dashboard = employeedashboard.rsBody.msg;                                    
                                    this.temp =  this.employeePersonalReport.employeeId;
                                    
                                    this.toneOfAllMail = this.employeePersonalReport.toneOfTeamMail;
                                    console.log(this.toneOfAllMail);
                                    if(this.toneOfAllMail == undefined || this.toneOfAllMail == null){
                                        console.log("in tone all mail exception");
                                        this.toneOfAllMail = {};
                                        this.toneOfAllMail['anger'] = 0;
                                        this.toneOfAllMail['joy'] = 0;
                                        this.toneOfAllMail['sadness'] = 0;
                                        this.toneOfAllMail['analytical'] = 0;
                                        this.toneOfAllMail['fear'] = 0;
                                        this.toneOfAllMail['tentative'] = 0;
                                        this.toneOfAllMail['confident'] = 0;
                                    }
                                    this.toneOfSentMail = this.employeePersonalReport.toneOfTeamSentMail;
                                    this.toneOfReceiveMail = this.employeePersonalReport.toneOfTeamReceiveMail;
                                    // dynamic create bar
                                   var total = 0;
                                   total = this.toneOfAllMail['anger'] + this.toneOfAllMail['joy'] + this.toneOfAllMail['sadness'] + this.toneOfAllMail['fear']
                                   + this.toneOfAllMail['tentative'] + this.toneOfAllMail['confident'] + this.toneOfAllMail['analytical'];
                                   var  reqTempInfoObj = new BaseContainer();
                                   reqTempInfoObj['anger'] = (this.toneOfAllMail['anger'] /total) * 100 +'%';
                                   reqTempInfoObj['joy'] = (this.toneOfAllMail['joy'] / total) *100 +'%';
                                   reqTempInfoObj['sadness'] = (this.toneOfAllMail['sadness'] / total) * 100 +'%';
                                   reqTempInfoObj['fear'] = (this.toneOfAllMail['fear'] / total) * 100 +'%';
                                   reqTempInfoObj['tentative'] = (this.toneOfAllMail['tentative'] / total) * 100 +'%';
                                   reqTempInfoObj['confident'] = (this.toneOfAllMail['confident'] / total) * 100 +'%';
                                   reqTempInfoObj['analytical'] = (this.toneOfAllMail['analytical'] / total) * 100 +'%';
                                   this.widthstyle = reqTempInfoObj;
                                    if(this.dashType['type'] != null)
                                      {
                                        var sharObj = new BaseContainer();         
                                        var val = this.backList[this.backList.length - 1];        
                                        sharObj['employeeId'] = val;
                                        this.dashType['type'] = null;
                                        //this.backList.splice(-1,1);
                                        var len = this.backList.length;
                                        if(len == 1)
                                        {
                                            this.ngAfterViewInit();
                                        }
                                        else
                                        {
                                            this.getEmployeeDetails(sharObj);
                                        }
                                      
                                      }
                                      else
                                      {
                                        this.backList = [];
                                        this.backList.push(this.temp);
                                        this.backFlag = true;
                                    
                                        this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee;
                                             for(var i = 0; i < this.employeeHierachy.length; i++)
                                              {
                                                  this.temData = {};
                                                  this.temData = this.employeeHierachy[i].toneOfTeamMail;
                                                   var total = 0;
                                                   total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                                                   + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                                                   var  reqTempInfoObj = new BaseContainer();
                                                   reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                                                   reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                                                   reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                                                   reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                                                   reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                                                   reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                                                   reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                                                   this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                                              
                                              }
                                        this.setFlag = true;
                                        this.empHirReFlag = false;
                                        if(this.employeeHierachy.length == 0){
                                            this.resultFlag = false;
                                        }else{
                                            this.resultFlag = true;
                                        }
                                      }
                                 

                                   
                            }
                        });
                  
              }
             else if(this.sortData['searchCriteria'] == null)
             {
        this.httpService.postRequest<EmployeeDashboard>(requestObj1,'app/auth/listEmployeeImidiateReportee').subscribe(
                employeedashboard=> {
                    if(employeedashboard.rsBody.result=='success')
                    {         
                        //this.resPersonalReport = employeedashboard.rsBody.msg;
                        this.resPersonalReport = Object.assign({}, employeedashboard.rsBody.msg);               
                        //this.employeeReport = employeedashboard.rsBody.msg.listOfEmployee;                        
                        this.employeePersonalReport = employeedashboard.rsBody.msg;
                        //this.dashboard = this.employeePersonalReport;
                        this.temp =  this.employeePersonalReport.employeeId;
                        console.log(this.employeePersonalReport);
                        this.toneOfAllMail = this.employeePersonalReport.toneOfTeamMail;
                        this.toneOfSentMail = this.employeePersonalReport.toneOfTeamSentMail;
                        this.toneOfReceiveMail = this.employeePersonalReport.toneOfTeamReceiveMail;
                        console.log(this.toneOfAllMail);
                                    if(this.toneOfAllMail == undefined || this.toneOfAllMail == null){
                                        console.log("in tone all mail exception");
                                        this.toneOfAllMail = {};
                                        this.toneOfAllMail['anger'] = 0;
                                        this.toneOfAllMail['joy'] = 0;
                                        this.toneOfAllMail['sadness'] = 0;
                                        this.toneOfAllMail['analytical'] = 0;
                                        this.toneOfAllMail['fear'] = 0;
                                        this.toneOfAllMail['tentative'] = 0;
                                        this.toneOfAllMail['confident'] = 0;
                                    }
                        // dynamic create bar
                       var total = 0;
                       total = this.toneOfAllMail['anger'] + this.toneOfAllMail['joy'] + this.toneOfAllMail['sadness'] + this.toneOfAllMail['fear']
                       + this.toneOfAllMail['tentative'] + this.toneOfAllMail['confident'] + this.toneOfAllMail['analytical'];
                       var  reqTempInfoObj = new BaseContainer();
                       reqTempInfoObj['anger'] = (this.toneOfAllMail['anger'] /total) * 100 +'%';
                       reqTempInfoObj['joy'] = (this.toneOfAllMail['joy'] / total) *100 +'%';
                       reqTempInfoObj['sadness'] = (this.toneOfAllMail['sadness'] / total) * 100 +'%';
                       reqTempInfoObj['fear'] = (this.toneOfAllMail['fear'] / total) * 100 +'%';
                       reqTempInfoObj['tentative'] = (this.toneOfAllMail['tentative'] / total) * 100 +'%';
                       reqTempInfoObj['confident'] = (this.toneOfAllMail['confident'] / total) * 100 +'%';
                       reqTempInfoObj['analytical'] = (this.toneOfAllMail['analytical'] / total) * 100 +'%';
                       this.widthstyle = reqTempInfoObj;
                        if(this.dashType['type'] != null)
                          {
                            var sharObj = new BaseContainer();         
                            var val = this.backList[this.backList.length - 1];        
                            sharObj['employeeId'] = val;
                            this.dashType['type'] = null;
                            //this.backList.splice(-1,1);
                            var len = this.backList.length;
                            if(len == 1)
                            {
                                this.ngAfterViewInit();
                            }
                            else
                            {
                                this.getEmployeeDetails(sharObj);
                            }
                          
                          }
                          else
                          {
                            this.backList = [];
                            this.backList.push(this.temp);
                            this.backFlag = true;
                        
                            this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee;
                                 for(var i = 0; i < this.employeeHierachy.length; i++)
                                  {
                                      this.temData = {};
                                      this.temData = this.employeeHierachy[i].toneOfTeamMail;
                                       var total = 0;
                                       total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                                       + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                                       var  reqTempInfoObj = new BaseContainer();
                                       reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                                       reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                                       reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                                       reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                                       reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                                       reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                                       reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                                       this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                                  
                                  }
                            this.setFlag = true;
                            this.empHirReFlag = false;
                          }
                          if(this.employeeHierachy.length == 0){
                            this.resultFlag = false;
                        }else{
                            this.resultFlag = true;
                        }
                        
                        //this.getClickEmployeeData(0);
                    }
                });
            }else if(this.toneOfAllMail == undefined){
                this.toneOfAllMail = {}
            }
            
            
        


        this.cdRef.detectChanges();
    }  
  public userId:any ='';
  getClickEmployeeData(index){
      
      
      this.activeindex = index;
      this.employeeTeam=[];
      var count = 0;
      var  requestObj = new BaseContainer();
      //requestObj['employeeId'] = this.employeeReport[index]['employeeId'];
      //this.empId = this.employeeReport[index]['employeeId'];
      //this.userId = this.employeeReport[index]['employeeId'];
      requestObj['pageNumber'] =count;
      this.temp = requestObj;
      
       if(this.sortData['searchCriteria'] != null)
      {
          requestObj['searchCriteria'] = this.sortData['searchCriteria'];
          requestObj['sortType'] = this.sortData['sortType'];
          this.httpService.postRequest<EmployeeDashboard>(requestObj,'app/auth/getSortEmployee').subscribe(
                employeedashboard=> {
                    if(employeedashboard.rsBody.result=='success')
                    {    
                         
                          this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee;
                             for(var i = 0; i < this.employeeHierachy.length; i++)
                              {
                                  this.temData = {};
                                  this.temData = this.employeeHierachy[i].toneOfTeamMail;
                                   var total = 0;
                                   total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                                   + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                                   var  reqTempInfoObj = new BaseContainer();
                                   reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                                   reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                                   reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                                   reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                                   reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                                   reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                                   reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                                   this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                              
                              } 

                              if(this.employeeHierachy.length == 0){
                                  this.resultFlag = false;
                              }else{
                                  this.resultFlag = true;
                              }
                           
                    }
                });
          
      }else{
      this.httpService.postRequest<EmployeeDashboard>(requestObj,'app/auth/listEmployeeHierachy').subscribe(
          employeedashboard=> {
              if(employeedashboard.rsBody.result=='success')
              {                                  
                  this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee;
                  for(var i = 0; i < this.employeeHierachy.length; i++)
                  {
                      this.temData = {};
                      this.temData = this.employeeHierachy[i].toneOfTeamMail;
                       var total = 0;
                       total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                       + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                       var  reqTempInfoObj = new BaseContainer();
                       reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                       reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                       reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                       reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                       reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                       reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                       reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                       this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                  
                  }  
                  if(this.employeeHierachy.length == 0){
                    this.resultFlag = false;
                }else{
                    this.resultFlag = true;
                }
             
        
              }
          });  
           
           }
      this.cdRef.detectChanges();
      
  }
    
  // search on base view data
  getPersonalId(index){
      this.employeeTeam=[];
      var index = 0;
      var count = 0;
      var  requestObj = new BaseContainer();
      requestObj['employeeId'] = this.employeePersonalReport.employeeId;
      this.userId = this.employeePersonalReport.employeeId;
      requestObj['pageNumber'] =count;
      this.temp = requestObj;
      this.httpService.postRequest<EmployeeDashboard>(requestObj,'app/auth/listEmployeeHierachy').subscribe(
          employeedashboard=> {
              if(employeedashboard.rsBody.result=='success')
              {
                  
                  this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee;
                  for(var i = 0; i < this.employeeHierachy.length; i++)
                  {
                      this.temData = {};
                      this.temData = this.employeeHierachy[i].toneOfTeamMail;
                       var total = 0;
                       total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                       + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                       var  reqTempInfoObj = new BaseContainer();
                       reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                       reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                       reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                       reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                       reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                       reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                       reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                       this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                  
                  } 
                  
                this.emplistofemp = this.employeeHierachy['listOfEmployee'];  
                  
              }
          });       
      this.cdRef.detectChanges();
      
  }
  getClickSent(sent){  
       
           $('.filteractivesent').css('background-color', '#F5F5F5');
           $('.filteractiveall').css('background-color', '#e9e9e9');
           $('.filteractivereceive').css('background-color', '#e9e9e9');
          
          this.employeePersonalReport['noOfMail'] = this.employeePersonalReport.noOfSentMail;
          this.toneOfAllMail['anger'] = this.toneOfSentMail.anger;
          this.toneOfAllMail['joy'] = this.toneOfSentMail.joy;
          this.toneOfAllMail['sadness'] = this.toneOfSentMail.sadness;
          this.toneOfAllMail['tentative'] = this.toneOfSentMail.tentative;
          this.toneOfAllMail['analytical'] = this.toneOfSentMail.analytical;
          this.toneOfAllMail['confident'] = this.toneOfSentMail.confident;
          this.toneOfAllMail['fear'] = this.toneOfSentMail.fear;      
         for(var i = 0; i<this.employeeHierachy.length; i++){
//         this.employeeHierachy['anger'] = this.employeeHierachy.toneOfTeamSentMail.anger;
           this.employeeHierachy[i]['noOfMail'] = this.employeeHierachy[i].noOfSentMail;
         }
  }

  changRouteTouch(name){
    this.router.navigate([name]);
  }
  
 getClickAll(index)
{   
         
           $('.filteractivesent').css('background-color', '#e9e9e9');
           $('.filteractiveall').css('background-color', '#f5f5f5');
           $('.filteractivereceive').css('background-color', '#e9e9e9');
         var count = 0;
         
         var  requestAllObj = new BaseContainer();
         requestAllObj['employeeId'] = this.temp;
         requestAllObj['pageNumber'] = count;
         this.httpService.postRequest<EmployeeDashboard>(requestAllObj,'app/auth/listEmployeeHierachy').subscribe(
          employeedashboard=> {
              if(employeedashboard.rsBody.result=='success')
              {                    
                 
                  this.toneOfAllMail = this.resPersonalReport.toneOfTeamMail;
                  this.employeePersonalReport['noOfMail'] = this.resPersonalReport.noOfMail; 
                   this.employeeHierachy = [];                      
                  this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee;
                  for(var i = 0; i < this.employeeHierachy.length; i++)
                  {
                      this.temData = {};
                      this.temData = this.employeeHierachy[i].toneOfTeamMail;
                       var total = 0;
                       total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                       + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                       var  reqTempInfoObj = new BaseContainer();
                       reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                       reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                       reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                       reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                       reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                       reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                       reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                       this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                  
                  }         
              }
          });   

}
  getClickReceive(received){    
           $('.filteractivesent').css('background-color', '#e9e9e9');
           $('.filteractiveall').css('background-color', '#e9e9e9');
           $('.filteractivereceive').css('background-color', '#f5f5f5');    
      this.employeePersonalReport['noOfMail'] = this.employeePersonalReport.noOfReceiveMail;
      this.toneOfAllMail['anger'] = this.toneOfReceiveMail.anger;
      this.toneOfAllMail['joy'] = this.toneOfReceiveMail.joy;
      this.toneOfAllMail['sadness'] = this.toneOfReceiveMail.sadness;
      this.toneOfAllMail['tentative'] = this.toneOfReceiveMail.tentative;
         
      this.toneOfAllMail['analytical'] = this.toneOfReceiveMail.analytical;
      this.toneOfAllMail['confident'] = this.toneOfReceiveMail.confident;
      this.toneOfAllMail['fear'] = this.toneOfReceiveMail.fear;   
         for(var i = 0; i<this.employeeHierachy.length; i++){
         //this.employeeHierachy['anger'] = this.employeeHierachy.toneOfTeamSentMail.anger;
       this.employeeHierachy[i]['noOfMail'] = this.employeeHierachy[i].noOfReceiveMail;
       }
}
         
  getClickFilter(){     
  
         this.setCreate = true;
       
         var  requestObj = new BaseContainer();       
         
         requestObj['searchCriteria'] = this.empfilterdesignation;
         //this.empfilterdesignation = [];
         //var len = this.empfilterdesignation.length;
         //this.empfilterdesignation.splice(len, len);
        
         this.httpService.postRequest<EmployeeDashboard>(requestObj,'app/auth/searchByEmployeedesignationHierachy').subscribe(
                 employeedashboard=> {
                     if(employeedashboard.rsBody.result=='success')
                     {
                         
                         this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee; 
                         for(var i = 0; i < this.employeeHierachy.length; i++)
                              {
                                  this.temData = {};
                                  this.temData = this.employeeHierachy[i].toneOfTeamMail;
                                   var total = 0;
                                   total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                                   + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                                   var  reqTempInfoObj = new BaseContainer();
                                   reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                                   reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                                   reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                                   reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                                   reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                                   reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                                   reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                                   this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                              
                              }
                        
                        this.empHirReFlag = false;                       
                        
                        
                     }
                 });      
         this.cdRef.detectChanges();
   }
//         designation_filter(value)
//         {
//             
//         }
         /**
         * created by Sonu Baghel
         * multi Select checkboxes for area_of_specialisation
         */
         designation_filter(index, targetIndex)
        {
           
         if(this.empfilterdesignation.indexOf(index)==-1)
         {
           this.designationList[targetIndex].checked = true;
           this.empfilterdesignation.push(index);
           
         }else{
             this.designationList[targetIndex].checked = false;
             for(var i=0; i < this.empfilterdesignation.length; i++)
            {
                
                var data = this.empfilterdesignation[i];
                
                if(index == data)
                {
                       this.empfilterdesignation.splice(i,1);               
                      
                }
                
             }
         //this.empfilterdesignation.splice(targetIndex,1);
         }
         
        }
    
    
    
    searchbyalfaexeemp(obj,targetIndex)
    {
        if(this.serachList.indexOf(obj)==-1)
         {
           this.ascOrDescList[targetIndex].checked = true;
           this.serachList.push(obj);
           
         }else{
            
            this.ascOrDescList[targetIndex].checked = false;
            for(var i=0; i < this.serachList.length; i++)
            {
                
                var data = this.serachList[i];
                
                if(obj == data)
                {
                       this.serachList.splice(i,1);               
                      
                }
                
             }
         
         }
        this.searchcreteria(this.serachList);  
    }
    
    searchbyateamtone(obj, targetIndex)
    {
        
         if(this.serachteamtone.indexOf(obj)==-1)
         {
           this.teamScoreList[targetIndex].checked = true;
           this.serachteamtone.push(obj);
           
         }else{
            
            this.teamScoreList[targetIndex].checked = false;
            for(var i=0; i < this.serachteamtone.length; i++)
            {
                
                var data = this.serachteamtone[i];
                
                if(obj == data)
                {
                       this.serachteamtone.splice(i,1);               
                      
                }
                
             }
         
         }

        //this.searchcreteriateamtone(this.serachteamtone);  
    }
    
    searchcreteria(obj)
    {
        var  resultList = new BaseContainer();
        resultList['searchCriteria'] = obj;
        resultList['employeeId'] = this.temp;
       
        //var requestObj['searchCriteria'] = this.serachList;
        this.httpService.postRequest<EmployeeDashboard>(resultList,'app/auth/listEmployeeHierachy').subscribe(
             employeedashboard=> {
                 if(employeedashboard.rsBody.result=='success')
                 {
                     
                     this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee;
                     for(var i = 0; i < this.employeeHierachy.length; i++)
                      {
                          this.temData = {};
                          this.temData = this.employeeHierachy[i].toneOfTeamMail;
                           var total = 0;
                           total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                           + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                           var  reqTempInfoObj = new BaseContainer();
                           reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                           reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                           reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                           reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                           reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                           reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                           reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                           this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                      
                      }  
                    
                    
                    
                 }
             }); 
          
     }
    
    searchcreteriateamtone(obj)
    {
        var  resultList = new BaseContainer();
        resultList['searchCriteria'] = obj;
        resultList['employeeId'] = this.temp;
       
        //var requestObj['searchCriteria'] = this.serachList;
        this.httpService.postRequest<EmployeeDashboard>(resultList,'app/auth/getSortEmployee').subscribe(
             employeedashboard=> {
                 if(employeedashboard.rsBody.result=='success')
                 {
                     
                     this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee;
                     for(var i = 0; i < this.employeeHierachy.length; i++)
                      {
                          this.temData = {};
                          this.temData = this.employeeHierachy[i].toneOfTeamMail;
                           var total = 0;
                           total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                           + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                           var  reqTempInfoObj = new BaseContainer();
                           reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                           reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                           reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                           reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                           reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                           reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                           reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                           this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                      
                      }  
                    
                    
                    
                 }
             }); 
          
     }
    
        // clear check box checked
    
    // clear check box checked 
        
  changRoute(name,tabName)
  {
    this.router.navigate([name,this.teamRelation]);
  }
    
  changRouteteam(name,tabName,obj)
  {
    var ids = this.employeeHierachy[obj]['employeeId'];     
    this.router.navigate([name,ids]);
  }
    
  changRouteteamOnSearch(name,tabName,obj)
  {
          var ids = this.searchList[obj]['employeeId'];   
          this.router.navigate([name,ids]);
  }

  
    
    
  changRouteemployee(name,tabName)
  {
         
    var userId = this.employeePersonalReport.employeeId;
    //this.router.navigate([name,empId]);
    this.router.navigate([name,userId]);
  }
 
  changRouteHir(name,tabName)
  {
         
          //var empId = this.employeeReport[obj].employeeId;
          //this.sharedService.setData({'tabName':tabName,'userId':this.temp});
          this.router.navigate([name,this.temp]);
  }
    
  changRouteHirSearch(name,tabName)
  {
         
          var empId = this.searchData['employeeId'];
          //this.sharedService.setData({'tabName':tabName,'userId':empId});
          this.router.navigate([name,empId]);
  }
    
  teamtonescore(obj,index)
  {
  
          var empId = this.employeeHierachy[index].employeeId;
          var  routObj = new BaseContainer();
          routObj['userId'] = empId; 
          routObj['type'] = obj;
          this.router.navigate(['my-team-email',routObj]);
  
  }
  teamtonescoreemail(index)
  {
  
          var empId = this.employeeHierachy[index].employeeId;
          var  routObj = new BaseContainer();
          routObj['userId'] = empId; 
          this.router.navigate(['my-team-email',routObj]);
  
  }
    
  teamtonescoresearch(obj,index)
  {
  
          var empId = this.searchList[index].employeeId;
          var  routObj = new BaseContainer();
          routObj['userId'] = empId; 
          routObj['type'] = obj;
          this.router.navigate(['my-team-email']);
  
  }
  teamtonescoresearchemail(index)
  {
  
          var empId = this.searchList[index].employeeId;
          var  routObj = new BaseContainer();
          routObj['userId'] = empId; 
          this.router.navigate(['my-team-email']);
  
  }
    
  teamtoneemployee(obj)
  {
  
          var empId = this.employeePersonalReport.employeeId;
          //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
          var  routObj = new BaseContainer();
          routObj['userId'] = empId; 
          this.router.navigate(['my-team-email',routObj]);
  
  }

  teamtoneemployeeAdverse(obj){

    var empId = this.employeePersonalReport.employeeId;
    var adverseFilter = "yes";
    //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
    var  routObj = new BaseContainer();
    routObj['userId'] = empId; 
    routObj['adverseFilter'] = adverseFilter;
    this.router.navigate(['my-team-email',routObj]);
  }

  teamtoneemployeeEscalation(obj){

    var empId = this.employeePersonalReport.employeeId;
    var escalationFilter = "yes";
    //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
    var  routObj = new BaseContainer();
    routObj['userId'] = empId; 
    routObj['escalationFilter'] = escalationFilter;
    this.router.navigate(['my-team-email',routObj]);
  }

  teamtoneemployeesearch(obj){

    var empId = this.employeePersonalReport.employeeId;
    //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
    var  routObj = new BaseContainer();
    routObj['userId'] = empId; 
    routObj['type'] = obj;
    this.router.navigate(['my-team-email',routObj]);

  }
  ngOnDestroy()
  {}
    
    opendropdown()
    {
        if(this.setCreate == true )
        {
            
            this.setCreate = false;
        }
        else
        {
            this.setCreate = true;
        }
       
        
    }
     namedropdown()
    {
        if(this.nameflag == true )
        {
            
            this.nameflag = false;
        }
        else
        {
            this.nameflag = true;
        }
       
        
    }
     teamtonedropdown()
    {
        if(this.teamtoneflag == true )
        {
            
            this.teamtoneflag = false;
        }
        else
        {
            this.teamtoneflag = true;
        }
       
        
    }
    currentprogressicons()
    {
        if(this.currentprogressflag == true )
        {
            
            this.currentprogressflag = false;
        }
        else
        {
            this.currentprogressflag = true;
        }
       
        
    }
    opendropdownname()
    {
        if(this.setsearchbyname == true )
        {
            
            this.setsearchbyname = false;
        }
        else
        {
            this.setsearchbyname = true;
        }
       
        
    }
    autocompleListFormatter = (data: any) => {
          let html = `<span style='color:black'>${data.employeeName}</span>`;
          return this._sanitizer.bypassSecurityTrustHtml(html);
      }
    
    
   onChange(value:number){
       
        this.centerConfig['employeeId'] = value['employeeId'];
       
        this.httpService.postRequest<EmployeeDashboard>(this.centerConfig,'app/auth/getEmpSearchResult').subscribe(
                 employeedashboard=> {
                     if(employeedashboard.rsBody.result=='success')
                     {
                        this.searchData = employeedashboard.rsBody.msg; 
                         this.searchList = employeedashboard.rsBody.msg.listOfEmployee;
                         for(var i = 0; i < this.searchList.length; i++)
                          {
                              this.temData = {};
                              this.temData = this.searchList[i].toneOfTeamMail;
                               var total = 0;
                               total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                               + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                               var  reqTempInfoObj = new BaseContainer();
                               reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                               reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                               reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                               reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                               reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                               reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                               reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                               this.searchList[i]['widthstyle'] = reqTempInfoObj;
                          
                          }
                         this.backFlag = false;
                         this.setFlag = false;
                        //this.getClickEmployeeData(0); 
                        
                        
                     }
                 });     
       
    
       
    }
    
    
    // serach by employeename
          autocompleSearchByName = (data: any) => {
          let html = `<span style='color:black'>${data.employeeName}</span>`;
          return this._sanitizer.bypassSecurityTrustHtml(html);
      }
    
    onChangeEmployee(value:number)
    {
        
       this.empDeObj = value;
    
    }
    
    searchByEmployeeNameSubmit()
    {
         this.setsearchbyname = true;
         this.backFlag = false;
         var  requestEmpObj = new BaseContainer();   
         requestEmpObj['employeeId']= this.employeePersonalReport.employeeId;
         requestEmpObj['employeeName']= this.typeObj['employeeName'];
         this.httpService.postRequest<EmployeeDashboard>(requestEmpObj,'app/auth/getEmpSearchResult').subscribe(
                 employeedashboard=> {
                     if(employeedashboard.rsBody.result=='success')
                     {
                        this.searchData = employeedashboard.rsBody.msg; 
                         this.searchList = employeedashboard.rsBody.msg.listOfEmployee;
                         for(var i = 0; i < this.searchList.length; i++)
                          {
                              this.temData = {};
                              this.temData = this.searchList[i].toneOfTeamMail;
                               var total = 0;
                               total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                               + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                               var  reqTempInfoObj = new BaseContainer();
                               reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                               reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                               reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                               reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                               reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                               reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                               reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                               this.searchList[i]['widthstyle'] = reqTempInfoObj;
                          
                          } 
                        this.setFlag = false;
                        
                         this.resObj = {};
                        //this.getClickEmployeeData(0); 
                        
                        
                     }
                 });  
        
    }
    
    // search by designation
       searchDesignation(){
       
        //this.centerConfig['designation'] = resObj['designation'];
           //var test = this.resObj;
       
        this.httpService.postRequest<EmployeeDashboard>(this.resObj,'app/auth/getEmpDesigResult').subscribe(
                 employeedashboard=> {
                     if(employeedashboard.rsBody.result=='success')
                     {
                        this.searchData = employeedashboard.rsBody.msg; 
                         this.searchList = employeedashboard.rsBody.msg.listOfEmployee;
                         for(var i = 0; i < this.searchList.length; i++)
                          {
                              this.temData = {};
                              this.temData = this.searchList[i].toneOfTeamMail;
                               var total = 0;
                               total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                               + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                               var  reqTempInfoObj = new BaseContainer();
                               reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                               reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                               reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                               reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                               reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                               reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                               reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                               this.searchList[i]['widthstyle'] = reqTempInfoObj;
                          
                          } 
                        this.setFlag = false;
                         this.resObj = {};
                        //this.getClickEmployeeData(0); 
                        
                        
                     }
                 });     
       
    
       
    }
    
    
    getemployeelistslider(obj){
        var  resob = new BaseContainer();
        resob['employeeId'] = this.empReportToId[obj]; 
    //  this.empReportToHierarchy = this.employeeHierachy[obj]['employeeName'];
        this.backList.push(resob['employeeId']);          
        this.getEmployeeDetails(resob);
    }

        // search by designation
       getemployeelist(obj)
       {
       
         //this.backList.push(this.temp);
         var  resob = new BaseContainer();
         resob['employeeId'] = this.employeeHierachy[obj]['employeeId']; 
         this.backList.push(resob['employeeId']);          
         this.getEmployeeDetails(resob);
         
       }
    
    // get employee details
    getEmployeeDetails(resob)
    {
        this.httpService.postRequest<EmployeeDashboard>(resob,'app/auth/listEmployeeHierachy').subscribe(
           employeedashboard=> {
             if(employeedashboard.rsBody.result=='success')
             {
                 //this.empHir = employeedashboard.rsBody.msg;
                 this.employeeResData = employeedashboard.rsBody.msg;
                  this.temp =  this.employeeResData.employeeId;
                 this.listOfTeamEmployee = employeedashboard.rsBody.msg.listOfEmployee;
                 this.empReportToHierarchy = employeedashboard.rsBody.msg.empReportToHierachy;
                 this.empReportToId = employeedashboard.rsBody.msg.empReportToId;
                 
                 for(var i = 0; i < this.listOfTeamEmployee.length; i++)
                  {
                      this.temData = {};
                      this.temData = this.listOfTeamEmployee[i].toneOfTeamMail;
                       var total = 0;
                       total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                       + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                       var  reqTempInfoObj = new BaseContainer();
                       reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                       reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                       reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                       reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                       reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                       reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                       reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                       this.listOfTeamEmployee[i]['widthstyle'] = reqTempInfoObj;
                  
                  } 
                 //this.employeeHierachy[obj]['empHirList'] = this.listOfTeamEmployee; 
                 this.employeeHierachy = this.listOfTeamEmployee; 
                 this.empHirReFlag = true;
                
                
             }
          }); 
    }
    
    getemployeeSearchlist(obj)
       {
         //var temp;
         if(this.temp == obj)
         {
             $(".sha-col"+obj).removeClass('bg-collapse');
             this.temp = null;
         }
        else
         {
            $(".sha-col"+obj).addClass('bg-collapse');
             this.temp = obj;
         }
         
         
         var  resob = new BaseContainer();
         resob['employeeId'] = this.searchList[obj]['employeeId'];
           
         //var empId = this.employeeHierachy[obj]['employeeId'];
         this.httpService.postRequest<EmployeeDashboard>(resob,'app/auth/listEmployeeHierachy').subscribe(
           employeedashboard=> {
             if(employeedashboard.rsBody.result=='success')
             {
                 //this.empHir = employeedashboard.rsBody.msg; 
                 //this.searchList[obj]['empHirList'] = employeedashboard.rsBody.msg.listOfEmployee;
                 this.listOfTeamEmployee = employeedashboard.rsBody.msg.listOfEmployee;
                 for(var i = 0; i < this.listOfTeamEmployee.length; i++)
                  {
                      this.temData = {};
                      this.temData = this.listOfTeamEmployee[i].toneOfTeamMail;
                       var total = 0;
                       total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                       + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                       var  reqTempInfoObj = new BaseContainer();
                       reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                       reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                       reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                       reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                       reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                       reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                       reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                       this.listOfTeamEmployee[i]['widthstyle'] = reqTempInfoObj;
                  
                  } 
                 this.searchList[obj]['empHirList'] = this.listOfTeamEmployee; 
                 //this.empHirList = employeedashboard.rsBody.msg.listOfEmployee;
                
                
             }
          }); 
       }
    
    
    
    
   designationList = [
    { id: 1, name: 'SVP'},
    { id: 2, name: 'AVP'},
    { id: 3, name: 'Managers'},
    { id: 4, name: 'Asstt. Managers'},
    { id: 5, name: 'Team Lead'},
    { id: 6, name: 'Employees'}
  ];
    
    ascOrDescList = [
        { id: 1, name: 'Alphabetically', option:'alphabetically'},
        { id: 2, name: 'Experience', option:'experience'},
        { id: 3, name: 'No. of Employee', option:'teamSize'},
        ];
    
    teamScoreList = [    
        { id: 1, name: 'Anger', option:'anger'},
        { id: 2, name: 'Joy', option:'joy'},
        { id: 3, name: 'Sadness', option:'sadness'},
        { id: 4, name: 'Fear', option:'fear'},
        { id: 5, name: 'Tentative', option:'tentative'},
        { id: 6, name: 'Confident', option:'confident'},
        { id: 7, name: 'Analytical', option:'analytical'}        
        ];
    currentProgressList = [
         { id: 1, name: 'Percentage', option:'percentage'},
         { id: 2, name: 'Improvement', option:'improvement'},
         { id: 3, name: 'Tone based', option:'tonebased'},
        ];
    
    
  resetAll() {
      //this.form.controls.designationList.controls.map(x => x.patchValue(false))
      this.empfilterdesignation = [];
      this.designationList.forEach((item) => {
        item.checked = false;
      })
      
      this.ngAfterViewInit();
   }
    
    resetNone()
    {
        
      this.serachList = [];
      this.ascOrDescList.forEach((item) => {
        item.checked = false;
      })
      
      this.ngAfterViewInit();
    
    }
    
    resetScoreNone()
    {
        
      this.serachteamtone = [];
      this.teamScoreList.forEach((item) => {
        item.checked = false;
      })
      
      this.ngAfterViewInit();
    
    }
    
    resetCurrentProgress()
    {
        
      this.currentprogressList = [];
      this.currentProgressList.forEach((item) => {
        item.checked = false;
      })
      
      this.ngAfterViewInit();
    
    }
    
    searchbycurrentprogress()
    {
        if(this.currentprogressList.indexOf(obj)==-1)
         {
           this.currentProgressList[targetIndex].checked = true;
           this.currentprogressList.push(obj);
           
         }else{
            
            this.currentProgressList[targetIndex].checked = false;
            for(var i=0; i < this.currentprogressList.length; i++)
            {
                
                var data = this.currentprogressList[i];
                
                if(obj == data)
                {
                       this.currentprogressList.splice(i,1);               
                      
                }
                
             }
         
         }

        this.searchcreteriateamtone(this.currentprogressList);  
    
    }
    
    asendignOrderSubmit()
    {
        this.orderflag = false;
        //var order = 'ASC';       
        this.ascAndDscSubmit('ASC', this.serachteamtone);
       

    }
    
    dsendignOrderSubmit()
    {
        this.orderflag = true;
        this.ascAndDscSubmit('DSC', this.serachteamtone);
    }
    
    asendignOrderTeamSubmit()
    {
        this.orderTeamflag = false;       
        this.ascAndDscSubmit('ASC', ['teamMail']);
       

    }
    
    dsendignOrderTeamSubmit()
    {
        this.orderTeamflag = true;
        this.ascAndDscSubmit('DSC', ['teamMail']);
    }
    
    // ASC And DSC Order List
    ascAndDscSubmit(order, searchcriteria)
    {
        var  resultList = new BaseContainer();
        resultList['searchCriteria'] = searchcriteria;
        resultList['employeeId'] = this.temp;
        resultList['sortType'] = order;
        //var requestObj['searchCriteria'] = this.serachList;
        this.httpService.postRequest<EmployeeDashboard>(resultList,'app/auth/getSortEmployee').subscribe(
             employeedashboard=> {
                 if(employeedashboard.rsBody.result=='success')
                 {
                     
                     this.employeeHierachy = employeedashboard.rsBody.msg.listOfEmployee;
                     for(var i = 0; i < this.employeeHierachy.length; i++)
                      {
                          this.temData = {};
                          this.temData = this.employeeHierachy[i].toneOfTeamMail;
                           var total = 0;
                           total = this.temData['anger'] + this.temData['joy'] + this.temData['sadness'] + this.temData['fear']
                           + this.temData['tentative'] + this.temData['confident'] + this.temData['analytical'];
                           var  reqTempInfoObj = new BaseContainer();
                           reqTempInfoObj['anger'] = (this.temData['anger'] /total) * 100 +'%';
                           reqTempInfoObj['joy'] = (this.temData['joy'] / total) *100 +'%';
                           reqTempInfoObj['sadness'] = (this.temData['sadness'] / total) * 100 +'%';
                           reqTempInfoObj['fear'] = (this.temData['fear'] / total) * 100 +'%';
                           reqTempInfoObj['tentative'] = (this.temData['tentative'] / total) * 100 +'%';
                           reqTempInfoObj['confident'] = (this.temData['confident'] / total) * 100 +'%';
                           reqTempInfoObj['analytical'] = (this.temData['analytical'] / total) * 100 +'%';
                           this.employeeHierachy[i]['widthstyle'] = reqTempInfoObj;
                      
                      }  
                    
                 }
             }); 
    
    }
    
    
    // 
    downloadsExcelSubmit()
    {
        //this.orderflag = true;
    }
    
    
    
    // back button
    backsubmit()
    {
        var backObj = new BaseContainer();        
        this.backList.splice(-1,1);
        var val = this.backList[this.backList.length - 1];
        var len = this.backList.length;
        backObj['employeeId'] = val;        
        if(len == 1)
        {
            this.ngAfterViewInit();
        }
        else
        {
            this.getEmployeeDetails(backObj);
        }
    }
    
    // reload click on back
    backReloadsubmit()
    {
     this.setFlag = true;        
     this.ngAfterViewInit();
    }
    
    // export to excel
    excelExportSubmit()
    {
        var resObj = new BaseContainer();
        resObj['employeeId'] = this.temp;
        this.httpService.postRequest<EmployeeDashboard>(resObj,'app/auth/listEmployeeToExcel').subscribe(
             employeedashboard=> {
                 if(employeedashboard.rsBody.result=='success')
                 {
                    var exeFile = employeedashboard.rsBody.msg;
                    $('#ExportExcelData').attr('href', exeFile);
                    $('#ExportExcelData')[0].click();
                 }
         }); 
    }
    
}
