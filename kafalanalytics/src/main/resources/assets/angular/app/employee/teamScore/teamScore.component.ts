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
import { TeamScore } from "./teamScore";
import { BaseContainer } from "../../BaseContainer";
import { SharedService } from "../../service/shared-data-service";
import { ChangeDetectorRef } from "@angular/core";
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { PaginationInstance } from "ngx-pagination";
declare var $: any;

var path = require("path");

var _publicPath = path.resolve(__dirname, "../../../../../../webapp");

var _templateURL = "templates/employee/teamScore.component.html";

@Component({
  selector: "app-root",
  templateUrl: _templateURL
})
export class TeamScoreComponent implements OnInit, OnDestroy, AfterViewInit {
  private teamScore: object = {};
  private employeeTeamScore: object = {};
  private toneOfPersonalMail: object = {};
  private listEmailAnalyser: any = [];
  private listEmailAnalyserMain: any = [];
  private listOfEmployee: any = [];
  private orgObj: Object = {};
  private employeeId: Object = {};
  private serObj: Object = {};
  private widthstyle: Object = {};
  public levelup: any = [];
  public teamRelation: any = {};
  public clientRelation: any = {};
  public tempData: Object = {};
  public parmsData: Object = {};
  public adverseEventFlag: boolean = true;
  public angerCount: number = 0;
  public joyCount: number = 0;
  public sadnessCount: number = 0;
  public fearCount: number = 0;
  public tentativeCount: number = 0;
  public confidentCount: number = 0;
  public analyticalCount: number = 0;
  public totalCount: number = 0;
  public serachteamtone: any = [];
  public serachteamtonedotted: any = [];
  public nameteamFlag: boolean = true;
  public resultFlag: boolean = true;
  public escalationFlag: boolean = true;
  public adverseFlag: boolean = true;
  public emailSubjectFlag: boolean = true;
  public employeeFlag: boolean = true;
  public clientFlag: boolean = true;
  public searchbyDateflag: boolean = true;
  public searchbyTimeflag: boolean = true;
  public teamtonedotedflag: boolean = true;
  public teamtoneflag: boolean = true;
  public selectAllflag: boolean = true;
  public selectAllColumnFlag: boolean = false;
  public teamColDottedFlag: boolean = true;
  public showColumnByName: any = ["employee", "client", "sentreceive"];
  public temp: Object = {};
  public searchByName: Object = {};
  public p: number = 1;
  @Input()
  id: string;
  @Input()
  page;
  @Input()
  maxSize: number;

  @Output()
  pageChange: EventEmitter<number> = new EventEmitter<number>();
  constructor(
    private httpService: HttpService,
    private router: Router,
    private sharedService: SharedService,
    private _sanitizer: DomSanitizer,
    private route: ActivatedRoute,
    private cdRef: ChangeDetectorRef
  ) {}
  teamScoreColumnList = [
    { id: 1, name: "Employee", option: "employee" },
    { id: 2, name: "Client", option: "client" },
    { id: 3, name: "CC", option: "cc" },
    { id: 4, name: "Date/Time", option: "datetime" },
    { id: 5, name: "Sent/Receive", option: "sentreceive" }
  ];
  teamScoreList = [
    { id: 1, name: "Anger", option: "anger", color: "text-danger" },
    { id: 2, name: "Joy", option: "joy", color: "text-success" },
    { id: 3, name: "Sadness", option: "sadness", color: "text-secondary" },
    { id: 4, name: "Fear", option: "fear", color: "text-dark" },
    { id: 5, name: "Tentative", option: "tentative", color: "text-warning" },
    { id: 6, name: "Confident", option: "confident", color: "text-purple" },
    { id: 7, name: "Analytical", option: "analytical", color: "text-gray" }
  ];

  teamScoreListDotted = [
    { id: 1, name: "Anger", option: "anger", color: "text-danger" },
    { id: 2, name: "Joy", option: "joy", color: "text-success" },
    { id: 3, name: "Sadness", option: "sadness", color: "text-secondary" },
    { id: 4, name: "Fear", option: "fear", color: "text-dark" },
    { id: 5, name: "Tentative", option: "tentative", color: "text-warning" },
    { id: 6, name: "Confident", option: "confident", color: "text-purple" },
    { id: 7, name: "Analytical", option: "analytical", color: "text-gray" }
  ];
  public userId: any = "";
  public identify: any = "";

  clickedInside($event: Event, colType) {
    $event.preventDefault();
    $event.stopPropagation(); // <- that will stop propagation on lower layers
  }

  @HostListener("document:click", ["$event"])
  clickedOutside($event) {
    this.clearFlags();
  }
  ngOnInit() {
    this.parmsData = this.route.snapshot.params;
    this.userId = this.parmsData["userId"];
  }

