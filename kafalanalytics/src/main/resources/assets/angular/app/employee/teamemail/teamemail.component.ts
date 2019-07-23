import {
  Component,
  OnInit,
  OnDestroy,
  AfterViewInit,
  Input,
  Output,
  EventEmitter,
  HostListener
} from "@angular/core";
import { HttpService } from "../../service/http.service";
import { Router, ActivatedRoute } from "@angular/router";
import { EmployeeDashboard } from "../employeedashboard/employeedashboard";
import { TeamEmail } from "./teamemail";
import { BaseContainer } from "../../BaseContainer";
import { SharedService } from "../../service/shared-data-service";
import { ChangeDetectorRef } from "@angular/core";
import { NgxPaginationModule } from "ngx-pagination";
import { MatTooltipModule } from "@angular/material/tooltip";
import { PaginationInstance } from "ngx-pagination";
import { ClientDashboard } from "../../../../../../../../target/classes/assets/angular/app/employee/clientdashboard/clientdashboard";

declare var $: any;

var path = require("path");

var _publicPath = path.resolve(__dirname, "../../../../../../webapp");

var _templateURL = "templates/employee/teamemail.component.html";

@Component({
  selector: "app-root",
  templateUrl: _templateURL
})
export class TeamEmailComponent implements OnInit, OnDestroy, AfterViewInit {
  private EmployeeTeamEmail: any = [];
  private EmployeeTeamEmailMain: any = [];

  constructor(
    private httpService: HttpService,
    private router: Router,
    private sharedService: SharedService,
    private route: ActivatedRoute,
    private cdRef: ChangeDetectorRef
  ) {}

  public userId: any = "";
  public identify: any = "";
  public employeeId: any = "";
  public parData: Object = {};
  public searchtext: any = "";
  public searchtextClient: any = "";
  public searchtextOrg: any = "";
  public backList: any = [];
  public dashboard: Object = {};
  public teamRelation: Object = {};
  public clientRelation: Object = {};
  public clientEmailId: String = "";
  public clientName: String = "";
  public companyId: String = "";
  public companyName: String = "";
  public adverseEventFlag: boolean = true;
  public adverseFlag: boolean = true;
  public escalationFlag: boolean = true;
  public adverseFilter: String = "";
  public escalationFilter: String = "";
  private employeeTeamEmail: object = {};
  public emailSubjectFlag: boolean = true;
  public employeeFlag: boolean = true;
  public clientFlag: boolean = true;
  public orgFlag: boolean = true;
  public resultFlag: boolean = true;
  public searchbyDateflag: boolean = true;
  public searchbyTimeflag: boolean = true;
  public teamtonedotedflag: boolean = true;
  public teamtoneflag: boolean = true;
  public teamColDottedFlag: boolean = true;
  public selectAllColumnFlag: boolean = false;
  public showColumnByName: any = [
    "employee",
    "client",
    "organization",
    "sentreceive"
  ];
  public p: number = 1;
  public startPage: number = 1;
  public lastPage: number = 1;
  public itemsPerPage: number = 10;
  @Input()
  id: string;
  @Input()
  page;
  @Input()
  maxSize: number;

  @Output()
  pageChange: EventEmitter<number> = new EventEmitter<number>();
  //   public page:number = 1;
  private orgObj: Object = {};
  teamScoreListDotted = [
    { id: 1, name: "Anger", option: "anger", color: "text-danger" },
    { id: 2, name: "Joy", option: "joy", color: "text-success" },
    { id: 3, name: "Sadness", option: "sadness", color: "text-secondary" },
    { id: 4, name: "Fear", option: "fear", color: "text-dark" },
    { id: 5, name: "Tentative", option: "tentative", color: "text-warning" },
    { id: 6, name: "Confident", option: "confident", color: "text-purple" },
    { id: 7, name: "Analytical", option: "analytical", color: "text-gray" }
  ];
  teamScoreColumnList = [
    { id: 1, name: "Employee", option: "employee" },
    { id: 2, name: "Client", option: "client" },
    { id: 3, name: "CC Client", option: "ccclient" },
    { id: 4, name: "CC Employee", option: "ccemployee" },
    { id: 5, name: "Organazation", option: "organization" },
    { id: 6, name: "Date/Time", option: "datetime" },
    { id: 7, name: "Sent/Receive", option: "sentreceive" }
  ];
  public selectAllflag: boolean = true;
  public serachteamtonedotted: any = [];

  clickedInside($event: Event) {
    $event.preventDefault();
    $event.stopPropagation(); // <- that will stop propagation on lower layers
  }

  @HostListener("document:click", ["$event"])
  clickedOutside($event) {
    this.clearFlags();
  }

