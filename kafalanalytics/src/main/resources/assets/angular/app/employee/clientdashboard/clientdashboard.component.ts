import { Component ,OnInit,OnDestroy,AfterViewInit,Input,Output,EventEmitter} from '@angular/core';
import {HttpService} from '../../service/http.service';
import { Router ,ActivatedRoute,Route} from '@angular/router';

import { ClientDashboard } from './clientdashboard';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { ChangeDetectorRef } from '@angular/core';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { EmployeeDashboard } from '../employeedashboard/employeedashboard';
import {PaginationInstance} from 'ngx-pagination';
declare var $:any;

var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL =   'templates/employee/clientdashboard.component.html' ;

@Component({
  selector: 'app-root',
  templateUrl: _templateURL
})
export class ClientDashboardComponent implements OnInit,OnDestroy,AfterViewInit {

  private clientdashboard:Object = {};
  private empdashboard:any = [];
  private clientdashboardDATA:object = {};
  private clientScore:object = {};
 private clienttoneScore:any = [];
  private listOfEmployee:any = [];  
  private employeeReport:object = {};
  private clientInfo:any = [];
  private clientInfoMain:any = [];
private employeeTeam:any = [];
public sortData:Object  = {};
public searchData:object = {};
public searchList:any = [];
public setFlag:boolean = true; 
public backFlag:boolean = true;
public resObj:object = {};
public typeObj:object = {};
private resPersonalReport:Object = {};
public temp:Object = {};
public tepData:Object = {};
public centerConfig:Object = {};
public companyId:Object = {};
public orgObj:Object = {};
public setObj:Object = {};
public setdesObj:Object = {};
public setOrgObj:Object = {};
public teamtoneflag:boolean = true;
public nameflag:boolean = true;
public serachteamtone:any = [];
public serachList:any = [];
public companyIndex:number = 0;
public activeCompanyIndex:number = 0;
public executiveFlag:boolean = true;
    public setsearchbyname:boolean = true;
    public setsearchbydesignation:boolean = true;
    public orderTeamflag:boolean = true;
    public orderflag:boolean = true;
    public widthstyle:Object = {};
    private dashboard:Object = {};
    public teamRelation:any = {};
    public clientRelation:any = {};
    public p:number = 1;
    public startPage:number = 1;
    public lastPage:number = 1;
    public itemsPerPage:number = 10;
    resultFlag:boolean = true;
    @Input() id: string;
    @Input() page;
    @Input() maxSize: number;

    @Output() pageChange: EventEmitter<number> = new EventEmitter<number>();
    
  constructor(
    private httpService: HttpService,
    private router:Router,
    private sharedService:SharedService,
    private _sanitizer: DomSanitizer,
    private route: ActivatedRoute,
    private cdRef:ChangeDetectorRef
    )
  {
    this.clientInfo = [];

  }
  ngOnInit() {
    this.teamRelation  =  this.route.snapshot.params;
      if(this.sharedService.getData() != undefined && this.sharedService.getData().tabName != undefined)
          {
          this.companyIndex = this.sharedService.getData().companyIndex;
        //   console.log(this.sharedService.getData().companyIndex);
          if(this.companyIndex == undefined){
              this.companyIndex = 0;
          }
          var tabName = this.sharedService.getData().tabName;
          if(tabName=='Client')
          {
              $('#emp-dasboard a[href="#client-tab"]').tab('show');
              if(this.sharedService.getData().searchCriteria != null)
              {
                  this.sortData['searchCriteria'] = this.sharedService.getData().searchCriteria;
                  this.sortData['sortType'] = this.sharedService.getData().sortType;
              }
              else
              {
                  this.sortData['searchCriteria'] = null;
              }
              
          }else
           {
             // $('#emp-dasboard a[href="#employee-tab"]').tab('show');
              if(this.sharedService.getData().searchCriteria != null)
              {
                  this.sortData['searchCriteria'] = this.sharedService.getData().searchCriteria;
                  this.sortData['sortType'] = this.sharedService.getData().sortType;
              }
              else
              {
                  this.sortData['searchCriteria'] = null;
              }
           }
          } 
      
      
     
  }