  teamGridDropdown($event) {
    $event.stopPropagation();
    if (this.teamColDottedFlag == true) {
      this.teamColDottedFlag = false;
    } else {
      this.teamColDottedFlag = true;
    }
  }
  ngAfterViewInit() {
    var count = 0;
    var requestObj = new BaseContainer();
    requestObj["employeeId"] = this.userId;
    this.employeeId = this.userId;
    requestObj["pageNumber"] = count;
    requestObj["identify"] = "all";
    this.temp = requestObj;
    this.httpService
      .postRequest<TeamScore>(
        requestObj,
        "app/auth/displayemployeeteamdashboard"
      )
      .subscribe(employeepersonalscore => {
        if (employeepersonalscore.rsBody.result == "success") {
          this.employeeTeamScore = employeepersonalscore.rsBody.msg;
          this.teamRelation = this.employeeTeamScore["relationWithTeam"];
          this.clientRelation = this.employeeTeamScore["relationWithClient"];
          this.tempData = this.employeeTeamScore.employeeId;
          this.levelup.push(this.tempData);
          this.teamScore = this.employeeTeamScore.toneOfTeamMail;
          this.toneOfPersonalMail = this.employeeTeamScore.toneOfPersonalMail;
          this.listEmailAnalyser = this.employeeTeamScore.listEmailAnalyser;
          this.listOfEmployee = this.employeeTeamScore.listOfEmployee;

          // bars details
          var total = 0;
          total =
            this.teamScore["anger"] +
            this.teamScore["joy"] +
            this.teamScore["sadness"] +
            this.teamScore["fear"] +
            this.teamScore["tentative"] +
            this.teamScore["confident"] +
            this.teamScore["analytical"];
          var reqTempInfoObj = new BaseContainer();
          reqTempInfoObj["anger"] =
            (this.teamScore["anger"] / total) * 100 + "%";
          reqTempInfoObj["joy"] = (this.teamScore["joy"] / total) * 100 + "%";
          reqTempInfoObj["sadness"] =
            (this.teamScore["sadness"] / total) * 100 + "%";
          reqTempInfoObj["fear"] = (this.teamScore["fear"] / total) * 100 + "%";
          reqTempInfoObj["tentative"] =
            (this.teamScore["tentative"] / total) * 100 + "%";
          reqTempInfoObj["confident"] =
            (this.teamScore["confident"] / total) * 100 + "%";
          reqTempInfoObj["analytical"] =
            (this.teamScore["analytical"] / total) * 100 + "%";
          this.widthstyle = reqTempInfoObj;

          for (var i = 0; i < this.listEmailAnalyser.length; i++) {
            if (
              this.listEmailAnalyser[i]["anger"] != 0 &&
              this.listEmailAnalyser[i]["anger"] != null
            ) {
              this.listEmailAnalyser[i]["anger"] = "anger";
              this.angerCount++;
            }
            if (
              this.listEmailAnalyser[i]["joy"] != 0 &&
              this.listEmailAnalyser[i]["joy"] != null
            ) {
              this.listEmailAnalyser[i]["joy"] = "joy";
              this.joyCount++;
            }
            if (
              this.listEmailAnalyser[i]["sadness"] != 0 &&
              this.listEmailAnalyser[i]["sadness"] != null
            ) {
              this.listEmailAnalyser[i]["sadness"] = "sadness";
              this.sadnessCount++;
            }
            if (
              this.listEmailAnalyser[i]["fear"] != 0 &&
              this.listEmailAnalyser[i]["fear"] != null
            ) {
              this.listEmailAnalyser[i]["fear"] = "fear";
              this.fearCount++;
            }
            if (
              this.listEmailAnalyser[i]["tentative"] != 0 &&
              this.listEmailAnalyser[i]["tentative"] != null
            ) {
              this.listEmailAnalyser[i]["tentative"] = "tentative";
              this.tentativeCount++;
            }
            if (
              this.listEmailAnalyser[i]["confident"] != 0 &&
              this.listEmailAnalyser[i]["confident"] != null
            ) {
              this.listEmailAnalyser[i]["confident"] = "confident";
              this.confidentCount++;
            }
            if (
              this.listEmailAnalyser[i]["analytical"] != 0 &&
              this.listEmailAnalyser[i]["analytical"] != null
            ) {
              this.listEmailAnalyser[i]["analytical"] = "analytical";
              this.analyticalCount++;
            }
            if (
              this.listEmailAnalyser[i]["escalated"] == null ||
              this.listEmailAnalyser[i]["escalated"] != "yes"
            ) {
              this.listEmailAnalyser[i]["escalated"] = "no";
            }

            if (
              this.listEmailAnalyser[i]["adverse"] == null ||
              this.listEmailAnalyser[i]["adverse"] != "yes"
            ) {
              this.listEmailAnalyser[i]["adverse"] = "no";
            }
            console.log(this.listEmailAnalyser);
          }

          this.listEmailAnalyserMain = this.listEmailAnalyser;
          if (this.listEmailAnalyserMain.length > 0) {
            this.toneOfPersonalMail["anger"] = Math.round(
              (this.angerCount / this.listEmailAnalyserMain.length) * 100
            );
            console.log(this.toneOfPersonalMail["anger"]);

            this.toneOfPersonalMail["joy"] = Math.round(
              (this.joyCount / this.listEmailAnalyserMain.length) * 100
            );
            console.log(this.toneOfPersonalMail["joy"]);

            this.toneOfPersonalMail["sadness"] = Math.round(
              (this.sadnessCount / this.listEmailAnalyserMain.length) * 100
            );

            this.toneOfPersonalMail["fear"] = Math.round(
              (this.fearCount / this.listEmailAnalyserMain.length) * 100
            );

            this.toneOfPersonalMail["tentative"] = Math.round(
              (this.tentativeCount / this.listEmailAnalyserMain.length) * 100
            );

            this.toneOfPersonalMail["confident"] = Math.round(
              (this.confidentCount / this.listEmailAnalyserMain.length) * 100
            );

            this.toneOfPersonalMail["analytical"] = Math.round(
              (this.analyticalCount / this.listEmailAnalyserMain.length) * 100
            );
          }
          if (this.listEmailAnalyser.length == 0) {
            this.resultFlag = false;
          }
        }
        this.listEmailAnalyserMain = this.listEmailAnalyser;
      });
    this.cdRef.detectChanges();
  }

  //   public userId:any ='';
  getClickEmployeelist(index) {
    var requestObj2 = new BaseContainer();
    requestObj2["employeeId"] = this.listOfEmployee[index]["employeeId"];
    this.levelup.push(requestObj2["employeeId"]);
    this.employeeId = requestObj2["employeeId"];
    this.getClickEmployeeDetails(requestObj2["employeeId"]);
  }

  clearFlags() {
    this.resultFlag = true;
    this.emailSubjectFlag = true;
    this.employeeFlag = true;
    this.clientFlag = true;
    this.searchbyDateflag = true;
    this.searchbyTimeflag = true;
    this.teamtonedotedflag = true;
    //  this.teamtoneflag = true;
    this.nameteamFlag = true;
    //this.teamColDottedFlag = true;
  }

  changRouteTouch(name) {
    this.router.navigate([name]);
  }

