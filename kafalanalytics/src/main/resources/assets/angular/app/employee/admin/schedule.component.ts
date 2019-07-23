import { Component ,OnInit,OnDestroy,AfterViewInit} from '@angular/core';
// import {HttpService} from '../../service/http.service';
import {HttpService} from '../../service/http.service';
import { OrganizationAdd } from './organizationadd';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { ChangeDetectorRef } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { Router ,ActivatedRoute, Route} from '@angular/router';
// import {NgxMaterialTimepickerModule} from 'ngx-material-timepicker';
// import {BaseContainer} from '../../BaseContainer';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import {Observable} from 'rxjs/Rx';
import { TimerObservable } from "rxjs/observable/TimerObservable";

// import { DataService } from "../data.service";
import { FormBuilder, FormArray, FormGroup} from '@angular/forms';


declare var $:any;

var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL =   'templates/employee/schedule.component.html';

export interface progressBar{
    adminId: string;
    totalEmps: number;
    currentEmpNum: number;
    currentEmailId: string;
    totalMails: number;
    currentMailNum: number;
    typeMailbox: string;
    status: string;
}

@Component({
  selector: 'app-root',
  templateUrl: _templateURL,
})

export class ScheduleComponent implements OnInit,OnDestroy,AfterViewInit {
  
    scheduleForm: FormGroup;
    scheduleFormWeekly: FormGroup;
    scheduleFormMonthly: FormGroup;
    private readonly notifier: NotifierService;
    private daily:boolean = true;
    private weekly:boolean = false;
    private monthly:boolean = false;
    private runNow: boolean = false;
    private week: any = [];
    private totalRecords: number = 0;
    private recordsCompleted: number = 0;
    private progressbarPercentage: number = 0;
    private progressbarPercentageMails: number = 0;
    private scheduleProgress: boolean = false;
    private scheduleWait: boolean = false;
    private alive: boolean;
    private interval: number;
    public userId: any = "";
    public parmsData: any = {};
    public progressBar: progressBar;
   
     constructor(private fb: FormBuilder,  
        private httpService: HttpService,
        private router:Router,
        private sharedService:SharedService,
        private route: ActivatedRoute,
        notifierService: NotifierService,
    ) {
        this.progressBar = {
            adminId: "",
            totalEmps: 0,
            currentEmpNum: 0,
            currentEmailId: "",
            totalMails: 0,
            currentMailNum: 0,
            typeMailbox: "",
            status: ""
        }
        this.notifier = notifierService;
    };


    ngOnInit() {
        this.runNow = false;
        this.alive = true;
        this.interval = 3000;
        this.scheduleForm = this.fb.group({
            date: '', 
            time: ''
            
        });

        this.scheduleFormWeekly = this.fb.group({
            day: '', 
            time: ''
            
            });

        this.scheduleFormMonthly = this.fb.group({
            date: '', 
            time: ''
            
        });

        this.week['MON'] = false;
        this.week['TUE'] = false;
        this.week['WED'] = false;
        this.week['THU'] = false;
        this.week['FRI'] = false;
        this.week['SAT'] = false;
        this.week['SUN'] = false;
        
        this.parmsData = this.route.snapshot.params;
        this.userId = this.parmsData["userId"];

          
    }

    ngAfterViewInit(){

        TimerObservable.create(0, this.interval)
      .takeWhile(() => this.alive)
      .subscribe(() => {
        this.checkProgress();
      });
       
    }