  ngAfterViewInit(){

        var  requestObj = new BaseContainer();      
        this.httpService.postRequest<ClientDashboard>(requestObj,'app/auth/listCompany').subscribe(
                clientdashboard=> {
                if(clientdashboard.rsBody.result=='success')
                {
                    this.empdashboard = clientdashboard.rsBody.msg;
                    this.dashboard = clientdashboard.rsBody.msg;
                    this.backFlag = true;
                     this.getClickedData(this.companyIndex);
                }
            });
       
       
        
    }
    //profile Details
  public userId:any ='';
  getClickedData(index){
      this.employeeTeam=[];
      this.clientScore ={};
      this.clientInfo =[];
      this.activeCompanyIndex = index;
        var  requestObj = new BaseContainer();
        requestObj['companyId'] = this.empdashboard[index]['id'];
         this.companyId = requestObj['id'];
         this.temp = requestObj;
      if(this.sortData['searchCriteria'] != null)
      {
          requestObj['searchCriteria'] = this.sortData['searchCriteria'];
          requestObj['sortType'] = this.sortData['sortType'];
          this.httpService.postRequest<ClientDashboard>(requestObj,'app/auth/listSortedClient').subscribe(
                clientdashboard=> {
                if(clientdashboard.rsBody.result=='success')
                {
                    console.log("first");
                    this.clientdashboardDATA = {};
                    this.resPersonalReport = Object.assign({}, clientdashboard.rsBody.msg); 
                    this.clientdashboardDATA = clientdashboard.rsBody.msg;
                    if(this.clientdashboardDATA.listOfEmployee.length!=0 )
                        {
                        this.clientScore = this.clientdashboardDATA.toneOfClientMail;
                        // bars 
                        var total = 0;
                        total = this.clientScore['anger'] + this.clientScore['joy'] + this.clientScore['sadness'] + this.clientScore['fear']
                        + this.clientScore['tentative'] + this.clientScore['confident'] + this.clientScore['analytical'];
                           var  reqTempObj = new BaseContainer();
                           reqTempObj['anger'] = (this.clientScore['anger'] /total) * 100 +'%';
                           reqTempObj['joy'] = (this.clientScore['joy'] / total) *100 +'%';
                           reqTempObj['sadness'] = (this.clientScore['sadness'] / total) * 100 +'%';
                           reqTempObj['fear'] = (this.clientScore['fear'] / total) * 100 +'%';
                           reqTempObj['tentative'] = (this.clientScore['tentative'] / total) * 100 +'%';
                           reqTempObj['confident'] = (this.clientScore['confident'] / total) * 100 +'%';
                           reqTempObj['analytical'] = (this.clientScore['analytical'] / total) * 100 +'%';
                           this.widthstyle = reqTempObj;
                        
                        // employee info
                        this.clientInfo = this.clientdashboardDATA.listOfEmployee;
                        for(var i = 0; i < this.clientInfo.length; i++)
                              {
                                  this.tepData = {};
                                  this.tepData = this.clientInfo[i].toneOfTeamMail;
                                   var total = 0;
                                   total = this.tepData['anger'] + this.tepData['joy'] + this.tepData['sadness'] + this.tepData['fear']
                                   + this.tepData['tentative'] + this.tepData['confident'] + this.tepData['analytical'];
                                   var  reqTempInfoObj = new BaseContainer();
                                   reqTempInfoObj['anger'] = (this.tepData['anger'] /total) * 100 +'%';
                                   reqTempInfoObj['joy'] = (this.tepData['joy'] / total) *100 +'%';
                                   reqTempInfoObj['sadness'] = (this.tepData['sadness'] / total) * 100 +'%';
                                   reqTempInfoObj['fear'] = (this.tepData['fear'] / total) * 100 +'%';
                                   reqTempInfoObj['tentative'] = (this.tepData['tentative'] / total) * 100 +'%';
                                   reqTempInfoObj['confident'] = (this.tepData['confident'] / total) * 100 +'%';
                                   reqTempInfoObj['analytical'] = (this.tepData['analytical'] / total) * 100 +'%';
                                   this.clientInfo[i]['widthstyle'] = reqTempInfoObj;
                                   console.log("email id: "+reqTempInfoObj['emailId']);
                              
                              }
                        
                        }

                        if(this.clientInfo.length == 0){
                            this.resultFlag = false;
                        }else{
                            this.resultFlag = true;
                        }
                     
                }
            });
      }
      else
          {

        this.httpService.postRequest<ClientDashboard>(requestObj,'app/auth/listCompanyClient').subscribe(
                clientdashboard=> {
                if(clientdashboard.rsBody.result=='success')
                {
                    console.log("second");
                    this.clientdashboardDATA = {};
                    this.clientdashboardDATA = clientdashboard.rsBody.msg;
                    console.log(this.clientdashboardDATA);
                    if(this.clientdashboardDATA.listOfEmployee.length!=0 )
                        {
                        this.clientScore = this.clientdashboardDATA.toneOfClientMail;
                           // bars
                        var total = 0;
                        total = this.clientScore['anger'] + this.clientScore['joy'] + this.clientScore['sadness'] + this.clientScore['fear']
                        + this.clientScore['tentative'] + this.clientScore['confident'] + this.clientScore['analytical'];
                           var  reqTempObj = new BaseContainer();
                           reqTempObj['anger'] = (this.clientScore['anger'] /total) * 100 +'%';
                           reqTempObj['joy'] = (this.clientScore['joy'] / total) *100 +'%';
                           reqTempObj['sadness'] = (this.clientScore['sadness'] / total) * 100 +'%';
                           reqTempObj['fear'] = (this.clientScore['fear'] / total) * 100 +'%';
                           reqTempObj['tentative'] = (this.clientScore['tentative'] / total) * 100 +'%';
                           reqTempObj['confident'] = (this.clientScore['confident'] / total) * 100 +'%';
                           reqTempObj['analytical'] = (this.clientScore['analytical'] / total) * 100 +'%';
                           this.widthstyle = reqTempObj;
                        
                            // clicnt for employee
                            this.clientInfo = this.clientdashboardDATA.listOfEmployee;
                            for(var i = 0; i < this.clientInfo.length; i++)
                              {
                                  this.tepData = {};
                                  this.tepData = this.clientInfo[i].toneOfTeamMail;
                                  //var data = this.employeeHierachy[i].toneOfTeamMail;
                                   var total = 0;
                                   total = this.tepData['anger'] + this.tepData['joy'] + this.tepData['sadness'] + this.tepData['fear']
                                   + this.tepData['tentative'] + this.tepData['confident'] + this.tepData['analytical'];
                                   var  reqTempInfoObj = new BaseContainer();
                                   reqTempInfoObj['anger'] = (this.tepData['anger'] /total) * 100 +'%';
                                   reqTempInfoObj['joy'] = (this.tepData['joy'] / total) *100 +'%';
                                   reqTempInfoObj['sadness'] = (this.tepData['sadness'] / total) * 100 +'%';
                                   reqTempInfoObj['fear'] = (this.tepData['fear'] / total) * 100 +'%';
                                   reqTempInfoObj['tentative'] = (this.tepData['tentative'] / total) * 100 +'%';
                                   reqTempInfoObj['confident'] = (this.tepData['confident'] / total) * 100 +'%';
                                   reqTempInfoObj['analytical'] = (this.tepData['analytical'] / total) * 100 +'%';
                                   this.clientInfo[i]['widthstyle'] = reqTempInfoObj;
                                  /*var tem['anger']= data['anger'].'%'; 
                                  this.employeeHierachy[i]['style'] = tem;*/
                              
                              }  
                              this.clientInfoMain = this.clientInfo;
                            
                        }

                            if(this.clientInfo.length == 0){
                                this.resultFlag = false;
                            }else{
                                this.resultFlag = true;
                            }
                                           
                    
                 //  this.clienttoneScore = this.clientInfo[index].toneOfTeamMail;
                    
                    
                }
            });   
          }
             
        this.cdRef.detectChanges();
    }
  
  
  changRoute(name,tabName)
  {
          
    this.sharedService.setData({'tabName':tabName});
    this.router.navigate([name]);
  }