  // employeeeDetails
  getClickEmployeeDetails(obj) {
    var count = 0;
    var requestObj = new BaseContainer();
    requestObj["employeeId"] = obj;
    requestObj["identify"] = "all";
    requestObj["pageNumber"] = count;
    this.httpService
      .postRequest<TeamScore>(
        requestObj,
        "app/auth/displayemployeeteamdashboard"
      )
      .subscribe(employeepersonalscore => {
        if (employeepersonalscore.rsBody.result == "success") {
          this.employeeTeamScore = employeepersonalscore.rsBody.msg;
          this.listOfEmployee = [];
          if (this.employeeTeamScore != "success") {
            this.teamScore = this.employeeTeamScore.toneOfTeamMail;
            this.toneOfPersonalMail = this.employeeTeamScore.toneOfPersonalMail;
            this.listEmailAnalyser = this.employeeTeamScore.listEmailAnalyser;
            this.listOfEmployee = this.employeeTeamScore.listOfEmployee;
            // bars details
            var total = 0;
            total =
              this.teamScore["anger"] +
              this.teamScore["joy"] +
              this.teamScore["sadness"] +
              this.teamScore["fear"] +
              this.teamScore["tentative"] +
              this.teamScore["confident"] +
              this.teamScore["analytical"];
            var reqTempInfoObj = new BaseContainer();
            reqTempInfoObj["anger"] =
              (this.teamScore["anger"] / total) * 100 + "%";
            reqTempInfoObj["joy"] = (this.teamScore["joy"] / total) * 100 + "%";
            reqTempInfoObj["sadness"] =
              (this.teamScore["sadness"] / total) * 100 + "%";
            reqTempInfoObj["fear"] =
              (this.teamScore["fear"] / total) * 100 + "%";
            reqTempInfoObj["tentative"] =
              (this.teamScore["tentative"] / total) * 100 + "%";
            reqTempInfoObj["confident"] =
              (this.teamScore["confident"] / total) * 100 + "%";
            reqTempInfoObj["analytical"] =
              (this.teamScore["analytical"] / total) * 100 + "%";
            this.widthstyle = reqTempInfoObj;

            for (var i = 0; i < this.listEmailAnalyser.length; i++) {
              if (
                this.listEmailAnalyser[i]["anger"] != 0 &&
                this.listEmailAnalyser[i]["anger"] != null
              ) {
                this.listEmailAnalyser[i]["anger"] = "anger";
                this.angerCount++;
              }
              if (
                this.listEmailAnalyser[i]["joy"] != 0 &&
                this.listEmailAnalyser[i]["joy"] != null
              ) {
                this.listEmailAnalyser[i]["joy"] = "joy";
                this.joyCount++;
              }
              if (
                this.listEmailAnalyser[i]["sadness"] != 0 &&
                this.listEmailAnalyser[i]["sadness"] != null
              ) {
                this.listEmailAnalyser[i]["sadness"] = "sadness";
                this.sadnessCount++;
              }
              if (
                this.listEmailAnalyser[i]["fear"] != 0 &&
                this.listEmailAnalyser[i]["fear"] != null
              ) {
                this.listEmailAnalyser[i]["fear"] = "fear";
                this.fearCount++;
              }
              if (
                this.listEmailAnalyser[i]["tentative"] != 0 &&
                this.listEmailAnalyser[i]["tentative"] != null
              ) {
                this.listEmailAnalyser[i]["tentative"] = "tentative";
                this.tentativeCount++;
              }
              if (
                this.listEmailAnalyser[i]["confident"] != 0 &&
                this.listEmailAnalyser[i]["confident"] != null
              ) {
                this.listEmailAnalyser[i]["confident"] = "confident";
                this.confidentCount++;
              }
              if (
                this.listEmailAnalyser[i]["analytical"] != 0 &&
                this.listEmailAnalyser[i]["analytical"] != null
              ) {
                this.listEmailAnalyser[i]["analytical"] = "analytical";
                this.analyticalCount++;
              }
              if (
                this.listEmailAnalyser[i]["escalated"] == null ||
                this.listEmailAnalyser[i]["escalated"] != "yes"
              ) {
                this.listEmailAnalyser[i]["escalated"] = "no";
              }

              if (
                this.listEmailAnalyser[i]["adverse"] == null ||
                this.listEmailAnalyser[i]["adverse"] != "yes"
              ) {
                this.listEmailAnalyser[i]["adverse"] = "no";
              }
            }
            this.listEmailAnalyserMain = this.listEmailAnalyser;
            if (this.listEmailAnalyserMain.length > 0) {
              this.toneOfPersonalMail["anger"] = Math.round(
                (this.angerCount / this.listEmailAnalyserMain.length) * 100
              );
              console.log(this.toneOfPersonalMail["anger"]);

              this.toneOfPersonalMail["joy"] = Math.round(
                (this.joyCount / this.listEmailAnalyserMain.length) * 100
              );
              console.log(this.toneOfPersonalMail["joy"]);

              this.toneOfPersonalMail["sadness"] = Math.round(
                (this.sadnessCount / this.listEmailAnalyserMain.length) * 100
              );

              this.toneOfPersonalMail["fear"] = Math.round(
                (this.fearCount / this.listEmailAnalyserMain.length) * 100
              );

              this.toneOfPersonalMail["tentative"] = Math.round(
                (this.tentativeCount / this.listEmailAnalyserMain.length) * 100
              );

              this.toneOfPersonalMail["confident"] = Math.round(
                (this.confidentCount / this.listEmailAnalyserMain.length) * 100
              );

              this.toneOfPersonalMail["analytical"] = Math.round(
                (this.analyticalCount / this.listEmailAnalyserMain.length) * 100
              );
            }
            if (this.listEmailAnalyser.length == 0) {
              this.resultFlag = false;
            }
          }
        }
      });
    this.cdRef.detectChanges();
  }
  public config: PaginationInstance = {
    id: "custom",
    itemsPerPage: 10,
    currentPage: 1
  };

  changRoute(name, tabName) {
    this.sharedService.setData({ tabName: tabName });
    this.router.navigate([name]);
  }

  changeRoutePersonal(name) {
    this.router.navigate([name, this.userId]);
  }

  teamtoneemployeeAdverse(obj) {
    var empId = this.userId;
    var adverseFilter = "yes";
    //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
    var routObj = new BaseContainer();
    routObj["userId"] = empId;
    routObj["adverseFilter"] = adverseFilter;
    this.router.navigate(["my-team-email", routObj]);
  }

  teamtoneemployeeEscalation(obj) {
    var empId = this.userId;
    var escalationFilter = "yes";
    //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
    var routObj = new BaseContainer();
    routObj["userId"] = empId;
    routObj["escalationFilter"] = escalationFilter;
    this.router.navigate(["my-team-email", routObj]);
  }