    checkProgress(){
        var  requestObject = new BaseContainer();
        requestObject["userId"] = this.userId;

        console.log("Request Called");

        this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/getbatchprogress').subscribe(
            organizationadd=> {
                if(organizationadd.rsBody != null){
                    if(organizationadd.rsBody.result == "success"){
                        // console.log("Return ob");
                        // console.log(organizationadd.rsBody.msg);
                        // console.log("progress bar");
                        this.progressBar = organizationadd.rsBody.msg;
                        // console.log(this.progressBar);
                        if(this.progressBar.status == 'active'){
                            this.scheduleProgress = true;
                        }
                        if(this.progressBar.totalEmps != 0 && this.progressBar.totalEmps >= this.progressBar.currentEmpNum){
                            this.progressbarPercentage = Math.round((this.progressBar.currentEmpNum / this.progressBar.totalEmps) * 100);
                        }

                        if(this.progressBar.totalMails != 0 && this.progressBar.totalMails >= this.progressBar.currentMailNum){
                            this.progressbarPercentageMails = Math.round((this.progressBar.currentMailNum / this.progressBar.totalMails) * 100);
                        }
                    }else if(organizationadd.rsBody.result == "error"){
                        console.log(organizationadd.rsBody.msg);
                    }
                }
            });        
    }

    changeRouteAdmin(name){
        var userId = this.userId;
        this.router.navigate([name,userId]);
      }
    
   
    scheduleBatch(){
        // console.log(this.scheduleForm);
        var  requestObject = new BaseContainer(); 
        requestObject = this.scheduleForm.value;

        this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/savecron').subscribe(
            organizationadd=> {
                // console.log(organizationadd.rsBody.result);
                // console.log(organizationadd.rsBody.msg);
                if(organizationadd.rsBody.result == "success"){
                    if(organizationadd.rsBody.msg == "101"){
                        this.notifier.notify( 'success', 'Batch scheduled successfully!' );
                    }else if(organizationadd.rsBody.msg == "102"){
                        this.notifier.notify( 'error', 'Oops! Some Error Occured!' );
                    }
                }


            });
    }


    stopBatchNow(){

        var  requestObject = new BaseContainer();
        requestObject["userId"] = this.userId;

        this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/stopBatchNow').subscribe(
            organizationadd=> {
                if(organizationadd.rsBody != null){
                    if(organizationadd.rsBody.result == "success"){
                        console.log(organizationadd.rsBody.msg);
                        this.scheduleProgress = false;
                        this.scheduleWait = false;
                        
                    }else if(organizationadd.rsBody.result == "error"){
                        console.log(organizationadd.rsBody.msg);
                    }
                }
            }); 


    }



    scheduleBatchWeekly(){

        if(this.week['MON'] == true || this.week['TUE'] == true || this.week['WED'] == true || this.week['THU'] == false || this.week['FRI'] == false || this.week['SAT'] == false || this.week['SUN'] == false){

            this.scheduleFormWeekly.value.day = "";

            if(this.week['MON'] == true){
                this.scheduleFormWeekly.value.day = this.scheduleFormWeekly.value.day + "1,"
            }
            if(this.week['TUE'] == true){
                this.scheduleFormWeekly.value.day = this.scheduleFormWeekly.value.day + "2,"
            }
            if(this.week['WED'] == true){
                this.scheduleFormWeekly.value.day = this.scheduleFormWeekly.value.day + "3,"
            }
            if(this.week['THU'] == true){
                this.scheduleFormWeekly.value.day = this.scheduleFormWeekly.value.day + "4,"
            }
            if(this.week['FRI'] == true){
                this.scheduleFormWeekly.value.day = this.scheduleFormWeekly.value.day + "5,"
            }
            if(this.week['SAT'] == true){
                this.scheduleFormWeekly.value.day = this.scheduleFormWeekly.value.day + "6,"
            }
            if(this.week['SUN'] == true){
                this.scheduleFormWeekly.value.day = this.scheduleFormWeekly.value.day + "7,"
            }

            this.scheduleFormWeekly.value.day = this.scheduleFormWeekly.value.day.slice(0, -1);
            console.log("Days" + JSON.stringify(this.scheduleFormWeekly.value, undefined ,2));
            console.log(JSON.stringify(this.scheduleFormWeekly.value));

            var  requestObject = new BaseContainer(); 
        requestObject = this.scheduleFormWeekly.value;

        this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/savecron').subscribe(
            organizationadd=> {
                // console.log(organizationadd.rsBody.result);
                // console.log(organizationadd.rsBody.msg);
                if(organizationadd.rsBody.result == "success"){
                    if(organizationadd.rsBody.msg == "101"){
                        this.notifier.notify( 'success', 'Batch scheduled successfully!' );
                    }else if(organizationadd.rsBody.msg == "102"){
                        this.notifier.notify( 'error', 'Oops! Some Error Occured!' );
                    }
                }


            });

        }else{
            console.log("no days selected");
        }


    }


    runCronNow(){

        var  requestObject = new BaseContainer(); 

        this.scheduleProgress = false;
        this.scheduleWait = true;

        this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/runcron').subscribe(
            organizationadd=> {
                // console.log(organizationadd.rsBody.result);
                // console.log(organizationadd.rsBody.msg);
                if(organizationadd.rsBody.result == "success"){
                    if(organizationadd.rsBody.msg == "101"){
                        console.log('Batch scheduled successfully!' );
                    }else if(organizationadd.rsBody.msg == "102"){
                        console.log('Oops! Some Error Occured!' );
                    }
                }


            });

    }

    
    tabtoggle(value: string){
        if(value == "daily"){
            this.daily = true;
            this.weekly = false;
            this.monthly = false;
        }else if(value == "weekly"){
            this.daily = false;
            this.weekly = true;
            this.monthly = false;
        }else if(value == "monthly"){
            this.daily = false;
            this.weekly = false;
            this.monthly = true;
        }
    }
   

    runBatchNow(){

        this.runNow = true;

    }

    stopBatch(){

        this.runNow = false;

    }

    setDayActive(day:string){
        if(this.week[day] == true){
          this.week[day] = false; 
        }else{
            this.week[day] = true;
        }
    }

    getWeekColor(day:string){
        if(this.week[day] == true){
            // this.week[day] = false;
            return '#439cc4';
        }else{
            // this.week[day] = true;
            return '#C6C6C6';
        }
    }


    changRoute(name,tabName)
    {
            this.sharedService.setData({'tabName':tabName});
            this.router.navigate([name]);
    }

    
    ngOnDestroy(){

        this.alive = false;
      
    }

    
}