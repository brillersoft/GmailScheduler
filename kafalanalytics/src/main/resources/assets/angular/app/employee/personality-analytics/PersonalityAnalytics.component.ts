import { Component ,OnInit,OnDestroy,AfterViewInit,Input,Output,EventEmitter} from '@angular/core';
import { Router ,ActivatedRoute, UrlHandlingStrategy} from '@angular/router';
import {BaseContainer} from '../../BaseContainer';
import {HttpService} from '../../service/http.service';
import { SharedService } from '../../service/shared-data-service';
declare var $:any;

var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL =   'templates/employee/personalityAnalytics.component.html';

@Component({
  selector: 'app-root',
  templateUrl: _templateURL
})
export class PersonalityAnalyticsComponent implements OnInit,OnDestroy,AfterViewInit {

    private dashboard:Object = {};

    private first:boolean = true;
    private second:boolean = false;
    private third:boolean = false;

    constructor(
        private router:Router,
        private httpService: HttpService,
        private sharedService:SharedService
    ){

    }

    ngOnInit(){
        this.first = true;
        this.second = false;
        this.third = false;

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
    }

    ngAfterViewInit(){

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

      changRouteTouch(name){
        this.first = true;
        this.second = false;
        this.third = false;
      }

      changRoute(name){
        this.router.navigate([name]);
      }

      changRoutePersonal(name,tabName){
        
          var  requestObj = new BaseContainer();
          var userId = this.dashboard['employeeId'];
          requestObj['userId'] = this.dashboard['employeeId'];
          this.router.navigate([name,userId]);
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

    firstclick(){
        this.first = false;
        this.second = true;
    }

    secondclick(){
        this.second = false;
        this.third = true;
    }

    ngOnDestroy(){
        
    }

}