  ngOnDestroy() {}

  // typehead
  autocompleListFormatter = (data: any) => {
    //this.setObj = data;
    let html = `<span style='color:black'>${data.subject}</span>`;
    return this._sanitizer.bypassSecurityTrustHtml(html);
  };

  // all filter
  getClickAll() {
    $(".filteractivesent").css("background-color", "#e9e9e9");
    $(".filteractiveall").css("background-color", "#f5f5f5");
    $(".filteractivereceive").css("background-color", "#e9e9e9");

    this.searchbyallsentreceive("all");
  }

  // sent filter
  getClickSent() {
    $(".filteractivesent").css("background-color", "#f5f5f5");
    $(".filteractiveall").css("background-color", "#e9e9e9");
    $(".filteractivereceive").css("background-color", "#e9e9e9");
    this.searchbyallsentreceive("sent");
  }

  // receive filter
  getClickReceive() {
    $(".filteractivesent").css("background-color", "#e9e9e9");
    $(".filteractiveall").css("background-color", "#e9e9e9");
    $(".filteractivereceive").css("background-color", "#f5f5f5");
    this.searchbyallsentreceive("receive");
  }

  toggleEscalation() {
    let EmployeeTeamEmailTemp: Array<Object> = [];
    console.log(this.escalationFlag);
    if (this.escalationFlag == true) {
      for (var i = 0; i < this.listEmailAnalyser.length; i++) {
        if (
          this.listEmailAnalyser[i]["escalated"] != null &&
          this.listEmailAnalyser[i]["escalated"] == "yes"
        ) {
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
        }
      }
      if (EmployeeTeamEmailTemp.length == 0) {
        this.listEmailAnalyser = [];
        this.resultFlag = false;
      } else {
        this.listEmailAnalyser = EmployeeTeamEmailTemp;
        this.clearFlags();
      }
      this.escalationFlag = false;
    } else {
      this.listEmailAnalyser = this.listEmailAnalyserMain;
      this.escalationFlag = true;
    }
  }
  toggleAdverse() {
    let EmployeeTeamEmailTemp: Array<Object> = [];
    console.log(this.adverseFlag);
    if (this.adverseFlag == true) {
      for (var i = 0; i < this.listEmailAnalyser.length; i++) {
        if (
          this.listEmailAnalyser[i]["adverse"] != null &&
          this.listEmailAnalyser[i]["adverse"] == "yes"
        ) {
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
        }
      }
      if (EmployeeTeamEmailTemp.length == 0) {
        this.listEmailAnalyser = [];
        this.resultFlag = false;
      } else {
        this.listEmailAnalyser = EmployeeTeamEmailTemp;
        this.clearFlags();
      }
      this.adverseFlag = false;
    } else {
      this.listEmailAnalyser = this.listEmailAnalyserMain;
      this.adverseFlag = true;
    }
  }

  // comman search filter
  searchbyallsentreceive(obj) {
    this.clearFlags();
    var count = 0;
    var searchObject = new BaseContainer();
    searchObject["employeeId"] = this.employeeId;
    searchObject["identify"] = obj;
    searchObject["pageNumber"] = count;
    this.httpService
      .postRequest<TeamScore>(
        searchObject,
        "app/auth/displayemployeeteamdashboard"
      )
      .subscribe(employeepersonalscore => {
        if (employeepersonalscore.rsBody.result == "success") {
          this.employeeTeamScore = employeepersonalscore.rsBody.msg;
          this.teamScore = this.employeeTeamScore.toneOfTeamMail;
          //   this.toneOfPersonalMail = this.employeeTeamScore.toneOfPersonalMail;
          this.listEmailAnalyser = this.employeeTeamScore.listEmailAnalyser;
          this.listOfEmployee = this.employeeTeamScore.listOfEmployee;
          for (var i = 0; i < this.listEmailAnalyser.length; i++) {
            if (
              this.listEmailAnalyser[i]["anger"] != 0 &&
              this.listEmailAnalyser[i]["anger"] != null
            ) {
              this.listEmailAnalyser[i]["anger"] = "anger";
            }
            if (
              this.listEmailAnalyser[i]["joy"] != 0 &&
              this.listEmailAnalyser[i]["joy"] != null
            ) {
              this.listEmailAnalyser[i]["joy"] = "joy";
            }
            if (
              this.listEmailAnalyser[i]["sadness"] != 0 &&
              this.listEmailAnalyser[i]["sadness"] != null
            ) {
              this.listEmailAnalyser[i]["sadness"] = "sadness";
            }
            if (
              this.listEmailAnalyser[i]["fear"] != 0 &&
              this.listEmailAnalyser[i]["fear"] != null
            ) {
              this.listEmailAnalyser[i]["fear"] = "fear";
            }
            if (
              this.listEmailAnalyser[i]["tentative"] != 0 &&
              this.listEmailAnalyser[i]["tentative"] != null
            ) {
              this.listEmailAnalyser[i]["tentative"] = "tentative";
            }
            if (
              this.listEmailAnalyser[i]["confident"] != 0 &&
              this.listEmailAnalyser[i]["confident"] != null
            ) {
              this.listEmailAnalyser[i]["confident"] = "confident";
            }
            if (
              this.listEmailAnalyser[i]["analytical"] != 0 &&
              this.listEmailAnalyser[i]["analytical"] != null
            ) {
              this.listEmailAnalyser[i]["analytical"] = "analytical";
            }
            if (
              this.listEmailAnalyser[i]["escalated"] == null ||
              this.listEmailAnalyser[i]["escalated"] != "yes"
            ) {
              this.listEmailAnalyser[i]["escalated"] = "no";
            }

            if (
              this.listEmailAnalyser[i]["adverse"] == null ||
              this.listEmailAnalyser[i]["adverse"] != "yes"
            ) {
              this.listEmailAnalyser[i]["adverse"] = "no";
            }
          }
        }
      });
  }

  // onchange data
  onChange(value: number) {
    this.serObj = value;
  }