  changRoutePersonalTeam(name,tabName){
    var  requestObj = new BaseContainer(); 
    this.router.navigate([name,this.clientdashboardDATA.employeeId]);
  }
 
  ngOnDestroy()
  {}
    
    
    autocompleListFormatter = (data: any) => {          
      let html = `<span style='color:black'>${data.clientName}</span>`;
      return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    
    onChangeClientName(value:number)
    {
        
         this.setObj = value;  
        
    }
    
    
    changRouteTouch(name){
        this.router.navigate([name]);
      }
   
   onChange(value:number)
   {
       this.backFlag = false;
       this.centerConfig = {};
       this.centerConfig['companyId'] = value['companyFK'];
       this.centerConfig['emailId'] = value['emailId'];
       this.centerConfig['employeeName'] = value['clientName'];
       this.centerConfig['designation'] = value['designation'];
       
        this.httpService.postRequest<EmployeeDashboard>(this.centerConfig,'app/auth/getClientSearchData').subscribe(
                 employeedashboard=> {
                     if(employeedashboard.rsBody.result=='success')
                     {
                        this.searchData = employeedashboard.rsBody.msg; 
                         this.searchList = employeedashboard.rsBody.msg.listOfEmployee;
                        this.setFlag = false;
                        //this.getClickEmployeeData(0); 
                        
                        
                     }
                 });  
       
    
       
    }

    public config: PaginationInstance = {
        id: 'custom',
        itemsPerPage: 10,
        currentPage: 1
    };
    
    // search by designation
       searchDesignation(){
      
       
        this.httpService.postRequest<EmployeeDashboard>(this.resObj,'app/auth/getClientSearchData').subscribe(
                 employeedashboard=> {
                     if(employeedashboard.rsBody.result=='success')
                     {
                        this.searchData = employeedashboard.rsBody.msg; 
                         this.searchList = employeedashboard.rsBody.msg.listOfEmployee;
                         this.setFlag = false;
                         this.resObj = {};
                        //this.getClickEmployeeData(0); 
                        
                        
                     }
                 });     
       
    
       
    }
    
    // sent submit   
    getClickSent(obj)
    {
        $('.filteractivesent').css('background-color', '#f5f5f5');
           $('.filteractiveall').css('background-color', '#e9e9e9');
           $('.filteractivereceive').css('background-color', '#e9e9e9'); 
        this.clientdashboardDATA.noOfMail = this.clientdashboardDATA.noOfSentMail;
         for(var i=0; i<this.clientInfo.length; i++)
         {
             
         this.clientInfo[i].noOfMail = this.clientInfo[i].noOfSentMail;
             
         }
    }
    
    // all submit
    getClickAll(obj)
    {
        $('.filteractivesent').css('background-color', '#e9e9e9');
           $('.filteractiveall').css('background-color', '#f5f5f5');
           $('.filteractivereceive').css('background-color', '#e9e9e9'); 
       this.clientdashboardDATA.noOfMail = this.resPersonalReport.noOfMail;
        
        var  requestAllObj = new BaseContainer();
         requestAllObj = this.temp;
        //  var  requestAllObj = new BaseContainer();
        //  requestAllObj['companyId'] = this.empdashboard[index]['id'];
        //   this.companyId = requestAllObj['id'];
        //   this.temp = requestAllObj;
        // this.httpService.postRequest<ClientDashboard>(requestAllObj,'app/auth/listCompanyClient').subscribe(
        //         clientdashboard=> {
        //         if(clientdashboard.rsBody.result=='success')
        //         {
        //             this.clientdashboardDATA = {};
        //             this.clientdashboardDATA = clientdashboard.rsBody.msg;
        //             if(this.clientdashboardDATA.listOfEmployee.length!=0 )
        //                 {
        //                 this.clientScore = this.clientdashboardDATA.toneOfClientMail;
        //                 this.clientInfo = this.clientdashboardDATA.listOfEmployee;
        //                 }                    
                    
        //          //  this.clienttoneScore = this.clientInfo[index].toneOfTeamMail;
                    
                    
        //         }
        //     });   

        this.httpService.postRequest<ClientDashboard>(requestAllObj,'app/auth/listCompanyClient').subscribe(
            clientdashboard=> {
            if(clientdashboard.rsBody.result=='success')
            {
                console.log("second");
                this.clientdashboardDATA = {};
                this.clientdashboardDATA = clientdashboard.rsBody.msg;
                console.log(this.clientdashboardDATA);
                if(this.clientdashboardDATA.listOfEmployee.length!=0 )
                    {
                    this.clientScore = this.clientdashboardDATA.toneOfClientMail;
                       // bars
                    var total = 0;
                    total = this.clientScore['anger'] + this.clientScore['joy'] + this.clientScore['sadness'] + this.clientScore['fear']
                    + this.clientScore['tentative'] + this.clientScore['confident'] + this.clientScore['analytical'];
                       var  reqTempObj = new BaseContainer();
                       reqTempObj['anger'] = (this.clientScore['anger'] /total) * 100 +'%';
                       reqTempObj['joy'] = (this.clientScore['joy'] / total) *100 +'%';
                       reqTempObj['sadness'] = (this.clientScore['sadness'] / total) * 100 +'%';
                       reqTempObj['fear'] = (this.clientScore['fear'] / total) * 100 +'%';
                       reqTempObj['tentative'] = (this.clientScore['tentative'] / total) * 100 +'%';
                       reqTempObj['confident'] = (this.clientScore['confident'] / total) * 100 +'%';
                       reqTempObj['analytical'] = (this.clientScore['analytical'] / total) * 100 +'%';
                       this.widthstyle = reqTempObj;
                    
                        // clicnt for employee
                        this.clientInfo = this.clientdashboardDATA.listOfEmployee;
                        for(var i = 0; i < this.clientInfo.length; i++)
                          {
                              this.tepData = {};
                              this.tepData = this.clientInfo[i].toneOfTeamMail;
                              //var data = this.employeeHierachy[i].toneOfTeamMail;
                               var total = 0;
                               total = this.tepData['anger'] + this.tepData['joy'] + this.tepData['sadness'] + this.tepData['fear']
                               + this.tepData['tentative'] + this.tepData['confident'] + this.tepData['analytical'];
                               var  reqTempInfoObj = new BaseContainer();
                               reqTempInfoObj['anger'] = (this.tepData['anger'] /total) * 100 +'%';
                               reqTempInfoObj['joy'] = (this.tepData['joy'] / total) *100 +'%';
                               reqTempInfoObj['sadness'] = (this.tepData['sadness'] / total) * 100 +'%';
                               reqTempInfoObj['fear'] = (this.tepData['fear'] / total) * 100 +'%';
                               reqTempInfoObj['tentative'] = (this.tepData['tentative'] / total) * 100 +'%';
                               reqTempInfoObj['confident'] = (this.tepData['confident'] / total) * 100 +'%';
                               reqTempInfoObj['analytical'] = (this.tepData['analytical'] / total) * 100 +'%';
                               this.clientInfo[i]['widthstyle'] = reqTempInfoObj;
                              /*var tem['anger']= data['anger'].'%'; 
                              this.employeeHierachy[i]['style'] = tem;*/
                          
                          }  
                    }                    
                
             //  this.clienttoneScore = this.clientInfo[index].toneOfTeamMail;
                
                
            }
        });
        
     }
    
    // received submit
    getClickReceive(obj)
    {
        $('.filteractivesent').css('background-color', '#e9e9e9');
           $('.filteractiveall').css('background-color', '#e9e9e9');
           $('.filteractivereceive').css('background-color', '#f5f5f5'); 
        this.clientdashboardDATA.noOfMail = this.clientdashboardDATA.noOfReceiveMail;
         for(var i=0; i<this.clientInfo.length; i++)
         {
             
           this.clientInfo[i].noOfMail = this.clientInfo[i].noOfReceiveMail;
             
         }
        
     }
    
    searchOrganization()
    {
        
        this.setsearchbyname = true;
        this.backFlag = false;
        this.centerConfig = {};
        this.centerConfig['companyId'] = this.setObj['companyFK'];
        this.centerConfig['emailId'] = this.setObj['emailId'];
        this.centerConfig['employeeName'] = this.setObj['clientName'];
        this.centerConfig['designation'] = this.setObj['designation'];
        this.centerConfig['id'] = this.companyId;
       
        this.httpService.postRequest<EmployeeDashboard>(this.centerConfig,'app/auth/getClientSearchData').subscribe(
                 employeedashboard=> {
                     if(employeedashboard.rsBody.result=='success')
                     {
                        this.searchData = employeedashboard.rsBody.msg; 
                         this.searchList = employeedashboard.rsBody.msg.listOfEmployee;
                         console.log(this.searchList);
                        this.setFlag = false;
                        //this.getClickEmployeeData(0); 
                        for(var i = 0; i < this.searchList.length; i++)
                              {
                                  this.tepData = {};
                                  this.tepData = this.searchList[i].toneOfTeamMail;
                                   var total = 0;
                                   total = this.tepData['anger'] + this.tepData['joy'] + this.tepData['sadness'] + this.tepData['fear']
                                   + this.tepData['tentative'] + this.tepData['confident'] + this.tepData['analytical'];
                                   var  reqTempInfoObj = new BaseContainer();
                                   reqTempInfoObj['anger'] = (this.tepData['anger'] /total) * 100 +'%';
                                   reqTempInfoObj['joy'] = (this.tepData['joy'] / total) *100 +'%';
                                   reqTempInfoObj['sadness'] = (this.tepData['sadness'] / total) * 100 +'%';
                                   reqTempInfoObj['fear'] = (this.tepData['fear'] / total) * 100 +'%';
                                   reqTempInfoObj['tentative'] = (this.tepData['tentative'] / total) * 100 +'%';
                                   reqTempInfoObj['confident'] = (this.tepData['confident'] / total) * 100 +'%';
                                   reqTempInfoObj['analytical'] = (this.tepData['analytical'] / total) * 100 +'%';
                                   this.searchList[i]['widthstyle'] = reqTempInfoObj;
                              
                              }

                              this.clientInfo = this.searchList;
                        
                        
                     }
                 });  
        
     }
    
    
       designantionforclient = (data: any) => {                   
      let html = `<span style='color:black'>${data.designation}</span>`;
      return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    organizationforclient = (data: any) => {      
        
            
      let html = `<span style='color:black'>${data.companyName}</span>`;
      return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    
    onChangeDesignation(value:number)
    {
        this.setDesObj = value;
    }
    onChangeOrganization(value:number)
    {
       this.setOrgObj = value;  
    }
    
    
    searchDesOrgSubmit()
    {
        this.setsearchbydesignation = true;
        this.backFlag = false;
        if(this.setOrgObj != null && this.setDesObj != null)
        {
            
        
                this.centerConfig = {};
                this.centerConfig['companyId'] = this.setOrgObj['id'];
                this.centerConfig['emailId'] = this.setDesObj['emailId'];
                this.centerConfig['employeeName'] = this.setDesObj['clientName'];
                this.centerConfig['designation'] = this.setDesObj['designation'];
               
                this.httpService.postRequest<EmployeeDashboard>(this.centerConfig,'app/auth/listSortedClient').subscribe(
                         employeedashboard=> {
                             if(employeedashboard.rsBody.result=='success')
                             {
                                 this.searchData = employeedashboard.rsBody.msg; 
                                 this.searchList = employeedashboard.rsBody.msg.listOfEmployee;
                                 this.setFlag = false;
                                //this.getClickEmployeeData(0); 
                                
                                
                             }
                         });  
           }
        
     }
    
    
    // open drop down filter
    
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
    
    
    // search by alphabeticaly search
    
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
    
    searchcreteria(obj)
    {
        var  resultList = new BaseContainer();
        resultList['searchCriteria'] = obj;
        resultList['companyId'] = this.temp['companyId'];
        //resultList['employeeId'] = this.employeeReport[this.activeindex]['employeeId'];
       
        //var requestObj['searchCriteria'] = this.serachList;
        this.httpService.postRequest<ClientDashboard>(resultList,'app/auth/listSortedClient').subscribe(
              clientdashboard=> {
                if(clientdashboard.rsBody.result=='success')
                {
                    this.clientdashboardDATA = {};
                    this.resPersonalReport = Object.assign({}, clientdashboard.rsBody.msg); 
                    this.clientdashboardDATA = clientdashboard.rsBody.msg;
                    if(this.clientdashboardDATA.listOfEmployee.length!=0 )
                        {
                        this.clientScore = this.clientdashboardDATA.toneOfClientMail;
                        this.clientInfo = this.clientdashboardDATA.listOfEmployee;
                        }
                     
                }
             }); 
          
     }
        // clear check box checked
        ascOrDescList = [
        { id: 1, name: 'Alphabetically', option:'alphabetically'}
        ];
    
    resetNone()
    {
        
      this.serachList = [];
      this.ascOrDescList.forEach((item) => {
        item.checked = false;
      })
      
      this.ngAfterViewInit();
    
    }
    
    
    
        teamScoreList = [    
        { id: 1, name: 'Anger', option:'anger'},
        { id: 2, name: 'Joy', option:'joy'},
        { id: 3, name: 'Sadness', option:'sadness'},
        { id: 4, name: 'Fear', option:'fear'},
        { id: 5, name: 'Tentative', option:'tentative'},
        { id: 6, name: 'Confident', option:'confident'},
        { id: 7, name: 'Analytical', option:'analytical'}        
        ];
    
    resetScoreNone()
    {
        
      this.serachteamtone = [];
      this.teamScoreList.forEach((item) => {
        item.checked = false;
      })
      
      this.ngAfterViewInit();
    
    }
    
    
    opendropdownname()
    {
       if(this.setsearchbyname == true)
       {
           this.setsearchbyname = false;
       }
       else
       {
           this.setsearchbyname = true;
       }    
    }
    
    opendropdownondesignation()
    {
       if(this.setsearchbydesignation == true)
       {
           this.setsearchbydesignation = false;
       }
       else
       {
           this.setsearchbydesignation = true;
       }    
    }
    
    asendignOrderSubmit()
    {
        this.orderflag = false;
         this.searchcreteriateamtone('ASC', this.serachteamtone)
    }
    
    dsendignOrderSubmit()
    {
        this.orderflag = true;
        this.searchcreteriateamtone('DSC', this.serachteamtone)
    }
    
     asendignOrderTeamSubmit()
    {
        this.orderTeamflag = false;       
        this.searchcreteriateamtone('ASC', ['teamMail']);
       

    }
    
    dsendignOrderTeamSubmit()
    {
        this.orderTeamflag = true;
        this.searchcreteriateamtone('DSC', ['teamMail']);
    }
    
        // comman function
    searchcreteriateamtone(obj, searchCreteria)
    {
        
        var  resultList = new BaseContainer();
        resultList['searchCriteria'] = searchCreteria;
        resultList['companyId'] = this.temp['companyId'];
        resultList['sortType'] = obj;
       
        //var requestObj['searchCriteria'] = this.serachList;
        this.httpService.postRequest<ClientDashboard>(resultList,'app/auth/listSortedClient').subscribe(
             clientdashboard=> {
                if(clientdashboard.rsBody.result=='success')
                {
                    this.clientdashboardDATA = {};
                    this.resPersonalReport = Object.assign({}, clientdashboard.rsBody.msg); 
                    this.clientdashboardDATA = clientdashboard.rsBody.msg;
                    if(this.clientdashboardDATA.listOfEmployee.length!=0 )
                    {
                    this.clientScore = this.clientdashboardDATA.toneOfClientMail;
                    this.clientInfo = this.clientdashboardDATA.listOfEmployee;
                      for(var i = 0; i < this.clientInfo.length; i++)
                      {
                          this.tepData = {};
                          this.tepData = this.clientInfo[i].toneOfTeamMail;
                          //var data = this.employeeHierachy[i].toneOfTeamMail;
                           var total = 0;
                           total = this.tepData['anger'] + this.tepData['joy'] + this.tepData['sadness'] + this.tepData['fear']
                           + this.tepData['tentative'] + this.tepData['confident'] + this.tepData['analytical'];
                           var  reqTempInfoObj = new BaseContainer();
                           reqTempInfoObj['anger'] = (this.tepData['anger'] /total) * 100 +'%';
                           reqTempInfoObj['joy'] = (this.tepData['joy'] / total) *100 +'%';
                           reqTempInfoObj['sadness'] = (this.tepData['sadness'] / total) * 100 +'%';
                           reqTempInfoObj['fear'] = (this.tepData['fear'] / total) * 100 +'%';
                           reqTempInfoObj['tentative'] = (this.tepData['tentative'] / total) * 100 +'%';
                           reqTempInfoObj['confident'] = (this.tepData['confident'] / total) * 100 +'%';
                           reqTempInfoObj['analytical'] = (this.tepData['analytical'] / total) * 100 +'%';
                           this.clientInfo[i]['widthstyle'] = reqTempInfoObj;
                          /*var tem['anger']= data['anger'].'%'; 
                          this.employeeHierachy[i]['style'] = tem;*/
                      
                      }
                    }
                     
                }
             }); 
          
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
            resObj['companyId'] = this.temp['companyId'];
            this.httpService.postRequest<EmployeeDashboard>(resObj,'app/auth/listClientToExcel').subscribe(
                 employeedashboard=> {
                     if(employeedashboard.rsBody.result=='success')
                     {
                        var exeFile = employeedashboard.rsBody.msg;
                        $('#ExportExcelData').attr('href', exeFile);
                        $('#ExportExcelData')[0].click();
                     }
             }); 
        }

        clientscore(i){
            
            var name = this.clientInfo[i].employeeName;
            var emailId  = this.clientInfo[i].emailId;
            var routObj = new BaseContainer();
            routObj['userId'] = this.clientdashboardDATA.employeeId;
            console.log("in client score");
            routObj['clientEmailId'] = emailId;
            routObj['clientName'] = name;
            this.router.navigate(['my-team-email',routObj]);

        }

        orgscore(){
            var routObj = new BaseContainer();
            routObj['userId'] = this.clientdashboardDATA.employeeId;
            routObj['companyId'] = this.empdashboard[this.activeCompanyIndex]['id'];
            routObj['companyName'] = this.empdashboard[this.activeCompanyIndex]['companyName'];
            console.log(this.empdashboard[this.activeCompanyIndex]['companyName']);
            this.router.navigate(['my-team-email',routObj]);

        }

        teamtoneemployeeAdverse(obj){

            var empId = this.clientdashboardDATA.employeeId;
            var adverseFilter = "yes";
            //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
            var  routObj = new BaseContainer();
            routObj['userId'] = empId; 
            routObj['adverseFilter'] = adverseFilter;
            this.router.navigate(['my-team-email',routObj]);
          }
        
          teamtoneemployeeEscalation(obj){
        
            var empId = this.clientdashboardDATA.employeeId;
            var escalationFilter = "yes";
            //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
            var  routObj = new BaseContainer();
            routObj['userId'] = empId; 
            routObj['escalationFilter'] = escalationFilter;
            this.router.navigate(['my-team-email',routObj]);
          }
        
        clienttonesearch(obj,i){

            var name = this.clientInfo[i].employeeName;
            var emailId  = this.clientInfo[i].emailId;
            //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
            var  routObj = new BaseContainer();
            routObj['clientEmailId'] = emailId;
            routObj['clientName'] = name;
            routObj['typeClient'] = obj;
            this.router.navigate(['my-team-email',routObj]);
        
          }

          orgtonesearch(obj){
            
            var  routObj = new BaseContainer();
            routObj['companyId'] = this.empdashboard[this.activeCompanyIndex]['id'];
            routObj['companyName'] = this.empdashboard[this.activeCompanyIndex]['companyName'];
            console.log(this.empdashboard[this.activeCompanyIndex]['companyName']);
            routObj['typeOrg'] = obj;
            this.router.navigate(['my-team-email',routObj]);
          }

          executivefn(event){
              console.log("executiive");
              if(event.target.checked){
                  console.log("checked");
                  this.clientInfo = [];
                  var j = 0;
                for(var i =0; i<this.clientInfoMain.length;i++){
                    
                    if(this.clientInfoMain[i]['executive'] != undefined){
                    if(this.clientInfoMain[i]['executive'].indexOf("yes")>-1){
                        console.log("here" + this.clientInfoMain[i]['executive']);
                        this.clientInfo[j] = this.clientInfoMain[i];
                        j++;
                    }
                }
                this.cdRef.detectChanges();
                }
                this.executiveFlag = false;
              }
              else{
                  this.clientInfo = this.clientInfoMain;
                  console.log("unchecked");
              }
          }

}