  ngOnInit() {
    console.log(this.route.snapshot.params);
    this.parData = this.route.snapshot.params;
    this.employeeId = this.parData["userId"];
    this.searchtext = this.parData["type"];
    this.clientEmailId = this.parData["clientEmailId"];
    this.clientName = this.parData["clientName"];
    this.searchtextClient = this.parData["typeClient"];
    this.searchtextOrg = this.parData["typeOrg"];
    this.companyId = this.parData["companyId"];
    this.companyName = this.parData["companyName"];
    this.adverseFilter = this.parData["adverseFilter"];
    this.escalationFilter = this.parData["escalationFilter"];
    //   this.page = 1;
    if (this.adverseFilter == null || this.adverseFilter == undefined) {
      this.adverseFilter = "";
    }
    if (this.escalationFilter == null || this.escalationFilter == undefined) {
      this.escalationFilter = "";
    }
    if (this.clientEmailId == undefined || this.clientName == undefined) {
      this.clientEmailId = "";
      this.clientName = "";
    }
    if (this.companyId == undefined) {
      this.companyId = "";
    }
    if (this.companyName == undefined) {
      this.companyName = "";
    }
    console.log("Adverse filter" + this.adverseFilter);
    // this.toggleAdverse();
    // this.toggleAdverse();
    console.log("company name" + this.companyName);
    /*if(this.sharedService.getData() != undefined && this.sharedService.getData().userId != undefined )
      {
     
      //this.userId = this.sharedService.getData().userId;
       this.employeeId = this.sharedService.getData().userId;
       this.searchtext = this.sharedService.getData().tesmscore;
       this.backList = this.sharedService.getData().empHir;
     
     
      } */
  }
  /**************************for grid column*****************/
  teamGridDropdown($event) {
    $event.stopPropagation();
    if (this.teamColDottedFlag == true) {
      this.teamColDottedFlag = false;
    } else {
      this.teamColDottedFlag = true;
    }
  }

  selectCollumnAll($event) {
    $event.stopPropagation();
    if (this.selectAllColumnFlag === false) {
      this.selectAllColumnFlag = true;
      for (let item of this.teamScoreColumnList) {
        if (!this.showColumnByName.includes(item.option)) {
          this.showColumnByName.push(item.option);
        }
        item.checked = true;
      }
    } else {
      this.selectAllColumnFlag = false;
      this.teamScoreColumnList.forEach(item => {
        item.checked = false;
      });
      this.showColumnByName = [];
    }
  }

  resetColumnAll() {
    this.showColumnByName = [];
    this.teamScoreColumnList.forEach(item => {
      item.checked = false;
    });
    this.teamtonedotedflag = true;

    this.ngAfterViewInit();
  }

  showColumnsByName(obj, targetIndex) {
    if (this.showColumnByName.indexOf(obj) == -1) {
      this.teamScoreColumnList[targetIndex].checked = true;
      this.showColumnByName.push(obj);
    } else {
      if (this.teamScoreColumnList[targetIndex].checked) {
        this.teamScoreColumnList[targetIndex].checked = false;
        for (var i = 0; i < this.showColumnByName.length; i++) {
          var data = this.showColumnByName[i];
          if (obj == data) {
            this.showColumnByName.splice(i, 1);
          }
        }
      } else {
        this.teamScoreColumnList[targetIndex].checked = true;
      }
    }
    this.selectAllColumnFlag =
      this.teamScoreColumnList.length == this.showColumnByName.length
        ? true
        : false;
  }
  /*******End of grid column*********/