  // search filter by email subject
  searchbyemailsubject() {
    this.clearFlags();
    var searchObj = new BaseContainer();
    searchObj["emailSubject"] = this.serObj["subject"];
    searchObj["employeeId"] = this.employeeId;
    this.httpService
      .postRequest<TeamScore>(searchObj, "app/auth/resultByMailSubject")
      .subscribe(teamScore => {
        if (teamScore.rsBody.result == "success") {
          this.listEmailAnalyser = teamScore.rsBody.msg;
          for (var i = 0; i < this.listEmailAnalyser.length; i++) {
            if (
              this.listEmailAnalyser[i]["anger"] != 0 &&
              this.listEmailAnalyser[i]["anger"] != null
            ) {
              this.listEmailAnalyser[i]["anger"] = "Anger";
            }
            if (
              this.listEmailAnalyser[i]["joy"] != 0 &&
              this.listEmailAnalyser[i]["joy"] != null
            ) {
              this.listEmailAnalyser[i]["joy"] = "Joy";
            }
            if (
              this.listEmailAnalyser[i]["sadness"] != 0 &&
              this.listEmailAnalyser[i]["sadness"] != null
            ) {
              this.listEmailAnalyser[i]["sadness"] = "sadness";
            }
            if (
              this.listEmailAnalyser[i]["fear"] != 0 &&
              this.listEmailAnalyser[i]["fear"] != null
            ) {
              this.listEmailAnalyser[i]["fear"] = "fear";
            }
            if (
              this.listEmailAnalyser[i]["tentative"] != 0 &&
              this.listEmailAnalyser[i]["tentative"] != null
            ) {
              this.listEmailAnalyser[i]["tentative"] = "tentative";
            }
            if (
              this.listEmailAnalyser[i]["confident"] != 0 &&
              this.listEmailAnalyser[i]["confident"] != null
            ) {
              this.listEmailAnalyser[i]["confident"] = "confident";
            }
            if (
              this.listEmailAnalyser[i]["analytical"] != 0 &&
              this.listEmailAnalyser[i]["analytical"] != null
            ) {
              this.listEmailAnalyser[i]["analytical"] = "analytical";
            }
            if (
              this.listEmailAnalyser[i]["escalated"] == null ||
              this.listEmailAnalyser[i]["escalated"] != "yes"
            ) {
              this.listEmailAnalyser[i]["escalated"] = "no";
            }

            if (
              this.listEmailAnalyser[i]["adverse"] == null ||
              this.listEmailAnalyser[i]["adverse"] != "yes"
            ) {
              this.listEmailAnalyser[i]["adverse"] = "no";
            }
          }
        }
      });
  }

  backtoemployee() {
    //var empId = this.employeePersonalReport.employeeId;
    // this.sharedService.setData({'tabName':'employee','userId':this.userId});
    // this.router.navigate(['my-employee-personal-score', this.userId]);
    window.history.back();
  }
  // back button
  backsubmit() {
    //var backObj = new BaseContainer();
    this.levelup.splice(-1, 1);
    var val = this.levelup[this.levelup.length - 1];
    var len = this.levelup.length;
    //backObj['employeeId'] = val;
    if (len == 0 || len == 1) {
      this.ngAfterViewInit();
    } else {
      this.getClickEmployeeDetails(val);
    }
  }

  nameOrTeamSerachSubmit() {
    if (this.nameteamFlag == true) {
      this.nameteamFlag = false;
    } else {
      this.nameteamFlag = true;
    }
  }

  emailSubjectSearch() {
    if (this.emailSubjectFlag == true) {
      this.emailSubjectFlag = false;
    } else {
      this.emailSubjectFlag = true;
    }
  }

