import { Component ,OnInit,OnDestroy,AfterViewInit, ChangeDetectorRef, ViewChild, ElementRef} from '@angular/core';
import {HttpService} from '../../service/http.service';
import { Router ,ActivatedRoute,Params} from '@angular/router';
import { Modal, BSModalContext } from 'angular2-modal/plugins/bootstrap';
import { EmployeeApp } from './employeeapp';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { UserIdleService } from 'angular-user-idle';
import {ValidationHandler} from '../ValidationHandler';
//import { EmployeeDashboardComponent } from "../employeedashboard/employeedashboard.component";
declare var $:any;
var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL =   'templates/employee/employeeapp.component.html';

@Component({
  selector: 'app-root',
  templateUrl: _templateURL,
  providers: [Modal,ValidationHandler],
})
export class EmployeeAppComponent implements OnInit,OnDestroy,AfterViewInit {
  //@ViewChild(EmployeeDashboardComponent) child;
  private employee:Object = {};
  private employeePersonalReport:object = {};
  private employeeId:any;
  private employeeRole:any;
  private drawernav:boolean = false;
  // @ViewChild('drawer') drawer: ElementRef;
  @ViewChild('signOut') signOut: ElementRef;

  constructor(
    private httpService: HttpService,
    private router:Router,
    private validationHandler : ValidationHandler,
    private sharedService:SharedService,

    private cdRef:ChangeDetectorRef,
    public modal: Modal,
    private userIdle: UserIdleService)
  {}
  
  public username:any;
  public dashboard:any = {};
  public teamRelation:any = {};
  public clientRelation:any = {};
  public userId:any = "";
  ngOnInit()
  {
          //Start watching for user inactivity.
    this.userIdle.startWatching();
    
    // Start watching when user idle is starting.
    this.userIdle.onTimerStart().subscribe(count => console.log(count));
    
    // Start watch when time is up.
    this.userIdle.onTimeout().subscribe(() => this.signUserOut());



      /*$(document).ready(function(){
          $("body").tooltip({ selector: '[data-toggle=tooltip]' });
      });*/
      

  }
  ngAfterViewInit(){
      
      //this.dashboard = this.child.dashboard;
    var  requestObj = new BaseContainer();
     this.httpService.postRequest<EmployeeApp>(this.dashboard,'app/auth/dislaydashboard').subscribe(
     studentapp=> {
     
     if(studentapp.rsBody.result=='success')
     {
        this.dashboard = studentapp.rsBody.msg;
        this.teamRelation = this.dashboard['relationWithTeam'];
        this.clientRelation = this.dashboard['relationWithClient'];
        this.employeeId = this.dashboard['employeeId'];
        //this.employeeRole = this.dashboard['role']; 
        this.employeeRole="admin";
        this.sharedService.setData({"role": this.employeeRole});
        console.log(this.employeeRole);
        console.log(this.employeeId);
      }
    });
    
     this.cdRef.detectChanges();
  }

  signUserOut(){
    let el: HTMLElement = this.signOut.nativeElement as HTMLElement;
    el.click();
  }

  drawer1(){
    if(this.drawernav == true){
      this.drawernav = false;
    }else{
      this.drawernav = true;
      // this.drawer.nativeElement.style.visibility = 'visible';
    }
    console.log(this.drawernav);
  }

  changeRoute(name){
    this.router.navigate([name]);
  }

  changeRouteAdmin(name){
    var userId = this.employeeId;
    this.router.navigate([name,userId]);
  }

  changRoute(name,tabName)
  {
          this.sharedService.setData({'tabName':tabName,'companyName':"",'companyIndex':0});
          this.router.navigate([name]);
  }
  changRouteemployee(name,tabName)
  {
    this.sharedService.setData({'companyName':"",'companyIndex':0});
          var userId = this.employeeId;
          //this.router.navigate([name,empId]);
          this.router.navigate([name,userId]);
  }
  changRouteTeamScore(name,tabName)
  {
    this.sharedService.setData({'companyName':"",'companyIndex':0});
    var userId = this.employeeId;
     this.router.navigate([name,userId]);
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

  ngOnDestroy(){
  }
  
}