  ngAfterViewInit() {
    var requestObj = new BaseContainer();
    requestObj["employeeId"] = this.employeeId;
    requestObj["companyId"] = this.companyId;
    requestObj["searchText"] = this.searchtext;
    requestObj["searchTextClient"] = this.searchtextClient;
    requestObj["searchTextOrg"] = this.searchtextOrg;
    requestObj["clientEmailId"] = this.clientEmailId;
    // requestObj['pageNumber']= count;
    requestObj["identify"] = "all";

    if (this.searchtext != "" && this.searchtext != null) {
      this.httpService
        .postRequest<EmployeeDashboard>(
          requestObj,
          "app/auth/listSortedEmailOnToneClk"
        )
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              console.log(this.dashboard);
              this.clientRelation = this.dashboard["relationWithClient"];
              this.teamRelation = this.dashboard["relationWithTeam"];
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
        });
    } else if (this.companyId != "" && this.searchtextOrg == undefined) {
      this.httpService
        .postRequest<ClientDashboard>(requestObj, "app/auth/getOrgEmails")
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              console.log("in client email service");
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
        });
    } else if (this.searchtextOrg != "" && this.searchtextOrg != null) {
      this.httpService
        .postRequest<EmployeeDashboard>(
          requestObj,
          "app/auth/listSortedEmailOnToneClkOrg"
        )
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              console.log(this.dashboard);
              this.clientRelation = this.dashboard["relationWithClient"];
              this.teamRelation = this.dashboard["relationWithTeam"];
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
        });
    } else if (
      this.clientEmailId != "" &&
      this.clientEmailId != null &&
      this.searchtextClient == undefined
    ) {
      this.httpService
        .postRequest<ClientDashboard>(requestObj, "app/auth/getClientEmails")
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              console.log("in client email service");
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
        });
    } else if (this.searchtextClient != "" && this.searchtextClient != null) {
      this.httpService
        .postRequest<EmployeeDashboard>(
          requestObj,
          "app/auth/listSortedEmailOnToneClkClient"
        )
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              console.log(this.dashboard);
              this.clientRelation = this.dashboard["relationWithClient"];
              this.teamRelation = this.dashboard["relationWithTeam"];
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
        });
    }
    //   else if(this.adverseFilter != "" && this.adverseFilter != null){
    //     this.httpService.postRequest<EmployeeDashboard>(requestObj,'app/auth/getEmpTeamEmail').subscribe(
    //         employeedashboard=> {
    //             if(employeedashboard.rsBody.result=='success')
    //             {
    //                 if(employeedashboard.rsBody.msg != "success")
    //                 {
    //                   this.dashboard = employeedashboard.rsBody.msg;
    //                   this.clientRelation = this.dashboard['relationWithClient'];
    //                   this.teamRelation = this.dashboard['relationWithTeam'];
    //                   this.EmployeeTeamEmail = this.dashboard['listEmailAnalyser'];
    //                   //this.EmployeeTeamEmail = employeedashboard.rsBody.msg;
    //                   // console.log("Hello");
    //                   console.log(this.dashboard);
    //                 if(this.EmployeeTeamEmail.length > 0)
    //                 {
    //                     for(var i=0; i < this.EmployeeTeamEmail.length; i++)
    //                     {
    //                         if(this.EmployeeTeamEmail[i]['anger'] !=0 && this.EmployeeTeamEmail[i]['anger'] !=null){
    //                             this.EmployeeTeamEmail[i]['anger'] = "anger";
    //                         }
    //                         if(this.EmployeeTeamEmail[i]['joy'] !=0 && this.EmployeeTeamEmail[i]['joy'] !=null){
    //                             this.EmployeeTeamEmail[i]['joy'] = "joy";
    //                         }
    //                         if(this.EmployeeTeamEmail[i]['sadness'] !=0 && this.EmployeeTeamEmail[i]['sadness'] !=null){
    //                             this.EmployeeTeamEmail[i]['sadness'] = "sadness";
    //                         }
    //                         if(this.EmployeeTeamEmail[i]['fear'] !=0 && this.EmployeeTeamEmail[i]['fear'] !=null){
    //                             this.EmployeeTeamEmail[i]['fear'] = "fear";
    //                         }
    //                         if(this.EmployeeTeamEmail[i]['tentative'] !=0 && this.EmployeeTeamEmail[i]['tentative'] !=null){
    //                             this.EmployeeTeamEmail[i]['tentative'] = "tentative";
    //                         }
    //                         if(this.EmployeeTeamEmail[i]['confident'] !=0 && this.EmployeeTeamEmail[i]['confident'] !=null){
    //                             this.EmployeeTeamEmail[i]['confident'] = "confident";
    //                         }
    //                         if(this.EmployeeTeamEmail[i]['analytical'] !=0 && this.EmployeeTeamEmail[i]['analytical'] !=null){
    //                             this.EmployeeTeamEmail[i]['analytical'] = "analytical";
    //                         }
    //                         if(this.EmployeeTeamEmail[i]['escalated'] == null || this.EmployeeTeamEmail[i]['escalated'] != "yes"){
    //                           this.EmployeeTeamEmail[i]['escalated'] = 'no';
    //                       }

    //                       if(this.EmployeeTeamEmail[i]['adverse'] == null || this.EmployeeTeamEmail[i]['adverse'] != "yes"){
    //                         this.EmployeeTeamEmail[i]['adverse'] = 'no';
    //                     }
    //                     }
    //                 }
    //                 this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
    //                 }

    //             }
    //             this.adverseFlag = true;
    //             this.toggleAdverse();
    //         });

    //         this.cdRef.detectChanges();
    //   }
    else {
      this.httpService
        .postRequest<EmployeeDashboard>(requestObj, "app/auth/getEmpTeamEmail")
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              this.clientRelation = this.dashboard["relationWithClient"];
              this.teamRelation = this.dashboard["relationWithTeam"];
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              //this.EmployeeTeamEmail = employeedashboard.rsBody.msg;
              // console.log("Hello");
              console.log(this.dashboard);
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
          if (this.adverseFilter != "" && this.adverseFilter != null) {
            this.adverseFlag = true;
            this.toggleAdverse();
          }
          if (this.escalationFilter != "" && this.escalationFilter != null) {
            this.escalationFlag = true;
            this.toggleEscalation();
          }
        });
      this.clearFlags();
    }
  }

  public config: PaginationInstance = {
    id: "custom",
    itemsPerPage: 10,
    currentPage: 1
  };

  clearFlags() {
    this.emailSubjectFlag = true;
    this.employeeFlag = true;
    this.clientFlag = true;
    this.orgFlag = true;
    this.resultFlag = true;
    this.searchbyDateflag = true;
    this.searchbyTimeflag = true;
    this.teamtonedotedflag = true;
   // this.teamtoneflag = true;
    // this.teamColDottedFlag = true;
  }

  changRoute(name, tabName) {
    //this.sharedService.setData({'teamemail':tabName, 'userId': this.employeeId, 'empHir': this.backList});
    //this.router.navigate([name]);
    window.history.back();
  }

  changRouteEmployee(name, tabName) {
    this.sharedService.setData({ tabName: tabName });
    this.router.navigate([name]);
  }

  changRoutePersonalTeam(name, tabName) {
    this.router.navigate([name, this.employeeId]);
  }

  teamtoneemployeeAdverse(obj) {
    var requestObj = new BaseContainer();
    requestObj["employeeId"] = this.employeeId;
    //   requestObj['companyId'] = this.companyId;
    //   requestObj['searchText']= this.searchtext;
    //   requestObj['searchTextClient'] = this.searchtextClient;
    //   requestObj['searchTextOrg'] = this.searchtextOrg;
    //   requestObj['clientEmailId'] = this.clientEmailId;
    // requestObj['pageNumber']= count;
    requestObj["identify"] = "all";

    this.httpService
      .postRequest<EmployeeDashboard>(requestObj, "app/auth/getEmpTeamEmail")
      .subscribe(employeedashboard => {
        if (employeedashboard.rsBody.result == "success") {
          if (employeedashboard.rsBody.msg != "success") {
            this.dashboard = employeedashboard.rsBody.msg;
            this.clientRelation = this.dashboard["relationWithClient"];
            this.teamRelation = this.dashboard["relationWithTeam"];
            this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
            //this.EmployeeTeamEmail = employeedashboard.rsBody.msg;
            // console.log("Hello");
            console.log(this.dashboard);
            if (this.EmployeeTeamEmail.length > 0) {
              for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                if (
                  this.EmployeeTeamEmail[i]["anger"] != 0 &&
                  this.EmployeeTeamEmail[i]["anger"] != null
                ) {
                  this.EmployeeTeamEmail[i]["anger"] = "anger";
                }
                if (
                  this.EmployeeTeamEmail[i]["joy"] != 0 &&
                  this.EmployeeTeamEmail[i]["joy"] != null
                ) {
                  this.EmployeeTeamEmail[i]["joy"] = "joy";
                }
                if (
                  this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                  this.EmployeeTeamEmail[i]["sadness"] != null
                ) {
                  this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                }
                if (
                  this.EmployeeTeamEmail[i]["fear"] != 0 &&
                  this.EmployeeTeamEmail[i]["fear"] != null
                ) {
                  this.EmployeeTeamEmail[i]["fear"] = "fear";
                }
                if (
                  this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                  this.EmployeeTeamEmail[i]["tentative"] != null
                ) {
                  this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                }
                if (
                  this.EmployeeTeamEmail[i]["confident"] != 0 &&
                  this.EmployeeTeamEmail[i]["confident"] != null
                ) {
                  this.EmployeeTeamEmail[i]["confident"] = "confident";
                }
                if (
                  this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                  this.EmployeeTeamEmail[i]["analytical"] != null
                ) {
                  this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                }
                if (
                  this.EmployeeTeamEmail[i]["escalated"] == null ||
                  this.EmployeeTeamEmail[i]["escalated"] != "yes"
                ) {
                  this.EmployeeTeamEmail[i]["escalated"] = "no";
                }

                if (
                  this.EmployeeTeamEmail[i]["adverse"] == null ||
                  this.EmployeeTeamEmail[i]["adverse"] != "yes"
                ) {
                  this.EmployeeTeamEmail[i]["adverse"] = "no";
                }
              }
            }
            this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
          }
        }
        this.searchtextClient = "";
        this.searchtextOrg = "";
        this.clientEmailId = "";
        this.companyName = "";
        this.escalationFilter = "";
        this.adverseFilter = "yes";
        this.adverseFlag = true;
        this.toggleAdverse();
      });
  }

  //   onPageChange(e)
  //   {
  //     if (e)
  //       this.page = e;
  //   }

  //   previousPage(){
  //       this.p--;

  //   }

  //   nextPage(){
  //       this.p++;
  //   }

  excelExportSubmit() {
    this.clearFlags;
    var count = 0;
    var resObj = new BaseContainer();
    // resObj = this.temp;
    resObj["employeeId"] = this.employeeId;
    resObj["identify"] = "all";
    resObj["pageNumber"] = count;
    this.httpService
      .postRequest<EmployeeDashboard>(
        resObj,
        "app/auth/listPersonalTeamToExcel"
      )
      .subscribe(employeedashboard => {
        if (employeedashboard.rsBody.result == "success") {
          var exeFile = employeedashboard.rsBody.msg;
          $("#ExportExcelData").attr("href", exeFile);
          $("#ExportExcelData")[0].click();
        }
      });
  }

  teamtoneemployeeEscalation(obj) {
    var requestObj = new BaseContainer();
    requestObj["employeeId"] = this.employeeId;
    //   requestObj['companyId'] = this.companyId;
    //   requestObj['searchText']= this.searchtext;
    //   requestObj['searchTextClient'] = this.searchtextClient;
    //   requestObj['searchTextOrg'] = this.searchtextOrg;
    //   requestObj['clientEmailId'] = this.clientEmailId;
    // requestObj['pageNumber']= count;
    requestObj["identify"] = "all";

    this.httpService
      .postRequest<EmployeeDashboard>(requestObj, "app/auth/getEmpTeamEmail")
      .subscribe(employeedashboard => {
        if (employeedashboard.rsBody.result == "success") {
          if (employeedashboard.rsBody.msg != "success") {
            this.dashboard = employeedashboard.rsBody.msg;
            this.clientRelation = this.dashboard["relationWithClient"];
            this.teamRelation = this.dashboard["relationWithTeam"];
            this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
            //this.EmployeeTeamEmail = employeedashboard.rsBody.msg;
            // console.log("Hello");
            console.log(this.dashboard);
            if (this.EmployeeTeamEmail.length > 0) {
              for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                if (
                  this.EmployeeTeamEmail[i]["anger"] != 0 &&
                  this.EmployeeTeamEmail[i]["anger"] != null
                ) {
                  this.EmployeeTeamEmail[i]["anger"] = "anger";
                }
                if (
                  this.EmployeeTeamEmail[i]["joy"] != 0 &&
                  this.EmployeeTeamEmail[i]["joy"] != null
                ) {
                  this.EmployeeTeamEmail[i]["joy"] = "joy";
                }
                if (
                  this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                  this.EmployeeTeamEmail[i]["sadness"] != null
                ) {
                  this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                }
                if (
                  this.EmployeeTeamEmail[i]["fear"] != 0 &&
                  this.EmployeeTeamEmail[i]["fear"] != null
                ) {
                  this.EmployeeTeamEmail[i]["fear"] = "fear";
                }
                if (
                  this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                  this.EmployeeTeamEmail[i]["tentative"] != null
                ) {
                  this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                }
                if (
                  this.EmployeeTeamEmail[i]["confident"] != 0 &&
                  this.EmployeeTeamEmail[i]["confident"] != null
                ) {
                  this.EmployeeTeamEmail[i]["confident"] = "confident";
                }
                if (
                  this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                  this.EmployeeTeamEmail[i]["analytical"] != null
                ) {
                  this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                }
                if (
                  this.EmployeeTeamEmail[i]["escalated"] == null ||
                  this.EmployeeTeamEmail[i]["escalated"] != "yes"
                ) {
                  this.EmployeeTeamEmail[i]["escalated"] = "no";
                }

                if (
                  this.EmployeeTeamEmail[i]["adverse"] == null ||
                  this.EmployeeTeamEmail[i]["adverse"] != "yes"
                ) {
                  this.EmployeeTeamEmail[i]["adverse"] = "no";
                }
              }
            }
            this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
          }
        }
        this.searchtextClient = "";
        this.searchtextOrg = "";
        this.clientEmailId = "";
        this.companyName = "";
        this.adverseFilter = "";
        this.escalationFilter = "yes";
        this.escalationFlag = true;
        this.toggleEscalation();
      });
  }

  ngOnDestroy() {}

  // all filter
  getClickAll() {
    $(".filteractivesent").css("background-color", "#e9e9e9");
    $(".filteractiveall").css("background-color", "#f5f5f5");
    $(".filteractivereceive").css("background-color", "#e9e9e9");

    this.searchbyallsentreceive("all");
  }

  getClickAllClient() {
    $(".filteractivesent").css("background-color", "#e9e9e9");
    $(".filteractiveall").css("background-color", "#f5f5f5");
    $(".filteractivereceive").css("background-color", "#e9e9e9");

    this.searchbyallsentreceiveclient("all");
  }

  // sent filter
  getClickSent() {
    $(".filteractivesent").css("background-color", "#f5f5f5");
    $(".filteractiveall").css("background-color", "#e9e9e9");
    $(".filteractivereceive").css("background-color", "#e9e9e9");
    this.searchbyallsentreceive("sent");
  }

  // sent filter
  getClickSentClient() {
    $(".filteractivesent").css("background-color", "#f5f5f5");
    $(".filteractiveall").css("background-color", "#e9e9e9");
    $(".filteractivereceive").css("background-color", "#e9e9e9");
    this.searchbyallsentreceiveclient("sent");
  }

  // receive filter
  getClickReceive() {
    $(".filteractivesent").css("background-color", "#e9e9e9");
    $(".filteractiveall").css("background-color", "#e9e9e9");
    $(".filteractivereceive").css("background-color", "#f5f5f5");
    this.searchbyallsentreceive("receive");
  }

  // receive filter
  getClickReceiveClient() {
    $(".filteractivesent").css("background-color", "#e9e9e9");
    $(".filteractiveall").css("background-color", "#e9e9e9");
    $(".filteractivereceive").css("background-color", "#f5f5f5");
    this.searchbyallsentreceiveclient("receive");
  }

  emailSubjectSearch() {
    if (this.emailSubjectFlag == true) {
      this.emailSubjectFlag = false;
    } else {
      this.emailSubjectFlag = true;
    }
  }

  searchSubject(obj) {
    console.log("in subject team email ");
    this.clearFlags();
    let EmployeeTeamEmailTemp: Array<Object> = [];
    let EmployeeTeamEmailTempMain: Array<Object> = [];
    for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
      if (this.EmployeeTeamEmail[i].subject.length != 0) {
        // let tempClient: Array<String> = this.EmployeeTeamEmailMain[i].toClientNames;

        if (
          this.EmployeeTeamEmail[i].subject
            .toString()
            .toLowerCase()
            .indexOf(obj.subject.toString().toLowerCase()) > -1
        ) {
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);

          console.log(this.EmployeeTeamEmail);
        }
      }
    }
    if (EmployeeTeamEmailTemp.length == 0) {
      this.EmployeeTeamEmail = [];
      this.resultFlag = false;
    } else {
      this.EmployeeTeamEmail = EmployeeTeamEmailTemp;
    }
    this.emailSubjectFlag = true;
  }

  employeeSerachSubmit() {
    if (this.employeeFlag == true) {
      this.employeeFlag = false;
    } else {
      this.employeeFlag = true;
    }
  }

  searchByEmp() {
    if (this.employeeFlag == true) {
      this.employeeFlag = false;
    } else {
      this.employeeFlag = true;
    }
  }

  clientSerachSubmit() {
    if (this.clientFlag == true) {
      this.clientFlag = false;
    } else {
      this.clientFlag = true;
    }
  }

  searchByOrg() {
    if (this.orgFlag == true) {
      this.orgFlag = false;
    } else {
      this.orgFlag = true;
    }
  }

  searchEmp(orgObj) {
    console.log("in search emp");
    this.clearFlags();
    let EmployeeTeamEmailTemp: Array<Object> = [];
    for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
      if (this.EmployeeTeamEmail[i].from.length != 0) {
        let tempEmp: String = this.EmployeeTeamEmail[i].from;
        if (
          tempEmp
            .toString()
            .toLowerCase()
            .indexOf(orgObj.EmpName.toString().toLowerCase()) > -1
        ) {
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);

          console.log(this.EmployeeTeamEmail);
        }
      } else if (this.EmployeeTeamEmail[i].toEmployeeNames.length != 0) {
        let tempEmp: Array<String> = this.EmployeeTeamEmail[i].toEmployeeNames;
        for (var j = 0; j < tempEmp.length; j++) {
          if (
            tempEmp[j]
              .toString()
              .toLowerCase()
              .indexOf(orgObj.EmpName.toString().toLowerCase()) > -1
          ) {
            EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);

            console.log(this.EmployeeTeamEmail);
          }
        }
      }
    }

    if (EmployeeTeamEmailTemp.length == 0) {
      this.EmployeeTeamEmail = [];
      this.resultFlag = false;
    } else {
      this.EmployeeTeamEmail = EmployeeTeamEmailTemp;
    }
    this.employeeFlag = true;
  }

  searchClient(obj) {
    console.log("in search client");
    this.clearFlags();
    let EmployeeTeamEmailTemp: Array<Object> = [];
    let EmployeeTeamEmailTempMain: Array<Object> = [];
    for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
      if (this.EmployeeTeamEmail[i].toClientNames.length != 0) {
        let tempClient: Array<String> = this.EmployeeTeamEmail[i].toClientNames;
        for (var j = 0; j < tempClient.length; j++) {
          if (
            tempClient[j]
              .toString()
              .toLowerCase()
              .indexOf(obj.clientName.toString().toLowerCase()) > -1
          ) {
            EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);

            console.log(this.EmployeeTeamEmail);
          }
        }
      } else if (this.EmployeeTeamEmail[i].senderName != null) {
        let tempClient: String = this.EmployeeTeamEmail[i].senderName;
        if (
          tempClient
            .toString()
            .toLowerCase()
            .indexOf(obj.clientName)
        ) {
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);

          console.log(this.EmployeeTeamEmail);
        }
      }
    }
    if (EmployeeTeamEmailTemp.length == 0) {
      this.EmployeeTeamEmail = [];
      this.resultFlag = false;
    } else {
      this.EmployeeTeamEmail = EmployeeTeamEmailTemp;
    }
    this.clientFlag = true;
  }

  searchOrg(obj) {
    console.log("in org search");
    this.clearFlags();
    let EmployeeTeamEmailTemp: Array<Object> = [];
    for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
      if (this.EmployeeTeamEmail[i].companyName != null) {
        if (
          this.EmployeeTeamEmail[i].companyName
            .toString()
            .toLowerCase()
            .indexOf(obj.OrgName.toString().toLowerCase()) > -1
        ) {
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
          console.log(this.EmployeeTeamEmail);
        }
      }
    }
    if (EmployeeTeamEmailTemp.length == 0) {
      this.EmployeeTeamEmail = [];
      this.resultFlag = false;
    } else {
      this.EmployeeTeamEmail = EmployeeTeamEmailTemp;
    }
    this.orgFlag = true;
  }

  toggleEscalation() {
    let EmployeeTeamEmailTemp: Array<Object> = [];
    console.log(this.escalationFlag);
    if (this.escalationFlag == true) {
      for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
        if (
          this.EmployeeTeamEmail[i]["escalated"] != null &&
          this.EmployeeTeamEmail[i]["escalated"] == "yes"
        ) {
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
        }
      }
      if (EmployeeTeamEmailTemp.length == 0) {
        this.EmployeeTeamEmail = [];
        this.resultFlag = false;
      } else {
        this.EmployeeTeamEmail = EmployeeTeamEmailTemp;
        this.clearFlags();
      }
      this.escalationFlag = false;
    } else {
      this.EmployeeTeamEmail = this.EmployeeTeamEmailMain;
      this.escalationFlag = true;
    }
  }

  changRouteTouch(name) {
    this.router.navigate([name]);
  }

  toggleAdverse() {
    let EmployeeTeamEmailTemp: Array<Object> = [];
    console.log(this.adverseFlag);
    if (this.adverseFlag == true) {
      for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
        if (
          this.EmployeeTeamEmail[i]["adverse"] != null &&
          this.EmployeeTeamEmail[i]["adverse"] == "yes"
        ) {
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
        }
      }
      if (EmployeeTeamEmailTemp.length == 0) {
        this.EmployeeTeamEmail = [];
        this.resultFlag = false;
      } else {
        this.EmployeeTeamEmail = EmployeeTeamEmailTemp;
        this.clearFlags();
      }
      this.adverseFlag = false;
    } else {
      this.EmployeeTeamEmail = this.EmployeeTeamEmailMain;
      this.adverseFlag = true;
    }
  }

  searchByDateSubmit() {
    if (this.searchbyDateflag == true) {
      this.searchbyDateflag = false;
    } else {
      this.searchbyDateflag = true;
    }
  }
  searchByTimeSubmit() {
    if (this.searchbyTimeflag == true) {
      this.searchbyTimeflag = false;
    } else {
      this.searchbyTimeflag = true;
    }
  }

  teamdotteddropdown() {
    if (this.teamtonedotedflag == true) {
      this.teamtonedotedflag = false;
    } else {
      this.teamtonedotedflag = true;
    }
  }

  teamtonedropdown() {
    if (this.teamtoneflag == true) {
      this.teamtoneflag = false;
    } else {
      this.teamtoneflag = true;
    }
  }

  startTime() {
    var input = $("#startTime");
    input.clockpicker({
      autoclose: true
    });
  }
  // end time
  endTime() {
    var input = $("#endTime");
    input.clockpicker({
      autoclose: true
    });
  }

  datePickerFrom() {
    setTimeout(() => {
      $(".datepickerform").datepicker({ dateFormat: "yy-mm-dd" });
    }, 100);
  }

  datePickerTo() {
    setTimeout(() => {
      $(".datepickerto").datepicker({ dateFormat: "yy-mm-dd" });
    }, 100);
  }

  searchByDateFromTO() {
    let EmployeeTeamEmailTemp: Array<Object> = [];
    var toneDateFrom: Date = new Date(this.orgObj.fromDate);
    var toneDateTo: Date = new Date(this.orgObj.upto);
    for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
      var tempDate: Date = new Date(this.EmployeeTeamEmail[i].date);
      if (tempDate >= toneDateFrom && tempDate <= toneDateTo) {
        EmployeeTeamEmailTemp.push(this.EmployeeTeamEmailMain[i]);
      }
    }

    if (EmployeeTeamEmailTemp.length == 0) {
      this.EmployeeTeamEmail = [];
      this.resultFlag = false;
    } else {
      this.EmployeeTeamEmail = EmployeeTeamEmailTemp;
    }
    this.searchbyDateflag = true;
  }

  selectScoreAll() {
    if (this.selectAllflag == true) {
      this.selectAllflag = false;
      this.teamScoreListDotted.forEach(item => {
        this.serachteamtonedotted.push(item.option);
        item.checked = true;
        console.log(item);
      });
    } else {
      this.selectAllflag = true;

      this.teamScoreListDotted.forEach(item => {
        item.checked = false;
      });

      this.serachteamtonedotted = [];
    }
  }

  searchbyateamtonedotted(obj, targetIndex) {
    if (this.serachteamtonedotted.indexOf(obj) == -1) {
      this.teamScoreListDotted[targetIndex].checked = true;
      this.serachteamtonedotted.push(obj);
    } else {
      this.teamScoreListDotted[targetIndex].checked = false;
      for (var i = 0; i < this.serachteamtonedotted.length; i++) {
        var data = this.serachteamtonedotted[i];

        if (obj == data) {
          this.serachteamtonedotted.splice(i, 1);
        }
      }
    }
  }

  applyTeamfilterDotted() {
    this.clearFlags();
    var tempTone: Array<String> = [];
    let EmployeeTeamEmailTemp: Array<Object> = [];
    for (var i = 0; i < this.teamScoreListDotted.length; i++) {
      if (this.teamScoreListDotted[i].checked == true) {
        tempTone.push(this.teamScoreListDotted[i].option);
      }
    }

    console.log(tempTone);
    var countTone: number = 0;
    for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
      countTone = 0;
      // console.log(this.EmployeeTeamEmail[i]['joy']);
      // console.log(this.EmployeeTeamEmail[i].joy);
      if (this.EmployeeTeamEmail[i].anger != 0) {
        if (tempTone.indexOf("anger") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
          console.log("detected anger");
          continue;
        }
      }
      if (this.EmployeeTeamEmail[i].joy != 0) {
        if (tempTone.indexOf("joy") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
          console.log("detected joy");
          continue;
        }
      }
      if (this.EmployeeTeamEmail[i].sadness != 0) {
        if (tempTone.indexOf("sadness") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
          console.log("detected sadness");
          continue;
        }
      }
      if (this.EmployeeTeamEmail[i].fear != 0) {
        if (tempTone.indexOf("fear") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
          console.log("detected fear");
          continue;
        }
      }
      if (this.EmployeeTeamEmail[i].tentative != 0) {
        if (tempTone.indexOf("tentative") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
          console.log("detected tentative");
          continue;
        }
      }
      if (this.EmployeeTeamEmail[i].confident != 0) {
        if (tempTone.indexOf("confident") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
          console.log("detected confident");
          continue;
        }
      }
      if (this.EmployeeTeamEmail[i].analytical != 0) {
        if (tempTone.indexOf("analytical") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.EmployeeTeamEmail[i]);
          console.log("detected analytical");
          continue;
        }
      }

      // if(tempTone.length ==  countTone){
      //     EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
      // }
    }

    if (EmployeeTeamEmailTemp.length == 0) {
      this.EmployeeTeamEmail = [];
      this.resultFlag = false;
    } else {
      this.EmployeeTeamEmail = EmployeeTeamEmailTemp;
    }
    this.teamtoneflag = true;
    this.clientFlag = true;
  }

  adverseevents() {
    if (this.adverseEventFlag == true) {
      this.teamScoreListDotted[0].checked = true;
      this.applyTeamfilterDotted();
      this.adverseEventFlag = false;
    } else {
      this.EmployeeTeamEmail = this.EmployeeTeamEmailMain;
      this.adverseEventFlag = true;
      this.resultFlag = true;
    }
  }

  clearAll() {
    this.EmployeeTeamEmail = this.EmployeeTeamEmailMain;
    this.clearFlags();
  }

  // comman search filter
  searchbyallsentreceive(obj) {
    var searchObject = new BaseContainer();
    searchObject["employeeId"] = this.employeeId;
    searchObject["identify"] = obj;
    searchObject["searchText"] = this.searchtext;

    if (this.searchtext != "" && this.searchtext != null) {
      this.httpService
        .postRequest<EmployeeDashboard>(
          searchObject,
          "app/auth/listSortedEmailOnToneClk"
        )
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              this.clientRelation = this.dashboard["relationWithClient"];
              this.teamRelation = this.dashboard["relationWithTeam"];
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
        });
    } else {
      this.httpService
        .postRequest<EmployeeDashboard>(
          searchObject,
          "app/auth/getEmpTeamEmail"
        )
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              this.clientRelation = this.dashboard["relationWithClient"];
              this.teamRelation = this.dashboard["relationWithTeam"];
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              //this.EmployeeTeamEmail = employeedashboard.rsBody.msg;
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
        });
    }
  }

  getSubject(i) {
    if (
      this.EmployeeTeamEmail[i]["subject"] != null &&
      this.EmployeeTeamEmail[i]["subject"] != undefined
    ) {
      return this.EmployeeTeamEmail[i]["subject"];
    } else {
      return "";
    }
  }

  // comman search filter
  searchbyallsentreceiveclient(obj) {
    var searchObject = new BaseContainer();
    searchObject["clientEmailId"] = this.clientEmailId;
    // searchObject['employeeId'] = this.employeeId;
    searchObject["identify"] = obj;
    searchObject["searchTextClient"] = this.searchtextClient;

    if (this.searchtextClient != "" && this.searchtextClient != null) {
      this.httpService
        .postRequest<EmployeeDashboard>(
          searchObject,
          "app/auth/listSortedEmailOnToneClkClient"
        )
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              this.clientRelation = this.dashboard["relationWithClient"];
              this.teamRelation = this.dashboard["relationWithTeam"];
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
        });
    } else if (this.clientEmailId != null) {
      this.httpService
        .postRequest<EmployeeDashboard>(
          searchObject,
          "app/auth/getClientEmails"
        )
        .subscribe(employeedashboard => {
          if (employeedashboard.rsBody.result == "success") {
            if (employeedashboard.rsBody.msg != "success") {
              this.dashboard = employeedashboard.rsBody.msg;
              this.clientRelation = this.dashboard["relationWithClient"];
              this.teamRelation = this.dashboard["relationWithTeam"];
              this.EmployeeTeamEmail = this.dashboard["listEmailAnalyser"];
              //this.EmployeeTeamEmail = employeedashboard.rsBody.msg;
              if (this.EmployeeTeamEmail.length > 0) {
                for (var i = 0; i < this.EmployeeTeamEmail.length; i++) {
                  if (
                    this.EmployeeTeamEmail[i]["anger"] != 0 &&
                    this.EmployeeTeamEmail[i]["anger"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["anger"] = "anger";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["joy"] != 0 &&
                    this.EmployeeTeamEmail[i]["joy"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["joy"] = "joy";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["sadness"] != 0 &&
                    this.EmployeeTeamEmail[i]["sadness"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["sadness"] = "sadness";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["fear"] != 0 &&
                    this.EmployeeTeamEmail[i]["fear"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["fear"] = "fear";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["tentative"] != 0 &&
                    this.EmployeeTeamEmail[i]["tentative"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["tentative"] = "tentative";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["confident"] != 0 &&
                    this.EmployeeTeamEmail[i]["confident"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["confident"] = "confident";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["analytical"] != 0 &&
                    this.EmployeeTeamEmail[i]["analytical"] != null
                  ) {
                    this.EmployeeTeamEmail[i]["analytical"] = "analytical";
                  }
                  if (
                    this.EmployeeTeamEmail[i]["escalated"] == null ||
                    this.EmployeeTeamEmail[i]["escalated"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["escalated"] = "no";
                  }

                  if (
                    this.EmployeeTeamEmail[i]["adverse"] == null ||
                    this.EmployeeTeamEmail[i]["adverse"] != "yes"
                  ) {
                    this.EmployeeTeamEmail[i]["adverse"] = "no";
                  }
                }
              }
              this.EmployeeTeamEmailMain = this.EmployeeTeamEmail;
            }
          }
        });
    }
  }
}