  employeeSerachSubmit() {
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

  selectScoreAll() {
    if (this.selectAllflag == true) {
      this.selectAllflag = false;
      this.teamScoreListDotted.forEach(item => {
        this.serachteamtonedotted.push(item.option);
        item.checked = true;
      });
    } else {
      this.selectAllflag = true;

      this.teamScoreListDotted.forEach(item => {
        item.checked = false;
      });

      this.serachteamtonedotted = [];
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
  resetScoreAll() {
    this.serachteamtone = [];
    this.teamScoreList.forEach(item => {
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
  // filter on email major
  searchbyateamtone(obj, targetIndex) {
    if (this.serachteamtone.indexOf(obj) == -1) {
      this.teamScoreList[targetIndex].checked = true;
      this.serachteamtone.push(obj);
    } else {
      this.teamScoreList[targetIndex].checked = false;
      for (var i = 0; i < this.serachteamtone.length; i++) {
        var data = this.serachteamtone[i];

        if (obj == data) {
          this.serachteamtone.splice(i, 1);
        }
      }
    }

    this.applyTeamfilterNormal(this.serachteamtone);
  }

  // over all filter
  allOverFilter(obj) {
    this.applyTeamfilterNormal([obj]);
  }
  applyTeamfilterNormal(obj) {
    this.clearFlags();
    var objResult = new BaseContainer();
    objResult["searchCriteria"] = obj;
    objResult["employeeId"] = this.employeeTeamScore.employeeId;
    //this.teamtonedotedflag = true;

    this.httpService
      .postRequest<EmployeePersonalScore>(
        objResult,
        "app/auth/sortMailByTeamTone"
      )
      .subscribe(employeepersonalscore => {
        if (employeepersonalscore.rsBody.result == "success") {
          this.employeeReports = employeepersonalscore.rsBody.msg;
          if (this.employeeReports != "success") {
            this.listEmailAnalyser = this.employeeReports;
            //this.toClientNames = this.listEmailAnalyser.toClientNames;
            // this.arraySize = this.listEmailAnalyser.toClientNames[0];

            if (this.listEmailAnalyser.length > 0) {
              for (var i = 0; i < this.listEmailAnalyser.length; i++) {
                if (
                  this.listEmailAnalyser[i]["anger"] != 0 &&
                  this.listEmailAnalyser[i]["anger"] != null
                ) {
                  this.listEmailAnalyser[i]["anger"] = "Anger";
                }
                if (
                  this.listEmailAnalyser[i]["joy"] != 0 &&
                  this.listEmailAnalyser[i]["joy"] != null
                ) {
                  this.listEmailAnalyser[i]["joy"] = "Joy";
                }
                if (
                  this.listEmailAnalyser[i]["sadness"] != 0 &&
                  this.listEmailAnalyser[i]["sadness"] != null
                ) {
                  this.listEmailAnalyser[i]["sadness"] = "sadness";
                }
                if (
                  this.listEmailAnalyser[i]["fear"] != 0 &&
                  this.listEmailAnalyser[i]["fear"] != null
                ) {
                  this.listEmailAnalyser[i]["fear"] = "fear";
                }
                if (
                  this.listEmailAnalyser[i]["tentative"] != 0 &&
                  this.listEmailAnalyser[i]["tentative"] != null
                ) {
                  this.listEmailAnalyser[i]["tentative"] = "tentative";
                }
                if (
                  this.listEmailAnalyser[i]["confident"] != 0 &&
                  this.listEmailAnalyser[i]["confident"] != null
                ) {
                  this.listEmailAnalyser[i]["confident"] = "confident";
                }
                if (
                  this.listEmailAnalyser[i]["analytical"] != 0 &&
                  this.listEmailAnalyser[i]["analytical"] != null
                ) {
                  this.listEmailAnalyser[i]["analytical"] = "analytical";
                }
                if (
                  this.listEmailAnalyser[i]["escalated"] == null ||
                  this.listEmailAnalyser[i]["escalated"] != "yes"
                ) {
                  this.listEmailAnalyser[i]["escalated"] = "no";
                }

                if (
                  this.listEmailAnalyser[i]["adverse"] == null ||
                  this.listEmailAnalyser[i]["adverse"] != "yes"
                ) {
                  this.listEmailAnalyser[i]["adverse"] = "no";
                }
              }
            }
          }
        }
      });
  }

  searchbyateamtonedotted(obj, targetIndex) {
    this.clearFlags();
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
  searchSubject(obj) {
    console.log("in subject team email ");
    this.clearFlags();
    let EmployeeTeamEmailTemp: Array<Object> = [];
    let EmployeeTeamEmailTempMain: Array<Object> = [];
    for (var i = 0; i < this.listEmailAnalyser.length; i++) {
      if (this.listEmailAnalyser[i].subject.length != 0) {
        // let tempClient: Array<String> = this.EmployeeTeamEmailMain[i].toClientNames;

        if (
          this.listEmailAnalyser[i].subject
            .toString()
            .toLowerCase()
            .indexOf(obj.subject.toString().toLowerCase()) > -1
        ) {
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);

          console.log(this.listEmailAnalyser);
        }
      }
    }
    if (EmployeeTeamEmailTemp.length == 0) {
      this.listEmailAnalyser = [];
      this.resultFlag = false;
    } else {
      this.listEmailAnalyser = EmployeeTeamEmailTemp;
    }
    this.emailSubjectFlag = true;
  }

  searchClient(obj) {
    console.log("in search client");
    this.clearFlags();
    let EmployeeTeamEmailTemp: Array<Object> = [];
    let EmployeeTeamEmailTempMain: Array<Object> = [];
    for (var i = 0; i < this.listEmailAnalyser.length; i++) {
      if (this.listEmailAnalyser[i].toClientNames.length != 0) {
        let tempClient: Array<String> = this.listEmailAnalyser[i].toClientNames;
        for (var j = 0; j < tempClient.length; j++) {
          if (
            tempClient[j]
              .toString()
              .toLowerCase()
              .indexOf(obj.clientName.toString().toLowerCase()) > -1
          ) {
            EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);

            console.log(this.listEmailAnalyser);
          }
        }
      }
    }
    if (EmployeeTeamEmailTemp.length == 0) {
      this.listEmailAnalyser = [];
      this.resultFlag = false;
    } else {
      this.listEmailAnalyser = EmployeeTeamEmailTemp;
    }
    this.clientFlag = true;
  }

  clearAll() {
    this.listEmailAnalyser = this.listEmailAnalyserMain;
    this.clearFlags();
  }

  selectedToneCircle(num) {
    this.listEmailAnalyser = this.listEmailAnalyserMain;
    this.teamScoreListDotted[num].checked = true;
    for (var i = 0; i < this.teamScoreListDotted.length; i++) {
      if (i != num) {
        this.teamScoreListDotted[i].checked = false;
      }
    }
    this.applyTeamfilterDotted();
  }
  applyTeamScoreColumnList() {
    this.clearFlags();
  }
  applyTeamfilterDotted() {
    // var objResult = new BaseContainer();
    // objResult['searchCriteria'] = this.serachteamtonedotted;
    // objResult['employeeId'] = this.employeeTeamScore.employeeId;
    // this.serachteamtonedotted = [];
    // this.teamtoneflag = true;
    // this.teamScoreListDotted.forEach((item) => {
    //    this.serachteamtonedotted.push(item.option);
    //    item.checked = false;
    //  })

    // this.selectAllflag = true;

    // this.httpService.postRequest<EmployeePersonalScore>(objResult,'app/auth/getMailFilterByTeamTone').subscribe(
    //      employeepersonalscore=> {
    //          if(employeepersonalscore.rsBody.result=='success')
    //          {
    //              this.employeeReports = employeepersonalscore.rsBody.msg;
    //              if(this.employeeReports != "success")
    //              {

    //                  this.listEmailAnalyser =  this.employeeReports;
    //                  //this.toClientNames = this.listEmailAnalyser.toClientNames;
    //                  // this.arraySize = this.listEmailAnalyser.toClientNames[0];

    //                  if(this.listEmailAnalyser.length > 0)
    //                  {
    //                      for(var i=0; i< this.listEmailAnalyser.length; i++)
    //                      {
    //                          if(this.listEmailAnalyser[i]['anger'] !=0 && this.listEmailAnalyser[i]['anger'] !=null){
    //                              this.listEmailAnalyser[i]['anger'] = "Anger";
    //                          }
    //                          if(this.listEmailAnalyser[i]['joy'] !=0 && this.listEmailAnalyser[i]['joy'] !=null){
    //                              this.listEmailAnalyser[i]['joy'] = "Joy";
    //                          }
    //                          if(this.listEmailAnalyser[i]['sadness'] !=0 && this.listEmailAnalyser[i]['sadness'] !=null){
    //                              this.listEmailAnalyser[i]['sadness'] = "sadness";
    //                          }
    //                          if(this.listEmailAnalyser[i]['fear'] !=0 && this.listEmailAnalyser[i]['fear'] !=null){
    //                              this.listEmailAnalyser[i]['fear'] = "fear";
    //                          }
    //                          if(this.listEmailAnalyser[i]['tentative'] !=0 && this.listEmailAnalyser[i]['tentative'] !=null){
    //                              this.listEmailAnalyser[i]['tentative'] = "tentative";
    //                          }
    //                          if(this.listEmailAnalyser[i]['confident'] !=0 && this.listEmailAnalyser[i]['confident'] !=null){
    //                              this.listEmailAnalyser[i]['confident'] = "confident";
    //                          }
    //                          if(this.listEmailAnalyser[i]['analytical'] !=0 && this.listEmailAnalyser[i]['analytical'] !=null){
    //                              this.listEmailAnalyser[i]['analytical'] = "analytical";
    //                          }
    //                      }

    //                  }

    //               }

    //          }
    //      });
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
    for (var i = 0; i < this.listEmailAnalyser.length; i++) {
      countTone = 0;
      // console.log(this.EmployeeTeamEmail[i]['joy']);
      // console.log(this.EmployeeTeamEmail[i].joy);
      if (this.listEmailAnalyser[i].anger != 0) {
        if (tempTone.indexOf("anger") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
          console.log("detected anger");
          continue;
        }
      }
      if (this.listEmailAnalyser[i].joy != 0) {
        if (tempTone.indexOf("joy") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
          console.log("detected joy");
          continue;
        }
      }
      if (this.listEmailAnalyser[i].sadness != 0) {
        if (tempTone.indexOf("sadness") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
          console.log("detected sadness");
          continue;
        }
      }
      if (this.listEmailAnalyser[i].fear != 0) {
        if (tempTone.indexOf("fear") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
          console.log("detected fear");
          continue;
        }
      }
      if (this.listEmailAnalyser[i].tentative != 0) {
        if (tempTone.indexOf("tentative") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
          console.log("detected tentative");
          continue;
        }
      }
      if (this.listEmailAnalyser[i].confident != 0) {
        if (tempTone.indexOf("confident") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
          console.log("detected confident");
          continue;
        }
      }
      if (this.listEmailAnalyser[i].analytical != 0) {
        if (tempTone.indexOf("analytical") > -1) {
          // countTone++;
          EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
          console.log("detected analytical");
          continue;
        }
      }

      // if(tempTone.length ==  countTone){
      //     EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
      // }
    }

    if (EmployeeTeamEmailTemp.length == 0) {
      this.listEmailAnalyser = [];
      this.resultFlag = false;
    } else {
      this.listEmailAnalyser = EmployeeTeamEmailTemp;
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
      this.listEmailAnalyser = this.listEmailAnalyserMain;
      this.adverseEventFlag = true;
      this.resultFlag = true;
    }
  }

  excelExportSubmit() {
    var resObj = new BaseContainer();
    resObj = this.temp;
    this.httpService
      .postRequest<EmployeeDashboard>(resObj, "app/auth/listTeamToExcel")
      .subscribe(employeedashboard => {
        if (employeedashboard.rsBody.result == "success") {
          var exeFile = employeedashboard.rsBody.msg;
          $("#ExportExcelData").attr("href", exeFile);
          $("#ExportExcelData")[0].click();
        }
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
    //    var resObj = new BaseContainer();
    //    resObj['startDate'] = $('.datepickerform').datepicker().val();
    //    resObj['endDate'] = $('.datepickerto').datepicker().val();
    //    resObj['emailId'] = this.employeeTeamScore.emailId;
    //    this.httpService.postRequest<EmployeeDashboard>(resObj,'app/auth/getTeamMailFilterByDate').subscribe(
    //         employeedashboard=> {
    //               if(employeedashboard.rsBody.result=='success')
    //              {
    //                  this.listEmailAnalyser = employeedashboard.rsBody.msg;
    //                  if(this.listEmailAnalyser != "success")
    //                  {

    //                      this.listEmailAnalyser =  employeedashboard.rsBody.msg;
    //                      //this.toClientNames = this.listEmailAnalyser.toClientNames;
    //                      // this.arraySize = this.listEmailAnalyser.toClientNames[0];

    //                      if(this.listEmailAnalyser.length > 0)
    //                      {
    //                          for(var i=0; i< this.listEmailAnalyser.length; i++)
    //                          {
    //                              if(this.listEmailAnalyser[i]['anger'] !=0 && this.listEmailAnalyser[i]['anger'] !=null){
    //                                  this.listEmailAnalyser[i]['anger'] = "Anger";
    //                              }
    //                              if(this.listEmailAnalyser[i]['joy'] !=0 && this.listEmailAnalyser[i]['joy'] !=null){
    //                                  this.listEmailAnalyser[i]['joy'] = "Joy";
    //                              }
    //                              if(this.listEmailAnalyser[i]['sadness'] !=0 && this.listEmailAnalyser[i]['sadness'] !=null){
    //                                  this.listEmailAnalyser[i]['sadness'] = "sadness";
    //                              }
    //                              if(this.listEmailAnalyser[i]['fear'] !=0 && this.listEmailAnalyser[i]['fear'] !=null){
    //                                  this.listEmailAnalyser[i]['fear'] = "fear";
    //                              }
    //                              if(this.listEmailAnalyser[i]['tentative'] !=0 && this.listEmailAnalyser[i]['tentative'] !=null){
    //                                  this.listEmailAnalyser[i]['tentative'] = "tentative";
    //                              }
    //                              if(this.listEmailAnalyser[i]['confident'] !=0 && this.listEmailAnalyser[i]['confident'] !=null){
    //                                  this.listEmailAnalyser[i]['confident'] = "confident";
    //                              }
    //                              if(this.listEmailAnalyser[i]['analytical'] !=0 && this.listEmailAnalyser[i]['analytical'] !=null){
    //                                  this.listEmailAnalyser[i]['analytical'] = "analytical";
    //                              }
    //                          }

    //                      }

    //                   }

    //              }
    //     });
    this.clearFlags();
    let EmployeeTeamEmailTemp: Array<Object> = [];
    var toneDateFrom: Date = new Date(this.orgObj.fromDate);
    var toneDateTo: Date = new Date(this.orgObj.upto);
    for (var i = 0; i < this.listEmailAnalyser.length; i++) {
      var tempDate: Date = new Date(this.listEmailAnalyser[i].date);
      if (tempDate >= toneDateFrom && tempDate <= toneDateTo) {
        EmployeeTeamEmailTemp.push(this.listEmailAnalyser[i]);
      }
    }

    if (EmployeeTeamEmailTemp.length == 0) {
      this.listEmailAnalyser = [];
      this.resultFlag = false;
    } else {
      this.listEmailAnalyser = EmployeeTeamEmailTemp;
    }
    this.searchbyDateflag = true;
  }

  // timer
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
  // serah by time
  searchByStartTimeEndTime() {
    var resObj = new BaseContainer();
    var starttime = $("#startTime")
      .clockpicker()
      .val();
    var endtime = $("#endTime")
      .clockpicker()
      .val();
    resObj["startTime"] = starttime + ":00";
    resObj["endTime"] = endtime + ":00";
    resObj["emailId"] = this.employeeTeamScore.emailId;
    this.searchbyTimeflag = true;
    this.httpService
      .postRequest<EmployeeDashboard>(
        resObj,
        "app/auth/getTeamMailFilterByTime"
      )
      .subscribe(employeedashboard => {
        if (employeedashboard.rsBody.result == "success") {
          this.listEmailAnalyser = employeedashboard.rsBody.msg;
          if (this.listEmailAnalyser != "success") {
            this.listEmailAnalyser = employeedashboard.rsBody.msg;
            //this.toClientNames = this.listEmailAnalyser.toClientNames;
            // this.arraySize = this.listEmailAnalyser.toClientNames[0];

            if (this.listEmailAnalyser.length > 0) {
              for (var i = 0; i < this.listEmailAnalyser.length; i++) {
                if (
                  this.listEmailAnalyser[i]["anger"] != 0 &&
                  this.listEmailAnalyser[i]["anger"] != null
                ) {
                  this.listEmailAnalyser[i]["anger"] = "Anger";
                }
                if (
                  this.listEmailAnalyser[i]["joy"] != 0 &&
                  this.listEmailAnalyser[i]["joy"] != null
                ) {
                  this.listEmailAnalyser[i]["joy"] = "Joy";
                }
                if (
                  this.listEmailAnalyser[i]["sadness"] != 0 &&
                  this.listEmailAnalyser[i]["sadness"] != null
                ) {
                  this.listEmailAnalyser[i]["sadness"] = "sadness";
                }
                if (
                  this.listEmailAnalyser[i]["fear"] != 0 &&
                  this.listEmailAnalyser[i]["fear"] != null
                ) {
                  this.listEmailAnalyser[i]["fear"] = "fear";
                }
                if (
                  this.listEmailAnalyser[i]["tentative"] != 0 &&
                  this.listEmailAnalyser[i]["tentative"] != null
                ) {
                  this.listEmailAnalyser[i]["tentative"] = "tentative";
                }
                if (
                  this.listEmailAnalyser[i]["confident"] != 0 &&
                  this.listEmailAnalyser[i]["confident"] != null
                ) {
                  this.listEmailAnalyser[i]["confident"] = "confident";
                }
                if (
                  this.listEmailAnalyser[i]["analytical"] != 0 &&
                  this.listEmailAnalyser[i]["analytical"] != null
                ) {
                  this.listEmailAnalyser[i]["analytical"] = "analytical";
                }
              }
            }
          }
        }
      });
  }

  // serach by emaployee name
  searchByEmployeeName() {
    var resObj = new BaseContainer();
    resObj["employeeName"] = this.searchByName.employeeName;
    resObj["employeeId"] = this.employeeTeamScore.employeeId;
    this.employeeFlag = true;
    this.httpService
      .postRequest<EmployeeDashboard>(
        resObj,
        "app/auth/getTeamMailFilterByEmpName"
      )
      .subscribe(employeedashboard => {
        if (employeedashboard.rsBody.result == "success") {
          this.listEmailAnalyser = employeedashboard.rsBody.msg;
          if (this.listEmailAnalyser != "success") {
            this.listEmailAnalyser = employeedashboard.rsBody.msg;
            //this.toClientNames = this.listEmailAnalyser.toClientNames;
            // this.arraySize = this.listEmailAnalyser.toClientNames[0];

            if (this.listEmailAnalyser.length > 0) {
              for (var i = 0; i < this.listEmailAnalyser.length; i++) {
                if (
                  this.listEmailAnalyser[i]["anger"] != 0 &&
                  this.listEmailAnalyser[i]["anger"] != null
                ) {
                  this.listEmailAnalyser[i]["anger"] = "Anger";
                }
                if (
                  this.listEmailAnalyser[i]["joy"] != 0 &&
                  this.listEmailAnalyser[i]["joy"] != null
                ) {
                  this.listEmailAnalyser[i]["joy"] = "Joy";
                }
                if (
                  this.listEmailAnalyser[i]["sadness"] != 0 &&
                  this.listEmailAnalyser[i]["sadness"] != null
                ) {
                  this.listEmailAnalyser[i]["sadness"] = "sadness";
                }
                if (
                  this.listEmailAnalyser[i]["fear"] != 0 &&
                  this.listEmailAnalyser[i]["fear"] != null
                ) {
                  this.listEmailAnalyser[i]["fear"] = "fear";
                }
                if (
                  this.listEmailAnalyser[i]["tentative"] != 0 &&
                  this.listEmailAnalyser[i]["tentative"] != null
                ) {
                  this.listEmailAnalyser[i]["tentative"] = "tentative";
                }
                if (
                  this.listEmailAnalyser[i]["confident"] != 0 &&
                  this.listEmailAnalyser[i]["confident"] != null
                ) {
                  this.listEmailAnalyser[i]["confident"] = "confident";
                }
                if (
                  this.listEmailAnalyser[i]["analytical"] != 0 &&
                  this.listEmailAnalyser[i]["analytical"] != null
                ) {
                  this.listEmailAnalyser[i]["analytical"] = "analytical";
                }
              }
            }
          }
        }
      });
  }
  // reload
  reload() {
    this.ngAfterViewInit();
  }
}
